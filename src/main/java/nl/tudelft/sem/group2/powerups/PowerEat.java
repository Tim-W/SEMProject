package nl.tudelft.sem.group2.powerups;

import nl.tudelft.sem.group2.AreaTracker;

/**
 * Created by dennis on 17-10-16.
 */
public class PowerEat extends Powerup {

    public PowerEat(int x, int y, int width, int height, AreaTracker areaTracker) {
        super(x, y, width, height, areaTracker);
    }

    /**
     * @return PowerUp to string format
     */
    public String toString() {
        return "Eat PowerUp";
    }
}
