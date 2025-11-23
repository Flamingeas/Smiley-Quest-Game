package settings.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import settings.GameState;

import java.util.Comparator;

/**
 * Splash screen displayed at the start of the game.
 * This screen shows an animated introduction sequence
 * using frames from a TextureAtlas. Once the animation
 * finishes, it transitions to the main menu.
 */
public class SplashScreen implements Screen {

    /** SpriteBatch used for rendering. */
    private SpriteBatch batch;

    /** Animation sequence of the splash screen frames. */
    private Animation<TextureRegion> animation;

    /** Time tracker for the animation. */
    private float time = 0f;

    /** Reference to the screen manager to switch screens. */
    private ScreenManager screenManager;

    /**
     * Creates a SplashScreen linked to a ScreenManager.
     *
     * @param manager the screen manager to handle screen transitions
     */
    public SplashScreen(ScreenManager manager) {
        this.screenManager = manager;
    }

    /**
     * Called once when the screen becomes visible.
     * Loads frames from a TextureAtlas and sets up
     * the animation.
     */
    @Override
    public void show() {
        batch = new SpriteBatch();

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("intro/intro.atlas"));
        Array<TextureAtlas.AtlasRegion> all = atlas.getRegions();

        Array<TextureAtlas.AtlasRegion> frames = new Array<>();
        for (TextureAtlas.AtlasRegion r : all) {
            if (r.name.startsWith("intro_")) {
                frames.add(r);
            }
        }

        frames.sort(Comparator.comparing(a -> a.name));

        System.out.println("Frames count = " + frames.size);

        animation = new Animation<>(1f / 60f, frames);
        animation.setPlayMode(Animation.PlayMode.NORMAL);
    }

    /**
     * Renders the splash animation.
     * Clears the screen, draws the current frame,
     * and transitions to the menu when the animation ends.
     *
     * @param delta time elapsed since the last frame
     */
    @Override
    public void render(float delta) {
        time += delta * 0.30f;

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(
                animation.getKeyFrame(time),
                0, 0,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()
        );
        batch.end();

        if (animation.isAnimationFinished(time)) {
            screenManager.setState(GameState.MENU);
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {batch.dispose();}
}
