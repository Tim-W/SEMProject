package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.board.AreaTracker;
import nl.tudelft.sem.group2.Logger;
import nl.tudelft.sem.group2.ScoreCounter;
import nl.tudelft.sem.group2.board.Coordinate;
import nl.tudelft.sem.group2.collisions.CollisionInterface;
import nl.tudelft.sem.group2.controllers.GameController;
import nl.tudelft.sem.group2.global.Globals;
import nl.tudelft.sem.group2.powerups.PowerUpType;
import nl.tudelft.sem.group2.scenes.GameScene;
import nl.tudelft.sem.group2.sound.SoundHandler;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;

import static nl.tudelft.sem.group2.scenes.GameScene.gridToCanvas;

/**
 * A cursor which can travel over lines and is controlled by user input (arrow keys).
 */
public class Cursor extends LineTraveller implements CollisionInterface {
    private static final Logger LOGGER = Logger.getLogger();

    private static final int ANIMATION_SPEED = 30;
    private int loops = 0;

    private LinkedList<double[][]> oldLines = new LinkedList<>();

    private Stix stix;
    private Fuse fuse;

    private ScoreCounter scoreCounter;
    private PowerUpType currentPowerup;

    private ArrayList<KeyCode> arrowKeys = new ArrayList<>();
    private KeyCode currentMove = null;
    private boolean isDrawing = false;
    private int speed = Globals.CURSOR_FAST;



    /**
     * Create a cursor.
     *
     * @param position    start position
     * @param width       width, used for collision detection
     * @param height      height, used for collision detection
     * @param stix        current stix to use
     * @param color       specifies color for this cursor.
     * @param lives       the amount of lives a players starts with
     */
    public Cursor(Point position, int width, int height, Stix stix, Color color, int lives) {
        super(position.x, position.y, width, height);
        Image[] sprite = new Image[1];

        String colorString = "red";
        if (color.equals(Color.BLUE)) {
            colorString = "blue";
        } else if (color.equals(Color.YELLOW)) {
            colorString = "yellow";
        } else {
            color = Color.RED;
        }
        sprite[0] = new Image("/images/cursor_" + colorString + ".png");
        setSprite(sprite);
        this.stix = stix;
        scoreCounter = new ScoreCounter(color);
        GameController.getInstance().getScene();
        scoreCounter.addObserver(GameScene.getScoreScene());
        this.scoreCounter.setLives(lives);
        this.currentPowerup = PowerUpType.NONE;
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
                assertMove(transX, transY);
            }
        }
    }


    private void assertMove(int transX, int transY) {
        if (getX() + transX >= 0 && getX() + transX <= Globals.BOARD_WIDTH / 2 && getY() + transY >= 0 && getY()
                + transY <= Globals.BOARD_WIDTH / 2) {
            if (grid.isUncovered(getIntX() + transX, getIntY() + transY) && isDrawing) {
                if (!stix.contains(new Point(getIntX() + transX, getIntY() + transY))
                        && !stix.contains(new Point(getIntX() + transX * 2, getIntY() + transY * 2))
                        && grid.isUncovered(getIntX() + transX + transY, getIntY() + transY + transX)
                        && grid.isUncovered(getIntX() + transX - transY, getIntY() + transY - transX)) {

                    if (grid.isOuterborder(getIntX(), getIntY())) {
                        stix.addToStix(new Point(getIntX(), getIntY()));
                    }
                    setX(getIntX() + transX);
                    setY(getIntY() + transY);
                    logCurrentMove();
                    stix.addToStix(new Point(getIntX(), getIntY()));
                }
            } else if (grid.isOuterborder(getIntX() + transX, getIntY() + transY)) {
                setX(getIntX() + transX);
                setY(getIntY() + transY);
                logCurrentMove();
            }
        }
    }

    /**
     * Method which tests if cursor intersects with other unit.
     * @param collidee the other unit
     * @return if cursor intersects with other unit
     */
    @Override
    public boolean intersect(Unit collidee) {
        return super.intersect(collidee) || fuse != null && fuse.intersect(this);
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


    /**
     * Return the quadrant the cursor is in, as follows.
     * 12
     * 34
     *
     * @return the quadrant the cursor is in
     */
    public int quadrant() {
        if (this.getX() < Globals.BOARD_WIDTH / 4) {
            if (this.getY() < Globals.BOARD_HEIGHT / 4) {
                return 0;
            } else {
                return 3;
            }
        } else if (this.getY() < Globals.BOARD_HEIGHT / 4) {
            System.out.println("in quadrant 2");
            return 1;
        }
        return 2;
    }

    /**
     * Gives the opposite quadrant the cursor is in.
     *
     * @return the opposite quadrant the cursor is in
     */
    public int oppositeQuadrant() {
        int quadrant = this.quadrant();

        return (quadrant + 2) % 4;
    }

    @Override
    public void draw(Canvas canvas) {
        int drawX = gridToCanvas(getIntX());
        int drawY = gridToCanvas(getIntY());
        final int lineCount = 10;
        if (loops < ANIMATION_SPEED + lineCount) {
            calculateLineCoordinates(drawX, drawY, canvas);
            if (oldLines.size() > lineCount || oldLines.size() > ANIMATION_SPEED - loops) {
                oldLines.removeLast();
            }
            GraphicsContext gC = canvas.getGraphicsContext2D();
            gC.setStroke(Color.WHITE);
            for (double[][] l : oldLines) {
                gC.beginPath();
                for (int i = 0; i < 4; i++) {
                    gC.moveTo(l[i][0], l[i][1]);
                    gC.lineTo(l[i][2], l[i][3]);
                }
                gC.stroke();
            }
            loops++;
        }
        canvas.getGraphicsContext2D().drawImage(
                getSprite()[getSpriteIndex()],
                drawX - getWidth() / 2 + 1,
                drawY - getHeight() / 2 + 1,
                getWidth(),
                getHeight()
        );
    }

    private void calculateLineCoordinates(int drawX, int drawY, Canvas canvas) {
        if (loops < ANIMATION_SPEED) {
            double height = canvas.getHeight();
            double heightVar = height / ANIMATION_SPEED * loops;
            double width = canvas.getWidth();
            double widthVar = width / ANIMATION_SPEED * loops;
            final double lineSize = 80.0;
            double lineSizeVar = (lineSize / ANIMATION_SPEED) * loops;
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

    /***** Handeling Fuse *****/
    /**
     * handles making fuse and makes it start moving.
     */
    public void handleFuse() {
        if (this.getStix().contains(new Point(this.getIntX(), this.getIntY()))) {
            if (fuse == null) {
                fuse =
                        new Fuse((int) this.getStix().getStixCoordinates().getFirst().getX(),
                                (int) this.getStix().getStixCoordinates().getFirst().getY(),
                                Globals.FUSE_WIDTH,
                                Globals.FUSE_HEIGHT, this.getStix());
            } else {
                fuse.moving();
            }
            this.setCurrentMove(null);
        }
    }

    /**
     * If there is a Fuse on the screen, remove it.
     */
    public void removeFuse() {
        fuse = null;
    }

    /**
     * When a new area is completed, calculate the new score.
     *
     * @param qix the qix of the game
     */
    public void calculateArea(Qix qix) {
        if (this.grid.isOuterborder(this.getIntX(), this.getIntY()) && !this.getStix().isEmpty()) {

            new SoundHandler().playSound("/sounds/Qix_Success.mp3", Globals.SUCCESS_SOUND_VOLUME);

            AreaTracker.getInstance().calculateNewArea(new Coordinate(qix.getIntX(), qix.getIntY()),
                    this.isFast(), getStix(), scoreCounter);

            //Remove the Fuse from the gameView when completing an area
            removeFuse();
        }
    }

    /***** Getters and setters *****/

    /**
     * @return true if the cursor is drawing
     */
    public boolean isDrawing() {
        return isDrawing;
    }

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
     * @return whether the cursor is moving fast or slow
     */
    public boolean isFast() {
        return (speed != Globals.CURSOR_SLOW);
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
     * @return the fuse if there is a fuse, otherwise null.
     */
    public Fuse getFuse() {
        return fuse;
    }

    /**
     * only for testing.
     *
     * @param fuse setter for fuse
     */
    public void setFuse(Fuse fuse) {
        this.fuse = fuse;
    }

    /**
     * @param keycode the key that is specific to this cursor.
     */
    public void addKey(KeyCode keycode) {
        arrowKeys.add(keycode);
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
     * Method which log the current movement of the cursor.
     * Only gets executed when log level is on detailledLogging.
     */
    private void logCurrentMove() {
        LOGGER.log(Level.FINE, "Cursor moved to (" + getX() + "," + getY() + ")", this.getClass());
    }

    /**
     * Getter for the amount of lives the cursor has.
     *
     * @return amount of lives left
     */
    public int getLives() {
        return scoreCounter.getLives();
    }

    /**
     * Method that decreases amount of lives cursor has upon dying.
     */
    public void cursorDied() {
        if (scoreCounter.getLives() >= 1) {
            scoreCounter.subtractLife();
        }
        this.quadrant();
        LOGGER.log(Level.INFO, "Player died, lives remaining: " + scoreCounter.getLives(), this.getClass());

        if (scoreCounter.getLives() > 0 && stix != null && !stix.isStixEmpty()) {
            Point newStartPos = stix.getStixCoordinates().getFirst();
            this.setX((int) newStartPos.getX());
            this.setY((int) newStartPos.getY());
            stix.emptyStix();
            fuse = null;
        }
    }

    public PowerUpType getCurrentPowerup() {
        return currentPowerup;
    }

    /**
     * sets the current powerup status of the cursor.
     *
     * @param currentPowerup the new powerup status
     */
    public void setCurrentPowerup(PowerUpType currentPowerup) {
        this.currentPowerup = currentPowerup;
    }


    /**
     * @return true if the cursor has a powerup active
     */
    public boolean hasPowerUp() {
        return this.currentPowerup != PowerUpType.NONE;
    }

    /**
     * Adds a life to the cursor.
     */
    public void addLife() {
        scoreCounter.addLife();
        LOGGER.log(Level.INFO, "added life to cursor. Current lives: " + scoreCounter.getLives(), Cursor.class);
    }

    /**
     * @return String format of cursor.
     */
    public String toString() {
        return "Cursor";
    }
}