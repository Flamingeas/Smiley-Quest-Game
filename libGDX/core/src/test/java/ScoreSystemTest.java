package system;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests ScoreSystem - Coverage Maximum")
class ScoreSystemTest {

    private ScoreSystem scoreSystem;

    @BeforeEach
    void setup() {
        scoreSystem = new ScoreSystem();
    }

    @Test
    @DisplayName("Score initial est 0")
    void testScoreInitial() {
        assertEquals(0, scoreSystem.getScore());
    }

    @Test
    @DisplayName("addScore ajoute des points positifs")
    void testAddScorePositif() {
        int result = scoreSystem.addScore(100);
        assertEquals(100, result);
        assertEquals(100, scoreSystem.getScore());
    }

    @Test
    @DisplayName("addScore avec points négatifs")
    void testAddScoreNegatif() {
        scoreSystem.addScore(100);
        int result = scoreSystem.addScore(-30);
        assertEquals(70, result);
        assertEquals(70, scoreSystem.getScore());
    }

    @Test
    @DisplayName("addScore plusieurs fois")
    void testAddScorePlusieurs() {
        scoreSystem.addScore(10);
        scoreSystem.addScore(20);
        scoreSystem.addScore(30);
        assertEquals(60, scoreSystem.getScore());
    }

    @Test
    @DisplayName("reset remet le score à 0")
    void testReset() {
        scoreSystem.addScore(500);
        scoreSystem.reset();
        assertEquals(0, scoreSystem.getScore());
    }

    @Test
    @DisplayName("onEvent ITEM_COLLECTED ajoute des points")
    void testOnEventItemCollected() {
        GameEvent event = new GameEvent("ITEM_COLLECTED", 50);
        scoreSystem.onEvent(event);
        assertEquals(50, scoreSystem.getScore());
    }

    @Test
    @DisplayName("onEvent PLAYER_HIT retire 30 points")
    void testOnEventPlayerHit() {
        scoreSystem.addScore(100);
        GameEvent event = new GameEvent("PLAYER_HIT", null);
        scoreSystem.onEvent(event);
        assertEquals(70, scoreSystem.getScore());
    }

    @Test
    @DisplayName("onEvent CHEST_FOUND ajoute 100 points")
    void testOnEventChestFound() {
        GameEvent event = new GameEvent("CHEST_FOUND", null);
        scoreSystem.onEvent(event);
        assertEquals(100, scoreSystem.getScore());
    }

    @Test
    @DisplayName("onEvent ENEMY_KILLED ajoute 20 points")
    void testOnEventEnemyKilled() {
        GameEvent event = new GameEvent("ENEMY_KILLED", null);
        scoreSystem.onEvent(event);
        assertEquals(20, scoreSystem.getScore());
    }

    @Test
    @DisplayName("onEvent PLAYER_DEAD ne change rien")
    void testOnEventPlayerDead() {
        scoreSystem.addScore(100);
        GameEvent event = new GameEvent("PLAYER_DEAD", null);
        scoreSystem.onEvent(event);
        assertEquals(100, scoreSystem.getScore());
    }

    @Test
    @DisplayName("onEvent WEAPON_EQUIPPED ne change rien")
    void testOnEventWeaponEquipped() {
        scoreSystem.addScore(100);
        GameEvent event = new GameEvent("WEAPON_EQUIPPED", null);
        scoreSystem.onEvent(event);
        assertEquals(100, scoreSystem.getScore());
    }

    @Test
    @DisplayName("Scénario complet de jeu")
    void testScenarioComplet() {
        scoreSystem.onEvent(new GameEvent("ITEM_COLLECTED", 10));
        scoreSystem.onEvent(new GameEvent("ITEM_COLLECTED", 20));
        scoreSystem.onEvent(new GameEvent("ENEMY_KILLED", null));
        scoreSystem.onEvent(new GameEvent("PLAYER_HIT", null));
        scoreSystem.onEvent(new GameEvent("CHEST_FOUND", null));

        // 10 + 20 + 20 - 30 + 100 = 120
        assertEquals(120, scoreSystem.getScore());
    }
}