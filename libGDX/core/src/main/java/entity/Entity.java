/**
 * Abstract base class representing a generic entity in the game.
 * <p>
 * Each entity has X and Y coordinates and an active status.
 * Provides methods to update the entity, interact with other entities,
 * and access or modify its position and activity state.
 * </p>
 */
package entity;

import utils.Coord;

public abstract class Entity {

    protected float x;
    protected float y;
    protected boolean isActive;

    /**
     * Constructs an Entity with specified coordinates.
     * The entity is set to active by default.
     *
     * @param x initial X coordinate
     * @param y initial Y coordinate
     */
    public Entity(float x, float y) {
        this.x = x;
        this.y = y;
        this.isActive = true;
    }

    /**
     * Updates the entity's state each frame.
     */
    public abstract void update();

    /**
     * Defines interaction behavior with another entity.
     *
     * @param e the entity to interact with
     */
    public abstract void interact(Entity e);

    /**
     * Returns the current coordinates of the entity.
     *
     * @return Coord object representing the entity's position
     */
    public Coord getCoord() {
        return new Coord(x, y);
    }

    /**
     * Returns the X coordinate.
     *
     * @return X coordinate
     */
    public float getX() {
        return x;
    }

    /**
     * Sets the X coordinate.
     *
     * @param x new X coordinate
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Returns the Y coordinate.
     *
     * @return Y coordinate
     */
    public float getY() {
        return y;
    }

    /**
     * Sets the Y coordinate.
     *
     * @param y new Y coordinate
     */
    public void setY(float y) {
        this.y = y;
    }

    /**
     * Returns whether the entity is active.
     *
     * @return true if active, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Sets the active state of the entity.
     *
     * @param active true to set active, false to deactivate
     */
    public void setActive(boolean active) {
        isActive = active;
    }
}