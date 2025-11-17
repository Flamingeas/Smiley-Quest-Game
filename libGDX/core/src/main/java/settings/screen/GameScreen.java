package settings.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import settings.GameWorld;
import level.Level;
import utils.Coord;
import entity.Player;
import com.badlogic.gdx.math.Matrix4;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GameScreen implements Screen {

    private OrthographicCamera camera;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Texture playerTexture;

    private float playerX, playerY;
    private float speed = 200f;
    private GameWorld gameWorld;

    private Player player;

    private float mapOffsetX;
    private float mapOffsetY;

    private Set<Integer> walkableTiles;
    private float tileSize = 32f;
    private TiledMapTileLayer dungeonLayer;

    private ShapeRenderer shapeRenderer;
    private SpriteBatch uiBatch;
    private BitmapFont font;

    private float rightPanelWidth;
    private float topPanelHeight;
    private float bottomPanelHeight;

    public GameScreen() {
        Level level = new Level(
                Collections.emptyList(),
                Collections.emptyList(),
                new Coord(2, 2),
                null
        );
        this.gameWorld = new GameWorld(level);
        this.player = new Player(0, 0, 3, speed);
        walkableTiles = new HashSet<>();
        walkableTiles.add(31);
        walkableTiles.add(37);
        walkableTiles.add(38);
        walkableTiles.add(39);
        walkableTiles.add(49);
        walkableTiles.add(50);
        walkableTiles.add(51);
        walkableTiles.add(52);
        walkableTiles.add(53);
        walkableTiles.add(54);
    }

    @Override
    public void show() {
        tiledMap = new TmxMapLoader().load("maps/sampleMap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 2f);
        dungeonLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Dungeon");

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        playerTexture = new Texture(Gdx.files.internal("images/smiley/playerboss.png"));

        shapeRenderer = new ShapeRenderer();
        uiBatch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(1.5f);

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        rightPanelWidth = screenWidth * 0.25f;
        topPanelHeight = 80f;
        bottomPanelHeight = 60f;

        float mapWidth = tiledMap.getProperties().get("width", Integer.class) * tileSize;
        float mapHeight = tiledMap.getProperties().get("height", Integer.class) * tileSize;

        float mapAreaWidth = screenWidth - rightPanelWidth;
        float mapAreaHeight = screenHeight - topPanelHeight - bottomPanelHeight;

        mapOffsetX = (mapAreaWidth - mapWidth) / 2f;
        mapOffsetY = (mapAreaHeight - mapHeight) / 2f + bottomPanelHeight;

        int startTileX = 0;
        int startTileY = 22;

        playerX = mapOffsetX + startTileX * tileSize;
        playerY = mapOffsetY + startTileY * tileSize;

        player.setX(playerX);
        player.setY(playerY);
    }

    private boolean canMoveTo(float newX, float newY) {
        float relativeX = newX - mapOffsetX;
        float relativeY = newY - mapOffsetY;

        int[][] corners = {
                {4, 4},
                {28, 4},
                {4, 28},
                {28, 28}
        };

        for (int[] corner : corners) {
            int tx = (int) ((relativeX + corner[0]) / tileSize);
            int ty = (int) ((relativeY + corner[1]) / tileSize);

            if (tx < 0 || ty < 0 ||
                    tx >= tiledMap.getProperties().get("width", Integer.class) ||
                    ty >= tiledMap.getProperties().get("height", Integer.class)) {
                return false;
            }

            if (!isTileWalkable(tx, ty)) {
                return false;
            }
        }
        return true;
    }

    private boolean isTileWalkable(int tileX, int tileY) {
        if (dungeonLayer == null) return true;

        TiledMapTileLayer.Cell cell = dungeonLayer.getCell(tileX, tileY);
        if (cell == null || cell.getTile() == null) return false;

        int id = cell.getTile().getId();
        if (id > 2000) id = id & 0x1FFFFFFF;

        return walkableTiles.contains(id);
    }

    private void handleInput(float delta) {
        float move = speed * delta;
        float newX = playerX;
        float newY = playerY;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) newX -= move;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) newX += move;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) newY += move;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) newY -= move;

        if (Gdx.input.isKeyPressed(Input.Keys.Q)) newX -= move;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) newX += move;
        if (Gdx.input.isKeyPressed(Input.Keys.Z)) newY += move;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) newY -= move;

        if (canMoveTo(newX, playerY)) playerX = newX;
        if (canMoveTo(playerX, newY)) playerY = newY;

        player.setX(playerX);
        player.setY(playerY);
    }

    private void renderUI() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.1f, 0.05f, 0.15f, 1f);
        shapeRenderer.rect(0, screenHeight - topPanelHeight, screenWidth, topPanelHeight);
        shapeRenderer.rect(0, 0, screenWidth - rightPanelWidth, bottomPanelHeight);
        shapeRenderer.setColor(0.15f, 0.08f, 0.2f, 1f);
        shapeRenderer.rect(screenWidth - rightPanelWidth, 0, rightPanelWidth, screenHeight);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(0.4f, 0.3f, 0.5f, 1f);
        shapeRenderer.rect(screenWidth - rightPanelWidth, 0, rightPanelWidth, screenHeight);
        shapeRenderer.line(0, screenHeight - topPanelHeight, screenWidth, screenHeight - topPanelHeight);
        shapeRenderer.line(0, bottomPanelHeight, screenWidth - rightPanelWidth, bottomPanelHeight);
        shapeRenderer.end();

        uiBatch.begin();
        font.setColor(1f, 0.8f, 0.2f, 1f);
        font.draw(uiBatch, "Niveau: 4", 20, screenHeight - 25);
        font.draw(uiBatch, "Score: 15", 200, screenHeight - 25);
        font.draw(uiBatch, "Inventaire", screenWidth - rightPanelWidth + 20, screenHeight - 30);
        font.draw(uiBatch, "Aucune arme", screenWidth - rightPanelWidth + 20, screenHeight - 80);
        font.setColor(0.9f, 0.9f, 0.9f, 1f);
        font.getData().setScale(1f);
        font.draw(uiBatch, "Legende", screenWidth - rightPanelWidth + 20, screenHeight / 2);
        font.draw(uiBatch, "Votre personnage", screenWidth - rightPanelWidth + 20, screenHeight / 2 - 40);
        font.draw(uiBatch, "Coffre au tresor", screenWidth - rightPanelWidth + 20, screenHeight / 2 - 70);
        font.getData().setScale(1.5f);
        font.setColor(1f, 0.8f, 0.2f, 1f);
        font.draw(uiBatch, "Deplacer: Fleches", 20, 30);
        uiBatch.end();
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

        renderUI();
    }

    @Override public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.update();
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        tiledMap.dispose();
        mapRenderer.dispose();
        playerTexture.dispose();
        uiBatch.dispose();
        shapeRenderer.dispose();
        font.dispose();
    }
}
