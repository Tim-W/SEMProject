package nl.tudelft.sem.group2.collisions;

import nl.tudelft.sem.group2.units.Unit;

/**
 * Created by Dennis on 11-10-16
 * Interface for collisions, using the intersect method.
 */
public interface CollisionInterface {

    /**
     * method that checks for intersections between the calling Unit and the parameter unit.
     *
     * @param unit the unit collisions need to be checked with
     * @return true if the units intersect, false if they don't
     */
    boolean intersect(Unit unit);

}
