package entity.enemies;

import entity.Entity;
import entity.Player;
import java.util.Random;

/**
 * Ennemi de type fantôme caractérisé par un déplacement spectral oscillant
 * et une poursuite constante du joueur.
 */
public class GhostEnemy extends Enemy {

    private final Player targetPlayer;
    private final Random rng;
    private float phase;
    /**
     * Constructeur du fantôme permettant l'utilisation des statistiques génériques.
     *
     * @param x position X initiale
     * @param y position Y initiale
     * @param genHealth points de vie génériques
     * @param genSpeed vitesse générique
     * @param genDamage dégâts génériques
     * @param player joueur suivi par le fantôme
     */
    public GhostEnemy(float x, float y, int genHealth, float genSpeed, int genDamage, Player player) {
        super(x, y, genHealth, genSpeed, genDamage, null);
        this.targetPlayer = player;
        this.rng = new Random();
        this.phase = rng.nextFloat() * 3.14f;
        scaleStats(1);
    }

    /**
     * Met à jour le fantôme en appliquant son mouvement spectral et son attaque.
     */
    @Override
    public void update() {
        if (!isActive) return;
        move(targetPlayer, 0.7f);
        attack(targetPlayer);
    }

    /**
     * Mouvement spectral du fantôme avec oscillation et variation légère de vitesse.
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

        float oscX = (float) Math.sin(phase) * 0.3f;
        float oscY = (float) Math.cos(phase) * 0.3f;
        phase += 0.05f;

        float speedVariation = 0.9f + rng.nextFloat() * 0.2f;
        float finalSpeed = speed * speedVariation * delta;

        x += (dx + oscX) * finalSpeed;
        y += (dy + oscY) * finalSpeed;
    }

    /**
     * Intéraction du fantôme avec une autre entité.
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
     * Attaque du fantôme déclenchée lorsqu'il est à coté du joueur.
     *
     * @param player joueur touché
     */
    @Override
    public void attack(Player player) {
        float dx = player.getX() - x;
        float dy = player.getY() - y;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        if (dist < 20f) {
            player.takeDamage(attackDamage);
        }
    }
}
