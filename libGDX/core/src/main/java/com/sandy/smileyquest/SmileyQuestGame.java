package com.sandy.smileyquest;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import settings.screen.ScreenManager;
import settings.GameState;

public class SmileyQuestGame extends Game {

    private ScreenManager screenManager;

    @Override
    public void create() {
        screenManager = new ScreenManager();

        screenManager.setState(GameState.MENU);
    }

    @Override
    public void render() {
        if (screenManager.getCurrentScreen() != null) {
            screenManager.getCurrentScreen().render(Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public void dispose() {
        if (screenManager.getCurrentScreen() != null) {
            screenManager.getCurrentScreen().dispose();
        }
    }
}
