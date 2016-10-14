package nl.tudelft.sem.group2.units;

import nl.tudelft.sem.group2.Logger;
import nl.tudelft.sem.group2.collisions.CollisionInterface;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.logging.Level;

/**
 * Class which keeps track of the current stix.
 */
public class Stix implements CollisionInterface {

    private static final Logger LOGGER = Logger.getLogger();
    private LinkedList<Point> stixCoordinates;

    /**
     * Constructor for stix class.
     */
    public Stix() {
        stixCoordinates = new LinkedList<>();
    }

    /**
     * Empties all the current coordinates out of current stix.
     */
    public void emptyStix() {
        //Empty the current stix
        stixCoordinates = null;
        stixCoordinates = new LinkedList<>();
    }

    /**
     * Checks if stix stix is empty.
     *
     * @return boolean isempty()
     */
    public boolean isStixEmpty() {
        return !(stixCoordinates != null && !stixCoordinates.isEmpty());
    }

    /**
     * Check if stix intersects with qix.
     *
     * @param unit the current unit casted to qix
     * @return if they intersect
     */
    public boolean intersect(Unit unit) {
        if (unit instanceof Qix) {

            Qix qix = (Qix) unit;
            if (!this.isStixEmpty()) {

                Polygon qixP = qix.toPolygon();
                for (Point point : this.getStixCoordinates()) {
                    if (qixP.intersects(point.getX(), point.getY(), 1, 1)) {
                        LOGGER.log(Level.INFO, qix.toString() + " collided with Stix at (" + point.getX()
                                + "," + point.getY() + ")", this.getClass());
                        return true;
                    }
                }
            }
        } else if (unit instanceof Cursor) {
            if (!this.isStixEmpty()) {
                Rectangle collideeR = new Rectangle(unit.getX(), unit.getY(), 2, 2);
                for (Point point : this.getStixCoordinates()) {
                    if (collideeR.intersects(point.getX(), point.getY(), 1, 1)) {

                        LOGGER.log(Level.INFO, unit.toString() + " collided with Stix at (" + point.getX()
                                + "," + point.getY() + ")", this.getClass());
                        return true;
                    }
                }
            }
        }
        return false;


    }

    /**
     * Method that adds a point to the current stix.
     *
     * @param coordinates point that gets added to the stix
     */
    public void addToStix(Point coordinates) {
        stixCoordinates.add(coordinates);
    }

    /**
     * Getter for the stix.
     *
     * @return the current stix
     */
    public LinkedList<Point> getStixCoordinates() {
        return stixCoordinates;
    }

}
