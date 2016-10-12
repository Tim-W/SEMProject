package nl.tudelft.sem.group2.controllers;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import nl.tudelft.sem.group2.AreaState;
import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.LaunchApp;
import nl.tudelft.sem.group2.Logger;
import nl.tudelft.sem.group2.ScoreCounter;
import nl.tudelft.sem.group2.collisions.CollisionHandler;
import nl.tudelft.sem.group2.global.Globals;
import nl.tudelft.sem.group2.scenes.GameScene;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Fuse;
import nl.tudelft.sem.group2.units.Qix;
import nl.tudelft.sem.group2.units.Sparx;
import nl.tudelft.sem.group2.units.SparxDirection;
import nl.tudelft.sem.group2.units.Stix;
import nl.tudelft.sem.group2.units.Unit;

import java.awt.Point;
import java.util.ArrayList;
import java.util.logging.Level;

import static nl.tudelft.sem.group2.LaunchApp.playSound;
import static nl.tudelft.sem.group2.scenes.GameScene.addUnit;
import static nl.tudelft.sem.group2.scenes.GameScene.setAreaTracker;

/**
 * Controller class for the GameScene to implement the MVC.
 */
public class GameController {

    private static final int NANO_SECONDS_PER_SECOND = 100000000;
    // Logger
    private static final Logger LOGGER = Logger.getLogger();
    // Units
    private static Cursor cursor;
    private static Qix qix;
    private static AreaTracker areaTracker;
    private static ScoreCounter scoreCounter;
    private final ArrayList<KeyCode> arrowKeys = new ArrayList<>();
    // Animation timer properties
    private AnimationTimer animationTimer;
    private Stix stix;
    private long previousTime;
    // Boolean that states if the game is running
    private boolean isRunning = false;
    private CollisionHandler collisionHandler;

    /**
     * Constructor for the GameController class.
     */
    public GameController() {
        arrowKeys.add(KeyCode.UP);
        arrowKeys.add(KeyCode.DOWN);
        arrowKeys.add(KeyCode.LEFT);
        arrowKeys.add(KeyCode.RIGHT);
        // Initialize models for scoretracking.
        stix = new Stix();
        areaTracker = new AreaTracker(stix);
        setAreaTracker(areaTracker);
        cursor = new Cursor(Globals.CURSOR_START_X, Globals.CURSOR_START_Y, Globals.BOARD_MARGIN * 2,
                Globals.BOARD_MARGIN * 2, getStix());
        Sparx sparxRight = new Sparx(Globals.CURSOR_START_X, 0, Globals.BOARD_MARGIN * 2,
                Globals.BOARD_MARGIN * 2, SparxDirection.RIGHT);
        Sparx sparxLeft = new Sparx(Globals.CURSOR_START_X, 0, Globals.BOARD_MARGIN * 2,
                Globals.BOARD_MARGIN * 2, SparxDirection.LEFT);
        // Initialize and add units to units set in Gamescene
        qix = new Qix();
        addUnit(cursor);
        addUnit(qix);
        addUnit(sparxRight);
        addUnit(sparxLeft);
        scoreCounter = new ScoreCounter();


        collisionHandler = new CollisionHandler();

        //Animation timer initialization
        previousTime = System.nanoTime();
        createAnimationTimer();

    }

    public static Cursor getCursor() {
        return cursor;
    }

    /**
     * Setter for testing.
     *
     * @param cursor
     */
    public static void setCursor(Cursor cursor) {
        GameController.cursor = cursor;
    }

    public static ScoreCounter getScoreCounter() {
        return scoreCounter;
    }

    public static AreaTracker getAreaTracker() {
        return areaTracker;
    }

    /**
     * Stop animations.
     */
    private void animationTimerStop() {
        animationTimer.stop();
    }

    /**
     * Start animations.
     */
    public void animationTimerStart() {
        animationTimer.start();
    }

    /**
     * Play a game over sound.
     * show game over text,
     * stop the animations.
     */
    private void gameOver() {
        animationTimerStop();
        LaunchApp.scene.setMessageBoxLayoutX(Globals.GAMEOVER_POSITION_X);
        LaunchApp.scene.setMessageLabel(" Game Over! ");

        //Plays game over sound
        playSound("/sounds/Qix_Death.mp3", Globals.GAME_OVER_SOUND_VOLUME);
        LOGGER.log(Level.INFO, "Game Over, player died with a score of "
                + scoreCounter.getTotalScore(), GameScene.class);
    }

    /**
     * TODO Play the game won sound.
     * stop the animations,
     * show that the player has won
     */
    private void gameWon() {
        animationTimerStop();
        playSound("/sounds/Qix_Succes.mp3", Globals.GAME_START_SOUND_VOLUME);
        LaunchApp.scene.setMessageBoxLayoutX(Globals.GAMEWON_POSITION_X);
        LaunchApp.scene.setMessageLabel(" You Won! ");
        LOGGER.log(Level.INFO, "Game Won! Player won with a score of " + scoreCounter.getTotalScore(), GameScene.class);
    }

    public Stix getStix() {
        return stix;
    }

    public void setStix(Stix stix) {
        this.stix = stix;
    }

    /**
     * Setup an animation timer that runs at 300FPS.
     */
    public void createAnimationTimer() {
        // animation timer for handling a loop
        animationTimer = new AnimationTimer() {
            public void handle(long now) {
                if (now - previousTime > NANO_SECONDS_PER_SECOND / 3) {
                    previousTime = now;
                    // draw
                    LaunchApp.scene.draw();

                    if (getScoreCounter().getTotalPercentage() >= getScoreCounter().getTargetPercentage()) {
                        gameWon();
                    }

                    if (collisionHandler.collisions(LaunchApp.scene.getUnits(), stix)) {
                        gameOver();
                    }
                    LaunchApp.scene.updateScorescene(scoreCounter);
                    calculateArea();

                }
            }

        };
    }

    /**
     * When a new area is completed, calculate the new score.
     */
    private void calculateArea() {
        if (areaTracker.getBoardGrid()[cursor.getX()][cursor.getY()] == AreaState.OUTERBORDER
                && !stix.getStixCoordinates().isEmpty()) {
            playSound("/sounds/Qix_Success.mp3", Globals.SUCCESS_SOUND_VOLUME);
            areaTracker.calculateNewArea(new Point(qix.getX(), qix.getY()),
                    cursor.isFast());
            //Remove the Fuse from the gameView when completing an area
            LaunchApp.scene.removeFuse();
        }
    }

    /**
     * Method that handles the action when a key is released.
     *
     * @param e describes which keyevent happened.
     */
    public void keyReleased(KeyEvent e) {
        KeyCode keyCode = e.getCode();
        if (keyCode.equals(cursor.getCurrentMove())) {

            handleFuse();

            cursor.setCurrentMove(null);
        } else if (keyCode.equals(KeyCode.X) || keyCode.equals(KeyCode.Z)) {
            cursor.setDrawing(false);
            cursor.setSpeed(2);
            handleFuse();
        }
    }

    /**
     * handles making fuse and makes it start moving.
     */
    private void handleFuse() {
        if (stix.getStixCoordinates().contains(new Point(cursor.getX(), cursor.getY()))) {
            boolean fuseExists = false;
            for (Unit unit : LaunchApp.scene.getUnits()) {
                if (unit instanceof Fuse) {
                    fuseExists = true;
                    ((Fuse) unit).setMoving(true);
                }
            }
            if (!fuseExists) {
                addUnit(
                        new Fuse((int) stix.getStixCoordinates().getFirst().getX(),
                                (int) stix.getStixCoordinates().getFirst().getY(),
                                Globals.FUSE_WIDTH,
                                Globals.FUSE_HEIGHT, stix));
            }
            cursor.setCurrentMove(null);
        }
    }

    /**
     * Method that handles the action when a key is pressed.
     *
     * @param e describes which keyevent happened.
     */
    public void keyPressed(KeyEvent e) {

        if (e.getCode().equals(KeyCode.SPACE) && !isRunning) {
            playSound("/sounds/Qix_NewLife.mp3", Globals.GAME_START_SOUND_VOLUME);
            animationTimerStart();
            LOGGER.log(Level.INFO, "Game started succesfully", this.getClass());
            isRunning = true;
            LaunchApp.scene.setMessageLabel("");
        } else if (arrowKeys.contains(e.getCode())) {
            if (cursor.isDrawing()) {
                for (Unit unit : LaunchApp.scene.getUnits()) {
                    if (unit instanceof Fuse) {
                        ((Fuse) unit).setMoving(false);
                    }
                }
            }
            cursor.setCurrentMove(e.getCode());
        } else if (e.getCode().equals(KeyCode.X)) {
            if (stix.getStixCoordinates() != null && !stix.getStixCoordinates().isEmpty()) {
                if (!cursor.isFast()) {
                    cursor.setSpeed(1);
                    cursor.setDrawing(true);
                    cursor.setFast(false);
                }
            } else {
                cursor.setSpeed(1);
                cursor.setDrawing(true);
                cursor.setFast(false);
            }
        } else if (e.getCode().equals(KeyCode.Z)) {
            cursor.setSpeed(2);
            cursor.setDrawing(true);
            cursor.setFast(true);
        }
    }

    /**
     * getter for testing.
     *
     * @return boolean isRunning
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * getter for testing.
     *
     * @return animationTimer
     */
    public AnimationTimer getAnimationTimer() {
        return animationTimer;
    }

    /**
     * setter for testing.
     */
    public void setPreviousTime(long previousTime) {
        this.previousTime = previousTime;
    }
}
