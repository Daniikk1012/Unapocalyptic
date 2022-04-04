package com.wgsoft.game.unapocalyptic.html;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.wgsoft.game.unapocalyptic.Unapocalyptic;

public final class Launcher extends GwtApplication {
    @Override
    public ApplicationListener createApplicationListener() {
        return new Unapocalyptic();
    }

    @Override
    public GwtApplicationConfiguration getConfig() {
        return new GwtApplicationConfiguration(true);
    }
}
