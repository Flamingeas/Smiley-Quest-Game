package settings.screen;

/**
 * Represents a general screen in the game.
 * Implementing classes define the behavior for different game screens,
 * such as the main menu, gameplay, pause screen, or settings screen.
 */
public interface Screen {
    public void show();
    public void update(float delta);
    public  void render(float delta);
    public void resize(int width, int height);
    public void pause();
    public void resume();
    public void hide();
    public void dispose();
}