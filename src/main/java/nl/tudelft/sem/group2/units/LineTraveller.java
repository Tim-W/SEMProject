package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

abstract public class LineTraveller extends Unit{

    protected Image[] sprite;
    protected int spriteIndex = 0;
    protected int width;
    protected int height;
    public LineTraveller(int x, int y, int width, int height) {
        super(x, y);
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
        canvas.getGraphicsContext2D().drawImage(sprite[spriteIndex],x*2-width/2,y*2-height/2,width,height);
        spriteIndex = (spriteIndex+1)%sprite.length;
    }
    public int getHeight() {
        return this.height;
    }
    public int getWidth() {
        return this.width;
    }
}
