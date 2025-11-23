import org.junit.jupiter.api.*;
import system.GameTimer;
import system.ScoreSystem;
import system.TimeBasedScoreRule;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Tests TimeBasedScoreRule - Coverage Maximum")
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
    @DisplayName("Création de la règle")
    void testCreation() {
        rule = new TimeBasedScoreRule(5f, 10, scoreSystem);
        assertNotNull(rule);
    }

    @Test
    @DisplayName("apply ne donne pas de points si temps insuffisant")
    void testApplyPasDePoints() {
        rule = new TimeBasedScoreRule(10f, 50, scoreSystem);
        when(timer.getElapsed()).thenReturn(5f);

        rule.apply(timer);

        assertEquals(0, scoreSystem.getScore());
    }

    @Test
    @DisplayName("apply donne des points après l'intervalle")
    void testApplyAvecIntervalle() {
        rule = new TimeBasedScoreRule(5f, 100, scoreSystem);
        when(timer.getElapsed()).thenReturn(5f);

        rule.apply(timer);

        assertEquals(100, scoreSystem.getScore());
    }

    @Test
    @DisplayName("apply plusieurs fois avec temps qui avance")
    void testApplyPlusieurs() {
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
    @DisplayName("Intervalle de 1 seconde")
    void testIntervalle1Seconde() {
        rule = new TimeBasedScoreRule(1f, 5, scoreSystem);

        when(timer.getElapsed()).thenReturn(1f);
        rule.apply(timer);
        assertEquals(5, scoreSystem.getScore());

        when(timer.getElapsed()).thenReturn(2f);
        rule.apply(timer);
        assertEquals(10, scoreSystem.getScore());
    }

    @Test
    @DisplayName("Points négatifs fonctionnent")
    void testPointsNegatifs() {
        rule = new TimeBasedScoreRule(3f, -50, scoreSystem);
        scoreSystem.addScore(200);

        when(timer.getElapsed()).thenReturn(3f);
        rule.apply(timer);

        assertEquals(150, scoreSystem.getScore());
    }

    @Test
    @DisplayName("Zéro points fonctionne")
    void testZeroPoints() {
        rule = new TimeBasedScoreRule(5f, 0, scoreSystem);

        when(timer.getElapsed()).thenReturn(5f);
        rule.apply(timer);

        assertEquals(0, scoreSystem.getScore());
    }
}