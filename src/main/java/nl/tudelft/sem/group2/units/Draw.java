package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;

/**
 * Created by Erik on 30-9-2016.
 */
public interface Draw {

    /**
     * Every frame, this method should be called.
     * It should draw a sprite or a list of sprites on a canvas.
     * @param canvas the canvas to draw on
     */
    void draw(Canvas canvas);
}
