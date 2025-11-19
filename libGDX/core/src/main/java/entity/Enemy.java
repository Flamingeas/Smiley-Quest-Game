package entity;

import level.Level;

/**
 * Représente un ennemi du jeu avec des stats qui évoluent selon le niveau.
 */
public class Enemy extends Entity {

    private int health;
    private float speed;
    private int attackDamage;

    private int genHealth;
    private float genSpeed;
    private int genDamage;

    private Level levelManager;

    /**
     * Crée un ennemi à une position donnée avec des stats de base.
     *
     * @param x Position X
     * @param y Position Y
     * @param genHealth Santé de base
     * @param genSpeed Vitesse de base
     * @param genDamage Dégâts de base
     * @param levelManager Référence au niveau actuel
     */
    public Enemy(float x, float y, int genHealth, float genSpeed, int genDamage, Level levelManager) {
        super(x, y);
        this.genHealth = genHealth;
        this.genSpeed = genSpeed;
        this.genDamage = genDamage;
        this.levelManager = levelManager;
        scaleStats(1);
        isActive = true;
    }

    /**
     * Ajuste les statistiques selon le niveau du jeu.
     *
     * @param level Niveau courant
     */
    public void scaleStats(int level) {
        health = (int)(genHealth * (1 + 0.2f * (level - 1)));
        speed = genSpeed * (1 + 0.1f * (level - 1));
        attackDamage = (int)(genDamage * (1 + 0.15f * (level - 1)));
    }

    /**
     * Met à jour l'état de l'ennemi à chaque frame.
     */
    public void update() {
        if (!isActive) return;
    }

    /**
     * Gère l'interaction avec une autre entité.
     *
     * @param e Entité avec laquelle interagir
     */
    @Override
    public void interact(Entity e) {
        if (e instanceof Player) {
            attack((Player) e);
        }
    }

    /**
     * Inflige des dégâts au joueur.
     *
     * @param player Joueur cible
     */
    public void attack(Player player) {
        if (player != null && isActive) {
            player.takeDamage(attackDamage);
        }
    }

    /**
     * L'ennemi reçoit des dégâts.
     *
     * @param damage Quantité de dégâts
     */
    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) isActive = false;
    }

    public int getHealth() { return health; }
    public float getSpeed() { return speed; }
    public int getAttackDamage() { return attackDamage; }
}
