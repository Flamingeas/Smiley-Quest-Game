package settings;

import entity.Player;
import entity.enemies.*;
import level.Level;
import settings.screen.ScreenManager;
import system.GameTimer;
import system.Observer;
import system.ScoreSystem;
import system.GameEvent;
import utils.Coord;
import settings.screen.GameScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the core game world, managing levels, player state, enemies, and game logic.
 * Handles enemy spawning, level progression, and observes game events.
 */
public class GameWorld implements Observer {

    private Level[] levels;
    private int currentLevelIndex = 0;

    private Player player;
    private GameState state;
    private final ScoreSystem scoreSystem;
    private final GameTimer timer;

    private List<Enemy> enemies;
    private boolean enemiesSpawned = false;

    private int selectedSmileyIndex;
    private GameScreen gameScreen;
    private ScreenManager screenManager;

    /**
     * Sets the current game screen.
     * @param gs the GameScreen instance
     */
    public void setGameScreen(GameScreen gs) {
        this.gameScreen = gs;
    }

    /**
     * Initializes the game world with a selected smiley index for the player.
     * Loads the first level and sets up player and score systems.
     * @param smileyIndex index for the player's smiley texture
     */
    public GameWorld(int smileyIndex) {
        this.selectedSmileyIndex = smileyIndex;

        this.levels = new Level[] {
                new Level("maps/sampleMap.tmx", new Coord(0, 22), new Coord(27, 4)),
                new Level("maps/laby.tmx", new Coord(0, 31), new Coord(28, 0)),
                new Level("maps/mapdebutant.tmx", new Coord(1, 28), new Coord(28, 1)),
                new Level("maps/final-map.tmx", new Coord(1, 30), new Coord(30, 1))
        };

        this.state = GameState.PLAYING;
        this.scoreSystem = new ScoreSystem();
        this.timer = new GameTimer();

        Level lvl = levels[currentLevelIndex];
        lvl.load();

        this.player = new Player(
                lvl.getPlayerSpawn().getX(),
                lvl.getPlayerSpawn().getY(),
                3,
                0.7f,
                this.selectedSmileyIndex
        );
        player.addObserver(this);

        this.enemies = new ArrayList<>();
    }

    /**
     * Checks if a tile is walkable in the current game screen.
     * @param x tile x-coordinate
     * @param y tile y-coordinate
     * @return true if walkable, false otherwise
     */
    public boolean isWalkableTile(int x, int y) {
        if (gameScreen == null) return false;
        return gameScreen.isWalkableTileRaw(x, y);
    }

    /**
     * Gets the current game screen.
     * @return the GameScreen instance
     */
    public GameScreen getGameScreen() {
        return gameScreen;
    }

    /**
     * Chooses a random walkable tile avoiding the player, chest, and existing enemies.
     * @return a Coord object representing the chosen tile
     */
    private Coord randomWalkableTile() {
        Level lvl = getCurrentLevel();
        int maxTry = 500;

        for (int i = 0; i < maxTry; i++) {
            int tx = (int)(Math.random() * lvl.getMapWidth());
            int ty = (int)(Math.random() * lvl.getMapHeight());

            if (!gameScreen.isWalkableTileRaw(tx, ty)) continue;

            int px = (int)((player.getX() - gameScreen.getMapOffsetX()) / gameScreen.getTileSize());
            int py = (int)((player.getY() - gameScreen.getMapOffsetY()) / gameScreen.getTileSize());

            if (Math.abs(tx - px) + Math.abs(ty - py) < 5) continue;

            Coord chest = lvl.getEndPoint();
            if (chest != null && tx == chest.getX() && ty == chest.getY()) continue;

            boolean occupied = false;
            for (Enemy e : enemies) {
                if ((int)e.tileX == tx && (int)e.tileY == ty) {
                    occupied = true;
                    break;
                }
            }
            if (occupied) continue;

            return new Coord(tx, ty);
        }
        return new Coord(1, 1);
    }

    /**
     * Spawns enemies for the specified level index, adjusting stats and positioning.
     * @param level level index
     */
    private void spawnEnemiesForLevel(int level) {
        int hpCap =
                (level == 0) ? 40 :
                        (level == 1) ? 60 :
                                (level == 2) ? 70 :
                                        (level == 3) ? 80 : 80;
        enemies = new ArrayList<>();

        if (level == 3) {
            System.out.println("Dernière map → nombre d'ennemis réduit");

            int enemyCount = 4;
            boolean spawnBoss = true;

            float speed = 1.0f;
            int health = 40;
            int damage = 12;

            for (int i = 0; i < enemyCount; i++) {
                Coord c = randomWalkableTile();

                SkeletonWeakEnemy e = new SkeletonWeakEnemy(
                        c.getX(), c.getY(),
                        health, speed, damage, player
                );
                e.health = Math.min(e.health, hpCap);
                e.texture = gameScreen.getEnemyRenderer().getTextureFor(e);
                e.setGameScreen(gameScreen);
                e.setPixelPosition(
                        gameScreen.getMapOffsetX() + c.getX() * gameScreen.getTileSize(),
                        gameScreen.getMapOffsetY() + c.getY() * gameScreen.getTileSize()
                );
                enemies.add(e);
            }

            if (spawnBoss) {
                Coord c = randomWalkableTile();

                Enemy boss = new FinalBossEnemy(
                        c.getX(), c.getY(),
                        300, 1.0f, 30, player
                );
                boss.health = Math.min(boss.health, hpCap);

                boss.texture = gameScreen.getEnemyRenderer().getTextureFor(boss);
                boss.setGameScreen(gameScreen);
                boss.setPixelPosition(
                        gameScreen.getMapOffsetX() + c.getX() * gameScreen.getTileSize(),
                        gameScreen.getMapOffsetY() + c.getY() * gameScreen.getTileSize()
                );
                enemies.add(boss);
            }

            return;
        }

        int enemyCount = 2 + level;
        int eliteCount = (level >= 2) ? (1 + level / 2) : 0;

        float genSpeed = 0.8f + (level * 0.2f);
        int genHealth = 30 + (level * 10);
        int genDamage = 10 + (level * 5);

        for (int i = 0; i < enemyCount; i++) {
            Coord c1 = randomWalkableTile();
            Enemy g = new GhostEnemy(c1.getX(), c1.getY(),
                    genHealth, genSpeed, genDamage, player);
            g.health = Math.min(g.health, hpCap);
            g.texture = gameScreen.getEnemyRenderer().getTextureFor(g);
            g.setGameScreen(gameScreen);
            g.setPixelPosition(gameScreen.getMapOffsetX() + c1.getX() * gameScreen.getTileSize(),
                    gameScreen.getMapOffsetY() + c1.getY() * gameScreen.getTileSize());
            enemies.add(g);

            Coord c2 = randomWalkableTile();
            Enemy l = new LarvaEnemy(c2.getX(), c2.getY(),
                    genHealth, genSpeed, genDamage, player);
            l.health = Math.min(l.health, hpCap);
            l.texture = gameScreen.getEnemyRenderer().getTextureFor(l);
            l.setGameScreen(gameScreen);
            l.setPixelPosition(gameScreen.getMapOffsetX() + c2.getX() * gameScreen.getTileSize(),
                    gameScreen.getMapOffsetY() + c2.getY() * gameScreen.getTileSize());
            enemies.add(l);
        }
        for (int i = 0; i < enemyCount; i++) {
            Coord c = randomWalkableTile();
            Enemy e = new SkeletonWeakEnemy(c.getX(), c.getY(), genHealth, genSpeed, genDamage, player);
            e.setGameScreen(gameScreen);
            e.setPixelPosition(gameScreen.getMapOffsetX() + c.getX() * gameScreen.getTileSize(),
                    gameScreen.getMapOffsetY() + c.getY() * gameScreen.getTileSize());
            enemies.add(e);
        }
        for (int i = 0; i < eliteCount; i++) {

            Coord c1 = randomWalkableTile();
            Enemy s = new SkeletonStrongEnemy(
                    c1.getX(), c1.getY(),
                    genHealth + 20, genSpeed + 0.2f, genDamage + 5, player
            );
            s.health = Math.min(s.health, hpCap);
            s.texture = gameScreen.getEnemyRenderer().getTextureFor(s);
            s.setGameScreen(gameScreen);
            s.setPixelPosition(gameScreen.getMapOffsetX() + c1.getX() * gameScreen.getTileSize(),
                    gameScreen.getMapOffsetY() + c1.getY() * gameScreen.getTileSize());
            enemies.add(s);

            Coord c2 = randomWalkableTile();
            Enemy d = new DragonEnemy(
                    c2.getX(), c2.getY(),
                    genHealth + 40, genSpeed + 0.3f, genDamage + 10, player
            );
            d.health = Math.min(d.health, hpCap);
            d.texture = gameScreen.getEnemyRenderer().getTextureFor(d);
            d.setGameScreen(gameScreen);
            d.setPixelPosition(gameScreen.getMapOffsetX() + c2.getX() * gameScreen.getTileSize(),
                    gameScreen.getMapOffsetY() + c2.getY() * gameScreen.getTileSize());
            enemies.add(d);
        }
    }
    /**
     * Updates the game world state, player, and enemies.
     * @param delta time elapsed since last update
     */
    public void update(float delta) {
        if (state == GameState.PLAYING) {
            timer.tick(delta);

            if (!enemiesSpawned) {
                spawnEnemiesForLevel(currentLevelIndex);
                enemiesSpawned = true;
            }
        }
        player.update();
        for (Enemy enemy : enemies) {
            enemy.update();
        }
    }
    /**
     * Advances to the next level if available.
     * @return true if next level exists, false otherwise
     */
    public boolean nextLevel() {
        if (currentLevelIndex < levels.length - 1) {
            currentLevelIndex++;
            enemiesSpawned = false;
            enemies.clear();

            Level lvl = levels[currentLevelIndex];
            player.setX(lvl.getPlayerSpawn().getX() * 32f);
            player.setY(lvl.getPlayerSpawn().getY() * 32f);

            return true;
        }
        return false;
    }
    /**
     * Publishes a game event for debugging or logging purposes.
     * @param e the GameEvent instance
     */
    public void publish(GameEvent e) {
        System.out.println("Événement : " + e.getType());
        scoreSystem.onEvent(e);
    }

    public void setState(GameState state) { this.state = state; }
    public GameState getState() { return this.state; }

    /**
     * Ends the game and sets the state to GAME_OVER.
     */
    public void endGame() {
        System.out.println("Fin du jeu !");
        this.state = GameState.GAME_OVER;
    }

    public Level getCurrentLevel() { return levels[currentLevelIndex]; }
    public Player getPlayer() { return player; }
    public ScoreSystem getScoreSystem() { return scoreSystem; }
    public GameTimer getTimer() { return timer; }
    public int getCurrentLevelNumber() { return currentLevelIndex + 1; }
    public int getTotalLevels() { return levels.length; }
    public List<Enemy> getEnemies() { return enemies; }
    public void setScreenManager(ScreenManager sm) {
        this.screenManager = sm;
    }
    /**
     * Handles notifications from observed game events and applies corresponding logic.
     * @param event the GameEvent instance
     */
    @Override
    public void onNotify(GameEvent event) {

        switch (event.getType()) {

            case "ITEM_COLLECTED" -> {
                int value = (int) event.getData();
                scoreSystem.addScore(value);
            }

            case "PLAYER_HIT" -> {
                scoreSystem.addScore(-30);
            }

            case "CHEST_FOUND" -> {
                scoreSystem.addScore(100);

                if (nextLevel()) {
                    scoreSystem.addScore(200);
                    enemiesSpawned = false;
                    gameScreen.loadLevel(getCurrentLevel());
                } else {
                    screenManager.setState(GameState.WINNER);
                }
            }
            case "PLAYER_DEAD" -> {
                screenManager.setState(GameState.GAME_OVER);
            }
        }
    }
}
