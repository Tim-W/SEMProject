package nl.tudelft.sem.group2.units;

import java.awt.Point;
import java.util.LinkedList;

/**
 * Class which keeps track of the current stix.
 */
public class Stix {

    private static LinkedList<Point> stixCoordinates = new LinkedList<>();

    /**
     * Constructor for stix class.
     */
    public Stix() {

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
