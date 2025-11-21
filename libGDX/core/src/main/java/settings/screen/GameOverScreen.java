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

public class GameOverScreen implements Screen {

    private static final Color OVERLAY_COLOR = new Color(0f, 0f, 0f, 0.75f);
    private static final Color GAME_OVER_COLOR = new Color(1f, 0f, 0f, 1f);
    private static final Color PROMPT_COLOR = new Color(1f, 1f, 0f, 1f);

    private static final int GAME_OVER_FONT_SIZE = 60;
    private static final int PROMPT_FONT_SIZE = 30;
    private static final float BLINK_SPEED = 0.5f;

    private final ScreenManager screenManager;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private BitmapFont gameOverFont;
    private BitmapFont promptFont;
    private BitmapFont fontText;

    private float stateTime = 0f;
    private boolean showPrompt = true;

    public GameOverScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        initializeFonts();
    }

    private void initializeFonts() {
        FreeTypeFontGenerator generator =
                new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P-Regular.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter gameOverParam =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        gameOverParam.size = GAME_OVER_FONT_SIZE;
        gameOverParam.color = GAME_OVER_COLOR;
        gameOverParam.borderWidth = 2;
        gameOverParam.borderColor = Color.BLACK;
        gameOverFont = generator.generateFont(gameOverParam);

        FreeTypeFontGenerator.FreeTypeFontParameter promptParam =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        promptParam.size = PROMPT_FONT_SIZE;
        promptParam.color = PROMPT_COLOR;
        promptParam.borderWidth = 1;
        promptParam.borderColor = Color.BLACK;
        promptFont = generator.generateFont(promptParam);

        FreeTypeFontGenerator.FreeTypeFontParameter textParam =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        textParam.size = 24;
        textParam.color = Color.WHITE;
        textParam.borderWidth = 1;
        textParam.borderColor = Color.BLACK;
        fontText = generator.generateFont(textParam);

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
        if (Gdx.input.justTouched()) {
            screenManager.setState(GameState.CHARACTER_SELECT);
        }

        if (Gdx.input.isKeyJustPressed(74)) {
            System.out.println("M pressed = Menu");
            screenManager.setState(GameState.MENU);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            System.out.println("R pressed = Select Character");
            screenManager.resumeGame();
        }
    }

    private void renderOverlay() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

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

        gameOverFont.setColor(Color.BLACK);
        drawCentered(gameOverFont, "GAME OVER", centerX + 3, centerY + 53);
        gameOverFont.setColor(GAME_OVER_COLOR);
        drawCentered(gameOverFont, "GAME OVER", centerX, centerY + 50);

        if (showPrompt) {
            promptFont.setColor(new Color(0, 0, 0, 0.7f));
            drawCentered(promptFont, "TAP TO RESTART", centerX + 2, centerY - 52);

            promptFont.setColor(PROMPT_COLOR);
            drawCentered(promptFont, "TAP TO RESTART", centerX, centerY - 50);
        }

        fontText.draw(batch, "R - Start Over", 100, 300);
        fontText.draw(batch, "M - Open Menu",  100, 240);

        batch.end();
    }

    private void drawCentered(BitmapFont font, String text, float x, float y) {
        GlyphLayout layout = new GlyphLayout(font, text);
        font.draw(batch, text, x - layout.width / 2f, y + layout.height / 2f);
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        gameOverFont.dispose();
        promptFont.dispose();
        fontText.dispose();
    }
}
