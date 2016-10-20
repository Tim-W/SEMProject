package nl.tudelft.sem.group2.powerups;

import nl.tudelft.sem.group2.units.Cursor;

/**
 * Class that describes a collision event between a cursor and a powerup.
 */
public class PowerupEvent {
    private Cursor cursor;
    private PowerUpType powerUpType;

    /**
     * Creates a powerupEvent.
     *
     * @param cursor      the cursor
     * @param powerUpType the powerupType
     */
    public PowerupEvent(Cursor cursor, PowerUpType powerUpType) {
        this.cursor = cursor;
        this.powerUpType = powerUpType;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public PowerUpType getPowerUpType() {
        return powerUpType;
    }
}
