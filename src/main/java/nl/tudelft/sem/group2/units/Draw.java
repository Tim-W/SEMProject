package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.GraphicsContext;

/**
 * Draw interface for drawing.
 */
public interface Draw {

    /**
     * Every frame, this method should be called.
     * It should draw a sprite or a list of sprites on a canvas.
     *
     * @param gc the graphicsContext of the canvas to draw on
     */
    void draw(GraphicsContext gc);
}
