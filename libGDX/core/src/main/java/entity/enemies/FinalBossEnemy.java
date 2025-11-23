/**
 * Represents the final boss enemy, featuring both melee and ranged attacks,
 * adaptive movement, and independent attack cooldowns.
 */
package entity.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import entity.Player;
import entity.Entity;

/**
 * The FinalBossEnemy class provides advanced combat behavior including
 * dual attack types (melee and ranged) and dynamic movement around the player.
 */
public class FinalBossEnemy extends Enemy {

    private float meleeCooldown = 0f;
    private float rangedCooldown = 0f;

    private final float meleeRange = 35f;
    private final float rangedRange = 260f;
    private final float keepDistance = 130f;

    /**
     * Creates a new instance of the Final Boss enemy using tile coordinates.
     *
     * @param tileX     X tile coordinate
     * @param tileY     Y tile coordinate
     * @param genHealth base health value
     * @param genSpeed  base movement speed
     * @param genDamage base attack damage
     * @param player    reference to the player
     */
    public FinalBossEnemy(float tileX, float tileY, int genHealth, float genSpeed, int genDamage, Player player) {
        super(tileX, tileY, genHealth, genSpeed, genDamage, player);

        this.texture = new Texture("enemies/FinalBossEnemy.png");
        this.targetPlayer = player;

        this.attackRange = meleeRange;
        scaleStats(1);
    }

    /**
     * Updates the boss logic including movement, melee attacks,
     * and ranged attacks, each with their own cooldown.
     */
    @Override
    public void update() {
        if (!isActive) return;

        float delta = Gdx.graphics.getDeltaTime();
        move(targetPlayer, delta);

        if (meleeCooldown <= 0f) {
            tryMeleeAttack(targetPlayer);
            meleeCooldown = 1.5f;
        } else {
            meleeCooldown -= delta;
        }

        if (rangedCooldown <= 0f) {
            tryRangedAttack(targetPlayer);
            rangedCooldown = 3f;
        } else {
            rangedCooldown -= delta;
        }
    }

    /**
     * Moves the boss toward the player until reaching a certain distance,
     * then applies an oscillating movement pattern for unpredictability.
     *
     * @param player reference player used for movement direction
     * @param delta  frame delta time
     */
    @Override
    public void move(Player player, float delta) {
        if (gameScreen == null || player == null) return;

        float dx = player.getX() - x;
        float dy = player.getY() - y;

        float dist = (float) Math.sqrt(dx*dx + dy*dy);
        if (dist == 0) return;

        dx /= dist;
        dy /= dist;

        float moveSpeed = speed * 32f * delta;
        float newX = x;
        float newY = y;

        if (dist > keepDistance) {
            newX += dx * moveSpeed;
            newY += dy * moveSpeed;

        } else {
            float t = Gdx.graphics.getFrameId() * 0.02f;
            newX += (float) Math.sin(t) * moveSpeed * 0.7f;
            newY += (float) Math.cos(t) * moveSpeed * 0.7f;
        }

        if (gameScreen.canMoveGeneric(newX, y)) x = newX;
        if (gameScreen.canMoveGeneric(x, newY)) y = newY;
    }

    /**
     * Interacts with another entity. If the entity is a player,
     * performs a melee attack.
     *
     * @param e entity to interact with
     */
    @Override
    public void interact(Entity e) {
        if (e instanceof Player player) {
            tryMeleeAttack(player);
        }
    }
    /**
     * Performs a melee attack when the player is very close.
     *
     * @param player the player being targeted
     */
    private void tryMeleeAttack(Player player) {
        if (player == null) return;

        float dx = player.getX() - x;
        float dy = player.getY() - y;

        float dist = (float) Math.sqrt(dx*dx + dy*dy);

        if (dist < meleeRange) {
            super.attack(player);
        }
    }
    /**
     * Performs a ranged attack when the player is within a larger radius.
     *
     * @param player the player being targeted
     */
    private void tryRangedAttack(Player player) {
        if (player == null) return;

        float dx = player.getX() - x;
        float dy = player.getY() - y;

        float dist = (float) Math.sqrt(dx*dx + dy*dy);

        if (dist < rangedRange) {
            int realDamage = attackDamage;
            attackDamage = attackDamage / 2;
            super.attack(player);
            attackDamage = realDamage;
        }
    }
}