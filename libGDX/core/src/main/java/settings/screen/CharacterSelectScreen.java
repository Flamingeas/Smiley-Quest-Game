package settings.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;

import settings.GameState;

public class CharacterSelectScreen implements Screen {

    private final ScreenManager screenManager;

    private SpriteBatch batch;
    private BitmapFont fontTitle;
    private BitmapFont fontText;

    private Texture arrowLeft;
    private Texture arrowRight;
    private Texture[] smileys;

    private int selectedIndex = 0;

    private float screenW;
    private float screenH;
    private float centerX;
    private float centerY;

    public CharacterSelectScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();

        screenW = Gdx.graphics.getWidth();
        screenH = Gdx.graphics.getHeight();
        centerX = screenW / 2f;
        centerY = screenH / 2f;

        arrowLeft = new Texture("images/ui/arrow_left.png");
        arrowRight = new Texture("images/ui/arrow_right.png");

        smileys = new Texture[]{
                new Texture("images/smiley/smiley_0.png"),
                new Texture("images/smiley/smiley_1.png"),
                new Texture("images/smiley/smiley_2.png"),
                new Texture("images/smiley/smiley_3.png"),
                new Texture("images/smiley/smiley_4.png"),
                new Texture("images/smiley/smiley_5.png"),
                new Texture("images/smiley/smiley_6.png")
        };

        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P-Regular.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();

        p.size = 40;
        p.color = Color.WHITE;
        fontTitle = gen.generateFont(p);

        p.size = 20;
        fontText = gen.generateFont(p);

        gen.dispose();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        handleInput();
        batch.begin();

        float titleY = centerY + 260;
        float smileyY = centerY;
        float arrowsY = smileyY - 10;
        float info1Y = smileyY - 180;
        float info2Y = info1Y - 60;
        float info3Y = info2Y - 60;

        fontTitle.draw(batch, "SELECT YOUR SMILEY", centerX - 330, titleY);

        batch.draw(arrowLeft, centerX - 260, arrowsY - 48, 96, 96);
        batch.draw(arrowRight, centerX + 160, arrowsY - 48, 96, 96);

        Texture current = smileys[selectedIndex];
        batch.draw(current, centerX - 64, smileyY - 64, 128, 128);

        fontText.draw(batch, "USE <- AND -> TO CHOOSE", centerX - 220, info1Y);
        fontText.draw(batch, "PRESS ENTER TO CONFIRM", centerX - 220, info2Y);
        fontText.draw(batch, "ESC TO CANCEL", centerX - 180, info3Y);

        batch.end();
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            selectedIndex = (selectedIndex + 1) % smileys.length;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            selectedIndex = (selectedIndex - 1 + smileys.length) % smileys.length;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            screenManager.setSelectedSmiley(selectedIndex);
            screenManager.setState(GameState.PLAYING);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            screenManager.setState(GameState.MENU);
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        arrowLeft.dispose();
        arrowRight.dispose();
        for (Texture t : smileys) t.dispose();
        fontTitle.dispose();
        fontText.dispose();
    }
}
