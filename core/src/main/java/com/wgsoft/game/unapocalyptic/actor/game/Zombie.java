package com.wgsoft.game.unapocalyptic.actor.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.wgsoft.game.unapocalyptic.AudioManager;
import com.wgsoft.game.unapocalyptic.screen.GameScreen;

public final class Zombie extends Actor {
    private final Player player;

    private final TextureRegion region;

    private Action activeAction;
    private boolean active;
    private boolean walking;

    public Zombie(final Player player, final Skin skin) {
        this.player = player;
        region = new TextureRegion(skin.getRegion("zombie"));

        setSize(120f, 120f);
        setY(0f, Align.top);

        addAction(Actions.run(() -> {
            setX(MathUtils.random(getStage().getWidth() - getWidth()));
        }));
        activeAction = getActiveAction();
        addAction(activeAction);
    }

    private Action getActiveAction() {
        return Actions.sequence(
            Actions.run(() -> {
                region.flip(MathUtils.randomBoolean(), false);
                active = true;
            }),
            Actions.moveBy(
                0f,
                GameScreen.GROUND_HEIGHT + getHeight(),
                10f
            ),
            Actions.run(() -> {
                walking = true;
            }),
            Actions.forever(Actions.run(() -> {
                final float direction;

                if(player.getX() < getX()) {
                    region.flip(!region.isFlipX(), false);
                    direction = -1f;
                } else {
                    region.flip(region.isFlipX(), false);
                    direction = 1f;
                }

                moveBy(direction * 120f * Gdx.graphics.getDeltaTime(), 0f);
            }))
        );
    }

    public void suffer(final boolean left) {
        if(active) {
            active = false;
            walking = false;

            removeAction(activeAction);
            activeAction = getActiveAction();
            addAction(Actions.sequence(
                Actions.moveToAligned(
                    getX()
                        + (left ? -1f : 1f)
                        * MathUtils.random(GameScreen.GROUND_HEIGHT),
                    0f,
                    Align.topLeft,
                    0.5f
                ),
                Actions.addAction(activeAction)
            ));
        }
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        batch.draw(region, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void act(final float delta) {
        if(walking
            && player.getStage() != null
            && Rectangle.tmp.set(getX(), getY(), getWidth(), getHeight())
                .overlaps(Rectangle.tmp2.set(
                    player.getX(),
                    player.getY(),
                    player.getWidth(),
                    player.getHeight()
                ))
        ) {
            AudioManager.playEatenSound();
            player.die();
        }

        super.act(delta);
    }
}
