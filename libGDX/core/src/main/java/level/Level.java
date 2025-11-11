package level;

import entity.Enemy;
import entity.Item;
import utils.Coord;

import java.util.List;

public class Level {
    private List<Enemy> enemies;
    private List<Item> items;
    private Coord playerSpawn;
    private Map map;

    public Level(List<Enemy> enemies, List<Item> items, Coord playerSpawn, Map map) {
        this.enemies = enemies;
        this.items = items;
        this.playerSpawn = playerSpawn;
        this.map = map;
    }
    public void load() {
    }
    public boolean isCleared() {
        return false;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }
    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }
    public List<Item> getItems() {
        return items;
    }
    public void setItems(List<Item> items) {
        this.items = items;
    }
    public Coord getPlayerSpawn() {
        return playerSpawn;
    }
    public void setPlayerSpawn(Coord playerSpawn) {
        this.playerSpawn = playerSpawn;
    }
    public Map getMap() {
        return map;
    }
    public void setMap(Map map) {
        this.map = map;
    }
}
