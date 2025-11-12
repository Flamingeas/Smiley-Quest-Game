package settings.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;


public class MenuScreen implements com.badlogic.gdx.Screen {

    final private String title = "Smiley Quest";
    private SpriteBatch batch;
    private BitmapFont fontTitle;
    private BitmapFont fontText;
    private Texture background;
    private GlyphLayout layoutTitle;
    private GlyphLayout layoutText;


    public MenuScreen() {}

    public void handleInput() {
        if (Gdx.input.isTouched()) {
            System.out.println("Clic détecté : transition vers l’écran de jeu.");
        }
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        background = new Texture(Gdx.files.internal("images/cool_smiley_giphy.png"));


        //Pac-Man Font
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter paramTitle = new FreeTypeFontGenerator.FreeTypeFontParameter();
        paramTitle.size = 70;
        paramTitle.color = Color.YELLOW;
        fontTitle = generator.generateFont(paramTitle);

        FreeTypeFontGenerator.FreeTypeFontParameter paramText = new FreeTypeFontGenerator.FreeTypeFontParameter();
        paramText.size = 40;
        paramText.color = Color.WHITE;
        fontText = generator.generateFont(paramText);

        generator.dispose();

        layoutTitle = new GlyphLayout(fontTitle, "Smiley Quest");
        layoutText = new GlyphLayout(fontText, "START");
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
        fontTitle.draw(batch, "Smiley Quest", 550, 800);
        fontText.draw(batch, "START", 500, 300);
        fontText.draw(batch, "SETTINGS", 800, 300);

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
        background.dispose();
        System.out.println("Cleansing of MenuScreen.");
    }
}
