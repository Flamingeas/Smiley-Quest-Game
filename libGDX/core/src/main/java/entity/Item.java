package entity;

import java.util.List;

public abstract class Item extends Entity {
    private List<Item> items;

    public Item(float x, float y) {
        super(x, y);
    }

    public abstract void collectItem(Player p);

    @Override
    public void update() {
    }

    @Override
    public void interact(Entity e) {
        if (e instanceof Player && isActive) {
            collectItem((Player) e);
            this.isActive = false;
        }
    }
}
