package level;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import entity.Weapon;
import utils.Coord;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Génère des armes aléatoirement sur une map Tiled.
 */
public class WeaponSpawner {

    /** IDs des tuiles marchables pour sampleMap */
    private static final Set<Integer> WALKABLE_TILES = new HashSet<>();
    static {
        WALKABLE_TILES.add(31);
        WALKABLE_TILES.add(37);
        WALKABLE_TILES.add(38);
        WALKABLE_TILES.add(39);
        WALKABLE_TILES.add(49);
        WALKABLE_TILES.add(50);
        WALKABLE_TILES.add(51);
        WALKABLE_TILES.add(52);
        WALKABLE_TILES.add(53);
        WALKABLE_TILES.add(54);
    }

    private static final int[] WEAPON_IDS = {105, 118, 119, 114};
    private static final int[] WEAPON_DAMAGES = {10, 15, 12, 8};
    private static final String[] WEAPON_NAMES = {"Épée", "Marteau", "Hache", "Poison"};

    private Random random;

    /**
     * Initialise le générateur d'armes.
     */
    public WeaponSpawner() {
        this.random = new Random();
    }

    /**
     * Génère des armes aléatoires sur la map.
     * @param tiledMap La map sur laquelle placer les armes
     * @param maxWeapons Nombre maximum d'armes à générer (max 10)
     * @return Liste des armes avec leur position et ID de tuile
     */
    public List<WeaponData> spawnWeapons(TiledMap tiledMap, int maxWeapons) {
        List<WeaponData> weaponDataList = new ArrayList<>();
        maxWeapons = Math.min(maxWeapons, 10);

        TiledMapTileLayer dungeonLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Dungeon");
        if (dungeonLayer == null) return weaponDataList;

        int mapWidth = tiledMap.getProperties().get("width", Integer.class);
        int mapHeight = tiledMap.getProperties().get("height", Integer.class);

        List<Coord> walkableTiles = findWalkableTiles(dungeonLayer, mapWidth, mapHeight);
        if (walkableTiles.isEmpty()) return weaponDataList;

        List<Coord> selectedPositions = selectRandomPositions(walkableTiles, maxWeapons);

        for (Coord position : selectedPositions) {
            int weaponIndex = random.nextInt(WEAPON_IDS.length);
            int weaponId = WEAPON_IDS[weaponIndex];
            int damage = WEAPON_DAMAGES[weaponIndex];
            String weaponName = WEAPON_NAMES[weaponIndex];

            Weapon weapon = new Weapon(position.getX(), position.getY(), damage);
            weapon.setName(weaponName);

            WeaponData weaponData = new WeaponData(weapon, weaponId, position);
            weaponDataList.add(weaponData);
        }

        return weaponDataList;
    }

    /**
     * Trouve toutes les cases marchables sur la couche.
     * @param layer Couche de la map
     * @param width Largeur de la map
     * @param height Hauteur de la map
     * @return Liste des coordonnées des cases marchables
     */
    private List<Coord> findWalkableTiles(TiledMapTileLayer layer, int width, int height) {
        List<Coord> walkableTiles = new ArrayList<>();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (isTileWalkable(layer, x, y)) walkableTiles.add(new Coord(x, y));
            }
        }
        return walkableTiles;
    }

    /**
     * Vérifie si une tuile est marchable.
     * @param layer Couche contenant la tuile
     * @param x Coordonnée X
     * @param y Coordonnée Y
     * @return true si la tuile est marchable
     */
    private boolean isTileWalkable(TiledMapTileLayer layer, int x, int y) {
        TiledMapTileLayer.Cell cell = layer.getCell(x, y);
        if (cell == null || cell.getTile() == null) return false;
        int tileId = cell.getTile().getId();
        if (tileId > 2000) tileId = tileId & 0x1FFFFFFF;
        return WALKABLE_TILES.contains(tileId);
    }

    /**
     * Sélectionne aléatoirement un nombre de positions parmi la liste disponible.
     * @param positions Liste de positions disponibles
     * @param count Nombre de positions à sélectionner
     * @return Liste des positions sélectionnées
     */
    private List<Coord> selectRandomPositions(List<Coord> positions, int count) {
        List<Coord> selected = new ArrayList<>();
        List<Coord> available = new ArrayList<>(positions);
        for (int i = 0; i < count && !available.isEmpty(); i++) {
            int randomIndex = random.nextInt(available.size());
            selected.add(available.remove(randomIndex));
        }
        return selected;
    }

    /**
     * Contient les informations d'une arme sur la map.
     */
    public static class WeaponData {
        private Weapon weapon;
        private int tileId;
        private Coord tilePosition;

        public WeaponData(Weapon weapon, int tileId, Coord tilePosition) {
            this.weapon = weapon;
            this.tileId = tileId;
            this.tilePosition = tilePosition;
        }

        /** @return L'objet arme */
        public Weapon getWeapon() {
            return weapon;
        }

        /** @return ID de la tuile correspondant à l'arme */
        public int getTileId() {
            return tileId;
        }

        /** @return Position de la tuile */
        public Coord getTilePosition() {
            return tilePosition;
        }
    }
}
