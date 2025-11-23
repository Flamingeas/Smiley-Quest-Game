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
import system.AudioManager;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("AudioManager Tests")
class AudioManagerTest {

    private AudioManager audioManager;
    private Music musicMock;
    private Preferences preferencesMock;

    @BeforeEach
    void setUp() {
        resetSingleton();

        musicMock = mock(Music.class);
        preferencesMock = mock(Preferences.class);

        Application applicationMock = mock(Application.class);
        Audio audioMock = mock(Audio.class);
        Files filesMock = mock(Files.class);

        Gdx.app = applicationMock;
        Gdx.audio = audioMock;
        Gdx.files = filesMock;

        when(applicationMock.getPreferences("SmileyQuestSettings")).thenReturn(preferencesMock);
        when(preferencesMock.getFloat("volume", 0.5f)).thenReturn(0.5f);
        when(preferencesMock.getBoolean("sound", true)).thenReturn(true);
        when(audioMock.newMusic(any())).thenReturn(musicMock);

        audioManager = AudioManager.getInstance();
    }

    @AfterEach
    void tearDown() {
        resetSingleton();
        Gdx.app = null;
        Gdx.audio = null;
        Gdx.files = null;
    }

    private void resetSingleton() {
        try {
            Field instanceField = AudioManager.class.getDeclaredField("instance");
            instanceField.setAccessible(true);
            instanceField.set(null, null);
        } catch (Exception e) {
            fail("Unable to reset singleton: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("getInstance should always return the same instance")
    void testSingleton() {
        AudioManager instance1 = AudioManager.getInstance();
        AudioManager instance2 = AudioManager.getInstance();
        assertSame(instance1, instance2, "getInstance should return the same instance every time");
    }

    @Test
    @DisplayName("Music should be set to loop")
    void testMusicLooping() {
        verify(musicMock, times(1)).setLooping(true);
    }

    @Test
    @DisplayName("Initial volume should be retrieved from preferences")
    void testInitialVolume() {
        verify(musicMock, times(1)).setVolume(0.5f);
    }

    @Test
    @DisplayName("Music should play if sound is enabled in preferences")
    void testMusicPlaysWithSoundEnabled() {
        verify(musicMock, times(1)).play();
    }

    @Test
    @DisplayName("Music should not play if sound is disabled")
    void testMusicDoesNotPlayIfSoundDisabled() {
        resetSingleton();

        Music newMusicMock = mock(Music.class);
        Preferences newPreferencesMock = mock(Preferences.class);

        when(newPreferencesMock.getFloat("volume", 0.5f)).thenReturn(0.5f);
        when(newPreferencesMock.getBoolean("sound", true)).thenReturn(false);
        when(Gdx.app.getPreferences("SmileyQuestSettings")).thenReturn(newPreferencesMock);
        when(Gdx.audio.newMusic(any())).thenReturn(newMusicMock);

        AudioManager manager = AudioManager.getInstance();
        verify(newMusicMock, never()).play();
    }

    @Test
    @DisplayName("setVolume should change music volume")
    void testSetVolume() {
        audioManager.setVolume(0.8f);
        verify(musicMock, times(2)).setVolume(anyFloat());
        verify(musicMock).setVolume(0.8f);
    }

    @Test
    @DisplayName("setVolume should save volume in preferences")
    void testVolumeSavedInPreferences() {
        audioManager.setVolume(0.7f);
        verify(preferencesMock).putFloat("volume", 0.7f);
        verify(preferencesMock).flush();
    }

    @Test
    @DisplayName("setVolume should clamp volume between 0 and 1")
    void testVolumeLimits() {
        audioManager.setVolume(1.5f);
        verify(musicMock).setVolume(1.0f);

        audioManager.setVolume(-0.5f);
        verify(musicMock).setVolume(0.0f);
    }

    @Test
    @DisplayName("setSoundEnabled(true) should play music")
    void testEnableSoundPlaysMusic() {
        when(musicMock.isPlaying()).thenReturn(false);
        audioManager.setSoundEnabled(true);
        verify(musicMock, times(2)).play();
    }

    @Test
    @DisplayName("setSoundEnabled(false) should pause music")
    void testDisableSoundPausesMusic() {
        when(musicMock.isPlaying()).thenReturn(true);
        audioManager.setSoundEnabled(false);
        verify(musicMock).pause();
    }

    @Test
    @DisplayName("setSoundEnabled should save state in preferences")
    void testSoundStateSavedInPreferences() {
        audioManager.setSoundEnabled(false);
        verify(preferencesMock).putBoolean("sound", false);
        verify(preferencesMock, atLeastOnce()).flush();
    }

    @Test
    @DisplayName("dispose should release music resources")
    void testDispose() {
        audioManager.dispose();
        verify(musicMock).dispose();
    }

    @Test
    @DisplayName("Sound effects map should exist and be empty initially")
    void testSoundEffectsMap() throws Exception {
        Field soundEffectsField = AudioManager.class.getDeclaredField("soundEffects");
        soundEffectsField.setAccessible(true);
        Map<String, ?> effects = (Map<String, ?>) soundEffectsField.get(audioManager);

        assertNotNull(effects, "Sound effects map should not be null");
        assertTrue(effects.isEmpty(), "Sound effects map should be empty initially");
    }
}
