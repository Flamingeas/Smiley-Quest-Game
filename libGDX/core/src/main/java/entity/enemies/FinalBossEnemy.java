package entity.enemies;

import entity.Player;
import entity.Entity;
import java.util.Random;

/**
 * Boss du niveau 5 combinant force brute et attaque à distance.
 */
public class FinalBossEnemy extends Enemy {

    private final Player targetPlayer;
    private final Random rng;
    private float attackCooldownMelee;
    private float attackCooldownRanged;

    /**
     * Constructeur du boss avec ses statistiques génériques.
     *
     * @param x position X initiale
     * @param y position Y initiale
     * @param genHealth points de vie génériques
     * @param genSpeed vitesse générique
     * @param genDamage dégâts génériques
     * @param player joueur ciblé
     */
    public FinalBossEnemy(float x, float y, int genHealth, float genSpeed, int genDamage, Player player, Random rng) {
        super(x, y, genHealth, genSpeed, genDamage, null);
        this.targetPlayer = player;
        this.rng = new Random();
        this.attackCooldownMelee = 0f;
        this.attackCooldownRanged = 0f;
        scaleStats(1);
    }


    /**
     * Met à jour le boss : mouvements et attaques multiples.
     */
    @Override
    public void update() {
        if (!isActive) return;

        move(targetPlayer, 1f);

        if (attackCooldownMelee <= 0f) {
            meleeAttack(targetPlayer);
            attackCooldownMelee = 1.5f;
        } else {
            attackCooldownMelee -= 0.1f;
        }

        if (attackCooldownRanged <= 0f) {
            rangedAttack(targetPlayer);
            attackCooldownRanged = 3f;
        } else {
            attackCooldownRanged -= 0.1f;
        }
    }

    /**
     * Déplacement agressif : se rapproche si trop loin du joueur.
     */
    @Override
    public void move(Player player, float delta) {
        float dx = player.getX() - x;
        float dy = player.getY() - y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance > 100f) {
            x += (dx / distance) * speed * delta;
            y += (dy / distance) * speed * delta;
        } else {
            x += Math.sin(System.currentTimeMillis() * 0.001f) * 0.5f;
            y += Math.cos(System.currentTimeMillis() * 0.001f) * 0.5f;
        }
    }

    /**
     * Intéraction avec le joueur.
     */
    @Override
    public void interact(Entity e) {
        if (e instanceof Player) {
            meleeAttack((Player) e);
            rangedAttack((Player) e);
        }
    }

    /**
     * Attaque corps à corps si proche du joueur.
     */
    private void meleeAttack(Player player) {
        float dx = player.getX() - x;
        float dy = player.getY() - y;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        if (dist < 30f) {
            player.takeDamage(attackDamage);
        }
    }

    /**
     * Attaque à distance type souffle ou projectiles.
     */
    private void rangedAttack(Player player) {
        float dx = player.getX() - x;
        float dy = player.getY() - y;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        if (dist < 250f) {
            player.takeDamage(attackDamage / 2);
        }
    }
}
