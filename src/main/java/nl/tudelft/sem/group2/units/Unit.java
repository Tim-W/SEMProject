package main.java.nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;

public abstract class Unit {
    protected int x;
    protected int y;
    protected Canvas canvas;
    Unit(int x, int y, Canvas canvas){
        this.x = x;
        this.y = y;
        this.canvas = canvas;
    }
    public abstract void animate();
    public abstract void draw();
    public int getX(){
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}


