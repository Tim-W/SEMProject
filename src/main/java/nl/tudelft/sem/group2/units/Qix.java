package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.LaunchApp;
import nl.tudelft.sem.group2.Logger;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;

import nl.tudelft.sem.group2.AreaState;
import static nl.tudelft.sem.group2.game.Board.gridToCanvas;
import nl.tudelft.sem.group2.global.Globals;

/**
 * A Qix is an enemy unit.
 * It moves randomly on the GameScene.
 * When the player touches the Qix while drawing,
 * or when the Qix touches the stix, it is game over.
 */
public class Qix extends Unit {

    private static final int LINE_LENGTH = 5;
    private static final int POSITION_LENGTH = 4;
    private static final double MINIMUM_COLOR_BRIGHTNESS = 0.3;
    private static final int PRECISION = 6;
    private static final int LINESCOUNT = 10;
    private static final int RANDOMNESSPOSITIONLENGTH = 4;
    private static final int RANDOMNESSLINELENGTH = 2;
    private static final int COLLISIONSIZE = 10;
    private static final Logger LOGGER = LaunchApp.getLogger();
    private int animationLoops = 0;
    private float[] direction = new float[2];
    private LinkedList<float[]> oldDirections = new LinkedList<float[]>();
    private LinkedList<float[]> oldCoordinates = new LinkedList<float[]>();
    private LinkedList<double[]> colorArray = new LinkedList<double[]>();
    private float[] coordinate = new float[2];

    /**
     * Create a new Qix.
     * Is by default placed on 30,30.
     * last parameters are for width and height but its just set to 1
     */
    public Qix() {
        super(Globals.QIX_START_X, Globals.QIX_START_Y, 1, 1);
    }

    @Override
    public void move() {
        coordinate[0] = getX();
        coordinate[1] = getY();
        if (animationLoops <= 0) {
            changeDirection();
        }
        checkLineCollision();
        this.setX((int) (coordinate[0] + direction[0]));
        this.setY((int) (coordinate[1] + direction[1]));
        coordinate[0] = getX();
        coordinate[1] = getY();
        float length = (float) Math.sqrt(direction[0] * direction[0] + direction[1] * direction[1]);
        float random = (float) Math.random() * RANDOMNESSLINELENGTH - RANDOMNESSLINELENGTH / 2;
        float scale = (LINE_LENGTH + random) / length;
        direction[0] *= scale;
        direction[1] *= scale;
        double[] colors = new double[3];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = Math.random() * (1 - MINIMUM_COLOR_BRIGHTNESS) + MINIMUM_COLOR_BRIGHTNESS;
        }
        colorArray.addFirst(colors);
        oldDirections.addFirst(new float[] {direction[0], direction[1]});
        oldCoordinates.addFirst(new float[] {coordinate[0], coordinate[1]});
        if (oldDirections.size() > LINESCOUNT) {
            oldDirections.removeLast();
            oldCoordinates.removeLast();
            colorArray.removeLast();
        }
        animationLoops--;
        logCurrentMove();
    }

    /**
     * create random direction for the qix.
     */
    private void changeDirection() {
        float length;
        do {
            direction[0] = Math.round(Math.random() * PRECISION) - PRECISION / 2;
            direction[1] = Math.round(Math.random() * PRECISION) - PRECISION / 2;
            length = (float) Math.sqrt(direction[0] * direction[0] + direction[1] * direction[1]);
        } while (length == 0);
        float random = (float) Math.random() * RANDOMNESSPOSITIONLENGTH - RANDOMNESSPOSITIONLENGTH / 2;
        float scale = (POSITION_LENGTH + random) / length;
        direction[0] *= scale;
        direction[1] *= scale;
        animationLoops += (int) (Math.random() * LINESCOUNT);
    }

    /**
     * Draw the qix on the canvas.
     * This incudes all the lines of the qix.
     */
    @Override
    public void draw(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.RED);
        for (int i = 0; i < oldDirections.size(); i++) {
            //get the random colors for the line
            gc.setStroke(Color.color(colorArray.get(i)[0], colorArray.get(i)[1], colorArray.get(i)[2]));
            gc.beginPath();
            //point 1 of the line
            float x1 = gridToCanvas((int) (oldCoordinates.get(i)[0] + oldDirections.get(i)[1]));
            float y1 = gridToCanvas((int) (oldCoordinates.get(i)[1] - oldDirections.get(i)[0]));
            //point 2 of the line
            float x2 = gridToCanvas((int) (oldCoordinates.get(i)[0] - oldDirections.get(i)[1]));
            float y2 = gridToCanvas((int) (oldCoordinates.get(i)[1] + oldDirections.get(i)[0]));
            //draw the line
            gc.moveTo(x1, y1);
            gc.lineTo(x2, y2);
            gc.stroke();
        }
    }

    /**
     * Functions reverts the direction of the qix if there is a innerborder or outerborder close to the qix.
     */
    private void checkLineCollision() {
        int gridLength = getAreaTracker().getBoardGrid().length;
        float length = (float) Math.sqrt(direction[0] * direction[0] + direction[1] * direction[1]);
        //loop through the grid
        for (int i = 0; i < gridLength; i++) {
            for (int j = 0; j < gridLength; j++) {
                if (getAreaTracker().getBoardGrid()[i][j] == AreaState.INNERBORDER
                        || getAreaTracker().getBoardGrid()[i][j] == AreaState.OUTERBORDER) {
                    float dx = coordinate[0] - i;
                    float dy = coordinate[1] - j;
                    float lengthNew = (float) Math.sqrt(dx * dx + dy * dy);
                    //if gridpoint is closer to the qix than COLLISIONSIZE revert the qix
                    if (lengthNew < COLLISIONSIZE) {
                        dx /= lengthNew;
                        dy /= lengthNew;
                        dx *= length;
                        dy *= length;
                        direction[0] = dx;
                        direction[1] = dy;
                        return;
                    }
                }
            }
        }
    }

    /**
     * Converts to a polygon.
     *
     * @return some polygon
     */
    public Polygon toPolygon() {
        ArrayList<Integer> xCor = new ArrayList<Integer>();
        ArrayList<Integer> yCor = new ArrayList<Integer>();

        for (int i = 0; i < this.getOldCoordinates().size(); i++) {
            xCor.add(Math.round(this.getOldCoordinates().get(i)[0]
                    + this.getOldDirections().get(i)[1]));
            xCor.add(Math.round(this.getOldCoordinates().get(i)[0]
                    - this.getOldDirections().get(i)[1]));

            yCor.add(Math.round(this.getOldCoordinates().get(i)[1]
                    + this.getOldDirections().get(i)[0]));
            yCor.add(Math.round(this.getOldCoordinates().get(i)[1]
                    - this.getOldDirections().get(i)[0]));
        }

        int[] xArr = new int[xCor.size()];
        int[] yArr = new int[xCor.size()];

        Iterator<Integer> xIterator = xCor.iterator();
        Iterator<Integer> yIterator = yCor.iterator();

        for (int i = 0; i < xArr.length; i++) {
            xArr[i] = xIterator.next();
            yArr[i] = yIterator.next();
        }

        return new Polygon(xArr, yArr, xArr.length);
    }

    /**
     * @return string representation of a Qix
     */
    public String toString() {
        return "Qix";
    }

    public LinkedList<float[]> getOldCoordinates() {
        return oldCoordinates;
    }

    public float[] getCoordinate() {
        return coordinate;
    }

    public LinkedList<float[]> getOldDirections() {
        return oldDirections;
    }

    /**
     * Method which logs the current movement of the qix.
     * Only gets executed when log level is on detailledLogging.
     */
    public void logCurrentMove() {
        LOGGER.log(Level.FINE, "Qix moved to (" + getX() + "," + getY() + ")", this.getClass());
    }
}
