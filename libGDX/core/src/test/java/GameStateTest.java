import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import settings.GameState;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour l'enum GameState
 * Vérifie que tous les états du jeu sont bien définis
 */
@DisplayName("Tests de GameState")
class GameStateTest {

    @Test
    @DisplayName("Tous les états devraient exister")
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