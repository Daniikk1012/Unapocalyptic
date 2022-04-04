package com.wgsoft.game.unapocalyptic.actor.game;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.wgsoft.game.unapocalyptic.Unapocalyptic;

public final class NukeSpawner extends Actor {
    private static final float SPAWN_INTERVAL_MIN = 0.25f;

    private final Unapocalyptic game;

    private final Player player;

    private final Skin skin;

    private float spawnInterval;
    private float time;

    public NukeSpawner(
        final Unapocalyptic game,
        final Player player,
        final Skin skin
    ) {
        this.game = game;
        this.player = player;
        this.skin = skin;

        spawnInterval = 10f;
    }

    @Override
    public void act(final float delta) {
        time += delta;

        while(time >= spawnInterval) {
            final Nuke nuke = new Nuke(game, player, skin);
            getParent().addActor(nuke);
            nuke.act(delta);

            time -= spawnInterval;
            spawnInterval -= 0.05f;

            if(spawnInterval < SPAWN_INTERVAL_MIN) {
                spawnInterval = SPAWN_INTERVAL_MIN;
            }
        }

        super.act(delta);
    }
}
