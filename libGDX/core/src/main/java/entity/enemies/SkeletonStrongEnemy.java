package entity.enemies;

import entity.Entity;
import entity.Player;
import java.util.Random;

public class SkeletonStrongEnemy extends Enemy {

    private final Random rng = new Random();

    public SkeletonStrongEnemy(float tileX, float tileY, int genHealth, float genSpeed, int genDamage, Player player) {
        super(tileX, tileY, genHealth, genSpeed, genDamage, player);

        this.targetPlayer = player;
        this.texture = new com.badlogic.gdx.graphics.Texture("enemies/SkeletonStrongEnemy.png");

        this.attackRange = 32f;
        scaleStats(1);
    }

    @Override
    public void update() {
        if (!isActive) return;

        float delta = com.badlogic.gdx.Gdx.graphics.getDeltaTime();

        move(targetPlayer, delta);
        attemptAttack(targetPlayer);
    }

    @Override
    public void move(Player player, float delta) {

        if (player == null || gameScreen == null) return;

        float dx = player.getX() - x;
        float dy = player.getY() - y;

        float len = (float) Math.sqrt(dx * dx + dy * dy);
        if (len == 0) return;

        dx /= len;
        dy /= len;

        dx += (rng.nextFloat() - 0.5f) * 0.15f;
        dy += (rng.nextFloat() - 0.5f) * 0.15f;

        float moveSpeed = speed * 40f * delta;

        float newX = x + dx * moveSpeed;
        float newY = y + dy * moveSpeed;

        boolean moved = false;

        if (gameScreen.canMoveGeneric(newX, y)) {
            x = newX;
            moved = true;
        }

        if (gameScreen.canMoveGeneric(x, newY)) {
            y = newY;
            moved = true;
        }

        if (!moved) {
            float angle = rng.nextFloat() * (float) Math.PI * 2;
            float ox = (float) Math.cos(angle) * moveSpeed * 0.8f;
            float oy = (float) Math.sin(angle) * moveSpeed * 0.8f;

            if (gameScreen.canMoveGeneric(x + ox, y)) x += ox;
            if (gameScreen.canMoveGeneric(x, y + oy)) y += oy;
        }
    }

    @Override
    public void interact(Entity e) {
        if (e instanceof Player player) {
            attemptAttack(player);
        }
    }

    private void attemptAttack(Player player) {
        if (player == null) return;

        float dx = player.getX() - x;
        float dy = player.getY() - y;

        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        if (dist <= attackRange) {
            super.attack(player);
        }
    }

    @Override
    public void attack(Player player) {
        attemptAttack(player);
    }
}
