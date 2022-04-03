package com.wgsoft.game.unapocalyptic.actor.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.wgsoft.game.unapocalyptic.Unapocalyptic;

public final class Hammer extends Actor {
    private static final float ORIGIN_LEFT = 60f;
    private static final float ORIGIN_RIGHT = 180f;

    private final Unapocalyptic game;

    private final TextureRegion region;

    private boolean smashing;

    public Hammer(final Unapocalyptic game, final Skin skin) {
        this.game = game;
        region = skin.getRegion("hammer");

        setSize(240f, 120f);
        setOriginY(60f);

        addAction(Actions.run(() -> {
            setFlip(false);
        }));
    }

    public void setFlip(final boolean flip) {
        region.flip(region.isFlipX() != flip, false);

        if(flip) {
            setOriginX(ORIGIN_LEFT);
        } else {
            setOriginX(ORIGIN_RIGHT);
        }

        setX(getParent().getWidth() / 2f - getOriginX());
    }

    public void smash() {
        if(!smashing) {
            smashing = true;

            addAction(Actions.sequence(
                Actions.rotateTo(0f),
                Actions.rotateBy(180f, 0.125f, Interpolation.slowFast),
                Actions.run(() -> {
                    game.playSmashSound();

                    Rectangle.tmp.set(
                        getParent().getX(Align.center)
                            - (region.isFlipX() ? ORIGIN_RIGHT : ORIGIN_LEFT),
                        getParent().getY() + getY(),
                        getWidth(),
                        getHeight()
                    );

                    for(final Actor actor:
                        getParent().getParent().getChildren()
                    ) {
                        if(actor instanceof Zombie
                            && Rectangle.tmp.overlaps(Rectangle.tmp2.set(
                                actor.getX(),
                                actor.getY(),
                                actor.getWidth(),
                                actor.getHeight()
                            ))
                        ) {
                                ((Zombie)actor).suffer(region.isFlipX());
                        }
                    }
                }),
                Actions.rotateTo(0f, 0.5f, Interpolation.fade),
                Actions.run(() -> {
                    smashing = false;
                })
            ));
        }
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        batch.draw(
            region,
            getX(),
            getY(),
            getOriginX(),
            getOriginY(),
            getWidth(),
            getHeight(),
            getScaleX(),
            getScaleY(),
            region.isFlipX() ? getRotation() : -getRotation()
        );
    }
}
