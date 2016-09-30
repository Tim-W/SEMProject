package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;

/**
 * Draw interface for drawing.
 */
public interface Draw {

    /**
     * Every frame, this method should be called.
     * It should draw a sprite or a list of sprites on a canvas.
     * @param canvas the canvas to draw on
     */
    void draw(Canvas canvas);
}
