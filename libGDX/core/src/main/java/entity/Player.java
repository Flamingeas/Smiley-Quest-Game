package entity;

import com.badlogic.gdx.graphics.Texture;
import system.Observable;
import system.GameEvent;
import system.Observer;

import java.util.ArrayList;
import java.util.List;

public class Player extends Entity implements Observable {

    private float speed;

    private int lives;
    private int maxLives;
    private final int hpPerLife = 100;
    private int currentHP;

    private Weapon currentWeapon;
    private final List<Observer> observers = new ArrayList<>();

    private Texture texture;
    private final List<Weapon> inventory = new ArrayList<>();

    private float invulnTime = 0f;
    private final float invulnDuration = 0.8f;

    /**
     * Constructor used by GameWorld.
     */
    public Player(float x, float y, int lives, float speed, int smileyIndex) {
        super(x, y);
        this.maxLives = lives;
        this.lives = maxLives;
        this.speed = speed;
        this.currentWeapon = null;

        this.currentHP = lives * hpPerLife;
        this.texture = new Texture("images/smiley/smiley_" + smileyIndex + ".png");
    }
    public void collectItem(Item item) {
        System.out.println("TEST DEBUG - ENVOI EVENT ITEM_COLLECTED");

        notifyEvent(new GameEvent("ITEM_COLLECTED", item.getValue()));
    }
    /** Optional fallback constructor */
    public Player() {
        super(0, 0);
        this.maxLives = 3;
        this.lives = 3;
        this.speed = 0.7f;
        this.currentHP = lives * hpPerLife;
    }

    /** Update = reduce invulnerability timer */
    @Override
    public void update() {
        if (invulnTime > 0) {
            invulnTime -= com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        }
    }

    /** Interaction with items */
    @Override
    public void interact(Entity e) {
        if (e instanceof Item && e.isActive()) {
            ((Item)e).interact(this);
        }
    }

    /** Reset for restarting the level */
    public void resetLevel() {
        this.isActive = true;
        this.currentWeapon = null;
        this.inventory.clear();
    }

    /** Full reset (new game) */
    public void resetFull() {
        this.lives = maxLives;
        this.currentHP = maxLives * hpPerLife;
        this.isActive = true;
        this.currentWeapon = null;
        this.inventory.clear();

        System.out.println("Joueur réinitialisé.");
        notifyEvent(new GameEvent("PLAYER_RESET", null));
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(GameEvent event) {
        for (Observer obs : observers) {
            obs.onNotify(event);
        }
    }

    public void notifyEvent(GameEvent event) {
        notifyObservers(event);
    }

    public List<Weapon> getInventory() {
        return inventory;
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public void addWeaponToInventory(Weapon weapon) {
        inventory.add(weapon);
        System.out.println("Arme ajoutée à l'inventaire : " + weapon.getName());
    }

    public void setCurrentWeapon(Weapon currentWeapon) {
        this.currentWeapon = currentWeapon;
        if (currentWeapon != null) {
            System.out.println("Arme équipée : " + currentWeapon.getName() +
                    " (Dégâts: " + currentWeapon.getDamage() + ")");
            notifyEvent(new GameEvent("WEAPON_EQUIPPED", currentWeapon));
        }
    }


    public int getLives() { return lives; }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public float getX() { return x; }
    public float getY() { return y; }

    public Texture getTexture() { return texture; }

    /**
     * Damage + invulnerability frames + life management.
     */
    public void takeDamage(int damage) {
        if (!isActive) return;

        if (invulnTime > 0) return;

        invulnTime = invulnDuration;

        currentHP -= damage;
        int oldLives = lives;

        lives = (int) Math.ceil((float) currentHP / hpPerLife);

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
}