/**
 * Represents a weapon item that can be collected and equipped by the player.
 * <p>
 * Weapons have damage, an active state, and an icon texture.
 * Collecting a weapon adds it to the player's inventory and equips it.
 * </p>
 */
package entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Weapon extends Item {

    private int damage;
    private boolean active;
    private TextureRegion icon;

    /**
     * Constructs a Weapon with position, damage, name, and icon.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param damage damage value of the weapon
     * @param name name of the weapon
     * @param iconRegion texture region representing the weapon icon
     */
    public Weapon(float x, float y, int damage, String name, TextureRegion iconRegion) {
        super(x, y, 100, name);
        this.damage = damage;
        this.active = true;
        this.icon = iconRegion;
    }

    /**
     * Collects the weapon, adding it to the player's inventory and equipping it.
     *
     * @param player the player collecting the weapon
     */
    @Override
    public void collectItem(Player player) {
        player.addWeaponToInventory(this);
        player.setCurrentWeapon(this);
        this.active = false;
    }

    /** Returns the damage of the weapon. */
    public int getDamage() { return damage; }

    /** Returns the icon texture of the weapon. */
    public TextureRegion getTexture() { return icon; }

    /** Returns whether the weapon is active. */
    public boolean isActive() { return active; }
}