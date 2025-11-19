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

        textures.put(GhostEnemy.class, new Texture("enemies/ghost.png"));
        textures.put(LarvaEnemy.class, new Texture("enemies/larva.png"));
        textures.put(SkeletonWeakEnemy.class, new Texture("enemies/skeleton.png"));
        textures.put(SkeletonStrongEnemy.class, new Texture("enemies/strong_skeleton.png"));
        textures.put(DragonEnemy.class, new Texture("enemies/dragon.png"));
        textures.put(FinalBossEnemy.class, new Texture("enemies/boss.png"));
    }

    public void render(SpriteBatch batch, Enemy e) {
        Texture t = textures.get(e.getClass());
        if (t != null && e.isActive()) {
            batch.draw(t, e.getX(), e.getY());
        }
    }
}
