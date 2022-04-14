package com.wgsoft.game.unapocalyptic.actor.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pools;
import com.wgsoft.game.unapocalyptic.AudioManager;
import com.wgsoft.game.unapocalyptic.actor.SmashParticleEffectActor;
import com.wgsoft.game.unapocalyptic.screen.GameScreen;

public final class Hammer extends Actor {
    private static final float ORIGIN_LEFT = 60f;
    private static final float ORIGIN_RIGHT = 180f;
    private static final float SMASH_OFFSET = 30f;

    private final TextureRegion region;

    private boolean smashing;

    public Hammer(final Skin skin) {
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

    public boolean smash() {
        if(!smashing) {
            smashing = true;

            addAction(Actions.sequence(
                Actions.rotateTo(0f),
                Actions.rotateBy(200f, 0.125f, Interpolation.slowFast),
                Actions.run(() -> {
                    AudioManager.playSmashSound();

                    final SmashParticleEffectActor smashParticleEffectActor =
                        Pools.obtain(SmashParticleEffectActor.class);
                    smashParticleEffectActor.setPosition(
                        getX() + getParent().getX() + 2 * getOriginX() + (
                            region.isFlipX()
                                ? -getWidth() + SMASH_OFFSET
                                : -SMASH_OFFSET
                        ),
                        GameScreen.GROUND_HEIGHT
                    );
                    smashParticleEffectActor.start();
                    getParent().getParent().addActor(smashParticleEffectActor);

                    Rectangle.tmp.set(
                        getParent().getX(Align.center)
                            - (region.isFlipX() ? ORIGIN_RIGHT : ORIGIN_LEFT),
                        GameScreen.GROUND_HEIGHT,
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

            return true;
        }

        return false;
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
        super.draw(batch, parentAlpha);
    }
}
