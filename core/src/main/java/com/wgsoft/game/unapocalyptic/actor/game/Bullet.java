package com.wgsoft.game.unapocalyptic.actor.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

public final class Bullet extends Actor {
    private static TextureRegion region;

    static {
        Pools.set(Bullet.class, new Pool<Bullet>() {
            @Override
            protected Bullet newObject() {
                return new Bullet();
            }
        });
    }

    public static Bullet obtain(final float x, final float y, final Skin skin) {
        if(region == null) {
            region = skin.getRegion("bullet");
        }

        final Bullet bullet = Pools.obtain(Bullet.class);
        bullet.setPosition(x, y, Align.center);
        return bullet;
    }

    private Bullet() {
        setSize(30f, 30f);
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
