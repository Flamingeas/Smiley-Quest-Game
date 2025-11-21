package entity.enemies;

import entity.Player;
import entity.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.Random;

public class DragonEnemy extends Enemy {

    private float attackCooldown = 0f;
    private final Random rng = new Random();

    public DragonEnemy(float tileX, float tileY, int genHealth, float genSpeed, int genDamage, Player player) {
        super(tileX, tileY, genHealth, genSpeed, genDamage, player);

        this.texture = new Texture("enemies/DragonEnemy.png");
        this.targetPlayer = player;

        scaleStats(1);
    }

    @Override
    public void update() {
        if (!isActive) return;

        float delta = Gdx.graphics.getDeltaTime();

        move(targetPlayer, delta);

        if (attackCooldown <= 0f) {
            attack(targetPlayer);
            attackCooldown = 2f;
        } else {
            attackCooldown -= delta;
        }
    }

    @Override
    public void move(Player player, float delta) {
        if (gameScreen == null || player == null) return;

        float dx = player.getX() - x;
        float dy = player.getY() - y;

        float dist = (float)Math.sqrt(dx * dx + dy * dy);
        if (dist == 0) return;

        float nx = dx / dist;
        float ny = dy / dist;

        float moveSpeed = speed * 32f * delta;
        float optimalDistance = 150f;

        float newX = x;
        float newY = y;

        if (dist > optimalDistance) {
            newX += nx * moveSpeed;
            newY += ny * moveSpeed;
        }
        else if (dist < optimalDistance * 0.7f) {
            newX -= nx * moveSpeed;
            newY -= ny * moveSpeed;
        }
        newX += (rng.nextFloat() - 0.5f) * 2f;
        newY += (rng.nextFloat() - 0.5f) * 2f;

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

        if (dist < 200f) {
            player.takeDamage(attackDamage);
        }
    }
}
