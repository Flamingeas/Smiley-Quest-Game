package settings.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.ScreenUtils;
import settings.GameState;
import system.AudioManager;

public class MenuScreen implements Screen {

    private final ScreenManager screenManager;
    private SpriteBatch batch;

    private BitmapFont fontTitle;
    private BitmapFont fontText;
    private Texture pointer;
    private Animation<TextureRegion> backgroundAnimation;

    private float stateTime = 0f;
    private int selectedIndex = 0;
    private final String[] menuItems = {"START", "SETTINGS", "EXIT"};
    private final String title = "Smiley Quest";

    private float spacing = 70f;
    private float posY = 150f;
    private GlyphLayout[] layouts;


    public MenuScreen(ScreenManager screenManager) {
        this.screenManager = screenManager;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        AudioManager.getInstance().setSoundEnabled(true);

        TextureRegion[] frames = new TextureRegion[18];
        for (int i = 0; i < 18; i++) {
            String filename = String.format("images/menuBackground-split/frame_%02d.png", i);
            frames[i] = new TextureRegion(new Texture(Gdx.files.internal(filename)));
        }
        backgroundAnimation = new Animation<>(0.07f, frames);

        pointer = new Texture(Gdx.files.internal("images/hand_cursor0000.png"));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/PressStart2P-Regular.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter titleParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        titleParam.size = 70;
        titleParam.color = Color.BLACK;
        fontTitle = generator.generateFont(titleParam);

        FreeTypeFontGenerator.FreeTypeFontParameter textParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        textParam.size = 40;
        textParam.color = Color.BLACK;
        fontText = generator.generateFont(textParam);

        generator.dispose();
        layouts = new GlyphLayout[menuItems.length];
        for (int i = 0; i < menuItems.length; i++) {
            layouts[i] = new GlyphLayout(fontText, menuItems[i]);
        }
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))
            selectedIndex = Math.min(selectedIndex + 1, menuItems.length - 1);

        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT))
            selectedIndex = Math.max(selectedIndex - 1, 0);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            activateMenuItem(selectedIndex);
        }

        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        float totalWidth = 0f;
        for (GlyphLayout layout : layouts) totalWidth += layout.width;
        totalWidth += spacing * (menuItems.length - 1);

        float x = (Gdx.graphics.getWidth() - totalWidth) / 2;
        for (int i = 0; i < menuItems.length; i++) {
            GlyphLayout layout = layouts[i];
            float itemX = x;
            float itemY = posY;
            if (mouseX >= itemX && mouseX <= itemX + layout.width &&
                    mouseY >= itemY - layout.height && mouseY <= itemY) {
                selectedIndex = i;
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    activateMenuItem(i);
                }
            }
            x += layout.width + spacing;
        }
    }

    private void activateMenuItem(int index) {
        switch (index) {
            case 0 -> screenManager.setState(GameState.PLAYING);
            case 1 -> screenManager.setState(GameState.SETTINGS);
            case 2 -> Gdx.app.exit();
        }
    }

    @Override
    public void render(float delta) {
        stateTime += delta;
        handleInput();

        ScreenUtils.clear(0, 0, 0.2f, 1);

        batch.begin();

        TextureRegion currentFrame = backgroundAnimation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        GlyphLayout layoutTitle = new GlyphLayout(fontTitle, title);
        float titleX = (Gdx.graphics.getWidth() - layoutTitle.width) / 2;
        fontTitle.draw(batch, title, titleX, Gdx.graphics.getHeight() - 100);

        float totalWidth = 0f;
        for (GlyphLayout layout : layouts) totalWidth += layout.width;
        totalWidth += spacing * (menuItems.length - 1);
        float startX = (Gdx.graphics.getWidth() - totalWidth) / 2;

        float x = startX;
        for (int i = 0; i < menuItems.length; i++) {
            fontText.draw(batch, menuItems[i], x, posY);
            x += layouts[i].width + spacing;
        }

        float pointerXPos = startX;
        for (int i = 0; i < selectedIndex; i++) pointerXPos += layouts[i].width + spacing;
        float pointerY = posY - pointer.getHeight() / 2;
        batch.draw(pointer, pointerXPos - pointer.getWidth() - 10, pointerY);

        batch.end();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override
    public void dispose() {
        batch.dispose();
        fontTitle.dispose();
        fontText.dispose();
        pointer.dispose();
        for (TextureRegion region : backgroundAnimation.getKeyFrames()) {
            region.getTexture().dispose();
        }
    }
}
