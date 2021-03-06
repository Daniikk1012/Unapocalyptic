package com.wgsoft.game.unapocalyptic.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.ui.ParticleEffectActor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

public final class SmashParticleEffectActor extends ParticleEffectActor {
    private static ParticleEffect instance;

    static {
        Pools.set(
            SmashParticleEffectActor.class,
            new Pool<SmashParticleEffectActor>() {
                @Override
                protected SmashParticleEffectActor newObject() {
                    return new SmashParticleEffectActor(
                        new ParticleEffect(instance)
                    );
                }
            }
        );
    }

    public SmashParticleEffectActor(final ParticleEffect particleEffect) {
        super(particleEffect, true);
        setAutoRemove(true);
    }

    public static void initialize(final Skin skin) {
        instance = new ParticleEffect();
        instance.load(Gdx.files.internal("prt/smash"), skin.getAtlas());
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
