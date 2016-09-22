package nl.tudelft.sem.group2.units;

import java.awt.Point;
import javafx.scene.image.Image;

import static nl.tudelft.sem.group2.global.Globals.BOARD_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.BOARD_WIDTH;
import static nl.tudelft.sem.group2.global.Globals.CURSOR_START_X;
import static nl.tudelft.sem.group2.global.Globals.CURSOR_START_Y;

/**
 * Describes the Fuse.
 * A Fuse is an enemy that travels over the stix which the cursor draws,
 * but it only moves over the stix when the cursor is standing still.
 */
public class Fuse extends LineTraveller {
    private int speed = 1;
    private int lastX = CURSOR_START_X;
    private int lastY = CURSOR_START_Y;
    private boolean moving = true;

    /**
     * Create a new Fuse.
     *
     * @param x      the start x coord
     * @param y      the start y coord
     * @param width  width of the fuse (used for collision detection)
     * @param height height of the fuse (used for collision detection)
     */
    public Fuse(int x, int y, int width, int height) {
        super(x, y, width, height);
        Image[] sprite = new javafx.scene.image.Image[2];
        sprite[0] = new javafx.scene.image.Image("/images/fuse-1.png");
        sprite[1] = new javafx.scene.image.Image("/images/fuse-2.png");
        setSprite(sprite);
    }

    @Override
    public void move() {
        if (moving) {
            for (int i = 0; i < speed; i++) {
                if (x < BOARD_WIDTH / 2
                        && !(x + 1 == lastX)
                        && areaTracker.getStix().contains(new Point(x + 1, y))) {
                    setLastCoordinates(x, y);
                    setX(x + 1);
                } else if (y < BOARD_HEIGHT / 2
                        && !(lastY == y + 1)
                        && areaTracker.getStix().contains(new Point(x, y + 1))) {
                    setLastCoordinates(x, y);
                    setY(y + 1);
                } else if (x > 0
                        && !(lastX == x - 1)
                        && areaTracker.getStix().contains(new Point(x - 1, y))) {
                    setLastCoordinates(x, y);
                    setX(x - 1);
                } else if (y > 0
                        && !(lastY == y - 1)
                        && areaTracker.getStix().contains(new Point(x, y - 1))) {
                    setLastCoordinates(x, y);
                    setY(y - 1);
                }
            }
        }
    }

    private void setLastCoordinates(int x, int y) {
        this.lastX = x;
        this.lastY = y;
    }

    /**
     *
     * @return string representation
     */
    public String toString() {
        return "Fuse";
    }

    /**
     *
     * @param moving if the fuse is moving
     */
    public void setMoving(boolean moving) {
        this.moving = moving;
    }
}
