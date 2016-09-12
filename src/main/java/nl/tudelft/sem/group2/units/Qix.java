package main.java.nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;

public class Qix extends Unit {

    public Qix(int x, int y) {
        super(x, y);
    }
    @Override
    public void move(Canvas canvas){

        draw(canvas);
    }
    public void draw(Canvas canvas) {
    }
}
