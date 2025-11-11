package system;

import java.util.ArrayList;
import java.util.List;

public class ScoreSystem {
    private int currentScore;
    private List<ScoringRule> rules;

    public ScoreSystem() {
        this.currentScore = 0;
        this.rules = new ArrayList<>();
    }

    public void addRule(ScoringRule rule) {
        rules.add(rule);
    }

    public void onEvent(GameEvent e) {
        for (ScoringRule rule : rules) {
            if (rule.isApplicable(e)) {
                int points = rule.calculatePoints(e);
                addScore(points);
            }
        }
    }
    public void addScore(int points) {
        currentScore += points;
        System.out.println("Score +" + points + " (total : " + currentScore + ")");
    }

    public int getScore() {
        return currentScore;
    }

    public void reset() {
        currentScore = 0;
    }
}
