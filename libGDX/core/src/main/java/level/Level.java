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

    private String mapFile;
    private Coord endPoint;

    public Level(List<Enemy> enemies, List<Item> items, Coord playerSpawn, Map map) {
        this.enemies = enemies;
        this.items = items;
        this.playerSpawn = playerSpawn;
        this.map = map;
    }

    public Level(String mapFile, Coord playerSpawn, Coord endPoint) {
        this.mapFile = mapFile;
        this.playerSpawn = playerSpawn;
        this.endPoint = endPoint;
    }

    public String getMapFile() { return mapFile; }
    public Coord getEndPoint() { return endPoint; }

    public void load() {
    }

    public boolean isCleared(float playerX, float playerY, float tileSize) {
        float px = playerX / tileSize;
        float py = playerY / tileSize;
        return ((int)px == endPoint.getX() && (int)py == endPoint.getY());
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
