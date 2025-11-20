package system;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import entity.enemies.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Système de rendu des ennemis.
 */
public class EnemyRenderer {

    private final Map<Class<?>, Texture> textures;

    public EnemyRenderer() {
        textures = new HashMap<>();

        textures.put(GhostEnemy.class, new Texture("images/enemies/phantom.png"));
        textures.put(LarvaEnemy.class, new Texture("images/enemies/larve.png"));
        textures.put(SkeletonWeakEnemy.class, new Texture("images/enemies/squelet.png"));
        textures.put(SkeletonStrongEnemy.class, new Texture("images/enemies/squelet1.png"));
        textures.put(DragonEnemy.class, new Texture("images/enemies/dragon.png"));
        textures.put(FinalBossEnemy.class, new Texture("images/enemies/dragon.png"));
    }

    public void render(SpriteBatch batch, Enemy e) {
        Texture t = textures.get(e.getClass());
        if (t != null && e.isActive()) {
            batch.draw(t, e.getX(), e.getY());
        }
    }
}
