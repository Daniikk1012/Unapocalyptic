package com.wgsoft.game.unapocalyptic.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.wgsoft.game.unapocalyptic.Unapocalyptic;

public final class Launcher {
    public static void main(final String[] args) {
        final Lwjgl3ApplicationConfiguration configuration =
            new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Unapocalyptic");
        configuration.setWindowedMode(800, 480);

        new Lwjgl3Application(new Unapocalyptic(), configuration);
    }
}
