package entity.enemies;

import entity.Entity;
import entity.Player;
import java.util.Random;

/**
 * Ennemi de type larve, rapide, faible et se déplaçant en ligne droite.
 */
public class LarvaEnemy extends Enemy {

    private float directionX;
    private float directionY;
    private final Random rng;

    /**
     * Constructeur de la larve.
     *
     * @param x position X initiale
     * @param y position Y initiale
     * @param genHealth points de vie génériques
     * @param genSpeed vitesse générique
     * @param genDamage dégâts génériques
     * @param player joueur ciblé (pour interactions)
     */
    public LarvaEnemy(float x, float y, int genHealth, float genSpeed, int genDamage, Player player) {
        super(x, y, genHealth, genSpeed, genDamage, player);
        rng = new Random();
        scaleStats(1);

        directionX = rng.nextFloat() * 2 - 1;
        directionY = rng.nextFloat() * 2 - 1;
    }

    /**
     * Met à jour la larve : déplacement en ligne droite et interaction avec le joueur.
     */
    @Override
    public void update() {
        if (!isActive) return;
        move(null, 1f);
        attackPlayerIfClose();
    }

    /**
     * Déplacement linéaire de la larve.
     */
    @Override
    public void move(Player player, float delta) {
        float finalSpeed = speed * delta;
        x += directionX * finalSpeed;
        y += directionY * finalSpeed;

        if (x < 0 || x > 800) directionX *= -1;
        if (y < 0 || y > 600) directionY *= -1;
    }
    @Override
    public void interact(Entity e) {
        if (e instanceof Player player) attack(player);
    }
    private void attackPlayerIfClose() {
        float dx = targetPlayer.getX() - x;
        float dy = targetPlayer.getY() - y;
        float dist = (float) Math.sqrt(dx*dx + dy*dy);
        if (dist < 15f) {
            targetPlayer.takeDamage(attackDamage);
        }
    }
    @Override
    public void attack(Player player) {
        player.takeDamage(attackDamage);
    }
}

