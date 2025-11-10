package settings.screen;

import java.util.ArrayList;
import java.util.List;

import level.Level;
import entity.Enemy;
import settings.GameWorld;
import utils.Coord;
import level.Map;
import level.Item;

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
    public void update(float delta) {
        world.update(delta);
    }

    @Override
    public void render(float delta) {
        world.render(delta);
    }
}
