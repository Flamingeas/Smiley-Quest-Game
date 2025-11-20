package settings.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;

import settings.GameState;

/**
 * Screen responsible for letting the player choose which smiley
 * character they want to use. The player can navigate through the available
 * smileys using the arrow keys and confirm their choice with ENTER.
 * ESC returns to the main menu.
 *
 * <p>This screen loads UI assets (arrows, fonts, sprites) and
 * handles keyboard inputs to update the selected index.</p>
 */
public class CharacterSelectScreen implements Screen {

    /** Reference to the screen manager controlling navigation. */
    private final ScreenManager screenManager;

    /** SpriteBatch used for rendering all textures and fonts. */
    private SpriteBatch batch;

    /** Font used for the screen title. */
    private BitmapFont fontTitle;

    /** Font used for informational text. */
    private BitmapFont fontText;

    /** Arrow icon used to navigate left. */
    private Texture arrowLeft;

    /** Arrow icon used to navigate right. */
    private Texture arrowRight;

    /** Array of textures representing all available smiley heads. */
    private Texture[] smileys;

    /** Index of the currently selected smiley in the array. */
    private int selectedIndex = 0;

    /** Screen width value (cached at show()). */
    private float screenW;

    /** Screen height value (cached at show()). */
    private float screenH;

    /** Center X coordinate of the screen. */
    private float centerX;

    /** Center Y coordinate of the screen. */
    private float centerY;

    /**
     * Creates a new character selection screen.
     *
     * @param screenManager The screen manager responsible for state switching.
     */
    public CharacterSelectScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    /**
     * Initializes all graphical resources (textures, fonts, positions).
     * Called automatically when this screen becomes active.
     */
    @Override
    public void show() {
        batch = new SpriteBatch();

        screenW = Gdx.graphics.getWidth();
        screenH = Gdx.graphics.getHeight();
        centerX = screenW / 2f;
        centerY = screenH / 2f;

        arrowLeft = new Texture("images/ui/arrow_left.png");
        arrowRight = new Texture("images/ui/arrow_right.png");

        smileys = new Texture[]{
                new Texture("images/smiley/smiley_0.png"),
                new Texture("images/smiley/smiley_1.png"),
                new Texture("images/smiley/smiley_2.png"),
                new Texture("images/smiley/smiley_3.png"),
                new Texture("images/smiley/smiley_4.png"),
                new Texture("images/smiley/smiley_5.png"),
                new Texture("images/smiley/smiley_6.png")
        };

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();

        p.size = 40;
        p.color = Color.WHITE;
        fontTitle = gen.generateFont(p);

        p.size = 20;
        fontText = gen.generateFont(p);

        gen.dispose();
    }

    /**
     * Renders the selection screen, handles input and draws
     * the currently selected smiley and UI instructions.
     *
     * @param delta Time elapsed since last frame.
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        handleInput();
        batch.begin();

        float titleY = centerY + 260;
        float smileyY = centerY;
        float arrowsY = smileyY - 10;
        float info1Y = smileyY - 180;
        float info2Y = info1Y - 60;
        float info3Y = info2Y - 60;

        fontTitle.draw(batch, "SELECT YOUR SMILEY", centerX - 330, titleY);

        batch.draw(arrowLeft, centerX - 260, arrowsY - 48, 96, 96);
        batch.draw(arrowRight, centerX + 160, arrowsY - 48, 96, 96);

        Texture current = smileys[selectedIndex];
        batch.draw(current, centerX - 64, smileyY - 64, 128, 128);

        fontText.draw(batch, "USE <- AND -> TO CHOOSE", centerX - 220, info1Y);
        fontText.draw(batch, "PRESS ENTER TO CONFIRM", centerX - 220, info2Y);
        fontText.draw(batch, "ESC TO CANCEL", centerX - 180, info3Y);

        batch.end();
    }

    /**
     * Handles all keyboard input for this screen:
     * <ul>
     *     <li>LEFT/RIGHT: Navigate between smileys</li>
     *     <li>ENTER: Confirm selection and switch to PLAYING</li>
     *     <li>ESCAPE: Return to the menu</li>
     * </ul>
     */
    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            selectedIndex = (selectedIndex + 1) % smileys.length;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            selectedIndex = (selectedIndex - 1 + smileys.length) % smileys.length;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            screenManager.setSelectedSmiley(selectedIndex);
            screenManager.setState(GameState.PLAYING);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            screenManager.setState(GameState.MENU);
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    /**
     * Releases all loaded assets to prevent memory leaks.
     */
    @Override
    public void dispose() {
        batch.dispose();
        arrowLeft.dispose();
        arrowRight.dispose();
        for (Texture t : smileys) t.dispose();
        fontTitle.dispose();
        fontText.dispose();
    }
}
