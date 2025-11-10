package entity;

import utils.Coord;

public abstract class Entity {
    protected float x;
    protected float y;
    protected boolean isActive;

    public Entity(float x, float y) {
        this.x = x;
        this.y = y;
        this.isActive = true;
    }
    public abstract void update();
    public abstract void interact(Entity e);

    public Coord getCoord() {
        return new Coord(x, y);
    }
    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }
    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }
    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean active) {
        isActive = active;
    }
}
