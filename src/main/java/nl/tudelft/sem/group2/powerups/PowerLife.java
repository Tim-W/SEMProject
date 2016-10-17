package nl.tudelft.sem.group2.powerups;

import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.units.Unit;

/**
 * Created by dennis on 17-10-16.
 */
public class PowerLife extends Powerup {

    public PowerLife(int x, int y, int width, int height, AreaTracker areaTracker) {
        super(x, y, width, height, areaTracker);
    }

    /**
     * Check for intersection between current unit and another unit.
     *
     * @param collidee the other unit
     * @return true if the collidee is on the same (x,y) coordinate as the current unit
     */
    @Override
    public boolean intersect(Unit collidee) {
        return super.intersect(collidee);
    }
}
