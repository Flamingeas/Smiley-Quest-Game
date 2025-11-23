package com.sandy.smileyquest;

import com.badlogic.gdx.Game;
import settings.screen.ScreenManager;
import settings.GameState;

/**
 * Main class for the SmileyQuest game. It initializes the game,
 * manages the active screen through the ScreenManager, and handles
 * rendering and disposal of resources.
 */
public class SmileyQuestGame extends Game {
    private ScreenManager screenManager;

    /**
     * Called when the game is first created. Initializes the ScreenManager
     * and sets the initial state to the main menu.
     */
    @Override
    public void create() {
        screenManager = new ScreenManager();
        screenManager.setState(GameState.MENU);
        setScreen(screenManager.getCurrentScreen());
    }

    /**
     * Called every frame to render the active screen. Delegates the rendering
     * process to the superclass, which forwards it to the current screen.
     */
    @Override
    public void render() {
        super.render();
    }

    /**
     * Releases resources used by the current screen when the game is closed.
     */
    @Override
    public void dispose() {
        if (screenManager.getCurrentScreen() != null) {
            screenManager.getCurrentScreen().dispose();
        }
    }
}
