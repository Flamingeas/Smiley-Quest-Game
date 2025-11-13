package settings.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import settings.GameState;

public class SplashScreen implements Screen {

    private SpriteBatch batch;
    private Animation<TextureRegion> animation;
    private float time = 0f;
    private ScreenManager screenManager;

    public SplashScreen(ScreenManager manager) {
        this.screenManager = manager;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("intro.atlas"));

        animation = new Animation<>(1f / 30f, atlas.findRegions("frame"));
        animation.setPlayMode(Animation.PlayMode.NORMAL);

        System.out.println("Loaded atlas frames: " + animation.getKeyFrames().length);
    }

    @Override
    public void render(float delta) {
        time += delta;

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
    @Override public void dispose() { batch.dispose(); }
}
