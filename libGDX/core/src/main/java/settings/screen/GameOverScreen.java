package settings.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import settings.GameState;

public class GameOverScreen implements Screen {

    private int finalScore;
    private SpriteBatch batch;

    public GameOverScreen() {}

    public GameOverScreen(int finalScore) {
        this.finalScore = finalScore;
    }

    public void showResults() {
        System.out.println("=== GAME OVER ===");
        System.out.println("Score final : " + finalScore);
        System.out.println("Appuyez sur [R] pour rejouer ou [M] pour revenir au menu.");
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        ScreenUtils.clear(1f, 0.92f, 0.23f, 1);

        TextureRegion[] frames = new TextureRegion[18];
        for (int i = 0; i < 18; i++) {
            String filename = String.format("images/menuBackground-split/frame_%02d.png", i);
            frames[i] = new TextureRegion(new Texture(Gdx.files.internal(filename)));
        }
        showResults();
    }


    public void update(float delta) {;
    }

    @Override
    public void render(float delta) {
        System.out.println("Rendu du GameOverScreen...");
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        System.out.println("Fermeture de l'écran Game Over.");
    }

    @Override
    public void dispose() {
        System.out.println("Nettoyage du GameOverScreen.");
    }
    public int getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }
}
