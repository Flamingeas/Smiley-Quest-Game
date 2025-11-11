package system;

public interface ScoringRule {

    default void apply(GameTimer timer) {
    }

    default boolean isApplicable(GameEvent event) {
        return false;
    }

    default int calculatePoints(GameEvent event) {
        return 0;
    }
}
