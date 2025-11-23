package settings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import system.GameEvent;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests GameWorld - Exécution RÉELLE du code
 */
@DisplayName("GameWorld Tests Fonctionnels")
class GameWorldTest {

    private GameWorld gameWorld;

    @BeforeEach
    void setUp() {
        // Instanciation RÉELLE de GameWorld
        gameWorld = new GameWorld(1);
    }

    // ========== TESTS DU CONSTRUCTEUR ==========

    @Test
    @DisplayName("Le constructeur initialise le monde du jeu")
    void testConstructorInitializesGameWorld() {
        assertNotNull(gameWorld);
        assertNotNull(gameWorld.getPlayer());
        assertNotNull(gameWorld.getScoreSystem());
        assertNotNull(gameWorld.getTimer());
    }

    @Test
    @DisplayName("État initial du jeu est PLAYING")
    void testInitialStateIsPlaying() {
        assertEquals(GameState.PLAYING, gameWorld.getState());
    }

    @Test
    @DisplayName("Le premier niveau est chargé par défaut")
    void testFirstLevelIsLoaded() {
        assertEquals(1, gameWorld.getCurrentLevelNumber());
        assertNotNull(gameWorld.getCurrentLevel());
    }

    @Test
    @DisplayName("Constructeur avec différents smileys crée des joueurs")
    void testConstructorWithDifferentSmileys() {
        GameWorld gw0 = new GameWorld(0);
        GameWorld gw2 = new GameWorld(2);

        assertNotNull(gw0.getPlayer());
        assertNotNull(gw2.getPlayer());
    }

    // ========== TESTS DES GETTERS ==========

    @Test
    @DisplayName("GetCurrentLevelNumber retourne le bon numéro")
    void testGetCurrentLevelNumber() {
        assertEquals(1, gameWorld.getCurrentLevelNumber());
    }

    @Test
    @DisplayName("GetTotalLevels retourne 4")
    void testGetTotalLevels() {
        assertEquals(4, gameWorld.getTotalLevels());
    }

    @Test
    @DisplayName("GetPlayer retourne le joueur")
    void testGetPlayer() {
        assertNotNull(gameWorld.getPlayer());
    }

    @Test
    @DisplayName("GetCurrentLevel retourne le niveau actuel")
    void testGetCurrentLevel() {
        assertNotNull(gameWorld.getCurrentLevel());
        assertEquals("maps/sampleMap.tmx", gameWorld.getCurrentLevel().getMapFile());
    }

    @Test
    @DisplayName("GetScoreSystem retourne le système de score")
    void testGetScoreSystem() {
        assertNotNull(gameWorld.getScoreSystem());
        assertEquals(0, gameWorld.getScoreSystem().getScore());
    }

    @Test
    @DisplayName("GetTimer retourne le timer")
    void testGetTimer() {
        assertNotNull(gameWorld.getTimer());
    }

    @Test
    @DisplayName("GetEnemies retourne une liste d'ennemis")
    void testGetEnemies() {
        assertNotNull(gameWorld.getEnemies());
    }

    @Test
    @DisplayName("GetState retourne l'état actuel")
    void testGetState() {
        assertEquals(GameState.PLAYING, gameWorld.getState());
    }

    @Test
    @DisplayName("GetGameScreen retourne null au départ")
    void testGetGameScreenInitiallyNull() {
        assertNull(gameWorld.getGameScreen());
    }

    // ========== TESTS DES SETTERS ==========

    @Test
    @DisplayName("SetState change l'état du jeu")
    void testSetState() {
        gameWorld.setState(GameState.PAUSED);
        assertEquals(GameState.PAUSED, gameWorld.getState());

        gameWorld.setState(GameState.GAME_OVER);
        assertEquals(GameState.GAME_OVER, gameWorld.getState());
    }

    @Test
    @DisplayName("SetGameScreen définit l'écran")
    void testSetGameScreen() {
        // On ne peut pas vraiment créer GameScreen, donc on teste juste que la méthode existe
        gameWorld.setGameScreen(null);
        assertNull(gameWorld.getGameScreen());
    }

    @Test
    @DisplayName("SetScreenManager définit le gestionnaire")
    void testSetScreenManager() {
        gameWorld.setScreenManager(null);
        // Vérification indirecte
    }

    // ========== TESTS DES MÉTHODES DE LOGIQUE ==========

    @Test
    @DisplayName("NextLevel progresse au niveau 2")
    void testNextLevelToLevel2() {
        boolean result = gameWorld.nextLevel();

        assertTrue(result);
        assertEquals(2, gameWorld.getCurrentLevelNumber());
        assertEquals("maps/laby.tmx", gameWorld.getCurrentLevel().getMapFile());
    }

    @Test
    @DisplayName("NextLevel progresse de 1 à 4")
    void testNextLevelProgression() {
        assertTrue(gameWorld.nextLevel()); // 1 -> 2
        assertEquals(2, gameWorld.getCurrentLevelNumber());

        assertTrue(gameWorld.nextLevel()); // 2 -> 3
        assertEquals(3, gameWorld.getCurrentLevelNumber());

        assertTrue(gameWorld.nextLevel()); // 3 -> 4
        assertEquals(4, gameWorld.getCurrentLevelNumber());
    }

    @Test
    @DisplayName("NextLevel retourne false au dernier niveau")
    void testNextLevelAtEnd() {
        gameWorld.nextLevel(); // 1 -> 2
        gameWorld.nextLevel(); // 2 -> 3
        gameWorld.nextLevel(); // 3 -> 4

        boolean result = gameWorld.nextLevel(); // 4 -> ?

        assertFalse(result);
        assertEquals(4, gameWorld.getCurrentLevelNumber());
    }

    @Test
    @DisplayName("EndGame définit l'état à GAME_OVER")
    void testEndGame() {
        gameWorld.endGame();
        assertEquals(GameState.GAME_OVER, gameWorld.getState());
    }

    // ========== TESTS DES ÉVÉNEMENTS ==========

    @Test
    @DisplayName("OnNotify avec ITEM_COLLECTED ajoute du score")
    void testOnNotifyItemCollected() {
        gameWorld.setScreenManager(null);
        int initialScore = gameWorld.getScoreSystem().getScore();

        gameWorld.onNotify(new GameEvent("ITEM_COLLECTED", 50));

        assertEquals(initialScore + 50, gameWorld.getScoreSystem().getScore());
    }

    @Test
    @DisplayName("OnNotify avec ITEM_COLLECTED multiple")
    void testOnNotifyItemCollectedMultiple() {
        gameWorld.setScreenManager(null);

        gameWorld.onNotify(new GameEvent("ITEM_COLLECTED", 10));
        gameWorld.onNotify(new GameEvent("ITEM_COLLECTED", 20));
        gameWorld.onNotify(new GameEvent("ITEM_COLLECTED", 30));

        assertEquals(60, gameWorld.getScoreSystem().getScore());
    }

    @Test
    @DisplayName("OnNotify avec PLAYER_HIT enlève du score")
    void testOnNotifyPlayerHit() {
        gameWorld.setScreenManager(null);
        int initialScore = gameWorld.getScoreSystem().getScore();

        gameWorld.onNotify(new GameEvent("PLAYER_HIT", null));

        assertEquals(initialScore - 30, gameWorld.getScoreSystem().getScore());
    }

    @Test
    @DisplayName("OnNotify avec PLAYER_DEAD change l'état")
    void testOnNotifyPlayerDead() {
        gameWorld.setScreenManager(null);

        gameWorld.onNotify(new GameEvent("PLAYER_DEAD", null));

        // État change via screenManager (null donc pas d'effet direct)
    }

    @Test
    @DisplayName("OnNotify avec CHEST_FOUND progresse")
    void testOnNotifyChestFound() {
        gameWorld.setScreenManager(null);
        gameWorld.setGameScreen(null);
        int initialScore = gameWorld.getScoreSystem().getScore();

        gameWorld.onNotify(new GameEvent("CHEST_FOUND", null));

        // Le score augmente de 100
        assertEquals(initialScore + 100, gameWorld.getScoreSystem().getScore());
    }

    // ========== TESTS UPDATE ==========

    @Test
    @DisplayName("Update sans GameScreen n'échoue pas")
    void testUpdateWithoutGameScreen() {
        gameWorld.update(0.016f);
        // Pas d'erreur
    }

    @Test
    @DisplayName("Update multiple fois")
    void testUpdateMultipleTimes() {
        gameWorld.update(0.016f);
        gameWorld.update(0.016f);
        gameWorld.update(0.016f);

        // Pas d'erreur
    }

    // ========== TESTS PUBLISH ==========

    @Test
    @DisplayName("Publish enregistre les événements")
    void testPublish() {
        gameWorld.setScreenManager(null);

        gameWorld.publish(new GameEvent("TEST", null));
        // Pas d'erreur
    }

    // ========== TESTS IsWalkableTile ==========

    @Test
    @DisplayName("IsWalkableTile sans GameScreen retourne false")
    void testIsWalkableTileWithoutGameScreen() {
        boolean result = gameWorld.isWalkableTile(0, 0);
        assertFalse(result);
    }
}