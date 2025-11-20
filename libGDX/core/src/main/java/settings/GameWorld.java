package settings;

import entity.Player;

import entity.enemies.FinalBossEnemy;
import entity.enemies.SkeletonWeakEnemy;
import entity.enemies.SkeletonStrongEnemy;
import entity.enemies.GhostEnemy;
import entity.enemies.LarvaEnemy;
import entity.enemies.DragonEnemy;

import level.Level;

import system.GameTimer;
import system.ScoreSystem;
import system.GameEvent;

import utils.Coord;

import entity.enemies.Enemy;
import settings.screen.GameScreen;

import java.util.ArrayList;
import java.util.List;

public class GameWorld {

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

    public void setGameScreen(GameScreen gs) {
        this.gameScreen = gs;
    }

    public GameWorld(int smileyIndex) {

        this.selectedSmileyIndex = smileyIndex;

        this.levels = new Level[] {
                new Level("maps/sampleMap.tmx",    new Coord(0, 22), new Coord(27, 4)),
                new Level("maps/laby.tmx",         new Coord(0, 31), new Coord(28, 0)),
                new Level("maps/mapdebutant.tmx",  new Coord(1, 28), new Coord(28, 1)),
                new Level("maps/map2.tmx",         new Coord(6, 20), new Coord(15, 15))
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
                2.0f,
                this.selectedSmileyIndex
        );

        this.enemies = new ArrayList<>();
    }

    private Coord randomWalkableTile() {
        Level lvl = getCurrentLevel();

        if (gameScreen == null) {
            System.out.println("⚠ GameScreen non lié à GameWorld !");
            return new Coord(1, 1);
        }

        int tries = 0;

        while (true) {
            tries++;

            int tx = (int) (Math.random() * lvl.getMapWidth());
            int ty = (int) (Math.random() * lvl.getMapHeight());

            if (gameScreen.isWalkableTileRaw(tx, ty)) {
                System.out.println("SPAWN TILE OK : " + tx + "," + ty);
                return new Coord(tx, ty);
            }

            if (tries % 2000 == 0)
                System.out.println("toujours rien trouvé après " + tries + " essais…");
        }
    }

    private void spawnEnemiesForLevel(int level) {

        enemies = new ArrayList<>();

        int enemyCount = 3 + (level * 2);
        float genSpeed = 0.8f + (level * 0.2f);
        int genHealth = 30 + (level * 10);
        int genDamage = 10 + (level * 5);

        for (int i = 0; i < enemyCount; i++) {

            Coord c1 = randomWalkableTile();
            Enemy g = new GhostEnemy(c1.getX(), c1.getY(), genHealth, genSpeed, genDamage, player);

            g.setGameScreen(gameScreen);
            g.setPixelPosition(
                    gameScreen.getMapOffsetX() + c1.getX() * gameScreen.getTileSize(),
                    gameScreen.getMapOffsetY() + c1.getY() * gameScreen.getTileSize()
            );
            enemies.add(g);

            Coord c2 = randomWalkableTile();
            Enemy l = new LarvaEnemy(c2.getX(), c2.getY(), genHealth, genSpeed, genDamage, player);

            l.setGameScreen(gameScreen);
            l.setPixelPosition(
                    gameScreen.getMapOffsetX() + c2.getX() * gameScreen.getTileSize(),
                    gameScreen.getMapOffsetY() + c2.getY() * gameScreen.getTileSize()
            );
            enemies.add(l);
        }

        for (int i = 0; i < enemyCount; i++) {
            Coord c = randomWalkableTile();
            Enemy e = new SkeletonWeakEnemy(c.getX(), c.getY(), genHealth, genSpeed, genDamage, player);

            e.setGameScreen(gameScreen);
            e.setPixelPosition(
                    gameScreen.getMapOffsetX() + c.getX() * gameScreen.getTileSize(),
                    gameScreen.getMapOffsetY() + c.getY() * gameScreen.getTileSize()
            );
            enemies.add(e);
        }

        if (level >= 2) {
            for (int i = 0; i < enemyCount / 2; i++) {
                Coord c1 = randomWalkableTile();
                Enemy s = new SkeletonStrongEnemy(
                        c1.getX(), c1.getY(),
                        genHealth + 20, genSpeed + 0.2f, genDamage + 5, player
                );

                s.setGameScreen(gameScreen);
                s.setPixelPosition(
                        gameScreen.getMapOffsetX() + c1.getX() * gameScreen.getTileSize(),
                        gameScreen.getMapOffsetY() + c1.getY() * gameScreen.getTileSize()
                );
                enemies.add(s);
                Coord c2 = randomWalkableTile();
                Enemy d = new DragonEnemy(
                        c2.getX(), c2.getY(),
                        genHealth + 40, genSpeed + 0.3f, genDamage + 10, player
                );
                d.setGameScreen(gameScreen);
                d.setPixelPosition(
                        gameScreen.getMapOffsetX() + c2.getX() * gameScreen.getTileSize(),
                        gameScreen.getMapOffsetY() + c2.getY() * gameScreen.getTileSize()
                );
                enemies.add(d);
            }
        }
        if (level == 4) {
            Coord c = randomWalkableTile();
            Enemy boss = new FinalBossEnemy(
                    c.getX(), c.getY(),
                    300,
                    1.0f,
                    30,
                    player
            );

            boss.setGameScreen(gameScreen);
            boss.setPixelPosition(
                    gameScreen.getMapOffsetX() + c.getX() * gameScreen.getTileSize(),
                    gameScreen.getMapOffsetY() + c.getY() * gameScreen.getTileSize()
            );
            enemies.add(boss);
        }
    }

    public void update(float delta) {
        if (state == GameState.PLAYING) {
            timer.tick(delta);
            getCurrentLevel().load();
            if (!enemiesSpawned) {
                spawnEnemiesForLevel(currentLevelIndex);
                enemiesSpawned = true;
            }
        }
        player.update();
        for (Enemy enemy : enemies) {
            enemy.update();
            enemy.interact(player);
        }
    }
    public void fullReset() {
        currentLevelIndex = 0;
        enemies.clear();
        enemiesSpawned = false;

        Level lvl = levels[0];
        Coord spawn = lvl.getPlayerSpawn();
        player.setX(spawn.getX() * 32f);
        player.setY(spawn.getY() * 32f);

        player.reset();
        timer.reset();
        scoreSystem.reset();

        state = GameState.PLAYING;
    }

    public boolean nextLevel() {

        if (currentLevelIndex < levels.length - 1) {

            currentLevelIndex++;
            enemiesSpawned = false;
            enemies.clear();

            System.out.println("➡ Passage au niveau suivant…");

            Level lvl = levels[currentLevelIndex];

            player.setX(lvl.getPlayerSpawn().getX() * 32f);
            player.setY(lvl.getPlayerSpawn().getY() * 32f);

            return true;
        }

        System.out.println("Il n’y a plus de niveaux");
        return false;
    }

    public void restart() {
        System.out.println("Redémarrage du niveau…");
        this.player.reset();
        this.timer.reset();
        this.scoreSystem.reset();
        this.state = GameState.PLAYING;
    }

    public void publish(GameEvent e) {
        System.out.println("Événement : " + e.getType());
    }

    public void setState(GameState state) { this.state = state; }
    public GameState getState() { return this.state; }

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
}
