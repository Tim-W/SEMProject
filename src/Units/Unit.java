package Units;

import javafx.scene.canvas.Canvas;

/**
 * Created by gijs on 8-9-2016.
 */
abstract class Unit {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Canvas canvas;
    Unit(int x, int y, int width, int height, Canvas canvas){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.canvas = canvas;
    }
    public abstract void draw();

    public int getX(){
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }
}


