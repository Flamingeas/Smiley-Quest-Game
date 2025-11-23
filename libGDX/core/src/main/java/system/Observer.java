package system;

/**
 * Represents an object that can receive notifications from an observable.
 * Implementing classes respond to events sent by objects implementing the Observable interface.
 */
public interface Observer {

    /**
     * Called when the observable sends a notification.
     *
     * @param event the game event that triggered this notification
     */
    void onNotify(GameEvent event);
}
