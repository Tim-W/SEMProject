package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.board.AreaState;
import nl.tudelft.sem.group2.board.AreaTracker;
import nl.tudelft.sem.group2.Logger;
import nl.tudelft.sem.group2.board.BoardGrid;
import nl.tudelft.sem.group2.board.Coordinate;
import nl.tudelft.sem.group2.collisions.CollisionInterface;
import nl.tudelft.sem.group2.global.Globals;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.logging.Level;

import static nl.tudelft.sem.group2.global.Globals.GRID_SURFACE;
import static nl.tudelft.sem.group2.scenes.GameScene.gridToCanvas;

/**
 * A Qix is an enemy unit.
 * It moves randomly on the board.
 * When the player touches the Qix while drawing,
 * or when the Qix touches the stix, the player loses a life and must start drawing again.
 */
public class Qix extends Unit implements CollisionInterface, Observer {

    private static final int POSITION_LENGTH = 4;
    private static final double MINIMUM_COLOR_BRIGHTNESS = 0.3;
    private static final int PRECISION = 6;
    private static final int LINESCOUNT = 10;
    private static final int RANDOMNESSPOSITIONLENGTH = 4;
    private static final int RANDOMNESSLINELENGTH = 2;
    private static final int COLLISIONSIZE = 10;
    private static final double DECREASELINESIZE = 0.1;
    private static final double DIVIDESTARTLINELENGTH = 8;
    private static final double MULTIPLIER = 1.5;
    private static final Logger LOGGER = Logger.getLogger();

    private int startLineLength;
    private double lineLength;
    private int animationLoops = 0;
    private float[] direction = new float[2];
    //TODO use the Coordinate class
    private LinkedList<float[]> oldDirections = new LinkedList<>();
    private LinkedList<float[]> oldCoordinates = new LinkedList<>();
    private LinkedList<double[]> colorArray = new LinkedList<>();
    private float[] coordinate = new float[2];

    /**
     * Create a new Qix.
     * Is by default placed on 30,30.
     * last parameters are for width and height but its just set to 1
     *
     * @param startLineLength the start line length of the qix
     */
    public Qix(int startLineLength) {
        super(Globals.QIX_START_X, Globals.QIX_START_Y, 1, 1);
        this.startLineLength = startLineLength;
        lineLength = startLineLength;
        LOGGER.log(Level.INFO, this.toString() + " created at (" + Globals.QIX_START_X + ","
                + Globals.QIX_START_Y + ")", this.getClass());
    }

    public static int getLINESCOUNT() {
        return LINESCOUNT;
    }

    @Override
    public void move() {
        coordinate[0] = getIntX();
        coordinate[1] = getIntY();
        if (animationLoops <= 0) {
            changeDirection();
        }
        checkLineCollision();
        this.setX((int) (coordinate[0] + direction[0]));
        this.setY((int) (coordinate[1] + direction[1]));
        coordinate[0] = getIntX();
        coordinate[1] = getIntY();
        float length = (float) Math.sqrt(direction[0] * direction[0] + direction[1] * direction[1]);
        float random = (float) Math.random() * RANDOMNESSLINELENGTH - RANDOMNESSLINELENGTH / 2;
        double scale = (lineLength + random) / length;
        direction[0] *= scale;
        direction[1] *= scale;
        double[] colors = new double[3];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = Math.random() * (1 - MINIMUM_COLOR_BRIGHTNESS) + MINIMUM_COLOR_BRIGHTNESS;
        }
        getColorArray().addFirst(colors);
        getOldDirections().addFirst(new float[]{direction[0], direction[1]});
        getOldCoordinates().addFirst(new float[]{coordinate[0], coordinate[1]});
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
            setDirection(Math.round(Math.random() * PRECISION) - PRECISION / 2, 0);
            setDirection(Math.round(Math.random() * PRECISION) - PRECISION / 2, 1);
            length = (float) Math.sqrt(getDirection(0) * getDirection(0) + getDirection(1) * getDirection(1));
        } while (length == 0);
        float random = (float) Math.random() * RANDOMNESSPOSITIONLENGTH - RANDOMNESSPOSITIONLENGTH / 2;
        float scale = (POSITION_LENGTH + random) / length;
        direction[0] *= scale;
        direction[1] *= scale;
        Random rng = new Random();
        animationLoops += rng.nextInt(LINESCOUNT);
    }

    /**
     * Draw the qix on the canvas.
     * This incudes all the lines of the qix.
     */
    @Override
    public void draw(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.RED);
        for (int i = 0; i < getOldDirections().size(); i++) {
            //get the random colors for the line
            gc.setStroke(Color.color(colorArray.get(i)[0], colorArray.get(i)[1], colorArray.get(i)[2]));
            gc.beginPath();
            //point 1 of the line
            float x1 = gridToCanvas((int) (getOldCoordinate(i)[0] + getOldDirection(i)[1]));
            float y1 = gridToCanvas((int) (getOldCoordinate(i)[1] - getOldDirection(i)[0]));
            //point 2 of the line
            float x2 = gridToCanvas((int) (getOldCoordinate(i)[0] - getOldDirection(i)[1]));
            float y2 = gridToCanvas((int) (getOldCoordinate(i)[1] + getOldDirection(i)[0]));
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
        float length = (float) Math.sqrt(direction[0] * direction[0] + direction[1] * direction[1]);
        //loop through the grid
        for (int i = 0; i < grid.getWidth(); i++) {
            for (int j = 0; j < grid.getWidth(); j++) {
                if (grid.isInnerborder(i, j) || grid.isOuterborder(i, j)) {
                    float dx = getCoordinate(0) - i;
                    float dy = getCoordinate(1) - j;
                    float lengthNew = (float) Math.sqrt(dx * dx + dy * dy);
                    //if gridpoint is closer to the qix than COLLISIONSIZE revert the qix
                    if (lengthNew < COLLISIONSIZE) {
                        dx /= lengthNew;
                        dy /= lengthNew;
                        dx *= length;
                        dy *= length;
                        setDirection(dx, 0);
                        setDirection(dy, 1);
                        return;
                    }
                }
            }
        }
    }

    public void setLineLength(int lineLength) {
        this.lineLength = lineLength;
    }

    /**
     * Converts to a polygon.
     *
     * @return some polygon
     */
    public Polygon toPolygon() {
        ArrayList<Integer> xCor = new ArrayList<>();
        ArrayList<Integer> yCor = new ArrayList<>();

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

    @Override
    public boolean intersect(Unit collidee) {
        if (!(collidee instanceof Qix)) {
            Polygon colliderP = this.toPolygon();

            // subtract one from width&height to make collisions look more real
            Rectangle collideeR = collidee.toRectangle();
            if (colliderP.intersects(collideeR)) {
                LOGGER.log(Level.INFO, this.toString() + " collided with " + collidee.toString()
                        + " at (" + this.getX() + "," + this.getY() + ")", this.getClass());
            }
            return colliderP.intersects(collideeR);
        }
        return false;
    }

    /**
     * @return string representation of a Qix
     */
    public String toString() {
        return "Qix";
    }

    LinkedList<float[]> getOldCoordinates() {
        return oldCoordinates;
    }

    void setOldCoordinates(LinkedList<float[]> oldCoordinates) {
        this.oldCoordinates = oldCoordinates;
    }

    /**
     * Getter for an old coordinate.
     *
     * @param i describes if you want the x or the y.
     * @return the x or y coordinate
     */
    float[] getOldCoordinate(int i) {
        return oldCoordinates.get(i);
    }

    /**
     * Getter for current coordinate.
     *
     * @param i describes if you want the x or the y.
     * @return the x or y coordinate
     */
    float getCoordinate(int i) {
        return coordinate[i];
    }

    void setAnimationLoops(int animationLoops) {
        this.animationLoops = animationLoops;
    }

    LinkedList<float[]> getOldDirections() {
        return oldDirections;
    }

    void setOldDirections(LinkedList<float[]> oldDirections) {
        this.oldDirections = oldDirections;
    }

    /**
     * Getter for old direction.
     *
     * @param i describes if you want the x or the y.
     * @return the x or y coordinate
     */
    float[] getOldDirection(int i) {
        return oldDirections.get(i);
    }

    /**
     * Getter for current direction.
     *
     * @param i describes if you want the x or the y.
     * @return the x or y coordinate
     */
    float getDirection(int i) {
        return direction[i];
    }

    /**
     * Setter for a direction.
     *
     * @param direction the new direction
     * @param i         at which place
     */
    void setDirection(float direction, int i) {
        this.direction[i] = direction;
    }

    LinkedList<double[]> getColorArray() {
        return colorArray;
    }

    /**
     * Method which logs the current movement of the qix.
     * Only gets executed when log level is on detailledLogging.
     */
    private void logCurrentMove() {
        LOGGER.log(Level.FINE, "Qix moved to (" + getX() + "," + getY() + ")", this.getClass());
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof AreaTracker) {
            double multiplier = Math.pow(((double) arg) / GRID_SURFACE,
                    DECREASELINESIZE + startLineLength / DIVIDESTARTLINELENGTH) * MULTIPLIER;
            if (multiplier < 1) {
                lineLength = startLineLength * multiplier;
            }
        }
    }
}