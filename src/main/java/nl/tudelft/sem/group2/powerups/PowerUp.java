package nl.tudelft.sem.group2.powerups;

import java.util.Timer;
import java.util.TimerTask;

/**
 *
 */
public class Powerup implements ExecuteDelay {

    private PowerUpType state;

    private int duration;

    private Timer timer;

    private TimerTask task;

    /**
     *
     * @param type
     */
    public Powerup(PowerUpType type) {
        this.state = type;
        this.timer = new Timer();
        this.duration = 0;
    }

    public void setState(PowerUpType type) {
        this.state = type;
        startDuration();
    }

    public PowerUpType getState() {
        return state;
    }

    /**
     *
     * @param duration is in seconds
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     *
     * @return true if the current is not the same as the standard state
     */
    public boolean notStandard() {
        return state != PowerUpType.NONE;
    }

    /**
     * start the time to set the type back to the standard type
     */
    public void startDuration() {
        if (duration > 0) {
            timer.schedule(task, MILLI_SECONDS_PER_SECOND * duration);
        }
    }

    @Override
    public void setTimerTask(TimerTask taske) {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                state = PowerUpType.NONE;
            }
        };
        this.task = timerTask;
    }


}
