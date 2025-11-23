import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import settings.GameState;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GameState Enum Tests")
class GameStateTest {

    @Test
    @DisplayName("All game states should exist")
    void testAllStatesExist() {
        assertNotNull(GameState.SPLASH);
        assertNotNull(GameState.MENU);
        assertNotNull(GameState.PLAYING);
        assertNotNull(GameState.PAUSED);
        assertNotNull(GameState.SETTINGS);
        assertNotNull(GameState.GAME_OVER);
        assertNotNull(GameState.CHARACTER_SELECT);
    }
}
