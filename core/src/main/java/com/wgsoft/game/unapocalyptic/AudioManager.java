package com.wgsoft.game.unapocalyptic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public final class AudioManager {
    private static AudioManager instance;

    private static AudioManager getInstance() {
        if(instance == null) {
            instance = new AudioManager();
        }

        return instance;
    }

    private final Sound eatenSound;
    private final Sound explosionSound;
    private final Sound hitSound;
    private final Sound shootSound;
    private final Sound smashSound;

    public AudioManager() {
        eatenSound = Gdx.audio.newSound(Gdx.files.internal("snd/eaten.wav"));
        explosionSound =
            Gdx.audio.newSound(Gdx.files.internal("snd/explosion.wav"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("snd/hit.wav"));
        shootSound = Gdx.audio.newSound(Gdx.files.internal("snd/shoot.wav"));
        smashSound = Gdx.audio.newSound(Gdx.files.internal("snd/smash.wav"));
    }

    public static void playEatenSound() {
        getInstance().eatenSound.play();
    }

    public static void playExplosionSound() {
        getInstance().explosionSound.play();
    }

    public static void playHitSound() {
        getInstance().hitSound.play();
    }

    public static void playShootSound() {
        getInstance().shootSound.play();
    }

    public static void playSmashSound() {
        getInstance().smashSound.play();
    }

    public static void dispose() {
        if(instance != null) {
            getInstance().eatenSound.dispose();
            getInstance().explosionSound.dispose();
            getInstance().hitSound.dispose();
            getInstance().shootSound.dispose();
            getInstance().smashSound.dispose();
        }
    }
}
