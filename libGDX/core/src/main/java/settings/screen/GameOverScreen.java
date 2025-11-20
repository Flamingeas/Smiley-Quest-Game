package settings.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import settings.GameState;

/**
 * Represents the Game Over screen displayed when the player loses.
 * This screen simply shows the final score and waits for player input
 * (R to restart, M to go back to the menu). It can later be expanded
 * with textures, animations, and UI buttons.
 */
public class GameOverScreen implements Screen {

    /** The final score achieved by the player before losing. */
    private int finalScore;

    /** SpriteBatch used for rendering (if graphical elements are added). */
    private SpriteBatch batch;

    /**
     * Default constructor.
     * Final score may be set later using {@link #setFinalScore(int)}.
     */
    public GameOverScreen() {}

    /**
     * Creates a Game Over screen with the given final score.
     *
     * @param finalScore The score the player reached before the game ended.
     */
    public GameOverScreen(int finalScore) {
        this.finalScore = finalScore;
    }

    /**
     * Displays textual results in the console.
     * This is currently a placeholder until the graphical UI is implemented.
     */
    public void showResults() {
        System.out.println("=== GAME OVER ===");
        System.out.println("Final score: " + finalScore);
        System.out.println("Press [R] to restart or [M] to return to the menu.");
    }

    /**
     * Initializes rendering resources and loads placeholder animation frames.
     * In its current state, the screen mostly outputs console feedback.
     */
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

    /**
     * Updates the Game Over screen.
     * Currently empty — logic may be added later for animations or inputs.
     *
     * @param delta Time elapsed since last frame.
     */
    public void update(float delta) {}

    /**
     * Renders the Game Over screen.
     * Currently outputs debug text for development/demo purposes.
     *
     * @param delta Time elapsed since last frame.
     */
    @Override
    public void render(float delta) {
        System.out.println("Rendering GameOverScreen...");
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    /**
     * Called when this screen is no longer the active one.
     * Logs a simple debug message.
     */
    @Override
    public void hide() {
        System.out.println("Closing GameOverScreen.");
    }

    /**
     * Frees resources allocated by this screen.
     */
    @Override
    public void dispose() {
        System.out.println("Disposing GameOverScreen.");
    }

    /**
     * @return The final score achieved by the player.
     */
    public int getFinalScore() {
        return finalScore;
    }

    /**
     * Sets the score value that will be displayed on the Game Over screen.
     *
     * @param finalScore The score to store.
     */
    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }
}
