package nl.tudelft.sem.group2.units;

import javafx.scene.image.Image;
import nl.tudelft.sem.group2.board.AreaTracker;
import nl.tudelft.sem.group2.board.BoardGrid;
import nl.tudelft.sem.group2.collisions.CollisionInterface;

import java.awt.Point;

import static nl.tudelft.sem.group2.global.Globals.BOARD_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.BOARD_WIDTH;
import static nl.tudelft.sem.group2.global.Globals.FUSE_DELAY;

/**
 * Describes the Fuse.
 * A Fuse is an enemy that travels over the stix which the cursor draws,
 * but it only moves over the stix when the cursor is standing still.
 */
public class Fuse extends LineTraveller implements CollisionInterface {
    private int speed = 1;
    private int lastX;
    private int lastY;
    private boolean moving = true;
    private Stix stix;
    private int delay;

    /**
     * Create a new Fuse.
     *
     * @param x           the start x coord
     * @param y           the start y coord
     * @param width       width of the fuse (used for collision detection)
     * @param height      height of the fuse (used for collision detection)
     * @param grid used for calculating areas
     * @param stix        current stix to use
     */
    public Fuse(int x, int y, int width, int height, BoardGrid grid, Stix stix) {
        super(x, y, width, height, grid);
        Image[] sprite = new Image[2];
        lastX = x;
        lastY = y;
        sprite[0] = new Image("/images/fuse-1.png");
        sprite[1] = new Image("/images/fuse-2.png");
        setSprite(sprite);
        this.stix = stix;
        delay = FUSE_DELAY;
    }

    @Override
    public void move() {
        if (delay > 0) {
            delay--;
        } else if (moving) {
            for (int i = 0; i < speed; i++) {
                if (getX() < BOARD_WIDTH / 2
                        && !(getX() + 1 == lastX)
                        && stix.getStixCoordinates().contains(new Point(getIntX() + 1, getIntY()))) {
                    setLastCoordinates(getIntX(), getIntY());
                    setX(getIntX() + 1);
                } else if (getY() < BOARD_HEIGHT / 2
                        && !(lastY == getY() + 1)
                        && stix.getStixCoordinates().contains(new Point(getIntX(), getIntY() + 1))) {
                    setLastCoordinates(getIntX(), getIntY());
                    setY(getIntY() + 1);
                } else if (getX() > 0
                        && !(lastX == getX() - 1)
                        && stix.getStixCoordinates().contains(new Point(getIntX() - 1, getIntY()))) {
                    setLastCoordinates(getIntX(), getIntY());
                    setX(getIntX() - 1);
                } else if (getY() > 0
                        && !(lastY == getY() - 1)
                        && stix.getStixCoordinates().contains(new Point(getIntX(), getIntY() - 1))) {
                    setLastCoordinates(getIntX(), getIntY());
                    setY(getIntY() - 1);
                }
            }
        }
    }

    private void setLastCoordinates(int x, int y) {
        this.lastX = x;
        this.lastY = y;
    }

    /**
     * @return string representation
     */
    public String toString() {
        return "Fuse";
    }

    public void setLastX(int lastX) {
        this.lastX = lastX;
    }

    public void setLastY(int lastY) {
        this.lastY = lastY;
    }

    public void moving() {
        this.moving = true;
    }
    public void notMoving() {
        this.moving = false;
    }

    /**
     * @param delay The value of the new delay
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }
}
