package nl.tudelft.sem.group2.units;

import java.awt.Point;
import java.util.LinkedList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import nl.tudelft.sem.group2.AreaState;

import static nl.tudelft.sem.group2.game.Board.gridToCanvas;
import static nl.tudelft.sem.group2.global.Globals.BOARD_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.BOARD_WIDTH;

/**
 * A cursor which can travel over lines and is controlled by user input (arrow keys).
 */
public class Cursor extends LineTraveller {
    private final int animationSpeed = 30;
    private KeyCode currentMove = null;
    private int loops = 0;
    private int speed = 2;
    private LinkedList<double[][]> oldLines = new LinkedList<double[][]>();
    //todo set stix to false when implementation is done
    private boolean stix = true;
    private boolean isDrawing;
    private boolean isFast;

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
        this.sprite = new Image[1];
        this.sprite[0] = new Image("/images/cursor.png");
    }

    @Override
    public void move() {
        for (int i = 0; i < speed; i++) {
            if (currentMove != null) {
                switch (currentMove) {
                    case LEFT:
                        if (x > 0) {
                            moveLeft();
                        }
                        break;
                    case RIGHT:
                        if (x < BOARD_WIDTH / 2) {
                            moveRight();
                        }
                        break;
                    case UP:
                        if (y > 0) {
                            moveUp();
                        }
                        break;
                    case DOWN:
                        if (y < BOARD_HEIGHT / 2) {
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
        if (uncoveredOn(x - 1, y) && isDrawing) {
            if (!areaTracker.getStix().contains(new Point(x - 1, y))
                    && !areaTracker.getStix().contains(new Point(x - 2, y))) {
                if (areaTracker.getBoardGrid()[x - 1][y - 1].equals(AreaState.UNCOVERED)
                        && areaTracker.getBoardGrid()[x - 1][y + 1].equals(AreaState.UNCOVERED)) {

                    if (outerBorderOn(x, y)) {
                        areaTracker.addToStix(new Point(x, y));
                    }
                    x--;
                    areaTracker.addToStix(new Point(x, y));
                }
            }
        } else if (outerBorderOn(x - 1, y)) {
            x--;
        }
    }

    private void moveRight() {
        if (uncoveredOn(x + 1, y) && isDrawing) {
            if (!areaTracker.getStix().contains(new Point(x + 1, y))
                    && !areaTracker.getStix().contains(new Point(x + 2, y))) {
                if (areaTracker.getBoardGrid()[x + 1][y - 1].equals(AreaState.UNCOVERED)
                        && areaTracker.getBoardGrid()[x + 1][y + 1].equals(AreaState.UNCOVERED)) {
                    if (outerBorderOn(x, y)) {
                        areaTracker.addToStix(new Point(x, y));
                    }
                    x++;
                    areaTracker.addToStix(new Point(x, y));
                }
            }
        } else if (outerBorderOn(x + 1, y)) {
            x++;
        }
    }

    private void moveDown() {
        if (uncoveredOn(x, y + 1) && isDrawing) {
            if (!areaTracker.getStix().contains(new Point(x, y + 1))
                    && !areaTracker.getStix().contains(new Point(x, y + 2))) {
                if (areaTracker.getBoardGrid()[x + 1][y + 1].equals(AreaState.UNCOVERED)
                        && areaTracker.getBoardGrid()[x - 1][y + 1].equals(AreaState.UNCOVERED)) {
                    if (outerBorderOn(x, y)) {
                        areaTracker.addToStix(new Point(x, y));
                    }
                    y++;
                    areaTracker.addToStix(new Point(x, y));
                }
            }
        } else if (outerBorderOn(x, y + 1)) {
            y++;
        }
    }

    private void moveUp() {
        if (uncoveredOn(x, y - 1) && isDrawing) {
            if (!areaTracker.getStix().contains(new Point(x, y - 1))
                    && !areaTracker.getStix().contains(new Point(x, y - 2))) {
                if (areaTracker.getBoardGrid()[x + 1][y - 1].equals(AreaState.UNCOVERED)
                        && areaTracker.getBoardGrid()[x - 1][y - 1].equals(AreaState.UNCOVERED)) {
                    if (outerBorderOn(x, y)) {
                        areaTracker.addToStix(new Point(x, y));
                    }
                    y--;
                    areaTracker.addToStix(new Point(x, y));
                }
            }
        } else if (outerBorderOn(x, y - 1)) {
            y--;
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
        int drawX = gridToCanvas(x);
        int drawY = gridToCanvas(y);
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
                sprite[spriteIndex],
                drawX - width / 2 + 1,
                drawY - height / 2 + 1,
                width,
                height
        );
        spriteIndex = (spriteIndex + 1) % sprite.length;
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
}