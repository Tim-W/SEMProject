package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.AreaState;
import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.KeypressHandler;
import nl.tudelft.sem.group2.Logger;
import nl.tudelft.sem.group2.ScoreCounter;
import nl.tudelft.sem.group2.collisions.CollisionInterface;
import nl.tudelft.sem.group2.global.Globals;
import nl.tudelft.sem.group2.powerups.PowerUpType;
import nl.tudelft.sem.group2.powerups.PowerupHandler;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;

import static nl.tudelft.sem.group2.scenes.GameScene.gridToCanvas;

/**
 * A cursor which can travel over lines and is controlled by user input (arrow keys).
 * There are draw keys which for player 1 are I and O and for player 2 Z and X.
 * With these draw keys a cursor can move from the lines and create it's own lines.
 * I and Z are used to draw fast, and O and X for drawing slowly but gaining double points.
 * The cursor can lose a live when colliding with the qix, a sparx or the fuse.
 */
public class Cursor extends LineTraveller implements CollisionInterface {
    private static final Logger LOGGER = Logger.getLogger();
    private int animationSpeed = Globals.ANIMATION_SPEED;
    private KeyCode currentMove = null;
    private int loops = 0;
    private int speed = 2;
    private LinkedList<double[][]> oldLines = new LinkedList<>();
    private boolean isDrawing = false;
    private boolean isFast = true;
    private Stix stix;
    private int lives;
    // 0 for nothing, 1 if life powerup, 2 if eat powerup and 3 if speed powerup
    private ArrayList<KeyCode> arrowKeys = new ArrayList<>();
    private KeyCode fastMoveKey, slowMoveKey;
    private ScoreCounter scoreCounter;
    private int id;
    private PowerupHandler powerupHandler;
    private FuseHandler fuseHandler;


    /**
     * Create a cursor.
     *
     * @param position    start position
     * @param width       width, used for collision detection
     * @param height      height, used for collision detection
     * @param areaTracker used for calculating areas
     * @param stix        current stix to use
     * @param id          identifies the cursor.
     * @param lives       the amount of lives a players starts with
     */
    public Cursor(Point position, int width, int height, AreaTracker areaTracker, Stix stix, int lives,
                  int id) {
        super(position.x, position.y, width, height, areaTracker);
        Image[] sprite = new Image[1];
        this.id = id;
        String colorString = "blue";
        //if player 2
        if (id == 1) {
            colorString = "red";
        }
        sprite[0] = new Image("/images/cursor-" + colorString + ".png");
        setSprite(sprite);
        this.stix = stix;
        this.lives = lives;
        powerupHandler = new PowerupHandler();
        fuseHandler = new FuseHandler(this);
    }

    @Override
    public void move() {
        for (int i = 0; i < speed; i++) {
            int transX = 0;
            int transY = 0;
            if (currentMove != null) {
                // A map containing relationships between keycodes and the movement directions.
                Map<KeyCode, CursorMovement> cursorMovementMap = new HashMap<>();
                cursorMovementMap.put(arrowKeys.get(2), new CursorMovement(-1, 0));
                cursorMovementMap.put(arrowKeys.get(3), new CursorMovement(1, 0));
                cursorMovementMap.put(arrowKeys.get(0), new CursorMovement(0, -1));
                cursorMovementMap.put(arrowKeys.get(1), new CursorMovement(0, 1));
                transX += cursorMovementMap.get(currentMove).getTransX();
                transY += cursorMovementMap.get(currentMove).getTransY();
                KeypressHandler.cursorAssertMove(this, transX, transY);
            }
        }
        if (fuseHandler.getFuse() != null) {
            fuseHandler.getFuse().move();
        }
    }

    /**
     * Handles a key press for the cursor.
     *
     * @param e the keyEvent of the key pressed
     */
    public void keyPressed(KeyEvent e) {
        if (getArrowKeys().contains(e.getCode())) {
            if (isDrawing() && fuseHandler.getFuse() != null) {
                fuseHandler.getFuse().setMoving(false);
            }
            setCurrentMove(e.getCode());
        } else if (e.getCode().equals(getSlowMoveKey())) {
            if (!stixDrawn() || !isFast()) {
                setSpeed(1);
                setDrawing(true);
                setFast(false);
            }
        } else if (e.getCode().equals(getFastMoveKey())) {
            setSpeed(2);
            setDrawing(true);
            setFast(true);
        }
        if (powerupHandler.getCurrentPowerup() == PowerUpType.SPEED) {
            setSpeed(getSpeed() + 1);
        }
    }

    /**
     * Handles a key being released.
     *
     * @param keyCode the key being released
     */
    public void keyReleased(KeyCode keyCode) {
        if (keyCode.equals(getCurrentMove())) {
            fuseHandler.handleFuse();
            setCurrentMove(null);
        } else if (keyCode.equals(getFastMoveKey()) || keyCode.equals(getSlowMoveKey())) {
            setDrawing(false);
            setSpeed(2);
            fuseHandler.handleFuse();
        }
    }

    /**
     * Method which tests if cursor intersects with other unit.
     *
     * @param collidee the other unit
     * @return if cursor intersects with other unit
     */
    @Override
    public boolean intersect(Unit collidee) {
        return super.intersect(collidee) || fuseHandler.getFuse() != null && fuseHandler.getFuse().intersect(this);
    }

    /**
     * @return the current move direction (up/down/left/right)
     */
    public KeyCode getCurrentMove() {
        return currentMove;
    }

    /**
     * @param currentMove the current move direction (up/down/left/right)
     */
    public void setCurrentMove(KeyCode currentMove) {
        this.currentMove = currentMove;
    }


    @Override
    public void draw(GraphicsContext gc) {
        int drawX = gridToCanvas(getX());
        int drawY = gridToCanvas(getY());
        drawStixAndFuse(gc);
        final int lineCount = 10;
        if (loops < animationSpeed + lineCount) {
            calculateLineCoordinates(drawX, drawY);
            if (oldLines.size() > lineCount || oldLines.size() > animationSpeed - loops) {
                oldLines.removeLast();
            }
            gc.setStroke(Color.WHITE);
            for (double[][] l : oldLines) {
                gc.beginPath();
                for (int i = 0; i < 4; i++) {
                    gc.moveTo(l[i][0], l[i][1]);
                    gc.lineTo(l[i][2], l[i][3]);
                }
                gc.stroke();
            }
            loops++;
        }
        gc.drawImage(
                getSprite()[getSpriteIndex()],
                drawX - getWidth() / 2 + 1,
                drawY - getHeight() / 2 + 1,
                getWidth(),
                getHeight()
        );
    }

    private void calculateLineCoordinates(int drawX, int drawY) {
        if (loops < animationSpeed) {
            double height = Globals.BORDER_BOTTOM_HEIGHT;
            double heightVar = height / animationSpeed * loops;
            double width = Globals.BOARD_WIDTH;
            double widthVar = width / animationSpeed * loops;
            final double lineSize = 80.0;
            double lineSizeVar = (lineSize / animationSpeed) * loops;
            double[][] line = new double[4][4];
            line[0][0] = width - widthVar + drawX - (lineSize - lineSizeVar);
            line[0][1] = -(height - heightVar) + drawY;
            line[0][2] = width - widthVar + drawX;
            line[0][3] = -(height - heightVar) + drawY + (lineSize - lineSizeVar);
            line[1][0] = width - widthVar + drawX - (lineSize - lineSizeVar);
            line[1][1] = height - heightVar + drawY;
            line[1][2] = width - widthVar + drawX;
            line[1][3] = height - heightVar + drawY - (lineSize - lineSizeVar);
            line[2][0] = -(width - widthVar) + drawX + (lineSize - lineSizeVar);
            line[2][1] = -(height - heightVar) + drawY;
            line[2][2] = -(width - widthVar) + drawX;
            line[2][3] = -(height - heightVar) + drawY + (lineSize - lineSizeVar);
            line[3][0] = -(width - widthVar) + drawX + (lineSize - lineSizeVar);
            line[3][1] = height - heightVar + drawY;
            line[3][2] = -(width - widthVar) + drawX;
            line[3][3] = height - heightVar + drawY - (lineSize - lineSizeVar);
            oldLines.addFirst(line);
        }
    }



    /**
     * When a new area is completed, calculate the new score.
     *
     * @param qix the qix of the game
     */
    public void calculateArea(Qix qix) {
        if (this.getAreaTracker().getBoardGrid()[this.getX()][this.getY()] == AreaState.OUTERBORDER
                && !this.getStix().getStixCoordinates().isEmpty()) {
            this.getAreaTracker().calculateNewArea(new Point(qix.getX(), qix.getY()),
                    this.isFast(), getStix(), scoreCounter);
            //Remove the Fuse from the gameView when completing an area
            fuseHandler.removeFuse();
        }
    }

    /**
     * Draw current Stix and Fuse on screen.
     */
    private void drawStixAndFuse(GraphicsContext gc) {
        stix.draw(gc, fuseHandler.getFuse(), isFast());
        if (fuseHandler.getFuse() != null) {
            fuseHandler.getFuse().draw(gc);
        }
    }

    /**
     * @return true if the cursor is drawing
     */
    public boolean isDrawing() {
        return isDrawing;
    }

    /***** Getters and setters *****/

    /**
     * @param drawing if cursor is moving while user has key X or Z down
     */
    public void setDrawing(boolean drawing) {
        isDrawing = drawing;
    }

    /**
     * @return the speed of the cursor
     */
    public int getSpeed() {
        return this.speed;
    }

    /**
     * @param speed the amount of pixels the cursor moves per when moving
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     * Get id of the cursor.
     *
     * @return int id
     */
    public int getId() {
        return id;
    }

    /**
     * @return whether the cursor is moving fast or slow
     */
    public boolean isFast() {
        return isFast;
    }

    /**
     * @param isFast whether the cursor is moving fast or slow
     */
    public void setFast(boolean isFast) {
        this.isFast = isFast;
    }

    /**
     * @return the stix of this cursor.
     */
    public Stix getStix() {
        return stix;
    }

    /**
     * only for testing.
     *
     * @param stix setter for stix
     */
    public void setStix(Stix stix) {
        this.stix = stix;
    }



    /**
     * @param keycode the key that is specific to this cursor.
     */
    public void addKey(KeyCode keycode) {
        arrowKeys.add(keycode);
    }

    /**
     * @param keycodes the keys that are specific to this cursor.
     */
    public void addKeys(Collection<KeyCode> keycodes) {
        arrowKeys.addAll(keycodes);
    }

    /**
     * @return this cursor specific keys.
     */
    public ArrayList<KeyCode> getArrowKeys() {
        return arrowKeys;
    }

    /**
     * @return The scoreCounter of this specific cursor.
     */
    public ScoreCounter getScoreCounter() {
        return scoreCounter;
    }

    /**
     * set the scorecounter of the cursor.
     *
     * @param scoreCounter of the cursor
     */
    public void setScoreCounter(ScoreCounter scoreCounter) {
        this.scoreCounter = scoreCounter;
    }

    /**
     * Method which log the current movement of the cursor.
     * Only gets executed when log level is on detailledLogging.
     */
    public void logCurrentMove() {
        LOGGER.log(Level.FINE, "Cursor moved to (" + getX() + "," + getY() + ")", this.getClass());
    }

    /**
     * Getter for the amount of lives the cursor has.
     *
     * @return amount of lives left
     */
    public int getLives() {
        return lives;
    }

    /**
     * Method that decreases amount of lives cursor has upon dying.
     */
    public void cursorDied() {
        if (lives >= 1) {
            subtractLife();
        }
        LOGGER.log(Level.INFO, "Player died, lives remaining: " + lives, this.getClass());

        if (stixDrawn()) {
            Point newStartPos = stix.getStixCoordinates().getFirst();
            this.setX((int) newStartPos.getX());
            this.setY((int) newStartPos.getY());
            stix.emptyStix();
            fuseHandler.removeFuse();
        }

        loops = 0;
        animationSpeed = Globals.DEATH_ANIMATION_SPEED;
    }


    /**
     * @return true if the cursor has drawn stix
     */
    private boolean stixDrawn() {
        return stix != null && !stix.isStixEmpty();
    }

    /**
     * Adds a life to the cursor.
     */
    public void addLife() {
        lives++;
        scoreCounter.notifyLife(lives);
        //scoreCounter.addLife();
        LOGGER.log(Level.INFO, "added life to cursor. Current lives: " + lives, Cursor.class);
    }

    /**
     * decrement a life to the cursor.
     */
    public void subtractLife() {
        lives--;
        scoreCounter.notifyLife(lives);
        LOGGER.log(Level.INFO, "subract life of cursor. Current lives: " + lives, Cursor.class);
    }

    public KeyCode getFastMoveKey() {
        return fastMoveKey;
    }

    public void setFastMoveKey(KeyCode fastMoveKey) {
        this.fastMoveKey = fastMoveKey;
    }

    public KeyCode getSlowMoveKey() {
        return slowMoveKey;
    }

    public void setSlowMoveKey(KeyCode slowMoveKey) {
        this.slowMoveKey = slowMoveKey;
    }

    public PowerupHandler getPowerupHandler() {
        return powerupHandler;
    }

    public FuseHandler getFuseHandler() {
        return fuseHandler;
    }

    /**
     * @return String format of cursor.
     */
    public String toString() {
        return "Cursor";
    }
}