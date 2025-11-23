package com.sandy.smileyquest.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.sandy.smileyquest.SmileyQuestGame;

/**
 * Entry point for the desktop version of Smiley Quest using LWJGL3 backend.
 * Configures window settings such as title, fullscreen, FPS, and decorations.
 */
public class DesktopLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        config.setTitle("Smiley Quest");

        config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());

        config.useVsync(true);
        config.setForegroundFPS(60);
        config.setResizable(false);
        config.setDecorated(false);

        new Lwjgl3Application(new SmileyQuestGame(), config);
    }
}
