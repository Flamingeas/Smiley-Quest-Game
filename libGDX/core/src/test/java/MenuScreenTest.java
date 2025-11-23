import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import settings.screen.MenuScreen;
import settings.screen.ScreenManager;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for MenuScreen.
 * These tests verify the initial state and structure of the menu without launching LibGDX.
 */
@DisplayName("MenuScreen Tests")
class MenuScreenTest {

    private MenuScreen menuScreen;

    /**
     * Executed before each test.
     * Creates a new instance of MenuScreen.
     */
    @BeforeEach
    void setUp() {
        menuScreen = new MenuScreen(null);
    }

    /**
     * Verifies that the menu has exactly 3 options.
     */
    @Test
    @DisplayName("Menu should have 3 options")
    void testMenuOptionsCount() throws Exception {
        Field menuItemsField = MenuScreen.class.getDeclaredField("menuItems");
        menuItemsField.setAccessible(true);
        String[] options = (String[]) menuItemsField.get(menuScreen);

        assertNotNull(options, "Menu items array should not be null");
        assertEquals(3, options.length, "Menu should have exactly 3 options");
    }

    /**
     * Verifies that the menu options have the correct text values.
     */
    @Test
    @DisplayName("Menu options should be START, SETTINGS, and EXIT")
    void testMenuOptionTexts() throws Exception {
        Field menuItemsField = MenuScreen.class.getDeclaredField("menuItems");
        menuItemsField.setAccessible(true);
        String[] options = (String[]) menuItemsField.get(menuScreen);

        assertEquals("START", options[0], "First option should be START");
        assertEquals("SETTINGS", options[1], "Second option should be SETTINGS");
        assertEquals("EXIT", options[2], "Third option should be EXIT");
    }

    /**
     * Verifies that the initially selected index is 0 (first option).
     */
    @Test
    @DisplayName("Initially, the first menu option should be selected")
    void testInitialSelectedIndex() throws Exception {
        Field selectedIndexField = MenuScreen.class.getDeclaredField("selectedIndex");
        selectedIndexField.setAccessible(true);
        int selectedIndex = (int) selectedIndexField.get(menuScreen);

        assertEquals(0, selectedIndex, "Initially, selected index should be 0 (first option)");
    }

    /**
     * Verifies that the game title is correct.
     */
    @Test
    @DisplayName("Title should be 'Smiley Quest'")
    void testGameTitle() throws Exception {
        Field titleField = MenuScreen.class.getDeclaredField("title");
        titleField.setAccessible(true);
        String title = (String) titleField.get(menuScreen);

        assertEquals("Smiley Quest", title, "Title should be 'Smiley Quest'");
    }

    /**
     * Verifies that the spacing between menu options is 70 pixels.
     */
    @Test
    @DisplayName("Spacing between menu options should be 70 pixels")
    void testOptionSpacing() throws Exception {
        Field spacingField = MenuScreen.class.getDeclaredField("spacing");
        spacingField.setAccessible(true);
        float spacing = (float) spacingField.get(menuScreen);

        assertEquals(70f, spacing, 0.01f, "Spacing should be 70 pixels");
    }

    /**
     * Verifies that the animation state time starts at 0.
     */
    @Test
    @DisplayName("Animation state time should start at 0")
    void testInitialStateTime() throws Exception {
        Field stateTimeField = MenuScreen.class.getDeclaredField("stateTime");
        stateTimeField.setAccessible(true);
        float stateTime = (float) stateTimeField.get(menuScreen);

        assertEquals(0f, stateTime, 0.01f, "State time should start at 0");
    }

    /**
     * Verifies that the MenuScreen class exists.
     */
    @Test
    @DisplayName("MenuScreen class exists")
    void testClassExists() {
        assertNotNull(MenuScreen.class);
    }

    /**
     * Verifies that MenuScreen implements the Screen interface.
     */
    @Test
    @DisplayName("MenuScreen implements Screen")
    void testImplementsScreen() {
        assertTrue(com.badlogic.gdx.Screen.class.isAssignableFrom(MenuScreen.class));
    }

    /**
     * Verifies that various fields exist in MenuScreen.
     */
    @Test
    @DisplayName("All expected fields exist")
    void testAllFieldsExist() {
        var fields = MenuScreen.class.getDeclaredFields();
        assertTrue(fields.length > 10, "There should be more than 10 fields");
    }

    /**
     * Verifies the existence of a specific field by name.
     */
    void assertFieldExists(String fieldName, Class<?> type) throws NoSuchFieldException {
        Field field = MenuScreen.class.getDeclaredField(fieldName);
        assertNotNull(field);
        if (type != null) {
            assertEquals(type, field.getType());
        }
    }

    @Test
    @DisplayName("MenuScreen has screenManager field")
    void testFieldScreenManagerExists() throws NoSuchFieldException {
        assertFieldExists("screenManager", null);
    }

    @Test
    @DisplayName("MenuScreen has batch field")
    void testFieldBatchExists() throws NoSuchFieldException {
        assertFieldExists("batch", null);
    }

    @Test
    @DisplayName("MenuScreen has fontTitle field")
    void testFieldFontTitleExists() throws NoSuchFieldException {
        assertFieldExists("fontTitle", null);
    }

    @Test
    @DisplayName("MenuScreen has fontText field")
    void testFieldFontTextExists() throws NoSuchFieldException {
        assertFieldExists("fontText", null);
    }

    @Test
    @DisplayName("MenuScreen has pointer field")
    void testFieldPointerExists() throws NoSuchFieldException {
        assertFieldExists("pointer", null);
    }

    @Test
    @DisplayName("MenuScreen has backgroundAnimation field")
    void testFieldBackgroundAnimationExists() throws NoSuchFieldException {
        assertFieldExists("backgroundAnimation", null);
    }

    @Test
    @DisplayName("MenuScreen has stateTime field of type float")
    void testFieldStateTimeExists() throws NoSuchFieldException {
        assertFieldExists("stateTime", float.class);
    }

    @Test
    @DisplayName("MenuScreen has selectedIndex field of type int")
    void testFieldSelectedIndexExists() throws NoSuchFieldException {
        assertFieldExists("selectedIndex", int.class);
    }

    @Test
    @DisplayName("MenuScreen has menuItems field")
    void testFieldMenuItemsExists() throws NoSuchFieldException {
        assertFieldExists("menuItems", null);
    }

    @Test
    @DisplayName("MenuScreen has title field")
    void testFieldTitleExists() throws NoSuchFieldException {
        assertFieldExists("title", null);
    }

    @Test
    @DisplayName("MenuScreen has spacing field of type float")
    void testFieldSpacingExists() throws NoSuchFieldException {
        assertFieldExists("spacing", float.class);
    }

    @Test
    @DisplayName("MenuScreen has posY field of type float")
    void testFieldPosYExists() throws NoSuchFieldException {
        assertFieldExists("posY", float.class);
    }

    @Test
    @DisplayName("MenuScreen has layouts field")
    void testFieldLayoutsExists() throws NoSuchFieldException {
        assertFieldExists("layouts", null);
    }

    @Test
    @DisplayName("MenuScreen has pointerX field of type float")
    void testFieldPointerXExists() throws NoSuchFieldException {
        assertFieldExists("pointerX", float.class);
    }

    @Test
    @DisplayName("MenuScreen has menuX field of type float")
    void testFieldMenuXExists() throws NoSuchFieldException {
        assertFieldExists("menuX", float.class);
    }

    @Test
    @DisplayName("MenuScreen has menuStartY field of type int")
    void testFieldMenuStartYExists() throws NoSuchFieldException {
        assertFieldExists("menuStartY", int.class);
    }

    @Test
    @DisplayName("MenuScreen has menuSpacing field of type int")
    void testFieldMenuSpacingExists() throws NoSuchFieldException {
        assertFieldExists("menuSpacing", int.class);
    }

    /**
     * Verifies that essential lifecycle methods exist.
     */
    @Test
    @DisplayName("All lifecycle methods exist")
    void testAllLifecycleMethodsExist() throws NoSuchMethodException {
        MenuScreen.class.getDeclaredMethod("show");
        MenuScreen.class.getDeclaredMethod("render", float.class);
        MenuScreen.class.getDeclaredMethod("resize", int.class, int.class);
        MenuScreen.class.getDeclaredMethod("pause");
        MenuScreen.class.getDeclaredMethod("resume");
        MenuScreen.class.getDeclaredMethod("hide");
        MenuScreen.class.getDeclaredMethod("dispose");
    }

    /**
     * Verifies that essential public and private methods exist and their visibility.
     */
    void assertMethodVisibility(String methodName, Class<?>[] params, boolean isPublic) throws NoSuchMethodException {
        var method = MenuScreen.class.getDeclaredMethod(methodName, params);
        if (isPublic) {
            assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
        } else {
            assertTrue(java.lang.reflect.Modifier.isPrivate(method.getModifiers()));
        }
    }

    @Test
    @DisplayName("show() method exists and is public")
    void testShowMethodExists() throws NoSuchMethodException {
        assertMethodVisibility("show", new Class<?>[]{}, true);
    }

    @Test
    @DisplayName("render(float) method exists and is public")
    void testRenderMethodExists() throws NoSuchMethodException {
        assertMethodVisibility("render", new Class<?>[]{float.class}, true);
    }

    @Test
    @DisplayName("dispose() method exists and is public")
    void testDisposeMethodExists() throws NoSuchMethodException {
        assertMethodVisibility("dispose", new Class<?>[]{}, true);
    }

    @Test
    @DisplayName("handleInput() method exists and is private")
    void testHandleInputMethodExists() throws NoSuchMethodException {
        assertMethodVisibility("handleInput", new Class<?>[]{}, false);
    }

    @Test
    @DisplayName("activateMenuItem(int) method exists and is private")
    void testActivateMenuItemMethodExists() throws NoSuchMethodException {
        assertMethodVisibility("activateMenuItem", new Class<?>[]{int.class}, false);
    }

    /**
     * Verifies that the MenuScreen constructor exists and is public.
     */
    @Test
    @DisplayName("MenuScreen constructor exists and is public")
    void testConstructorExistsAndPublic() throws NoSuchMethodException {
        var constructor = MenuScreen.class.getDeclaredConstructor(ScreenManager.class);
        assertNotNull(constructor);
        assertTrue(java.lang.reflect.Modifier.isPublic(constructor.getModifiers()));
    }

    /**
     * Verifies that MenuScreen class itself is public.
     */
    @Test
    @DisplayName("MenuScreen class is public")
    void testMenuScreenIsPublic() {
        assertTrue(java.lang.reflect.Modifier.isPublic(MenuScreen.class.getModifiers()));
    }
}
