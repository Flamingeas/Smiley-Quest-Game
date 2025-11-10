package settings;

import entity.Player;
import level.Level;
import system.GameTimer;
import system.ScoreSystem;
import events.GameEvent;

public class GameWorld {
    private Level currentLevel;
    private Player player;
    private GameState state;
    private ScoreSystem scoreSystem;
    private GameTimer timer;

    public GameWorld(Level level) {
        this.currentLevel = level;
        this.player = new Player();
        this.state = GameState.PLAYING;
        this.scoreSystem = new ScoreSystem();
        this.timer = new GameTimer();
    }

    public void update() {
        if (state == GameState.PLAYING) {
            timer.update();
            currentLevel.load();
        }
    }
    }

    public void nextLevel() {
        System.out.println("Chargement du niveau suivant...");
        this.currentLevel = new Level(); // temporaire
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
}
