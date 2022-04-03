package com.wgsoft.game.unapocalyptic.actor.game;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.wgsoft.game.unapocalyptic.Unapocalyptic;

public final class ZombieSpawner extends Actor {
    public ZombieSpawner(
        final Unapocalyptic game,
        final Player player,
        final Skin skin
    ) {
        addAction(Actions.forever(Actions.delay(15f, Actions.run(() -> {
            getParent().addActor(new Zombie(game, player, skin));
        }))));
    }
}
