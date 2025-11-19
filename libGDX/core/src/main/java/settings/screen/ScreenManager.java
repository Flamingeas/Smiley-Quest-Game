package settings.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import settings.GameState;
import settings.GameWorld;
import level.Level;
import utils.Coord;

public class ScreenManager {
    private GameState state;
    private Screen currentScreen;
    private GameScreen gameScreen;
    private GameWorld gameWorld;

    public void setState(GameState newState) {
        this.state = newState;

        switch (newState) {
            case SPLASH -> setScreen(new SplashScreen(this));
            case MENU -> setScreen(new MenuScreen(this));

            case PLAYING -> {
                if (gameWorld == null) {
                    Level level1 = new Level(
                            1,
                            "maps/sampleMap.tmx",
                            new Coord(0, 0)
                    );
                    gameWorld = new GameWorld(level1);
                }
                if (gameScreen == null) {
                    gameScreen = new GameScreen(this, gameWorld);
                }
                setScreen(gameScreen);
            }
            case PAUSED -> setScreen(new PausedScreen(this));
            case SETTINGS -> setScreen(new SettingsScreen(this));
            case GAME_OVER -> setScreen(new GameOverScreen());
        }
    }
    public void setScreen(Screen screen) {
        if (currentScreen != null)
            currentScreen.hide();

        currentScreen = screen;
        currentScreen.show();

        ((Game) Gdx.app.getApplicationListener()).setScreen(currentScreen);
    }
    public Screen getCurrentScreen() { return currentScreen; }
    public GameState getState() { return state; }
}
