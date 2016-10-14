package nl.tudelft.sem.group2.units;

import javafx.scene.image.Image;
import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.Logger;
import nl.tudelft.sem.group2.collisions.CollisionInterface;

import java.util.logging.Level;

import static nl.tudelft.sem.group2.global.Globals.BOARD_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.BOARD_WIDTH;

/**
 * An enemy unit which travels over the outerborders.
 * When a Sparx collides with a cursor, the views ends.
 */
public class Sparx extends LineTraveller implements CollisionInterface {
    private static final Logger LOGGER = Logger.getLogger();
    private final SparxDirection sparxDirection;
    private int speed = 2;
    private int lastX;
    private int lastY;

    /**
     * Create a new Sparx with its default sprites.
     *
     * @param x              x coord to start at
     * @param y              y coord to start at
     * @param width          width, used for collision
     * @param height         height, used for collision
     * @param areaTracker    used for calculating areas
     * @param sparxDirection direction in which the sparx starts moving,
     *                       which is either LEFT or RIGHT
     */
    public Sparx(int x, int y, int width, int height, AreaTracker areaTracker, SparxDirection sparxDirection) {
        super(x, y, width, height, areaTracker);
        lastX = x;
        lastY = y;
        Image[] sprite = new Image[2];
        sprite[0] = new Image("/images/sparx-1.png");
        sprite[1] = new Image("/images/sparx-2.png");
        setSprite(sprite);
        this.sparxDirection = sparxDirection;
    }

    @Override
    public void move() {

        for (int i = 0; i < speed; i++) {
            switch (sparxDirection) {
                case LEFT:
                    moveLeft();
                    break;
                case RIGHT:
                    moveRight();
                    break;
                default:
                    LOGGER.log(Level.WARNING, "Warning: unknown move direction", this.getClass());
                    break;
            }
        }
        logCurrentMove();
    }

    private void moveRight() {
        if (getX() < BOARD_WIDTH / 2 && !(getX() + 1 == lastX) && outerBorderOn(getX() + 1, getY())) {
            setXAndLastX(getX() + 1);
        } else if (getY() < BOARD_HEIGHT / 2 && !(lastY == getY() + 1) && outerBorderOn(getX(), getY() + 1)) {
            setYAndLastY(getY() + 1);
        } else if (getX() > 0 && !(lastX == getX() - 1) && outerBorderOn(getX() - 1, getY())) {
            setXAndLastX(getX() - 1);
        } else if (getY() > 0 && !(lastY == getY() - 1) && outerBorderOn(getX(), getY() - 1)) {
            setYAndLastY(getY() - 1);
        } else if (getX() < BOARD_WIDTH / 2 && !(getX() + 1 == lastX) && innerBorderOn(getX() + 1, getY())) {
            setXAndLastX(getX() + 1);
        } else if (getY() < BOARD_HEIGHT / 2 && !(lastY == getY() + 1) && innerBorderOn(getX(), getY() + 1)) {
            setYAndLastY(getY() + 1);
        } else if (getX() > 0 && !(lastX == getX() - 1) && innerBorderOn(getX() - 1, getY())) {
            setXAndLastX(getX() - 1);
        } else if (getY() > 0 && !(lastY == getY() - 1) && innerBorderOn(getX(), getY() - 1)) {
            setYAndLastY(getY() - 1);
        }

    }

    private void moveLeft() {
        if (getX() > 0 && !(lastX == getX() - 1) && outerBorderOn(getX() - 1, getY())) {
            setXAndLastX(getX() - 1);
        } else if (getY() > 0 && !(lastY == getY() - 1) && outerBorderOn(getX(), getY() - 1)) {
            setYAndLastY(getY() - 1);
        } else if (getX() < BOARD_WIDTH / 2 && !(getX() + 1 == lastX) && outerBorderOn(getX() + 1, getY())) {
            setXAndLastX(getX() + 1);
        } else if (getY() < BOARD_HEIGHT / 2 && !(lastY == getY() + 1) && outerBorderOn(getX(), getY() + 1)) {
            setYAndLastY(getY() + 1);
        } else if (getX() > 0 && !(lastX == getX() - 1) && innerBorderOn(getX() - 1, getY())) {
            setXAndLastX(getX() - 1);
        } else if (getY() > 0 && !(lastY == getY() - 1) && innerBorderOn(getX(), getY() - 1)) {
            setYAndLastY(getY() - 1);
        } else if (getX() < BOARD_WIDTH / 2 && !(getX() + 1 == lastX) && innerBorderOn(getX() + 1, getY())) {
            setXAndLastX(getX() + 1);
        } else if (getY() < BOARD_HEIGHT / 2 && !(lastY == getY() + 1) && innerBorderOn(getX(), getY() + 1)) {
            setYAndLastY(getY() + 1);
        }
    }

    private void setLastCoordinates(int x, int y) {
        this.lastX = x;
        this.lastY = y;
    }

    /**
     * Setter for x coordinates.
     *
     * @param x new x value
     */
    public void setXAndLastX(int x) {
        setLastCoordinates(getX(), getY());
        setX(x);
    }

    /**
     * Setter for y coordinates.
     *
     * @param y new y value
     */
    public void setYAndLastY(int y) {
        setLastCoordinates(getX(), getY());
        setY(y);
    }

    /**
     * @return string representation of a Sparx
     */
    public String toString() {
        return "Sparx";
    }

    /**
     * Log the current movement of the sparx.
     * Only gets executed when log level is on detailledLogging.
     */
    public void logCurrentMove() {
        LOGGER.log(Level.FINE, "Sparx moved to (" + getX() + "," + getY() + ")", this.getClass());
    }
}
