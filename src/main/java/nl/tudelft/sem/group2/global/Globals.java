package nl.tudelft.sem.group2.global;

/**
 * A list of definitions that can be used throughout the project.
 */
public final class Globals {

    public static final int BOARD_WIDTH = 300;
    public static final int BOARD_HEIGHT = 300;
    public static final int GRID_WIDTH = BOARD_WIDTH / 2;
    public static final int GRID_HEIGHT = BOARD_HEIGHT / 2;
    public static final int GRID_SURFACE = GRID_WIDTH * GRID_HEIGHT;
    public static final int GAME_WIDTH = 340;
    public static final int GAME_HEIGHT = 420;
    public static final int CURSOR_START_X = 75;
    public static final int CURSOR_START_Y = 150;
    public static final int BOARD_MARGIN = 8;
    public static final int GAME_OFFSET_X = 15;
    public static final int GAME_OFFSET_Y = 100;
    public static final int BORDER_BOTTOM_HEIGHT = 20;
    public static final int BORDER_BOTTOM_POSITION_Y = 380;
    public static final int MESSAGEBOX_POSITION_X = 50;
    public static final int MESSAGEBOX_POSITION_Y = 176;
    public static final int SCORESCENE_POSITION_Y = 100;
    public static final int GAMEOVER_POSITION_X = 103;
    public static final int GAMEWON_POSITION_X = 115;
    public static final double GAME_START_SOUND_VOLUME = 0.09;
    public static final double SUCCESS_SOUND_VOLUME = 0.5;
    public static final double GAME_OVER_SOUND_VOLUME = 0.2;
    public static final int FUSE_WIDTH = 16;
    public static final int FUSE_HEIGHT = 16;
    public static final int QIX_START_X = 30;
    public static final int QIX_START_Y = 30;
    public static final int FAST_AREA_MULTIPLIER = 10000;
    public static final int SLOW_AREA_MULTIPLIER = 20000;
    public static final int FUSE_DELAY = 13;
    public static final int STARTSCENE_VBOX_LAYOUT_Y = 80;
    public static final int STARTSCENE_HBOX_SPACING = 20;
    public static final int STARTSCENE_BUTTON_WIDTH = 120;
    public static final String HELP_SCREEN_MESSAGE = "\n1 player: \nUse arrow-keys to move. Use o to draw fast "
            + "and i to draw slow.\n\n2 players: \nfirst player uses the regular controls."
            + "Second player uses wasd-keys to move and z and x to draw\n\n"
            + "There are 3 powerups in the game:\n- Speed - Gives you extra speed\n- Life - Gives you an extra life"
            + "\n- Hungry - Lets you eat Sparx\n\nPress ESCAPE to return to the main menu";
    public static final int STARTSCENE_SPACING = 30;
    public static final double POWERUP_THRESHOLD = 0.003;
    public static final int POWERUP_LIFETIME = 200;
    public static final int POWERUP_SPEED_DURATION = 75;
    public static final int POWERUP_EAT_DURATION = 100;
    public static final int CURSOR_FAST = 2;
    public static final int CURSOR_SLOW = 1;
    public static final double HALF = 0.5;
    public static final int STARTSCENE_HELPTEXT_WRAPPING = 280;
    public static final int NANO_SECONDS_PER_SECOND = 100000000;
    public static final int LIVES = 3;
    public static final int LEVELS = 3;
    public static final int ANIMATION_SPEED = 30;
    public static final int DEATH_ANIMATION_SPEED = 20;


    private Globals() {
    }
}
