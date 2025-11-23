package system;

/**
 * Represents an event that occurs in the game.
 * Each event has a type (a string identifier) and optional data associated with it.
 */
public class GameEvent {

    /** Type of the event (e.g., "score", "collision"). */
    private final String type;

    /** Optional additional data related to the event. */
    private final Object data;

    /**
     * Creates a new game event with the specified type and data.
     *
     * @param type the type of the event
     * @param data additional data for the event, can be null
     */
    public GameEvent(String type, Object data) {
        this.type = type;
        this.data = data;
    }

    /**
     * Returns the type of the event.
     *
     * @return the event type
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the data associated with the event.
     *
     * @return the event data
     */
    public Object getData() {
        return data;
    }

    /**
     * Returns a string representation of the event, including its type and data.
     *
     * @return a string describing the event
     */
    @Override
    public String toString() {
        return "GameEvent{" +
                "type='" + type + '\'' +
                ", data=" + data +
                '}';
    }
}
