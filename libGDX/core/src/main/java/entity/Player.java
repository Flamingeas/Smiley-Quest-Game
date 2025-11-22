package entity;

import com.badlogic.gdx.graphics.Texture;
import system.Observable;
import system.GameEvent;
import system.Observer;
import entity.Item;
import entity.Weapon;

import java.util.ArrayList;
import java.util.List;

/**

 * Représente le joueur du jeu.
 * * Position gérée par GameScreen
 * * Gère score, vies, arme équipée, events, damage
 * * Texture dynamique selon le smiley choisi
 */
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

    /** Constructeur principal utilisé par GameWorld */
    public Player(float x, float y, int lives, float speed, int smileyIndex) {
        super(x, y);
        this.maxLives = lives;
        this.lives = lives;
        this.speed = speed;
        this.score = 0;
        this.currentWeapon = null;
        this.currentHP = lives * hpPerLife;
        this.texture = new Texture("images/smiley/smiley_" + smileyIndex + ".png");
    }

    /** Constructeur fallback pas utilisé en jeu */
    public Player() {
        super(0, 0);
        this.maxLives = 3;
        this.lives = 3;
        this.speed = 0.7f;
        this.score = 0;
        this.currentWeapon = null;
        this.currentHP = lives * hpPerLife;
    }

    /** Update du joueur (vide ici, déplacement géré ailleurs) */
    @Override
    public void update() { }

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
        System.out.println("Score modifié de " + points + ". Total : " + score);
        notifyEvent(new GameEvent("SCORE_UPDATED", score));
    }

    /** Perdre une vie directement */
    public void loseLife() {
        takeDamage(hpPerLife);
    }

    /** Gagner une vie */
    public void gainLife() {
        if (lives < maxLives) {
            lives++;
            currentHP += hpPerLife;
            System.out.println("Vie gagnée ! Total : " + lives);
            notifyEvent(new GameEvent("LIFE_GAINED", lives));
        }
    }

    /** Reset pour restart level */
    public void reset() {
        this.score = 0;
        this.lives = maxLives;
        this.currentHP = maxLives * hpPerLife;
        this.isActive = true;
        this.currentWeapon = null;

        System.out.println("Joueur réinitialisé.");
        notifyEvent(new GameEvent("PLAYER_RESET", null));
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

    /** Weapon getter/setter */
    public Weapon getCurrentWeapon() { return currentWeapon; }

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

    /** Prendre des dégâts et gérer les cœurs */
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

    /** Debug */
    public void displayInfo() {
        System.out.println("=== INFO JOUEUR ===");
        System.out.println("Position : (" + x + ", " + y + ")");
        System.out.println("Vies : " + lives + " (" + currentHP + "/" + (maxLives*hpPerLife) + " HP)");
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
    public Texture getTexture() { return texture; }
}
