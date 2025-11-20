package entity.enemies;

import entity.Player;
import entity.Entity;
import java.util.Random;

/**
 * Ennemi de type squelette (fort) caractérisé par une attaque puissante et un déplacement rapide.
 */
public class SkeletonStrongEnemy extends Enemy {

    private final Player targetPlayer;
    private final Random rng;

    /**
     * Constructeur du squelette fort avec statistiques amplifiées.
     *
     * @param x position X initiale
     * @param y position Y initiale
     * @param genHealth points de vie de base
     * @param genSpeed vitesse de base
     * @param genDamage dégâts de base
     * @param player joueur ciblé
     */
    public SkeletonStrongEnemy(float x, float y, int genHealth, float genSpeed, int genDamage, Player player) {
        super(x, y, genHealth, genSpeed, genDamage, null);
        this.targetPlayer = player;
        this.rng = new Random();
        scaleStats(1);
    }

    /**
     * Mise à jour de l’ennemi : déplacement et attaque.
     */
    @Override
    public void update() {
        if (!isActive) return;
        move(targetPlayer, 1f);
        attack(targetPlayer);
    }

    /**
     * Déplacement vers le joueur, variation aléatoire pour un comportement moins prévisible, un peu fou.
     *
     * @param player joueur ciblé
     * @param delta temps écoulé
     */
    @Override
    public void move(Player player, float delta) {
        float dx = player.getX() - x;
        float dy = player.getY() - y;
        float length = (float) Math.sqrt(dx * dx + dy * dy);
        if (length != 0) {
            dx /= length;
            dy /= length;
        }

        float randomVariation = 0.2f * (rng.nextFloat() - 0.5f);
        float finalSpeed = speed * (1 + randomVariation) * delta;

        x += dx * finalSpeed;
        y += dy * finalSpeed;
    }

    /**
     * Interaction avec une entité.
     *
     * @param e entité rencontrée
     */
    @Override
    public void interact(Entity e) {
        if (e instanceof Player) {
            attack((Player) e);
        }
    }

    /**
     * Attaque puissante lorsque le joueur est proche.
     *
     * @param player joueur touché
     */
    @Override
    public void attack(Player player) {
        float dx = player.getX() - x;
        float dy = player.getY() - y;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        if (dist < 25f) {
            player.takeDamage(attackDamage);
        }
    }
}
