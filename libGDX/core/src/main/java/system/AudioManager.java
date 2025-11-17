package system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Preferences;
import java.util.HashMap;
import java.util.Map;

public class AudioManager implements Observer {

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
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }

    public void addSoundEffect(String key, String path) {
        Sound s = Gdx.audio.newSound(Gdx.files.internal(path));
        soundEffects.put(key, s);
    }

    @Override
    public void onNotify(GameEvent event) {
        String VOLUME_CHANGED = "";
        String SOUND_TOGGLED = "";
        switch (event.getType()) {
            case VOLUME_CHANGED -> backgroundMusic.setVolume(event.getVolume());
            case SOUND_TOGGLED -> setSoundEnabled(event.isSoundEnabled());
        }
    }

    public void dispose() {
        if (backgroundMusic != null) backgroundMusic.dispose();
        for (Sound s : soundEffects.values()) {
            s.dispose();
        }
    }
}