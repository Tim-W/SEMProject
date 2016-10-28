package nl.tudelft.sem.group2.powerups;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Erik on 28-10-2016.
 */
public class PowerUp<E> implements ExecuteDelay {

    private E state;
    private E standard;

    private int duration;

    private Timer timer;

    private TimerTask task;

    /**
     *
     * @param standard
     */
    public PowerUp(E standard) {
        this.standard = standard;
        this.state = standard;
        this.timer = new Timer();
        this.duration = 0;
    }

    public void setState(E type) {
        this.state = type;
        startDuration();
    }

    public E getState() {
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
        return state != standard;
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
                state = standard;
            }
        };
        this.task = timerTask;
    }


}
