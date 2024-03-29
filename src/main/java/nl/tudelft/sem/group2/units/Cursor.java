package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.Logger;
import nl.tudelft.sem.group2.ScoreCounter;
import nl.tudelft.sem.group2.board.AreaTracker;
import nl.tudelft.sem.group2.board.BoardGrid;
import nl.tudelft.sem.group2.collisions.CollisionInterface;
import nl.tudelft.sem.group2.cursorMovement.CursorKeypressHandler;
import nl.tudelft.sem.group2.global.Globals;
import nl.tudelft.sem.group2.powerups.CursorPowerupHandler;
import nl.tudelft.sem.group2.sound.SoundHandler;

import java.awt.Point;
import java.util.LinkedList;
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

    private int loops = 0;

    private LinkedList<double[][]> oldLines = new LinkedList<>();

    private Stix stix;
    private int lives;

    private ScoreCounter scoreCounter;
    private boolean isDrawing = false;
    private int speed = Globals.CURSOR_FAST;
    private int id;
    private CursorPowerupHandler cursorPowerupHandler;
    private FuseHandler fuseHandler;
    private CursorKeypressHandler cursorKeypressHandler;


    /**
     * Create a cursor.
     *
     * @param position start position
     * @param width    width, used for the sprite
     * @param height   height, used for the sprite
     * @param stix     current stix to use
     * @param lives    the amount of lives a players starts with
     * @param id       identifies the cursor.
     */
    public Cursor(Point position, int width, int height, Stix stix, int lives, int id) {
        super(position.x, position.y, width, height);
        Image[] sprite = new Image[1];
        this.id = id;
        String colorString = "yellow";
        //if player 2
        if (id == 1) {
            colorString = "red";
        }
        sprite[0] = new Image("/images/cursor-" + colorString + ".png");
        setSprite(sprite);
        this.stix = stix;
        this.lives = lives;
        cursorPowerupHandler = new CursorPowerupHandler();
        fuseHandler = new FuseHandler(this);
        cursorKeypressHandler = new CursorKeypressHandler(this);
    } /*

    *//*
     * Custom constructor for testing purposes.
     *
     * @param position    spawn position fo cursor.
     * @param width       width of cursor.
     * @param height      height of cursor.
     * @param stix        stix of the cursor.
     *//*
    public Cursor(Point position, int width, int height, Stix stix) {
        super(position.x, position.y, width, height);
        this.stix = stix;
        cursorPowerupHandler = new CursorPowerupHandler();
        fuseHandler = new FuseHandler(this);
    }
*/

    @Override
    public void move() {
        for (int i = 0; i < speed; i++) {
            cursorKeypressHandler.cursorAssertMove();
        }
        if (fuseHandler.getFuse() != null) {
            fuseHandler.getFuse().move();
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


    @Override
    public void draw(GraphicsContext gc) {
        int drawX = gridToCanvas(getX());
        int drawY = gridToCanvas(getY());
        drawStixAndFuse(gc);
        final int lineCount = 10;
        if (loops < animationSpeed + lineCount) {
            calculateLineCoordinates(drawX, drawY, gc.getCanvas());
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
        cursorPowerupHandler.applyEffect(gc);
    }

    private void calculateLineCoordinates(int drawX, int drawY, Canvas canvas) {
        if (loops < animationSpeed) {
            double height = canvas.getHeight();
            double heightVar = height / Globals.ANIMATION_SPEED * loops;
            double width = canvas.getWidth();
            double widthVar = width / Globals.ANIMATION_SPEED * loops;
            final double lineSize = 80.0;
            double lineSizeVar = (lineSize / Globals.ANIMATION_SPEED) * loops;
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
     * @param boardGrid the boardgrid of the game
     */
    public void calculateArea(Qix qix, BoardGrid boardGrid) {
        if (getGrid().isOuterborder(this.getX(), this.getY()) && !this.getStix().isEmpty()) {

            SoundHandler.playSound("/sounds/qix-success.mp3", Globals.SUCCESS_SOUND_VOLUME);

            AreaTracker.getInstance().calculateNewArea(new Point(qix.getX(), qix.getY()),
                    this.isFast(), getStix(), scoreCounter, boardGrid);

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
     * @return the CursorKeypressHandler of the cursor.
     */
    public CursorKeypressHandler getCursorKeypressHandler() {
        return cursorKeypressHandler;
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
        return speed != Globals.CURSOR_SLOW;
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

    public void setLives(int lives) {
        this.lives = lives;
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
     * Return the quadrant the cursor is in, as follows.
     * 01
     * 32
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

    /**
     * @return true if the cursor has drawn stix
     */
    public boolean stixDrawn() {
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

    public CursorPowerupHandler getCursorPowerupHandler() {
        return cursorPowerupHandler;
    }

    public FuseHandler getFuseHandler() {
        return fuseHandler;
    }

    public void setFuseHandler(FuseHandler fuseHandler) {
        this.fuseHandler = fuseHandler;
    }

    public int getLoops() {
        return loops;
    }

    public void setLoops(int loops) {
        this.loops = loops;
    }

    /**
     * @return String format of cursor.
     */
    public String toString() {
        return "Cursor";
    }
}