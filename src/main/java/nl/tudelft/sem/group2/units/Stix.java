package nl.tudelft.sem.group2.units;

import nl.tudelft.sem.group2.Logger;

import java.awt.Point;
import java.awt.Polygon;
import java.util.LinkedList;
import java.util.logging.Level;

/**
 * Class which keeps track of the current stix.
 */
public class Stix {

    private static LinkedList<Point> stixCoordinates;
    private static final Logger LOGGER = Logger.getLogger();

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
     * @return boolean isempty()
     */
    public boolean isStixEmpty() {
        return !(stixCoordinates != null && !stixCoordinates.isEmpty());
    }

    /**
     * Check if stix intersects with qix.
     * @param qix the current qix
     * @return if they intersect
     */
    public boolean intersect(Qix qix) {
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
