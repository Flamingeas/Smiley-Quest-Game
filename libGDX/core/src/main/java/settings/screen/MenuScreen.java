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
import com.badlogic.gdx.Input;
import settings.GameState;

public class MenuScreen implements com.badlogic.gdx.Screen {

    final private String title = "Smiley Quest";
    private final ScreenManager screenManager;
    private SpriteBatch batch;
    private BitmapFont fontTitle;
    private BitmapFont fontText;
    private Texture background;
    private GlyphLayout layoutTitle;
    private GlyphLayout layoutText;
    private String[] menuItems = {"START", "SETTINGS", "EXIT"};
    private int selectedIndex = 0;
    private Texture pointer;
    private float pointerX = 550;
    private float pointerYStart = 300;
    private float pointerYOffset = 50;



    public MenuScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            selectedIndex = Math.min(selectedIndex + 1, menuItems.length - 1);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            selectedIndex = Math.max(selectedIndex - 1, 0);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            switch (selectedIndex) {
                case 0:
                    screenManager.setState(GameState.PLAYING);
                    break;
                case 1:
                    screenManager.setState(GameState.SETTINGS);
                    break;
                case 2:
                    Gdx.app.exit();
                    break;
            }
        }
    }


    @Override
    public void show() {
        batch = new SpriteBatch();

        background = new Texture(Gdx.files.internal("images/cool_smiley_giphy.png"));
        pointer = new Texture(Gdx.files.internal("images/hand_cursor0000.png"));

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
//        fontText.draw(batch, "START", 850, 400);
//        fontText.draw(batch, "SETTINGS", 800, 300);
//        fontText.draw(batch, "EXIT", 850, 300);


        int startY = 300;
        int spacing = 50;

        for (int i = 0; i < menuItems.length; i++) {
            fontText.draw(batch, menuItems[i], 650, startY - i * spacing);
        }

// Cursor
        batch.draw(pointer, 600, startY - selectedIndex * spacing - pointer.getHeight() / 2);


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
