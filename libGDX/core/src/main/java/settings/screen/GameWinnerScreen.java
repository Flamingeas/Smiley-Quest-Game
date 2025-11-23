package settings.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import settings.GameState;

/**
 * Ecran de ictoire du jeu.
 * Il affiche un message de victoire centré avec une incitation à redémarrer.
 */
public class GameWinnerScreen implements Screen {

    private static final Color BGCOLOR = new Color(0f, 0f, 0f, 0.75f);
    private static final Color WINNER_COLOR = new Color(0f, 1f, 0f, 1f);
    private static final Color PROMPT_COLOR = new Color(1f, 1f, 0f, 1f);

    private static final int WINNER_FONT_SIZE = 60;
    private static final int PROMPT_FONT_SIZE = 30;
    private static final float BLINK_SPEED = 0.5f;

    private final ScreenManager screenManager;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    /** Police pour le titre "YOU WIN!" */
    private BitmapFont winnerFont;
    /** Police pour l'invite de redémarrage clignotante */
    private BitmapFont promptFont;
    private BitmapFont fontText;

    /** État d'animation pour le clignotement de l'invite */
    private float stateTime = 0f;
    /** Indique si le texte d'invite doit être affiché (pour le clignotement) */
    private boolean showPrompt = true;

    /**
     * Constructeur de l'écran de victoire.
     * @param screenManager Le gestionnaire d'écrans pour naviguer entre les vues du jeu.
     */
    public GameWinnerScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    /**
     * Appelé lorsque cet écran devient l'écran actuel du jeu.
     * Initialise les ressources graphiques (batch, renderer, polices).
     */
    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        initializeFonts();
    }

    /**
     * Initialise les objets BitmapFont à partir du générateur FreeType.
     */
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

        FreeTypeFontGenerator.FreeTypeFontParameter textParam =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        textParam.size = 24;
        textParam.color = Color.WHITE;
        textParam.borderWidth = 1;
        textParam.borderColor = Color.BLACK;
        fontText = generator.generateFont(textParam);

        generator.dispose();
    }

    /**
     * Appelé par la boucle de jeu pour mettre à jour et dessiner l'écran.
     * Gère la mise à jour de l'état d'animation et le dessin des éléments.
     * @param delta Le temps écoulé depuis le dernier appel.
     */
    @Override
    public void render(float delta) {
        stateTime += delta;
        showPrompt = ((int)(stateTime / BLINK_SPEED) % 2 == 0);

        handleInput();
        renderOverlay();
        renderText();
    }

    /**
     * Gère les entrées utilisateur pour la navigation (redémarrer ou menu).
     */
    private void handleInput() {
        if (Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            screenManager.setState(GameState.CHARACTER_SELECT);
        }

        if (Gdx.input.isKeyJustPressed(74)) {
            System.out.println("M pressed = Menu");
            screenManager.setState(GameState.MENU);
        }
    }

    /**
     * Affiche l'arrière-plan de l'écran avec une surcouche semi-transparente.
     */
    private void renderOverlay() {
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(BGCOLOR);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();
    }

    /**
     * Affiche le texte du titre de victoire et l'invite de redémarrage.
     */
    private void renderText() {
        batch.begin();

        float centerX = Gdx.graphics.getWidth() / 2f;
        float centerY = Gdx.graphics.getHeight() / 2f;

        String winnerText = "YOU WON!";

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

        fontText.draw(batch, "M - Open Menu",  100, 240);
        batch.end();
    }

    /**
     * Dessine un texte centré à la position donnée.
     * @param font La police à utiliser.
     * @param text Le texte à dessiner.
     * @param x La coordonnée X du centre.
     * @param y La coordonnée Y du centre.
     */
    private void drawCenteredText(BitmapFont font, String text, float x, float y) {
        GlyphLayout layout = new GlyphLayout(font, text);
        font.draw(batch, text, x - layout.width / 2, y + layout.height / 2);
    }

    @Override
    public void resize(int width, int height) {}

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