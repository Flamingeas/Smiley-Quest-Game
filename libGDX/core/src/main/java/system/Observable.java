package system;

/**
 * Represents an object that can be observed by observers.
 * Implementing classes allow observers to register and unregister,
 * and notify all registered observers when an event occurs.
 */
public interface Observable {

    /**
     * Registers an observer to receive updates from this observable.
     *
     * @param o the observer to add
     */
    void addObserver(Observer o);

    /**
     * Removes a previously registered observer.
     *
     * @param o the observer to remove
     */
    void removeObserver(Observer o);

    /**
     * Notifies all registered observers of a game event.
     *
     * @param event the event to notify observers about
     */
    void notifyObservers(GameEvent event);
}
