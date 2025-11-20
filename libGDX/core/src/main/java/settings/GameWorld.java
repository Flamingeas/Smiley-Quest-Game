package settings;

import entity.Player;
import level.Level;
import system.GameTimer;
import system.ScoreSystem;
import system.GameEvent;
import utils.Coord;
import entity.enemies.Enemy;
import java.util.ArrayList;
import java.util.List;

public class GameWorld {

    private Level[] levels;
    private int currentLevelIndex = 0;

    private Player player;
    private GameState state;
    private final ScoreSystem scoreSystem;
    private final GameTimer timer;
    private int selectedSmileyIndex;
    private List<Enemy> enemies;


    public GameWorld(Level firstLevel,int selectedSmileyIndex) {

        this.selectedSmileyIndex = selectedSmileyIndex;
        this.levels = new Level[] {
                new Level("maps/sampleMap.tmx", new Coord(0, 22), new Coord(27, 4)),
                new Level("maps/laby.tmx", new Coord(1, 29), new Coord(4, 12)),
                new Level("maps/mapdebutant.tmx", new Coord(1, 28), new Coord(28, 1)),
                new Level("maps/map4.tmx", new Coord(1, 30), new Coord(28, 1)),
                new Level("maps/final-map.tmx", new Coord(1, 30), new Coord(30, 1))
        };

        this.state = GameState.PLAYING;
        this.scoreSystem = new ScoreSystem();
        this.timer = new GameTimer();

        Level lvl = levels[currentLevelIndex];
        this.player = new Player(lvl.getPlayerSpawn().getX(), lvl.getPlayerSpawn().getY(), 3, 2.0f, selectedSmileyIndex);
        this.enemies = new ArrayList<>();
        spawnEnemiesForLevel(currentLevelIndex);
    }

    private void spawnEnemiesForLevel(int level) {
        enemies = new ArrayList<>();


        int enemyCount = 3 + (level * 2);
        float genSpeed = 0.8f + (level * 0.2f);
        int genHealth = 30 + (level * 10);
        int genDamage = 10 + (level * 5);

//        for (int i = 0; i < enemyCount; i++) {
//            enemies.add(new AggressiveEnemy(
//                    randomX(),
//                    randomY(),
//                    genHealth,
//                    genSpeed,
//                    genDamage,
//                    player
//            ));
//        }
    }

    public void update(float delta) {
        if (state == GameState.PLAYING) {
            timer.tick(delta);
            getCurrentLevel().load();
        }
        player.update();

        for (Enemy enemy : enemies) {
            enemy.update();
            enemy.interact(player);
        }
    }

    public boolean nextLevel() {
        if (currentLevelIndex < levels.length - 1) {
            currentLevelIndex++;
            System.out.println("Chargement du niveau suivant...");

            Level lvl = levels[currentLevelIndex];
            player.setX(lvl.getPlayerSpawn().getX()*32f);
            player.setY(lvl.getPlayerSpawn().getY()*32f);

            return true;
        }

        System.out.println("Plus de niveaux !");
        return false;
    }

    public void restart() {
        System.out.println("Redémarrage du niveau...");
        this.player.reset();
        this.timer.reset();
        this.scoreSystem.reset();
        this.state = GameState.PLAYING;
    }

    public void publish(GameEvent e) {
        System.out.println("Événement reçu : " + e.getType());
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public GameState getState() {
        return this.state;
    }

    public void endGame() {
        System.out.println("Fin du jeu !");
        this.state = GameState.GAME_OVER;
    }

    public Level getCurrentLevel() {
        return levels[currentLevelIndex];
    }

    public Player getPlayer() {
        return player;
    }

    public ScoreSystem getScoreSystem() {
        return scoreSystem;
    }

    public GameTimer getTimer() {
        return timer;
    }

    public int getCurrentLevelNumber() {
        return currentLevelIndex + 1;
    }

    public int getTotalLevels() {
        return levels.length;
    }

}
