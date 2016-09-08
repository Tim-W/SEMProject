package nl.tudelft.sem.group2.units;
/**
 * Created by gijs on 8-9-2016.
 */
abstract class Unit {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    Unit(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
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


