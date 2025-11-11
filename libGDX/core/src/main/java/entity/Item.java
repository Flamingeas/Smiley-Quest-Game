package entity;

import system.GameEvent;

public abstract class Item extends Entity {

    protected int value;
    protected String name;

    public Item(float x, float y, int value, String name) {
        super(x, y);
        this.value = value;
        this.name = name;
    }
    public abstract void collectItem(Player p);

    @Override
    public void update() {
    }

    @Override
    public void interact(Entity e) {
        if (e instanceof Player && isActive) {
            collectItem((Player) e);
            onCollectEffect();
            this.isActive = false;
        }
    }
    public void onCollectEffect() {
        System.out.println(name + " collecté !");
    }
    public GameEvent createEvent() {
        return new GameEvent("ITEM_COLLECTED", this);
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
