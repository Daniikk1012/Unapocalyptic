package com.wgsoft.game.unapocalyptic.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.wgsoft.game.unapocalyptic.Constants;
import com.wgsoft.game.unapocalyptic.PreferenceManager;
import com.wgsoft.game.unapocalyptic.Unapocalyptic;
import com.wgsoft.game.unapocalyptic.actor.game.Comet;
import com.wgsoft.game.unapocalyptic.actor.game.NukeSpawner;
import com.wgsoft.game.unapocalyptic.actor.game.Player;
import com.wgsoft.game.unapocalyptic.actor.game.ZombieSpawner;

public final class GameScreen extends ScreenAdapter {
    public static final float GROUND_HEIGHT = 240f;

    private final Unapocalyptic game;

    private final Stage stage;

    private final WidgetGroup gameGroup;
    private final Label scoreLabel;
    private Action scoreAction;
    private final Label highScoreLabel;
    private final Label descriptionLabel;
    private final Widget restartActor;

    private float time;

    public GameScreen(final Unapocalyptic game) {
        this.game = game;
        stage = new Stage(new ScreenViewport(), game.getSpriteBatch());

        final Image groundImage =
            new Image(game.getSkin().getTiledDrawable("ground"));
        ((TiledDrawable)groundImage.getDrawable())
            .setScale(GROUND_HEIGHT / groundImage.getDrawable().getMinHeight());

        final Container<Image> groundContainer = new Container<>(groundImage);
        groundContainer.setFillParent(true);
        groundContainer.bottom();
        groundContainer.fillX();
        groundContainer.height(GROUND_HEIGHT);
        stage.addActor(groundContainer);

        gameGroup = new WidgetGroup();
        gameGroup.setFillParent(true);
        stage.addActor(gameGroup);

        final Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.top();

        scoreLabel =
            new Label("0", game.getSkin(), "montserrat-extrabold-outline");
        rootTable.add(scoreLabel);

        rootTable.row();

        highScoreLabel =
            new Label("", game.getSkin(), "montserrat-regular-medium");
        rootTable.add(highScoreLabel);

        rootTable.row();

        descriptionLabel = new Label(
            "Press any key to restart",
            game.getSkin(),
            "montserrat-regular-medium"
        );
        rootTable.add(descriptionLabel);

        stage.addActor(rootTable);

        restartActor = new Widget();
        restartActor.setFillParent(true);
        restartActor.addListener(new InputListener() {
            @Override
            public boolean touchDown(
                final InputEvent event,
                final float x,
                final float y,
                final int pointer,
                final int button
            ) {
                start();
                return true;
            }

            @Override
            public boolean keyDown(final InputEvent event, final int keycode) {
                start();
                return true;
            }

            @Override
            public boolean keyUp(final InputEvent event, final int keycode) {
                return true;
            }
        });
        stage.addActor(restartActor);
    }

    public void start() {
        gameGroup.clear();

        final Player player = new Player(this, game.getSkin());

        final Comet comet = new Comet(player, game.getSkin());
        gameGroup.addActor(comet);

        stage.setKeyboardFocus(player);
        gameGroup.addActor(player);

        gameGroup.addActor(new ZombieSpawner(player, game.getSkin()));
        gameGroup.addActor(new NukeSpawner(player, game.getSkin()));

        scoreAction = Actions.forever(Actions.run(() -> {
            scoreLabel.setText((int)time);
        }));
        scoreLabel.addAction(scoreAction);

        highScoreLabel.setVisible(false);

        descriptionLabel.setVisible(false);

        restartActor.setVisible(false);

        time = 0f;
    }

    public void gameOver() {
        stage.setKeyboardFocus(restartActor);

        scoreLabel.removeAction(scoreAction);

        if((int)time > PreferenceManager.getHighScore()) {
            PreferenceManager.setHighScore((int)time);
            PreferenceManager.flush();
        }

        highScoreLabel
            .setText("High Score: " + PreferenceManager.getHighScore());
        highScoreLabel.setVisible(true);

        descriptionLabel.setVisible(true);

        restartActor.setVisible(true);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(final float delta) {
        time += delta;

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(final int width, final int height) {
        ((ScreenViewport)stage.getViewport()).setUnitsPerPixel(
            width * Constants.SCREEN_HEIGHT > Constants.SCREEN_WIDTH * height
                ? Constants.SCREEN_HEIGHT / height
                : Constants.SCREEN_WIDTH / width
        );
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
