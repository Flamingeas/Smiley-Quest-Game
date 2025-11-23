/**
 * Represents a ghost-type enemy featuring oscillating spectral movement
 * and constant pursuit of the player.
 */
package entity.enemies;

import com.badlogic.gdx.graphics.Texture;
import entity.Entity;
import entity.Player;
import java.util.Random;

/**
 * GhostEnemy implements a spectral movement pattern combined with
 * continuous tracking of the player. Its movement includes sinusoidal
 * oscillation to create an unpredictable drifting motion.
 */
public class GhostEnemy extends Enemy {

    private final Random rng = new Random();
    private float phase;

    /**
     * Creates a new GhostEnemy using tile-based coordinates and generated stats.
     *
     * @param tileX      tile X coordinate
     * @param tileY      tile Y coordinate
     * @param genHealth  base health
     * @param genSpeed   base speed
     * @param genDamage  base damage
     * @param player     target player
     */
    public GhostEnemy(float tileX, float tileY,
                      int genHealth, float genSpeed, int genDamage,
                      Player player) {

        super(tileX, tileY, genHealth, genSpeed, genDamage, player);

        this.phase = rng.nextFloat() * (float)Math.PI;
        this.attackRange = 40f;

        this.texture = new Texture("enemies/GhostEnemy.png");
        this.targetPlayer = player;

        scaleStats(1);
    }

    /**
     * Update logic:
     * - parent handles knockback / base timers
     * - ghost-style movement
     * - ghost attack via Enemy.baseAttack()
     */
    @Override
    public void update() {

        if (!isActive) return;

        super.update();

        float delta = com.badlogic.gdx.Gdx.graphics.getDeltaTime();

        move(targetPlayer, delta);
        super.attack(targetPlayer);
    }

    /**
     * Spectral drifting movement:
     * - always moving toward the player
     * - with sinusoidal drift to simulate floating
     */
    @Override
    public void move(Player player, float delta) {

        if (player == null || !isActive) return;

        float dx = player.getX() - x;
        float dy = player.getY() - y;

        float dist = (float) Math.sqrt(dx*dx + dy*dy);
        if (dist != 0f) {
            dx /= dist;
            dy /= dist;
        }

        float oscX = (float) Math.sin(phase) * 0.40f;
        float oscY = (float) Math.cos(phase) * 0.40f;
        phase += 0.045f;

        float moveSpeed = speed * delta * 28f;

        float newX = x + (dx + oscX) * moveSpeed;
        float newY = y + (dy + oscY) * moveSpeed;

        x = newX;
        y = newY;
    }

    /**
     * Interaction via collision: ghost applies immediate damage.
     */
    @Override
    public void interact(Entity e) {
        if (e instanceof Player player) {
            super.attack(player);
        }
    }

    /**
     * Override of attack: ghost uses very short melee distance.
     */
    @Override
    public void attack(Player player) {

        if (player == null || !isActive) return;

        float dx = player.getX() - x;
        float dy = player.getY() - y;
        float dist = (float) Math.sqrt(dx*dx + dy*dy);

        if (dist < 18f) {
            player.takeDamage(attackDamage);
        }
    }
}
