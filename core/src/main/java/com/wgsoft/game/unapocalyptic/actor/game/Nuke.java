package com.wgsoft.game.unapocalyptic.actor.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pools;
import com.wgsoft.game.unapocalyptic.AudioManager;
import com.wgsoft.game.unapocalyptic.actor.ExplosionParticleEffectActor;
import com.wgsoft.game.unapocalyptic.screen.GameScreen;

public final class Nuke extends Actor {
    private final Player player;

    private final TextureRegion region;

    private final float align;
    private float progress;

    public Nuke(final Player player, final Skin skin) {
        this.player = player;
        region = skin.getRegion("nuke");

        setSize(240f, 240f);
        align = MathUtils.random();
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        batch.draw(region, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void act(final float delta) {
        progress += 30f * delta;
        setPosition(
            align * (getStage().getWidth() - getWidth()),
            getStage().getHeight() - progress
        );

        Rectangle.tmp.set(getX(), getY(), getWidth(), getHeight());

        loop: {
            for(final Actor actor: getParent().getChildren()) {
                if(actor instanceof Bullet
                    && Rectangle.tmp.overlaps(Rectangle.tmp2.set(
                        actor.getX(),
                        actor.getY(),
                        actor.getWidth(),
                        actor.getHeight()))
                ) {
                    final ExplosionParticleEffectActor particleEffectActor =
                        Pools.obtain(ExplosionParticleEffectActor.class);
                    particleEffectActor.setPosition(
                        getX(Align.center),
                        getY(Align.center),
                        Align.center
                    );
                    particleEffectActor.start();
                    getParent().addActor(particleEffectActor);

                    AudioManager.playHitSound();
                    remove();
                    actor.remove();
                    break loop;
                }
            }

            if(getY() < GameScreen.GROUND_HEIGHT) {
                final ExplosionParticleEffectActor particleEffectActor =
                    Pools.obtain(ExplosionParticleEffectActor.class);
                particleEffectActor.setPosition(
                    getX(Align.center),
                    getY(),
                    Align.center
                );
                particleEffectActor.start();
                getParent().addActor(particleEffectActor);

                AudioManager.playExplosionSound();
                remove();
                player.die();
            }
        }

        super.act(delta);
    }
}
