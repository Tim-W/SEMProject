package main.java.nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;

public abstract class Unit {
    protected int x;
    protected int y;
    Unit(int x, int y){
        this.x = x;
        this.y = y;
    }
    public abstract void move(Canvas canvas);
    public abstract void draw(Canvas canvas);
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


