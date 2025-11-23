package settings;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import settings.screen.ScreenManager;
import system.GameEvent;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour GameWorld - Version simplifiée sans LibGDX.
 * Ces tests vérifient la structure et les méthodes accessibles.
 */
@DisplayName("Tests du GameWorld")
class GameWorldTest {

    // ========== TESTS DE STRUCTURE ==========

    @Test
    @DisplayName("La classe GameWorld existe")
    void testerClasseExiste() {
        assertNotNull(GameWorld.class);
    }

    @Test
    @DisplayName("Le champ levels existe")
    void testerChampLevelsExiste() throws Exception {
        Field champ = GameWorld.class.getDeclaredField("levels");
        assertNotNull(champ);
    }

    @Test
    @DisplayName("Le champ currentLevelIndex existe")
    void testerChampCurrentLevelIndexExiste() throws Exception {
        Field champ = GameWorld.class.getDeclaredField("currentLevelIndex");
        assertNotNull(champ);
        assertEquals(int.class, champ.getType());
    }

    @Test
    @DisplayName("Le champ player existe")
    void testerChampPlayerExiste() throws Exception {
        Field champ = GameWorld.class.getDeclaredField("player");
        assertNotNull(champ);
    }

    @Test
    @DisplayName("Le champ state existe")
    void testerChampStateExiste() throws Exception {
        Field champ = GameWorld.class.getDeclaredField("state");
        assertNotNull(champ);
    }

    @Test
    @DisplayName("Le champ scoreSystem existe")
    void testerChampScoreSystemExiste() throws Exception {
        Field champ = GameWorld.class.getDeclaredField("scoreSystem");
        assertNotNull(champ);
    }

    @Test
    @DisplayName("Le champ timer existe")
    void testerChampTimerExiste() throws Exception {
        Field champ = GameWorld.class.getDeclaredField("timer");
        assertNotNull(champ);
    }

    @Test
    @DisplayName("Le champ enemies existe")
    void testerChampEnemiesExiste() throws Exception {
        Field champ = GameWorld.class.getDeclaredField("enemies");
        assertNotNull(champ);
    }

    @Test
    @DisplayName("Le champ enemiesSpawned existe")
    void testerChampEnemiesSpawnedExiste() throws Exception {
        Field champ = GameWorld.class.getDeclaredField("enemiesSpawned");
        assertNotNull(champ);
        assertEquals(boolean.class, champ.getType());
    }

    @Test
    @DisplayName("Le champ selectedSmileyIndex existe")
    void testerChampSelectedSmileyIndexExiste() throws Exception {
        Field champ = GameWorld.class.getDeclaredField("selectedSmileyIndex");
        assertNotNull(champ);
        assertEquals(int.class, champ.getType());
    }

    @Test
    @DisplayName("Le champ gameScreen existe")
    void testerChampGameScreenExiste() throws Exception {
        Field champ = GameWorld.class.getDeclaredField("gameScreen");
        assertNotNull(champ);
    }

    @Test
    @DisplayName("Le champ screenManager existe")
    void testerChampScreenManagerExiste() throws Exception {
        Field champ = GameWorld.class.getDeclaredField("screenManager");
        assertNotNull(champ);
    }

    // ========== TESTS DES MÉTHODES ==========

    @Test
    @DisplayName("La méthode setGameScreen existe")
    void testerMethodeSetGameScreenExiste() throws Exception {
        var methode = GameWorld.class.getDeclaredMethod("setGameScreen",
                settings.screen.GameScreen.class);
        assertNotNull(methode);
    }

    @Test
    @DisplayName("La méthode getGameScreen existe")
    void testerMethodeGetGameScreenExiste() throws Exception {
        var methode = GameWorld.class.getDeclaredMethod("getGameScreen");
        assertNotNull(methode);
    }

    @Test
    @DisplayName("La méthode isWalkableTile existe")
    void testerMethodeIsWalkableTileExiste() throws Exception {
        var methode = GameWorld.class.getDeclaredMethod("isWalkableTile",
                int.class, int.class);
        assertNotNull(methode);
    }

    @Test
    @DisplayName("La méthode update existe")
    void testerMethodeUpdateExiste() throws Exception {
        var methode = GameWorld.class.getDeclaredMethod("update", float.class);
        assertNotNull(methode);
    }

    @Test
    @DisplayName("La méthode nextLevel existe")
    void testerMethodeNextLevelExiste() throws Exception {
        var methode = GameWorld.class.getDeclaredMethod("nextLevel");
        assertNotNull(methode);
        assertEquals(boolean.class, methode.getReturnType());
    }

    @Test
    @DisplayName("La méthode publish existe")
    void testerMethodePublishExiste() throws Exception {
        var methode = GameWorld.class.getDeclaredMethod("publish", GameEvent.class);
        assertNotNull(methode);
    }

    @Test
    @DisplayName("La méthode setState existe")
    void testerMethodeSetStateExiste() throws Exception {
        var methode = GameWorld.class.getDeclaredMethod("setState", GameState.class);
        assertNotNull(methode);
    }

    @Test
    @DisplayName("La méthode getState existe")
    void testerMethodeGetStateExiste() throws Exception {
        var methode = GameWorld.class.getDeclaredMethod("getState");
        assertNotNull(methode);
        assertEquals(GameState.class, methode.getReturnType());
    }

    @Test
    @DisplayName("La méthode endGame existe")
    void testerMethodeEndGameExiste() throws Exception {
        var methode = GameWorld.class.getDeclaredMethod("endGame");
        assertNotNull(methode);
    }

    @Test
    @DisplayName("La méthode getCurrentLevel existe")
    void testerMethodeGetCurrentLevelExiste() throws Exception {
        var methode = GameWorld.class.getDeclaredMethod("getCurrentLevel");
        assertNotNull(methode);
    }

    @Test
    @DisplayName("La méthode getPlayer existe")
    void testerMethodeGetPlayerExiste() throws Exception {
        var methode = GameWorld.class.getDeclaredMethod("getPlayer");
        assertNotNull(methode);
    }

    @Test
    @DisplayName("La méthode getScoreSystem existe")
    void testerMethodeGetScoreSystemExiste() throws Exception {
        var methode = GameWorld.class.getDeclaredMethod("getScoreSystem");
        assertNotNull(methode);
    }

    @Test
    @DisplayName("La méthode getTimer existe")
    void testerMethodeGetTimerExiste() throws Exception {
        var methode = GameWorld.class.getDeclaredMethod("getTimer");
        assertNotNull(methode);
    }

    @Test
    @DisplayName("La méthode getCurrentLevelNumber existe")
    void testerMethodeGetCurrentLevelNumberExiste() throws Exception {
        var methode = GameWorld.class.getDeclaredMethod("getCurrentLevelNumber");
        assertNotNull(methode);
        assertEquals(int.class, methode.getReturnType());
    }

    @Test
    @DisplayName("La méthode getTotalLevels existe")
    void testerMethodeGetTotalLevelsExiste() throws Exception {
        var methode = GameWorld.class.getDeclaredMethod("getTotalLevels");
        assertNotNull(methode);
        assertEquals(int.class, methode.getReturnType());
    }

    @Test
    @DisplayName("La méthode getEnemies existe")
    void testerMethodeGetEnemiesExiste() throws Exception {
        var methode = GameWorld.class.getDeclaredMethod("getEnemies");
        assertNotNull(methode);
    }

    @Test
    @DisplayName("La méthode setScreenManager existe")
    void testerMethodeSetScreenManagerExiste() throws Exception {
        var methode = GameWorld.class.getDeclaredMethod("setScreenManager",
                ScreenManager.class);
        assertNotNull(methode);
    }

    @Test
    @DisplayName("La méthode onNotify existe")
    void testerMethodeOnNotifyExiste() throws Exception {
        var methode = GameWorld.class.getDeclaredMethod("onNotify", GameEvent.class);
        assertNotNull(methode);
    }

    // ========== TESTS D'INTERFACES ==========

    @Test
    @DisplayName("GameWorld implémente Observer")
    void testerImplementeObserver() {
        assertTrue(system.Observer.class.isAssignableFrom(GameWorld.class));
    }

    // ========== TESTS DE CONSTRUCTEUR (sans l'appeler) ==========

    @Test
    @DisplayName("Le constructeur avec int existe")
    void testerConstructeurExiste() throws Exception {
        var constructeur = GameWorld.class.getDeclaredConstructor(int.class);
        assertNotNull(constructeur);
    }

    @Test
    @DisplayName("Le constructeur est public")
    void testerConstructeurPublic() throws Exception {
        var constructeur = GameWorld.class.getDeclaredConstructor(int.class);
        assertTrue(java.lang.reflect.Modifier.isPublic(constructeur.getModifiers()));
    }
}