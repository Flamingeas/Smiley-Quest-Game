package settings.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import level.Level;
import settings.GameState;

public class ScreenManager {
    private GameState state;
    private Screen currentScreen;

    public ScreenManager() {
    }

    public void setState(GameState newState) {
        this.state = newState;

        switch (newState) {
            case SPLASH:
                setScreen(new SplashScreen(this));
                break;

            case MENU:
                setScreen(new MenuScreen(this));
                break;

            case PLAYING:
                setScreen(new GameScreen();
                break;

            case PAUSED:
                setScreen(new PausedScreen());
                break;

            case SETTINGS:
                setScreen(new SettingsScreen(this));
                break;

            case GAME_OVER:
                setScreen(new GameOverScreen());
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + newState);
        }
    }

    public void setScreen(Screen screen) {
        if (currentScreen != null) {
            currentScreen.hide();
            currentScreen.dispose();
        }

        currentScreen = screen;
        currentScreen.show();
        ((Game) Gdx.app.getApplicationListener()).setScreen(currentScreen);
    }

    public Screen getCurrentScreen() {
        return currentScreen;
    }

    public GameState getState() {
        return state;
    }
}
