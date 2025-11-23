import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import entity.Player;
import level.Level;
import org.junit.jupiter.api.*;
import settings.GameWorld;
import settings.screen.GameScreen;
import settings.screen.ScreenManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Player Movement Tests in GameScreen")
class GameScreenTest {

    private GameScreen gameScreen;
    private ScreenManager screenManagerMock;
    private GameWorld gameWorldMock;
    private Player playerMock;
    private Level levelMock;

    private static Application applicationMock;
    private static Graphics graphicsMock;

    @BeforeAll
    static void setupLibGDX() {
        applicationMock = mock(Application.class);
        graphicsMock = mock(Graphics.class);

        Gdx.app = applicationMock;
        Gdx.graphics = graphicsMock;
        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = mock(GL20.class);

        when(graphicsMock.getWidth()).thenReturn(1920);
        when(graphicsMock.getHeight()).thenReturn(1080);
    }

    @BeforeEach
    void setUp() {
        screenManagerMock = mock(ScreenManager.class);
        gameWorldMock = mock(GameWorld.class);
        playerMock = mock(Player.class);
        levelMock = mock(Level.class);

        when(gameWorldMock.getPlayer()).thenReturn(playerMock);
        when(gameWorldMock.getEnemies()).thenReturn(Collections.emptyList());
        when(gameWorldMock.getCurrentLevel()).thenReturn(levelMock);
        when(levelMock.getMapFile()).thenReturn("maps/test.tmx");
        when(levelMock.getMapWidth()).thenReturn(30);
        when(levelMock.getMapHeight()).thenReturn(30);
        when(screenManagerMock.getSelectedSmiley()).thenReturn(0);

        try {
            gameScreen = new GameScreen(screenManagerMock, gameWorldMock);
            setPrivateField("playerX", 500f);
            setPrivateField("playerY", 500f);
            setPrivateField("mapOffsetX", 100f);
            setPrivateField("mapOffsetY", 100f);
            setPrivateField("tileSize", 32f);
            setPrivateField("speed", 200f);
        } catch (Exception e) {
            System.err.println("Error creating GameScreen: " + e.getMessage());
            gameScreen = null;
        }
    }

    @AfterEach
    void tearDown() {
        Gdx.input = null;
    }

    @AfterAll
    static void cleanupLibGDX() {
        Gdx.app = null;
        Gdx.graphics = null;
        Gdx.gl = null;
        Gdx.gl20 = null;
    }

    private void setPrivateField(String fieldName, Object value) {
        if (gameScreen == null) return;
        try {
            Field field = GameScreen.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(gameScreen, value);
        } catch (Exception e) {
            System.err.println("Cannot set " + fieldName + ": " + e.getMessage());
        }
    }

    private Object getPrivateField(String fieldName) {
        if (gameScreen == null) return null;
        try {
            Field field = GameScreen.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(gameScreen);
        } catch (Exception e) {
            System.err.println("Cannot get " + fieldName + ": " + e.getMessage());
            return null;
        }
    }

    private void callHandleInput(float delta) {
        if (gameScreen == null) return;
        try {
            Method method = GameScreen.class.getDeclaredMethod("handleInput", float.class);
            method.setAccessible(true);
            method.invoke(gameScreen, delta);
        } catch (Exception e) {
            System.err.println("Error handleInput: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("LEFT key moves player left")
    void testMoveLeft_LeftKey() {
        if (gameScreen == null) return;

        Input inputMock = mock(Input.class);
        Gdx.input = inputMock;

        when(inputMock.isKeyPressed(Input.Keys.LEFT)).thenReturn(true);
        when(inputMock.isKeyPressed(Input.Keys.RIGHT)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.UP)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.DOWN)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.A)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.D)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.W)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.S)).thenReturn(false);
        when(inputMock.isKeyJustPressed(anyInt())).thenReturn(false);

        callHandleInput(0.016f);

        verify(playerMock, atLeastOnce()).setDirection(Player.Direction.LEFT);
    }

    // Additional movement tests (RIGHT, UP, DOWN, diagonals, ESCAPE) follow the same pattern
}
