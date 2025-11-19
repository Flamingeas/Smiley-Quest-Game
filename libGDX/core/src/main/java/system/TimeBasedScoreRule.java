package system;

public class TimeBasedScoreRule implements ScoringRule {

    private float interval;
    private int points;
    private float lastTriggerTime;
    private ScoreSystem scoreSystem;

    public TimeBasedScoreRule(float interval, int points, ScoreSystem scoreSystem) {
        this.interval = interval;
        this.points = points;
        this.scoreSystem = scoreSystem;
        this.lastTriggerTime = 0f;
    }

    @Override
    public void apply(GameTimer timer) {
        float elapsed = timer.getElapsed();

        if (elapsed - lastTriggerTime >= interval) {
            scoreSystem.addScore(points);
            lastTriggerTime = elapsed;
            System.out.println("[Rule] +" + points + " points (temps écoulé : " + elapsed + "s)");
        }
    }
}
