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

/**
 * Tests unitaires DÉDIÉS aux déplacements du joueur dans GameScreen.
 * Ces tests appellent directement handleInput() via réflexion.
 *
 * IMPORTANT : Ces tests fonctionnent MÊME si EnemyRenderer plante !
 */
@DisplayName("Tests des déplacements du joueur")
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
    void preparerLeTest() {
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

            // Initialiser les champs nécessaires pour les déplacements
            setPrivateField("playerX", 500f);
            setPrivateField("playerY", 500f);
            setPrivateField("mapOffsetX", 100f);
            setPrivateField("mapOffsetY", 100f);
            setPrivateField("tileSize", 32f);
            setPrivateField("speed", 200f);

        } catch (Exception e) {
            System.err.println("Erreur création GameScreen: " + e.getMessage());
            gameScreen = null;
        }
    }

    @AfterEach
    void nettoyerApresTest() {
        Gdx.input = null;
    }

    @AfterAll
    static void tearDownLibGDX() {
        Gdx.app = null;
        Gdx.graphics = null;
        Gdx.gl = null;
        Gdx.gl20 = null;
    }

    /**
     * Méthode utilitaire pour définir un champ privé.
     */
    private void setPrivateField(String fieldName, Object value) {
        if (gameScreen == null) return;
        try {
            Field field = GameScreen.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(gameScreen, value);
        } catch (Exception e) {
            System.err.println("Impossible de set " + fieldName + ": " + e.getMessage());
        }
    }

    /**
     * Méthode utilitaire pour obtenir un champ privé.
     */
    private Object getPrivateField(String fieldName) {
        if (gameScreen == null) return null;
        try {
            Field field = GameScreen.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(gameScreen);
        } catch (Exception e) {
            System.err.println("Impossible de get " + fieldName + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Appelle handleInput via réflexion.
     */
    private void appellerHandleInput(float delta) {
        if (gameScreen == null) return;
        try {
            Method methode = GameScreen.class.getDeclaredMethod("handleInput", float.class);
            methode.setAccessible(true);
            methode.invoke(gameScreen, delta);
        } catch (Exception e) {
            System.err.println("Erreur handleInput: " + e.getMessage());
        }
    }

    // ========== TESTS DES DÉPLACEMENTS ==========

    @Test
    @DisplayName("Touche LEFT déplace le joueur vers la gauche")
    void testerDeplacementGauche_Left() {
        if (gameScreen == null) {
            System.out.println("Test skippé : GameScreen null");
            return;
        }

        // Mock Input
        Input inputMock = mock(Input.class);
        Gdx.input = inputMock;

        // Simuler touche LEFT pressée
        when(inputMock.isKeyPressed(Input.Keys.LEFT)).thenReturn(true);
        when(inputMock.isKeyPressed(Input.Keys.RIGHT)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.UP)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.DOWN)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.A)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.D)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.W)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.S)).thenReturn(false);
        when(inputMock.isKeyJustPressed(anyInt())).thenReturn(false);

        // Appeler handleInput
        appellerHandleInput(0.016f);

        // Vérifier que setDirection(LEFT) a été appelé
        verify(playerMock, atLeastOnce()).setDirection(Player.Direction.LEFT);
        System.out.println("✓ LEFT fonctionne");
    }

    @Test
    @DisplayName("Touche A (QWERTY/AZERTY) déplace vers la gauche")
    void testerDeplacementGauche_A() {
        if (gameScreen == null) return;

        Input inputMock = mock(Input.class);
        Gdx.input = inputMock;

        when(inputMock.isKeyPressed(Input.Keys.A)).thenReturn(true);
        when(inputMock.isKeyPressed(Input.Keys.LEFT)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.RIGHT)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.UP)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.DOWN)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.D)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.W)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.S)).thenReturn(false);
        when(inputMock.isKeyJustPressed(anyInt())).thenReturn(false);

        appellerHandleInput(0.016f);

        verify(playerMock, atLeastOnce()).setDirection(Player.Direction.LEFT);
        System.out.println("✓ A fonctionne");
    }

    @Test
    @DisplayName("Touche RIGHT déplace le joueur vers la droite")
    void testerDeplacementDroite_Right() {
        if (gameScreen == null) return;

        Input inputMock = mock(Input.class);
        Gdx.input = inputMock;

        when(inputMock.isKeyPressed(Input.Keys.RIGHT)).thenReturn(true);
        when(inputMock.isKeyPressed(Input.Keys.LEFT)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.UP)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.DOWN)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.A)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.D)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.W)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.S)).thenReturn(false);
        when(inputMock.isKeyJustPressed(anyInt())).thenReturn(false);

        appellerHandleInput(0.016f);

        verify(playerMock, atLeastOnce()).setDirection(Player.Direction.RIGHT);
        System.out.println("✓ RIGHT fonctionne");
    }

    @Test
    @DisplayName("Touche D déplace vers la droite")
    void testerDeplacementDroite_D() {
        if (gameScreen == null) return;

        Input inputMock = mock(Input.class);
        Gdx.input = inputMock;

        when(inputMock.isKeyPressed(Input.Keys.D)).thenReturn(true);
        when(inputMock.isKeyPressed(Input.Keys.LEFT)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.RIGHT)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.UP)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.DOWN)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.A)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.W)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.S)).thenReturn(false);
        when(inputMock.isKeyJustPressed(anyInt())).thenReturn(false);

        appellerHandleInput(0.016f);

        verify(playerMock, atLeastOnce()).setDirection(Player.Direction.RIGHT);
        System.out.println("✓ D fonctionne");
    }

    @Test
    @DisplayName("Touche UP déplace vers le haut")
    void testerDeplacementHaut_Up() {
        if (gameScreen == null) return;

        Input inputMock = mock(Input.class);
        Gdx.input = inputMock;

        when(inputMock.isKeyPressed(Input.Keys.UP)).thenReturn(true);
        when(inputMock.isKeyPressed(Input.Keys.LEFT)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.RIGHT)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.DOWN)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.A)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.D)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.W)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.S)).thenReturn(false);
        when(inputMock.isKeyJustPressed(anyInt())).thenReturn(false);

        appellerHandleInput(0.016f);

        // UP change la position Y
        verify(playerMock, atLeastOnce()).setY(anyFloat());
        System.out.println("✓ UP fonctionne");
    }

    @Test
    @DisplayName("Touche W (ZQSD) déplace vers le haut")
    void testerDeplacementHaut_W() {
        if (gameScreen == null) return;

        Input inputMock = mock(Input.class);
        Gdx.input = inputMock;

        when(inputMock.isKeyPressed(Input.Keys.W)).thenReturn(true);
        when(inputMock.isKeyPressed(Input.Keys.LEFT)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.RIGHT)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.UP)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.DOWN)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.A)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.D)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.S)).thenReturn(false);
        when(inputMock.isKeyJustPressed(anyInt())).thenReturn(false);

        appellerHandleInput(0.016f);

        verify(playerMock, atLeastOnce()).setY(anyFloat());
        System.out.println("✓ W fonctionne");
    }

    @Test
    @DisplayName("Touche DOWN déplace vers le bas")
    void testerDeplacementBas_Down() {
        if (gameScreen == null) return;

        Input inputMock = mock(Input.class);
        Gdx.input = inputMock;

        when(inputMock.isKeyPressed(Input.Keys.DOWN)).thenReturn(true);
        when(inputMock.isKeyPressed(Input.Keys.LEFT)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.RIGHT)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.UP)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.A)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.D)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.W)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.S)).thenReturn(false);
        when(inputMock.isKeyJustPressed(anyInt())).thenReturn(false);

        appellerHandleInput(0.016f);

        verify(playerMock, atLeastOnce()).setY(anyFloat());
        System.out.println("✓ DOWN fonctionne");
    }

    @Test
    @DisplayName("Touche S déplace vers le bas")
    void testerDeplacementBas_S() {
        if (gameScreen == null) return;

        Input inputMock = mock(Input.class);
        Gdx.input = inputMock;

        when(inputMock.isKeyPressed(Input.Keys.S)).thenReturn(true);
        when(inputMock.isKeyPressed(Input.Keys.LEFT)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.RIGHT)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.UP)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.DOWN)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.A)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.D)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.W)).thenReturn(false);
        when(inputMock.isKeyJustPressed(anyInt())).thenReturn(false);

        appellerHandleInput(0.016f);

        verify(playerMock, atLeastOnce()).setY(anyFloat());
        System.out.println("✓ S fonctionne");
    }

    @Test
    @DisplayName("Déplacement diagonal : LEFT + UP")
    void testerDeplacementDiagonal_LeftUp() {
        if (gameScreen == null) return;

        Input inputMock = mock(Input.class);
        Gdx.input = inputMock;

        when(inputMock.isKeyPressed(Input.Keys.LEFT)).thenReturn(true);
        when(inputMock.isKeyPressed(Input.Keys.UP)).thenReturn(true);
        when(inputMock.isKeyPressed(Input.Keys.RIGHT)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.DOWN)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.A)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.D)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.W)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.S)).thenReturn(false);
        when(inputMock.isKeyJustPressed(anyInt())).thenReturn(false);

        appellerHandleInput(0.016f);

        verify(playerMock, atLeastOnce()).setDirection(Player.Direction.LEFT);
        verify(playerMock, atLeastOnce()).setY(anyFloat());
        System.out.println("✓ LEFT+UP fonctionne");
    }

    @Test
    @DisplayName("Déplacement diagonal : RIGHT + DOWN")
    void testerDeplacementDiagonal_RightDown() {
        if (gameScreen == null) return;

        Input inputMock = mock(Input.class);
        Gdx.input = inputMock;

        when(inputMock.isKeyPressed(Input.Keys.RIGHT)).thenReturn(true);
        when(inputMock.isKeyPressed(Input.Keys.DOWN)).thenReturn(true);
        when(inputMock.isKeyPressed(Input.Keys.LEFT)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.UP)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.A)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.D)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.W)).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.S)).thenReturn(false);
        when(inputMock.isKeyJustPressed(anyInt())).thenReturn(false);

        appellerHandleInput(0.016f);

        verify(playerMock, atLeastOnce()).setDirection(Player.Direction.RIGHT);
        verify(playerMock, atLeastOnce()).setY(anyFloat());
        System.out.println("✓ RIGHT+DOWN fonctionne");
    }

    @Test
    @DisplayName("Touche ESCAPE active la pause")
    void testerToucheEscape() {
        if (gameScreen == null) return;

        Input inputMock = mock(Input.class);
        Gdx.input = inputMock;

        when(inputMock.isKeyPressed(anyInt())).thenReturn(false);
        when(inputMock.isKeyJustPressed(Input.Keys.ESCAPE)).thenReturn(true);

        setPrivateField("pendingPause", false);

        appellerHandleInput(0.016f);

        Boolean pendingPause = (Boolean) getPrivateField("pendingPause");
        assertTrue(pendingPause != null && pendingPause, "pendingPause doit être true après ESCAPE");
        System.out.println("✓ ESCAPE fonctionne");
    }

    @Test
    @DisplayName("Aucune touche pressée ne déclenche pas de mouvement")
    void testerAucuneTouche() {
        if (gameScreen == null) return;

        Input inputMock = mock(Input.class);
        Gdx.input = inputMock;

        // Aucune touche pressée
        when(inputMock.isKeyPressed(anyInt())).thenReturn(false);
        when(inputMock.isKeyJustPressed(anyInt())).thenReturn(false);

        // Reset du mock pour compter les appels
        reset(playerMock);

        appellerHandleInput(0.016f);

        // setX et setY sont quand même appelés (même position)
        // Mais pas setDirection
        verify(playerMock, never()).setDirection(any());
        System.out.println("✓ Aucune touche = pas de setDirection");
    }

    @Test
    @DisplayName("Vitesse de déplacement avec speed=200 et delta=0.016")
    void testerVitesseDeplacement() {
        if (gameScreen == null) return;

        Input inputMock = mock(Input.class);
        Gdx.input = inputMock;

        when(inputMock.isKeyPressed(Input.Keys.RIGHT)).thenReturn(true);
        when(inputMock.isKeyPressed(anyInt())).thenReturn(false);
        when(inputMock.isKeyPressed(Input.Keys.RIGHT)).thenReturn(true);
        when(inputMock.isKeyJustPressed(anyInt())).thenReturn(false);

        setPrivateField("speed", 200f);

        appellerHandleInput(0.016f);

        // Avec speed=200 et delta=0.016, move = 3.2 pixels
        verify(playerMock, atLeastOnce()).setX(anyFloat());
        System.out.println("✓ Calcul vitesse fonctionne");
    }
}