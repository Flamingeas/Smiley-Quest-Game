package settings.screen;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour MenuScreen.
 * Ces tests vérifient l'état initial du menu sans lancer LibGDX.
 */
@DisplayName("Tests du MenuScreen")
class MenuScreenTest {

    private MenuScreen menuScreen;

    /**
     * Exécuté avant chaque test.
     * Crée une nouvelle instance de MenuScreen.
     */
    @BeforeEach
    void preparerLeTest() {
        menuScreen = new MenuScreen(null);
    }

    /**
     * Teste que le tableau des options du menu existe et contient 3 éléments.
     */
    @Test
    @DisplayName("Le menu doit avoir 3 options")
    void testerNombreOptionsMenu() throws Exception {
        Field champMenuItems = MenuScreen.class.getDeclaredField("menuItems");
        champMenuItems.setAccessible(true);
        String[] options = (String[]) champMenuItems.get(menuScreen);

        assertNotNull(options, "Le tableau des options ne doit pas être null");
        assertEquals(3, options.length, "Il doit y avoir exactement 3 options");
    }

    /**
     * Teste que les 3 options ont les bons textes.
     */
    @Test
    @DisplayName("Les options doivent être START, SETTINGS et EXIT")
    void testerTextesDesOptions() throws Exception {
        Field champMenuItems = MenuScreen.class.getDeclaredField("menuItems");
        champMenuItems.setAccessible(true);
        String[] options = (String[]) champMenuItems.get(menuScreen);

        assertEquals("START", options[0], "La première option doit être START");
        assertEquals("SETTINGS", options[1], "La deuxième option doit être SETTINGS");
        assertEquals("EXIT", options[2], "La troisième option doit être EXIT");
    }

    /**
     * Teste que l'index sélectionné au départ est 0 (première option).
     */
    @Test
    @DisplayName("Au démarrage, la première option doit être sélectionnée")
    void testerIndexInitial() throws Exception {
        Field champIndex = MenuScreen.class.getDeclaredField("selectedIndex");
        champIndex.setAccessible(true);
        int indexSelectionne = (int) champIndex.get(menuScreen);

        assertEquals(0, indexSelectionne,
                "Au démarrage, l'index doit être 0 (première option)");
    }

    /**
     * Teste que le titre du jeu est correct.
     */
    @Test
    @DisplayName("Le titre doit être 'Smiley Quest'")
    void testerTitreDuJeu() throws Exception {
        Field champTitre = MenuScreen.class.getDeclaredField("title");
        champTitre.setAccessible(true);
        String titre = (String) champTitre.get(menuScreen);

        assertEquals("Smiley Quest", titre, "Le titre doit être 'Smiley Quest'");
    }

    /**
     * Teste que l'espacement entre les options est bien de 70 pixels.
     */
    @Test
    @DisplayName("L'espacement entre les options doit être de 70 pixels")
    void testerEspacement() throws Exception {
        Field champEspacement = MenuScreen.class.getDeclaredField("spacing");
        champEspacement.setAccessible(true);
        float espacement = (float) champEspacement.get(menuScreen);

        assertEquals(70f, espacement, 0.01f,
                "L'espacement doit être de 70 pixels");
    }

    /**
     * Teste que le temps d'animation démarre à 0.
     */
    @Test
    @DisplayName("Le temps d'animation doit commencer à 0")
    void testerTempsAnimationInitial() throws Exception {
        Field champTemps = MenuScreen.class.getDeclaredField("stateTime");
        champTemps.setAccessible(true);
        float temps = (float) champTemps.get(menuScreen);

        assertEquals(0f, temps, 0.01f,
                "Le temps d'animation doit commencer à 0");
    }
}