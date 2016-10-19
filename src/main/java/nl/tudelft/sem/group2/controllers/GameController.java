package nl.tudelft.sem.group2.controllers;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.Logger;
import nl.tudelft.sem.group2.collisions.CollisionHandler;
import nl.tudelft.sem.group2.global.Globals;
import nl.tudelft.sem.group2.scenes.GameScene;
import nl.tudelft.sem.group2.sound.SoundHandler;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Fuse;
import nl.tudelft.sem.group2.units.Qix;
import nl.tudelft.sem.group2.units.Sparx;
import nl.tudelft.sem.group2.units.SparxDirection;
import nl.tudelft.sem.group2.units.Stix;
import nl.tudelft.sem.group2.units.Unit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Level;
import java.awt.Point;

/**
 * Controller class for the GameScene to implement the MVC.
 */
public final class GameController {

    // Logger
    private static final Logger LOGGER = Logger.getLogger();
    private static GameController gameController;
    // Animation timer properties
    private AnimationTimer animationTimer;
    // Units
    private ArrayList<Cursor> cursors;
    private Qix qix;
    private AreaTracker areaTracker;
    private Set<Unit> units;

    private long previousTime;
    // Boolean that states if the game is running
    private boolean isRunning = false;
    private CollisionHandler collisionHandler;
    private GameScene gameScene;

    private LinkedList<KeyCode> cursorFastMoveKey = new LinkedList<>();
    private LinkedList<KeyCode> cursorSlowMoveKey = new LinkedList<>();

    /**
     * Constructor for the GameController class.
     */
    private GameController() {
        // Initialize models for scoretracking.

        areaTracker = new AreaTracker();


        Group group = new Group();
        gameScene = new GameScene(group, Color.BLACK);

        collisionHandler = new CollisionHandler();

        //Animation timer initialization
        previousTime = System.nanoTime();
        createAnimationTimer();

    }

    /**
     * returns the single instance of GameController.
     *
     * @return the only GameController
     */
    public static GameController getInstance() {
        //if (gameController == null) {
        // Put lock on class since it we do not want to instantiate it twice
        synchronized (GameController.class) {
            // Check if logger is in the meanwhile not already instantiated.
            if (gameController == null) {
                gameController = new GameController();
            }
        }
        //}
        return gameController;
    }

    /**
     * Methods which sets the current gameController to null so a new one can be instantiated.
     */
    public static void deleteGameController() {
        GameController.gameController = null;
    }

    /**
     * Method which generates two cursor, two sparx and a qix which is used for a single player match.
     * This also binds the correct key controls to the right cursor.
     */
    public void makeCursors() {

        cursors = new ArrayList<>();
        //first
        createFirstCursor();

        //second
        Stix stix2 = new Stix();
        cursors.add(new Cursor(new Point(0, 0), Globals.BOARD_MARGIN * 2,
                Globals.BOARD_MARGIN * 2, areaTracker, stix2, Color.RED, Globals.LIVES));
        cursors.get(1).addKey(KeyCode.W);
        cursors.get(1).addKey(KeyCode.S);
        cursors.get(1).addKey(KeyCode.A);
        cursors.get(1).addKey(KeyCode.D);
        cursorFastMoveKey.add(KeyCode.Z);
        cursorSlowMoveKey.add(KeyCode.X);


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
    }

    /**
     * Method which generates one cursor, two sparx and a qix which is used for a single player match.
     * This also binds the correct key controls to the right cursor.
     */
    public void makeCursor() {

        cursors = new ArrayList<>();
        //first
        createFirstCursor();

        Sparx sparxRight = new Sparx(Globals.CURSOR_START_X, 0, Globals.BOARD_MARGIN * 2,
                Globals.BOARD_MARGIN * 2, areaTracker, SparxDirection.RIGHT);
        Sparx sparxLeft = new Sparx(Globals.CURSOR_START_X, 0, Globals.BOARD_MARGIN * 2,
                Globals.BOARD_MARGIN * 2, areaTracker, SparxDirection.LEFT);
        // Initialize and add units to units set in Gamescene
        qix = new Qix(areaTracker);
        addUnit(cursors.get(0));
        addUnit(qix);
        addUnit(sparxRight);
        addUnit(sparxLeft);
    }

    /**
     * Creates first cursor to avoid code duplication.
     */
    private void createFirstCursor() {
        Stix stix = new Stix();
        cursors.add(new Cursor(new Point(Globals.CURSOR_START_X, Globals.CURSOR_START_Y), Globals.BOARD_MARGIN * 2,
                Globals.BOARD_MARGIN * 2, areaTracker, stix, Color.YELLOW, Globals.LIVES));
        cursors.get(0).addKey(KeyCode.UP);
        cursors.get(0).addKey(KeyCode.DOWN);
        cursors.get(0).addKey(KeyCode.LEFT);
        cursors.get(0).addKey(KeyCode.RIGHT);
        cursorFastMoveKey.add(KeyCode.O);
        cursorSlowMoveKey.add(KeyCode.I);
    }

    /**
     * Adds a cursor to the cursors array which can at most contain two cursors.
     *
     * @param cursor cursor to add to cursor array.
     */
    public void addCursor(Cursor cursor) {
        if (cursors.size() < 2 && cursor != null) {
            cursors.add(cursor);
        }
    }

    /*****
     * Units.
     *****/

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
        gameScene.setMessageBoxLayoutX(Globals.GAMEOVER_POSITION_X);
        gameScene.setMessageLabel(" Game Over! ");

        //Plays game over sound

        String score = "";
        for (Cursor cursor : cursors) {
            score += cursor.getScoreCounter() + ", ";
            LOGGER.log(Level.INFO, "Game Over, player died with a score of "
                    + cursor.getScoreCounter().getTotalScore(), GameController.class);
        }
    }

    /**
     * TODO Play the game won sound.
     * stop the animations,
     * show that the player has won
     */
    private void gameWon() {
        animationTimerStop();
        new SoundHandler().playSound("/sounds/Qix_Succes.mp3", Globals.GAME_START_SOUND_VOLUME);
        gameScene.setMessageBoxLayoutX(Globals.GAMEWON_POSITION_X);
        gameScene.setMessageLabel(" You Won! ");

        //check high score
        int score = 0;
        for (Cursor cursor : cursors) {
            if (cursor.getScoreCounter().getTotalScore() > score) {
                score = cursor.getScoreCounter().getTotalScore();
            }
        }
        LOGGER.log(Level.INFO, "Game Won! Player won with a score of " + score, GameScene.class);
    }


    /****** Key events ******/

    /**
     * Setup an animation timer that runs at 300FPS.
     */
    public void createAnimationTimer() {
        // animation timer for handling a loop
        animationTimer = new AnimationTimer() {
            public void handle(long now) {
                if (now - previousTime > Globals.NANO_SECONDS_PER_SECOND / 3) {
                    previousTime = now;
                    // draw
                    gameScene.draw();

                    for (Cursor cursor : cursors) {
                        if (cursor.getScoreCounter().hasWon()) {
                            gameWon();
                        }
                    }

                    for (Cursor cursor : cursors) {
                        if (collisionHandler.collisions(getUnits(), cursor.getStix())) {
                            cursor.cursorDied();
                            new SoundHandler().playSound("/sounds/Qix_Death.mp3", Globals.GAME_OVER_SOUND_VOLUME);
                            if (cursor.getLives() == 0) {
                                gameOver();
                            }
                        }
                    }
                    for (Cursor cursor : cursors) {
                        cursor.calculateArea(qix);
                    }
                }
            }

        };
    }

    /**
     * Method that handles the action when a key is released.
     *
     * @param e describes which keyevent happened.
     */
    public void keyReleased(KeyEvent e) {
        KeyCode keyCode = e.getCode();
        for (int i = 0; i < cursors.size(); i++) {
            Cursor cursor = cursors.get(i);
            if (keyCode.equals(cursor.getCurrentMove())) {
                cursor.handleFuse();
                cursor.setCurrentMove(null);
            } else if (keyCode.equals(cursorFastMoveKey.get(i)) || keyCode.equals(cursorSlowMoveKey.get(i))) {
                cursor.setDrawing(false);
                cursor.setSpeed(2);
                cursor.handleFuse();
            }
        }
    }

    /**
     * Method that handles the action when a key is pressed.
     *
     * @param e describes which keyevent happened.
     */
    public void keyPressed(KeyEvent e) {

        if (e.getCode().equals(KeyCode.SPACE) && !isRunning) {
            new SoundHandler().playSound("/sounds/Qix_NewLife.mp3", Globals.GAME_START_SOUND_VOLUME);
            animationTimerStart();
            LOGGER.log(Level.INFO, "Game started succesfully", this.getClass());
            isRunning = true;
            gameScene.setMessageLabel("");
        } else {
            for (int i = 0; i < cursors.size(); i++) {
                Cursor cursor = cursors.get(i);
                if (cursor.getArrowKeys().contains(e.getCode())) {
                    if (cursor.isDrawing() && cursor.getFuse() != null) {
                        cursor.getFuse().setMoving(false);
                    }
                    cursor.setCurrentMove(e.getCode());
                } else if (e.getCode().equals(cursorSlowMoveKey.get(i))) {
                    if (cursor.getStix().getStixCoordinates() != null
                            && !cursor.getStix().getStixCoordinates().isEmpty()) {
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
                } else if (e.getCode().equals(cursorFastMoveKey.get(i))) {
                    cursor.setSpeed(2);
                    cursor.setDrawing(true);
                    cursor.setFast(true);

                }
            }
        }
    }

    //Getters

    public AreaTracker getAreaTracker() {
        return areaTracker;
    }

    public GameScene getScene() {
        return gameScene;
    }

    public void setGameScene(GameScene scene) {
        this.gameScene = scene;
    }

    public ArrayList<Cursor> getCursors() {
        return cursors;
    }

    public Set<Unit> getUnits() {
        return units;
    }
}
