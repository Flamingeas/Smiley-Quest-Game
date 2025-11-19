package settings.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.OrthographicCamera;
import settings.GameWorld;
import entity.Player;
import settings.GameState;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GameScreen implements Screen {

    private final ScreenManager screenManager;
    private final GameWorld gameWorld;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private OrthographicCamera uiCamera;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMapTileLayer dungeonLayer;

    private float mapOffsetX;
    private float mapOffsetY;
    private final float tileSize = 32f;

    private Texture playerTexture;
    private Player player;
    private float playerX, playerY;
    private float speed = 200f;

    private Texture arrowUp;
    private Texture arrowDown;
    private Texture arrowLeft;
    private Texture arrowRight;
    private Texture keyEsc;
    private Texture keySpace;

    private Set<Integer> walkableTiles = new HashSet<>();

    private BitmapFont font;
    private BitmapFont fontTitle;
    private ShapeRenderer shapeRenderer;

    private float leftPanelWidth;
    private float rightPanelWidth;

    private boolean pendingPause = false;
    private boolean initialized = false;

    private TextureRegion chestIcon;

    public GameScreen(ScreenManager screenManager, GameWorld gameWorld) {
        this.screenManager = screenManager;
        this.gameWorld = gameWorld;

        Collections.addAll(walkableTiles,
                31, 37, 38, 39, 49, 50, 51, 52, 53, 54
        );

        this.player = new Player(0, 0, 3, speed);
    }

    @Override
    public void show() {
        if (!initialized) {

            batch = new SpriteBatch();
            shapeRenderer = new ShapeRenderer();

            tiledMap = new TmxMapLoader().load("maps/sampleMap.tmx");
            mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 2f);
            dungeonLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Dungeon");

            camera = new OrthographicCamera();
            camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            uiCamera = new OrthographicCamera();
            uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            playerTexture = new Texture("images/smiley/playerboss.png");

            arrowUp = new Texture("images/ui/arrow_up.png");
            arrowDown = new Texture("images/ui/arrow_down.png");
            arrowLeft = new Texture("images/ui/arrow_left.png");
            arrowRight = new Texture("images/ui/arrow_right.png");
            keyEsc = new Texture("images/ui/echap.png");
            keySpace = new Texture("images/ui/attack.png");

            TiledMapTile tile = tiledMap.getTileSets().getTile(90);
            chestIcon = tile.getTextureRegion();

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
            float screenH = Gdx.graphics.getHeight();

            leftPanelWidth = screenW * 0.20f;
            rightPanelWidth = screenW * 0.25f;

            float mapWidth = 1024f;
            float mapHeight = 1024f;

            float mapZoneW = screenW - leftPanelWidth - rightPanelWidth;

            mapOffsetX = leftPanelWidth + (mapZoneW - mapWidth) / 2f;
            mapOffsetY = (screenH - mapHeight) / 2f;

            int spawnTileX = 0;
            int spawnTileY = 22;

            playerX = mapOffsetX + spawnTileX * tileSize;
            playerY = mapOffsetY + spawnTileY * tileSize;

            initialized = true;
        }
    }

    private void handleInput(float delta) {
        float move = speed * delta;
        float newX = playerX;
        float newY = playerY;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) newX -= move;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) newX += move;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) newY += move;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) newY -= move;

        if (canMoveTo(newX, playerY)) playerX = newX;
        if (canMoveTo(playerX, newY)) playerY = newY;

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) pendingPause = true;

        player.setX(playerX);
        player.setY(playerY);
    }

    private boolean canMoveTo(float newX, float newY) {

        if (newX < mapOffsetX) return false;
        if (newY < mapOffsetY) return false;
        if (newX + 32 > mapOffsetX + 1024) return false;
        if (newY + 32 > mapOffsetY + 1024) return false;

        float relX = newX - mapOffsetX;
        float relY = newY - mapOffsetY;

        int[][] corners = {
                {4, 4},
                {28, 4},
                {4, 28},
                {28, 28}
        };

        for (int[] c : corners) {
            int tx = (int) ((relX + c[0]) / tileSize);
            int ty = (int) ((relY + c[1]) / tileSize);

            if (!walkableTiles.contains(getTileId(tx, ty))) return false;
        }
        return true;
    }

    private int getTileId(int tx, int ty) {
        if (tx < 0 || ty < 0 ||
                tx >= dungeonLayer.getWidth() || ty >= dungeonLayer.getHeight())
            return -1;

        TiledMapTileLayer.Cell cell = dungeonLayer.getCell(tx, ty);
        if (cell == null || cell.getTile() == null) return -1;

        int id = cell.getTile().getId();
        if (id > 2000) id &= 0x1FFFFFFF;

        return id;
    }

    @Override
    public void render(float delta) {

        handleInput(delta);
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

        if (pendingPause) {
            pendingPause = false;
            screenManager.setState(GameState.PAUSED);
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
        font.draw(batch, "Niveau : " + gameWorld.getCurrentLevelNumber(), leftX + 25, statutY + blockHeightStatut - 80);
        font.draw(batch, "Score  : 15", leftX + 25, statutY + blockHeightStatut - 130);

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
        batch.draw(chestIcon, rightXtxt + 230, legendContentY - 35, 60, 60);

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
        batch.dispose();
        shapeRenderer.dispose();
        tiledMap.dispose();
        mapRenderer.dispose();
        playerTexture.dispose();
        font.dispose();
        fontTitle.dispose();
        arrowUp.dispose();
        arrowDown.dispose();
        arrowLeft.dispose();
        arrowRight.dispose();
        keyEsc.dispose();
    }
}
