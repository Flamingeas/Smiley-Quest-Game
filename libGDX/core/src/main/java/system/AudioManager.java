package system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Preferences;
import java.util.HashMap;
import java.util.Map;

public class AudioManager {

    private static AudioManager instance;

    private Music backgroundMusic;
    private final Map<String, Sound> soundEffects;
    private Preferences preferences;

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

    public static AudioManager getInstance() {
        if (instance == null) instance = new AudioManager();
        return instance;
    }

    public void addSoundEffect(String key, String path) {
        Sound s = Gdx.audio.newSound(Gdx.files.internal(path));
        soundEffects.put(key, s);
    }

    public void setVolume(float volume) {
        volume = Math.max(0f, Math.min(1f, volume));
        if (backgroundMusic != null) backgroundMusic.setVolume(volume);
        preferences.putFloat("volume", volume);
        preferences.flush();
        System.out.println("Volume set to " + (int)(volume*100) + "%");
    }

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

    public void dispose() {
        if (backgroundMusic != null) backgroundMusic.dispose();
        for (Sound s : soundEffects.values()) s.dispose();
    }
}
