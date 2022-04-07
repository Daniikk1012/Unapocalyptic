package com.wgsoft.game.unapocalyptic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.wgsoft.game.unapocalyptic.actor.SmashParticleEffectActor;
import com.wgsoft.game.unapocalyptic.screen.GameScreen;

public final class Unapocalyptic extends Game {
    private SpriteBatch spriteBatch;

    private Skin skin;

    private Sound eatenSound;
    private Sound explosionSound;
    private Sound hitSound;
    private Sound shootSound;
    private Sound smashSound;

    private ParticleEffect smashParticleEffect;

    private GameScreen gameScreen;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();

        skin = new Skin(Gdx.files.internal("img/skin.json"));

        for(final BitmapFont font: skin.getAll(BitmapFont.class).values()) {
            for(final TextureRegion region: font.getRegions()) {
                region.getTexture().setFilter(
                    Texture.TextureFilter.Linear,
                    Texture.TextureFilter.Linear
                );
            }
        }

        eatenSound = Gdx.audio.newSound(Gdx.files.internal("snd/eaten.wav"));
        explosionSound =
            Gdx.audio.newSound(Gdx.files.internal("snd/explosion.wav"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("snd/hit.wav"));
        shootSound = Gdx.audio.newSound(Gdx.files.internal("snd/shoot.wav"));
        smashSound = Gdx.audio.newSound(Gdx.files.internal("snd/smash.wav"));

        smashParticleEffect = new ParticleEffect();
        smashParticleEffect
            .load(Gdx.files.internal("prt/smash"), skin.getAtlas());
        SmashParticleEffectActor.setInstance(smashParticleEffect);

        gameScreen = new GameScreen(this);

        gameScreen.start();
        setScreen(gameScreen);
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();

        gameScreen.dispose();

        spriteBatch.dispose();

        skin.dispose();

        eatenSound.dispose();
        explosionSound.dispose();
        hitSound.dispose();
        shootSound.dispose();
        smashSound.dispose();

        smashParticleEffect.dispose();
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public Skin getSkin() {
        return skin;
    }

    public void playEatenSound() {
        eatenSound.play();
    }

    public void playExplosionSound() {
        explosionSound.play();
    }

    public void playHitSound() {
        hitSound.play();
    }

    public void playShootSound() {
        shootSound.play();
    }

    public void playSmashSound() {
        smashSound.play();
    }
}
