package main.java.nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

public class Sparx extends LineTraveller {
    public Sparx(int x, int y, int width, int height, Canvas canvas, Image[] sprite) {
        super(x, y, width, height, sprite);
    }

    @Override
    public void move(Canvas canvas){

        draw(canvas);
    }
}
