package settings;

import entity.Player;
import level.Level;
import system.GameTimer;
import system.ScoreSystem;
import system.GameEvent;

public class GameWorld {
    private final Level currentLevel;
    private final Player player;
    private GameState state;
    private final ScoreSystem scoreSystem;
    private final GameTimer timer;
    private int currentLevelNumber = 1;

    public GameWorld(Level level) {
        this.currentLevel = level;
        this.player = new Player(level.getPlayerSpawn().getX(), level.getPlayerSpawn().getY(), 3, 2.0f);
        this.state = GameState.PLAYING;
        this.scoreSystem = new ScoreSystem();
        this.timer = new GameTimer();
    }
    public void update(float delta) {
        if (state == GameState.PLAYING) {
            timer.tick(delta);
        }
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
        return currentLevel;
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
        return currentLevelNumber;
    }
    public void nextLevel() {
        currentLevelNumber++;
        System.out.println("Passage au niveau " + currentLevelNumber);
    }
}
