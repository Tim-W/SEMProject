package nl.tudelft.sem.group2.collisions;

import nl.tudelft.sem.group2.controllers.GameController;
import nl.tudelft.sem.group2.powerups.PowerEat;
import nl.tudelft.sem.group2.powerups.PowerLife;
import nl.tudelft.sem.group2.powerups.PowerSpeed;
import nl.tudelft.sem.group2.powerups.Powerup;
import nl.tudelft.sem.group2.powerups.PowerupEvent;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Qix;
import nl.tudelft.sem.group2.units.Sparx;
import nl.tudelft.sem.group2.units.Stix;
import nl.tudelft.sem.group2.units.Unit;

import java.util.ArrayList;
import java.util.Set;

import static nl.tudelft.sem.group2.powerups.PowerUpType.EAT;
import static nl.tudelft.sem.group2.powerups.PowerUpType.LIFE;
import static nl.tudelft.sem.group2.powerups.PowerUpType.SPEED;

/**
 * Class which handles all the collisions.
 */
public class CollisionHandler {

    private ArrayList<Unit> unitsList;
    private ArrayList<Cursor> cursorList;

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
    //TODO check if this still works
    public boolean collisions(Set<Unit> units, Stix stix) {
        if (units == null || units.isEmpty()) {
            return false;
        }
        initializeLists(units);
        for (Cursor cursor : cursorList) {
            unitsList.remove(cursor);
            for (Unit collidee : unitsList) {
                if (collidee instanceof Powerup) {
                    continue;
                } else if (collidee instanceof Qix) {
                    if (stix != null && stix.intersect(collidee)) {
                        return true;
                    } else if (collidee.intersect(cursor) && cursor.uncoveredOn(cursor.getX(), cursor.getY())) {
                        return true;
                    }
                } else {
                    if (cursor.intersect(collidee)) {
                        if (cursor.getCurrentPowerup() == EAT && collidee instanceof Sparx) {
                            unitsList.remove(collidee);
                            GameController.getInstance().removeUnit(collidee);
                            return false;
                        } else {
                            return true;
                        }
                    } else if (collidee instanceof Cursor && (cursor.getStix().intersect(collidee)
                            || ((Cursor) collidee).getStix().intersect(cursor))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void initializeLists(Set<Unit> units) {
        unitsList = new ArrayList<>();
        unitsList.addAll(units);
        cursorList = new ArrayList<>();
        for (Unit collider : unitsList) {
            if (collider instanceof Cursor) {
                cursorList.add((Cursor) collider);
            }
        }
    }

    /**
     * Returns an integer > 0 if cursor collides with a powerup.
     *
     * @param units the list of units
     * @param cursors cursors of the game
     * @return 0 if no collision, 1 if life powerup, 2 if eat powerup and 3 if speed powerup
     */
    public PowerupEvent powerUpCollisions(Set<Unit> units, ArrayList<Cursor> cursors) {
        if (units == null || units.isEmpty()) {
            return null;
        }
        ArrayList<Unit> unitsList = new ArrayList<>();
        for (Unit unit : units) {
            if (unit instanceof Powerup || unit instanceof Cursor) {
                unitsList.add(unit);
            }
        }
        if (unitsList.isEmpty()) {
            return null;
        }

        for (Cursor cursor : cursors) {
            for (Unit collidee : unitsList) {
                if (collidee instanceof PowerLife && cursor.intersect(collidee)) {
                    units.remove(collidee);
                    return new PowerupEvent(cursor, LIFE);
                } else if (collidee instanceof PowerEat && cursor.intersect(collidee)) {
                    units.remove(collidee);
                    return new PowerupEvent(cursor, EAT);
                } else if (collidee instanceof PowerSpeed && cursor.intersect(collidee)) {
                    units.remove(collidee);
                    return new PowerupEvent(cursor, SPEED);
                }
            }
        }

        return null;
    }
}
