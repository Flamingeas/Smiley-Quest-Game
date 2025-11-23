package system;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ScoreSystem.
 * Verifies score initialization, adding points, resetting, and reacting to GameEvents.
 */
@DisplayName("ScoreSystem Tests - Maximum Coverage")
class ScoreSystemTest {

    private ScoreSystem scoreSystem;

    @BeforeEach
    void setup() {
        scoreSystem = new ScoreSystem();
    }

    @Test
    @DisplayName("Initial score is 0")
    void testInitialScore() {
        assertEquals(0, scoreSystem.getScore());
    }

    @Test
    @DisplayName("addScore adds positive points")
    void testAddPositiveScore() {
        int result = scoreSystem.addScore(100);
        assertEquals(100, result);
        assertEquals(100, scoreSystem.getScore());
    }

    @Test
    @DisplayName("addScore with negative points")
    void testAddNegativeScore() {
        scoreSystem.addScore(100);
        int result = scoreSystem.addScore(-30);
        assertEquals(70, result);
        assertEquals(70, scoreSystem.getScore());
    }

    @Test
    @DisplayName("addScore multiple times")
    void testAddScoreMultipleTimes() {
        scoreSystem.addScore(10);
        scoreSystem.addScore(20);
        scoreSystem.addScore(30);
        assertEquals(60, scoreSystem.getScore());
    }

    @Test
    @DisplayName("reset sets score to 0")
    void testReset() {
        scoreSystem.addScore(500);
        scoreSystem.reset();
        assertEquals(0, scoreSystem.getScore());
    }

    @Test
    @DisplayName("onEvent ITEM_COLLECTED adds points")
    void testOnEventItemCollected() {
        GameEvent event = new GameEvent("ITEM_COLLECTED", 50);
        scoreSystem.onEvent(event);
        assertEquals(50, scoreSystem.getScore());
    }

    @Test
    @DisplayName("onEvent PLAYER_HIT subtracts 30 points")
    void testOnEventPlayerHit() {
        scoreSystem.addScore(100);
        GameEvent event = new GameEvent("PLAYER_HIT", null);
        scoreSystem.onEvent(event);
        assertEquals(70, scoreSystem.getScore());
    }

    @Test
    @DisplayName("onEvent CHEST_FOUND adds 100 points")
    void testOnEventChestFound() {
        GameEvent event = new GameEvent("CHEST_FOUND", null);
        scoreSystem.onEvent(event);
        assertEquals(100, scoreSystem.getScore());
    }

    @Test
    @DisplayName("onEvent ENEMY_KILLED adds 20 points")
    void testOnEventEnemyKilled() {
        GameEvent event = new GameEvent("ENEMY_KILLED", null);
        scoreSystem.onEvent(event);
        assertEquals(20, scoreSystem.getScore());
    }

    @Test
    @DisplayName("onEvent PLAYER_DEAD does not change score")
    void testOnEventPlayerDead() {
        scoreSystem.addScore(100);
        GameEvent event = new GameEvent("PLAYER_DEAD", null);
        scoreSystem.onEvent(event);
        assertEquals(100, scoreSystem.getScore());
    }

    @Test
    @DisplayName("onEvent WEAPON_EQUIPPED does not change score")
    void testOnEventWeaponEquipped() {
        scoreSystem.addScore(100);
        GameEvent event = new GameEvent("WEAPON_EQUIPPED", null);
        scoreSystem.onEvent(event);
        assertEquals(100, scoreSystem.getScore());
    }

    @Test
    @DisplayName("Full gameplay scenario")
    void testFullScenario() {
        scoreSystem.onEvent(new GameEvent("ITEM_COLLECTED", 10));
        scoreSystem.onEvent(new GameEvent("ITEM_COLLECTED", 20));
        scoreSystem.onEvent(new GameEvent("ENEMY_KILLED", null));
        scoreSystem.onEvent(new GameEvent("PLAYER_HIT", null));
        scoreSystem.onEvent(new GameEvent("CHEST_FOUND", null));

        assertEquals(120, scoreSystem.getScore());
    }
}
