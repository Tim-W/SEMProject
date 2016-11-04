package nl.tudelft.sem.group2.powerups;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import nl.tudelft.sem.group2.global.Globals;

/**
 * Created by dennis on 28-10-16.
 */
public class CursorPowerupHandler {

    private PowerUpType currentPowerup;
    private int powerUpDuration;

    /**
     * Creates a powerupHandler for a cursor.
     */
    public CursorPowerupHandler() {
        this.currentPowerup = PowerUpType.NONE;
        powerUpDuration = 0;
    }

    /**
     * apply powerup effect to graphicsContext.
     *
     * @param gc GraphicsContext of the canvas
     */
    public void applyEffect(GraphicsContext gc) {
        switch (currentPowerup) {
            case EAT:
                gc.applyEffect(new ColorAdjust(1, 0, 0, 0));
                break;
            case SPEED:
                gc.applyEffect(new ColorAdjust(0, Globals.HALF, 0, 0));
                break;
            default:
                break;
        }
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

    /**
     * updatePowerUp.
     */
    public void updatePowerUp() {
        if (hasPowerup() && getPowerUpDuration() <= 0) {
            setCurrentPowerup(PowerUpType.NONE);
        }

        if (hasPowerup() && getPowerUpDuration() > 0) {
            decrementDuration();
        }
    }
}
