package system;

import java.util.ArrayList;
import java.util.List;

public class GameTimer {

    private float elapsed;
    private List<ScoringRule> rules;

    public GameTimer() {
        this.elapsed = 0f;
        this.rules = new ArrayList<>();
    }
    public void reset() {
        this.elapsed = 0f;
    }
    public void tick(float delta) {
        this.elapsed += delta;
        for (ScoringRule rule : rules) {
            rule.apply(this);
        }
    }
    public float getElapsed() {
        return elapsed;
    }
    public void addRule(ScoringRule rule) {
        rules.add(rule);
    }
    public void clearRules() {
        rules.clear();
    }
}
