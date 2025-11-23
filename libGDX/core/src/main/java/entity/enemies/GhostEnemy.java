/**
 * Represents a ghost-type enemy featuring oscillating spectral movement
 * and constant pursuit of the player.
 */
package entity.enemies;

import entity.Entity;
import entity.Player;
import java.util.Random;

/**
 * GhostEnemy implements a spectral movement pattern combined with
 * continuous tracking of the player. Its movement includes sinusoidal
 * oscillation to create an unpredictable drifting motion.
 */
public class GhostEnemy extends Enemy {

    private final Player targetPlayer;
    private final Random rng;
    private float phase;

    /**
     * Creates a new GhostEnemy using generic stats and initial coordinates.
     * The ghost begins with a random oscillation phase.
     *
     * @param x         initial X coordinate (tile-based)
     * @param y         initial Y coordinate (tile-based)
     * @param genHealth base health value
     * @param genSpeed  base movement speed
     * @param genDamage base attack damage
     * @param player    player the ghost will follow
     */
    public GhostEnemy(float x, float y, int genHealth, float genSpeed, int genDamage, Player player) {
        super(x, y, genHealth, genSpeed, genDamage, player);
        this.targetPlayer = player;
        this.rng = new Random();
        this.phase = rng.nextFloat() * 3.14f;
        scaleStats(1);

        this.attackRange = 40f;
    }

    /**
     * Updates the ghost's movement and triggers its attack logic.
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
     * Applies spectral drifting movement with sinusoidal oscillation
     * while steadily moving toward the player.
     *
     * @param player player the ghost is targeting
     * @param delta  time delta for movement scaling
     */
    @Override
    public void move(Player player, float delta) {

        float dx = player.getX() - x;
        float dy = player.getY() - y;

        float dist = (float)Math.sqrt(dx*dx + dy*dy);
        if (dist != 0) {
            dx /= dist;
            dy /= dist;
        }

        float oscX = (float)Math.sin(phase) * 0.4f;
        float oscY = (float)Math.cos(phase) * 0.4f;
        phase += 0.04f;

        float finalSpeed = speed * delta * 28f;

        x += (dx + oscX) * finalSpeed;
        y += (dy + oscY) * finalSpeed;
    }

    /**
     * Ghost interaction logic: attacks the player on collision.
     *
     * @param e entity encountered
     */
    @Override
    public void interact(Entity e) {
        if (e instanceof Player) {
            attack((Player) e);
        }
    }

    /**
     * Performs a melee attack when the ghost is close enough to the player.
     *
     * @param player player to damage
     */
    @Override
    public void attack(Player player) {
        float dx = player.getX() - x;
        float dy = player.getY() - y;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        if (dist < 20f) {
            player.takeDamage(attackDamage);
        }
    }
}