package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import nl.tudelft.sem.group2.AreaTracker;

abstract public class LineTraveller extends Unit{

    protected Image[] sprite;
    protected int spriteIndex = 0;
    protected int width;
    protected int height;
    public LineTraveller(int x, int y, int width, int height, AreaTracker areaTracker) {
        super(x, y, areaTracker);
        this.width = width;
        this.height = height;
    }
    //Todo needs to be implemented
    //returns true if there is a line on the given coordinates.
    private boolean checkLine(int x, int y){
        return false;
    }
    @Override
    public void draw(Canvas canvas) {
        canvas.getGraphicsContext2D().drawImage(sprite[spriteIndex],x-width/2,y-height/2,width,height);
        spriteIndex = (spriteIndex+1)%sprite.length;
    }
    public int getHeight() {
        return this.height;
    }
    public int getWidth() {
        return this.width;
    }
}
