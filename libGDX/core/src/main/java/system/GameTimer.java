package system;

import java.util.ArrayList;
import java.util.List;

/**
 * Tracks the elapsed time in the game and manages scoring rules.
 *
 * The timer can be updated each frame, reset, and apply scoring rules automatically.
 */
public class GameTimer {

    /** Time elapsed since the timer started, in seconds. */
    private float elapsed;

    /** List of scoring rules that are applied each tick. */
    private List<ScoringRule> rules;

    /**
     * Creates a new game timer with zero elapsed time and an empty set of rules.
     */
    public GameTimer() {
        this.elapsed = 0f;
        this.rules = new ArrayList<>();
    }

    /**
     * Resets the elapsed time to zero.
     */
    public void reset() {
        this.elapsed = 0f;
    }

    /**
     * Advances the timer by a given delta time and applies all registered scoring rules.
     *
     * @param delta the time in seconds since the last update
     */
    public void tick(float delta) {
        this.elapsed += delta;
        for (ScoringRule rule : rules) {
            rule.apply(this);
        }
    }

    /**
     * Returns the current elapsed time in seconds.
     *
     * @return the elapsed time
     */
    public float getElapsed() {
        return elapsed;
    }

    /**
     * Adds a new scoring rule to be applied every tick.
     *
     * @param rule the scoring rule to add
     */
    public void addRule(ScoringRule rule) {
        rules.add(rule);
    }

    /**
     * Removes all scoring rules from the timer.
     */
    public void clearRules() {
        rules.clear();
    }
}
