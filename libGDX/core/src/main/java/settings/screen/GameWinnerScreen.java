package settings.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import settings.GameState;

/**
 * Écran Victoire/Gagnant style Pac-Man avec overlay semi-transparent
 * qui laisse voir l'écran de jeu en arrière-plan.
 */
public class GameWinnerScreen implements Screen {

    private static final Color OVERLAY_COLOR = new Color(0f, 0f, 0f, 0.75f);
    private static final Color WINNER_COLOR = new Color(0f, 1f, 0f, 1f);
    private static final Color PROMPT_COLOR = new Color(1f, 1f, 0f, 1f);

    private static final int WINNER_FONT_SIZE = 60;
    private static final int PROMPT_FONT_SIZE = 30;
    private static final float BLINK_SPEED = 0.5f;

    private final ScreenManager screenManager;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont winnerFont;
    private BitmapFont promptFont;
    private BitmapFont fontTitle;
    private BitmapFont fontText;

    private float stateTime = 0f;
    private boolean showPrompt = true;

    public GameWinnerScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        screenManager.resumeGame();

        initializeFonts();
    }

    private void initializeFonts() {
        FreeTypeFontGenerator generator =
                new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P-Regular.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter winnerParam =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        winnerParam.size = WINNER_FONT_SIZE;
        winnerParam.color = WINNER_COLOR;
        winnerParam.borderWidth = 2;
        winnerParam.borderColor = Color.BLACK;
        winnerFont = generator.generateFont(winnerParam);

        FreeTypeFontGenerator.FreeTypeFontParameter promptParam =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        promptParam.size = PROMPT_FONT_SIZE;
        promptParam.color = PROMPT_COLOR;
        promptParam.borderWidth = 1;
        promptParam.borderColor = Color.BLACK;
        promptFont = generator.generateFont(promptParam);

        generator.dispose();
    }

    @Override
    public void render(float delta) {
        stateTime += delta;
        showPrompt = ((int)(stateTime / BLINK_SPEED) % 2 == 0);

        handleInput();
        renderOverlay();
        renderText();
    }

    private void handleInput() {
        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            screenManager.setState(GameState.CHARACTER_SELECT);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            screenManager.setState(GameState.MENU);
        }
    }

    private void renderOverlay() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(OVERLAY_COLOR);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void renderText() {
        batch.begin();

        float centerX = Gdx.graphics.getWidth() / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f;

        String winnerText = "YOU WIN!";

        winnerFont.setColor(Color.BLACK);
        drawCenteredText(winnerFont, winnerText, centerX + 3, centerY + 50 - 3);
        winnerFont.setColor(WINNER_COLOR);
        drawCenteredText(winnerFont, winnerText, centerX, centerY + 50);

        if (showPrompt) {
            String promptText = "TAP OR PRESS SPACE TO RESTART";
            promptFont.setColor(new Color(0, 0, 0, 0.7f));
            drawCenteredText(promptFont, promptText, centerX + 2, centerY - 50 - 2);
            promptFont.setColor(PROMPT_COLOR);
            drawCenteredText(promptFont, promptText, centerX, centerY - 50);
        }

        batch.end();
    }

    /**
     * Dessine un texte centré à la position donnée.
     * Note: GlyphLayout is expensive to create every frame. In a production
     * app, this should be cached, but for simplicity, we use it here.
     */
    private void drawCenteredText(BitmapFont font, String text, float x, float y) {
        GlyphLayout layout = new GlyphLayout(font, text);
        font.draw(batch, text, x - layout.width / 2, y + layout.height / 2);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
        if (winnerFont != null) {
            winnerFont.dispose();
        }
        if (promptFont != null) {
            promptFont.dispose();
        }
    }
}