package main.java.nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;

/**
 * Created by gijs on 8-9-2016.
 */
public abstract class Unit {
    protected int x;
    protected int y;
    protected Canvas canvas;
    Unit(int x, int y, Canvas canvas){
        this.x = x;
        this.y = y;
        this.canvas = canvas;
    }
    public abstract void draw();

    public int getX(){
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}


