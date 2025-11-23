package entity.enemies;

import entity.Entity;
import entity.Player;
import java.util.Random;

public class GhostEnemy extends Enemy {

    private final Player targetPlayer;
    private final Random rng;
    private float phase;

    public GhostEnemy(float x, float y, int genHealth, float genSpeed, int genDamage, Player player) {
        super(x, y, genHealth, genSpeed, genDamage, player);
        this.targetPlayer = player;
        this.rng = new Random();
        this.phase = rng.nextFloat() * 3.14f;
        scaleStats(1);

        this.attackRange = 40f;
    }

    @Override
    public void update() {
        if (!isActive) return;

        super.update();

        float delta = com.badlogic.gdx.Gdx.graphics.getDeltaTime();

        move(targetPlayer, delta);

        super.attack(targetPlayer);
    }

    @Override
    public void move(Player player, float delta) {

        float dx = player.getX() - x;
        float dy = player.getY() - y;

        float dist = (float)Math.sqrt(dx*dx + dy*dy);
        if (dist != 0) {
            dx /= dist;
            dy /= dist;
        }

        float oscX = (float)Math.sin(phase) * 0.4f;
        float oscY = (float)Math.cos(phase) * 0.4f;
        phase += 0.04f;

        float finalSpeed = speed * delta * 28f;

        x += (dx + oscX) * finalSpeed;
        y += (dy + oscY) * finalSpeed;
    }
}
