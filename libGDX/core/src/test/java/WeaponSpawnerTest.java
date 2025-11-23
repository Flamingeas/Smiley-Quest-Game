package level;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import com.badlogic.gdx.utils.ObjectMap;
import entity.Weapon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import settings.GameWorld;
import utils.Coord;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("WeaponSpawner Tests")
class WeaponSpawnerTest {

    @Mock private TiledMap tiledMap;
    @Mock private MapLayer tileLayer;
    @Mock private TiledMapTileSet tileSet;
    @Mock private TiledMapTile tile;
    @Mock private GameWorld gameWorld;

    private WeaponSpawner spawner;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        spawner = new WeaponSpawner();
    }

    // ========== TESTS DU CONSTRUCTEUR ==========
    @Test
    @DisplayName("WeaponSpawner peut être instancié")
    void testConstructor() {
        assertNotNull(spawner);
    }

    // ========== TESTS DE SPAWN WEAPONS ==========
    @Test
    @DisplayName("SpawnWeapons retourne une liste non-null")
    void testSpawnWeaponsReturnsNotNull() {
        mockMapWithLayers(10, 10);
        when(gameWorld.isWalkableTile(anyInt(), anyInt())).thenReturn(true);

        List<WeaponSpawner.WeaponData> result = spawner.spawnWeapons(tiledMap, "test.tmx", 5, gameWorld);

        assertNotNull(result);
    }

    @Test
    @DisplayName("SpawnWeapons avec map sample utilise MAP1")
    void testSpawnWeaponsWithSampleMap() {
        mockMapWithLayers(10, 10);
        when(gameWorld.isWalkableTile(anyInt(), anyInt())).thenReturn(true);
        mockTilesetWithWeapons();

        List<WeaponSpawner.WeaponData> result = spawner.spawnWeapons(tiledMap, "sampleMap.tmx", 4, gameWorld);

        assertTrue(result.size() <= 4);
    }

    @Test
    @DisplayName("SpawnWeapons avec SampleSheet utilise MAP1")
    void testSpawnWeaponsWithSampleSheet() {
        mockMapWithLayers(10, 10);
        when(gameWorld.isWalkableTile(anyInt(), anyInt())).thenReturn(true);
        mockTilesetWithWeapons();

        List<WeaponSpawner.WeaponData> result = spawner.spawnWeapons(tiledMap, "SampleSheet.tmx", 4, gameWorld);

        assertTrue(result.size() <= 4);
    }

    @Test
    @DisplayName("SpawnWeapons retourne max weapons")
    void testSpawnWeaponsRespectMaxWeapons() {
        mockMapWithLayers(20, 20);
        when(gameWorld.isWalkableTile(anyInt(), anyInt())).thenReturn(true);
        mockTilesetWithWeapons();

        List<WeaponSpawner.WeaponData> result = spawner.spawnWeapons(tiledMap, "test.tmx", 3, gameWorld);

        assertTrue(result.size() <= 3);
    }

    @Test
    @DisplayName("SpawnWeapons avec max = 0 retourne liste vide")
    void testSpawnWeaponsMaxZero() {
        mockMapWithLayers(10, 10);
        when(gameWorld.isWalkableTile(anyInt(), anyInt())).thenReturn(true);

        List<WeaponSpawner.WeaponData> result = spawner.spawnWeapons(tiledMap, "test.tmx", 0, gameWorld);

        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("SpawnWeapons sans tuiles walkable retourne liste vide")
    void testSpawnWeaponsNoWalkableTiles() {
        mockMapWithLayers(10, 10);
        when(gameWorld.isWalkableTile(anyInt(), anyInt())).thenReturn(false);

        List<WeaponSpawner.WeaponData> result = spawner.spawnWeapons(tiledMap, "test.tmx", 5, gameWorld);

        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("SpawnWeapons avec tileset vide retourne liste vide")
    void testSpawnWeaponsEmptyTileset() {
        mockMapWithLayers(10, 10);
        when(gameWorld.isWalkableTile(anyInt(), anyInt())).thenReturn(true);

        TiledMapTileSets emptyTileSets = mock(TiledMapTileSets.class);
        when(tiledMap.getTileSets()).thenReturn(emptyTileSets);
        when(emptyTileSets.iterator()).thenReturn(new ArrayList<TiledMapTileSet>().iterator());

        List<WeaponSpawner.WeaponData> result = spawner.spawnWeapons(tiledMap, "test.tmx", 5, gameWorld);

        assertEquals(0, result.size());
    }

    // ========== TESTS DE WEAPON DATA ==========
    @Test
    @DisplayName("WeaponData contient le weapon")
    void testWeaponDataContainsWeapon() {
        mockMapWithLayers(10, 10);
        when(gameWorld.isWalkableTile(anyInt(), anyInt())).thenReturn(true);
        mockTilesetWithWeapons();

        List<WeaponSpawner.WeaponData> result = spawner.spawnWeapons(tiledMap, "sampleMap.tmx", 1, gameWorld);

        assertTrue(result.size() > 0);
        assertNotNull(result.get(0).getWeapon());
    }

    @Test
    @DisplayName("WeaponData contient le tileId")
    void testWeaponDataContainsTileId() {
        mockMapWithLayers(10, 10);
        when(gameWorld.isWalkableTile(anyInt(), anyInt())).thenReturn(true);
        mockTilesetWithWeapons();

        List<WeaponSpawner.WeaponData> result = spawner.spawnWeapons(tiledMap, "sampleMap.tmx", 1, gameWorld);

        assertTrue(result.size() > 0);
        assertTrue(result.get(0).getTileId() > 0);
    }

    @Test
    @DisplayName("WeaponData contient la position")
    void testWeaponDataContainsTilePosition() {
        mockMapWithLayers(10, 10);
        when(gameWorld.isWalkableTile(anyInt(), anyInt())).thenReturn(true);
        mockTilesetWithWeapons();

        List<WeaponSpawner.WeaponData> result = spawner.spawnWeapons(tiledMap, "sampleMap.tmx", 1, gameWorld);

        assertTrue(result.size() > 0);
        assertNotNull(result.get(0).getTilePosition());
    }

    @Test
    @DisplayName("Plusieurs WeaponData sont à des positions différentes")
    void testWeaponDataDifferentPositions() {
        mockMapWithLayers(20, 20);
        when(gameWorld.isWalkableTile(anyInt(), anyInt())).thenReturn(true);
        mockTilesetWithWeapons();

        List<WeaponSpawner.WeaponData> result = spawner.spawnWeapons(tiledMap, "sampleMap.tmx", 2, gameWorld);

        if (result.size() == 2) {
            Coord pos1 = result.get(0).getTilePosition();
            Coord pos2 = result.get(1).getTilePosition();
            assertNotEquals(pos1, pos2);
        }
    }

    // ========== TESTS HELPERS ==========
    private void mockMapWithLayers(int width, int height) {
        MapLayers layers = mock(MapLayers.class);
        when(tiledMap.getLayers()).thenReturn(layers);

        when(tileLayer.getWidth()).thenReturn(width);
        when(tileLayer.getHeight()).thenReturn(height);

        List<MapLayer> layerList = new ArrayList<>();
        layerList.add(tileLayer);
        when(layers.iterator()).thenReturn(layerList.iterator());
    }

    private void mockTilesetWithWeapons() {
        TiledMapTileSets tileSets = mock(TiledMapTileSets.class);
        when(tiledMap.getTileSets()).thenReturn(tileSets);

        List<TiledMapTile> tiles = new ArrayList<>();
        for (int i = 0; i < 120; i++) {
            tiles.add(null);
        }

        when(tileSet.iterator()).thenReturn(tiles.iterator());
        when(tileSets.iterator()).thenReturn(List.of(tileSet).iterator());

        when(tiledMap.getTileSets().getTile(anyInt())).thenReturn(tile);
    }

    private void mockTilesetWithWeaponProperty(String weaponName, int expectedDamage) {
        TiledMapTileSets tileSets = mock(TiledMapTileSets.class);
        when(tiledMap.getTileSets()).thenReturn(tileSets);

        TiledMapTile weaponTile = mock(TiledMapTile.class);
        when(weaponTile.getId()).thenReturn(100);

        ObjectMap<String, Object> properties = new ObjectMap<>();
        properties.put("weapon", weaponName);
        when(weaponTile.getProperties()).thenReturn(properties);

        List<TiledMapTile> tiles = new ArrayList<>();
        tiles.add(weaponTile);
        when(tileSet.iterator()).thenReturn(tiles.iterator());
        when(tileSets.iterator()).thenReturn(List.of(tileSet).iterator());

        when(tiledMap.getTileSets().getTile(anyInt())).thenReturn(weaponTile);
    }
}
