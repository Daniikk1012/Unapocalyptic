package com.wgsoft.game.unapocalyptic.actor.game;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public final class ZombieSpawner extends Actor {
    public ZombieSpawner(final Player player, final Skin skin) {
        addAction(Actions.forever(Actions.delay(15f, Actions.run(() -> {
            getParent().addActor(new Zombie(player, skin));
        }))));
    }
}
