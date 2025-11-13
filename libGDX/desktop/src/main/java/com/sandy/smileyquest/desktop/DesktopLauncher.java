package com.sandy.smileyquest.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.sandy.smileyquest.SmileyQuestGame;

public class DesktopLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        config.setTitle("Smiley Quest");

        config.setWindowedMode(1280, 720);

        config.setDecorated(true);

        config.useVsync(true);
        config.setForegroundFPS(60);

        new Lwjgl3Application(new SmileyQuestGame(), config);
    }
}
