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
    @Override public void dispose() { batch.dispose(); }
}
