package nl.tudelft.sem.group2.powerups;

import javafx.scene.canvas.Canvas;
import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.units.Unit;

/**
 * Created by dennis on 17-10-16.
 */
public class Powerup extends Unit {

    public Powerup(int x, int y, int width, int height, AreaTracker areaTracker) {
        super(x, y, width, height, areaTracker);
    }

    /**
     * Powerups stand still.
     */
    @Override
    public void move() {
    }

    /**
     * Every frame, this method should be called.
     * It should draw a sprite or a list of sprites on a canvas.
     *
     * @param canvas the canvas to draw on
     */
    @Override
    public void draw(Canvas canvas) {

    }
}
