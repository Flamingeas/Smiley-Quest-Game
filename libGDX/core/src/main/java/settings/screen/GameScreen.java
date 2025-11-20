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

    private Texture arrowUp, arrowDown, arrowLeft, arrowRight;
    private Texture keyEsc, keySpace;

    private BitmapFont font, fontTitle;
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
        this.enemyRenderer = new EnemyRenderer();

        this.gameWorld.setGameScreen(this);
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

    private void loadLevel(Level lvl) {

        if (tiledMap != null)
            tiledMap.dispose();

        tiledMap = new TmxMapLoader().load(lvl.getMapFile());

        int widthTiles = tiledMap.getProperties().get("width", Integer.class);
        int heightTiles = tiledMap.getProperties().get("height", Integer.class);
        lvl.setMapSize(widthTiles, heightTiles);

        int tileWidth = tiledMap.getProperties().get("tilewidth", Integer.class);
        scale = (tileWidth <= 16) ? 2f : 1f;
        tileSize = tileWidth * scale;

        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, scale);

        dungeonLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Dungeon");
        if (dungeonLayer == null)
            dungeonLayer = (TiledMapTileLayer) tiledMap.getLayers().get(0);

        weaponsLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Weapons");
        if (weaponsLayer == null) {
            weaponsLayer = new TiledMapTileLayer(widthTiles, heightTiles, tileWidth, tileWidth);
            tiledMap.getLayers().add(weaponsLayer);
        }

        configureWalkableTiles();

        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        float worldW = widthTiles * tileSize;
        float worldH = heightTiles * tileSize;

        float mapZoneW = screenW - leftPanelWidth - rightPanelWidth;

        mapOffsetX = leftPanelWidth + (mapZoneW - worldW) / 2f;
        mapOffsetY = (screenH - worldH) / 2f;

        Coord spawn = lvl.getPlayerSpawn();
        playerX = mapOffsetX + spawn.getX() * tileSize;
        playerY = mapOffsetY + spawn.getY() * tileSize;

        player.setX(playerX);
        player.setY(playerY);

        TiledMapTile chest = tiledMap.getTileSets().getTile(90);
        if (chest != null)
            chestIcon = chest.getTextureRegion();

        WeaponSpawner spawner = new WeaponSpawner();
        weaponDataList = spawner.spawnWeapons(tiledMap, 8);

        placeWeaponsOnMap();

        System.out.println("➡ Level loaded: " + lvl.getMapFile());

        for (Enemy enemy : gameWorld.getEnemies()) {

            enemy.setPixelPosition(
                    mapOffsetX + enemy.tileX * tileSize,
                    mapOffsetY + enemy.tileY * tileSize
            );
        }

    }

    /** UTILITÉ : Permet à GameWorld de vérifier si un tile est walkable */
    public boolean isWalkableTileRaw(int tileX, int tileY) {
        if (dungeonLayer == null) return false;

        TiledMapTileLayer.Cell cell = dungeonLayer.getCell(tileX, tileY);
        if (cell == null || cell.getTile() == null) return false;

        int id = cell.getTile().getId();
        if (id > 2000) id &= 0x1FFFFFFF;

        return walkableTiles.contains(id);
    }
    public float getMapOffsetX() {
        return mapOffsetX;
    }

    public float getMapOffsetY() {
        return mapOffsetY;
    }

    public float getTileSize() {
        return tileSize;
    }

    private void placeWeaponsOnMap() {
        for (WeaponSpawner.WeaponData data : weaponDataList) {
            Coord pos = data.getTilePosition();
            int id = data.getTileId();

            TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
            cell.setTile(tiledMap.getTileSets().getTile(id));

            weaponsLayer.setCell((int) pos.getX(), (int) pos.getY(), cell);
        }
    }

    private void configureWalkableTiles() {
        walkableTiles.clear();
        String map = gameWorld.getCurrentLevel().getMapFile();

        if (map.contains("sampleMap")) {
            walkableTiles.add(31); walkableTiles.add(37);
            walkableTiles.add(38); walkableTiles.add(39);
            walkableTiles.add(49); walkableTiles.add(50);
            walkableTiles.add(51); walkableTiles.add(52);
            walkableTiles.add(53); walkableTiles.add(54);

        } else if (map.contains("laby")) {
            walkableTiles.add(68); walkableTiles.add(38);

        } else if (map.contains("mapdebutant")) {
            walkableTiles.add(961); walkableTiles.add(963);
            walkableTiles.add(964); walkableTiles.add(967);

        } else if (map.contains("map4")) {
            walkableTiles.add(1); walkableTiles.add(40); walkableTiles.add(70);

        } else if (map.contains("final-map")) {
            walkableTiles.add(7); walkableTiles.add(68);

        } else {
            walkableTiles.add(1);
        }
    }

    private boolean canMoveTo(float newX, float newY) {

        float rx = newX - mapOffsetX;
        float ry = newY - mapOffsetY;

        Level lvl = gameWorld.getCurrentLevel();

        float worldW = lvl.getMapWidth() * tileSize;
        float worldH = lvl.getMapHeight() * tileSize;

        if (rx < 0 || ry < 0 || rx + 32 > worldW || ry + 32 > worldH)
            return false;

        int[][] corners = {{4,4},{28,4},{4,28},{28,28}};

        for (int[] c : corners) {
            int tx = (int) ((rx + c[0]) / tileSize);
            int ty = (int) ((ry + c[1]) / tileSize);

            if (!isTileWalkable(tx, ty))
                return false;
        }

        return true;
    }
    public boolean canMoveGeneric(float posX, float posY) {

        float rx = posX - mapOffsetX;
        float ry = posY - mapOffsetY;

        Level lvl = gameWorld.getCurrentLevel();

        float worldW = lvl.getMapWidth() * tileSize;
        float worldH = lvl.getMapHeight() * tileSize;

        if (rx < 0 || ry < 0 || rx + 32 > worldW || ry + 32 > worldH)
            return false;

        int[][] corners = {{4,4},{28,4},{4,28},{28,28}};

        for (int[] c : corners) {
            int tx = (int)((rx + c[0]) / tileSize);
            int ty = (int)((ry + c[1]) / tileSize);

            if (!isTileWalkable(tx, ty))
                return false;
        }

        return true;
    }

    private boolean isTileWalkable(int tileX, int tileY) {
        if (dungeonLayer == null) return false;

        TiledMapTileLayer.Cell cell = dungeonLayer.getCell(tileX, tileY);
        if (cell == null || cell.getTile() == null)
            return false;

        int id = cell.getTile().getId();

        if (id > 2000)
            id &= 0x1FFFFFFF;

        return walkableTiles.contains(id);
    }

    private void handleInput(float delta) {

        float move = speed * delta;
        float newX = playerX;
        float newY = playerY;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) ||
                Gdx.input.isKeyPressed(Input.Keys.A))
            newX -= move;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) ||
                Gdx.input.isKeyPressed(Input.Keys.D))
            newX += move;

        if (Gdx.input.isKeyPressed(Input.Keys.UP) ||
                Gdx.input.isKeyPressed(Input.Keys.W))
            newY += move;

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) ||
                Gdx.input.isKeyPressed(Input.Keys.S))
            newY -= move;

        if (canMoveTo(newX, playerY)) playerX = newX;
        if (canMoveTo(playerX, newY)) playerY = newY;

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
            pendingPause = true;

        player.setX(playerX);
        player.setY(playerY);
    }

    private void checkWeaponCollisions() {
        if (weaponDataList == null || weaponDataList.isEmpty()) return;

        float rx = playerX - mapOffsetX;
        float ry = playerY - mapOffsetY;

        int px = (int) (rx / tileSize);
        int py = (int) (ry / tileSize);

        for (int i = weaponDataList.size() - 1; i >= 0; i--) {

            WeaponSpawner.WeaponData data = weaponDataList.get(i);
            Weapon weapon = data.getWeapon();

            if (!weapon.isActive()) continue;

            Coord pos = data.getTilePosition();

            if (px == (int) pos.getX() && py == (int) pos.getY()) {

                weapon.collectItem(player);

                weaponsLayer.setCell(px, py, null);
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
                mapRenderer.getBatch().getTransformMatrix().idt()
                        .translate(mapOffsetX, mapOffsetY, 0)
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

        Level lvl = gameWorld.getCurrentLevel();

        if (lvl.isCleared(playerX - mapOffsetX, playerY - mapOffsetY, tileSize)) {
            if (gameWorld.nextLevel())
                loadLevel(gameWorld.getCurrentLevel());
            else
                screenManager.setState(GameState.GAME_OVER);
        }

        if (pendingPause) {
            pendingPause = false;
            screenManager.setState(GameState.PAUSED);
        }

        batch.begin();
        for (Enemy enemy : gameWorld.getEnemies()) {
            enemyRenderer.render(batch, enemy);
        }
        batch.end();
    }

    private void drawBlock(float x, float y, float w, float h) {
        shapeRenderer.setColor(0,0,0,0.35f);
        shapeRenderer.rect(x, y, w, h);
        shapeRenderer.setColor(1,1,1,0.20f);
        shapeRenderer.rect(x, y, w, 3);
    }

    private void drawHUD() {

        float W = Gdx.graphics.getWidth();
        float H = Gdx.graphics.getHeight();

        float leftMargin = 20;
        float rightMargin = 20;

        float leftBlockWidth = leftPanelWidth - 2 * leftMargin;
        float rightBlockWidth = rightPanelWidth - 2 * rightMargin;

        float blockHStatut = 190;
        float blockHCmd = 600;
        float blockHInv = 190;
        float blockHLeg = 250;

        float leftX = leftMargin;
        float rightX = W - rightPanelWidth + rightMargin;

        float topMargin = 40;
        float gap = 40;

        float statutY = H - topMargin - blockHStatut;
        float cmdY    = statutY - gap - blockHCmd;
        float invY    = H - topMargin - blockHInv;
        float legY    = invY - gap - blockHLeg - 30;

        shapeRenderer.setProjectionMatrix(uiCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(1f, 0.92f, 0.23f, 1f);
        shapeRenderer.rect(0, 0, leftPanelWidth, H);

        shapeRenderer.setColor(0.95f, 0.75f, 0.20f, 1f);
        shapeRenderer.rect(W - rightPanelWidth, 0, rightPanelWidth, H);

        drawBlock(leftX, statutY, leftBlockWidth, blockHStatut);
        drawBlock(leftX, cmdY, leftBlockWidth, blockHCmd);

        drawBlock(rightX, invY, rightBlockWidth, blockHInv);
        drawBlock(rightX, legY, rightBlockWidth, blockHLeg);

        float keyW = 60, keyH = 60;
        float keysX = leftX + leftBlockWidth / 2f - 100;
        float keysY = cmdY + blockHCmd - 150;

        shapeRenderer.setColor(0.85f, 0.85f, 0.85f, 1f);
        shapeRenderer.rect(keysX + 70, keysY, keyW, keyH);
        shapeRenderer.rect(keysX, keysY - 80, keyW, keyH);
        shapeRenderer.rect(keysX + 70, keysY - 80, keyW, keyH);
        shapeRenderer.rect(keysX + 140, keysY - 80, keyW, keyH);

        float lineY = keysY - 200;
        float escX = keysX + 10;
        float spaceX = escX + 150;

        shapeRenderer.rect(escX, lineY, keyW, keyH);
        shapeRenderer.rect(spaceX, lineY, keyW, keyH);

        shapeRenderer.end();

        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();

        fontTitle.draw(batch, "STATUS", leftX + 20, statutY + blockHStatut - 25);

        font.draw(batch,
                "Level : " + gameWorld.getCurrentLevelNumber() + "/" + gameWorld.getTotalLevels(),
                leftX + 25, statutY + blockHStatut - 80);

        font.draw(batch,
                "Score : " + gameWorld.getScoreSystem().getScore(),
                leftX + 25, statutY + blockHStatut - 130);

        fontTitle.draw(batch, "COMMANDS", leftX + 20, cmdY + blockHCmd - 25);

        batch.draw(arrowUp, keysX + 70 - 2, keysY + (keyH - 64) / 2f, 64, 64);
        batch.draw(arrowLeft, keysX - 2, keysY - 80 + (keyH - 64) / 2f, 64, 64);
        batch.draw(arrowDown, keysX + 70 - 2, keysY - 80 + (keyH - 64) / 2f, 64, 64);
        batch.draw(arrowRight, keysX + 140 - 2, keysY - 80 + (keyH - 64) / 2f, 64, 64);

        batch.draw(keyEsc, escX, lineY, 64, 64);
        font.draw(batch, "Pause", escX - 25, lineY - 15);

        batch.draw(keySpace, spaceX, lineY, 64, 64);
        font.draw(batch, "Attack", spaceX - 35, lineY - 15);

        float rightTxt = rightX + 20;

        fontTitle.draw(batch, "INVENTORY", rightTxt, invY + blockHInv - 25);
        font.draw(batch, "No weapon", rightTxt, invY + blockHInv - 80);

        fontTitle.draw(batch, "LEGEND", rightTxt, legY + blockHLeg - 25);

        float legY2 = legY + blockHLeg - 90;

        font.draw(batch, "Player:", rightTxt, legY2);
        batch.draw(playerTexture, rightTxt + 230, legY2 - 35, 60, 60);

        legY2 -= 100;

        font.draw(batch, "Objective:", rightTxt, legY2);
        if (chestIcon != null)
            batch.draw(chestIcon, rightTxt + 230, legY2 - 35, 60, 60);

        batch.end();
    }

    @Override public void resize(int w, int h) {
        camera.setToOrtho(false, w, h);
        uiCamera.setToOrtho(false, w, h);
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
