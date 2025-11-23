/**
 * Represents the player character in the game.
 * <p>
 * Handles position (managed by GameScreen), score, lives, equipped weapon,
 * game events, damage, and dynamic texture based on selected smiley.
 * </p>
 */
package entity;

import com.badlogic.gdx.graphics.Texture;
import system.Observable;
import system.GameEvent;
import system.Observer;
import entity.Item;
import entity.Weapon;

import java.util.ArrayList;
import java.util.List;

public class Player extends Entity implements Observable {

    private float speed;
    private int score;
    private int lives;
    private int maxLives;
    private int hpPerLife = 100;
    private int currentHP;

    private Weapon currentWeapon;
    private final List<Observer> observers = new ArrayList<>();

    private Texture texture;
    private List<Weapon> inventory = new ArrayList<>();

    /**
     * Main constructor used by GameWorld.
     *
     * @param x initial X position
     * @param y initial Y position
     * @param lives number of starting lives
     * @param speed player movement speed
     * @param smileyIndex index of the smiley texture
     */
    public Player(float x, float y, int lives, float speed, int smileyIndex) {
        super(x, y);
        this.score = 0;
        this.maxLives = 3;
        this.lives = maxLives;
        this.speed = speed;
        this.currentWeapon = null;
        this.currentHP = lives * hpPerLife;
        this.texture = new Texture("images/smiley/smiley_" + smileyIndex + ".png");
    }

    /**
     * Fallback constructor (not used in game).
     */
    public Player() {
        super(0, 0);
        this.lives = 3;
        this.speed = 0.7f;
        this.currentWeapon = null;
        this.currentHP = lives * hpPerLife;
    }

    /** Update method called each frame (empty for Player). */
    @Override
    public void update() { }

    /**
     * Handles interaction with other entities, e.g., collecting items.
     *
     * @param e entity to interact with
     */
    @Override
    public void interact(Entity e) {
        if (e instanceof Item && e.isActive()) {
            ((Item) e).interact(this);
        }
    }

    /**
     * Collects an item and increases score accordingly.
     *
     * @param item item to collect
     */
    public void collectItem(Item item) {
        score += item.getValue();
        System.out.println("Item collecté : +" + item.getValue() + " points !");
        notifyEvent(item.createEvent());
    }

    /** Adds score manually. */
    public void addScore(int points) {
        this.score += points;
        System.out.println("Score modifié de " + points + ". Total : " + score);
        notifyEvent(new GameEvent("SCORE_UPDATED", score));
    }

    /** Causes the player to lose one life. */
    public void loseLife() {
        takeDamage(hpPerLife);
    }

    /** Adds one life to the player if below maxLives. */
    public void gainLife() {
        if (lives < maxLives) {
            lives++;
            currentHP += hpPerLife;
            System.out.println("Vie gagnée ! Total : " + lives);
            notifyEvent(new GameEvent("LIFE_GAINED", lives));
        }
    }

    /** Resets player state for restarting a level. */
    public void resetLevel() {
        this.isActive = true;
        this.currentWeapon = null;
        this.inventory.clear();
    }

    /** Resets player completely (score, lives, inventory). */
    public void resetFull() {
        this.score = 0;
        this.lives = maxLives;
        this.currentHP = maxLives * hpPerLife;
        this.isActive = true;
        this.currentWeapon = null;
        this.inventory.clear();

        System.out.println("Joueur réinitialisé.");
        notifyEvent(new GameEvent("PLAYER_RESET", null));
    }

    /** Adds an observer to the player. */
    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    /** Removes an observer from the player. */
    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    /** Notifies all observers of a game event. */
    @Override
    public void notifyObservers(GameEvent event) {
        for (Observer obs : observers) {
            obs.onNotify(event);
        }
    }

    /** Helper to notify observers of an event. */
    public void notifyEvent(GameEvent event) {
        notifyObservers(event);
    }

    /** Returns the player's inventory. */
    public List<Weapon> getInventory() {
        return inventory;
    }

    /** Returns currently equipped weapon. */
    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    /** Adds a weapon to the inventory. */
    public void addWeaponToInventory(Weapon weapon) {
        inventory.add(weapon);
        System.out.println("Arme ajoutée à l'inventaire : " + weapon.getName());
    }

    /** Equips a weapon and notifies observers. */
    public void setCurrentWeapon(Weapon currentWeapon) {
        this.currentWeapon = currentWeapon;
        if (currentWeapon != null) {
            System.out.println("Arme équipée : " + currentWeapon.getName() +
                    " (Dégâts: " + currentWeapon.getDamage() + ")");
            notifyEvent(new GameEvent("WEAPON_EQUIPPED", currentWeapon));
        }
    }

    /** Getters and setters for HP, score, speed, and position. */
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getLives() { return lives; }
    public void setLives(int lives) { this.lives = lives; }
    public int getMaxLives() { return maxLives; }

    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public float getX() { return x; }
    public float getY() { return y; }

    /**
     * Reduces player HP and manages lives, notifies observers if hit or dead.
     *
     * @param damage damage amount
     */
    public void takeDamage(int damage) {
        if (!isActive) return;

        currentHP -= damage;
        int oldLives = lives;
        lives = (int)Math.ceil((float)currentHP / hpPerLife);

        if (lives < oldLives) {
            System.out.println("💔 Cœur perdu ! Vies restantes : " + lives);
            notifyEvent(new GameEvent("PLAYER_HIT", lives));
        }

        if (lives <= 0) {
            currentHP = 0;
            lives = 0;
            isActive = false;
            System.out.println("💀 Le joueur est mort !");
            notifyEvent(new GameEvent("PLAYER_DEAD", null));
        }
    }

    /** Prints debug information about the player. */
    public void displayInfo() {
        System.out.println("=== INFO JOUEUR ===");
        System.out.println("Position : (" + x + ", " + y + ")");
        System.out.println("Vies : " + lives + " (" + currentHP + "/" + (maxLives*hpPerLife) + " HP)");
        System.out.println("Score : " + score);
        System.out.println("Vitesse : " + speed);

        if (currentWeapon != null) {
            System.out.println("Arme : " + currentWeapon.getName() +
                    " (Dégâts: " + currentWeapon.getDamage() + ")");
        } else {
            System.out.println("Arme : Aucune");
        }

        System.out.println("==================");
    }

    /** Returns the player's texture. */
    public Texture getTexture() { return texture; }
}