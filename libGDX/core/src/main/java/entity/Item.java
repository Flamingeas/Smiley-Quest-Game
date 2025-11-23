/**
 * Abstract base class representing a collectible item in the game.
 * <p>
 * Items have a value, a name, and a position (inherited from Entity).
 * They can be collected by the player and trigger events or effects upon collection.
 * </p>
 */
package entity;

import system.GameEvent;

public abstract class Item extends Entity {

    protected int value;
    protected String name;

    /**
     * Constructs an Item with specified coordinates, value, and name.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param value numerical value of the item
     * @param name name of the item
     */
    public Item(float x, float y, int value, String name) {
        super(x, y);
        this.value = value;
        this.name = name;
    }

    /**
     * Defines the behavior when the item is collected by a player.
     *
     * @param p player collecting the item
     */
    public abstract void collectItem(Player p);

    /**
     * Update method called each frame (no default behavior for items).
     */
    @Override
    public void update() {
    }

    /**
     * Handles interaction with another entity. Collects the item if the entity is a player.
     *
     * @param e entity interacting with the item
     */
    @Override
    public void interact(Entity e) {
        if (e instanceof Player && isActive) {
            collectItem((Player) e);
            onCollectEffect();
            this.isActive = false;
        }
    }

    /**
     * Effect triggered when the item is collected.
     */
    public void onCollectEffect() {
        System.out.println(name + " collecté !");
    }

    /**
     * Creates a game event representing this item being collected.
     *
     * @return GameEvent object for item collection
     */
    public GameEvent createEvent() {
        return new GameEvent("ITEM_COLLECTED", this);
    }

    /**
     * Returns the value of the item.
     *
     * @return value of the item
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets the value of the item.
     *
     * @param value new value of the item
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Returns the name of the item.
     *
     * @return name of the item
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the item.
     *
     * @param name new name of the item
     */
    public void setName(String name) {
        this.name = name;
    }
}