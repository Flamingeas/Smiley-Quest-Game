/**
 * Represents a weak skeleton enemy in the game.
 * Inherits generic stats from Enemy and targets the player.
 */
package entity.enemies;

import entity.Entity;
import entity.Player;
import java.util.Random;

public class SkeletonWeakEnemy extends Enemy {

    /** Random generator used for slight movement variation. */
    private final Random rng;

    /**
     * Constructs a SkeletonWeakEnemy with generic stats.
     * Sets texture and target player.
     *
     * @param tileX initial tile X position
     * @param tileY initial tile Y position
     * @param genHealth base health value
     * @param genSpeed base speed
     * @param genDamage base attack damage
     * @param player target player
     */
    public SkeletonWeakEnemy(float tileX, float tileY, int genHealth, float genSpeed, int genDamage, Player player) {
        super(tileX, tileY, genHealth, genSpeed, genDamage, player);

        this.targetPlayer = player;
        this.rng = new Random();

        this.texture = new com.badlogic.gdx.graphics.Texture("enemies/SkeletonWeakEnemy.png");

        scaleStats(1);
    }

    /**
     * Updates the enemy each frame: moves towards the player and attacks if in range.
     */
    @Override
    public void update() {
        if (!isActive) return;

        super.update();

        float delta = com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        move(targetPlayer, delta);

        tryAttack(targetPlayer);
    }
    /**
     * Moves the enemy towards the player with slight random variation.
     * Attempts a fallback movement if blocked.
     *
     * @param player target player
     * @param delta elapsed time
     */
    @Override
    public void move(Player player, float delta) {

        if (player == null || gameScreen == null) return;

        float dx = player.getX() - x;
        float dy = player.getY() - y;

        float len = (float)Math.sqrt(dx*dx + dy*dy);
        if (len == 0) return;

        dx /= len;
        dy /= len;

        dx += (rng.nextFloat() - 0.5f) * 0.20f;
        dy += (rng.nextFloat() - 0.5f) * 0.20f;
        float len2 = (float)Math.sqrt(dx*dx + dy*dy);
        dx /= len2;
        dy /= len2;

        float moveSpeed = speed * 32f * delta;

        float newX = x + dx * moveSpeed;
        float newY = y + dy * moveSpeed;

        boolean moved = false;

        if (gameScreen.canMoveGeneric(newX, y)) {
            x = newX;
            moved = true;
        }
        if (gameScreen.canMoveGeneric(x, newY)) {
            y = newY;
            moved = true;
        }
        if (!moved) {
            float angle = rng.nextFloat() * (float)Math.PI * 2;
            float ox = (float)Math.cos(angle) * moveSpeed * 0.6f;
            float oy = (float)Math.sin(angle) * moveSpeed * 0.6f;

            if (gameScreen.canMoveGeneric(x + ox, y)) x += ox;
            if (gameScreen.canMoveGeneric(x, y + oy)) y += oy;
        }
    }
    /**
     * Handles interaction with another entity: attacks if the entity is a player.
     *
     * @param e entity encountered
     */
    @Override
    public void interact(Entity e) {
        if (e instanceof Player player) {
            tryAttack(player);
        }
    }

    /**
     * Attacks the player if within attack range.
     *
     * @param player player to attack
     */
    @Override
    public void attack(Player player) {
        tryAttack(player);
    }

    private void tryAttack(Player player) {
        float dx = player.getX() - x;
        float dy = player.getY() - y;

        float dist = (float)Math.sqrt(dx*dx + dy*dy);

        if (dist < 22f) {
            super.attack(player);
        }
    }
}
