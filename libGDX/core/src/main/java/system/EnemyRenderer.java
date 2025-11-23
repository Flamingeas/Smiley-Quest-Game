package system;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import entity.enemies.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Renders enemies on the screen and provides access to their textures.
 *
 * Maintains a mapping from enemy classes to their textures, and draws
 * the appropriate texture for active enemies.
 */
public class EnemyRenderer {

    /** Maps each enemy class to its corresponding texture. */
    private final Map<Class<?>, Texture> textures;

    /**
     * Initializes the renderer and loads textures for all enemy types.
     */
    public EnemyRenderer() {
        textures = new HashMap<>();

        textures.put(GhostEnemy.class, new Texture("enemies/GhostEnemy.png"));
        textures.put(LarvaEnemy.class, new Texture("enemies/LarvaEnemy.png"));
        textures.put(SkeletonWeakEnemy.class, new Texture("enemies/SkeletonWeakEnemy.png"));
        textures.put(SkeletonStrongEnemy.class, new Texture("enemies/SkeletonStrongEnemy.png"));
        textures.put(DragonEnemy.class, new Texture("enemies/DragonEnemy.png"));
        textures.put(FinalBossEnemy.class, new Texture("enemies/FinalBossEnemy.png"));
    }

    /**
     * Draws the given enemy on the screen using its associated texture.
     * Only renders the enemy if it is active.
     *
     * @param batch the SpriteBatch used for drawing
     * @param e the enemy to render
     */
    public void render(SpriteBatch batch, Enemy e) {
        Texture t = textures.get(e.getClass());
        if (t != null && e.isActive()) {
            batch.draw(t, e.getX(), e.getY());
        }
    }

    /**
     * Returns the icon region of the given enemy.
     *
     * @param enemy the enemy
     * @return the TextureRegion representing the enemy's icon
     */
    public TextureRegion getIcon(Enemy enemy) {
        return enemy.getIconRegion();
    }

    /**
     * Returns the texture associated with the enemy's class.
     *
     * @param enemy the enemy
     * @return the Texture for this enemy type, or null if not found
     */
    public Texture getTextureFor(Enemy enemy) {
        return textures.get(enemy.getClass());
    }
}
