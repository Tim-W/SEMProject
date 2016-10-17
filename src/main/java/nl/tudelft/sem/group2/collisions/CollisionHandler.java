package nl.tudelft.sem.group2.collisions;

import nl.tudelft.sem.group2.controllers.GameController;
import nl.tudelft.sem.group2.powerups.PowerEat;
import nl.tudelft.sem.group2.powerups.PowerLife;
import nl.tudelft.sem.group2.powerups.PowerSpeed;
import nl.tudelft.sem.group2.powerups.Powerup;
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
     *
     * @param units set of units in the game atm
     * @param stix  current stix
     * @return if there is a collision
     */
    public boolean collisions(Set<Unit> units, Stix stix) {
        if (units == null || units.isEmpty()) {
            return false;
        }
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
            if (collidee instanceof Powerup) {
                continue;
            }

            if (collidee instanceof Qix) {
                Cursor temp = (Cursor) collider;
                if (stix.intersect(collidee)) {
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

    /**
     * Returns an integer > 0 if cursor collides with a powerup.
     *
     * @param units the list of units
     * @return 0 if no collision, 1 if life powerup, 2 if eat powerup and 3 if speed powerup
     */
    public int powerUpCollisions(Set<Unit> units) {
        if (units == null || units.isEmpty()) {
            return 0;
        }
        ArrayList<Unit> unitsList = new ArrayList<>();
        for (Unit unit : units) {
            if (unit instanceof Powerup || unit instanceof Cursor) {
                unitsList.add(unit);
            }
        }

        if (unitsList.isEmpty()) {
            return 0;
        }

        int indexOfCursor = 0;
        for (int i = 0; i < unitsList.size(); i++) {
            Unit collider = unitsList.get(i);
            if (collider instanceof Cursor) {
                indexOfCursor = i;
                break;
            }
        }
        Cursor cursor = (Cursor) unitsList.get(indexOfCursor);
        unitsList.remove(indexOfCursor);

        for (Unit collidee : unitsList) {
            if (collidee instanceof PowerLife && cursor.intersect(collidee)) {
                synchronized (GameController.class) {
                    GameController.getInstance().removeUnit(collidee);
                }
                return 1;
            } else if (collidee instanceof PowerEat && cursor.intersect(collidee)) {
                synchronized (GameController.class) {
                    GameController.getInstance().removeUnit(collidee);
                }
                return 2;
            } else if (collidee instanceof PowerSpeed && cursor.intersect(collidee)) {
                synchronized (GameController.class) {
                    GameController.getInstance().removeUnit(collidee);
                }
                return 3;
            }
        }

        return 0;
    }
}
