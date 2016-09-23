package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.LaunchApp;
import nl.tudelft.sem.group2.Logger;
import nl.tudelft.sem.group2.scenes.GameScene;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.logging.Level;

/**
 * An object that can be moved an drawn on a 2D grid.
 * Supports intersection checking between two units.
 */
public abstract class Unit {
    private int x;
    private int y;
    private int width;
    private int height;
    private AreaTracker areaTracker;
    private static final Logger LOGGER = LaunchApp.getLogger();

    /**
     * Create a Unit at (x,y) position.
     *
     * @param x x coord
     * @param y y coord
     * @param width  width, used for collision
     * @param height height, used for collision
     */
    Unit(int x, int y, int width, int height) {
        this.setX(x);
        this.setY(y);
        this.setWidth(width);
        this.setHeight(height);
        this.setAreaTracker(GameScene.getAreaTracker());
        LOGGER.log(Level.INFO, this.toString() + " created at (" + x + "," + y + ")", this.getClass());
    }

    /**
     * Every frame, this method should be called.
     * The x and y coordinates may be changed using setX, setY,
     * after which the unit can take another position on the screen.
     */
    public abstract void move();

    /**
     * Every frame, this method should be called.
     * It should draw a sprite or a list of sprites on a canvas.
     * @param canvas the canvas to draw on
     */
    public abstract void draw(Canvas canvas);

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
     * @param collidee the other unit
     * @return true if the collidee is on the same (x,y) coordinate as the current unit
     */
    public boolean intersect(Unit collidee) {
        if (this instanceof Qix) {
            Qix qix = (Qix) this;
            Polygon colliderP = qix.toPolygon();

            // subtract one from width&height to make collisions look more real
            Rectangle collideeR = new Rectangle(collidee.getX(),
                    collidee.getY(), collidee.getWidth() / 2 - 1,
                    collidee.getHeight() / 2 - 1);
            if (colliderP.intersects(collideeR)) {
                LOGGER.log(Level.INFO, this.toString() + " collided with "
                        + collidee.toString() + " at (" + this.getX()
                        + "," + this.getY() + ")", this.getClass());
                return true;
            } else {
                return false;
            }
        }

        if (collidee instanceof Qix) {
            Qix qix = (Qix) collidee;
            Polygon collideeP = qix.toPolygon();

            // subtract one from width&height to make collisions look more real
            Rectangle colliderR = new Rectangle(this.getX(), this.getY(),
                    this.getWidth() / 2 - 1, this.getHeight() / 2 - 1);
            if (collideeP.intersects(colliderR)) {
                LOGGER.log(Level.INFO, this.toString() + " collided with "
                        + collidee.toString() + " at (" + this.getX()
                        + "," + this.getY() + ")", this.getClass());
                return true;
            } else {
                return false;
            }
        }

        Rectangle colliderR = new Rectangle(this.getX(), this.getY(), 2, 2);
        // subtract one from width&height to make collisions look more real
        Rectangle collideeR = new Rectangle(collidee.getX(), collidee.getY(),
                2, 2);
        if (colliderR.intersects(collideeR)) {
            LOGGER.log(Level.INFO, this.toString() + " collided with "
                    + collidee.toString() + " at (" + this.getX()
                    + "," + this.getY() + ")", this.getClass());
        }
        return colliderR.intersects(collideeR);
    }

    public AreaTracker getAreaTracker() {
        return areaTracker;
    }

    public void setAreaTracker(AreaTracker areaTracker) {
        this.areaTracker = areaTracker;
    }
}
