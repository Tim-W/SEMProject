package nl.tudelft.sem.group2.controllers;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.Logger;
import nl.tudelft.sem.group2.ScoreCounter;
import nl.tudelft.sem.group2.board.AreaTracker;
import nl.tudelft.sem.group2.board.BoardGrid;
import nl.tudelft.sem.group2.collisions.CollisionHandler;
import nl.tudelft.sem.group2.global.Globals;
import nl.tudelft.sem.group2.level.LevelHandler;
import nl.tudelft.sem.group2.powerups.PowerEat;
import nl.tudelft.sem.group2.powerups.PowerLife;
import nl.tudelft.sem.group2.powerups.PowerSpeed;
import nl.tudelft.sem.group2.powerups.PowerUpType;
import nl.tudelft.sem.group2.powerups.Powerup;
import nl.tudelft.sem.group2.powerups.PowerupEvent;
import nl.tudelft.sem.group2.scenes.GameScene;
import nl.tudelft.sem.group2.scenes.ScoreScene;
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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static edu.emory.mathcs.backport.java.util.Arrays.asList;
import static nl.tudelft.sem.group2.global.Globals.LEVELS;

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
    private Set<Unit> units;

    private long previousTime;
    private CollisionHandler collisionHandler;
    private LevelHandler levelHandler;
    private GameScene gameScene;
    private boolean twoPlayers;

    /**
     * Constructor for the GameController class.
     */
    private GameController() {
        // Initialize models for scoretracking.
        levelHandler = new LevelHandler();
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
        // Put lock on class since it we do not want to instantiate it twice
        synchronized (GameController.class) {
            // Check if logger is in the meanwhile not already instantiated.
            if (gameController == null) {
                gameController = new GameController();
            }
        }
        return gameController;
    }

    /**
     * only used for testing.
     */
    public static void deleteGameController() {
        GameController.gameController = null;
    }

    /**
     * initialize gameController with one player.
     */
    public void initializeSinglePlayer() {
        twoPlayers = false;
        levelHandler.nextLevel(twoPlayers);
        gameScene = new GameScene(new Group(), Color.BLACK, createScoreScene());
        makeUnits();
    }

    /**
     * initialize gameController with multiple players.
     */
    public void initializeMultiPlayer() {
        twoPlayers = true;
        levelHandler.nextLevel(twoPlayers);
        gameScene = new GameScene(new Group(), Color.BLACK, createScoreScene());
        makeUnits();
    }

    /**
     * Method which generates a cursor, a sparx and a qix which is used for a single player match.
     * This also binds the correct key controls to the right cursor.
     */
    private void makeUnits() {
        cursors = new ArrayList<>();
        //first
        Stix stix = new Stix();
        Cursor cursor1 = new Cursor(new Point(Globals.CURSOR_START_X, Globals.CURSOR_START_Y), Globals.BOARD_MARGIN * 2,
                Globals.BOARD_MARGIN * 2, stix, Globals.LIVES, 0);
        KeyCode[] keys = new KeyCode[] {KeyCode.UP, KeyCode.DOWN, KeyCode.LEFT, KeyCode.RIGHT};
        cursor1.addKeys(asList(keys));
        cursor1.setFastMoveKey(KeyCode.O);
        cursor1.setSlowMoveKey(KeyCode.I);
        addCursor(cursor1);
        addUnit(cursor1);
        setScoreCounterInCursor(cursor1);
        if (twoPlayers) {
            //second
            Stix stix2 = new Stix();
            Cursor cursor2 = new Cursor(new Point(0, 0), Globals.BOARD_MARGIN * 2,
                    Globals.BOARD_MARGIN * 2, stix2, Globals.LIVES, 1);
            keys = new KeyCode[] {KeyCode.W, KeyCode.S, KeyCode.A, KeyCode.D};
            cursor2.addKeys(asList(keys));
            cursor2.setFastMoveKey(KeyCode.Z);
            cursor2.setSlowMoveKey(KeyCode.X);
            addUnit(cursor2);
            addCursor(cursor2);
            setScoreCounterInCursor(cursor2);
        }
        Sparx sparxLeft = new Sparx(Globals.CURSOR_START_X, 0, Globals.BOARD_MARGIN * 2,
                Globals.BOARD_MARGIN * 2, SparxDirection.LEFT);
        Sparx sparxRight = new Sparx(Globals.CURSOR_START_X, 0, Globals.BOARD_MARGIN * 2,
                Globals.BOARD_MARGIN * 2, SparxDirection.RIGHT);
        // Initialize and add units to units set in Gamescene
        qix = new Qix(levelHandler.getLevel().getQixSize());
        AreaTracker.getInstance().addObserver(qix);
        addUnit(qix);
        addUnit(sparxRight);
        addUnit(sparxLeft);
    }

    private void setScoreCounterInCursor(Cursor cursor) {
        ScoreCounter scoreCounter = new ScoreCounter(cursor.getId(), levelHandler.getLevel().getPercentage());
        scoreCounter.addObserver(gameScene.getScoreScene());
        cursor.setScoreCounter(scoreCounter);
    }

    private void resetLevel() {
        units.clear();
        AreaTracker.reset();
        collisionHandler = new CollisionHandler();
        makeUnits();
        gameScene.getScoreScene().reset();
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
            return;
        }
        units.add(unit);
    }

    private ScoreScene createScoreScene() {
        Group group = new Group();
        return new ScoreScene(group, Globals.GAME_WIDTH, Globals.SCORESCENE_POSITION_Y);
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
        levelHandler.getLevel().pause();
        gameScene.setMessageBoxLayoutX(Globals.GAMEOVER_POSITION_X);
        gameScene.setMessage(" Game Over! ");

        //Plays game over sound

        String score = "";
        for (Cursor cursor : cursors) {
            score += cursor.getScoreCounter() + ", ";
            LOGGER.log(java.util.logging.Level.INFO, "Game Over, player died with a score of "
                    + cursor.getScoreCounter().getTotalScore(), GameController.class);
        }
    }

    /**
     * TODO Play the game won sound.
     * stop the animations,
     * show that the player has won
     */
    private void gameWon() {
        levelHandler.getLevel().pause();
        SoundHandler.playSound("/sounds/qix-success.mp3", Globals.GAME_START_SOUND_VOLUME);
        if (levelHandler.getLevelID() == LEVELS) {
            gameScene.setMessageBoxLayoutX(Globals.GAMEWON_POSITION_X);
            gameScene.setMessage("Game won!");
            //LOGGER.log(java.util.logging.Level.INFO, "Game Won! Player won with a score of " + score, GameScene
            // .class);

        } else {
            gameScene.setMessageBoxLayoutX(Globals.MESSAGEBOX_POSITION_X);
            gameScene.setMessage("Press SPACE to go to the next level!");
            levelHandler.nextLevel(twoPlayers);
            resetLevel();
        }
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
                    gameScene.draw(units);

                    if (levelHandler.getLevel().isRunning()) {
                        gameScene.move(units);
                        for (Cursor cursor : cursors) {
                            if (cursor.getScoreCounter().hasWon()) {
                                gameWon();
                            }
                            if (collisionHandler.collisions(getUnits(), cursor)) {
                                cursor.cursorDied();
                                SoundHandler.playSound("/sounds/qix-death.mp3", Globals.GAME_OVER_SOUND_VOLUME);
                                if (cursor.getLives() == 0) {
                                    gameOver();
                                }
                            }
                            cursor.calculateArea(qix);
                        }
                        handlePowerups();
                        spawnPowerup();
                    }

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
                int[] coordinates = GameController.getInstance().getGrid()
                        .findPowerupLocation(cursor.oppositeQuadrant());
                Sparx sparx = new Sparx(coordinates[0], coordinates[1], Globals.BOARD_MARGIN * 2,
                        Globals.BOARD_MARGIN * 2, SparxDirection.randomDirection());
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

        PowerupEvent powerupEvent = collisionHandler.powerUpCollisions(units, cursors);
        if (powerupEvent != null) {
            Cursor cursor = powerupEvent.getCursor();
            switch (powerupEvent.getPowerUpType()) {
                case NONE:
                    return;
                case LIFE:
                    cursor.addLife();
                    return;
                case EAT:
                    cursor.getPowerupHandler().setCurrentPowerup(PowerUpType.EAT);
                    cursor.getPowerupHandler().setPowerUpDuration(Globals.POWERUP_EAT_DURATION);
                    return;
                case SPEED:
                    cursor.getPowerupHandler().setCurrentPowerup(PowerUpType.SPEED);
                    cursor.getPowerupHandler().setPowerUpDuration(Globals.POWERUP_SPEED_DURATION);
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

                int quadrant = cursor.oppositeQuadrant();

                int[] coordinates = GameController.getInstance().getGrid().findPowerupLocation(quadrant);
                Powerup powerup = null;
                Map<PowerUpType, Powerup> powerupMap = new HashMap<>();
                powerupMap.put(PowerUpType.EAT, new PowerEat(coordinates[0], coordinates[1],
                        Globals.BOARD_MARGIN * 2, Globals.BOARD_MARGIN * 2));
                powerupMap.put(PowerUpType.LIFE, new PowerLife(coordinates[0], coordinates[1],
                        Globals.BOARD_MARGIN * 2, Globals.BOARD_MARGIN * 2));
                powerupMap.put(PowerUpType.SPEED, new PowerSpeed(coordinates[0], coordinates[1],
                        Globals.BOARD_MARGIN * 2, Globals.BOARD_MARGIN * 2));
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
            if (cursor.getPowerupHandler().getCurrentPowerup() != PowerUpType.NONE) {
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
            if (cursor.getPowerupHandler().hasPowerup()
                    && cursor.getPowerupHandler().getPowerUpDuration() <= 0) {
                cursor.getPowerupHandler().setCurrentPowerup(PowerUpType.NONE);
            }

            if (cursor.getPowerupHandler().hasPowerup()
                    && cursor.getPowerupHandler().getPowerUpDuration() > 0) {
                cursor.getPowerupHandler().decrementDuration();
            }
        }
    }

    /**
     * check if game is multiplayer.
     *
     * @return boolean true if multiplayer
     */
    public boolean isTwoPlayers() {
        return twoPlayers;
    }

    /**
     * Method that handles the action when a key is released.
     *
     * @param e describes which keyevent happened.
     */
    public void keyReleased(KeyEvent e) {
        KeyCode keyCode = e.getCode();
        for (Cursor cursor : cursors) {
            cursor.keyReleased(keyCode);
        }
    }

    /**
     * Method that handles the action when a key is pressed.
     *
     * @param e describes which keyevent happened.
     */
    public void keyPressed(KeyEvent e) {
        initializeCursorSpeed();
        if (e.getCode().equals(KeyCode.SPACE) && !levelHandler.getLevel().isRunning()) {
            SoundHandler.playSound("/sounds/qix-new-life.mp3", Globals.GAME_START_SOUND_VOLUME);
            if (!levelHandler.getLevel().isRunning()) {
                animationTimerStart();
                LOGGER.log(java.util.logging.Level.INFO, "Game started succesfully", this.getClass());
            }
            levelHandler.getLevel().start();
            gameScene.setMessage("");
        } else {
            for (Cursor cursor : cursors) {
                cursor.keyPressed(e);
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

    //Getters

    public GameScene getGameScene() {
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

    public LevelHandler getLevelHandler() {
        return levelHandler;
    }

    /**
     * only used for testing.
     *
     * @param levelHandler LevelHandler
     */
    public void setLevelHandler(LevelHandler levelHandler) {
        this.levelHandler = levelHandler;
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

    /**
     * @return the boardgrid
     */
    public BoardGrid getGrid() {
        if (levelHandler.getLevel() == null) {
            return null;
        }
        return levelHandler.getLevel().getBoardGrid();
    }
}
