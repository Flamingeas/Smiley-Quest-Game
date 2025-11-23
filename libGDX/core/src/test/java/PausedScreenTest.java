package settings.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import org.junit.jupiter.api.*;
import settings.GameState;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour PausedScreen avec bon coverage JaCoCo.
 */
@DisplayName("Tests du PausedScreen")
class PausedScreenTest {

    private PausedScreen pausedScreen;
    private ScreenManager screenManagerMock;

    @BeforeEach
    void preparerLeTest() {
        screenManagerMock = mock(ScreenManager.class);
        pausedScreen = new PausedScreen(screenManagerMock);
    }

    @AfterEach
    void nettoyerApresTest() {
        Gdx.input = null;
    }

    // ========== TESTS DU CONSTRUCTEUR ==========

    @Test
    @DisplayName("Le constructeur initialise screenManager")
    void testerConstructeur() throws Exception {
        Field champManager = PausedScreen.class.getDeclaredField("screenManager");
        champManager.setAccessible(true);
        ScreenManager manager = (ScreenManager) champManager.get(pausedScreen);

        assertSame(screenManagerMock, manager);
    }

    @Test
    @DisplayName("Le constructeur initialise batch à null")
    void testerConstructeurBatch() throws Exception {
        Field champBatch = PausedScreen.class.getDeclaredField("batch");
        champBatch.setAccessible(true);
        Object batch = champBatch.get(pausedScreen);

        assertNull(batch);
    }

    @Test
    @DisplayName("Le constructeur initialise fontTitle à null")
    void testerConstructeurFontTitle() throws Exception {
        Field champFont = PausedScreen.class.getDeclaredField("fontTitle");
        champFont.setAccessible(true);
        Object font = champFont.get(pausedScreen);

        assertNull(font);
    }

    @Test
    @DisplayName("Le constructeur initialise fontText à null")
    void testerConstructeurFontText() throws Exception {
        Field champFont = PausedScreen.class.getDeclaredField("fontText");
        champFont.setAccessible(true);
        Object font = champFont.get(pausedScreen);

        assertNull(font);
    }

    // ========== TESTS DE handleInput ==========

    @Test
    @DisplayName("handleInput avec touche J retourne au menu")
    void testerHandleInputToucheJ() throws Exception {
        Input inputMock = mock(Input.class);
        Gdx.input = inputMock;

        when(inputMock.isKeyJustPressed(74)).thenReturn(true);
        when(inputMock.isKeyJustPressed(anyInt())).thenReturn(false);
        when(inputMock.isKeyJustPressed(74)).thenReturn(true);

        Method methode = PausedScreen.class.getDeclaredMethod("handleInput");
        methode.setAccessible(true);
        methode.invoke(pausedScreen);

        verify(screenManagerMock, times(1)).setState(GameState.MENU);
    }

    @Test
    @DisplayName("handleInput avec touche R reprend le jeu")
    void testerHandleInputToucheR() throws Exception {
        Input inputMock = mock(Input.class);
        Gdx.input = inputMock;

        when(inputMock.isKeyJustPressed(Input.Keys.R)).thenReturn(true);
        when(inputMock.isKeyJustPressed(anyInt())).thenReturn(false);
        when(inputMock.isKeyJustPressed(Input.Keys.R)).thenReturn(true);

        Method methode = PausedScreen.class.getDeclaredMethod("handleInput");
        methode.setAccessible(true);
        methode.invoke(pausedScreen);

        verify(screenManagerMock, times(1)).resumeGame();
    }

    @Test
    @DisplayName("handleInput sans touche ne fait rien")
    void testerHandleInputSansTouche() throws Exception {
        Input inputMock = mock(Input.class);
        Gdx.input = inputMock;

        when(inputMock.isKeyJustPressed(anyInt())).thenReturn(false);

        Method methode = PausedScreen.class.getDeclaredMethod("handleInput");
        methode.setAccessible(true);
        methode.invoke(pausedScreen);

        verifyNoInteractions(screenManagerMock);
    }

    @Test
    @DisplayName("handleInput avec J et R en même temps")
    void testerHandleInputDeuxTouches() throws Exception {
        Input inputMock = mock(Input.class);
        Gdx.input = inputMock;

        when(inputMock.isKeyJustPressed(74)).thenReturn(true);
        when(inputMock.isKeyJustPressed(Input.Keys.R)).thenReturn(true);
        when(inputMock.isKeyJustPressed(anyInt())).thenReturn(false);
        when(inputMock.isKeyJustPressed(74)).thenReturn(true);
        when(inputMock.isKeyJustPressed(Input.Keys.R)).thenReturn(true);

        Method methode = PausedScreen.class.getDeclaredMethod("handleInput");
        methode.setAccessible(true);
        methode.invoke(pausedScreen);

        verify(screenManagerMock).setState(GameState.MENU);
        verify(screenManagerMock).resumeGame();
    }

    @Test
    @DisplayName("handleInput détecte plusieurs touches")
    void testerHandleInputPlusieursTouches() throws Exception {
        Input inputMock = mock(Input.class);
        Gdx.input = inputMock;

        // Simuler que plusieurs touches sont pressées (le for loop)
        when(inputMock.isKeyJustPressed(anyInt())).thenReturn(false);
        when(inputMock.isKeyJustPressed(74)).thenReturn(true);

        Method methode = PausedScreen.class.getDeclaredMethod("handleInput");
        methode.setAccessible(true);
        methode.invoke(pausedScreen);

        // Vérifier que la boucle s'est exécutée
        verify(inputMock, atLeast(1)).isKeyJustPressed(anyInt());
    }

    // ========== TESTS DU CYCLE DE VIE ==========

    @Test
    @DisplayName("resize ne plante pas")
    void testerResize() {
        assertDoesNotThrow(() -> pausedScreen.resize(1920, 1080));
        assertDoesNotThrow(() -> pausedScreen.resize(800, 600));
        assertDoesNotThrow(() -> pausedScreen.resize(0, 0));
    }

    @Test
    @DisplayName("pause ne plante pas")
    void testerPause() {
        assertDoesNotThrow(() -> pausedScreen.pause());
    }

    @Test
    @DisplayName("resume ne plante pas")
    void testerResume() {
        assertDoesNotThrow(() -> pausedScreen.resume());
    }

    @Test
    @DisplayName("hide ne plante pas")
    void testerHide() {
        assertDoesNotThrow(() -> pausedScreen.hide());
    }

    @Test
    @DisplayName("dispose ne plante pas sans show")
    void testerDispose() {
        assertDoesNotThrow(() -> pausedScreen.dispose());
    }

    @Test
    @DisplayName("dispose avec batch null")
    void testerDisposeAvecBatchNull() throws Exception {
        Field champBatch = PausedScreen.class.getDeclaredField("batch");
        champBatch.setAccessible(true);
        champBatch.set(pausedScreen, null);

        assertDoesNotThrow(() -> pausedScreen.dispose());
    }

    @Test
    @DisplayName("Cycle de vie complet")
    void testerCycleDeVieComplet() {
        assertDoesNotThrow(() -> {
            pausedScreen.resize(1920, 1080);
            pausedScreen.pause();
            pausedScreen.resume();
            pausedScreen.hide();
            pausedScreen.dispose();
        });
    }

    // ========== TESTS SUPPLÉMENTAIRES ==========

    @Test
    @DisplayName("Créer plusieurs instances")
    void testerPlusieursInstances() {
        PausedScreen screen1 = new PausedScreen(screenManagerMock);
        PausedScreen screen2 = new PausedScreen(screenManagerMock);
        PausedScreen screen3 = new PausedScreen(screenManagerMock);

        assertNotNull(screen1);
        assertNotNull(screen2);
        assertNotNull(screen3);
    }

    @Test
    @DisplayName("PausedScreen implémente Screen")
    void testerImplementeScreen() {
        assertTrue(com.badlogic.gdx.Screen.class.isAssignableFrom(PausedScreen.class));
    }

    @Test
    @DisplayName("Appeler handleInput plusieurs fois")
    void testerHandleInputPlusieursFois() throws Exception {
        Input inputMock = mock(Input.class);
        Gdx.input = inputMock;

        when(inputMock.isKeyJustPressed(anyInt())).thenReturn(false);

        Method methode = PausedScreen.class.getDeclaredMethod("handleInput");
        methode.setAccessible(true);

        // Appeler 5 fois
        for (int i = 0; i < 5; i++) {
            methode.invoke(pausedScreen);
        }

        // Pas d'erreur et pas d'interaction
        verifyNoInteractions(screenManagerMock);
    }
}