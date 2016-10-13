package nl.tudelft.sem.group2.controllers;

import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.LaunchApp;
import nl.tudelft.sem.group2.Logger;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import static nl.tudelft.sem.group2.LaunchApp.playSound;

/**
 * Controller class for the GameScene to implement the MVC.
 */
public class GameController {

    private static final int NANO_SECONDS_PER_SECOND = 100000000;
    // Logger
    private static final Logger LOGGER = Logger.getLogger();
    // Units
    private List<Cursor> cursors;
    private Qix qix;
    private AreaTracker areaTracker;
    // Animation timer properties
    private AnimationTimer animationTimer;
    private Set<Unit> units;
    private long previousTime;
    // Boolean that states if the game is running
    private boolean isRunning = false;
    private CollisionHandler collisionHandler;

    /**
     * Constructor for the GameController class.
     */
    public GameController() {
        // Initialize models for scoretracking.

        areaTracker = new AreaTracker();

        makeCursors();


        Sparx sparxRight = new Sparx(Globals.CURSOR_START_X, 0, Globals.BOARD_MARGIN * 2,
                Globals.BOARD_MARGIN * 2, areaTracker, SparxDirection.RIGHT);
        Sparx sparxLeft = new Sparx(Globals.CURSOR_START_X, 0, Globals.BOARD_MARGIN * 2,
                Globals.BOARD_MARGIN * 2, areaTracker, SparxDirection.LEFT);
        // Initialize and add units to units set in Gamescene
        qix = new Qix(areaTracker);
        addUnit(cursors.get(0));
        addUnit(cursors.get(1));
        addUnit(qix);
        addUnit(sparxRight);
        addUnit(sparxLeft);


        collisionHandler = new CollisionHandler();

        //Animation timer initialization
        previousTime = System.nanoTime();
        createAnimationTimer();

    }

    private void makeCursors() {

        cursors = new ArrayList<>();
        //first
        Stix stix = new Stix();
        cursors.add(new Cursor(Globals.CURSOR_START_X, Globals.CURSOR_START_Y, Globals.BOARD_MARGIN * 2,
                Globals.BOARD_MARGIN * 2, areaTracker, stix, Color.YELLOW));
        cursors.get(0).addKey(KeyCode.UP);
        cursors.get(0).addKey(KeyCode.DOWN);
        cursors.get(0).addKey(KeyCode.LEFT);
        cursors.get(0).addKey(KeyCode.RIGHT);

        //second
        Stix stix2 = new Stix();
        cursors.add(new Cursor(0, 0, Globals.BOARD_MARGIN * 2,
                Globals.BOARD_MARGIN * 2, areaTracker, stix2, Color.RED));
        cursors.get(1).addKey(KeyCode.W);
        cursors.get(1).addKey(KeyCode.S);
        cursors.get(1).addKey(KeyCode.A);
        cursors.get(1).addKey(KeyCode.D);
    }

    public Cursor getCursor() {
        return cursors.get(0);
    }

    /***** Units *****/
    /**
     * @return units of the board
     */
    public Set<Unit> getUnits() {
        return units;
    }

    /**
     * Add a unit.
     *
     * @param unit unit to add
     */
    public void addUnit(Unit unit) {
        if (units == null) {
            units = new HashSet<>();
        }
        if (unit instanceof Fuse) {
            for (Unit unit1 : units) {
                if (unit1 instanceof Fuse) {
                    return;
                }
            }
        }
        units.add(unit);
    }

    /****** AnimationTime ******/

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
     *
     * @param previousTime setter
     */
    public void setPreviousTime(long previousTime) {
        this.previousTime = previousTime;
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
        String score = "";
        for (Cursor cursor : cursors) {
            score += cursor.getScoreCounter() + ", ";
        }
        LOGGER.log(Level.INFO, "Game Over, player(s) died with a score of "
                + score, GameScene.class);
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

        //check high score
        int score = 0;
        for (Cursor cursor : cursors) {
            if (cursor.getScoreCounter().getTotalScore() > score) {
                score = cursor.getScoreCounter().getTotalScore();
            }
        }
        LOGGER.log(Level.INFO, "Game Won! Player won with a score of " + score, GameScene.class);
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

                    for (Cursor cursor : cursors) {
                        if (cursor.getScoreCounter().hasWon()) {
                            gameWon();
                        }
                    }


                    for (Cursor cursor : cursors) {
                        if (collisionHandler.collisions(getUnits(), cursor.getStix())) {
                            gameOver();
                        }
                    }
                    //LaunchApp.scene.updateScorescene(scoreCounter);

                    for (Cursor cursor : cursors) {
                        cursor.calculateArea(qix);
                    }

                }
            }

        };
    }


    /****** Key events ******/

    /**
     * Method that handles the action when a key is released.
     *
     * @param e describes which keyevent happened.
     */
    public void keyReleased(KeyEvent e) {
        KeyCode keyCode = e.getCode();
        if (keyCode.equals(cursors.get(0).getCurrentMove())) {

            cursors.get(0).handleFuse();

            cursors.get(0).setCurrentMove(null);
        } else if (keyCode.equals(KeyCode.X) || keyCode.equals(KeyCode.Z)) {
            cursors.get(0).setDrawing(false);
            cursors.get(0).setSpeed(2);
            cursors.get(0).handleFuse();
        } else if (keyCode.equals(cursors.get(1).getCurrentMove())) {

            cursors.get(1).handleFuse();

            cursors.get(1).setCurrentMove(null);
        } else if (keyCode.equals(KeyCode.O) || keyCode.equals(KeyCode.I)) {
            cursors.get(1).setDrawing(false);
            cursors.get(1).setSpeed(2);
            cursors.get(1).handleFuse();
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
            /*** first cursor ***/
        } else if (cursors.get(0).getArrowKeys().contains(e.getCode())) {
            if (cursors.get(0).isDrawing()) {
                if (cursors.get(0).getFuse() != null) {
                    cursors.get(0).getFuse().setMoving(false);
                }
            }
            cursors.get(0).setCurrentMove(e.getCode());
        } else if (e.getCode().equals(KeyCode.X)) {
            if (cursors.get(0).getStix().getStixCoordinates() != null && !cursors.get(0).getStix().getStixCoordinates().isEmpty()) {
                if (!cursors.get(0).isFast()) {
                    cursors.get(0).setSpeed(1);
                    cursors.get(0).setDrawing(true);
                    cursors.get(0).setFast(false);
                }
            } else {
                cursors.get(0).setSpeed(1);
                cursors.get(0).setDrawing(true);
                cursors.get(0).setFast(false);
            }
        } else if (e.getCode().equals(KeyCode.Z)) {
            cursors.get(0).setSpeed(2);
            cursors.get(0).setDrawing(true);
            cursors.get(0).setFast(true);

            /*** second cursor ***/
        } else if (cursors.get(1).getArrowKeys().contains(e.getCode())) {
            if (cursors.get(1).isDrawing()) {
                if (cursors.get(1).getFuse() != null) {
                    cursors.get(1).getFuse().setMoving(false);
                }
            }
            cursors.get(1).setCurrentMove(e.getCode());
        } else if (e.getCode().equals(KeyCode.O)) {
            if (cursors.get(1).getStix().getStixCoordinates() != null && !cursors.get(1).getStix().getStixCoordinates().isEmpty()) {
                if (!cursors.get(1).isFast()) {
                    cursors.get(1).setSpeed(1);
                    cursors.get(1).setDrawing(true);
                    cursors.get(1).setFast(false);
                }
            } else {
                cursors.get(1).setSpeed(1);
                cursors.get(1).setDrawing(true);
                cursors.get(1).setFast(false);
            }
        } else if (e.getCode().equals(KeyCode.I)) {
            cursors.get(1).setSpeed(2);
            cursors.get(1).setDrawing(true);
            cursors.get(1).setFast(true);
        }
    }

    /**
     * AreaTracker.
     * @return the area tracker
     */
    public AreaTracker getAreaTracker() {
        return areaTracker;
    }
}
