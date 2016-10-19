package nl.tudelft.sem.group2.powerups;

import nl.tudelft.sem.group2.units.Cursor;

public class PowerupEvent {
    private Cursor cursor;
    private PowerUpType powerUpType;

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
