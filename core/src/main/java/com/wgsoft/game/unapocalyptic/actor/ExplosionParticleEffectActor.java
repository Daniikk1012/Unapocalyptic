package com.wgsoft.game.unapocalyptic.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.ui.ParticleEffectActor;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.wgsoft.game.unapocalyptic.Unapocalyptic;

public final class ExplosionParticleEffectActor extends ParticleEffectActor {
    private static ParticleEffect instance;

    static {
        Pools.set(
            ExplosionParticleEffectActor.class,
            new Pool<ExplosionParticleEffectActor>() {
                @Override
                protected ExplosionParticleEffectActor newObject() {
                    return new ExplosionParticleEffectActor(
                        new ParticleEffect(instance)
                    );
                }
            }
        );
    }

    public ExplosionParticleEffectActor(final ParticleEffect particleEffect) {
        super(particleEffect, true);
        setAutoRemove(true);
    }

    public static void initialize(final Unapocalyptic game) {
        instance = new ParticleEffect();
        instance.load(
            Gdx.files.internal("prt/explosion"), game.getSkin().getAtlas()
        );
    }

    @Override
    public boolean remove() {
        final boolean result = super.remove();
        Pools.free(this);
        return result;
    }

    public static void disposeInstance() {
        instance.dispose();
    }
}
