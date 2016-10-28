package nl.tudelft.sem.group2.powerups;

import java.util.TimerTask;

/**
 * Created by Erik on 28-10-2016.
 */
public interface ExecuteDelay {

    int MILLI_SECONDS_PER_SECOND = 1000;

    /**
     *
     * @param duration is in seconds
     */
    void setDuration(int duration);

    /**
     * start the time to set the type back to the standard type
     */
    void startDuration();

    /**
     *
     * @param task
     */
    void setTimerTask(TimerTask task);

}
