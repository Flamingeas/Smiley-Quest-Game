/**
 * Handles the spawning of weapons on tiled maps.
 * <p>
 * Supports legacy MAP1 system and modern tileset-based maps.
 * Spawns weapons at walkable tiles and generates WeaponData for game integration.
 * </p>
 */
package level;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.*;
import entity.Weapon;
import utils.Coord;

import java.util.*;

public class WeaponSpawner {

    private static final int[] MAP1_WEAPON_IDS = {105, 118, 119, 114};
    private static final int[] MAP1_WEAPON_DMG = {10, 15, 12, 8};
    private static final String[] MAP1_WEAPON_NAMES = {"Épée", "Marteau", "Hache", "Poison"};

    /**
     * Spawns weapons based on map type.
     */
    public List<WeaponData> spawnWeapons(
            TiledMap tiledMap,
            String mapFile,
            int maxWeapons,
            settings.GameWorld gameWorld
    ) {
        if (mapFile.contains("sample") || mapFile.contains("SampleSheet")) {
            return spawnWeaponsMap1(tiledMap, maxWeapons, gameWorld);
        }

        return spawnFromTileset(tiledMap, maxWeapons, gameWorld);
    }

    /** MAP1 legacy system weapon spawner */
    private List<WeaponData> spawnWeaponsMap1(TiledMap map, int max, settings.GameWorld gameWorld) {
        List<WeaponData> result = new ArrayList<>();
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        int w = layer.getWidth();
        int h = layer.getHeight();

        List<Coord> freeTiles = new ArrayList<>();
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                if (gameWorld.isWalkableTile(x, y))
                    freeTiles.add(new Coord(x, y));

        Collections.shuffle(freeTiles);
        max = Math.min(max, MAP1_WEAPON_IDS.length);

        for (int i = 0; i < max; i++) {
            int tileId = MAP1_WEAPON_IDS[i];
            TiledMapTile tile = map.getTileSets().getTile(tileId);
            TextureRegion icon = tile != null ? tile.getTextureRegion() : null;
            Coord pos = freeTiles.get(i);

            Weapon weapon = new Weapon(pos.getX(), pos.getY(), MAP1_WEAPON_DMG[i], MAP1_WEAPON_NAMES[i], icon);

            weapon.setHeldRegion(icon);

            result.add(new WeaponData(weapon, tileId, pos));
        }

        return result;
    }

    /** Modern tileset-based weapon spawner */
    private List<WeaponData> spawnFromTileset(TiledMap map, int max, settings.GameWorld gameWorld) {
        List<WeaponTile> declaredWeapons = new ArrayList<>();

        for (TiledMapTileSet set : map.getTileSets()) {
            for (TiledMapTile tile : set) {
                if (tile == null) continue;

                if (tile.getProperties().containsKey("weapon")) {
                    String name = tile.getProperties().get("weapon", String.class);
                    int dmg = getWeaponDamage(name);
                    declaredWeapons.add(new WeaponTile(tile.getId(), name, dmg));
                }
            }
        }

        if (declaredWeapons.isEmpty()) return new ArrayList<>();
        Collections.shuffle(declaredWeapons);

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        int w = layer.getWidth();
        int h = layer.getHeight();

        List<Coord> walkable = new ArrayList<>();
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                if (gameWorld.isWalkableTile(x, y))
                    walkable.add(new Coord(x, y));

        Collections.shuffle(walkable);

        max = Math.min(max, declaredWeapons.size());
        max = Math.min(max, walkable.size());

        List<WeaponData> result = new ArrayList<>();

        for (int i = 0; i < max; i++) {
            WeaponTile wt = declaredWeapons.get(i);
            Coord pos = walkable.get(i);

            TiledMapTile tile = map.getTileSets().getTile(wt.tileId);
            TextureRegion icon = tile != null ? tile.getTextureRegion() : null;

            Weapon weapon = new Weapon(pos.getX(), pos.getY(), wt.damage, wt.name, icon);

            weapon.setHeldRegion(icon);

            result.add(new WeaponData(weapon, wt.tileId, pos));
        }

        return result;
    }
    /** Set weapon damage based on name */
    private int getWeaponDamage(String name) {
        switch (name.toLowerCase()) {
            case "sword", "magicsword" -> {
                return 5;
            }
            case "divinesword" -> {
                return 8;
            }
            case "legendaryhach", "massivhache" -> {
                return 6;
            }
            case "massivmass", "hammer", "marteau" -> {
                return 7;
            }
            case "poison" -> {
                return 3;
            }
            case "thor" -> {
                return 8;
            }
            default -> {
                return 4;
            }
        }
    }
    private static class WeaponTile {
        int tileId;
        String name;
        int damage;

        WeaponTile(int tileId, String name, int damage) {
            this.tileId = tileId;
            this.name = name;
            this.damage = damage;
        }
    }

    /** Data for one spawned weapon */
    public static class WeaponData {
        private Weapon weapon;
        private int tileId;
        private Coord tilePosition;

        public WeaponData(Weapon weapon, int tileId, Coord tilePos) {
            this.weapon = weapon;
            this.tileId = tileId;
            this.tilePosition = tilePos;
        }

        public Weapon getWeapon() { return weapon; }
        public int getTileId() { return tileId; }
        public Coord getTilePosition() { return tilePosition; }
    }
}
