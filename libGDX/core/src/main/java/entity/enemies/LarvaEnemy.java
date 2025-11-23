package entity.enemies;

import entity.Entity;
import entity.Player;
import java.util.Random;

/**
 * Ennemi Larve : déplacement aléatoire + attaque seulement si proche,
 * avec un cooldown propre (hérité de Enemy).
 */
public class LarvaEnemy extends Enemy {

    private float directionX;
    private float directionY;
    private final Random rng;

    public LarvaEnemy(float x, float y, int genHealth, float genSpeed, int genDamage, Player player) {
        super(x, y, genHealth, genSpeed, genDamage, player);

        this.rng = new Random();
        regenerateDirection();
        scaleStats(1);
        this.attackRange = 40f;
    }

    private void regenerateDirection() {
        directionX = rng.nextFloat() * 2 - 1;
        directionY = rng.nextFloat() * 2 - 1;

        float len = (float)Math.sqrt(directionX * directionX + directionY * directionY);
        if (len != 0) {
            directionX /= len;
            directionY /= len;
        }
    }

    @Override
    public void update() {
        if (!isActive) return;

        float delta = com.badlogic.gdx.Gdx.graphics.getDeltaTime();

        super.update();

        move(targetPlayer, delta);
        attack(targetPlayer);
    }

    @Override
    public void move(Player player, float delta) {
        if (gameScreen == null) return;

        float speedStep = speed * 40f * delta;

        float newX = x + directionX * speedStep;
        float newY = y + directionY * speedStep;

        boolean moved = false;

        if (gameScreen.canMoveGeneric(newX, y)) {
            x = newX;
            moved = true;
        } else {
            directionX *= -1;
        }

        if (gameScreen.canMoveGeneric(x, newY)) {
            y = newY;
            moved = true;
        } else {
            directionY *= -1;
        }

        if (!moved) {
            regenerateDirection();
        }
    }

    @Override
    public void attack(Player player) {
        if (player == null) return;

        float dx = player.getX() - x;
        float dy = player.getY() - y;
        float dist = (float)Math.sqrt(dx * dx + dy * dy);

        if (dist < 40f) {
            super.attack(player);
        }
    }
}
