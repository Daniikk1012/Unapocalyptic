package com.wgsoft.game.unapocalyptic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public final class PreferenceManager {
    private static final String HIGH_SCORE = "high-score";

    private static Preferences instance;

    public static void initialize() {
        instance = Gdx.app.getPreferences("com.wgsoft.game.unapocalyptic");
    }

    public static void flush() {
        instance.flush();
    }

    public static void setHighScore(final int highScore) {
        instance.putInteger(HIGH_SCORE, highScore);
    }

    public static int getHighScore() {
        return instance.getInteger(HIGH_SCORE, 0);
    }
}
