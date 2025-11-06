public class ScreenManager {
    private GameState currentState;
    private Screen currentScreen;

    public ScreenManager() {
        setScreen(GameState.MENU);
    }

    public void setScreen(GameState state) {
        switch (state) {
            case MENU:
                currentScreen = new MenuScreen(this);
                break;
            case PLAYING:
                currentScreen = new GameScreen(this);
                break;
            case PAUSED:
                currentScreen = new PausedScreen(this);
                break;
            case GAME_OVER:
                currentScreen = new GameOverScreen(this);
                break;
        }

        currentState = state;
    }

    public void
}