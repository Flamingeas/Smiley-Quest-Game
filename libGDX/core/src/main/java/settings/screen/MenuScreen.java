package settings.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;

public class MenuScreen implements com.badlogic.gdx.Screen {

    final private String title = "Smiley Quest";
    private SpriteBatch batch;
    private BitmapFont fontTitle;
    private BitmapFont fontText;

    public MenuScreen() {}

    public void handleInput() {
        if (Gdx.input.isTouched()) {
            System.out.println("Clic détecté : transition vers l’écran de jeu.");
        }
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        FreeTypeFontGenerator generator =
                new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter paramTitle = new FreeTypeFontGenerator.FreeTypeFontParameter();
        paramTitle.size = 32;
        paramTitle.color = Color.YELLOW;
        fontTitle = generator.generateFont(paramTitle);

        FreeTypeFontGenerator.FreeTypeFontParameter paramText = new FreeTypeFontGenerator.FreeTypeFontParameter();
        paramText.size = 16;
        paramText.color = Color.WHITE;
        fontText = generator.generateFont(paramText);

        generator.dispose();
        System.out.println("Menu principal affiché !");
    }

    public void update(float delta) {
        handleInput();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        handleInput();

        batch.begin();
        fontTitle.draw(batch, "Smiley Quest", 100, 360);
        fontText.draw(batch, "START", 100, 300);
        batch.end();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void hide() {
        System.out.println("Close the MenuScreen.");
    }

    @Override
    public void dispose() {
        batch.dispose();
        fontTitle.dispose();
        fontText.dispose();
        System.out.println("Cleansing of MenuScreen.");
    }
}
