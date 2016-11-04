package nl.tudelft.sem.group2.powerups;

import nl.tudelft.sem.group2.global.Globals;
import nl.tudelft.sem.group2.units.LineTraveller;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Main Powerup class.
 */
public abstract class PowerupUnit extends LineTraveller implements ExecuteDelay {

    protected PowerUpType type;

    private int duration;

    private Timer timer;

    private TimerTask task;


    /**
     * Creates a new powerup.
     *
     * @param x           x coord
     * @param y           y coord
     * @param width       width, used for collision
     * @param height      height, used for collision
     */
    public PowerupUnit(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.duration = Globals.POWERUP_LIFETIME;
        this.timer = new Timer();
    }

    /**
     * Powerups stand still.
     */
    @Override
    public void move() {
    }

    public PowerUpType getType() {
        return type;
    }

    @Override
    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public void startDuration() {
        if (duration > 0) {
            timer.schedule(task, MILLI_SECONDS_PER_SECOND * duration);
        }
    }

    @Override
    public void setTimerTask(TimerTask task) {
        this.task = task;
    }


    /**
     * @return Powerup to string format
     */
    public String toString() {
        return "Powerup";
    }
}
