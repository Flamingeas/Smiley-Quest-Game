package system;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

        textures.put(GhostEnemy.class, new Texture("enemies/GhostEnemy.png"));
        textures.put(LarvaEnemy.class, new Texture("enemies/LarvaEnemy.png"));
        textures.put(SkeletonWeakEnemy.class, new Texture("enemies/SkeletonWeakEnemy.png"));
        textures.put(SkeletonStrongEnemy.class, new Texture("enemies/SkeletonStrongEnemy.png"));
        textures.put(DragonEnemy.class, new Texture("enemies/DragonEnemy.png"));
        textures.put(FinalBossEnemy.class, new Texture("enemies/FinalBossEnemy.png"));
    }

    public void render(SpriteBatch batch, Enemy e) {
        Texture t = textures.get(e.getClass());
        if (t != null && e.isActive()) {
            batch.draw(t, e.getX(), e.getY());
        }
    }
    public TextureRegion getIcon(Enemy enemy) {
        return enemy.getIconRegion();
    }
    public Texture getTextureFor(Enemy enemy) {
        return textures.get(enemy.getClass());
    }
}
