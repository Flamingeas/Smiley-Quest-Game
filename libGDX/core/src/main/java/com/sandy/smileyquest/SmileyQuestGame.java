package com.sandy.smileyquest;

import com.badlogic.gdx.Game;
import settings.screen.ScreenManager;
import settings.GameState;

public class SmileyQuestGame extends Game {

    private ScreenManager screenManager;

    @Override
    public void create() {
        screenManager = new ScreenManager();
        screenManager.setState(GameState.MENU);
        setScreen(screenManager.getCurrentScreen());
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        if (screenManager.getCurrentScreen() != null) {
            screenManager.getCurrentScreen().dispose();
        }
    }
}
