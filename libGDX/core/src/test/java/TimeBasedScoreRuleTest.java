import org.junit.jupiter.api.*;
import system.GameTimer;
import system.ScoreSystem;
import system.TimeBasedScoreRule;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TimeBasedScoreRule.
 * Verifies score is awarded correctly based on time intervals, including positive, negative, and zero points.
 */
@DisplayName("TimeBasedScoreRule Tests - Maximum Coverage")
class TimeBasedScoreRuleTest {

    private ScoreSystem scoreSystem;
    private GameTimer timer;
    private TimeBasedScoreRule rule;

    @BeforeEach
    void setup() {
        scoreSystem = new ScoreSystem();
        timer = mock(GameTimer.class);
    }

    @Test
    @DisplayName("Rule creation")
    void testCreation() {
        rule = new TimeBasedScoreRule(5f, 10, scoreSystem);
        assertNotNull(rule);
    }

    @Test
    @DisplayName("apply does not add points if time is insufficient")
    void testApplyNoPoints() {
        rule = new TimeBasedScoreRule(10f, 50, scoreSystem);
        when(timer.getElapsed()).thenReturn(5f);

        rule.apply(timer);

        assertEquals(0, scoreSystem.getScore());
    }

    @Test
    @DisplayName("apply awards points after interval")
    void testApplyWithInterval() {
        rule = new TimeBasedScoreRule(5f, 100, scoreSystem);
        when(timer.getElapsed()).thenReturn(5f);

        rule.apply(timer);

        assertEquals(100, scoreSystem.getScore());
    }

    @Test
    @DisplayName("apply multiple times with advancing time")
    void testApplyMultiple() {
        rule = new TimeBasedScoreRule(2f, 10, scoreSystem);

        when(timer.getElapsed()).thenReturn(1f);
        rule.apply(timer);
        assertEquals(0, scoreSystem.getScore());

        when(timer.getElapsed()).thenReturn(2f);
        rule.apply(timer);
        assertEquals(10, scoreSystem.getScore());

        when(timer.getElapsed()).thenReturn(4f);
        rule.apply(timer);
        assertEquals(20, scoreSystem.getScore());
    }

    @Test
    @DisplayName("1-second interval")
    void testOneSecondInterval() {
        rule = new TimeBasedScoreRule(1f, 5, scoreSystem);

        when(timer.getElapsed()).thenReturn(1f);
        rule.apply(timer);
        assertEquals(5, scoreSystem.getScore());

        when(timer.getElapsed()).thenReturn(2f);
        rule.apply(timer);
        assertEquals(10, scoreSystem.getScore());
    }

    @Test
    @DisplayName("Negative points work")
    void testNegativePoints() {
        rule = new TimeBasedScoreRule(3f, -50, scoreSystem);
        scoreSystem.addScore(200);

        when(timer.getElapsed()).thenReturn(3f);
        rule.apply(timer);

        assertEquals(150, scoreSystem.getScore());
    }

    @Test
    @DisplayName("Zero points work")
    void testZeroPoints() {
        rule = new TimeBasedScoreRule(5f, 0, scoreSystem);

        when(timer.getElapsed()).thenReturn(5f);
        rule.apply(timer);

        assertEquals(0, scoreSystem.getScore());
    }
}
