package nl.tudelft.sem.group2.powerups;

/**
 * Created by dennis on 28-10-16.
 */
public class PowerupHandler {

    private PowerUpType currentPowerup;
    private int powerUpDuration;

    /**
     * Creates a powerupHandler for a cursor.
     */
    public PowerupHandler() {
        this.currentPowerup = PowerUpType.NONE;
        powerUpDuration = 0;
    }

    public PowerUpType getCurrentPowerup() {
        return currentPowerup;
    }

    /**
     * sets the current powerup status of the cursor.
     *
     * @param currentPowerup the new powerup status
     */
    public void setCurrentPowerup(PowerUpType currentPowerup) {
        this.currentPowerup = currentPowerup;
    }


    public int getPowerUpDuration() {
        return powerUpDuration;
    }

    public void setPowerUpDuration(int powerUpDuration) {
        this.powerUpDuration = powerUpDuration;
    }

    /**
     * @return true if there is a powerup active.
     */
    public boolean hasPowerup() {
        return currentPowerup != PowerUpType.NONE;
    }

    /**
     * Decrements the powerup duration by one.
     */
    public void decrementDuration() {
        powerUpDuration--;
    }
}
