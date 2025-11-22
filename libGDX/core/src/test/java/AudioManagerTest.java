package system;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour AudioManager.
 * Utilise des objets simulés (mocks) pour remplacer LibGDX.
 */
@DisplayName("Tests du AudioManager")
class AudioManagerTest {

    private AudioManager audioManager;
    private Music musiqueMock;
    private Preferences preferencesMock;

    /**
     * Prépare l'environnement de test avant chaque test.
     * Simule les composants LibGDX nécessaires.
     */
    @BeforeEach
    void preparerLeTest() {
        // Réinitialiser le singleton avant chaque test
        reinitialiserSingleton();

        // Créer des objets simulés pour LibGDX
        musiqueMock = mock(Music.class);
        preferencesMock = mock(Preferences.class);

        Application applicationMock = mock(Application.class);
        Audio audioMock = mock(Audio.class);
        Files filesMock = mock(Files.class);

        // Configurer Gdx (classe statique de LibGDX)
        Gdx.app = applicationMock;
        Gdx.audio = audioMock;
        Gdx.files = filesMock;

        // Définir le comportement des objets simulés
        when(applicationMock.getPreferences("SmileyQuestSettings")).thenReturn(preferencesMock);
        when(preferencesMock.getFloat("volume", 0.5f)).thenReturn(0.5f);
        when(preferencesMock.getBoolean("sound", true)).thenReturn(true);
        when(audioMock.newMusic(any())).thenReturn(musiqueMock);

        // Créer l'instance d'AudioManager
        audioManager = AudioManager.getInstance();
    }

    /**
     * Nettoie après chaque test.
     */
    @AfterEach
    void nettoyerApresTest() {
        reinitialiserSingleton();
        Gdx.app = null;
        Gdx.audio = null;
        Gdx.files = null;
    }

    /**
     * Réinitialise le singleton AudioManager pour les tests.
     */
    private void reinitialiserSingleton() {
        try {
            Field champInstance = AudioManager.class.getDeclaredField("instance");
            champInstance.setAccessible(true);
            champInstance.set(null, null);
        } catch (Exception e) {
            fail("Impossible de réinitialiser le singleton: " + e.getMessage());
        }
    }

    /**
     * Teste que getInstance retourne toujours la même instance (pattern Singleton).
     */
    @Test
    @DisplayName("getInstance doit toujours retourner la même instance")
    void testerSingleton() {
        AudioManager instance1 = AudioManager.getInstance();
        AudioManager instance2 = AudioManager.getInstance();

        assertSame(instance1, instance2,
                "getInstance doit retourner la même instance à chaque appel");
    }

    /**
     * Teste que la musique est configurée en boucle au démarrage.
     */
    @Test
    @DisplayName("La musique doit être configurée en boucle")
    void testerMusiqueEnBoucle() {
        // Vérifier que setLooping(true) a été appelé
        verify(musiqueMock, times(1)).setLooping(true);
    }

    /**
     * Teste que le volume initial est bien récupéré des préférences.
     */
    @Test
    @DisplayName("Le volume initial doit être récupéré des préférences")
    void testerVolumeInitial() {
        // Vérifier que setVolume a été appelé avec la valeur des préférences
        verify(musiqueMock, times(1)).setVolume(0.5f);
    }

    /**
     * Teste que la musique démarre automatiquement si le son est activé.
     */
    @Test
    @DisplayName("La musique doit démarrer si le son est activé dans les préférences")
    void testerDemarrageMusiqueAvecSonActive() {
        // Vérifier que play() a été appelé
        verify(musiqueMock, times(1)).play();
    }

    /**
     * Teste que la musique ne démarre pas si le son est désactivé.
     */
    @Test
    @DisplayName("La musique ne doit pas démarrer si le son est désactivé")
    void testerPasDeDemarrageSiSonDesactive() {
        // Réinitialiser le singleton
        reinitialiserSingleton();

        // Créer un nouveau mock de musique pour ce test
        Music nouvelleMusiqueeMock = mock(Music.class);
        Preferences nouvellesPreferencesMock = mock(Preferences.class);

        // Configurer : son désactivé
        when(nouvellesPreferencesMock.getFloat("volume", 0.5f)).thenReturn(0.5f);
        when(nouvellesPreferencesMock.getBoolean("sound", true)).thenReturn(false);
        when(Gdx.app.getPreferences("SmileyQuestSettings")).thenReturn(nouvellesPreferencesMock);
        when(Gdx.audio.newMusic(any())).thenReturn(nouvelleMusiqueeMock);

        // Créer une nouvelle instance
        AudioManager manager = AudioManager.getInstance();

        // Vérifier que play() n'a jamais été appelé sur ce nouveau mock
        verify(nouvelleMusiqueeMock, never()).play();
    }

    /**
     * Teste que setVolume change bien le volume de la musique.
     */
    @Test
    @DisplayName("setVolume doit changer le volume de la musique")
    void testerChangementVolume() {
        // Changer le volume
        audioManager.setVolume(0.8f);

        // Vérifier que setVolume a été appelé sur la musique
        // times(2) car appelé 1 fois au démarrage + 1 fois par notre appel
        verify(musiqueMock, times(2)).setVolume(anyFloat());
        verify(musiqueMock).setVolume(0.8f);
    }

    /**
     * Teste que setVolume sauvegarde le volume dans les préférences.
     */
    @Test
    @DisplayName("setVolume doit sauvegarder le volume dans les préférences")
    void testerSauvegardeVolume() {
        // Changer le volume
        audioManager.setVolume(0.7f);

        // Vérifier que les préférences sont mises à jour
        verify(preferencesMock).putFloat("volume", 0.7f);
        verify(preferencesMock).flush();
    }

    /**
     * Teste que setVolume limite le volume entre 0 et 1.
     */
    @Test
    @DisplayName("setVolume doit limiter le volume entre 0 et 1")
    void testerLimitesVolume() {
        // Tester volume trop élevé
        audioManager.setVolume(1.5f);
        verify(musiqueMock).setVolume(1.0f);

        // Tester volume négatif
        audioManager.setVolume(-0.5f);
        verify(musiqueMock).setVolume(0.0f);
    }

    /**
     * Teste que setSoundEnabled(true) lance la musique si elle est arrêtée.
     */
    @Test
    @DisplayName("setSoundEnabled(true) doit lancer la musique")
    void testerActivationSon() {
        // Simuler : la musique n'est pas en cours de lecture
        when(musiqueMock.isPlaying()).thenReturn(false);

        // Activer le son
        audioManager.setSoundEnabled(true);

        // Vérifier que play() a été appelé (2 fois : démarrage + notre appel)
        verify(musiqueMock, times(2)).play();
    }

    /**
     * Teste que setSoundEnabled(false) met en pause la musique.
     */
    @Test
    @DisplayName("setSoundEnabled(false) doit mettre en pause la musique")
    void testerDesactivationSon() {
        // Simuler : la musique est en cours de lecture
        when(musiqueMock.isPlaying()).thenReturn(true);

        // Désactiver le son
        audioManager.setSoundEnabled(false);

        // Vérifier que pause() a été appelé
        verify(musiqueMock).pause();
    }

    /**
     * Teste que setSoundEnabled sauvegarde l'état dans les préférences.
     */
    @Test
    @DisplayName("setSoundEnabled doit sauvegarder l'état dans les préférences")
    void testerSauvegardeEtatSon() {
        // Désactiver le son
        audioManager.setSoundEnabled(false);

        // Vérifier que les préférences sont mises à jour
        verify(preferencesMock).putBoolean("sound", false);
        verify(preferencesMock, atLeastOnce()).flush();
    }

    /**
     * Teste que dispose libère correctement la musique.
     */
    @Test
    @DisplayName("dispose doit libérer la musique")
    void testerDispose() {
        // Appeler dispose
        audioManager.dispose();

        // Vérifier que dispose a été appelé sur la musique
        verify(musiqueMock).dispose();
    }

    /**
     * Teste que le Map soundEffects existe et est vide au démarrage.
     */
    @Test
    @DisplayName("Le dictionnaire des effets sonores doit exister et être vide")
    void testerDictionnaireEffetsSonores() throws Exception {
        // Récupérer le champ privé soundEffects
        Field champSoundEffects = AudioManager.class.getDeclaredField("soundEffects");
        champSoundEffects.setAccessible(true);
        Map<String, ?> effets = (Map<String, ?>) champSoundEffects.get(audioManager);

        // Vérifier qu'il existe et est vide
        assertNotNull(effets, "Le dictionnaire des effets sonores ne doit pas être null");
        assertTrue(effets.isEmpty(), "Le dictionnaire doit être vide au démarrage");
    }
}