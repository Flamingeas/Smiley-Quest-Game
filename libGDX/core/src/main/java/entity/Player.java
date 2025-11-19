package entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import system.Observable;
import system.GameEvent;
import system.Observer;

import java.util.ArrayList;
import java.util.List;

public class Player extends Entity implements Observable {

    private float speed;
    private int score;
    private int lives;
    private Weapon currentWeapon;
    private final List<Observer> observers = new ArrayList<>();

    public Player(float x, float y, int lives, float speed) {
        super(x, y);
        this.score = 0;
        this.lives = lives;
        this.speed = speed;
        this.currentWeapon = null;
    }

    public Player() {
        super(0, 0);
        this.lives = 3;
        this.speed = 2.0f;
        this.score = 0;
        this.currentWeapon = null;
    }

    @Override
    public void update() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) x -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) x += speed;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) y += speed;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) y -= speed;

        if (Gdx.input.isKeyPressed(Input.Keys.Q)) x -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) x += speed;
        if (Gdx.input.isKeyPressed(Input.Keys.Z)) y += speed;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) y -= speed;
    }

    @Override
    public void interact(Entity e) {
        if (e instanceof Item && e.isActive()) {
            ((Item) e).interact(this);
        }
    }

    public void collectItem(Item item) {
        score += item.getValue();
        System.out.println("Item collecté : +" + item.getValue() + " points !");

        GameEvent event = item.createEvent();
        notifyEvent(event);
    }

    public void addScore(int points) {
        this.score += points;
        System.out.println("Score augmenté de " + points + ". Total : " + score);
        notifyEvent(new GameEvent("SCORE_UPDATED", score));
    }

    public void loseLife() {
        lives--;
        System.out.println("Vie perdue ! Vies restantes : " + lives);
        notifyEvent(new GameEvent("PLAYER_HIT", lives));
        if (lives <= 0) {
            isActive = false;
            notifyEvent(new GameEvent("PLAYER_DEAD", null));
        }
    }

    public void gainLife() {
        lives++;
        System.out.println("Vie gagnée ! Total : " + lives);
        notifyEvent(new GameEvent("LIFE_GAINED", lives));
    }

    public void reset() {
        this.score = 0;
        this.lives = 3;
        this.isActive = true;
        this.currentWeapon = null;
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

    public Weapon getCurrentWeapon() {
        return currentWeapon;
    }

    public void setCurrentWeapon(Weapon currentWeapon) {
        this.currentWeapon = currentWeapon;
        if (currentWeapon != null) {
            System.out.println("Arme équipée : " + currentWeapon.getName() +
                    " (Dégâts: " + currentWeapon.getDamage() + ")");
            notifyEvent(new GameEvent("WEAPON_EQUIPPED", currentWeapon));
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void takeDamage(int damage) {
        System.out.println("Le joueur subit " + damage + " points de dégâts !");
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

    /**
     * Affiche les informations du joueur (pour debug)
     */
    public void displayInfo() {
        System.out.println("=== INFO JOUEUR ===");
        System.out.println("Position : (" + x + ", " + y + ")");
        System.out.println("Vies : " + lives);
        System.out.println("Score : " + score);
        System.out.println("Vitesse : " + speed);
        if (currentWeapon != null) {
            System.out.println("Arme équipée : " + currentWeapon.getName() +
                    " (Dégâts: " + currentWeapon.getDamage() + ")");
        } else {
            System.out.println("Arme équipée : Aucune");
        }
        System.out.println("==================");
    }
}