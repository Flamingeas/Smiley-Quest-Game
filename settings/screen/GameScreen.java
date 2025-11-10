public class GameScreen extends Screen {
    private GameWorld world;

    public GameScreen() {
        this.world = new GameWorld;
    }

    @Override
    public void update(float delta) {
        world.update(delta);
    }

    @Override
    public void render(float delta) {
        if(currenScreen != null) {
            currenScreen.render(delta);
        }
    }
}