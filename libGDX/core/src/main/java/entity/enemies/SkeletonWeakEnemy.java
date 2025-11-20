package entity.enemies;

import entity.Entity;
import entity.Player;
import java.util.Random;

/**
 * Ennemi de type squelette, agressif et poursuivant le joueur.
 */
public class SkeletonWeakEnemy extends Enemy {

    private final Random rng;

    /**
     * Constructeur du squelette.
     *
     * @param x position X initiale
     * @param y position Y initiale
     * @param genHealth points de vie génériques
     * @param genSpeed vitesse générique
     * @param genDamage dégâts génériques
     * @param player joueur ciblé
     */
    public SkeletonWeakEnemy(float x, float y, int genHealth, float genSpeed, int genDamage, Player player, Random rng) {
        super(x, y, genHealth, genSpeed, genDamage, null);
        this.targetPlayer = player;
        this.rng = new Random();
        scaleStats(1);
    }

    /**
     * Mise à jour du squelette : déplacement vers le joueur et attaque.
     */
    @Override
    public void update() {
        if (!isActive) return;
        move(targetPlayer, 1f);
        attack(targetPlayer);
    }

    /**
     * Le squelette se dirige vers le joueur avec légère variation aléatoire.
     *
     * @param player joueur ciblé
     * @param delta temps écoulé
     */
    @Override
    public void move(Player player, float delta) {
        float dx = player.getX() - x;
        float dy = player.getY() - y;
        float length = (float) Math.sqrt(dx*dx + dy*dy);
        if (length != 0) {
            dx /= length;
            dy /= length;
        }

        float variationX = (rng.nextFloat() - 0.5f) * 0.1f;
        float variationY = (rng.nextFloat() - 0.5f) * 0.1f;

        x += (dx + variationX) * speed * delta;
        y += (dy + variationY) * speed * delta;
    }

    @Override
    public void interact(Entity e) {
        if (e instanceof Player) attack((Player) e);
    }

    @Override
    public void attack(Player player) {
        float dx = player.getX() - x;
        float dy = player.getY() - y;
        float dist = (float) Math.sqrt(dx*dx + dy*dy);
        if (dist < 25f) {
            player.takeDamage(attackDamage);
        }
    }
}
