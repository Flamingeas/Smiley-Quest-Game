package settings.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import settings.GameWorld;
import level.Level;
import utils.Coord;

import java.util.Collections;

public class GameScreen implements Screen {

    private OrthographicCamera camera;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;
    private GameWorld gameWorld;

    public GameScreen() {
        // Tu peux préparer ton Level et GameWorld ici
        Level level = new Level(
                Collections.emptyList(), // pas d'ennemis pour l’instant
                Collections.emptyList(), // pas d’items
                new Coord(2, 2),         // spawn joueur provisoire
                null                     // on remplace plus tard
        );

        this.gameWorld = new GameWorld(level);
    }

    @Override
    public void show() {
        // Chargement de la map Tiled
        tiledMap = new TmxMapLoader().load("maps/mapdebutant.tmx");

        // Renderer et caméra
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1f);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        System.out.println("Map chargée et affichée !");
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();

        mapRenderer.setView(camera);
        mapRenderer.render();

        // Mise à jour du monde de jeu
        gameWorld.update(delta);
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override
    public void dispose() {
        tiledMap.dispose();
        mapRenderer.dispose();
    }
}
