package settings.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;
import settings.GameState;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.InputAdapter;


public class SettingsScreen implements Screen {

    private float volume;
    private final ScreenManager screenManager;
    private SpriteBatch batch;
    private BitmapFont fontTitle;
    private BitmapFont fontText;
    private Texture pointer;
    private int selectedIndex = 0;
    private final String title = "Settings";
    private final String[] menuItems = {"Volume", "Fullscreen", "Sound", "Back"};

    private final float pointerX = 400;
    private final float menuX = 500;
    private final int menuStartY = 400;
    private final int menuSpacing = 100;
    private Preferences preferences;


    public SettingsScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
            selectedIndex = Math.max(selectedIndex - 1, 0);
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
            selectedIndex = Math.min(selectedIndex + 1, menuItems.length - 1);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                adjustVolume(volume - amountY * 0.05f);
                return true;
            }
        });

        if (selectedIndex == 0) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_SUBTRACT)) {
                setVolume(volume - 0.1f);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.EQUALS) || Gdx.input.isKeyJustPressed(Input.Keys.PLUS) || Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_ADD)) {
                setVolume(volume + 0.1f);
            }
        }

        if (selectedIndex == 1 && (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) ||
                Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) ||
                Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))) {
            setFullscreen(!isFullscreen());
        }
        if (selectedIndex == 2 && (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) ||
                Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) ||
                Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))) {
            setSoundEnabled(!isSoundEnabled());
        }
        if ((selectedIndex == 3 && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) ||
                Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            screenManager.setState(GameState.MENU);
        }
    }

    public void adjustVolume(float value) {
        float old = volume;
        this.volume = Math.max(0f, Math.min(1f, value));

        if (preferences != null) {
            preferences.putFloat("volume", volume);
            preferences.flush();
        }
        if (Math.abs(old - volume) > 1e-6) {
            System.out.println("Volume adjusted to " + (int) (volume * 100) + "%");
        }
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        pointer = new Texture(Gdx.files.internal("images/hand_cursor0000.png"));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P-Regular.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter titleParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        titleParam.size = 70;
        titleParam.color = Color.BLACK;
        fontTitle = generator.generateFont(titleParam);

        FreeTypeFontGenerator.FreeTypeFontParameter textParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        textParam.size = 40;
        textParam.color = Color.BLACK;
        fontText = generator.generateFont(textParam);

        generator.dispose();

        preferences = Gdx.app.getPreferences("SmileyQuestSettings");
        if (!preferences.contains("volume")) preferences.putFloat("volume", 0.5f);
        if (!preferences.contains("fullscreen")) preferences.putBoolean("fullscreen", false);
        if (!preferences.contains("sound")) preferences.putBoolean("sound", true);
        preferences.flush();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                setVolume(volume - amountY * 0.05f);
                return true;
            }
        });

        volume = preferences.getFloat("volume");

        System.out.println("Settings screen visible! volume=" + (int)(volume*100) + "%");
    }

    @Override
    public void render(float delta) {
        handleInput();
        ScreenUtils.clear(1f, 0.92f, 0.23f, 1);

        batch.begin();

        GlyphLayout layoutTitle = new GlyphLayout(fontTitle, title);
        float titleX = (Gdx.graphics.getWidth() - layoutTitle.width) / 2;
        fontTitle.draw(batch, title, titleX, Gdx.graphics.getHeight() - 100);

        for (int i = 0; i < menuItems.length; i++) {
            String text = menuItems[i];
            if (i == 0) text += " : " + (int) (volume * 100) + "%";
            if (i == 1) text += " : " + (isFullscreen() ? "ON" : "OFF") ;
            if (i == 2) text += " : " + (isSoundEnabled() ? "ON" : "OFF") ;

            fontText.draw(batch, text, menuX, menuStartY - i * menuSpacing);
        }
        batch.draw(pointer, pointerX, menuStartY - selectedIndex * menuSpacing - pointer.getHeight() / 2);

        batch.end();
    }

    private void setVolume(float value) {
        volume = Math.max(0f, Math.min(1f, value));
        preferences.putFloat("volume", volume);
        preferences.flush();
        System.out.println("Volume set to " + (int)(volume*100) + "%");
    }

    private boolean isFullscreen() {
        return preferences.getBoolean("fullscreen");
    }

    private void setFullscreen(boolean value) {
        preferences.putBoolean("fullscreen", value);
        preferences.flush();
        System.out.println("Fullscreen: " + (value ? "ON" : "OFF"));
    }

    private boolean isSoundEnabled() {
        return preferences.getBoolean("sound");
    }

    private void setSoundEnabled(boolean value) {
        preferences.putBoolean("sound", value);
        preferences.flush();
        System.out.println("Sound: " + (value ? "ON" : "OFF"));
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void hide() {
        System.out.println("Closed settings screen!");
    }

    @Override
    public void dispose() {
        if (fontTitle != null) fontTitle.dispose();
        if (fontText != null) fontText.dispose();
        if (pointer != null) pointer.dispose();
        if (batch != null) batch.dispose();
        System.out.println("Cleansing of SettingsScreen.");
    }

    public float getVolume() {
        return volume;
    }
}
