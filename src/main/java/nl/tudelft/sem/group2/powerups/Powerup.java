package nl.tudelft.sem.group2.powerups;

import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.units.LineTraveller;

/**
 * Created by dennis on 17-10-16.
 */
public class Powerup extends LineTraveller {

    public Powerup(int x, int y, int width, int height, AreaTracker areaTracker) {
        super(x, y, width, height, areaTracker);
    }

    /**
     * Powerups stand still.
     */
    @Override
    public void move() {
    }

}
