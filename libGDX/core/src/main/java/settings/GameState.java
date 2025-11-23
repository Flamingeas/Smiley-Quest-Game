package settings;

/**
 * Represents the different states of the game.
 * Each state corresponds to a distinct phase in the game's lifecycle.
 */
public enum GameState {
    /** Initial splash screen displayed at startup. */
    SPLASH,

    /** Main menu screen where the player can navigate options. */
    MENU,

    /** Active gameplay state. */
    PLAYING,

    /** Game is paused; gameplay is temporarily halted. */
    PAUSED,

    /** Settings menu where players can adjust game options. */
    SETTINGS,

    /** Game over state after player loses all lives or fails. */
    GAME_OVER,

    /** Smiley selection screen before starting the game. */
    CHARACTER_SELECT,

    /** Game won; player has successfully completed the objectives. */
    WINNER;
}
