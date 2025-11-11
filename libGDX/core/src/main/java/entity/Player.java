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
    private List<Observer> o = new ArrayList<>();


    public Player(float x, float y, int lives, float speed) {
        super(x,y);
        this.score = 0;
        this.lives = 3;
        this.speed = 2.0f;
    }

    @Override
    public void update() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) x -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) x += speed;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) y += speed;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) y += speed;
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
    public void loseLife() {
        lives--;
        System.out.println("Vie perdue ! Vies restantes : " + lives);
        if (lives <= 0) {
            isActive = false;
        }
    }
    public void gainLife() {
        lives++;
        System.out.println("Vie gagnée ! Total : " + lives);
    }
    public void reset() {
        this.score = 0;
        this.lives = 3;
        this.isActive = true;
        System.out.println("Joueur réinitialisé.");
    }

    public void setCurrentWeapon(Weapon weapon) {
        this.currentWeapon = weapon;
    }

    public Weapon getCurrentWeapon() {
        return currentWeapon;
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

    public void addScore(int points) {
        this.score += points;
        System.out.println("Score augmenté de " + points + ". Total : " + score);
    }

    @Override
    public void addObserver(Observer o) {

    }

    @Override
    public void removeObserver(Observer o) {

    }

    @Override
    public void notifyObservers(GameEvent event) {

    }
}
