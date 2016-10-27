package nl.tudelft.sem.group2.units;

import nl.tudelft.sem.group2.board.AreaTracker;
import nl.tudelft.sem.group2.Logger;
import nl.tudelft.sem.group2.board.BoardGrid;
import nl.tudelft.sem.group2.collisions.CollisionInterface;
import nl.tudelft.sem.group2.board.Coordinate;

import java.awt.Rectangle;
import java.util.logging.Level;

/**
 * An object that can be moved an drawn on a 2D grid.
 * Supports intersection checking between two units.
 */
public abstract class Unit extends Coordinate implements Draw, Movable, CollisionInterface {
    private static final Logger LOGGER = Logger.getLogger();
    protected BoardGrid grid;

    /**
     * Create a Unit at (x,y) position.
     *
     * @param coordinate  x coord
     * @param grid used for calculating areas
     */
    public Unit(Coordinate coordinate, BoardGrid grid) {
        super(coordinate.getIntX(), coordinate.getIntX(), coordinate.getWidth(), coordinate.getHeight());
        this.grid = grid;
        LOGGER.log(Level.INFO, this.toString() + " created at (" + x + "," + y + ")", this.getClass());
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
        Rectangle colliderR = this.toRectangle();
        // subtract one from width&height to make collisions look more real
        Rectangle collideeR = collidee.toRectangle();
        if (colliderR.intersects(collideeR)) {
            LOGGER.log(Level.WARNING, this.toString() + " collided with " + collidee.toString() + " at (" + this.getX()
                    + "," + this.getY() + ")", this.getClass());
            return true;
        }
        return false;
    }

    public Rectangle toRectangle() {
        return new Rectangle(this.x, this.y, this.getWidth(), this.getHeight());
    }

}
