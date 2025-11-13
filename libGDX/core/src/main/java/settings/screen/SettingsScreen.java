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

public class SettingsScreen implements Screen {

    private float volume = 0.5f;
    private final ScreenManager screenManager;
    private SpriteBatch batch;
    private BitmapFont fontTitle;
    private BitmapFont fontText;
    private Texture pointer;
    private int selectedIndex = 0;
    private final String title = "Settings";
    private final String[] menuItems = {"Volume", "Graphics", "Back"};

    private final float pointerX = 400;
    private final float menuX = 500;
    private final int menuStartY = 400;
    private final int menuSpacing = 100;

    public SettingsScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
            selectedIndex = Math.min(selectedIndex + 1, menuItems.length - 1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
            selectedIndex = Math.max(selectedIndex - 1, 0);
        if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS) && selectedIndex == 0) {
            adjustVolume(-0.1f);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.PLUS) && selectedIndex == 0) {
            adjustVolume(+0.1f);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (selectedIndex == 2) {
                screenManager.setState(GameState.MENU);
            }
        }
    }
    public void adjustVolume(float value) {
        this.volume = Math.max(0f, Math.min(1f, value));
        System.out.println("Volume adjusted to " + (volume * 100) + "%");
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        pointer = new Texture(Gdx.files.internal("images/hand_cursor0000.png"));

        // Font
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
        System.out.println("Settings screen visible!");
    }

    public void update(float delta) {
    }

    @Override
    public void render(float delta) {
        handleInput();
        ScreenUtils.clear(0.1f, 0.1f, 0.3f, 1);

        batch.begin();

        // Title
        GlyphLayout layoutTitle = new GlyphLayout(fontTitle, title);
        float titleX = (Gdx.graphics.getWidth() - layoutTitle.width) / 2;
        fontTitle.draw(batch, title, titleX, Gdx.graphics.getHeight() - 100);

        // Menu
        for (int i = 0; i < menuItems.length; i++) {
            String text = menuItems[i];
            if (i == 0) text += " : " + (int)(volume * 100) + "%";
            fontText.draw(batch, text, menuX, menuStartY - i * menuSpacing);
        }

        batch.draw(pointer, pointerX, menuStartY - selectedIndex * menuSpacing - pointer.getHeight() / 2);
        System.out.println("SettingsScreen");
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
        fontTitle.dispose();
        fontText.dispose();
        pointer.dispose();
        batch.dispose();
        System.out.println("Cleansing of SettingsScreen.");
    }
    public float getVolume() {
        return volume;
    }
}
