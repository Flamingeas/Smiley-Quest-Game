package entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Weapon extends Item {

    private int damage;
    private boolean active;
    private TextureRegion icon;

    public Weapon(float x, float y, int damage, String name, TextureRegion iconRegion) {
        super(x, y, 100, name);
        this.damage = damage;
        this.active = true;
        this.icon = iconRegion;
    }

    @Override
    public void collectItem(Player player) {
        player.addWeaponToInventory(this);
        player.setCurrentWeapon(this);
        this.active = false;
    }

    public int getDamage() { return damage; }
    public TextureRegion getTexture() { return icon; }
    public boolean isActive() { return active; }
}
