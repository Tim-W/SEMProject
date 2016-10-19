package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.AreaState;
import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.Logger;
import nl.tudelft.sem.group2.ScoreCounter;
import nl.tudelft.sem.group2.collisions.CollisionInterface;
import nl.tudelft.sem.group2.controllers.GameController;
import nl.tudelft.sem.group2.global.Globals;
import nl.tudelft.sem.group2.scenes.GameScene;
import nl.tudelft.sem.group2.sound.SoundHandler;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;

import static nl.tudelft.sem.group2.global.Globals.BOARD_WIDTH;
import static nl.tudelft.sem.group2.scenes.GameScene.gridToCanvas;

/**
 * A cursor which can travel over lines and is controlled by user input (arrow keys).
 */
public class Cursor extends LineTraveller implements CollisionInterface {
    private static final Logger LOGGER = Logger.getLogger();
    private final int animationSpeed = 30;
    private KeyCode currentMove = null;
    private int loops = 0;
    private int speed = 2;
    private LinkedList<double[][]> oldLines = new LinkedList<>();
    private boolean isDrawing = false;
    private boolean isFast = true;
    private Stix stix;
    private Fuse fuse;
    private ArrayList<KeyCode> arrowKeys = new ArrayList<>();
    private ScoreCounter scoreCounter;


    /**
     * Create a cursor.
     *
     * @param x           start x coordinate
     * @param y           start y coordinate
     * @param width       width, used for collision detection
     * @param height      height, used for collision detection
     * @param areaTracker used for calculating areas
     * @param stix        current stix to use
     * @param color       specifies color for this cursor.
     * @param lives       the amount of lives a players starts with
     */
    public Cursor(int x, int y, int width, int height, AreaTracker areaTracker, Stix stix, Color color, int lives) {
        super(x, y, width, height, areaTracker);
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
        scoreCounter.setLives(lives);
    }

    @Override
    public void move() {
        for (int i = 0; i < speed; i++) {
            int transX = 0;
            int transY = 0;

            if (currentMove != null) {
                if (currentMove.equals(arrowKeys.get(2))) {
                    transX = -1;
                } else if (currentMove.equals(arrowKeys.get(3))) {
                    transX = 1;
                } else if (currentMove.equals(arrowKeys.get(0))) {
                    transY = -1;
                } else if (currentMove.equals(arrowKeys.get(1))) {
                    transY = 1;
                }
                assertMove(transX, transY);
            }
        }
    }


    private void assertMove(int transX, int transY) {
        if (getX() + transX >= 0 && getX() + transX <= BOARD_WIDTH / 2 && getY() + transY >= 0 && getY()
                + transY <= BOARD_WIDTH / 2) {
            if (uncoveredOn(getX() + transX, getY() + transY) && isDrawing) {
                if (!stix.getStixCoordinates().contains(new Point(getX() + transX, getY() + transY))
                        && !stix.getStixCoordinates().contains(new Point(getX() + transX * 2,
                        getY() + transY * 2))
                        && getAreaTracker().getBoardGrid()[getX() + transX + transY]
                        [getY() + transY + transX].equals(AreaState
                        .UNCOVERED)
                        && getAreaTracker().getBoardGrid()[getX() + transX - transY]
                        [getY() + transY - transX].equals(AreaState
                        .UNCOVERED)) {

                    if (outerBorderOn(getX(), getY())) {
                        stix.addToStix(new Point(getX(), getY()));
                    }
                    setX(getX() + transX);
                    setY(getY() + transY);
                    logCurrentMove();
                    stix.addToStix(new Point(getX(), getY()));
                }
            } else if (outerBorderOn(getX() + transX, getY() + transY)) {
                setX(getX() + transX);
                setY(getY() + transY);
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
        return super.intersect(collidee) || (fuse != null && fuse.intersect(this));
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
    public void draw(Canvas canvas) {
        int drawX = gridToCanvas(getX());
        int drawY = gridToCanvas(getY());
        final int lineCount = 10;
        if (loops < animationSpeed + lineCount) {
            calculateLineCoordinates(drawX, drawY, canvas);
            if (oldLines.size() > lineCount || oldLines.size() > animationSpeed - loops) {
                oldLines.removeLast();
            }
            GraphicsContext gC = canvas.getGraphicsContext2D();
            gC.setStroke(javafx.scene.paint.Color.WHITE);
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
        if (loops < animationSpeed) {
            double height = canvas.getHeight();
            double heightVar = height / animationSpeed * loops;
            double width = canvas.getWidth();
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

    /***** Handeling Fuse *****/
    /**
     * handles making fuse and makes it start moving.
     */
    public void handleFuse() {
        if (this.getStix().getStixCoordinates().contains(new Point(this.getX(), this.getY()))) {
            if (fuse == null) {
                fuse =
                        new Fuse((int) this.getStix().getStixCoordinates().getFirst().getX(),
                                (int) this.getStix().getStixCoordinates().getFirst().getY(),
                                Globals.FUSE_WIDTH,
                                Globals.FUSE_HEIGHT, this.getAreaTracker(), this.getStix());
            } else {
                fuse.setMoving(true);
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
        if (this.getAreaTracker().getBoardGrid()[this.getX()][this.getY()] == AreaState.OUTERBORDER
                && !this.getStix().getStixCoordinates().isEmpty()) {
            new SoundHandler().playSound("/sounds/Qix_Success.mp3", Globals.SUCCESS_SOUND_VOLUME);
            this.getAreaTracker().calculateNewArea(new Point(qix.getX(), qix.getY()),
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
     * @param speed the amount of pixels the cursor moves per when moving
     */
    public void setSpeed(int speed) {
        this.speed = speed;
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
     * @return the fuse if there is a fuse, otherwise null.
     */
    public Fuse getFuse() {
        return fuse;
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
            scoreCounter.setLives(scoreCounter.getLives() - 1);
        }
        LOGGER.log(Level.INFO, "Player died, lives remaining: " + scoreCounter.getLives(), this.getClass());
        if (scoreCounter.getLives() == 0 && this.isDrawing()) {
             Point newStartPos = stix.getStixCoordinates().getFirst();
            this.setX((int) newStartPos.getX());
            this.setY((int) newStartPos.getY());
            stix.emptyStix();
        }
    }
}