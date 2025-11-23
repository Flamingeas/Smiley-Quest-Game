/**
 * Represents a tile-based map for a level.
 * <p>
 * Handles map dimensions, tile data, rendering, and walkability.
 * </p>
 */
package level;

public class Map {

    private int width;
    private int height;
    private int[][] tiles;

    /**
     * Constructs a default 10x10 map with border walls.
     */
    public Map() {
        this.width = 10;
        this.height = 10;
        this.tiles = new int[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (x == 0 || y == 0 || x == width - 1 || y == height - 1) {
                    tiles[x][y] = 1;
                } else {
                    tiles[x][y] = 0;
                }
            }
        }
    }

    /**
     * Constructs a map with specified width and height.
     * @param width map width in tiles
     * @param height map height in tiles
     */
    public Map(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new int[width][height];
    }

    /** Loads the map resources. */
    public void load() {
        System.out.println("Chargement de la carte (" + width + "x" + height + ")");
    }

    /**
     * Renders the map to console, using '#' for walls and '.' for empty tiles.
     */
    public void render() {
        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                System.out.print(tiles[x][y] == 1 ? "#" : ".");
            }
            System.out.println();
        }
    }

    /**
     * Checks if a tile at (x, y) is walkable.
     * @param x X coordinate of the tile
     * @param y Y coordinate of the tile
     * @return true if walkable, false if blocked or out of bounds
     */
    public boolean isWalkable(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height)
            return false;
        return tiles[x][y] == 0;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int[][] getTiles() {
        return tiles;
    }

    public void setTiles(int[][] tiles) {
        this.tiles = tiles;
    }
}
