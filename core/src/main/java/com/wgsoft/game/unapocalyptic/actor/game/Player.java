package com.wgsoft.game.unapocalyptic.actor.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Pools;
import com.wgsoft.game.unapocalyptic.AudioManager;
import com.wgsoft.game.unapocalyptic.Constants;
import com.wgsoft.game.unapocalyptic.screen.GameScreen;

public final class Player extends Group {
    private static final float JUMP_HEIGHT = 60f;
    private static final float JUMP_DURATION = 0.5f;

    private final GameScreen gameScreen;

    private final Animation<TextureRegion> standAnimation;
    private final Animation<TextureRegion> walkAnimation;

    private Animation<TextureRegion> animation;
    private float animationTime;

    private final Hammer hammer;

    private boolean a, d;
    private boolean left, right;
    private float direction;
    private boolean flip;

    public Player(final GameScreen gameScreen, final Skin skin) {
        this.gameScreen = gameScreen;

        standAnimation = new Animation<>(
            1f,
            skin.getRegions("player/stand"),
            Animation.PlayMode.LOOP
        );
        walkAnimation = new Animation<>(
            0.25f,
            skin.getRegions("player/walk"),
            Animation.PlayMode.LOOP
        );

        animation = standAnimation;

        setSize(120f, 120f);
        setPosition(
            Constants.SCREEN_WIDTH / 2f,
            GameScreen.GROUND_HEIGHT,
            Align.bottom | Align.center
        );

        hammer = new Hammer(skin);
        addActor(hammer);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(
                final InputEvent event,
                final float x,
                final float y,
                final int pointer,
                final int button
            ) {
                switch(button) {
                    case Input.Buttons.LEFT:
                        shoot();
                        return true;
                    case Input.Buttons.RIGHT:
                        smash();
                        return true;
                }

                return false;
            }

            @Override
            public boolean keyDown(final InputEvent event, final int keycode) {
                switch(keycode) {
                    case Input.Keys.A:
                        a = true;
                        updateDirection();
                        return true;
                    case Input.Keys.D:
                        d = true;
                        updateDirection();
                        return true;
                    case Input.Keys.LEFT:
                        left = true;
                        updateDirection();
                        return true;
                    case Input.Keys.RIGHT:
                        right = true;
                        updateDirection();
                        return true;
                    case Input.Keys.W:
                    case Input.Keys.UP:
                    case Input.Keys.Z:
                        shoot();
                        return true;
                    case Input.Keys.S:
                    case Input.Keys.DOWN:
                    case Input.Keys.X:
                    case Input.Keys.SPACE:
                        smash();
                        return true;
                }

                return false;
            }

            @Override
            public boolean keyUp(final InputEvent event, final int keycode) {
                switch(keycode) {
                    case Input.Keys.A:
                        a = false;
                        updateDirection();
                        return true;
                    case Input.Keys.D:
                        d = false;
                        updateDirection();
                        return true;
                    case Input.Keys.LEFT:
                        left = false;
                        updateDirection();
                        return true;
                    case Input.Keys.RIGHT:
                        right = false;
                        updateDirection();
                        return true;
                    case Input.Keys.W:
                    case Input.Keys.UP:
                    case Input.Keys.Z:
                    case Input.Keys.S:
                    case Input.Keys.DOWN:
                    case Input.Keys.X:
                    case Input.Keys.SPACE:
                        return true;
                }

                return false;
            }
        });
    }

    private void updateDirection() {
        direction = 0f;

        if(a || left) {
            direction -= 1f;
        }

        if (d || right) {
            direction += 1f;
        }

        if(direction < 0f) {
            flip = true;
            animation = walkAnimation;
        } else if(direction > 0f) {
            flip = false;
            animation = walkAnimation;
        } else {
            animation = standAnimation;
        }

        hammer.setFlip(flip);
    }

    private void shoot() {
        AudioManager.playShootSound();
        final Bullet bullet = Pools.obtain(Bullet.class);
        bullet
            .setPosition(getX(Align.center), getY(Align.center), Align.center);
        getParent().addActor(bullet);
    }

    private void smash() {
        if(hammer.smash()) {
            addAction(Actions.sequence(
                Actions.moveBy(
                    0f,
                    JUMP_HEIGHT,
                    JUMP_DURATION / 2f,
                    Interpolation.pow2Out
                ),
                Actions.moveBy(
                    0f,
                    -JUMP_HEIGHT,
                    JUMP_DURATION / 2f,
                    Interpolation.pow2In
                )
            ));
        }
    }

    public void die() {
        gameScreen.gameOver();
        remove();
    }

    @Override
    public Actor hit(float x, float y, boolean touchable) {
        return this;
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
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(final float delta) {
        animationTime += delta;

        final TextureRegion region = animation.getKeyFrame(animationTime);

        region.flip(region.isFlipX() != flip, false);

        moveBy(direction * 840f * delta, 0f);

        if(getStage() != null) {
            if(getX() < 0f) {
                setX(0f);
            } else if(getRight() > getStage().getWidth()) {
                setX(getStage().getWidth(), Align.right);
            }
        }

        super.act(delta);
    }
}
