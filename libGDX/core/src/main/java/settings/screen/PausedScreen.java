package settings.screen;

import com.badlogic.gdx.Screen;

public class PausedScreen implements Screen {

    private boolean resumeRequested = false;

    public PausedScreen() {}

    public void handleInput() {
        System.out.println("[PAUSE] Appuyez sur R pour reprendre ou M pour menu.");
    }

    @Override
    public void show() {
        System.out.println("=== JEU EN PAUSE ===");
        handleInput();
    }

    public void update(float delta) {
    }

    @Override
    public void render(float delta) {
        System.out.println("Affichage de l’écran Pause...");
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        System.out.println("Fermeture de l’écran Pause.");
    }

    @Override
    public void dispose() {
        System.out.println("Nettoyage du PausedScreen.");
    }

    public boolean isResumeRequested() {
        return resumeRequested;
    }

    public void setResumeRequested(boolean resumeRequested) {
        this.resumeRequested = resumeRequested;
    }
}
