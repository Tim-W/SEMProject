package nl.tudelft.sem.group2.units;

import javafx.scene.image.Image;

/**
 * Created by gijs on 8-9-2016.
 */
abstract public class LineTraveller extends Unit{

    private Image[] sprite;
    private int spriteIndex = 0;
    LineTraveller(int x, int y, int width, int height, Image[] sprite) {
        super(x, y, width, height);
        this.sprite = sprite;
    }
    //Todo needs to be implemented
    //returns true if there is a line on the given coordinates.
    private boolean checkLine(int x, int y){
        return false;
    }
    @Override
    //todo create image on scene.
    public void draw() {
        spriteIndex = (spriteIndex+1)%sprite.length;
    }
}
