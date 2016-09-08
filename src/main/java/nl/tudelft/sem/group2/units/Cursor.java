package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

/**
 * Created by gijs on 8-9-2016.
 */
public class Cursor extends LineTraveller {
    Cursor(int x, int y, int width, int height, Canvas canvas, Image[] sprite) {
        super(x, y, width, height, canvas, sprite);
    }
}
