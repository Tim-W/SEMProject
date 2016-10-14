package nl.tudelft.sem.group2.global;

/**
 * A list of definitions that can be used throughout the project.
 */
public final class Globals {

    public static final int BOARD_WIDTH = 300;
    public static final int BOARD_HEIGHT = 300;
    public static final int GAME_WIDTH = 340;
    public static final int GAME_HEIGHT = 420;
    public static final double TARGET_PERCENTAGE = .65;
    public static final int CURSOR_START_X = 75;
    public static final int CURSOR_START_Y = 150;
    public static final int BOARD_MARGIN = 8;
    public static final int GAME_OFFSET_X = 15;
    public static final int GAME_OFFSET_Y = 75;
    public static final int BORDER_BOTTOM_HEIGHT = 20;
    public static final int BORDER_BOTTOM_POSITION_Y = 380;
    public static final int MESSAGEBOX_POSITION_X = 50;
    public static final int MESSAGEBOX_POSITION_Y = 176;
    public static final int SCORESCENE_POSITION_Y = 60;
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
    public static final String HELP_SCREEN_MESSAGE = "1 player: \nUse arrow-keys to move. Use z and x to draw fast "
            + "and slow.\n\n2 players: \nfirst player uses the regular controls. Second player uses wasd-keys to"
            + " move and i and o to draw\n\nPress 1 for single player game and 2 for a 2 player game!";
    public static final int STARTSCENE_SPACING = 30;
    public static final int STARTSCENE_HELPTEXT_WRAPPING = 200;

    private Globals() {
    }
}
