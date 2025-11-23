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
import system.AudioManager;

/**
 * Screen that handles game settings.
 * This screen allows the player to:
 * - Toggle sound on/off
 * - Adjust volume
 * - Return to the main menu
 * The screen supports keyboard and mouse navigation.
 */
public class SettingsScreen implements Screen {

    /** Current volume level (0.0 to 1.0) */
    private float volume;

    /** ScreenManager used to switch screens */
    private final ScreenManager screenManager;

    /** Main batch for drawing */
    private SpriteBatch batch;

    /** Font for the screen title */
    private BitmapFont fontTitle;

    /** Font for menu items */
    private BitmapFont fontText;

    /** Pointer icon displayed next to the selected menu item */
    private Texture pointer;

    /** Currently selected menu index */
    private int selectedIndex = 0;

    /** Screen title */
    private final String title = "Settings";

    /** Menu item labels */
    private final String[] menuItems = {"Sound", "Back"};

    /** X position of pointer */
    private final float pointerX = 400;

    /** X position of menu items */
    private final float menuX = 650;

    /** Starting Y position of the first menu item */
    private final int menuStartY = 600;

    /** Vertical spacing between menu items */
    private final int menuSpacing = 100;

    /** Preferences object to store settings */
    private Preferences preferences;

    /**
     * Creates a SettingsScreen linked to a ScreenManager.
     *
     * @param screenManager the screen manager to handle screen transitions
     */
    public SettingsScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    /**
     * Handles keyboard and mouse input for menu navigation.
     * Arrow keys select items, enter or click activates them.
     * Adjusts sound setting when applicable.
     */
    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) selectedIndex = Math.max(selectedIndex - 1, 0);
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) selectedIndex = Math.min(selectedIndex + 1, menuItems.length - 1);

        if (selectedIndex == 0 && (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) || Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))) {
            setSoundEnabled(!isSoundEnabled());
        }
        if ((selectedIndex == 1 && Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            screenManager.setState(GameState.MENU);
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

            for (int i = 0; i < menuItems.length; i++) {
                String text = menuItems[i];
                if (i == 0) text += " : " + (isSoundEnabled() ? "ON" : "OFF");

                GlyphLayout layout = new GlyphLayout(fontText, text);
                float itemY = menuStartY - i * menuSpacing;
                float itemHeight = layout.height;
                float itemWidth = layout.width;

                if (mouseX >= menuX && mouseX <= menuX + itemWidth &&
                        mouseY >= itemY - itemHeight && mouseY <= itemY) {
                    selectedIndex = i;
                    switch (i) {
                        case 0 -> setSoundEnabled(!isSoundEnabled());
                        case 1 -> screenManager.setState(GameState.MENU);
                    }
                }
            }
        }
    }

    /**
     * Sets the volume level and updates preferences and audio manager.
     *
     * @param value volume level between 0.0 and 1.0
     */
    private void adjustVolume(float value) {
        volume = Math.max(0f, Math.min(1f, value));
        preferences.putFloat("volume", volume);
        preferences.flush();
        AudioManager.getInstance().setVolume(volume);
    }

    /** Returns whether sound is currently enabled. */
    private boolean isSoundEnabled() {
        return preferences.getBoolean("sound");
    }

    /** Enables or disables sound and updates preferences. */
    private void setSoundEnabled(boolean value) {
        preferences.putBoolean("sound", value);
        preferences.flush();
    }

    /** Loads fonts, textures, preferences, and initializes the screen. */
    @Override
    public void show() {
        batch = new SpriteBatch();
        pointer = new Texture(Gdx.files.internal("images/hand_cursor0000.png"));
        AudioManager.getInstance().setSoundEnabled(true);

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
        if (!preferences.contains("sound")) preferences.putBoolean("sound", true);
        preferences.flush();
    }

    /** Renders the settings screen including title, menu items, and pointer. */
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
            if (i == 0) text += " : " + (isSoundEnabled() ? "ON" : "OFF");
            fontText.draw(batch, text, menuX, menuStartY - i * menuSpacing);
        }

        batch.draw(pointer, pointerX, menuStartY - selectedIndex * menuSpacing - pointer.getHeight() / 2);
        batch.end();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    /** Frees resources like fonts, textures, and batch. */
    @Override
    public void dispose() {
        if (fontTitle != null) fontTitle.dispose();
        if (fontText != null) fontText.dispose();
        if (pointer != null) pointer.dispose();
        if (batch != null) batch.dispose();
    }
}