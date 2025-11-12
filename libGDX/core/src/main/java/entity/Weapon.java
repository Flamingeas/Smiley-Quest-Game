package entity;

public class Weapon extends Item {
    private int damage;

    public Weapon(float x, float y, int damage) {
        super(x, y, 100, "Weapon");
        this.damage = damage;
    }

    @Override
    public void collectItem(Player player) {
        collect(player);
    }

    public void collect(Player player) {
        System.out.println("Le joueur a ramassé une arme (" + damage + " dégâts).");
        equip(player);
        isActive = false;
    }

    public void equip(Player player) {
        System.out.println("Le joueur équipe l’arme.");
        player.setCurrentWeapon(this);
        player.addScore(100);
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
