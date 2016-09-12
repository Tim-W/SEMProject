package main.java.nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

public class Cursor extends LineTraveller {
    public Cursor(int x, int y, int width, int height, Canvas canvas, Image[] sprite) {
        super(x, y, width, height, canvas, sprite);
    }

    @Override
    public void animate(){

        draw();
    }

}