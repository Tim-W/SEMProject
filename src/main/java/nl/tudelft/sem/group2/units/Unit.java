package nl.tudelft.sem.group2.units;

import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.Logger;
import nl.tudelft.sem.group2.collisions.CollisionInterface;

import java.awt.Rectangle;
import java.util.logging.Level;

/**
 * An object that can be moved an drawn on a 2D grid.
 * Supports intersection checking between two units.
 */
public abstract class Unit implements Draw, Movable, CollisionInterface {
    private static final Logger LOGGER = Logger.getLogger();
    private int x;
    private int y;
    private int width;
    private int height;
    private AreaTracker areaTracker;

    /**
     * Create a Unit at (x,y) position.
     *
     * @param x           x coord
     * @param y           y coord
     * @param width       width, used for collision
     * @param height      height, used for collision
     * @param areaTracker used for calculating areas
     */
    public Unit(int x, int y, int width, int height, AreaTracker areaTracker) {
        this.setX(x);
        this.setY(y);
        this.setWidth(width);
        this.setHeight(height);
        this.setAreaTracker(areaTracker);
        LOGGER.log(Level.INFO, this.toString() + " created at (" + x + "," + y + ")", this.getClass());
    }


    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Check for intersection between current unit and another unit.
     *
     * @param collidee the other unit
     * @return true if the collidee is on the same (x,y) coordinate as the current unit
     */
    public boolean intersect(Unit collidee) {
        if (collidee instanceof Qix) {
            Qix qix = (Qix) collidee;
            return qix.intersect(this);
        }
        Rectangle colliderR = new Rectangle(this.getX(), this.getY(), 2, 2);
        // subtract one from width&height to make collisions look more real
        Rectangle collideeR = new Rectangle(collidee.getX(), collidee.getY(),
                2, 2);
        if (colliderR.intersects(collideeR)) {
            LOGGER.log(Level.WARNING, this.toString() + " collided with " + collidee.toString() + " at (" + this.getX()
                    + "," + this.getY() + ")", this.getClass());
            return true;
        }
        return false;
    }

    public AreaTracker getAreaTracker() {
        return areaTracker;
    }

    public void setAreaTracker(AreaTracker areaTracker) {
        this.areaTracker = areaTracker;
    }
}
