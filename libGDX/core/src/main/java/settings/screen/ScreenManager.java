package settings.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.sandy.smileyquest.SmileyQuestGame;
import settings.GameState;
import settings.GameWorld;

public class ScreenManager {

    private GameState state;

    private Screen currentScreen;

    private GameScreen gameScreen;
    private PausedScreen pausedScreen;
    private MenuScreen menuScreen;
    private SettingsScreen settingsScreen;
    private GameOverScreen gameOverScreen;
    private WinnerScreen winnerScreen;

    private GameWorld gameWorld;
    private SmileyQuestGame game;
    private Screen backgroundScreen;

    private int selectedSmileyIndex = 0;

    public ScreenManager() {
        pausedScreen = new PausedScreen(this);
        menuScreen = new MenuScreen(this);
        settingsScreen = new SettingsScreen(this);
        gameOverScreen= new GameOverScreen(this);
        winnerScreen = new WinnerScreen(this);
    }

    public void setState(GameState newState) {
        this.state = newState;
        backgroundScreen = null;

        switch (newState) {

            case MENU -> setScreen(menuScreen);

            case CHARACTER_SELECT ->
                    setScreen(new CharacterSelectScreen(this));

            case PLAYING -> {
                gameWorld = new GameWorld(selectedSmileyIndex);
                gameScreen = new GameScreen(this, gameWorld);
                gameWorld.setGameScreen(gameScreen);
                setScreen(gameScreen);
            }

            case PAUSED -> setScreen(pausedScreen);

            case SETTINGS -> setScreen(settingsScreen);

            case GAME_OVER -> {
                backgroundScreen = gameScreen;
                setScreen(gameOverScreen);
            }
            case WINNER -> setScreen(winnerScreen);

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

    public void setSelectedSmiley(int index) {
        this.selectedSmileyIndex = index;
    }

    public int getSelectedSmiley() {
        return selectedSmileyIndex;
    }
}
