package system;

/**
 * Defines a rule for calculating points in the game.
 * Implementations can define when the rule is applicable and how many points to award
 */
public interface ScoringRule {

    /**
     * Applies the rule over time using the provided game timer.
     * This default implementation does nothing.
     *
     * @param timer the current game timer
     */
    default void apply(GameTimer timer) {
    }

    /**
     * Checks if this scoring rule is applicable for a given game event.
     *
     * @param event the game event to check
     * @return true if the rule applies to the event, false otherwise
     */
    default boolean isApplicable(GameEvent event) {
        return false;
    }

    /**
     * Calculates the points for a given game event according to this rule
     *
     * @param event the game event to calculate points for
     * @return the number of points, default is 0
     */
    default int calculatePoints(GameEvent event) {
        return 0;
    }
}
