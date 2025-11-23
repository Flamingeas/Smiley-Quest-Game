package settings.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.sandy.smileyquest.SmileyQuestGame;
import settings.GameState;
import settings.GameWorld;

public class ScreenManager {

    /** Current game state */
    private GameState state;

    /** Currently active screen */
    private Screen currentScreen;

    /** Gameplay screen */
    private GameScreen gameScreen;

    /** Pause screen */
    private PausedScreen pausedScreen;

    /** Main menu screen */
    private MenuScreen menuScreen;

    /** Settings screen */
    private SettingsScreen settingsScreen;

    /** Game over screen */
    private GameOverScreen gameOverScreen;

    /** Current game world instance for gameplay */
    GameWorld gameWorld;

    /** Reference to the main game class */
    private SmileyQuestGame game;

    /** Optional background screen (used for overlays like game over) */
    private Screen backgroundScreen;

    /** Currently selected smiley index for character selection */
    private int selectedSmileyIndex = 0;


    /**
     * Manages the game's screens and transitions between them.
     * The ScreenManager handles:
     * - switching between different screens (menu, gameplay, pause, settings, game over)
     * - initializing and disposing screens as needed
     * - keeping track of the current game state
     * - maintaining the selected character (smiley)
     */
    public ScreenManager() {
        pausedScreen = new PausedScreen(this);
        menuScreen = new MenuScreen(this);
        settingsScreen = new SettingsScreen(this);
        gameOverScreen= new GameOverScreen(this);
    }

    /**
     * Changes the current game state and switches to the corresponding screen.
     *
     * @param newState the new game state
     */
    public void setState(GameState newState) {
        this.state = newState;
        backgroundScreen = null;

        switch (newState) {

            case MENU -> {
                gameWorld = null;
                setScreen(menuScreen);
            }

            case CHARACTER_SELECT ->
                    setScreen(new CharacterSelectScreen(this));

            case PLAYING -> {

                gameWorld = new GameWorld(selectedSmileyIndex);
                gameScreen = new GameScreen(this, gameWorld);

                gameWorld.setGameScreen(gameScreen);
                gameWorld.setScreenManager(this);
                gameWorld.getPlayer().resetFull();

                setScreen(gameScreen);
            }
            case PAUSED -> setScreen(pausedScreen);

            case SETTINGS -> setScreen(settingsScreen);

            case GAME_OVER -> {
                backgroundScreen = gameScreen;
                setScreen(gameOverScreen);
            }
            case WINNER -> setScreen(new GameWinnerScreen(this));
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

    /**
     * Returns the currently selected smiley character index.
     *
     * @return the selected smiley index
     */
    public int getSelectedSmiley() {
        return selectedSmileyIndex;
    }
}
