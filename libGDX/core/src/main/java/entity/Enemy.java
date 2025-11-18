package entity;

import level.Level;

public abstract class Enemy extends Entity {
    protected int health;
    protected float speed;
    protected int attackDamage;

    protected int genHealth;
    protected float genSpeed;
    protected int genDamage;

    public Enemy(float x, float y, int genHealth, float genSpeed, int genDamage, Level levelManager) {
        super(x, y);
        this.genHealth = genHealth;
        this.genSpeed = genSpeed;
        this.genDamage = genDamage;
        scaleStats(levelManager.getCurrentLevel());
    }

    protected void scaleStats(int level) {
        health = (int)(genHealth * (1 + 0.2f * (level - 1)));
        speed = genSpeed * (1 + 0.1f * (level - 1));
        attackDamage = (int)(genDamage * (1 + 0.15f * (level - 1)));
    }

    public abstract void updateAI();
    public abstract void attack(Player player);

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) isActive = false;
    }

    public int getHealth() { return health; }
    public float getSpeed() { return speed; }
    public int getAttackDamage() { return attackDamage; }
}
