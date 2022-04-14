package com.wgsoft.game.unapocalyptic.actor.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

public final class Bullet extends Actor {
    private static Skin skin;

    static {
        Pools.set(Bullet.class, new Pool<Bullet>() {
            @Override
            protected Bullet newObject() {
                return new Bullet(skin);
            }
        });
    }

    private final TextureRegion region;

    public Bullet(final Skin skin) {
        region = skin.getRegion("bullet");

        setSize(30f, 30f);
    }

    public static void initialize(final Skin skin) {
        Bullet.skin = skin;
    }

    @Override
    public boolean remove() {
        final boolean result = super.remove();
        Pools.free(this);
        return result;
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
