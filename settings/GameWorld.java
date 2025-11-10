public class GameWorld {
    private Level currentLevel;
    private Player player;
    private GameState state;
    private ScoreSystem scoreSystem;
    private GameTimer timer;

    public GameWorld(Level level) {
        currentLevel = level;
    }
}