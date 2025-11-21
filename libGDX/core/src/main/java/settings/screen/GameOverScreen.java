package settings.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import settings.GameState;

public class GameOverScreen implements Screen {

    private final ScreenManager screenManager;

    private Viewport viewport;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    private final Color TRANSPARENT_BLACK = new Color(0f, 0f, 0f, 0.7f);

    public GameOverScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    @Override
    public void show() {
        viewport = new FitViewport(800, 600);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(2);
    }

    /**
     * Effacer l'écran, et le rendre transparant, en centrant les mots "Game Over" et "Tap to restart".
     * @param delta The time in seconds since the last render.
     *  Réinitialiser le jeu, et revenir à l'écran de jeu.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(TRANSPARENT_BLACK);
        shapeRenderer.rect(0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        shapeRenderer.end();

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        float centerX = viewport.getWorldWidth() / 2f;

        font.setColor(Color.RED);
        font.draw(batch, "GAME OVER", centerX, 400, 0, Align.center, false);

        font.setColor(Color.WHITE);
        font.draw(batch, "Tap to Restart", centerX, 300, 0, Align.center, false);

        batch.end();

        if (Gdx.input.justTouched()) {
            screenManager.setState(GameState.PLAYING);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }
    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }
}
