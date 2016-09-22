package nl.tudelft.sem.group2.scenes;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.AreaState;
import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.ScoreCounter;
import nl.tudelft.sem.group2.game.Board;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Fuse;
import nl.tudelft.sem.group2.units.Qix;
import nl.tudelft.sem.group2.units.Sparx;
import nl.tudelft.sem.group2.units.SparxDirection;
import nl.tudelft.sem.group2.units.Unit;

import static nl.tudelft.sem.group2.LaunchApp.playSound;
import static nl.tudelft.sem.group2.global.Globals.BOARD_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.BOARD_MARGIN;
import static nl.tudelft.sem.group2.global.Globals.BOARD_WIDTH;
import static nl.tudelft.sem.group2.global.Globals.BORDER_BOTTOM_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.BORDER_BOTTOM_POSITION_Y;
import static nl.tudelft.sem.group2.global.Globals.CURSOR_START_X;
import static nl.tudelft.sem.group2.global.Globals.CURSOR_START_Y;
import static nl.tudelft.sem.group2.global.Globals.FUSE_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.FUSE_WIDTH;
import static nl.tudelft.sem.group2.global.Globals.GAMEOVER_POSITION_X;
import static nl.tudelft.sem.group2.global.Globals.GAMEWON_POSITION_X;
import static nl.tudelft.sem.group2.global.Globals.GAME_OFFSET_X;
import static nl.tudelft.sem.group2.global.Globals.GAME_OFFSET_Y;
import static nl.tudelft.sem.group2.global.Globals.GAME_OVER_SOUND_VOLUME;
import static nl.tudelft.sem.group2.global.Globals.GAME_START_SOUND_VOLUME;
import static nl.tudelft.sem.group2.global.Globals.GAME_WIDTH;
import static nl.tudelft.sem.group2.global.Globals.MESSAGEBOX_POSITION_X;
import static nl.tudelft.sem.group2.global.Globals.MESSAGEBOX_POSITION_Y;
import static nl.tudelft.sem.group2.global.Globals.SCORESCENE_POSITION_Y;
import static nl.tudelft.sem.group2.global.Globals.SUCCESS_SOUND_VOLUME;

/**
 * GameScene contains all the information about the current game.
 */
public class GameScene extends Scene {

    private static final int NANO_SECONDS_PER_SECOND = 100000000;
    private static AnimationTimer animationTimer;
    private static Cursor cursor;
    private static AreaTracker areaTracker;
    private static ScoreCounter scoreCounter;
    private static Label messageLabel;
    private static VBox messageBox;
    private long previousTime;
    private Board board;
    private boolean isRunning = false;
    private Qix qix;
    private ScoreScene scoreScene;

    // TODO implement life system
    // private int lifes;

    /**
     * Create a new GameScene.
     * Sets up all listeners and default objects to play the game
     *
     * @param root  the root scene this scene is built on
     * @param color background color of the scene
     */
    public GameScene(final Group root, Color color) {
        super(root, color);
        Canvas canvas = new Canvas(BOARD_WIDTH + 2 * BOARD_MARGIN, BOARD_HEIGHT + 2 * BOARD_MARGIN);
        canvas.setLayoutX(GAME_OFFSET_X);
        canvas.setLayoutY(GAME_OFFSET_Y);
        areaTracker = new AreaTracker();
        cursor = new Cursor(CURSOR_START_X, CURSOR_START_Y, BOARD_MARGIN * 2, BOARD_MARGIN * 2);
        board = new Board(canvas);

        scoreCounter = new ScoreCounter();

        messageLabel = new Label("Press SPACE to begin!");
        final int messageBoxSpacing = 10;
        messageBox = new VBox(messageBoxSpacing);
        addSparx();

        qix = new Qix();
        // Hacky way to create black bottom border
        Canvas bottomBorder = new Canvas(BOARD_WIDTH, BORDER_BOTTOM_HEIGHT);
        bottomBorder.setLayoutY(BORDER_BOTTOM_POSITION_Y);
        board.addUnit(cursor);
        board.addUnit(qix);
        areaTracker = new AreaTracker();
        scoreCounter = new ScoreCounter();
        addMessageBox();

        createScoreScene();

        root.getChildren().add(scoreScene);
        root.getChildren().add(canvas);
        root.getChildren().add(bottomBorder);
        root.getChildren().add(messageBox);

        previousTime = System.nanoTime();
        board.draw();

        registerKeyPressedHandler();
        registerKeyReleasedHandler();
        createAnimationTimer();
    }

    /**
     * Start animations.
     */
    public static void animationTimerStart() {
        animationTimer.start();
    }

    /**
     * Stop animations.
     */
    public static void animationTimerStop() {
        animationTimer.stop();
    }

    public static AreaTracker getAreaTracker() {
        return areaTracker;
    }

    public static ScoreCounter getScoreCounter() {
        return scoreCounter;
    }

    public static Cursor getQixCursor() {
        return cursor;
    }

    /**
     * Play a game over sound.
     * show game over text,
     * stop the animations.
     */
    public static void gameOver() {
        // TODO add code for gameover
        animationTimerStop();
        messageBox.setLayoutX(GAMEOVER_POSITION_X);
        messageLabel.setText(" Game Over! ");

        //Plays game over sound
        playSound("/sounds/Qix_Death.mp3", GAME_OVER_SOUND_VOLUME);
    }

    /**
     * TODO Play the game won sound.
     * stop the animations,
     * show that the player has won
     */
    public static void gameWon() {
        animationTimerStop();
        messageBox.setLayoutX(GAMEWON_POSITION_X);
        messageLabel.setText(" You Won! ");
    }

    private void createScoreScene() {
        Group group = new Group();
        scoreScene = new ScoreScene(group, GAME_WIDTH, SCORESCENE_POSITION_Y);

        // TODO shift this to a game class and save/load score
        scoreScene.setScore(0);
        scoreScene.setClaimedPercentage(0);
    }

    private void addSparx() {
        Sparx sparxRight = new Sparx(CURSOR_START_X, 0, BOARD_MARGIN * 2, BOARD_MARGIN * 2, SparxDirection.RIGHT);
        Sparx sparxLeft = new Sparx(CURSOR_START_X, 0, BOARD_MARGIN * 2, BOARD_MARGIN * 2, SparxDirection.LEFT);
        board.addUnit(sparxRight);
        board.addUnit(sparxLeft);
    }

    private void addMessageBox() {
        // Messagebox&label for displaying start and end messages
        messageBox.setAlignment(Pos.CENTER);
        messageBox.getChildren().add(messageLabel);
        messageBox.setStyle("-fx-background-color: #000000;");
        messageBox.setLayoutX(MESSAGEBOX_POSITION_X);
        messageBox.setLayoutY(MESSAGEBOX_POSITION_Y);
        messageLabel.setStyle("-fx-font-size: 24px;");
        messageLabel.setTextFill(Color.YELLOW);
    }

    private void registerKeyPressedHandler() {
        final ArrayList<KeyCode> arrowKeys = new ArrayList<KeyCode>();
        arrowKeys.add(KeyCode.UP);
        arrowKeys.add(KeyCode.DOWN);
        arrowKeys.add(KeyCode.LEFT);
        arrowKeys.add(KeyCode.RIGHT);

        setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                if (e.getCode().equals(KeyCode.SPACE) && !isRunning) {
                    // TODO remove this start and start using game
                    playSound("/sounds/Qix_NewLife.mp3", GAME_START_SOUND_VOLUME);
                    animationTimer.start();
                    isRunning = true;
                    messageLabel.setText("");
                } else if (arrowKeys.contains(e.getCode())) {
                    for (Unit unit : board.getUnits()) {
                        if (unit instanceof Fuse) {
                            ((Fuse) unit).setMoving(false);
                        }
                    }
                    cursor.setCurrentMove(e.getCode());
                } else if (e.getCode().equals(KeyCode.X)) {
                    cursor.setSpeed(1);
                    cursor.setDrawing(true);
                    cursor.setFast(false);
                } else if (e.getCode().equals(KeyCode.Z)) {
                    cursor.setSpeed(2);
                    cursor.setDrawing(true);
                    cursor.setFast(true);
                }
            }
        });
    }

    private void registerKeyReleasedHandler() {
        setOnKeyReleased(new EventHandler<KeyEvent>() {
            public void handle(KeyEvent e) {
                KeyCode keyCode = e.getCode();
                if (keyCode.equals(cursor.getCurrentMove())) {
                    if (areaTracker.getStix().contains(new Point(cursor.getX(), cursor.getY()))) {
                        boolean fuseExists = false;
                        for (Unit unit : board.getUnits()) {
                            if (unit instanceof Fuse) {
                                fuseExists = true;
                                ((Fuse) unit).setMoving(true);
                            }
                        }
                        if (!fuseExists) {
                            board.addUnit(
                                    new Fuse((int) areaTracker.getStix().getFirst().getX(),
                                            (int) areaTracker.getStix().getFirst().getY(),
                                            FUSE_WIDTH,
                                            FUSE_HEIGHT));
                        }
                    }
                    cursor.setCurrentMove(null);
                } else if (keyCode.equals(KeyCode.X)) {
                    cursor.setDrawing(false);
                    cursor.setSpeed(2);
                } else if (keyCode.equals(KeyCode.Z)) {
                    cursor.setDrawing(false);
                    cursor.setSpeed(2);
                }
            }
        });
    }

    /**
     * Setup an animation timer that runs at 300FPS.
     */
    public void createAnimationTimer() {
        // animation timer for handling a loop
        animationTimer = new AnimationTimer() {
            public void handle(long now) {
                if (now - previousTime > NANO_SECONDS_PER_SECOND / 3) {
                    if (scoreCounter.getTotalPercentage() >= scoreCounter.getTargetPercentage()) {
                        gameWon();
                    }
                    previousTime = now;
                    // draw
                    board.draw();
                    board.collisions();
                    qixStixCollisions();
                    scoreScene.setScore(scoreCounter.getTotalScore());
                    scoreScene.setClaimedPercentage((int) (scoreCounter.getTotalPercentage() * 100));
                    // TODO turn this on for area calculation
                    calculateArea();
                }
            }

        };
    }

    /**
     * Calculates collisions between Stix and Qix.
     */
    private void qixStixCollisions() {
        Polygon qixP = qix.toPolygon();
        for (Point point : areaTracker.getStix()) {
            if (qixP.intersects(point.getX(), point.getY(), 1, 1)) {
                gameOver();
            }
        }
    }

    /**
     * When a new area is completed, calculate the new score.
     */
    private void calculateArea() {
        // TODO turn on if isdrawing is implemented
        // if (cursor.isDrawing()) {

        if (areaTracker.getBoardGrid()[cursor.getX()][cursor.getY()] == AreaState.OUTERBORDER
                && !areaTracker.getStix().isEmpty()) {
            playSound("/sounds/Qix_Success.mp3", SUCCESS_SOUND_VOLUME);
            areaTracker.calculateNewArea(new Point(qix.getX(), qix.getY()),
                    cursor.isFast());
            //Remove the Fuse from the board when completing an area
            board.removeFuse();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }

}
