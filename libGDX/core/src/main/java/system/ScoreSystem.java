package system;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the game's scoring system.
 * The ScoreSystem tracks the current score and applies scoring rules
 * whenever a game event occurs.
 */
public class ScoreSystem {

    /** Current score of the game. */
    private int currentScore;

    /** List of scoring rules that determine how points are awarded. */
    private List<ScoringRule> rules;

    /**
     * Creates a new ScoreSystem with zero initial score.
     */
    public ScoreSystem() {
        this.currentScore = 0;
        this.rules = new ArrayList<>();
    }

    /**
     * Adds a scoring rule to the system.
     *
     * @param rule the scoring rule to add
     */
    public void addRule(ScoringRule rule) {
        rules.add(rule);
    }

    /**
     * Processes a game event and applies any applicable scoring rules.
     *
     * @param e the game event to handle
     */
    public void onEvent(GameEvent e) {
        for (ScoringRule rule : rules) {
            if (rule.isApplicable(e)) {
                int points = rule.calculatePoints(e);
                addScore(points);
            }
        }
    }

    /**
     * Adds points to the current score.
     *
     * @param points the number of points to add
     * @return the updated score
     */
    public int addScore(int points) {
        currentScore += points;
        System.out.println("Score +" + points + " (total : " + currentScore + ")");
        return currentScore;
    }

    /**
     * Returns the current score.
     *
     * @return the current score
     */
    public int getScore() {
        return currentScore;
    }

    /**
     * Returns the score after adding the current score to itself.
     * (Note: This may be a redundant method and could reset the score unexpectedly.)
     *
     * @return the updated score
     */
    public int getAddScore() {
        return addScore(currentScore);
    }

    /**
     * Resets the score to zero.
     */
    public void reset() {
        currentScore = 0;
    }
}
