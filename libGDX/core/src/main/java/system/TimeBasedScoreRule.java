package system;

/**
 * A scoring rule that awards points based on elapsed time.
 * Points are added to the ScoreSystem at regular intervals while the game timer is running.
 */
public class TimeBasedScoreRule implements ScoringRule {

    /** Interval in seconds between point awards. */
    private float interval;

    /** Number of points awarded each interval. */
    private int points;

    /** The last time the points were awarded. */
    private float lastTriggerTime;

    /** The ScoreSystem that receives the points. */
    private ScoreSystem scoreSystem;

    /**
     * Creates a new TimeBasedScoreRule.
     *
     * @param interval the time interval in seconds between awarding points
     * @param points the number of points to add each interval
     * @param scoreSystem the ScoreSystem to update
     */
    public TimeBasedScoreRule(float interval, int points, ScoreSystem scoreSystem) {
        this.interval = interval;
        this.points = points;
        this.scoreSystem = scoreSystem;
        this.lastTriggerTime = 0f;
    }

    /**
     * Applies the scoring rule based on the game timer.
     *
     * If the elapsed time since the last trigger exceeds the interval,
     * points are added to the ScoreSystem.
     *
     * @param timer the GameTimer to check elapsed time
     */
    @Override
    public void apply(GameTimer timer) {
        float elapsed = timer.getElapsed();

        if (elapsed - lastTriggerTime >= interval) {
            scoreSystem.addScore(points);
            lastTriggerTime = elapsed;
            System.out.println("[Rule] +" + points + " points (elapsed: " + elapsed + "s)");
        }
    }
}
