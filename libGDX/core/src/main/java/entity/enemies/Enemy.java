/**
 * Represents a generic enemy entity spawned using tile-based coordinates.
 * Subclasses define specific movement and attack behaviors.
 */
package entity.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import entity.Entity;
import entity.Player;

/**
 * Base class for all enemy types. It stores generated stats, tile-based spawn
 * position, references to the target player and the GameScreen, and provides
 * shared logic such as receiving damage, interacting, or scaling stats.
 */
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

    protected Player targetPlayer;
    public Texture texture;
    protected float attackCooldown = 0.8f;
    protected float attackTimer = 0f;
    protected float attackRange = 40f;
    /**
     * Creates a new Enemy instance using tile coordinates (safe spawn position).
     * Pixel coordinates are set later by the GameScreen.
     *
     * @param tileX     X position in tile coordinates
     * @param tileY     Y position in tile coordinates
     * @param genHealth generated base health
     * @param genSpeed  generated base movement speed
     * @param genDamage generated base attack damage
     * @param player    player reference used as target
     */
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

    /**
     * Returns a 32x32 texture region used as an icon for UI elements.
     *
     * @return texture region containing the enemy icon
     */
    public TextureRegion getIconRegion() {
        return new TextureRegion(texture, 0, 0, 32, 32);
    }

    /**
     * Sets the enemy's pixel position, computed externally by the GameScreen
     * after tile size and offsets are applied.
     *
     * @param px pixel X coordinate
     * @param py pixel Y coordinate
     */
    public void setPixelPosition(float px, float py) {
        this.x = px;
        this.y = py;
    }

    /**
     * Scales the enemy's stats according to the level. Currently applies the
     * generated values directly.
     *
     * @param level scale factor or level number
     */
    public void scaleStats(int level) {
        this.health = genHealth;
        this.speed = genSpeed;
        this.attackDamage = genDamage;
    }

    /**
     * Default update behavior for enemies. Subclasses normally extend or
     * override this method.
     */
    @Override
    public void update() {
        if (!isActive) return;

        if (attackTimer > 0) {
            attackTimer -= com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        }
    }

    /**
     * Interacts with another entity. If the entity is a player, deals damage.
     *
     * @param e the entity interacted with
     */
    @Override
    public void interact(Entity e) {
    }

    /**
     * Applies damage to the player if both the enemy and player are active.
     *
     * @param player the player to attack
     */
    public void attack(Player player) {
        if (player == null || !isActive) return;
        if (attackTimer > 0) return;

        if (!isPlayerClose(player, attackRange)) {
            return;
        }

        System.out.println(getClass().getSimpleName() + " >>> attaque !");
        player.takeDamage(attackDamage);
        attackTimer = attackCooldown;
    }
    public boolean isPlayerClose(Player player, float range) {
        float dx = player.getX() - x;
        float dy = player.getY() - y;
        return (dx*dx + dy*dy) < (range * range);
    }

    /**
     * Assigns the GameScreen to the enemy, allowing movement validation.
     *
     * @param gs the GameScreen instance
     */
    public void setGameScreen(settings.screen.GameScreen gs) {
        this.gameScreen = gs;
    }

    /**
     * Applies incoming damage to the enemy. If health reaches zero, the
     * enemy becomes inactive.
     *
     * @param damage amount of damage taken
     */
    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) isActive = false;
    }

    /**
     * Abstract method defining movement behavior. Implemented by subclasses.
     *
     * @param player player used as reference for movement
     * @param delta  frame delta time
     */
    public abstract void move(Player player, float delta);

    public int getHealth() { return health; }
    public float getSpeed() { return speed; }
    public int getAttackDamage() { return attackDamage; }

    /**
     * Returns the enemy's current texture.
     *
     * @return texture of the enemy
     */
    public Texture getTexture() {
        return texture;
    }
}
