package settings.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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
    private float tileSize;
    private TiledMapTileLayer dungeonLayer;
    private float scale;

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
//        walkableTiles.add(31);
//        walkableTiles.add(37);
//        walkableTiles.add(38);
//        walkableTiles.add(39);
//        walkableTiles.add(49);
//        walkableTiles.add(50);
//        walkableTiles.add(51);
//        walkableTiles.add(52);
//        walkableTiles.add(53);
//        walkableTiles.add(54);
    }

    @Override
    public void show() {
        tiledMap = new TmxMapLoader().load(gameWorld.getCurrentLevel().getMapFile());

        int tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        scale = (tileWidth <= 16) ? 2.0f : 1.0f;
        tileSize = tileWidth * scale;

        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, scale);

        dungeonLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Dungeon");

        if (dungeonLayer == null && tiledMap.getLayers().getCount() > 0) {
            dungeonLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        }

        configureWalkableTiles();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        playerTexture = new Texture(Gdx.files.internal("images/smiley/player.png"));

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float mapWidth = tiledMap.getProperties().get("width", Integer.class) * tileSize;
        float mapHeight = tiledMap.getProperties().get("height", Integer.class) * tileSize;

        mapOffsetX = (screenWidth * 0.75f - mapWidth) / 2f;
        mapOffsetY = (screenHeight - mapHeight) / 2f;


        Level currentLevel = gameWorld.getCurrentLevel();
        Coord spawn = currentLevel.getPlayerSpawn();
        playerX = mapOffsetX + spawn.getX() * tileSize;
        playerY = mapOffsetY + spawn.getY() * tileSize;

        player.setX(playerX);
        player.setY(playerY);

        System.out.println("GameScreen show() : map chargée, player initialisé.");
        System.out.println("TileSize: " + tileSize + " (scale: " + scale + ")");
        System.out.println("GameScreen show() : map centrée gauche (" + mapOffsetX + ", " + mapOffsetY + ")");
    }

    private void configureWalkableTiles() {
        walkableTiles.clear();

        String mapFile = gameWorld.getCurrentLevel().getMapFile();

        if (mapFile.contains("sampleMap")) {
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
            System.out.println("Configuration : Map complexe (sampleMap)");

        } else if (mapFile.contains("laby")) {
            walkableTiles.add(1);
            System.out.println("Configuration : Map simple");
        } else if (mapFile.contains("mapdebutant")) {
            walkableTiles.add(961);
            walkableTiles.add(963);
            walkableTiles.add(964);
            walkableTiles.add(967);
        } else if (mapFile.contains("map2") ){
            walkableTiles.add(323);
        } else {
            System.out.println("Map inconnue, configuration par défaut");
            walkableTiles.add(1);
        }
    }

    private boolean canMoveTo(float newX, float newY) {

        float relativeX = newX - mapOffsetX;
        float relativeY = newY - mapOffsetY;

        float mapWidth = tiledMap.getProperties().get("width", Integer.class) * tileSize;
        float mapHeight = tiledMap.getProperties().get("height", Integer.class) * tileSize;

        if (relativeX < 0 || relativeY < 0 ||
                relativeX + 32 > mapWidth || relativeY + 32 > mapHeight) {
            return false;
        }

        int[][] corners = {
                {4, 4},
                {28, 4},
                {4, 28},
                {28, 28}
        };

        for (int[] corner : corners) {
            int tileX = (int) ((relativeX + corner[0]) / tileSize);
            int tileY = (int) ((relativeY + corner[1]) / tileSize);

            if (tileX < 0 || tileY < 0 ||
                    tileX >= tiledMap.getProperties().get("width", Integer.class) ||
                    tileY >= tiledMap.getProperties().get("height", Integer.class)) {
                return false;
            }

            if (!isTileWalkable(tileX, tileY)) {
                return false;
            }
        }

        return true;
    }

    private boolean isTileWalkable(int tileX, int tileY) {
        if (dungeonLayer == null) {
            return true;
        }

        TiledMapTileLayer.Cell cell = dungeonLayer.getCell(tileX, tileY);

        if (cell == null || cell.getTile() == null) {
            return false;
        }

        int tileId = cell.getTile().getId();

        if (tileId > 2000) {
            tileId = tileId & 0x1FFFFFFF;
        }

        return walkableTiles.contains(tileId);
    }

    private void handleInput(float delta) {
        float move = speed * delta;
        float newX = playerX;
        float newY = playerY;

        //Mode fleches
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))  newX -= move;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) newX += move;
        if (Gdx.input.isKeyPressed(Input.Keys.UP))    newY += move;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))  newY -= move;

        //Mode AZERTY
        if (Gdx.input.isKeyPressed(Input.Keys.A))  newX -= move;
        if (Gdx.input.isKeyPressed(Input.Keys.D))  newX += move;
        if (Gdx.input.isKeyPressed(Input.Keys.W))  newY += move;
        if (Gdx.input.isKeyPressed(Input.Keys.S))  newY -= move;

        //Mode QWERTY
        if (Gdx.input.isKeyPressed(Input.Keys.Q))  newX -= move;
        if (Gdx.input.isKeyPressed(Input.Keys.Z))  newY += move;

        if (canMoveTo(newX, playerY)) {
            playerX = newX;
        }
        if (canMoveTo(playerX, newY)) {
            playerY = newY;
        }

        player.setX(playerX);
        player.setY(playerY);
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

        Level currentLevel = gameWorld.getCurrentLevel();
        if (currentLevel.isCleared(playerX - mapOffsetX, playerY - mapOffsetY, tileSize)) {
            if (gameWorld.nextLevel()) {
                System.out.println("Niveau suivant !");
                dispose();
                show();
                return;
            } else {
                System.out.println("Tous les niveaux sont terminés !");
            }
        }
    }

    public Player getPlayer() {
        return player;
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        if (tiledMap != null) tiledMap.dispose();
        if (mapRenderer != null) mapRenderer.dispose();
        if (playerTexture != null) playerTexture.dispose();
    }
}