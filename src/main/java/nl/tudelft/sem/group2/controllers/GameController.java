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
import nl.tudelft.sem.group2.powerups.PowerEat;
import nl.tudelft.sem.group2.powerups.PowerLife;
import nl.tudelft.sem.group2.powerups.PowerSpeed;
import nl.tudelft.sem.group2.powerups.PowerUpType;
import nl.tudelft.sem.group2.powerups.Powerup;
import nl.tudelft.sem.group2.powerups.PowerupEvent;
import nl.tudelft.sem.group2.scenes.GameScene;
import nl.tudelft.sem.group2.sound.SoundHandler;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Fuse;
import nl.tudelft.sem.group2.units.Qix;
import nl.tudelft.sem.group2.units.Sparx;
import nl.tudelft.sem.group2.units.SparxDirection;
import nl.tudelft.sem.group2.units.Stix;
import nl.tudelft.sem.group2.units.Unit;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

/**
 * Controller class for the GameScene to implement the MVC.
 */
public final class GameController {

    // Logger
    private static final Logger LOGGER = Logger.getLogger();
    //TODO MAKE STARTUP ARGUMENT
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
     * only used for testing.
     */
    public static void deleteGameController() {
        GameController.gameController = null;
    }

    /**
     * Method which generates a cursor, a sparx and a qix which is used for a single player match.
     * This also binds the correct key controls to the right cursor.
     *
     * @param multiplayer if true 2 cursors are set
     */
    public void makeCursors(boolean multiplayer) {

        cursors = new ArrayList<>();
        //first
        Stix stix = new Stix();
        cursors.add(new Cursor(new Point(Globals.CURSOR_START_X, Globals.CURSOR_START_Y), Globals.BOARD_MARGIN * 2,
                Globals.BOARD_MARGIN * 2, areaTracker, stix, Color.YELLOW, Globals.LIVES));
        cursors.get(0).addKey(KeyCode.UP);
        cursors.get(0).addKey(KeyCode.DOWN);
        cursors.get(0).addKey(KeyCode.LEFT);
        cursors.get(0).addKey(KeyCode.RIGHT);
        cursorFastMoveKey.add(KeyCode.O);
        cursorSlowMoveKey.add(KeyCode.I);
        if (multiplayer) {
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
            addUnit(cursors.get(1));
        }
        Sparx sparxLeft = new Sparx(Globals.CURSOR_START_X, 0, Globals.BOARD_MARGIN * 2,
                Globals.BOARD_MARGIN * 2, areaTracker, SparxDirection.LEFT);
        Sparx sparxRight = new Sparx(Globals.CURSOR_START_X, 0, Globals.BOARD_MARGIN * 2,
                Globals.BOARD_MARGIN * 2, areaTracker, SparxDirection.RIGHT);
        // Initialize and add units to units set in Gamescene
        qix = new Qix(areaTracker);
        addUnit(cursors.get(0));
        addUnit(qix);
        addUnit(sparxRight);
        addUnit(sparxLeft);
    }

    /**
     * Adds a cursor to the cursors array which can at most contain two cursors.
     *
     * @param cursor cursor to add to cursor array.
     */
    public void addCursor(Cursor cursor) {
        if (cursor != null && cursors.size() < 2) {
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

                    handlePowerups();
                    spawnPowerup();
                }
            }
        };
    }

    private void checkSparx() {
        int nSparx = 0;
        for (Unit u : units) {
            if (u instanceof Sparx) {
                nSparx++;
            }
        }

        while (nSparx < 2) {
            for (Cursor cursor : cursors) {
                int[] coordinates = areaTracker.findPowerupLocation(
                        AreaTracker.oppositeQuadrant(cursor.getX(), cursor.getY()));
                Sparx sparx = new Sparx(coordinates[0], coordinates[1], Globals.BOARD_MARGIN * 2,
                        Globals.BOARD_MARGIN * 2, areaTracker, SparxDirection.randomDirection());
                addUnit(sparx);
                nSparx++;
            }
        }
    }

    private void handlePowerups() {
        applyPowerups();

        Iterator<Unit> iter = units.iterator();
        //code to remove powerups from the board after a certain amount of time
        while (iter.hasNext()) {
            Unit unit = iter.next();
            if (unit instanceof Powerup) {
                Powerup powerup = (Powerup) unit;
                powerup.decreaseDuration();
                if (powerup.getDuration() <= 0) {
                    iter.remove();
                }
            }
        }

        PowerupEvent powerupEvent = collisionHandler.powerUpCollisions(units);
        if (powerupEvent != null) {
            Cursor cursor = powerupEvent.getCursor();
            switch (powerupEvent.getPowerUpType()) {
                case NONE:
                    return;
                case LIFE:
                    cursor.addLife();
                    return;
                case EAT:
                    cursor.setCurrentPowerup(PowerUpType.EAT);
                    cursor.setPowerUpDuration(Globals.POWERUP_EAT_DURATION);
                    return;
                case SPEED:
                    cursor.setCurrentPowerup(PowerUpType.SPEED);
                    cursor.setPowerUpDuration(Globals.POWERUP_SPEED_DURATION);
            }
        }
    }

    /**
     * Spawns a new powerup at random and when none is active yet.
     */
    private void spawnPowerup() {
        double rand = ThreadLocalRandom.current().nextDouble();
        if (rand < Globals.POWERUP_THRESHOLD && !powerUpActive()) {

            for (Cursor cursor : cursors) {

                int quadrant = AreaTracker.oppositeQuadrant(cursor.getX(), cursor.getY());

                int[] coordinates = areaTracker.findPowerupLocation(quadrant);
                Powerup powerup = null;
                Map<PowerUpType, Powerup> powerupMap = new HashMap<>();
                powerupMap.put(PowerUpType.EAT, new PowerEat(coordinates[0], coordinates[1],
                        Globals.BOARD_MARGIN * 2, Globals.BOARD_MARGIN * 2, areaTracker));
                powerupMap.put(PowerUpType.LIFE, new PowerLife(coordinates[0], coordinates[1],
                        Globals.BOARD_MARGIN * 2, Globals.BOARD_MARGIN * 2, areaTracker));
                powerupMap.put(PowerUpType.SPEED, new PowerSpeed(coordinates[0], coordinates[1],
                        Globals.BOARD_MARGIN * 2, Globals.BOARD_MARGIN * 2, areaTracker));
                powerup = powerupMap.get(PowerUpType.randomType());
                if (powerup == null) {
                    return;
                }
                addUnit(powerup);
            }
        }
    }

    /**
     * @return true if a power up is active
     */
    private boolean powerUpActive() {
        for (Cursor cursor : cursors) {
            if (cursor.hasPowerUp()) {
                return true;
            }
        }

        for (Unit u : units) {
            if (u instanceof Powerup) {
                return true;
            }
        }
        return false;
    }

    private void applyPowerups() {
        for (Cursor cursor : cursors) {
            if (cursor.hasPowerUp() && cursor.getPowerUpDuration() <= 0) {
                cursor.setCurrentPowerup(PowerUpType.NONE);
            }

            if (cursor.hasPowerUp() && cursor.getPowerUpDuration() > 0) {
                cursor.decrementPowerupDuration();
            }
        }
    }


//    /**
//     * When a new area is completed, calculate the new score.
//     */
//    private void calculateArea() {
//        if (areaTracker.getBoardGrid()[cursor.getX()][cursor.getY()] == AreaState.OUTERBORDER
//                && !stix.getStixCoordinates().isEmpty()) {
//            new SoundHandler().playSound("/sounds/Qix_Success.mp3", Globals.SUCCESS_SOUND_VOLUME);
//            areaTracker.calculateNewArea(new Point(qix.getX(), qix.getY()),
//                    cursor.isFast());
//            areaTracker.updateScoreCounter(cursor.isFast());
//            //Remove the Fuse from the gameView when completing an area
//            gameScene.removeFuse();
//        }
//    }

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
        initializeCursorSpeed();
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
                    if (!stixNotEmpty(i) || !cursor.isFast()) {
                        cursor.setSpeed(1);
                        cursor.setDrawing(true);
                        cursor.setFast(false);
                    }
                } else if (e.getCode().equals(cursorFastMoveKey.get(i))) {
                    cursor.setSpeed(2);
                    cursor.setDrawing(true);
                    cursor.setFast(true);
                }
                if (cursor.getCurrentPowerup() == PowerUpType.SPEED) {
                    cursor.setSpeed(cursor.getSpeed() + 1);
                }
            }
        }
    }

    private void initializeCursorSpeed() {
        for (Cursor cursor : cursors) {
            if (cursor.isFast() || !cursor.isDrawing()) {
                cursor.setSpeed(Globals.CURSOR_FAST);
            } else {
                cursor.setSpeed(Globals.CURSOR_SLOW);
            }
        }
    }

    /**
     * getter for testing.
     *
     * @return boolean isRunning
     */
    boolean isRunning() {
        return isRunning;
    }

    private boolean stixNotEmpty(int cursorIndex) {
        return cursors.get(cursorIndex).getStix().getStixCoordinates() != null
                && !cursors.get(cursorIndex).getStix().getStixCoordinates().isEmpty();
    }

    //Getters

    /**
     * AreaTracker.
     *
     * @return the area tracker
     */
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

    /**
     * only used for testing.
     *
     * @param units setter.
     */
    public static void setUnits(Set<Unit> units) {
        getInstance().units = units;
    }

    /**
     * removes a unit of the list of units.
     *
     * @param unit the unit to be removed
     */
    public void removeUnit(Unit unit) {
        units.remove(unit);
        checkSparx();
    }

    public LinkedList<KeyCode> getCursorFastMoveKey() {
        return cursorFastMoveKey;
    }

    public void setCursorFastMoveKey(LinkedList<KeyCode> cursorFastMoveKey) {
        this.cursorFastMoveKey = cursorFastMoveKey;
    }

    public LinkedList<KeyCode> getCursorSlowMoveKey() {
        return cursorSlowMoveKey;
    }

    public void setCursorSlowMoveKey(LinkedList<KeyCode> cursorSlowMoveKey) {
        this.cursorSlowMoveKey = cursorSlowMoveKey;
    }
}
