package entity.enemies;

import com.badlogic.gdx.graphics.Texture;
import entity.Player;
import java.util.Random;

/**
 * Represents a Larva-type enemy characterized by straight-line movement,
 * bouncing behavior upon collisions, and weak but fast attack behavior.
 */
public class LarvaEnemy extends Enemy {

    private float directionX;
    private float directionY;
    private final Random rng = new Random();

    /**
     * Constructs a Larva enemy using tile-based positions and generated stats.
     */
    public LarvaEnemy(float tileX, float tileY,
                      int genHealth, float genSpeed, int genDamage,
                      Player player) {

        super(tileX, tileY, genHealth, genSpeed, genDamage, player);

        this.texture = new Texture("enemies/LarvaEnemy.png");
        this.attackRange = 40f;

        regenerateDirection();
        scaleStats(1);
    }

    /**
     * Generates a random normalized direction for the larva.
     */
    private void regenerateDirection() {
        directionX = rng.nextFloat() * 2f - 1f;
        directionY = rng.nextFloat() * 2f - 1f;

        float len = (float) Math.sqrt(directionX * directionX + directionY * directionY);
        if (len != 0f) {
            directionX /= len;
            directionY /= len;
        }
    }

    /**
     * Update sequence:
     * - Knockback / cooldown via super.update()
     * - Movement
     * - Attack if close enough
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
     * Moves linearly and bounces on walls.
     */
    @Override
    public void move(Player player, float delta) {

        if (gameScreen == null) return;

        float step = speed * 40f * delta;

        float newX = x + directionX * step;
        float newY = y + directionY * step;

        boolean moved = false;

        if (gameScreen.canMoveGeneric(newX, y)) {
            x = newX;
            moved = true;
        } else {
            directionX *= -1;
        }

        if (gameScreen.canMoveGeneric(x, newY)) {
            y = newY;
            moved = true;
        } else {
            directionY *= -1;
        }

        if (!moved) {
            regenerateDirection();
        }
    }

    /**
     * Attack on close proximity using parent's cooldown system.
     */
    @Override
    public void attack(Player player) {
        if (player == null) return;

        float dx = player.getX() - x;
        float dy = player.getY() - y;
        float dist = (float)Math.sqrt(dx * dx + dy * dy);

        if (dist < 35f) {
            super.attack(player);
        }
    }
}
