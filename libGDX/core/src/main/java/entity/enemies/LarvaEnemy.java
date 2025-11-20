package entity.enemies;

import entity.Entity;
import entity.Player;

import java.util.Random;

/**
 * Ennemi Larve : se déplace en ligne droite, rebondit, faible mais rapide.
 */
public class LarvaEnemy extends Enemy {

    private float directionX;
    private float directionY;
    private final Random rng;

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
    @Override
    public void update() {
        if (!isActive) return;
        float delta = com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        move(null, delta);
        attackPlayerIfClose();
    }
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
    private void attackPlayerIfClose() {
        float dx = targetPlayer.getX() - x;
        float dy = targetPlayer.getY() - y;

        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        if (dist < 15f) {
            targetPlayer.takeDamage(attackDamage);
        }
    }
    @Override
    public void interact(Entity e) {
        if (e instanceof Player player) {
            attack(player);
        }
    }
    @Override
    public void attack(Player player) {
        player.takeDamage(attackDamage);
    }
}
