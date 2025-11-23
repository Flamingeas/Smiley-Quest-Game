package entity.enemies;

import entity.Entity;
import entity.Player;

import java.util.Random;

/**
 * Represents a Larva-type enemy characterized by straight-line movement,
 * bouncing behavior upon collisions, and overall weak but fast attributes.
 */
public class LarvaEnemy extends Enemy {

    private float directionX;
    private float directionY;
    private final Random rng;

    /**
     * Constructs a Larva enemy using generic stats.
     *
     * @param x initial X position
     * @param y initial Y position
     * @param genHealth base health value
     * @param genSpeed base movement speed
     * @param genDamage base damage value
     * @param player target player
     */
    public LarvaEnemy(float x, float y, int genHealth, float genSpeed, int genDamage, Player player) {
        super(x, y, genHealth, genSpeed, genDamage, player);
        this.rng = new Random();
        directionX = rng.nextFloat() * 2 - 1;
        directionY = rng.nextFloat() * 2 - 1;
        float len = (float) Math.sqrt(directionX * directionX + directionY * directionY);
        if (len != 0) {
            directionX /= len;
            directionY /= len;
        }
        scaleStats(1);
    }

    /**
     * Updates the Larva enemy by moving it and triggering an attack
     * if the player is within proximity.
     */
    @Override
    public void update() {
        if (!isActive) return;
        float delta = com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        move(null, delta);
        attackPlayerIfClose();
    }

    /**
     * Handles Larva movement including bouncing off obstacles and
     * recalculating a direction if movement becomes blocked.
     *
     * @param player unused for this enemy type
     * @param delta  elapsed time
     */
    @Override
    public void move(Player player, float delta) {

        if (gameScreen == null) return;

        float speedStep = speed * 40f * delta;

        float newX = x + directionX * speedStep;
        float newY = y + directionY * speedStep;

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
            directionX = rng.nextFloat() * 2 - 1;
            directionY = rng.nextFloat() * 2 - 1;

            float len = (float) Math.sqrt(directionX * directionX + directionY * directionY);
            if (len != 0) {
                directionX /= len;
                directionY /= len;
            }
        }
    }

    /**
     * Checks the player's proximity and inflicts damage
     * if the Larva is close enough.
     */
    private void attackPlayerIfClose() {
        float dx = targetPlayer.getX() - x;
        float dy = targetPlayer.getY() - y;

        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        if (dist < 15f) {
            targetPlayer.takeDamage(attackDamage);
        }
    }

    /**
     * Handles interaction with another entity.
     * If the entity is a Player, it triggers an attack.
     *
     * @param e encountered entity
     */
    @Override
    public void interact(Entity e) {
        if (e instanceof Player player) {
            attack(player);
        }
    }

    /**
     * Inflicts damage directly to the player.
     *
     * @param player the player being attacked
     */
    @Override
    public void attack(Player player) {
        player.takeDamage(attackDamage);
    }
}
