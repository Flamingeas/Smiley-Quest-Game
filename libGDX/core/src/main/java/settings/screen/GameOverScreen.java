package settings.screen;

import com.badlogic.gdx.Screen;

public class GameOverScreen implements Screen {

    private int finalScore;

    public GameOverScreen() {}

    public GameOverScreen(int finalScore) {
        this.finalScore = finalScore;
    }

    public void showResults() {
        System.out.println("=== GAME OVER ===");
        System.out.println("Score final : " + finalScore);
        System.out.println("Appuyez sur [R] pour rejouer ou [M] pour revenir au menu.");
    }

    public void handleInput() {
    }

    @Override
    public void show() {
        showResults();
    }


    public void update(float delta) {
        handleInput();
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
