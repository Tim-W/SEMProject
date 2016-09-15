package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import nl.tudelft.sem.group2.AreaState;

import static nl.tudelft.sem.group2.game.Board.gridToCanvas;

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
    public boolean uncoveredOn(int x, int y){
        return areaTracker.getBoardGrid()[x][y].equals(AreaState.UNCOVERED);
    }
    protected boolean innerBorderOn(int x, int y){
        return areaTracker.getBoardGrid()[x][y].equals(AreaState.INNERBORDER);
    }
    protected boolean outerBorderOn(int x, int y){
        return areaTracker.getBoardGrid()[x][y].equals(AreaState.OUTERBORDER);
    }
    @Override
    public void draw(Canvas canvas) {
        canvas.getGraphicsContext2D().drawImage(sprite[spriteIndex],gridToCanvas(x)-width/2,gridToCanvas(y)-height/2,width,height);
        spriteIndex = (spriteIndex+1)%sprite.length;
    }
    public int getHeight() {
        return this.height;
    }
    public int getWidth() {
        return this.width;
    }
}
