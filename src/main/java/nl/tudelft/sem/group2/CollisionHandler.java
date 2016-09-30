package nl.tudelft.sem.group2;

import java.util.ArrayList;
import java.util.Set;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Qix;
import nl.tudelft.sem.group2.units.Stix;
import nl.tudelft.sem.group2.units.Unit;

/**
 * Class which handles all the collisions.
 */
public class CollisionHandler {

    /**
     * Basic cosntructor for collision handler class.
     */
    public CollisionHandler() {

    }

    /**
     * Check all collisions between Units.
     * Determines what to do when two units collide.
     * This method should be called every gameframe.
     * @param units set of units in the game atm
     * @param stix current stix
     * @return if there is a collision
     */
    public boolean collisions(Set<Unit> units, Stix stix) {
        ArrayList<Unit> unitsList = new ArrayList<>();
        unitsList.addAll(units);

        int indexOfCursor = 0;
        for (int i = 0; i < unitsList.size(); i++) {
            Unit collider = unitsList.get(i);
            if (collider instanceof Cursor) {
                indexOfCursor = i;
                break;
            }
        }
        Unit collider = unitsList.get(indexOfCursor);
        unitsList.remove(indexOfCursor);

        for (Unit collidee : unitsList) {

            if (collidee instanceof Qix) {
                Cursor temp = (Cursor) collider;
                if (stix.intersect((Qix) collidee)) {
                    return true;
                } else if (collidee.intersect(collider) && temp.uncoveredOn(temp.getX(), temp.getY())) {
                    return true;
                }

            } else {
                if (collider.intersect(collidee)) {
                    return true;
                }
            }
        }
        return false;
    }
}
