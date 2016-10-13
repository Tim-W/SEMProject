package nl.tudelft.sem.group2.collisions;

import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Qix;
import nl.tudelft.sem.group2.units.Stix;
import nl.tudelft.sem.group2.units.Unit;

import java.util.ArrayList;
import java.util.Set;

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
        if (units == null || units.isEmpty()) {
            return false;
        }
        ArrayList<Unit> unitsList = new ArrayList<>();
        unitsList.addAll(units);

        ArrayList<Cursor> cursorList = new ArrayList<>();

        for (int i = 0; i < unitsList.size(); i++) {
            Unit collider = unitsList.get(i);
            if (collider instanceof Cursor) {
                cursorList.add((Cursor)collider);
            }
        }

        for(Cursor cursor: cursorList) {
            unitsList.remove(cursor);
            for (Unit collidee : unitsList) {

                if (collidee instanceof Qix) {
                    if (stix != null && stix.intersect(collidee)) {
                        return true;
                    } else if (collidee.intersect(cursor) && cursor.uncoveredOn(cursor.getX(), cursor.getY())) {
                        return true;
                    }

                } else {
                    if (cursor.intersect(collidee)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
