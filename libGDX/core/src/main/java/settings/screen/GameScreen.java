package settings.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import entity.Player;
import entity.Weapon;
import entity.enemies.Enemy;
import level.Level;
import level.WeaponSpawner;
import settings.GameState;
import settings.GameWorld;
import system.EnemyRenderer;
import utils.Coord;

/**
 * GameScreen fusionné :
 * - HUD + menu pause (ta version)
 * - maps dynamiques + niveaux + armes (version collègue)
 */
public class GameScreen implements Screen {
    private OrthographicCamera camera;
    private OrthographicCamera uiCamera;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMapTileLayer dungeonLayer;
    private TiledMapTileLayer weaponsLayer;

    private float mapOffsetX;
    private float mapOffsetY;
    private float tileSize;
    private float scale = 1f;

    private Texture playerTexture;
    private Player player;
    private float playerX, playerY;
    private float speed = 200f;

    private List<WeaponSpawner.WeaponData> weaponDataList;

    private Set<Integer> walkableTiles;

    private final ScreenManager screenManager;
    private final GameWorld gameWorld;

    private SpriteBatch batch;
    private Texture arrowUp;
    private Texture arrowDown;
    private Texture arrowLeft;
    private Texture arrowRight;
    private Texture keyEsc;
    private Texture keySpace;
    private BitmapFont font;
    private BitmapFont fontTitle;
    private ShapeRenderer shapeRenderer;
    private float leftPanelWidth;
    private float rightPanelWidth;
    private TextureRegion chestIcon;

    private boolean pendingPause = false;
    private boolean initialized = false;
    private EnemyRenderer enemyRenderer;


    public GameScreen(ScreenManager screenManager, GameWorld gameWorld) {
        this.screenManager = screenManager;
        this.gameWorld = gameWorld;

        this.player = gameWorld.getPlayer();
        this.walkableTiles = new HashSet<>();
        this.weaponDataList = new ArrayList<>();
        enemyRenderer = new EnemyRenderer();
    }

    @Override
    public void show() {
        if (!initialized) {
            batch = new SpriteBatch();
            shapeRenderer = new ShapeRenderer();
            camera = new OrthographicCamera();
            camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            uiCamera = new OrthographicCamera();
            uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            int smiley = screenManager.getSelectedSmiley();
            playerTexture = new Texture("images/smiley/smiley_" + smiley + ".png");

            arrowUp = new Texture("images/ui/arrow_up.png");
            arrowDown = new Texture("images/ui/arrow_down.png");
            arrowLeft = new Texture("images/ui/arrow_left.png");
            arrowRight = new Texture("images/ui/arrow_right.png");
            keyEsc = new Texture("images/ui/echap.png");
            keySpace = new Texture("images/ui/attack.png");

            FreeTypeFontGenerator gen =
                    new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P-Regular.ttf"));

            FreeTypeFontGenerator.FreeTypeFontParameter p =
                    new FreeTypeFontGenerator.FreeTypeFontParameter();
            p.size = 24;
            p.color = Color.WHITE;
            font = gen.generateFont(p);

            FreeTypeFontGenerator.FreeTypeFontParameter pt =
                    new FreeTypeFontGenerator.FreeTypeFontParameter();
            pt.size = 30;
            pt.borderWidth = 3f;
            pt.borderColor = Color.BLACK;
            pt.color = Color.WHITE;
            fontTitle = gen.generateFont(pt);

            gen.dispose();

            float screenW = Gdx.graphics.getWidth();
            leftPanelWidth = screenW * 0.20f;
            rightPanelWidth = screenW * 0.25f;

            loadLevel(gameWorld.getCurrentLevel());

            initialized = true;
        }
    }
    /**
     * Charge une map Tiled + configure la map, les offsets, le spawn, les armes, etc.
     */
    private void loadLevel(Level lvl) {
        if (tiledMap != null) {
            tiledMap.dispose();
        }

        tiledMap = new TmxMapLoader().load(lvl.getMapFile());

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
            int mapWidthTiles = tiledMap.getProperties().get("width", Integer.class);
            int mapHeightTiles = tiledMap.getProperties().get("height", Integer.class);
            weaponsLayer = new TiledMapTileLayer(mapWidthTiles, mapHeightTiles, tileWidth, tileWidth);
            tiledMap.getLayers().add(weaponsLayer);
        }

        configureWalkableTiles();

        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();
        int mapWidthTiles = tiledMap.getProperties().get("width", Integer.class);
        int mapHeightTiles = tiledMap.getProperties().get("height", Integer.class);
        float mapWidth = mapWidthTiles * tileSize;
        float mapHeight = mapHeightTiles * tileSize;

        float mapZoneW = screenW - leftPanelWidth - rightPanelWidth;
        mapOffsetX = leftPanelWidth + (mapZoneW - mapWidth) / 2f;
        mapOffsetY = (screenH - mapHeight) / 2f;

        Coord spawn = lvl.getPlayerSpawn();
        playerX = mapOffsetX + spawn.getX() * tileSize;
        playerY = mapOffsetY + spawn.getY() * tileSize;

        player.setX(playerX);
        player.setY(playerY);

        TiledMapTile tile = tiledMap.getTileSets().getTile(90);
        if (tile != null) {
            chestIcon = tile.getTextureRegion();
        }
        WeaponSpawner spawner = new WeaponSpawner();
        weaponDataList = spawner.spawnWeapons(tiledMap, 8);
        placeWeaponsOnMap();

        System.out.println("➡ Niveau chargé : " + lvl.getMapFile());
    }
    /**
     * Place les armes générées sur la couche Weapons.
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

    /**
     * Configure les tiles marchables selon la map.
     */
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
            walkableTiles.add(68);
            walkableTiles.add(38);
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
        } else if (mapFile.contains("map4")){
            walkableTiles.add(1);
            walkableTiles.add(40);
            walkableTiles.add(70);
        } else if (mapFile.contains("final-map")){
            walkableTiles.add(7);
            walkableTiles.add(68);
        } else {
            walkableTiles.add(1);
        }
    }

    /**
     * Vérifie si le joueur peut se déplacer à une nouvelle position.
     */
    private boolean canMoveTo(float newX, float newY) {
        float relativeX = newX - mapOffsetX;
        float relativeY = newY - mapOffsetY;

        int mapWidthTiles = tiledMap.getProperties().get("width", Integer.class);
        int mapHeightTiles = tiledMap.getProperties().get("height", Integer.class);
        float mapWidth = mapWidthTiles * tileSize;
        float mapHeight = mapHeightTiles * tileSize;

        if (relativeX < 0 || relativeY < 0 ||
                relativeX + 32 > mapWidth || relativeY + 32 > mapHeight)
            return false;
        int[][] corners = {
                {4, 4},
                {28, 4},
                {4, 28},
                {28, 28}
        };
        for (int[] c : corners) {
            int tx = (int) ((relativeX + c[0]) / tileSize);
            int ty = (int) ((relativeY + c[1]) / tileSize);
            if (!isTileWalkable(tx, ty)) return false;
        }
        return true;
    }
    /**
     * Retourne si une tile est marchable (en fonction du dungeonLayer + walkableTiles).
     */
    private boolean isTileWalkable(int tileX, int tileY) {
        if (dungeonLayer == null) return true;
        TiledMapTileLayer.Cell cell = dungeonLayer.getCell(tileX, tileY);
        if (cell == null || cell.getTile() == null) return false;
        int id = cell.getTile().getId();
        if (id > 2000) id &= 0x1FFFFFFF;
        return walkableTiles.contains(id);
    }
    /**
     * Gère les inputs (mouvements + pause).
     */
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

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) pendingPause = true;

        player.setX(playerX);
        player.setY(playerY);
    }
    /**
     * Vérifie si le joueur ramasse une arme.
     */
    private void checkWeaponCollisions() {
        if (weaponDataList == null || weaponDataList.isEmpty()) return;

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
        mapRenderer.getBatch().setTransformMatrix(
                mapRenderer.getBatch().getTransformMatrix().idt().translate(mapOffsetX, mapOffsetY, 0)
        );
        mapRenderer.setView(camera);
        mapRenderer.render();
        mapRenderer.getBatch().setTransformMatrix(
                mapRenderer.getBatch().getTransformMatrix().idt()
        );
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(playerTexture, playerX, playerY, 32, 32);
        batch.end();
        drawHUD();
        Level currentLevel = gameWorld.getCurrentLevel();
        if (currentLevel.isCleared(playerX - mapOffsetX, playerY - mapOffsetY, tileSize)) {
            if (gameWorld.nextLevel()) {
                loadLevel(gameWorld.getCurrentLevel());
            } else {
                screenManager.setState(GameState.GAME_OVER);
            }
        }
        if (pendingPause) {
            pendingPause = false;
            screenManager.setState(GameState.PAUSED);
        }

        for (Enemy enemy : gameWorld.getCurrentLevel().getEnemies()) {
            enemyRenderer.render(batch, enemy);
        }
    }

    private void drawBlock(float x, float y, float w, float h) {
        shapeRenderer.setColor(0, 0, 0, 0.35f);
        shapeRenderer.rect(x, y, w, h);
        shapeRenderer.setColor(1, 1, 1, 0.20f);
        shapeRenderer.rect(x, y, w, 3);
    }

    private void drawHUD() {
        float W = Gdx.graphics.getWidth();
        float H = Gdx.graphics.getHeight();
        float leftMargin = 20f;
        float rightMargin = 20f;
        float leftBlockWidth = leftPanelWidth - 2 * leftMargin;
        float rightBlockWidth = rightPanelWidth - 2 * rightMargin;
        float blockHeightStatut = 190f;
        float blockHeightCmd = 600f;
        float blockHeightInv = 190f;
        float blockHeightLegend = 250f;
        float leftX = leftMargin;
        float rightX = W - rightPanelWidth + rightMargin;
        float topMargin = 40f;
        float gapBlocks = 40f;
        float statutY = H - topMargin - blockHeightStatut;
        float cmdY = statutY - gapBlocks - blockHeightCmd;
        float invY = H - topMargin - blockHeightInv;
        float legendY = invY - gapBlocks - blockHeightLegend - 30;

        shapeRenderer.setProjectionMatrix(uiCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1f, 0.92f, 0.23f, 1f);
        shapeRenderer.rect(0, 0, leftPanelWidth, H);
        shapeRenderer.setColor(0.95f, 0.75f, 0.20f, 1f);
        shapeRenderer.rect(W - rightPanelWidth, 0, rightPanelWidth, H);

        drawBlock(leftX, statutY, leftBlockWidth, blockHeightStatut);
        drawBlock(leftX, cmdY, leftBlockWidth, blockHeightCmd);

        drawBlock(rightX, invY, rightBlockWidth, blockHeightInv);
        drawBlock(rightX, legendY, rightBlockWidth, blockHeightLegend);

        float keyW = 60;
        float keyH = 60;

        float keysX = leftX + leftBlockWidth / 2f - 100;
        float keysY = cmdY + blockHeightCmd - 150;

        shapeRenderer.setColor(0.85f, 0.85f, 0.85f, 1f);
        shapeRenderer.rect(keysX + 70, keysY, keyW, keyH);
        shapeRenderer.rect(keysX,        keysY - 80, keyW, keyH);
        shapeRenderer.rect(keysX + 70,   keysY - 80, keyW, keyH);
        shapeRenderer.rect(keysX + 140,  keysY - 80, keyW, keyH);

        float lineY = keysY - 200;
        float escBoxX = keysX + 10;
        float spaceBoxX = escBoxX + 150;

        shapeRenderer.rect(escBoxX, lineY, keyW, keyH);
        shapeRenderer.rect(spaceBoxX, lineY, keyW, keyH);
        shapeRenderer.end();

        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();

        fontTitle.draw(batch, "STATUT", leftX + 20, statutY + blockHeightStatut - 25);

        font.draw(batch,
                "Niveau : " + gameWorld.getCurrentLevelNumber() + "/" + gameWorld.getTotalLevels(),
                leftX + 25, statutY + blockHeightStatut - 80);
        font.draw(batch,
                "Score  : " + gameWorld.getScoreSystem().getScore(),
                leftX + 25, statutY + blockHeightStatut - 130);


        fontTitle.draw(batch, "Commandes", leftX + 20, cmdY + blockHeightCmd - 25);

        batch.draw(arrowUp,      keysX + 70 - 2, keysY + (keyH - 64) / 2f, 64, 64);
        batch.draw(arrowLeft,    keysX - 2,      keysY - 80 + (keyH - 64) / 2f, 64, 64);
        batch.draw(arrowDown,    keysX + 70 - 2, keysY - 80 + (keyH - 64) / 2f, 64, 64);
        batch.draw(arrowRight,   keysX + 140 - 2, keysY - 80 + (keyH - 64) / 2f, 64, 64);

        float escImgX = escBoxX;
        float escImgY = lineY;
        batch.draw(keyEsc, escImgX, escImgY, 64, 64);
        font.draw(batch, "Pause", escImgX - 25, escImgY - 15);

        float spaceImgX = spaceBoxX;
        float spaceImgY = lineY;
        batch.draw(keySpace, spaceImgX, spaceImgY, 64, 64);
        font.draw(batch, "Attack", spaceImgX - 35, spaceImgY - 15);

        float rightXtxt = rightX + 20;

        fontTitle.draw(batch, "INVENTAIRE", rightXtxt, invY + blockHeightInv - 25);
        font.draw(batch, "Aucune arme", rightXtxt, invY + blockHeightInv - 80);

        fontTitle.draw(batch, "LEGENDE", rightXtxt, legendY + blockHeightLegend - 25);

        float legendContentY = legendY + blockHeightLegend - 90;
        font.draw(batch, "Joueur:", rightXtxt, legendContentY);
        batch.draw(playerTexture, rightXtxt + 230, legendContentY - 35, 60, 60);

        legendContentY -= 100;
        font.draw(batch, "Objectif:", rightXtxt, legendContentY);
        if (chestIcon != null) {
            batch.draw(chestIcon, rightXtxt + 230, legendContentY - 35, 60, 60);
        }

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        uiCamera.setToOrtho(false, width, height);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (tiledMap != null) tiledMap.dispose();
        if (mapRenderer != null) mapRenderer.dispose();
        if (playerTexture != null) playerTexture.dispose();
        if (font != null) font.dispose();
        if (fontTitle != null) fontTitle.dispose();
        if (arrowUp != null) arrowUp.dispose();
        if (arrowDown != null) arrowDown.dispose();
        if (arrowLeft != null) arrowLeft.dispose();
        if (arrowRight != null) arrowRight.dispose();
        if (keyEsc != null) keyEsc.dispose();
        if (keySpace != null) keySpace.dispose();
    }
}
