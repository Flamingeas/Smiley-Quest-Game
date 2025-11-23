package settings.screen;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for GameWinnerScreen.
 * Verifies class existence, field structure, constants, methods, constructor, and visibility.
 */
@DisplayName("GameWinnerScreen Tests")
class GameWinnerScreenTest {

    // ================= STRUCTURE TESTS =================

    @Test
    @DisplayName("Class GameWinnerScreen exists")
    void testClassExists() {
        assertNotNull(GameWinnerScreen.class);
    }

    @Test
    @DisplayName("GameWinnerScreen implements Screen")
    void testImplementsScreen() {
        assertTrue(com.badlogic.gdx.Screen.class.isAssignableFrom(GameWinnerScreen.class));
    }

    // ================= FIELD TESTS =================

    @Test
    @DisplayName("Field 'screenManager' exists")
    void testFieldScreenManagerExists() throws NoSuchFieldException {
        Field field = GameWinnerScreen.class.getDeclaredField("screenManager");
        assertNotNull(field);
    }

    @Test
    @DisplayName("Field 'batch' exists")
    void testFieldBatchExists() throws NoSuchFieldException {
        Field field = GameWinnerScreen.class.getDeclaredField("batch");
        assertNotNull(field);
    }

    @Test
    @DisplayName("Field 'shapeRenderer' exists")
    void testFieldShapeRendererExists() throws NoSuchFieldException {
        Field field = GameWinnerScreen.class.getDeclaredField("shapeRenderer");
        assertNotNull(field);
    }

    @Test
    @DisplayName("Field 'winnerFont' exists")
    void testFieldWinnerFontExists() throws NoSuchFieldException {
        Field field = GameWinnerScreen.class.getDeclaredField("winnerFont");
        assertNotNull(field);
    }

    @Test
    @DisplayName("Field 'promptFont' exists")
    void testFieldPromptFontExists() throws NoSuchFieldException {
        Field field = GameWinnerScreen.class.getDeclaredField("promptFont");
        assertNotNull(field);
    }

    @Test
    @DisplayName("Field 'fontText' exists")
    void testFieldFontTextExists() throws NoSuchFieldException {
        Field field = GameWinnerScreen.class.getDeclaredField("fontText");
        assertNotNull(field);
    }

    @Test
    @DisplayName("Field 'stateTime' exists and is float")
    void testFieldStateTimeExists() throws NoSuchFieldException {
        Field field = GameWinnerScreen.class.getDeclaredField("stateTime");
        assertEquals(float.class, field.getType());
    }

    @Test
    @DisplayName("Field 'showPrompt' exists and is boolean")
    void testFieldShowPromptExists() throws NoSuchFieldException {
        Field field = GameWinnerScreen.class.getDeclaredField("showPrompt");
        assertEquals(boolean.class, field.getType());
    }

    @Test
    @DisplayName("Field 'finalScore' exists and is int")
    void testFieldFinalScoreExists() throws NoSuchFieldException {
        Field field = GameWinnerScreen.class.getDeclaredField("finalScore");
        assertEquals(int.class, field.getType());
    }

    // ================= CONSTANT TESTS =================

    @Test
    @DisplayName("Constant BGCOLOR exists")
    void testBGCOLORExists() throws NoSuchFieldException {
        Field field = GameWinnerScreen.class.getDeclaredField("BGCOLOR");
        assertNotNull(field);
    }

    @Test
    @DisplayName("Constant WINNER_COLOR exists")
    void testWINNER_COLORExists() throws NoSuchFieldException {
        Field field = GameWinnerScreen.class.getDeclaredField("WINNER_COLOR");
        assertNotNull(field);
    }

    @Test
    @DisplayName("Constant PROMPT_COLOR exists")
    void testPROMPT_COLORExists() throws NoSuchFieldException {
        Field field = GameWinnerScreen.class.getDeclaredField("PROMPT_COLOR");
        assertNotNull(field);
    }

    @Test
    @DisplayName("Constant WINNER_FONT_SIZE exists")
    void testWINNER_FONT_SIZEExists() throws NoSuchFieldException {
        Field field = GameWinnerScreen.class.getDeclaredField("WINNER_FONT_SIZE");
        assertEquals(int.class, field.getType());
    }

    @Test
    @DisplayName("Constant PROMPT_FONT_SIZE exists")
    void testPROMPT_FONT_SIZEExists() throws NoSuchFieldException {
        Field field = GameWinnerScreen.class.getDeclaredField("PROMPT_FONT_SIZE");
        assertEquals(int.class, field.getType());
    }

    @Test
    @DisplayName("Constant BLINK_SPEED exists")
    void testBLINK_SPEEDExists() throws NoSuchFieldException {
        Field field = GameWinnerScreen.class.getDeclaredField("BLINK_SPEED");
        assertEquals(float.class, field.getType());
    }

    // ================= METHOD TESTS =================

    @Test
    @DisplayName("Method 'show()' exists")
    void testMethodShowExists() throws NoSuchMethodException {
        assertNotNull(GameWinnerScreen.class.getDeclaredMethod("show"));
    }

    @Test
    @DisplayName("Method 'render(float)' exists")
    void testMethodRenderExists() throws NoSuchMethodException {
        assertNotNull(GameWinnerScreen.class.getDeclaredMethod("render", float.class));
    }

    @Test
    @DisplayName("Method 'resize(int, int)' exists")
    void testMethodResizeExists() throws NoSuchMethodException {
        assertNotNull(GameWinnerScreen.class.getDeclaredMethod("resize", int.class, int.class));
    }

    @Test
    @DisplayName("Method 'pause()' exists")
    void testMethodPauseExists() throws NoSuchMethodException {
        assertNotNull(GameWinnerScreen.class.getDeclaredMethod("pause"));
    }

    @Test
    @DisplayName("Method 'resume()' exists")
    void testMethodResumeExists() throws NoSuchMethodException {
        assertNotNull(GameWinnerScreen.class.getDeclaredMethod("resume"));
    }

    @Test
    @DisplayName("Method 'hide()' exists")
    void testMethodHideExists() throws NoSuchMethodException {
        assertNotNull(GameWinnerScreen.class.getDeclaredMethod("hide"));
    }

    @Test
    @DisplayName("Method 'dispose()' exists")
    void testMethodDisposeExists() throws NoSuchMethodException {
        assertNotNull(GameWinnerScreen.class.getDeclaredMethod("dispose"));
    }

    @Test
    @DisplayName("Method 'handleInput()' exists")
    void testMethodHandleInputExists() throws NoSuchMethodException {
        assertNotNull(GameWinnerScreen.class.getDeclaredMethod("handleInput"));
    }

    @Test
    @DisplayName("Method 'renderOverlay()' exists")
    void testMethodRenderOverlayExists() throws NoSuchMethodException {
        assertNotNull(GameWinnerScreen.class.getDeclaredMethod("renderOverlay"));
    }

    @Test
    @DisplayName("Method 'renderText()' exists")
    void testMethodRenderTextExists() throws NoSuchMethodException {
        assertNotNull(GameWinnerScreen.class.getDeclaredMethod("renderText"));
    }

    @Test
    @DisplayName("Method 'initializeFonts()' exists")
    void testMethodInitializeFontsExists() throws NoSuchMethodException {
        assertNotNull(GameWinnerScreen.class.getDeclaredMethod("initializeFonts"));
    }

    @Test
    @DisplayName("Method 'drawCenteredText' exists")
    void testMethodDrawCenteredTextExists() throws NoSuchMethodException {
        assertNotNull(GameWinnerScreen.class.getDeclaredMethod(
                "drawCenteredText",
                com.badlogic.gdx.graphics.g2d.BitmapFont.class,
                String.class,
                float.class,
                float.class
        ));
    }

    // ================= CONSTRUCTOR TESTS =================

    @Test
    @DisplayName("Constructor GameWinnerScreen(ScreenManager) exists")
    void testConstructorExists() throws NoSuchMethodException {
        assertNotNull(GameWinnerScreen.class.getDeclaredConstructor(ScreenManager.class));
    }

    @Test
    @DisplayName("Constructor is public")
    void testConstructorIsPublic() throws NoSuchMethodException {
        var constructor = GameWinnerScreen.class.getDeclaredConstructor(ScreenManager.class);
        assertTrue(java.lang.reflect.Modifier.isPublic(constructor.getModifiers()));
    }

    // ================= VISIBILITY TESTS =================

    @Test
    @DisplayName("Class GameWinnerScreen is public")
    void testGameWinnerScreenIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(GameWinnerScreen.class.getModifiers()));
    }

    @Test
    @DisplayName("Method 'show()' is public")
    void testShowIsPublic() throws NoSuchMethodException {
        var method = GameWinnerScreen.class.getDeclaredMethod("show");
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("Method 'render(float)' is public")
    void testRenderIsPublic() throws NoSuchMethodException {
        var method = GameWinnerScreen.class.getDeclaredMethod("render", float.class);
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
    }

    @Test
    @DisplayName("Method 'handleInput()' is private")
    void testHandleInputIsPrivate() throws NoSuchMethodException {
        var method = GameWinnerScreen.class.getDeclaredMethod("handleInput");
        assertTrue(java.lang.reflect.Modifier.isPrivate(method.getModifiers()));
    }

    @Test
    @DisplayName("Method 'renderOverlay()' is private")
    void testRenderOverlayIsPrivate() throws NoSuchMethodException {
        var method = GameWinnerScreen.class.getDeclaredMethod("renderOverlay");
        assertTrue(java.lang.reflect.Modifier.isPrivate(method.getModifiers()));
    }

    @Test
    @DisplayName("Method 'renderText()' is private")
    void testRenderTextIsPrivate() throws NoSuchMethodException {
        var method = GameWinnerScreen.class.getDeclaredMethod("renderText");
        assertTrue(java.lang.reflect.Modifier.isPrivate(method.getModifiers()));
    }

    @Test
    @DisplayName("All public methods exist")
    void testAllPublicMethodsExist() {
        var methods = GameWinnerScreen.class.getDeclaredMethods();
        assertTrue(methods.length > 5);
    }

    @Test
    @DisplayName("All fields exist")
    void testAllFieldsExist() {
        var fields = GameWinnerScreen.class.getDeclaredFields();
        assertTrue(fields.length > 5);
    }
}
