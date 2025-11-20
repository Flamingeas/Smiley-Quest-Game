package entity.enemies;

import entity.Entity;
import entity.Player;
import java.util.Random;

public class SkeletonWeakEnemy extends Enemy {

    private final Random rng;

    public SkeletonWeakEnemy(float tileX, float tileY, int genHealth, float genSpeed, int genDamage, Player player) {
        super(tileX, tileY, genHealth, genSpeed, genDamage, player);

        this.targetPlayer = player;
        this.rng = new Random();

        this.texture = new com.badlogic.gdx.graphics.Texture("enemies/SkeletonWeakEnemy.png");

        scaleStats(1);
    }

    @Override
    public void update() {
        if (!isActive) return;

        float delta = com.badlogic.gdx.Gdx.graphics.getDeltaTime();
        move(targetPlayer, delta);
        attack(targetPlayer);
    }

    @Override
    public void move(Player player, float delta) {

        if (player == null || gameScreen == null) return;

        float dx = player.getX() - x;
        float dy = player.getY() - y;

        float len = (float)Math.sqrt(dx*dx + dy*dy);
        if (len == 0) return;

        dx /= len;
        dy /= len;

        dx += (rng.nextFloat() - 0.5f) * 0.20f;
        dy += (rng.nextFloat() - 0.5f) * 0.20f;
        float len2 = (float)Math.sqrt(dx*dx + dy*dy);
        dx /= len2;
        dy /= len2;

        float moveSpeed = speed * 32f * delta;

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
            float angle = rng.nextFloat() * (float)Math.PI * 2;
            float ox = (float)Math.cos(angle) * moveSpeed * 0.6f;
            float oy = (float)Math.sin(angle) * moveSpeed * 0.6f;

            if (gameScreen.canMoveGeneric(x + ox, y)) x += ox;
            if (gameScreen.canMoveGeneric(x, y + oy)) y += oy;
        }
    }

    @Override
    public void interact(Entity e) {
        if (e instanceof Player) {
            attack((Player)e);
        }
    }

    @Override
    public void attack(Player player) {
        float dx = player.getX() - x;
        float dy = player.getY() - y;

        float dist = (float)Math.sqrt(dx*dx + dy*dy);

        if (dist < 22f) {
            player.takeDamage(attackDamage);
        }
    }
}
