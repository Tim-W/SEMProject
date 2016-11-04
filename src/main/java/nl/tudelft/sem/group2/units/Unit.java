package nl.tudelft.sem.group2.units;

import nl.tudelft.sem.group2.Logger;
import nl.tudelft.sem.group2.board.BoardGrid;
import nl.tudelft.sem.group2.board.Coordinate;
import nl.tudelft.sem.group2.collisions.CollisionInterface;
import nl.tudelft.sem.group2.gameController.GameController;

import java.awt.Rectangle;
import java.util.logging.Level;

/**
 * An object that can be moved an drawn on a 2D grid.
 * Supports intersection checking between two units.
 * This is the base class of most of the entities in 'Qix'.
 * This allows for standardized methods which are the same across all those entities.
 */
public abstract class Unit extends Coordinate implements Draw, Movable, CollisionInterface {

    private static final Logger LOGGER = Logger.getLogger();


    /**
     * Create a Unit at (x,y) position.
     *
     * @param x      x coord
     * @param y      y coord
     * @param width  width, used for the sprite
     * @param height height, used for the sprite
     */
    public Unit(int x, int y, int width, int height) {
        super(x, y, width, height);
        LOGGER.log(Level.INFO, this.toString() + " created at (" + x + "," + y + ")", this.getClass());
    }

    /**
     * The grid instance that holds the states of the coordinates of the board.
     *
     * @return the grid instance
     */
    protected BoardGrid getGrid() {
        return GameController.getInstance().getGrid();
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

    @Override
    public boolean equals(Object obj) {
        return this == obj;
    }

    @Override
    public int hashCode() {
        return super.hashCode() + LOGGER.hashCode();
    }

    /**
     * @return the rectangle representation of this Unit
     */
    public Rectangle toRectangle() {
        return new Rectangle(getX(), getY(), 2, 2);
    }

}
