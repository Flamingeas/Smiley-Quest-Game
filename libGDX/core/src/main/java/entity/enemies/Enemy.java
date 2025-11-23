package entity.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import entity.Entity;
import entity.Player;

public abstract class Enemy extends Entity {

    public float tileX;
    public float tileY;
    protected settings.screen.GameScreen gameScreen;

    public int health;
    protected float speed;
    protected int attackDamage;

    protected int genHealth;
    protected float genSpeed;
    protected int genDamage;

    public float knockbackX = 0;
    public float knockbackY = 0;
    private float knockbackTimer = 0f;

    protected Player targetPlayer;
    public Texture texture;

    protected float attackCooldown = 0f;
    protected float attackCooldownMax = 0.8f;
    protected float attackRange = 40f;

    protected float aggroRange = 150f;

    protected float attackTimer = 0f;

    public Enemy(float tileX, float tileY, int genHealth, float genSpeed, int genDamage, Player player) {
        super(0, 0);
        this.tileX = tileX;
        this.tileY = tileY;

        this.genHealth = genHealth;
        this.genSpeed = genSpeed;
        this.genDamage = genDamage;

        scaleStats(1);

        isActive = true;
        this.targetPlayer = player;
    }

    public TextureRegion getIconRegion() {
        return new TextureRegion(texture, 0, 0, 32, 32);
    }

    public void setPixelPosition(float px, float py) {
        this.x = px;
        this.y = py;
    }

    public void scaleStats(int level) {
        this.health = genHealth;
        this.speed = genSpeed;
        this.attackDamage = genDamage;
    }

    @Override
    public void update() {
        if (!isActive) return;

        float delta = com.badlogic.gdx.Gdx.graphics.getDeltaTime();

        if (knockbackTimer > 0) {
            knockbackTimer -= delta;

            x += knockbackX;
            y += knockbackY;

            knockbackX *= 0.9f;
            knockbackY *= 0.9f;

            return;
        }

        if (attackCooldown > 0)
            attackCooldown -= delta;

        if (attackTimer > 0)
            attackTimer -= delta;

        if (targetPlayer == null) return;

        move(targetPlayer, delta);

        if (isPlayerClose(targetPlayer, attackRange) && attackCooldown <= 0) {
            targetPlayer.takeDamage(attackDamage);
            attackCooldown = attackCooldownMax;
        }
    }

    @Override
    public void interact(Entity e) {}

    public void attack(Player player) {
        if (player == null || !isActive) return;
        if (attackCooldown > 0) return;

        if (!isPlayerClose(player, attackRange))
            return;

        System.out.println(getClass().getSimpleName() + " >>> attaque !");
        player.takeDamage(attackDamage);
        attackCooldown = attackCooldownMax;
    }

    public boolean isPlayerClose(Player player, float range) {
        float dx = player.getX() - x;
        float dy = player.getY() - y;
        return dx*dx + dy*dy < range * range;
    }

    public void setGameScreen(settings.screen.GameScreen gs) {
        this.gameScreen = gs;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) isActive = false;
    }

    public abstract void move(Player player, float delta);

    public int getHealth() { return health; }
    public float getSpeed() { return speed; }
    public int getAttackDamage() { return attackDamage; }
    public Texture getTexture() { return texture; }

    public void applyKnockback(float forceX, float forceY) {
        knockbackX = forceX;
        knockbackY = forceY;
        knockbackTimer = 0.12f;
    }
}