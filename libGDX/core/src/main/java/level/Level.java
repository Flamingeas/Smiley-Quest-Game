package level;

import utils.Coord;

public class Level {
    private int number;
    private String mapPath;
    private Coord playerSpawn;

    public Level(int number, String mapPath, Coord playerSpawn) {
        this.number = number;
        this.mapPath = mapPath;
        this.playerSpawn = playerSpawn;
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
}
