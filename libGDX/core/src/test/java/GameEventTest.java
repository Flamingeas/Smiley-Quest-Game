package system;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GameEvent Tests")
class GameEventTest {

    private GameEvent event;

    @Test
    @DisplayName("Constructor creates an event with type and data")
    void testConstructorWithTypeAndData() {
        event = new GameEvent("ITEM_COLLECTED", 50);
        assertNotNull(event);
    }

    @Test
    @DisplayName("Constructor creates an event with type and null data")
    void testConstructorWithTypeAndNullData() {
        event = new GameEvent("PLAYER_DEAD", null);
        assertNotNull(event);
    }

    @Test
    @DisplayName("Constructor creates an event with empty type string")
    void testConstructorWithEmptyType() {
        event = new GameEvent("", "data");
        assertNotNull(event);
    }

    @Test
    @DisplayName("getType returns the correct type")
    void testGetType() {
        event = new GameEvent("ITEM_COLLECTED", 50);
        assertEquals("ITEM_COLLECTED", event.getType());
    }

    @Test
    @DisplayName("getType returns the type for multiple events")
    void testGetTypeDifferentEvents() {
        GameEvent event1 = new GameEvent("PLAYER_HIT", null);
        GameEvent event2 = new GameEvent("CHEST_FOUND", 100);
        GameEvent event3 = new GameEvent("LEVEL_UP", 5);

        assertEquals("PLAYER_HIT", event1.getType());
        assertEquals("CHEST_FOUND", event2.getType());
        assertEquals("LEVEL_UP", event3.getType());
    }

    @Test
    @DisplayName("getType returns empty string")
    void testGetTypeEmpty() {
        event = new GameEvent("", "data");
        assertEquals("", event.getType());
    }

    @Test
    @DisplayName("getData returns the correct data")
    void testGetData() {
        event = new GameEvent("ITEM_COLLECTED", 50);
        assertEquals(50, event.getData());
    }

    @Test
    @DisplayName("getData returns null if no data")
    void testGetDataNull() {
        event = new GameEvent("PLAYER_DEAD", null);
        assertNull(event.getData());
    }

    @Test
    @DisplayName("getData returns a string")
    void testGetDataString() {
        event = new GameEvent("MESSAGE", "Hello");
        assertEquals("Hello", event.getData());
    }

    @Test
    @DisplayName("getData returns a boolean")
    void testGetDataBoolean() {
        event = new GameEvent("STATUS", true);
        assertEquals(true, event.getData());
    }

    @Test
    @DisplayName("getData returns a float")
    void testGetDataFloat() {
        event = new GameEvent("SCORE", 123.45f);
        assertEquals(123.45f, event.getData());
    }

    @Test
    @DisplayName("getData returns a complex object")
    void testGetDataObject() {
        Object obj = new Object();
        event = new GameEvent("CUSTOM", obj);
        assertEquals(obj, event.getData());
    }

    @Test
    @DisplayName("toString contains type")
    void testToStringContainsType() {
        event = new GameEvent("ITEM_COLLECTED", 50);
        assertTrue(event.toString().contains("ITEM_COLLECTED"));
    }

    @Test
    @DisplayName("toString contains data")
    void testToStringContainsData() {
        event = new GameEvent("ITEM_COLLECTED", 50);
        assertTrue(event.toString().contains("50"));
    }

    @Test
    @DisplayName("toString contains 'GameEvent'")
    void testToStringContainsGameEvent() {
        event = new GameEvent("TEST", null);
        assertTrue(event.toString().contains("GameEvent"));
    }

    @Test
    @DisplayName("toString with null data")
    void testToStringWithNullData() {
        event = new GameEvent("PLAYER_DEAD", null);
        assertTrue(event.toString().contains("null"));
    }

    @Test
    @DisplayName("toString with string data")
    void testToStringWithStringData() {
        event = new GameEvent("MESSAGE", "Important");
        assertTrue(event.toString().contains("Important"));
    }

    @Test
    @DisplayName("Two events with same type and data are equal")
    void testEquality() {
        GameEvent event1 = new GameEvent("ITEM_COLLECTED", 50);
        GameEvent event2 = new GameEvent("ITEM_COLLECTED", 50);

        assertEquals(event1.getType(), event2.getType());
        assertEquals(event1.getData(), event2.getData());
    }

    @Test
    @DisplayName("Two events with different types are not equal")
    void testDifferentTypes() {
        GameEvent event1 = new GameEvent("ITEM_COLLECTED", 50);
        GameEvent event2 = new GameEvent("PLAYER_HIT", 50);

        assertNotEquals(event1.getType(), event2.getType());
    }

    @Test
    @DisplayName("Two events with different data are not equal")
    void testDifferentData() {
        GameEvent event1 = new GameEvent("ITEM_COLLECTED", 50);
        GameEvent event2 = new GameEvent("ITEM_COLLECTED", 100);

        assertNotEquals(event1.getData(), event2.getData());
    }

    @Test
    @DisplayName("ITEM_COLLECTED event with integer data")
    void testItemCollectedEvent() {
        event = new GameEvent("ITEM_COLLECTED", 25);
        assertEquals("ITEM_COLLECTED", event.getType());
        assertEquals(25, event.getData());
    }

    @Test
    @DisplayName("PLAYER_HIT event without data")
    void testPlayerHitEvent() {
        event = new GameEvent("PLAYER_HIT", null);
        assertEquals("PLAYER_HIT", event.getType());
        assertNull(event.getData());
    }

    @Test
    @DisplayName("PLAYER_DEAD event without data")
    void testPlayerDeadEvent() {
        event = new GameEvent("PLAYER_DEAD", null);
        assertEquals("PLAYER_DEAD", event.getType());
        assertNull(event.getData());
    }

    @Test
    @DisplayName("CHEST_FOUND event without data")
    void testChestFoundEvent() {
        event = new GameEvent("CHEST_FOUND", null);
        assertEquals("CHEST_FOUND", event.getType());
        assertNull(event.getData());
    }

    @Test
    @DisplayName("SCORE_UPDATED event with integer data")
    void testScoreUpdatedEvent() {
        event = new GameEvent("SCORE_UPDATED", 1000);
        assertEquals("SCORE_UPDATED", event.getType());
        assertEquals(1000, event.getData());
    }

    @Test
    @DisplayName("Type field is immutable")
    void testTypeImmutable() {
        event = new GameEvent("ORIGINAL", 50);
        String originalType = event.getType();

        GameEvent event2 = new GameEvent("MODIFIED", 50);
        assertEquals(originalType, event.getType());
    }

    @Test
    @DisplayName("Data field is immutable")
    void testDataImmutable() {
        event = new GameEvent("TEST", 50);
        int originalData = (int) event.getData();

        GameEvent event2 = new GameEvent("TEST", 100);
        assertEquals(originalData, event.getData());
    }

    @Test
    @DisplayName("Event with very long type string")
    void testLongType() {
        String longType = "VERY_LONG_EVENT_TYPE_NAME_FOR_TESTING_PURPOSES_123456789";
        event = new GameEvent(longType, null);
        assertEquals(longType, event.getType());
    }

    @Test
    @DisplayName("Event with zero data")
    void testZeroData() {
        event = new GameEvent("TEST", 0);
        assertEquals(0, event.getData());
    }

    @Test
    @DisplayName("Event with negative data")
    void testNegativeData() {
        event = new GameEvent("TEST", -50);
        assertEquals(-50, event.getData());
    }

    @Test
    @DisplayName("Event type with special characters")
    void testSpecialCharactersInType() {
        event = new GameEvent("TEST_EVENT-123", 50);
        assertEquals("TEST_EVENT-123", event.getType());
    }
}
