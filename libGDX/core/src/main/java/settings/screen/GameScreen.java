package settings.screen;

import java.util.ArrayList;
import java.util.List;

import entity.Item;
import entity.Enemy;
import settings.GameWorld;
import utils.Coord;
import level.Level;
import level.Map;
import com.badlogic.gdx.Screen;


public class GameScreen implements Screen {

    private final GameWorld world;

    public GameScreen() {
        List<Enemy> enemies = new ArrayList<>();
        List<Item> items = new ArrayList<>();
        Coord playerSpawn = new Coord(0, 0);
        Map map = new Map();

        Level level1 = new Level(enemies, items, playerSpawn, map);
        this.world = new GameWorld(level1);
    }

    @Override
    public void show() {
        System.out.println("Affichage de l’écran de jeu");
    }

    public void update(float delta) {
        world.update(delta);
    }

    @Override
    public void render(float delta) {
        world.update(delta);
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        System.out.println("Nettoyage du GameScreen");
    }
}
