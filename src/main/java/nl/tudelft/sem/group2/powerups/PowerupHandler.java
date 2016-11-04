package nl.tudelft.sem.group2.powerups;

import nl.tudelft.sem.group2.board.BoardGrid;
import nl.tudelft.sem.group2.collisions.CollisionHandler;
import nl.tudelft.sem.group2.global.Globals;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by gijs on 3-11-2016.
 */
public class PowerupHandler {
    /**
     * Basic constructor for powerup handler class.
     */
    public PowerupHandler() {

    }

    /**
     * @param units            of the gameController.
     * @param collisionHandler of the Gamecontroller
     * @param cursors          of the gameController.
     */
    public void handlePowerups(Set<Unit> units, CollisionHandler collisionHandler, ArrayList<Cursor> cursors) {
        applyPowerups(cursors);

        PowerupEvent powerupEvent = collisionHandler.powerUpCollisions(units, cursors);
        if (powerupEvent != null) {
            Cursor cursor = powerupEvent.getCursor();
            switch (powerupEvent.getPowerUpType()) {
                case LIFE:
                    cursor.addLife();
                    return;
                case EAT:
                    cursor.getCursorPowerupHandler().setCurrentPowerup(PowerUpType.EAT);
                    cursor.getCursorPowerupHandler().setPowerUpDuration(Globals.POWERUP_EAT_DURATION);
                    return;
                case SPEED:
                    cursor.getCursorPowerupHandler().setCurrentPowerup(PowerUpType.SPEED);
                    cursor.getCursorPowerupHandler().setPowerUpDuration(Globals.POWERUP_SPEED_DURATION);
                    break;
                default:
            }
        }
    }

    /**
     * done for testing purposes.
     *
     * @return double
     */
    public double rand() {
        return ThreadLocalRandom.current().nextDouble();
    }

    /**
     * Spawns a new powerup at random and when none is active yet.
     *
     * @param units   of the gameController.
     * @param cursors of the gameController.
     * @param grid    of the gameController.
     */
    public void spawnPowerup(Set<Unit> units, ArrayList<Cursor> cursors, BoardGrid grid) {
        double rand = rand();
        if (rand < Globals.POWERUP_THRESHOLD && !powerUpActive(units, cursors)) {

            for (Cursor cursor : cursors) {

                int quadrant = cursor.oppositeQuadrant();

                int[] coordinates = grid.findPowerupLocation(quadrant);
                Map<PowerUpType, Powerup> powerupMap = new HashMap<>();
                powerupMap.put(PowerUpType.EAT, new PowerEat(coordinates[0], coordinates[1],
                        Globals.BOARD_MARGIN * 2, Globals.BOARD_MARGIN * 2));
                powerupMap.put(PowerUpType.LIFE, new PowerLife(coordinates[0], coordinates[1],
                        Globals.BOARD_MARGIN * 2, Globals.BOARD_MARGIN * 2));
                powerupMap.put(PowerUpType.SPEED, new PowerSpeed(coordinates[0], coordinates[1],
                        Globals.BOARD_MARGIN * 2, Globals.BOARD_MARGIN * 2));
                Powerup powerup = powerupMap.get(PowerUpType.randomType());
                if (powerup == null) {
                    return;
                }
                units.add(powerup);
            }
        }
    }

    /**
     * @return true if a power up is active
     */
    private boolean powerUpActive(Set<Unit> units, ArrayList<Cursor> cursors) {
        for (Cursor cursor : cursors) {
            if (cursor.getCursorPowerupHandler().getCurrentPowerup() != PowerUpType.NONE) {
                return true;
            }
        }

        for (Unit u : units) {
            if (u instanceof Powerup) {
                return true;
            }
        }
        return false;
    }

    private void applyPowerups(ArrayList<Cursor> cursors) {
        for (Cursor cursor : cursors) {
            cursor.getCursorPowerupHandler().updatePowerUp();
        }
    }

}
