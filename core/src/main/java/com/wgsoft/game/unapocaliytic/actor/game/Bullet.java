package com.wgsoft.game.unapocalyptic.actor.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

public final class Bullet extends Actor {
    private final TextureRegion region;

    public Bullet(final float x, final float y, final Skin skin) {
        region = skin.getRegion("bullet");

        setSize(30f, 30f);
        setPosition(x, y, Align.center);
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        batch.draw(region, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void act(final float delta) {
        moveBy(0f, 1080f * delta);

        if(getStage() != null && getY() > getStage().getHeight()) {
            remove();
        }

        super.act(delta);
    }
}
