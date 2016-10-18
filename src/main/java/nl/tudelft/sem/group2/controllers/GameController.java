package nl.tudelft.sem.group2.controllers;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.AreaState;
import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.Logger;
import nl.tudelft.sem.group2.ScoreCounter;
import nl.tudelft.sem.group2.collisions.CollisionHandler;
import nl.tudelft.sem.group2.global.Globals;
import nl.tudelft.sem.group2.powerups.PowerEat;
import nl.tudelft.sem.group2.powerups.PowerLife;
import nl.tudelft.sem.group2.powerups.PowerSpeed;
import nl.tudelft.sem.group2.powerups.PowerUpType;
import nl.tudelft.sem.group2.powerups.Powerup;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

/**
 * Controller class for the GameScene to implement the MVC.
 */
public final class GameController {

    private static final int NANO_SECONDS_PER_SECOND = 100000000;
    // Logger
    private static final Logger LOGGER = Logger.getLogger();
    //TODO MAKE STARTUP ARGUMENT
    private static final int LIVES = 3;
    private static GameController gameController;
    // Animation timer properties
    private static AnimationTimer animationTimer;
    // Units
    private static Cursor cursor;
    private static Qix qix;
    // Models for score tracking
    private static AreaTracker areaTracker;
    private static ScoreCounter scoreCounter;
    private static Set<Unit> units;
    private final ArrayList<KeyCode> arrowKeys = new ArrayList<>();
    // Animation timer properties
    private Stix stix;
    private long previousTime;
    // Boolean that states if the game is running
    private boolean isRunning = false;
    private CollisionHandler collisionHandler;
    private GameScene gameScene;

    /**
     * Constructor for the GameController class.
     */
    private GameController() {
        arrowKeys.add(KeyCode.UP);
        arrowKeys.add(KeyCode.DOWN);
        arrowKeys.add(KeyCode.LEFT);
        arrowKeys.add(KeyCode.RIGHT);
        // Initialize models for scoretracking.
        stix = new Stix();
        areaTracker = new AreaTracker(stix);
        scoreCounter = new ScoreCounter();

        units = new HashSet<>();

        // Initialize and add units to units set in Gamescene
        qix = new Qix(areaTracker);
        cursor = new Cursor(Globals.CURSOR_START_X, Globals.CURSOR_START_Y, Globals.BOARD_MARGIN * 2,
                Globals.BOARD_MARGIN * 2, stix, areaTracker, LIVES);
        Sparx sparxRight = new Sparx(Globals.CURSOR_START_X, 0, Globals.BOARD_MARGIN * 2,
                Globals.BOARD_MARGIN * 2, SparxDirection.RIGHT, areaTracker);
        Sparx sparxLeft = new Sparx(Globals.CURSOR_START_X, 0, Globals.BOARD_MARGIN * 2,
                Globals.BOARD_MARGIN * 2, SparxDirection.LEFT, areaTracker);

        Group group = new Group();
        gameScene = new GameScene(group, Color.BLACK);
        addUnit(cursor);
        addUnit(qix);
        addUnit(sparxRight);
        addUnit(sparxLeft);


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
        if (gameController == null) {
            // Put lock on class since it we do not want to instantiate it twice
            synchronized (GameController.class) {
                // Check if logger is in the meanwhile not already instantiated.
                if (gameController == null) {
                    gameController = new GameController();
                }
            }
        }
        return gameController;
    }

    /**
     * only used for testing.
     *
     * @param gameController setter.
     */
    public static void setGameController(GameController gameController) {
        GameController.gameController = gameController;
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
    private void animationTimerStart() {
        animationTimer.start();
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
        new SoundHandler().playSound("/sounds/Qix_Death.mp3", Globals.GAME_OVER_SOUND_VOLUME);
        LOGGER.log(Level.INFO, "Game Over, player died with a score of "
                + scoreCounter.getTotalScore(), GameController.class);
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
        LOGGER.log(Level.INFO, "Game Won! Player won with a score of " + scoreCounter.getTotalScore(), GameScene.class);
    }

    //GETTERS
    public AreaTracker getAreaTracker() {
        return areaTracker;
    }

    public Cursor getCursor() {
        return cursor;
    }

    //SETTERS
    public void setCursor(Cursor cursor) {
        GameController.cursor = cursor;
    }

    public ScoreCounter getScoreCounter() {
        return scoreCounter;
    }

    public GameScene getScene() {
        return gameScene;
    }

    public Stix getStix() {
        return stix;
    }

    public void setStix(Stix stix) {
        this.stix = stix;
    }

    /**
     * only used for testing.
     *
     * @param gameScene setter
     */
    public void setGameScene(GameScene gameScene) {
        this.gameScene = gameScene;
    }

    /**
     * Setup an animation timer that runs at 300FPS.
     */
    private void createAnimationTimer() {
        // animation timer for handling a loop
        animationTimer = new AnimationTimer() {
            public void handle(long now) {
                if (now - previousTime > NANO_SECONDS_PER_SECOND / 3) {
                    previousTime = now;
                    // draw
                    gameScene.draw();

                    if (getScoreCounter().getTotalPercentage() >= getScoreCounter().getTargetPercentage()) {
                        gameWon();
                    }

                    if (collisionHandler.collisions(units, stix)) {
                        cursor.cursorDied();
                        if (cursor.getLives() == 0) {
                            gameOver();
                        }
                    }
                    gameScene.updateScorescene(scoreCounter, cursor);
                    calculateArea();
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
            int[] coordinates = areaTracker.findPowerupLocation(cursor.oppositeQuadrant());
            Sparx sparx = new Sparx(coordinates[0], coordinates[1], Globals.BOARD_MARGIN * 2,
                    Globals.BOARD_MARGIN * 2, SparxDirection.randomDirection(), areaTracker);
            addUnit(sparx);
            nSparx++;
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
                if (powerup.getDuration() < 0) {
                    iter.remove();
                }
            }
        }

        PowerUpType powerUpType = collisionHandler.powerUpCollisions(units);
        switch (powerUpType) {
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

    /**
     * Spawns a new powerup at random and when none is active yet.
     */
    private void spawnPowerup() {
        double rand = ThreadLocalRandom.current().nextDouble();
        if (rand < Globals.POWERUP_THRESHOLD + 1 && !powerUpActive()) {
            PowerUpType type = PowerUpType.randomType();
            int quadrant = cursor.oppositeQuadrant();

            int[] coordinates = areaTracker.findPowerupLocation(quadrant);

            Powerup powerup = null;
            switch (type) {
                case EAT:
                    powerup = new PowerEat(coordinates[0], coordinates[1],
                            Globals.BOARD_MARGIN * 2, Globals.BOARD_MARGIN * 2, areaTracker);
                    break;
                case LIFE:
                    powerup = new PowerLife(coordinates[0], coordinates[1],
                            Globals.BOARD_MARGIN * 2, Globals.BOARD_MARGIN * 2, areaTracker);
                    break;
                case SPEED:
                    powerup = new PowerSpeed(coordinates[0], coordinates[1],
                            Globals.BOARD_MARGIN * 2, Globals.BOARD_MARGIN * 2, areaTracker);
                    break;
                case NONE:
                    return;
            }
            LOGGER.log(Level.INFO, powerup.toString() + " spawned at (" + powerup.getX() + ", "
                    + powerup.getY() + ")", GameController.this.getClass());
            //powerup = new PowerEat(coordinates[0], coordinates[1],
            //        Globals.BOARD_MARGIN * 2, Globals.BOARD_MARGIN * 2, areaTracker);
            addUnit(powerup);
        }
    }

    /**
     * @return true if a power up is active
     */
    private boolean powerUpActive() {
        if (cursor.getCurrentPowerup() != PowerUpType.NONE) {
            return true;
        }

        for (Unit u : units) {
            if (u instanceof Powerup) {
                return true;
            }
        }
        return false;
    }

    private void applyPowerups() {
        if (cursor.hasPowerUp() && cursor.getPowerUpDuration() <= 0) {
            cursor.setCurrentPowerup(PowerUpType.NONE);
        }

        if (cursor.hasPowerUp() && cursor.getPowerUpDuration() > 0) {
            cursor.decrementPowerupDuration();
        }
    }



    /**
     * When a new area is completed, calculate the new score.
     */
    private void calculateArea() {
        if (areaTracker.getBoardGrid()[cursor.getX()][cursor.getY()] == AreaState.OUTERBORDER
                && !stix.getStixCoordinates().isEmpty()) {
            new SoundHandler().playSound("/sounds/Qix_Success.mp3", Globals.SUCCESS_SOUND_VOLUME);
            areaTracker.calculateNewArea(new Point(qix.getX(), qix.getY()),
                    cursor.isFast());
            areaTracker.updateScoreCounter(cursor.isFast());
            //Remove the Fuse from the gameView when completing an area
            gameScene.removeFuse();
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
            for (Unit unit : units) {
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
                                Globals.FUSE_HEIGHT, stix, areaTracker));
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
        initializeCursorSpeed();
        if (e.getCode().equals(KeyCode.SPACE) && !isRunning) {
            new SoundHandler().playSound("/sounds/Qix_NewLife.mp3", Globals.GAME_START_SOUND_VOLUME);
            animationTimerStart();
            LOGGER.log(Level.INFO, "Game started succesfully", this.getClass());
            isRunning = true;
            gameScene.setMessageLabel("");
        } else if (arrowKeys.contains(e.getCode())) {
            if (cursor.isDrawing()) {
                for (Unit unit : units) {
                    if (unit instanceof Fuse) {
                        ((Fuse) unit).setMoving(false);
                    }
                }
            }
            cursor.setCurrentMove(e.getCode());
        } else if (e.getCode().equals(KeyCode.X)) {
            if (stix.getStixCoordinates() != null && !stix.getStixCoordinates().isEmpty()) {
                if (!cursor.isFast()) {
                    cursor.setSpeed(Globals.CURSOR_SLOW);
                    cursor.setDrawing(true);
                    cursor.setFast(false);
                }
            } else {
                cursor.setSpeed(Globals.CURSOR_SLOW);
                cursor.setDrawing(true);
                cursor.setFast(false);
            }
        } else if (e.getCode().equals(KeyCode.Z)) {
            cursor.setSpeed(Globals.CURSOR_FAST);
            cursor.setDrawing(true);
            cursor.setFast(true);
        }
        if (cursor.getCurrentPowerup() == PowerUpType.SPEED) {
            cursor.setSpeed(cursor.getSpeed() + 1);
        }
    }

    private void initializeCursorSpeed() {
        if (cursor.isFast() || !cursor.isDrawing()) {
            cursor.setSpeed(Globals.CURSOR_FAST);
        } else {
            cursor.setSpeed(Globals.CURSOR_SLOW);
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
     * Add a unit.
     *
     * @param unit unit to add
     */
    private void addUnit(Unit unit) {
        if (unit instanceof Fuse) {
            for (Unit unit1 : units) {
                if (unit1 instanceof Fuse) {
                    return;
                }
            }
        }
        units.add(unit);
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
        GameController.units = units;
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
}
