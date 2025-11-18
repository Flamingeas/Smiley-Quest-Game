package entity;

public class Enemy extends Entity {
    private int health;
    private float speed;
    private boolean isAggressive;
    private float attackRange;
    private Player target;

    public Enemy(float x, float y, int health, float speed, boolean isAggressive, float attackRange,  Player target) {
        super(x, y);
        this.health = health;
        this.speed = speed;
        this.isAggressive = isAggressive;
        this.attackRange = attackRange;
        this.target = target;
    }
    @Override
    public void update() {
        if (isActive) {
            if (isAggressive && target != null) {
                float dx = target.getX() - x;
                float dy = target.getY() - y;
                float length = (float) Math.sqrt(dx*dx + dy*dy);
                if (length > 0) {
                    x += speed * dx / length * 0.1f;
                    y += speed * dy / length * 0.1f;
                }
            } else {
                x += speed * 0.1f;
            }
        }
    }

    @Override
    public void interact(Entity e) {
        if (e instanceof Player player) {
            float distance = (float) Math.sqrt(Math.pow(player.getX()-x, 2) + Math.pow(player.getY()-y, 2));
            if (distance <= attackRange) {
                attack(player);
            }
        }
    }
    public void attack(Player player) {
        System.out.println("The enemy is attacking !");
        player.takeDamage(10);
    }

    public void takeDamage(int damage) {
        health -= damage;
        System.out.println("Enemy hit ! Remaining PV : " + health);
        if (health <= 0) {
            isActive = false;
            System.out.println("The enemy is dead !");
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
