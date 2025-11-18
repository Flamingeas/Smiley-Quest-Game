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
import com.badlogic.gdx.math.Matrix4;

import settings.GameWorld;
import level.Level;
import level.WeaponSpawner;
import utils.Coord;
import entity.Player;
import entity.Weapon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Écran principal du jeu qui gère l'affichage de la map, du joueur et des armes.
 */
public class GameScreen implements Screen {

    /** Caméra pour afficher la map et le joueur */
    private OrthographicCamera camera;

    /** Map chargée depuis le fichier Tiled */
    private TiledMap tiledMap;

    /** Renderer pour afficher la map orthogonale */
    private OrthogonalTiledMapRenderer mapRenderer;

    /** Texture du joueur */
    private Texture playerTexture;

    /** Position du joueur à l'écran */
    private float playerX, playerY;

    /** Vitesse du joueur */
    private float speed = 200f;

    /** Monde du jeu */
    private GameWorld gameWorld;

    /** Objet joueur */
    private Player player;

    /** Décalage pour centrer la map à l'écran */
    private float mapOffsetX;
    private float mapOffsetY;

    /** Ensemble des IDs de tiles où le joueur peut marcher */
    private Set<Integer> walkableTiles;

    /** Taille d'une tile en pixels */
    private float tileSize;

    /** Couche principale de la map (dungeon) */
    private TiledMapTileLayer dungeonLayer;

    /** Échelle de la map selon la taille des tiles */
    private float scale;

    /** Liste des armes sur la map */
    private List<WeaponSpawner.WeaponData> weaponDataList;

    /** Couche des armes sur la map */
    private TiledMapTileLayer weaponsLayer;

    /**
     * Initialise le GameScreen avec un niveau par défaut et un joueur.
     */
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
        weaponDataList = new ArrayList<>();
    }

    /**
     * Charge la map, initialise la caméra et place le joueur et les armes.
     */
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

        weaponsLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Weapons");
        if (weaponsLayer == null) {
            int mapWidth = tiledMap.getProperties().get("width", Integer.class);
            int mapHeight = tiledMap.getProperties().get("height", Integer.class);
            weaponsLayer = new TiledMapTileLayer(mapWidth, mapHeight, tileWidth, tileWidth);
            tiledMap.getLayers().add(weaponsLayer);
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

        Coord spawn = gameWorld.getCurrentLevel().getPlayerSpawn();
        playerX = mapOffsetX + spawn.getX() * tileSize;
        playerY = mapOffsetY + spawn.getY() * tileSize;

        player.setX(playerX);
        player.setY(playerY);

        WeaponSpawner spawner = new WeaponSpawner();
        weaponDataList = spawner.spawnWeapons(tiledMap, 8);

        placeWeaponsOnMap();
    }

    /**
     * Place les armes générées sur la couche Weapons de la map.
     */
    private void placeWeaponsOnMap() {
        for (WeaponSpawner.WeaponData weaponData : weaponDataList) {
            Coord pos = weaponData.getTilePosition();
            int tileId = weaponData.getTileId();
            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            cell.setTile(tiledMap.getTileSets().getTile(tileId));
            weaponsLayer.setCell((int) pos.getX(), (int) pos.getY(), cell);
        }
    }

    /** Configure les tiles où le joueur peut marcher selon la map */
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
        } else if (mapFile.contains("laby")) {
            walkableTiles.add(1);
        } else if (mapFile.contains("mapdebutant")) {
            walkableTiles.add(961);
            walkableTiles.add(963);
            walkableTiles.add(964);
            walkableTiles.add(967);
        } else if (mapFile.contains("map2")) {
            walkableTiles.add(323);
            walkableTiles.add(810);
            walkableTiles.add(405);
            walkableTiles.add(385);
        } else {
            walkableTiles.add(1);
        }
    }

    /**
     * Vérifie si le joueur peut se déplacer à une nouvelle position.
     * @param newX position X cible
     * @param newY position Y cible
     * @return true si le joueur peut marcher à cet endroit
     */
    private boolean canMoveTo(float newX, float newY) {
        float relativeX = newX - mapOffsetX;
        float relativeY = newY - mapOffsetY;
        float mapWidth = tiledMap.getProperties().get("width", Integer.class) * tileSize;
        float mapHeight = tiledMap.getProperties().get("height", Integer.class) * tileSize;

        if (relativeX < 0 || relativeY < 0 || relativeX + 32 > mapWidth || relativeY + 32 > mapHeight)
            return false;

        int[][] corners = {
                {4, 4},
                {28, 4},
                {4, 28},
                {28, 28}
        };

        for (int[] corner : corners) {
            int tileX = (int) ((relativeX + corner[0]) / tileSize);
            int tileY = (int) ((relativeY + corner[1]) / tileSize);
            if (!isTileWalkable(tileX, tileY)) return false;
        }
        return true;
    }

    /**
     * Vérifie si une tile est marchable.
     * @param tileX coordonnées X de la tile
     * @param tileY coordonnées Y de la tile
     * @return true si la tile est marchable
     */
    private boolean isTileWalkable(int tileX, int tileY) {
        if (dungeonLayer == null) return true;
        TiledMapTileLayer.Cell cell = dungeonLayer.getCell(tileX, tileY);
        if (cell == null || cell.getTile() == null) return false;
        int tileId = cell.getTile().getId();
        if (tileId > 2000) tileId = tileId & 0x1FFFFFFF;
        return walkableTiles.contains(tileId);
    }

    /** Gère les déplacements du joueur selon les touches */
    private void handleInput(float delta) {
        float move = speed * delta;
        float newX = playerX;
        float newY = playerY;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) newX -= move;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) newX += move;
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) newY += move;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) newY -= move;

        if (canMoveTo(newX, playerY)) playerX = newX;
        if (canMoveTo(playerX, newY)) playerY = newY;

        player.setX(playerX);
        player.setY(playerY);
    }

    /**
     * Vérifie si le joueur ramasse une arme et met à jour la map.
     */
    private void checkWeaponCollisions() {
        float relativeX = playerX - mapOffsetX;
        float relativeY = playerY - mapOffsetY;
        int playerTileX = (int) (relativeX / tileSize);
        int playerTileY = (int) (relativeY / tileSize);

        for (int i = weaponDataList.size() - 1; i >= 0; i--) {
            WeaponSpawner.WeaponData weaponData = weaponDataList.get(i);
            Weapon weapon = weaponData.getWeapon();
            if (!weapon.isActive()) continue;

            Coord weaponPos = weaponData.getTilePosition();
            if (playerTileX == (int) weaponPos.getX() && playerTileY == (int) weaponPos.getY()) {
                weapon.collectItem(player);
                weaponsLayer.setCell((int) weaponPos.getX(), (int) weaponPos.getY(), null);
            }
        }
    }

    @Override
    public void render(float delta) {
        handleInput(delta);
        gameWorld.update(delta);
        checkWeaponCollisions();

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
                dispose();
                show();
            }
        }
    }

    /** Retourne le joueur */
    public Player getPlayer() {
        return player;
    }

    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}

    @Override
    public void dispose() {
        if (tiledMap != null) tiledMap.dispose();
        if (mapRenderer != null) mapRenderer.dispose();
        if (playerTexture != null) playerTexture.dispose();
    }
}
