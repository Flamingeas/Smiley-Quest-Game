package level;

import entity.enemies.Enemy;
import entity.Item;
import utils.Coord;

import java.util.ArrayList;
import java.util.List;

public class Level {

    private List<Enemy> enemies;
    private List<Item> items;

    private Coord playerSpawn;
    private Coord endPoint;

    private String mapFile;

    private int mapWidthTiles;
    private int mapHeightTiles;

    public Level(String mapFile, Coord playerSpawn, Coord endPoint) {
        this.mapFile = mapFile;
        this.playerSpawn = playerSpawn;
        this.endPoint = endPoint;
        this.enemies = new ArrayList<>();
        this.items = new ArrayList<>();
    }

    public String getMapFile() { return mapFile; }
    public Coord getPlayerSpawn() { return playerSpawn; }
    public Coord getEndPoint() { return endPoint; }

    public boolean isCleared(float px, float py, float tileSize) {
        int tx = (int)(px / tileSize);
        int ty = (int)(py / tileSize);
        return tx == endPoint.getX() && ty == endPoint.getY();
    }

    public void setMapSize(int width, int height) {
        this.mapWidthTiles = width;
        this.mapHeightTiles = height;
    }

    public int getMapWidth() { return mapWidthTiles; }
    public int getMapHeight() { return mapHeightTiles; }

    public List<Enemy> getEnemies() { return enemies; }
    public void setEnemies(List<Enemy> enemies) { this.enemies = enemies; }

    public List<Item> getItems() { return items; }
    public void setItems(List<Item> items) { this.items = items; }

    public void load() {
    }
}
