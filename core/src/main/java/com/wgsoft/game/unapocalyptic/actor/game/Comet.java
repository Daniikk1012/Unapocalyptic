package com.wgsoft.game.unapocalyptic.actor.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.wgsoft.game.unapocalyptic.AudioManager;
import com.wgsoft.game.unapocalyptic.screen.GameScreen;

public final class Comet extends Actor {
    private final float SPEED_MAX = 240f;

    private final Player player;

    private final Animation<TextureRegion> animation;
    private float animationTime;

    private float speed;
    private float progress;

    public Comet(final Player player, final Skin skin) {
        this.player = player;
        animation = new Animation<>(
            0.5f,
            skin.getRegions("comet"),
            Animation.PlayMode.LOOP
        );

        setSize(480f, 480f);
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        batch.draw(
            animation.getKeyFrame(animationTime),
            getX(),
            getY(),
            getWidth(),
            getHeight()
        );
    }

    @Override
    public void act(final float delta) {
        animationTime += delta;

        speed += 1f * delta;

        if(speed > SPEED_MAX) {
            speed = SPEED_MAX;
        }

        progress += speed * delta;
        setPosition(
            getStage().getWidth() / 2f,
            getStage().getHeight() - progress,
            Align.bottom | Align.center
        );

        Rectangle.tmp.set(getX(), getY(), getWidth(), getHeight());

        for(final Actor actor: getParent().getChildren()) {
            if(actor instanceof Bullet
                && Rectangle.tmp.overlaps(Rectangle.tmp2.set(
                    actor.getX(),
                    actor.getY(),
                    actor.getWidth(),
                    actor.getHeight()))
            ) {
                AudioManager.playHitSound();

                progress -= 30f;

                if(progress < 0f) {
                    progress = 0f;
                }

                actor.remove();
            }
        }

        if(getY() < GameScreen.GROUND_HEIGHT) {
            AudioManager.playExplosionSound();
            remove();
            player.die();
        }

        super.act(delta);
    }
}
