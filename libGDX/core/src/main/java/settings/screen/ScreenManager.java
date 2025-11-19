package settings.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import settings.GameState;
import settings.GameWorld;

public class ScreenManager {

    private GameState state;

    private Screen currentScreen;

    private GameScreen gameScreen;
    private PausedScreen pausedScreen;
    private MenuScreen menuScreen;
    private SettingsScreen settingsScreen;

    private GameWorld gameWorld;

    public ScreenManager() {
        pausedScreen = new PausedScreen(this);
        menuScreen = new MenuScreen(this);
        settingsScreen = new SettingsScreen(this);
    }

    public void setState(GameState newState) {
        this.state = newState;

        switch (newState) {

            case MENU -> setScreen(menuScreen);

            case PLAYING -> {
                if (gameWorld == null) {
                    gameWorld = new GameWorld(null);
                }
                if (gameScreen == null) {
                    gameScreen = new GameScreen(this, gameWorld);
                }
                setScreen(gameScreen);
            }

            case PAUSED -> setScreen(pausedScreen);

            case SETTINGS -> setScreen(settingsScreen);

            case GAME_OVER -> setScreen(new GameOverScreen());

            case SPLASH -> setScreen(new SplashScreen(this));
        }
    }
    public void resumeGame() {
        this.state = GameState.PLAYING;
        setScreen(gameScreen);
    }
    public void setScreen(Screen screen) {
        if (currentScreen != null)
            currentScreen.hide();

        currentScreen = screen;
        currentScreen.show();

        ((Game) Gdx.app.getApplicationListener()).setScreen(screen);
    }
    public Screen getCurrentScreen() { return currentScreen; }
    public GameState getState() { return state; }
}
