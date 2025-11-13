package settings.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import settings.GameWorld;
import level.Level;
import utils.Coord;

import com.badlogic.gdx.math.Matrix4;

import java.util.Collections;

public class GameScreen implements Screen {

    private OrthographicCamera camera;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Texture playerTexture;

    private float playerX, playerY;
    private float speed = 200f;
    private GameWorld gameWorld;

    private float mapOffsetX;
    private float mapOffsetY;

    public GameScreen() {
        Level level = new Level(
                Collections.emptyList(),
                Collections.emptyList(),
                new Coord(2, 2),
                null
        );
        this.gameWorld = new GameWorld(level);
    }

    @Override
    public void show() {

        tiledMap = new TmxMapLoader().load("maps/sampleMap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 2f);


        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();


        playerX = camera.viewportWidth / 2f - 16f;
        playerY = camera.viewportHeight / 2f - 16f;


        playerTexture = new Texture(Gdx.files.internal("images/player.png"));

        //pour centrer la map
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float mapWidth = tiledMap.getProperties().get("width", Integer.class) * 16 * 2f;
        float mapHeight = tiledMap.getProperties().get("height", Integer.class) * 16 * 2f;

        mapOffsetX = (screenWidth * 0.75f - mapWidth) / 2f;
        mapOffsetY = (screenHeight - mapHeight) / 2f;

        System.out.println("GameScreen show() : map chargée, player initialisé.");
        System.out.println("GameScreen show() : map centrée gauche (" + mapOffsetX + ", " + mapOffsetY + ")");
    }

    private void handleInput(float delta) {
        float move = speed * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))  playerX -= move;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) playerX += move;
        if (Gdx.input.isKeyPressed(Input.Keys.UP))    playerY += move;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))  playerY -= move;

        if (Gdx.input.isKeyPressed(Input.Keys.Q))  playerX -= move;
        if (Gdx.input.isKeyPressed(Input.Keys.D))  playerX += move;
        if (Gdx.input.isKeyPressed(Input.Keys.Z))  playerY += move;
        if (Gdx.input.isKeyPressed(Input.Keys.S))  playerY -= move;
    }

    @Override
    public void render(float delta) {
        handleInput(delta);
        gameWorld.update(delta);

        ScreenUtils.clear(0, 0, 0, 1);

        camera.update();
        mapRenderer.setView(camera);

        Matrix4 originalMatrix = new Matrix4(mapRenderer.getBatch().getTransformMatrix());
        Matrix4 offsetMatrix = new Matrix4(originalMatrix);
        offsetMatrix.translate(mapOffsetX, mapOffsetY, 0);
        mapRenderer.getBatch().setTransformMatrix(offsetMatrix);

        mapRenderer.render();

        mapRenderer.getBatch().setTransformMatrix(originalMatrix);

        mapRenderer.getBatch().begin();
        mapRenderer.getBatch().draw(playerTexture, playerX, playerY, 32, 32);
        mapRenderer.getBatch().end();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override
    public void dispose() {
        tiledMap.dispose();
        mapRenderer.dispose();
        playerTexture.dispose();
    }
}
