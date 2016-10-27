package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import nl.tudelft.sem.group2.board.AreaTracker;

/**
 * Created by Erik on 17-10-2016.
 */
public class ConcreteUnit extends Unit {
    /**
     * Create a Unit at (x,y) position.
     *
     * @param x           x coord
     * @param y           y coord
     * @param width       width, used for collision
     * @param height      height, used for collision
     * @param areaTracker used for calculating areas
     */
    ConcreteUnit(int x, int y, int width, int height, AreaTracker areaTracker) {
        super(x, y, width, height, areaTracker);
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void move() {

    }
}
