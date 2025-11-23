package system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Preferences;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages background music and sound effects for the game.
 * Implements the singleton pattern to ensure a single instance across the game.
 * Handles volume control, enabling/disabling sound, and disposing audio resources.
 */
public class AudioManager {

    /** Singleton instance. */
    private static AudioManager instance;

    /** Background music track. */
    private Music backgroundMusic;

    /** Map of sound effect names to Sound objects. */
    private final Map<String, Sound> soundEffects;

    /** Stores user preferences for volume and sound. */
    private Preferences preferences;

    /**
     * Private constructor to enforce singleton pattern.
     * Loads background music and applies saved preferences.
     */
    private AudioManager() {
        soundEffects = new HashMap<>();
        preferences = Gdx.app.getPreferences("SmileyQuestSettings");

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/pacman-background-music.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(preferences.getFloat("volume", 0.5f));
        if (preferences.getBoolean("sound", true)) {
            backgroundMusic.play();
        }
    }

    /**
     * Returns the singleton instance of AudioManager.
     *
     * @return the AudioManager instance
     */
    public static AudioManager getInstance() {
        if (instance == null) instance = new AudioManager();
        return instance;
    }

    /**
     * Sets the background music volume and saves it in preferences.
     *
     * @param volume value between 0 (mute) and 1 (max)
     */
    public void setVolume(float volume) {
        volume = Math.max(0f, Math.min(1f, volume));
        if (backgroundMusic != null) backgroundMusic.setVolume(volume);
        preferences.putFloat("volume", volume);
        preferences.flush();
        System.out.println("Volume set to " + (int)(volume*100) + "%");
    }

    /**
     * Enables or disables all game sounds.
     * Plays or pauses the background music accordingly.
     *
     * @param enabled true to enable sound, false to disable
     */
    public void setSoundEnabled(boolean enabled) {
        preferences.putBoolean("sound", enabled);
        preferences.flush();
        if (enabled) {
            if (!backgroundMusic.isPlaying()) backgroundMusic.play();
        } else {
            if (backgroundMusic.isPlaying()) backgroundMusic.pause();
        }
        System.out.println("Sound " + (enabled ? "ON" : "OFF"));
    }

    /**
     * Disposes all audio resources (music and sound effects).
     */
    public void dispose() {
        if (backgroundMusic != null) backgroundMusic.dispose();
        for (Sound s : soundEffects.values()) s.dispose();
    }
}
