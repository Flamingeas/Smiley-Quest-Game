package settings.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import settings.GameState;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour PausedScreen.
 * Vérifie l'état initial et le comportement de navigation.
 */
@DisplayName("Tests du PausedScreen")
class PausedScreenTest {

    private PausedScreen pausedScreen;
    private ScreenManager screenManagerMock;

    /**
     * Prépare l'environnement de test avant chaque test.
     */
    @BeforeEach
    void preparerLeTest() {
        // Créer un mock du ScreenManager
        screenManagerMock = mock(ScreenManager.class);

        // Créer l'écran de pause
        pausedScreen = new PausedScreen(screenManagerMock);
    }

    /**
     * Teste que le ScreenManager est bien stocké.
     */
    @Test
    @DisplayName("Le ScreenManager ne doit pas être null")
    void testerScreenManagerNonNull() throws Exception {
        // Récupérer le champ privé screenManager
        Field champManager = PausedScreen.class.getDeclaredField("screenManager");
        champManager.setAccessible(true);
        ScreenManager manager = (ScreenManager) champManager.get(pausedScreen);

        // Vérifications
        assertNotNull(manager, "Le ScreenManager ne doit pas être null");
        assertSame(screenManagerMock, manager,
                "Le ScreenManager doit être celui passé au constructeur");
    }

    /**
     * Teste que le batch est null avant l'appel à show().
     */
    @Test
    @DisplayName("Le batch doit être null avant show()")
    void testerBatchNullAvantShow() throws Exception {
        // Récupérer le champ privé batch
        Field champBatch = PausedScreen.class.getDeclaredField("batch");
        champBatch.setAccessible(true);
        Object batch = champBatch.get(pausedScreen);

        // Vérification
        assertNull(batch, "Le batch doit être null avant l'appel à show()");
    }

    /**
     * Teste que fontTitle est null avant l'appel à show().
     */
    @Test
    @DisplayName("fontTitle doit être null avant show()")
    void testerFontTitleNullAvantShow() throws Exception {
        // Récupérer le champ privé fontTitle
        Field champFont = PausedScreen.class.getDeclaredField("fontTitle");
        champFont.setAccessible(true);
        Object font = champFont.get(pausedScreen);

        // Vérification
        assertNull(font, "fontTitle doit être null avant l'appel à show()");
    }

    /**
     * Teste que fontText est null avant l'appel à show().
     */
    @Test
    @DisplayName("fontText doit être null avant show()")
    void testerFontTextNullAvantShow() throws Exception {
        // Récupérer le champ privé fontText
        Field champFont = PausedScreen.class.getDeclaredField("fontText");
        champFont.setAccessible(true);
        Object font = champFont.get(pausedScreen);

        // Vérification
        assertNull(font, "fontText doit être null avant l'appel à show()");
    }

    /**
     * Teste que la méthode handleInput existe et est privée.
     */
    @Test
    @DisplayName("La méthode handleInput doit exister et être privée")
    void testerHandleInputExiste() throws Exception {
        // Récupérer la méthode privée handleInput
        Method methode = PausedScreen.class.getDeclaredMethod("handleInput");

        // Vérifications
        assertNotNull(methode, "La méthode handleInput doit exister");
        assertTrue(java.lang.reflect.Modifier.isPrivate(methode.getModifiers()),
                "handleInput doit être une méthode privée");
    }

    /**
     * Teste que handleInput appelle setState(MENU) quand la touche J (74) est pressée.
     */
    @Test
    @DisplayName("Presser la touche J doit retourner au menu")
    void testerRetourMenuAvecToucheJ() throws Exception {
        // Mock de Gdx.input
        Input inputMock = mock(Input.class);
        Gdx.input = inputMock;

        // Simuler : touche J (code 74) pressée
        when(inputMock.isKeyJustPressed(74)).thenReturn(true);
        when(inputMock.isKeyJustPressed(anyInt())).thenReturn(false);
        when(inputMock.isKeyJustPressed(74)).thenReturn(true);

        // Récupérer et appeler handleInput
        Method methode = PausedScreen.class.getDeclaredMethod("handleInput");
        methode.setAccessible(true);
        methode.invoke(pausedScreen);

        // Vérifier que setState(MENU) a été appelé
        verify(screenManagerMock).setState(GameState.MENU);
    }

    /**
     * Teste que handleInput appelle resumeGame() quand la touche R est pressée.
     */
    @Test
    @DisplayName("Presser la touche R doit reprendre le jeu")
    void testerReprendreJeuAvecToucheR() throws Exception {
        // Mock de Gdx.input
        Input inputMock = mock(Input.class);
        Gdx.input = inputMock;

        // Simuler : touche R pressée
        when(inputMock.isKeyJustPressed(Input.Keys.R)).thenReturn(true);
        when(inputMock.isKeyJustPressed(anyInt())).thenReturn(false);
        when(inputMock.isKeyJustPressed(Input.Keys.R)).thenReturn(true);

        // Récupérer et appeler handleInput
        Method methode = PausedScreen.class.getDeclaredMethod("handleInput");
        methode.setAccessible(true);
        methode.invoke(pausedScreen);

        // Vérifier que resumeGame() a été appelé
        verify(screenManagerMock).resumeGame();
    }

    /**
     * Teste que handleInput ne fait rien si aucune touche n'est pressée.
     */
    @Test
    @DisplayName("Ne rien presser ne doit rien déclencher")
    void testerAucuneTouchePressee() throws Exception {
        // Mock de Gdx.input
        Input inputMock = mock(Input.class);
        Gdx.input = inputMock;

        // Simuler : aucune touche pressée
        when(inputMock.isKeyJustPressed(anyInt())).thenReturn(false);

        // Récupérer et appeler handleInput
        Method methode = PausedScreen.class.getDeclaredMethod("handleInput");
        methode.setAccessible(true);
        methode.invoke(pausedScreen);

        // Vérifier qu'aucune méthode du ScreenManager n'a été appelée
        verifyNoInteractions(screenManagerMock);
    }

    /**
     * Teste que presser J et R en même temps ne provoque pas d'erreur.
     */
    @Test
    @DisplayName("Presser J et R simultanément doit appeler les deux actions")
    void testerDeuxTouchesSimultanees() throws Exception {
        // Mock de Gdx.input
        Input inputMock = mock(Input.class);
        Gdx.input = inputMock;

        // Simuler : J et R pressées en même temps
        when(inputMock.isKeyJustPressed(74)).thenReturn(true);
        when(inputMock.isKeyJustPressed(Input.Keys.R)).thenReturn(true);
        when(inputMock.isKeyJustPressed(anyInt())).thenReturn(false);
        when(inputMock.isKeyJustPressed(74)).thenReturn(true);
        when(inputMock.isKeyJustPressed(Input.Keys.R)).thenReturn(true);

        // Récupérer et appeler handleInput
        Method methode = PausedScreen.class.getDeclaredMethod("handleInput");
        methode.setAccessible(true);
        methode.invoke(pausedScreen);

        // Vérifier que les deux méthodes ont été appelées
        verify(screenManagerMock).setState(GameState.MENU);
        verify(screenManagerMock).resumeGame();
    }

    /**
     * Teste que dispose ne lance pas d'exception même si show() n'a pas été appelé.
     */
    @Test
    @DisplayName("dispose() ne doit pas planter même sans appeler show()")
    void testerDisposeSansShow() {
        // dispose() devrait gérer le cas où batch et fonts sont null
        assertDoesNotThrow(() -> pausedScreen.dispose(),
                "dispose() ne doit pas planter même si show() n'a pas été appelé");
    }

    /**
     * Teste que les méthodes du cycle de vie existent.
     */
    @Test
    @DisplayName("Les méthodes resize, pause, resume et hide doivent exister")
    void testerMethodesCycleDeVie() throws Exception {
        // Vérifier que ces méthodes existent
        assertNotNull(PausedScreen.class.getDeclaredMethod("resize", int.class, int.class),
                "La méthode resize doit exister");
        assertNotNull(PausedScreen.class.getDeclaredMethod("pause"),
                "La méthode pause doit exister");
        assertNotNull(PausedScreen.class.getDeclaredMethod("resume"),
                "La méthode resume doit exister");
        assertNotNull(PausedScreen.class.getDeclaredMethod("hide"),
                "La méthode hide doit exister");
    }

    /**
     * Teste que l'écran implémente bien l'interface Screen.
     */
    @Test
    @DisplayName("PausedScreen doit implémenter l'interface Screen")
    void testerImplementeScreen() {
        // Vérifier que PausedScreen implémente Screen
        assertTrue(com.badlogic.gdx.Screen.class.isAssignableFrom(PausedScreen.class),
                "PausedScreen doit implémenter l'interface Screen");
    }

    /**
     * Nettoie après les tests.
     */
    @org.junit.jupiter.api.AfterEach
    void nettoyerApresTest() {
        // Réinitialiser Gdx.input
        Gdx.input = null;
    }
}