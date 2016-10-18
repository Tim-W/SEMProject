package nl.tudelft.sem.group2.powerups;

import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.global.Globals;
import nl.tudelft.sem.group2.units.LineTraveller;

/**
 * Created by dennis on 17-10-16.
 */
public class Powerup extends LineTraveller {

    private int duration;

    public Powerup(int x, int y, int width, int height, AreaTracker areaTracker, int duration) {
        super(x, y, width, height, areaTracker);
        this.duration = duration;
    }

    public Powerup(int x, int y, int width, int height, AreaTracker areaTracker) {
        super(x, y, width, height, areaTracker);
        this.duration = Globals.POWERUP_LIFETIME;
    }

    /**
     * Powerups stand still.
     */
    @Override
    public void move() {
    }

    public void decreaseDuration(int n) {
        duration -= n;
    }

    public void decreaseDuration() {
        decreaseDuration(1);
    }

    public int getDuration() {
        return this.duration;
    }

    /**
     * @return PowerUp to string format
     */
    public String toString() {
        return "PowerUp";
    }
}
