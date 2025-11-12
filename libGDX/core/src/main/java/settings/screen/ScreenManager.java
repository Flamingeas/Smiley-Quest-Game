package settings.screen;

import settings.GameState;
import com.badlogic.gdx.Screen;


public class ScreenManager {
    private GameState state;
    private Screen currentScreen;

    public ScreenManager() {
        setState(GameState.MENU);
    }

    public void setState(GameState newState) {
        this.state = newState;

        switch (newState) {
            case MENU:
                setScreen(new MenuScreen());
                break;
            case PLAYING:
                setScreen(new GameScreen());
                break;
            case PAUSED:
                setScreen(new PausedScreen());
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
        }

        public Screen getCurrentScreen() {
            return currentScreen;
        }

        public GameState getState() {
            return state;
        }

}