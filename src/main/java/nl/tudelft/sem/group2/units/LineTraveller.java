package nl.tudelft.sem.group2.units;


import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

/**
 * Created by gijs on 8-9-2016.
 */
abstract public class LineTraveller extends Unit{

    private Image[] sprite;
    private int spriteIndex = 0;
    LineTraveller(int x, int y, int width, int height, Canvas canvas, Image[] sprite) {
        super(x, y, width, height,canvas);
        this.sprite = sprite;
    }
    //Todo needs to be implemented
    //returns true if there is a line on the given coordinates.
    private boolean checkLine(int x, int y){
        return false;
    }
    @Override
    public void draw() {
        canvas.getGraphicsContext2D().drawImage(sprite[spriteIndex],x-width/2,y-height/2,width,height);
        spriteIndex = (spriteIndex+1)%sprite.length;
    }
}
