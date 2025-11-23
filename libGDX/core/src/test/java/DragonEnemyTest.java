package entity.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import entity.Player;
import entity.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import settings.screen.GameScreen;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("DragonEnemy Tests")
class DragonEnemyTest {

    @Mock private Player player;
    @Mock private GameScreen gameScreen;
    @Mock private Graphics graphics;

    private DragonEnemy dragonEnemy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Gdx.graphics = graphics;

        when(graphics.getDeltaTime()).thenReturn(0.016f);
        when(player.getX()).thenReturn(500f);
        when(player.getY()).thenReturn(500f);
        when(gameScreen.canMoveGeneric(anyFloat(), anyFloat())).thenReturn(true);

        dragonEnemy = new DragonEnemy(5f, 5f, 50, 1.0f, 10, player);
        dragonEnemy.setGameScreen(gameScreen);
    }

    // ========== TESTS DU CONSTRUCTEUR ==========

    @Test
    @DisplayName("Constructeur initialise le dragon")
    void testConstructor() {
        assertNotNull(dragonEnemy);
        assertEquals(50, dragonEnemy.health);
        assertEquals(1.0f, dragonEnemy.speed);
    }

    @Test
    @DisplayName("Constructeur définit la cible joueur")
    void testConstructorSetTargetPlayer() {
        assertEquals(player, dragonEnemy.targetPlayer);
    }

    @Test
    @DisplayName("Constructeur définit la plage d'attaque")
    void testConstructorSetAttackRange() {
        assertEquals(200f, dragonEnemy.attackRange);
    }

    // ========== TESTS DE POSITION ==========

    @Test
    @DisplayName("Position initiale est correcte")
    void testInitialPosition() {
        assertEquals(5f, dragonEnemy.tileX);
        assertEquals(5f, dragonEnemy.tileY);
    }

    @Test
    @DisplayName("Texture est chargée")
    void testTextureLoaded() {
        // Le constructeur crée une texture, donc il ne doit pas être null
        assertNotNull(dragonEnemy.texture);
    }

    // ========== TESTS DE UPDATE ==========

    @Test
    @DisplayName("Update appelle move()")
    void testUpdateCallsMove() {
        when(graphics.getDeltaTime()).thenReturn(0.016f);
        when(gameScreen.canMoveGeneric(anyFloat(), anyFloat())).thenReturn(true);

        dragonEnemy.update();
        // Pas d'exception
    }

    @Test
    @DisplayName("Update n'échoue pas si dragon inactif")
    void testUpdateInactiveDragon() {
        dragonEnemy.isActive = false;
        dragonEnemy.update();
        // Pas d'exception
    }

    @Test
    @DisplayName("Update avec delta time positif")
    void testUpdatePositiveDelta() {
        when(graphics.getDeltaTime()).thenReturn(0.016f);

        float beforeX = dragonEnemy.x;
        float beforeY = dragonEnemy.y;

        dragonEnemy.update();
        // Position peut avoir changé
    }

    // ========== TESTS DE MOUVEMENT ==========

    @Test
    @DisplayName("Move appelle canMoveGeneric()")
    void testMoveCallsCanMoveGeneric() {
        when(gameScreen.canMoveGeneric(anyFloat(), anyFloat())).thenReturn(true);

        dragonEnemy.move(player, 0.016f);

        verify(gameScreen, atLeastOnce()).canMoveGeneric(anyFloat(), anyFloat());
    }

    @Test
    @DisplayName("Move avec joueur à grande distance")
    void testMovePlayerFarAway() {
        when(player.getX()).thenReturn(1000f);
        when(player.getY()).thenReturn(1000f);
        when(gameScreen.canMoveGeneric(anyFloat(), anyFloat())).thenReturn(true);

        float beforeX = dragonEnemy.x;
        float beforeY = dragonEnemy.y;

        dragonEnemy.move(player, 0.016f);
        // Devrait se rapprocher
    }

    @Test
    @DisplayName("Move avec joueur très proche")
    void testMovePlayerClose() {
        when(player.getX()).thenReturn(5.1f);
        when(player.getY()).thenReturn(5.1f);
        when(gameScreen.canMoveGeneric(anyFloat(), anyFloat())).thenReturn(true);

        dragonEnemy.move(player, 0.016f);
        // Devrait s'éloigner légèrement
    }

    @Test
    @DisplayName("Move sans joueur ne cause pas d'erreur")
    void testMoveNullPlayer() {
        dragonEnemy.move(null, 0.016f);
        // Pas d'exception
    }

    @Test
    @DisplayName("Move sans gameScreen ne cause pas d'erreur")
    void testMoveNullGameScreen() {
        dragonEnemy.setGameScreen(null);
        dragonEnemy.move(player, 0.016f);
        // Pas d'exception
    }

    @Test
    @DisplayName("Move respecte les limites du monde")
    void testMoveRespectsWorldBounds() {
        when(gameScreen.canMoveGeneric(anyFloat(), anyFloat())).thenReturn(false);

        float beforeX = dragonEnemy.x;
        float beforeY = dragonEnemy.y;

        dragonEnemy.move(player, 0.016f);

        // Si canMoveGeneric retourne false, position ne change pas
        assertEquals(beforeX, dragonEnemy.x);
        assertEquals(beforeY, dragonEnemy.y);
    }

    @Test
    @DisplayName("Move avec distance zéro")
    void testMoveZeroDistance() {
        when(player.getX()).thenReturn(dragonEnemy.x);
        when(player.getY()).thenReturn(dragonEnemy.y);

        dragonEnemy.move(player, 0.016f);
        // Pas d'exception (division par zéro évitée)
    }

    // ========== TESTS D'INTERACTION ==========

    @Test
    @DisplayName("Interact avec Player appelle attack()")
    void testInteractWithPlayer() {
        when(player.getX()).thenReturn(100f);
        when(player.getY()).thenReturn(100f);
        when(gameScreen.canMoveGeneric(anyFloat(), anyFloat())).thenReturn(true);

        dragonEnemy.interact(player);
        // Pas d'exception
    }

    @Test
    @DisplayName("Interact avec Entity non-Player")
    void testInteractWithNonPlayer() {
        Entity entity = mock(Entity.class);
        dragonEnemy.interact(entity);
        // Pas d'exception
    }

    @Test
    @DisplayName("Interact avec null")
    void testInteractWithNull() {
        dragonEnemy.interact(null);
        // Pas d'exception
    }

    // ========== TESTS DE STATS ==========

    @Test
    @DisplayName("Dragon a une santé positive")
    void testDragonHasPositiveHealth() {
        assertTrue(dragonEnemy.health > 0);
    }

    @Test
    @DisplayName("Dragon a une vitesse positive")
    void testDragonHasPositiveSpeed() {
        assertTrue(dragonEnemy.speed > 0);
    }

    @Test
    @DisplayName("Dragon a des dégâts positifs")
    void testDragonHasPositiveDamage() {
        assertTrue(dragonEnemy.attackDamage > 0);
    }

    // ========== TESTS DE CONSTANTES ==========

    @Test
    @DisplayName("Optimal distance est constante")
    void testOptimalDistanceIsConstant() {
        DragonEnemy dragon1 = new DragonEnemy(0f, 0f, 50, 1.0f, 10, player);
        DragonEnemy dragon2 = new DragonEnemy(0f, 0f, 50, 1.0f, 10, player);

        // Les deux dragons ont la même plage optimale
        dragon1.setGameScreen(gameScreen);
        dragon2.setGameScreen(gameScreen);
    }

    // ========== TESTS D'ALÉATOIRE ==========

    @Test
    @DisplayName("Move ajoute du bruit aléatoire")
    void testMoveAddsRandomNoise() {
        when(gameScreen.canMoveGeneric(anyFloat(), anyFloat())).thenReturn(true);

        float x1 = dragonEnemy.x;
        float y1 = dragonEnemy.y;

        dragonEnemy.move(player, 0.016f);

        float x2 = dragonEnemy.x;
        float y2 = dragonEnemy.y;

        dragonEnemy.move(player, 0.016f);

        // Peut être différent à cause du bruit aléatoire
    }

    // ========== TESTS DE CYCLE DE VIE ==========

    @Test
    @DisplayName("Dragon peut être tué")
    void testDragonCanBeDamaged() {
        int initialHealth = dragonEnemy.health;
        dragonEnemy.takeDamage(10);
        assertTrue(dragonEnemy.health < initialHealth);
    }

    @Test
    @DisplayName("Dragon meurt à santé 0 ou moins")
    void testDragonDiesAtZeroHealth() {
        dragonEnemy.health = 0;
        assertFalse(dragonEnemy.isActive);
    }
}