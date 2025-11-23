package settings.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;
import settings.GameState;


/**
 * Represents the pause screen of the game.
 * Responsibilities include rendering a semi-transparent overlay,
 * displaying pause instructions, and handling input to either
 * resume the game or return to the main menu.
 */
public class PausedScreen implements Screen {

    private final ScreenManager screenManager;
    private SpriteBatch batch;
    private BitmapFont fontTitle;
    private BitmapFont fontText;

    /**
     * The global screen manager used to switch between screens.
     */
    public PausedScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    /**
     * Called when the pause screen becomes visible.
     * Initializes the fonts and the sprite batch.
     */

    @Override
    public void show() {

        batch = new SpriteBatch();

        FreeTypeFontGenerator gen =
                new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P-Regular.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter t1 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        t1.size = 55;
        t1.color = Color.WHITE;
        fontTitle = gen.generateFont(t1);

        FreeTypeFontGenerator.FreeTypeFontParameter t2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        t2.size = 28;
        t2.color = Color.YELLOW;
        fontText = gen.generateFont(t2);

        gen.dispose();
    }

    /**
     * Handles keyboard input for the pause screen.
     * Pressing 'R' resumes the game.
     * Pressing 'M' returns to the main menu.
     * Logs all key presses for debugging purposes.
     */
    private void handleInput() {
        for (int i = 0; i < 256; i++) {
            if (Gdx.input.isKeyJustPressed(i)) {
                System.out.println("KEY PRESSED : " + i);
            }
        }

        if (Gdx.input.isKeyJustPressed(74)) {
            System.out.println("M pressed");
            screenManager.setState(GameState.MENU);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            screenManager.resumeGame();
        }
    }


    /**
     * Renders the pause screen overlay.
     * Displays the title and instructions for resuming or returning to the menu.
     *
     * @param delta the time elapsed since the last frame in seconds
     */
    @Override
    public void render(float delta) {

        handleInput();

        ScreenUtils.clear(0, 0, 0, 0.80f);

        batch.begin();

        String title = "PAUSE";
        GlyphLayout layout = new GlyphLayout(fontTitle, title);
        float tx = (Gdx.graphics.getWidth() - layout.width) / 2;
        fontTitle.draw(batch, title, tx, Gdx.graphics.getHeight() - 140);

        fontText.draw(batch, "R - Reprendre la partie", 100, 300);
        fontText.draw(batch, "M - Retour au menu",       100, 240);

        batch.end();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        fontTitle.dispose();
        fontText.dispose();
    }
}
