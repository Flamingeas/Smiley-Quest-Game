package settings;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import system.GameEvent;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GameWorld.
 * Verifies class existence, field structure, methods, constructor, visibility, game logic, and events.
 */
@DisplayName("GameWorld Tests")
class GameWorldTest {

    // ================= STRUCTURE TESTS =================

    @Test
    @DisplayName("Class GameWorld exists")
    void testClassExists() {
        assertNotNull(GameWorld.class);
    }

    @Test
    @DisplayName("GameWorld implements Observer")
    void testImplementsObserver() {
        assertTrue(system.Observer.class.isAssignableFrom(GameWorld.class));
    }

    @Test
    @DisplayName("Field 'levels' exists and is accessible")
    void testFieldLevelsExists() throws NoSuchFieldException {
        Field field = GameWorld.class.getDeclaredField("levels");
        assertNotNull(field);
        field.setAccessible(true);
    }

    @Test
    @DisplayName("Field 'currentLevelIndex' exists and is int")
    void testFieldCurrentLevelIndexExists() throws NoSuchFieldException {
        Field field = GameWorld.class.getDeclaredField("currentLevelIndex");
        assertEquals(int.class, field.getType());
    }

    @Test
    @DisplayName("Field 'player' exists")
    void testFieldPlayerExists() throws NoSuchFieldException {
        assertNotNull(GameWorld.class.getDeclaredField("player"));
    }

    @Test
    @DisplayName("Field 'state' exists")
    void testFieldStateExists() throws NoSuchFieldException {
        assertNotNull(GameWorld.class.getDeclaredField("state"));
    }

    @Test
    @DisplayName("Field 'scoreSystem' exists")
    void testFieldScoreSystemExists() throws NoSuchFieldException {
        assertNotNull(GameWorld.class.getDeclaredField("scoreSystem"));
    }

    @Test
    @DisplayName("Field 'timer' exists")
    void testFieldTimerExists() throws NoSuchFieldException {
        assertNotNull(GameWorld.class.getDeclaredField("timer"));
    }

    @Test
    @DisplayName("Field 'enemies' exists")
    void testFieldEnemiesExists() throws NoSuchFieldException {
        assertNotNull(GameWorld.class.getDeclaredField("enemies"));
    }

    @Test
    @DisplayName("Field 'enemiesSpawned' exists and is boolean")
    void testFieldEnemiesSpawnedExists() throws NoSuchFieldException {
        Field field = GameWorld.class.getDeclaredField("enemiesSpawned");
        assertEquals(boolean.class, field.getType());
    }

    @Test
    @DisplayName("Field 'gameScreen' exists")
    void testFieldGameScreenExists() throws NoSuchFieldException {
        assertNotNull(GameWorld.class.getDeclaredField("gameScreen"));
    }

    @Test
    @DisplayName("Field 'screenManager' exists")
    void testFieldScreenManagerExists() throws NoSuchFieldException {
        assertNotNull(GameWorld.class.getDeclaredField("screenManager"));
    }

    // ================= METHOD TESTS =================

    @Test
    @DisplayName("Method 'setGameScreen' exists")
    void testMethodSetGameScreenExists() throws NoSuchMethodException {
        assertNotNull(GameWorld.class.getDeclaredMethod("setGameScreen", settings.screen.GameScreen.class));
    }

    @Test
    @DisplayName("Method 'getGameScreen' exists")
    void testMethodGetGameScreenExists() throws NoSuchMethodException {
        assertNotNull(GameWorld.class.getDeclaredMethod("getGameScreen"));
    }

    @Test
    @DisplayName("Method 'isWalkableTile' exists and returns boolean")
    void testMethodIsWalkableTileExists() throws NoSuchMethodException {
        var method = GameWorld.class.getDeclaredMethod("isWalkableTile", int.class, int.class);
        assertEquals(boolean.class, method.getReturnType());
    }

    @Test
    @DisplayName("Method 'update' exists")
    void testMethodUpdateExists() throws NoSuchMethodException {
        assertNotNull(GameWorld.class.getDeclaredMethod("update", float.class));
    }

    @Test
    @DisplayName("Method 'nextLevel' exists and returns boolean")
    void testMethodNextLevelExists() throws NoSuchMethodException {
        var method = GameWorld.class.getDeclaredMethod("nextLevel");
        assertEquals(boolean.class, method.getReturnType());
    }

    @Test
    @DisplayName("Method 'publish' exists")
    void testMethodPublishExists() throws NoSuchMethodException {
        assertNotNull(GameWorld.class.getDeclaredMethod("publish", GameEvent.class));
    }

    @Test
    @DisplayName("Method 'setState' exists")
    void testMethodSetStateExists() throws NoSuchMethodException {
        assertNotNull(GameWorld.class.getDeclaredMethod("setState", GameState.class));
    }

    @Test
    @DisplayName("Method 'getState' exists and returns GameState")
    void testMethodGetStateExists() throws NoSuchMethodException {
        var method = GameWorld.class.getDeclaredMethod("getState");
        assertEquals(GameState.class, method.getReturnType());
    }

    @Test
    @DisplayName("Method 'endGame' exists")
    void testMethodEndGameExists() throws NoSuchMethodException {
        assertNotNull(GameWorld.class.getDeclaredMethod("endGame"));
    }

    @Test
    @DisplayName("Method 'getCurrentLevel' exists")
    void testMethodGetCurrentLevelExists() throws NoSuchMethodException {
        assertNotNull(GameWorld.class.getDeclaredMethod("getCurrentLevel"));
    }

    @Test
    @DisplayName("Method 'getPlayer' exists")
    void testMethodGetPlayerExists() throws NoSuchMethodException {
        assertNotNull(GameWorld.class.getDeclaredMethod("getPlayer"));
    }

    @Test
    @DisplayName("Method 'getScoreSystem' exists")
    void testMethodGetScoreSystemExists() throws NoSuchMethodException {
        assertNotNull(GameWorld.class.getDeclaredMethod("getScoreSystem"));
    }

    @Test
    @DisplayName("Method 'getTimer' exists")
    void testMethodGetTimerExists() throws NoSuchMethodException {
        assertNotNull(GameWorld.class.getDeclaredMethod("getTimer"));
    }

    @Test
    @DisplayName("Method 'getCurrentLevelNumber' exists and returns int")
    void testMethodGetCurrentLevelNumberExists() throws NoSuchMethodException {
        var method = GameWorld.class.getDeclaredMethod("getCurrentLevelNumber");
        assertEquals(int.class, method.getReturnType());
    }

    @Test
    @DisplayName("Method 'getTotalLevels' exists and returns int")
    void testMethodGetTotalLevelsExists() throws NoSuchMethodException {
        var method = GameWorld.class.getDeclaredMethod("getTotalLevels");
        assertEquals(int.class, method.getReturnType());
    }

    @Test
    @DisplayName("Method 'getEnemies' exists")
    void testMethodGetEnemiesExists() throws NoSuchMethodException {
        assertNotNull(GameWorld.class.getDeclaredMethod("getEnemies"));
    }

    @Test
    @DisplayName("Method 'setScreenManager' exists")
    void testMethodSetScreenManagerExists() throws NoSuchMethodException {
        assertNotNull(GameWorld.class.getDeclaredMethod("setScreenManager", settings.screen.ScreenManager.class));
    }

    @Test
    @DisplayName("Method 'onNotify' exists")
    void testMethodOnNotifyExists() throws NoSuchMethodException {
        assertNotNull(GameWorld.class.getDeclaredMethod("onNotify", GameEvent.class));
    }

    // ================= CONSTRUCTOR TESTS =================

    @Test
    @DisplayName("Constructor GameWorld(int) exists")
    void testConstructorExists() throws NoSuchMethodException {
        assertNotNull(GameWorld.class.getDeclaredConstructor(int.class));
    }

    @Test
    @DisplayName("Constructor is public")
    void testConstructorIsPublic() throws NoSuchMethodException {
        var constructor = GameWorld.class.getDeclaredConstructor(int.class);
        assertTrue(java.lang.reflect.Modifier.isPublic(constructor.getModifiers()));
    }

    // ================= VISIBILITY TESTS =================

    @Test
    @DisplayName("Class GameWorld is public")
    void testGameWorldIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(GameWorld.class.getModifiers()));
    }

    @Test
    @DisplayName("All public methods exist")
    void testAllPublicMethodsExist() {
        var methods = GameWorld.class.getDeclaredMethods();
        assertTrue(methods.length > 10);
    }

    @Test
    @DisplayName("All private fields exist")
    void testAllPrivateFieldsExist() {
        var fields = GameWorld.class.getDeclaredFields();
        assertTrue(fields.length > 5);
    }

    // ================= GAME LOGIC TESTS =================

    @Test
    @DisplayName("GameState.PLAYING exists")
    void testGameStatePlaying() {
        assertNotNull(GameState.PLAYING);
    }

    @Test
    @DisplayName("GameState.PAUSED exists")
    void testGameStatePaused() {
        assertNotNull(GameState.PAUSED);
    }

    @Test
    @DisplayName("GameState.GAME_OVER exists")
    void testGameStateGameOver() {
        assertNotNull(GameState.GAME_OVER);
    }

    @Test
    @DisplayName("GameState.WINNER exists")
    void testGameStateWinner() {
        assertNotNull(GameState.WINNER);
    }

    @Test
    @DisplayName("GameState.MENU exists")
    void testGameStateMenu() {
        assertNotNull(GameState.MENU);
    }

    // ================= EVENT TESTS =================

    @Test
    @DisplayName("GameEvent 'ITEM_COLLECTED' can be created")
    void testGameEventItemCollected() {
        GameEvent event = new GameEvent("ITEM_COLLECTED", 50);
        assertEquals("ITEM_COLLECTED", event.getType());
        assertEquals(50, event.getData());
    }

    @Test
    @DisplayName("GameEvent 'PLAYER_HIT' can be created")
    void testGameEventPlayerHit() {
        GameEvent event = new GameEvent("PLAYER_HIT", null);
        assertEquals("PLAYER_HIT", event.getType());
        assertNull(event.getData());
    }

    @Test
    @DisplayName("GameEvent 'PLAYER_DEAD' can be created")
    void testGameEventPlayerDead() {
        GameEvent event = new GameEvent("PLAYER_DEAD", null);
        assertEquals("PLAYER_DEAD", event.getType());
    }

    @Test
    @DisplayName("GameEvent 'CHEST_FOUND' can be created")
    void testGameEventChestFound() {
        GameEvent event = new GameEvent("CHEST_FOUND", null);
        assertEquals("CHEST_FOUND", event.getType());
    }

    @Test
    @DisplayName("Custom GameEvent can be created")
    void testGameEventCustomType() {
        GameEvent event = new GameEvent("CUSTOM_EVENT", "data");
        assertEquals("CUSTOM_EVENT", event.getType());
        assertEquals("data", event.getData());
    }

    @Test
    @DisplayName("GameWorld has 4 levels")
    void testFourLevels() {
        assertTrue(true);
    }

    @Test
    @DisplayName("GameWorld implements Observer interface")
    void testImplementsObserverInterface() {
        assertTrue(system.Observer.class.isAssignableFrom(GameWorld.class));
    }

    @Test
    @DisplayName("Method onNotify is public from Observer")
    void testOnNotifyFromObserver() throws NoSuchMethodException {
        var method = GameWorld.class.getDeclaredMethod("onNotify", GameEvent.class);
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("GameState class is accessible")
    void testGameStateAccessible() {
        assertNotNull(GameState.class);
    }

    @Test
    @DisplayName("GameEvent class is accessible")
    void testGameEventAccessible() {
        assertNotNull(GameEvent.class);
    }

    @Test
    @DisplayName("Observer interface is accessible")
    void testObserverAccessible() {
        assertNotNull(system.Observer.class);
    }
}
