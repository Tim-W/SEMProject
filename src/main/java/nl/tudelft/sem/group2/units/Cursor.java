package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import nl.tudelft.sem.group2.AreaState;
import nl.tudelft.sem.group2.LaunchApp;
import nl.tudelft.sem.group2.Logger;

import java.awt.Point;
import java.util.LinkedList;
import java.util.logging.Level;

import static nl.tudelft.sem.group2.game.Board.gridToCanvas;
import static nl.tudelft.sem.group2.global.Globals.BOARD_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.BOARD_WIDTH;

/**
 * A cursor which can travel over lines and is controlled by user input (arrow keys).
 */
public class Cursor extends LineTraveller {
    private static final Logger LOGGER = LaunchApp.getLogger();
    private KeyCode currentMove = null;
    private final int animationSpeed = 30;
    private int loops = 0;
    private int speed = 2;
    private LinkedList<double[][]> oldLines = new LinkedList<double[][]>();
    //TODO set stix to false when implementation is done
    private boolean isDrawing = false;
    private boolean isFast = true;

    /**
     * Create a cursor.
     *
     * @param x      start x coordinate
     * @param y      start y coordinate
     * @param width  width, used for collision detection
     * @param height height, used for collision detection
     */
    public Cursor(int x, int y, int width, int height) {
        super(x, y, width, height);
        Image[] sprite = new Image[1];
        sprite[0] = new Image("/images/cursor.png");
        setSprite(sprite);
    }

    @Override
    public void move() {
        for (int i = 0; i < speed; i++) {
            if (currentMove != null) {
                switch (currentMove) {
                    case LEFT:
                        if (getX() > 0) {
                            moveLeft();
                        }
                        break;
                    case RIGHT:
                        if (getX() < BOARD_WIDTH / 2) {
                            moveRight();
                        }
                        break;
                    case UP:
                        if (getY() > 0) {
                            moveUp();
                        }
                        break;
                    case DOWN:
                        if (getY() < BOARD_HEIGHT / 2) {
                            moveDown();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void moveLeft() {
        if (uncoveredOn(getX() - 1, getY()) && isDrawing) {
            if (!getAreaTracker().getStix().contains(new Point(getX() - 1, getY()))
                    && !getAreaTracker().getStix().contains(new Point(getX() - 2, getY())) &&
                    getAreaTracker().getBoardGrid()[getX() - 1][getY() - 1].equals(AreaState.UNCOVERED)
                    && getAreaTracker().getBoardGrid()[getX() - 1][getY() + 1].equals(AreaState.UNCOVERED)) {

                    if (outerBorderOn(getX(), getY())) {
                        getAreaTracker().addToStix(new Point(getX(), getY()));
                    }
                    setX(getX() - 1);
                    logCurrentMove();
                    getAreaTracker().addToStix(new Point(getX(), getY()));

            }
        } else if (outerBorderOn(getX() - 1, getY())) {
            setX(getX() - 1);
            logCurrentMove();
        }
    }

    private void moveRight() {
        if (uncoveredOn(getX() + 1, getY()) && isDrawing) {
            if (!getAreaTracker().getStix().contains(new Point(getX() + 1, getY()))
                    && !getAreaTracker().getStix().contains(new Point(getX() + 2, getY())) &&
                    getAreaTracker().getBoardGrid()[getX() + 1][getY() - 1].equals(AreaState.UNCOVERED)
                    && getAreaTracker().getBoardGrid()[getX() + 1][getY() + 1].equals(AreaState.UNCOVERED)) {

                    if (outerBorderOn(getX(), getY())) {
                        getAreaTracker().addToStix(new Point(getX(), getY()));
                    }
                    setX(getX() + 1);
                    logCurrentMove();
                    getAreaTracker().addToStix(new Point(getX(), getY()));

            }
        } else if (outerBorderOn(getX() + 1, getY())) {
            setX(getX() + 1);
            logCurrentMove();
        }
    }

    private void moveDown() {
        if (uncoveredOn(getX(), getY() + 1) && isDrawing) {
            if (!getAreaTracker().getStix().contains(new Point(getX(), getY() + 1))
                    && !getAreaTracker().getStix().contains(new Point(getX(), getY() + 2)) &&
                    getAreaTracker().getBoardGrid()[getX() + 1][getY() + 1].equals(AreaState.UNCOVERED)
                    && getAreaTracker().getBoardGrid()[getX() - 1][getY() + 1].equals(AreaState.UNCOVERED)) {

                    if (outerBorderOn(getX(), getY())) {
                        getAreaTracker().addToStix(new Point(getX(), getY()));
                    }
                    setY(getY() + 1);
                    logCurrentMove();
                    getAreaTracker().addToStix(new Point(getX(), getY()));

            }
        } else if (outerBorderOn(getX(), getY() + 1)) {
            setY(getY() + 1);
            logCurrentMove();
        }
    }

    private void moveUp() {
        if (uncoveredOn(getX(), getY() - 1) && isDrawing) {
            if (!getAreaTracker().getStix().contains(new Point(getX(), getY() - 1))
                    && !getAreaTracker().getStix().contains(new Point(getX(), getY() - 2)) &&
                    getAreaTracker().getBoardGrid()[getX() + 1][getY() - 1].equals(AreaState.UNCOVERED)
                    && getAreaTracker().getBoardGrid()[getX() - 1][getY() - 1].equals(AreaState.UNCOVERED)) {

                    if (outerBorderOn(getX(), getY())) {
                        getAreaTracker().addToStix(new Point(getX(), getY()));
                    }
                    setY(getY() - 1);
                    logCurrentMove();
                    getAreaTracker().addToStix(new Point(getX(), getY()));

            }
        } else if (outerBorderOn(getX(), getY() - 1)) {
            setY(getY() - 1);
            logCurrentMove();
        }
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

        setSpriteIndex(getSpriteIndex() + 1 % getSprite().length);
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

    /**
     * @param drawing if cursor is moving while user has key X or Z down
     */
    public void setDrawing(boolean drawing) {
        isDrawing = drawing;
    }

    /**
     *
     * @param speed the amount of pixels the cursor moves per when moving
     */
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    /**
     *
     * @return whether the cursor is moving fast or slow
     */
    public boolean isFast() {
        return isFast;
    }

    /**
     *
     * @param isFast whether the cursor is moving fast or slow
     */
    public void setFast(boolean isFast) {
        this.isFast = isFast;
    }

    public void logCurrentMove() {
        LOGGER.log(Level.FINE, "Cursor moved to (" + getX() + "," + getY() + ")", this.getClass());
    }
}