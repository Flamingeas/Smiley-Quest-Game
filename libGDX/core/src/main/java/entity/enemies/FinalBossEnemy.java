package entity.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import entity.Player;
import entity.Entity;

public class FinalBossEnemy extends Enemy {

    private float attackCooldownMelee = 0f;
    private float attackCooldownRanged = 0f;

    public FinalBossEnemy(float tileX, float tileY, int genHealth, float genSpeed, int genDamage, Player player) {
        super(tileX, tileY, genHealth, genSpeed, genDamage, player);

        this.targetPlayer = player;
        this.texture = new Texture("enemies/FinalBossEnemy.png");

        scaleStats(1);
    }

    @Override
    public void update() {
        if (!isActive) return;

        float delta = Gdx.graphics.getDeltaTime();

        move(targetPlayer, delta);

        if (attackCooldownMelee <= 0f) {
            meleeAttack(targetPlayer);
            attackCooldownMelee = 1.5f;
        } else {
            attackCooldownMelee -= delta;
        }

        if (attackCooldownRanged <= 0f) {
            rangedAttack(targetPlayer);
            attackCooldownRanged = 3f;
        } else {
            attackCooldownRanged -= delta;
        }
    }

    @Override
    public void move(Player player, float delta) {

        if (gameScreen == null) return;
        if (player == null) return;

        float dx = player.getX() - x;
        float dy = player.getY() - y;
        float dist = (float)Math.sqrt(dx*dx + dy*dy);
        if (dist == 0) return;

        dx /= dist;
        dy /= dist;

        float moveSpeed = speed * 32f * delta;

        float newX = x;
        float newY = y;

        if (dist > 130f) {
            newX += dx * moveSpeed;
            newY += dy * moveSpeed;

        } else {
            float t = Gdx.graphics.getFrameId() * 0.02f;
            newX += (float)Math.sin(t) * moveSpeed * 0.7f;
            newY += (float)Math.cos(t) * moveSpeed * 0.7f;
        }

        if (gameScreen.canMoveGeneric(newX, y)) {
            x = newX;
        }
        if (gameScreen.canMoveGeneric(x, newY)) {
            y = newY;
        }
    }

    @Override
    public void interact(Entity e) {
        if (e instanceof Player) {
            meleeAttack((Player)e);
        }
    }
    private void meleeAttack(Player player) {
        float dx = player.getX() - x;
        float dy = player.getY() - y;
        float dist = (float)Math.sqrt(dx*dx + dy*dy);

        if (dist < 30f) {
            player.takeDamage(attackDamage);
        }
    }
    private void rangedAttack(Player player) {
        float dx = player.getX() - x;
        float dy = player.getY() - y;
        float dist = (float)Math.sqrt(dx*dx + dy*dy);
        if (dist < 260f) {
            player.takeDamage(attackDamage / 2);
        }
    }
}
