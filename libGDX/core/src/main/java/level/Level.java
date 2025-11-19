package level;

import entity.Enemy;
import entity.Item;
import utils.Coord;

import java.util.List;

public class Level {
    private List<Enemy> enemies;
    private List<Item> items;
    private int number;
    private String mapPath;
    private Coord playerSpawn;
    private Map map;
    private String mapFile;
    private Coord endPoint;

    public Level(int number, List<Enemy> enemies, List<Item> items, Coord playerSpawn, Map map, String mapPath) {
        this.enemies = enemies;
        this.items = items;
        this.number = number;
        this.mapPath = mapPath;
        this.playerSpawn = playerSpawn;
        this.map = map;
    }
    public Level(String mapFile, Coord playerSpawn, Coord endPoint) {
        this.mapFile = mapFile;
        this.playerSpawn = playerSpawn;
        this.endPoint = endPoint;
    }
    public void load() {
    }
    public String getMapFile() { return mapFile; }
    public Coord getEndPoint() { return endPoint; }
    public List<Enemy> getEnemies() {
        return enemies;
    }
    public boolean isCleared(float playerX, float playerY, float tileSize) {
        float px = playerX / tileSize;
        float py = playerY / tileSize;
        return ((int)px == endPoint.getX() && (int)py == endPoint.getY());
    }
    public List<Item> getItems() {
        return items;
    }
    public void setItems(List<Item> items) {
        this.items = items;
    }
    public int getNumber() {
        return number;
    }
    public String getMapPath() {
        return mapPath;
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