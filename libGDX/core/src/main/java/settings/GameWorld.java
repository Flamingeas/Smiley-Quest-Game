package settings;

import entity.Player;
import level.Level;
import system.GameTimer;
import system.ScoreSystem;
import system.GameEvent;
import utils.Coord;

public class GameWorld {

    private Level[] levels;
    private int currentLevelIndex = 0;

    private Player player;
    private GameState state;
    private final ScoreSystem scoreSystem;
    private final GameTimer timer;

    public GameWorld(Level firstLevel) {

        this.levels = new Level[] {
                new Level("maps/sampleMap.tmx", new Coord(0, 22), new Coord(27, 4)),
                new Level("maps/mapdebutant.tmx", new Coord(1, 28), new Coord(28, 1)),
                new Level("maps/map2.tmx", new Coord(6, 16), new Coord(28, 1))
        };

        this.state = GameState.PLAYING;
        this.scoreSystem = new ScoreSystem();
        this.timer = new GameTimer();

        Level lvl = levels[currentLevelIndex];
        this.player = new Player(lvl.getPlayerSpawn().getX(), lvl.getPlayerSpawn().getY(), 3, 2.0f);
    }

    public void update(float delta) {
        if (state == GameState.PLAYING) {
            timer.tick(delta);
            getCurrentLevel().load();
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
}
