package entity;

public class Enemy extends Entity {
    private int health;
    private float speed;
    private boolean isAggressive;

    public Enemy(float x, float y, int health, float speed, boolean isAggressive) {
        super(x, y);
        this.health = health;
        this.speed = speed;
        this.isAggressive = isAggressive;
    }
    @Override
    public void update() {
        if (isActive) {
            x += speed * 0.1f;
        }
    }

    @Override
    public void interact(Entity e) {
        if (e instanceof Player) {
            System.out.println("L'ennemi attaque le joueur !");
            attack((Player) e);
        }
    }
    public void attack(Player player) {
        System.out.println("L'ennemi inflige des dégâts !");
        player.takeDamage(10);
    }

    public void takeDamage(int damage) {
        health -= damage;
        System.out.println("Ennemi touché ! PV restants : " + health);
        if (health <= 0) {
            isActive = false;
            System.out.println("Ennemi vaincu !");
        }
    }
    public int getHealth() {
        return health;
    }
    public void setHealth(int health) {
        this.health = health;
    }
    public float getSpeed() {
        return speed;
    }
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    public boolean isAggressive() {
        return isAggressive;
    }
    public void setAggressive(boolean aggressive) {
        isAggressive = aggressive;
    }
}