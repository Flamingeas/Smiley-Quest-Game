package entity;

import com.badlogic.gdx.graphics.Texture;
import system.Observable;
import system.GameEvent;
import system.Observer;
import entity.Item;
import entity.Weapon;

/**
 * Représente le joueur du jeu.
 * - Position gérée par GameScreen (pas ici)
 * - Gère score, vies, arme équipée, events, damage
 * - Texture dynamique selon le smiley choisi
 */
import java.util.ArrayList;
import java.util.List;

public class Player extends Entity implements Observable {
    private float speed;
    private int score;
    private int lives;

    private Weapon currentWeapon;
    private final List<Observer> observers = new ArrayList<>();

    private Texture texture;
    private List<Weapon> inventory = new ArrayList<>();

    /** Constructeur principal utilisé par GameWorld */
    public Player(float x, float y, int lives, float speed, int smileyIndex) {
        super(x, y);
        this.score = 0;
        this.lives = lives;
        this.speed = speed;
        this.currentWeapon = null;

        this.texture = new Texture("images/smiley/smiley_" + smileyIndex + ".png");
    }
    /** Constructeur fallback pas utilisé en jeu */
    public Player() {
        super(0, 0);
        this.lives = 3;
        this.speed = 0.7f;
        this.score = 0;
        this.currentWeapon = null;
    }

    /**
     * Update du joueur.
     * GameScreen gère déjà les touches => ici on ne bouge RIEN.
     */
    @Override
    public void update() {
    }

    @Override
    public void interact(Entity e) {
        if (e instanceof Item && e.isActive()) {
            ((Item) e).interact(this);
        }
    }

    /** Ajout score via items */
    public void collectItem(Item item) {
        score += item.getValue();
        System.out.println("Item collecté : +" + item.getValue() + " points !");
        notifyEvent(item.createEvent());
    }

    /** Ajoute du score manuellement */
    public void addScore(int points) {
        this.score += points;
        System.out.println("Score augmenté de " + points + ". Total : " + score);
        notifyEvent(new GameEvent("SCORE_UPDATED", score));
    }

    /** Perdre une vie */
    public void loseLife() {
        lives--;
        System.out.println("Vie perdue ! Vies restantes : " + lives);

        notifyEvent(new GameEvent("PLAYER_HIT", lives));

        if (lives <= 0) {
            isActive = false;
            notifyEvent(new GameEvent("PLAYER_DEAD", null));
        }
    }
    /** Gagner une vie */
    public void gainLife() {
        lives++;
        System.out.println("Vie gagnée ! Total : " + lives);
        notifyEvent(new GameEvent("LIFE_GAINED", lives));
    }
    /** Reset pour restart level */
    public void resetLevel() {
        this.isActive = true;
        this.currentWeapon = null;
        this.inventory.clear();    }

    public void resetFull() {
        this.score = 0;
        this.lives = 3;
        this.isActive = true;
        this.currentWeapon = null;
        this.inventory.clear();
    }
    /** Gestion des Observers */
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
    /** Weapon getter/setter */
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

    /** HP / Score / Stats getters-setters */
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getLives() { return lives; }
    public void setLives(int lives) { this.lives = lives; }

    public float getSpeed() { return speed; }
    public void setSpeed(float speed) { this.speed = speed; }

    /** Position setters/getters */
    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }
    public float getX() { return x; }
    public float getY() { return y; }

    /** Damage reçu */
    public void takeDamage(int damage) {
        System.out.println("Le joueur subit " + damage + " dégâts !");
        lives -= damage;

        if (lives > 0) {
            System.out.println("Vies restantes : " + lives);
            notifyEvent(new GameEvent("PLAYER_HIT", lives));
        } else {
            lives = 0;
            isActive = false;
            System.out.println("💀 Le joueur est mort !");
            notifyEvent(new GameEvent("PLAYER_DEAD", null));
        }
    }

    /** Debug */
    public void displayInfo() {
        System.out.println("=== INFO JOUEUR ===");
        System.out.println("Position : (" + x + ", " + y + ")");
        System.out.println("Vies : " + lives);
        System.out.println("Score : " + score);
        System.out.println("Vitesse : " + speed);

        if (currentWeapon != null) {
            System.out.println("Arme : " + currentWeapon.getName()
                    + " (Dégâts: " + currentWeapon.getDamage() + ")");
        } else {
            System.out.println("Arme : Aucune");
        }

        System.out.println("==================");
    }

    public Texture getTexture() {
        return texture;
    }
}
