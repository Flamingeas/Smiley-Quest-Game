package level;

import com.badlogic.gdx.maps.tiled.*;
import entity.Weapon;
import utils.Coord;

import java.util.*;

public class WeaponSpawner {

    private Random random = new Random();

    private static final int[] MAP1_WEAPON_IDS = {105, 118, 119, 114};
    private static final int[] MAP1_WEAPON_DMG = {10, 15, 12, 8};
    private static final String[] MAP1_WEAPON_NAMES = {"Épée", "Marteau", "Hache", "Poison"};

    public List<WeaponData> spawnWeapons(
            TiledMap tiledMap,
            String mapFile,
            int maxWeapons,
            settings.GameWorld gameWorld
    ) {
        if (mapFile.contains("sampleMap")) {
            System.out.println("MAP1 détectée → ancien système");
            return spawnWeaponsMap1(tiledMap, maxWeapons, gameWorld);
        }

        System.out.println("Nouveau système activé (weapon=\"...\")");
        return spawnWeaponsModern(tiledMap, maxWeapons, gameWorld);
    }

    private List<WeaponData> spawnWeaponsMap1(
            TiledMap map,
            int max,
            settings.GameWorld gameWorld
    ) {
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
            Coord pos = freeTiles.get(i);

            Weapon weapon = new Weapon(pos.getX(), pos.getY(), MAP1_WEAPON_DMG[i]);
            weapon.setName(MAP1_WEAPON_NAMES[i]);

            result.add(new WeaponData(weapon, MAP1_WEAPON_IDS[i], pos));
        }

        return result;
    }

    private List<WeaponData> spawnWeaponsModern(
            TiledMap map,
            int max,
            settings.GameWorld gameWorld
    ) {
        List<WeaponTile> tilesetWeapons = new ArrayList<>();

        for (TiledMapTileSet set : map.getTileSets()) {
            for (TiledMapTile tile : set) {
                if (tile == null) continue;

                if (tile.getProperties().containsKey("weapon")) {
                    String weaponName = tile.getProperties().get("weapon", String.class);
                    int damage = getWeaponDamage(weaponName);

                    tilesetWeapons.add(new WeaponTile(tile.getId(), weaponName, damage));
                }
            }
        }

        if (tilesetWeapons.isEmpty()) {
            System.out.println("⚠ Aucun tile weapon=\"...\" trouvé !");
            return new ArrayList<>();
        }

        Collections.shuffle(tilesetWeapons);
        int duplicates = switch(gameWorld.getCurrentLevelNumber()) {
            case 2 -> 2;
            case 3 -> 3;
            case 4 -> 4;
            default -> 1;
        };
        List<WeaponTile> expanded = new ArrayList<>();

        for (WeaponTile wt : tilesetWeapons) {
            for (int i = 0; i < duplicates; i++) {
                expanded.add(new WeaponTile(wt.tileId, wt.name, wt.damage));
            }
        }

        tilesetWeapons = expanded;
        Collections.shuffle(tilesetWeapons);

        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        int w = layer.getWidth();
        int h = layer.getHeight();

        List<Coord> walkable = new ArrayList<>();

        Coord chest = gameWorld.getCurrentLevel().getEndPoint();
        int px = (int)((gameWorld.getPlayer().getX()
                - gameWorld.getGameScreen().getMapOffsetX())
                / gameWorld.getGameScreen().getTileSize());
        int py = (int)((gameWorld.getPlayer().getY()
                - gameWorld.getGameScreen().getMapOffsetY())
                / gameWorld.getGameScreen().getTileSize());

        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++) {

                if (!gameWorld.isWalkableTile(x, y)) continue;

                if (chest != null && x == chest.getX() && y == chest.getY())
                    continue;

                if (x == px && y == py) continue;

                walkable.add(new Coord(x, y));
            }

        if (walkable.isEmpty()) {
            System.out.println("Aucun tile walkable trouvé !");
            return new ArrayList<>();
        }

        Collections.shuffle(walkable);

        max = Math.min(max, tilesetWeapons.size());
        max = Math.min(max, walkable.size());
        List<WeaponData> result = new ArrayList<>();

        for (int i = 0; i < max; i++) {
            WeaponTile wt = tilesetWeapons.get(i);
            Coord pos = walkable.get(i);

            Weapon weapon = new Weapon(pos.getX(), pos.getY(), wt.damage);
            weapon.setName(wt.name);

            result.add(new WeaponData(weapon, wt.tileId, pos));
        }

        return result;
    }

    private int getWeaponDamage(String name) {
        return switch (name.toLowerCase()) {
            case "sword" -> 10;
            case "thor" -> 15;
            case "poison" -> 8;
            case "axe" -> 12;
            default -> 5;
        };
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
