package com.wgsoft.game.unapocalyptic.actor.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.wgsoft.game.unapocalyptic.Constants;
import com.wgsoft.game.unapocalyptic.Unapocalyptic;
import com.wgsoft.game.unapocalyptic.screen.GameScreen;

public final class Player extends Group {
    private final GameScreen gameScreen;

    private final TextureRegion region;

    private boolean left, right;
    private float direction;

    public Player(
        final Unapocalyptic game,
        final GameScreen gameScreen,
        final Skin skin
    ) {
        this.gameScreen = gameScreen;
        region = skin.getRegion("player");

        setSize(120f, 120f);
        setPosition(
            Constants.SCREEN_WIDTH / 2f,
            GameScreen.GROUND_HEIGHT,
            Align.bottom | Align.center
        );

        final Hammer hammer = new Hammer(game, skin);
        addActor(hammer);

        addListener(new InputListener() {
            @Override
            public boolean keyDown(final InputEvent event, final int keycode) {
                switch(keycode) {
                    case Input.Keys.LEFT:
                        left = true;

                        if(right) {
                            direction = 0f;
                        } else {
                            direction = -1f;
                            region.flip(!region.isFlipX(), false);
                            hammer.setFlip(true);
                        }

                        return true;
                    case Input.Keys.RIGHT:
                        right = true;

                        if(left) {
                            direction = 0f;
                        } else {
                            direction = 1f;
                            region.flip(region.isFlipX(), false);
                            hammer.setFlip(false);
                        }

                        return true;
                    case Input.Keys.Z:
                        game.playShootSound();
                        getParent().addActor(new Bullet(
                            getX(Align.center),
                            getY(Align.center),
                            skin
                        ));
                        return true;
                    case Input.Keys.SPACE:
                        hammer.smash();
                        return true;
                }

                return false;
            }

            @Override
            public boolean keyUp(final InputEvent event, final int keycode) {
                switch(keycode) {
                    case Input.Keys.LEFT:
                        left = false;

                        if(right) {
                            direction = 1f;
                            region.flip(region.isFlipX(), false);
                            hammer.setFlip(false);
                        } else {
                            direction = 0f;
                        }

                        return true;
                    case Input.Keys.RIGHT:
                        right = false;

                        if(left) {
                            direction = -1f;
                            region.flip(!region.isFlipX(), false);
                            hammer.setFlip(true);
                        } else {
                            direction = 0f;
                        }

                        return true;
                    case Input.Keys.Z:
                    case Input.Keys.SPACE:
                        return true;
                }

                return false;
            }
        });
    }

    public void die() {
        gameScreen.gameOver();
        remove();
    }

    @Override
    public void draw(final Batch batch, final float parentAlpha) {
        batch.draw(region, getX(), getY(), getWidth(), getHeight());
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(final float delta) {
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
