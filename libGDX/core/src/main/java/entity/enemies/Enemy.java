package entity.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import entity.Entity;
import entity.Player;

/**
 * Représente un ennemi avec coordonnées TILE (spawn safe).
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

    /**
     * Constructeur Enemy : on fournit des coordonnées TILES.
     * x et y pixel seront fixés par GameScreen ensuite.
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
    public TextureRegion getIconRegion() {
        return new TextureRegion(texture, 0, 0, 32, 32);
    }
    /**
     * Appelé par GameScreen après calcul du tileSize + offsets.
     */
    public void setPixelPosition(float px, float py) {
        this.x = px;
        this.y = py;
    }

    /**
     * Ajuste les stats selon le niveau.
     */
    public void scaleStats(int level) {
        this.health = genHealth;
        this.speed = genSpeed;
        this.attackDamage = genDamage;
    }
    /**
     * Update générique.
     */
    public void update() {
        if (!isActive) return;
    }
    /**
     * Interaction (dommages au joueur).
     */
    @Override
    public void interact(Entity e) {
        if (e instanceof Player) {
            attack((Player) e);
        }
    }
    public void attack(Player player) {
        if (player != null && isActive) {
            player.takeDamage(attackDamage);
        }
    }
    public void setGameScreen(settings.screen.GameScreen gs) {
        this.gameScreen = gs;
    }
    /**
     * L'ennemi reçoit des dégâts.
     */
    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) isActive = false;
    }
    /**
     * Déplacement (spécifique aux classes filles).
     */
    public abstract void move(Player player, float delta);
    public int getHealth() { return health; }
    public float getSpeed() { return speed; }
    public int getAttackDamage() { return attackDamage; }
    public Texture getTexture() {
        return texture;
    }
}
