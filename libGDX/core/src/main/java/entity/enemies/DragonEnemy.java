package entity.enemies;

import entity.Player;
import entity.Entity;
import java.util.Random;

/**
 * Ennemi de type dragon caractérisé par des attaques à distance et un déplacement stratégique.
 */
public class DragonEnemy extends Enemy {

    private final Player targetPlayer;
    private final Random rng;
    private float attackCooldown;

    /**
     * Constructeur du dragon permettant l'utilisation des statistiques génériques.
     *
     * @param x position X initiale
     * @param y position Y initiale
     * @param genHealth points de vie génériques
     * @param genSpeed vitesse générique
     * @param genDamage dégâts génériques
     * @param player joueur ciblé
     */
    public DragonEnemy(float x, float y, int genHealth, float genSpeed, int genDamage, Player player) {
        super(x, y, genHealth, genSpeed, genDamage, null);
        this.targetPlayer = player;
        this.rng = new Random();
        this.attackCooldown = 0f;
        scaleStats(1);
    }

    /**
     * Met à jour le dragon en appliquant son déplacement stratégique et son attaque à distance.
     */
    @Override
    public void update() {
        if (!isActive) return;
        move(targetPlayer, 1f);
        if (attackCooldown <= 0f) {
            attack(targetPlayer);
            attackCooldown = 2f;
        } else {
            attackCooldown -= 0.1f;
        }
    }

    /**
     * Déplacement stratégique : le dragon attaque à une distance optimale.
     *
     * @param player joueur ciblé
     * @param delta temps écoulé
     */
    @Override
    public void move(Player player, float delta) {
        float dx = player.getX() - x;
        float dy = player.getY() - y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        float optimalDistance = 150f;
        if (distance > optimalDistance) {
            x += (dx / distance) * speed * delta;
            y += (dy / distance) * speed * delta;
        } else if (distance < optimalDistance * 0.8f) {
            x -= (dx / distance) * speed * delta;
            y -= (dy / distance) * speed * delta;
        }
    }

    /**
     * Intéraction du dragon avec une autre entité.
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
     * Attaque du dragon : inflige des dégâts à distance si le joueur est dans sa zone.
     *
     * @param player joueur touché
     */
    @Override
    public void attack(Player player) {
        float dx = player.getX() - x;
        float dy = player.getY() - y;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        if (dist < 200f) {
            player.takeDamage(attackDamage);
        }
    }
}
