package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.AreaTracker;

public class Cursor extends LineTraveller {
    private KeyCode currentMove = null;
    private int loops = 0;
    private int speed = 2;
    public Cursor(int x, int y, int width, int height, AreaTracker areaTracker) {
        super(x, y, width, height,areaTracker);
        this.sprite = new Image[1];
        this.sprite[0] = new Image("/images/cursor.png");
    }

    @Override
    public void move(){
        for (int i = 0; i < speed; i++) {
            if (currentMove != null) {
                if (currentMove.equals(KeyCode.LEFT) && x > 1) {
                    x--;
                } else if (currentMove.equals(KeyCode.RIGHT) && x < 300) {
                    x++;
                } else if (currentMove.equals(KeyCode.UP) && y > 0) {
                    y--;
                } else if (currentMove.equals(KeyCode.DOWN) && y < 300) {
                    y++;
                }
            }
        }
    }
    public KeyCode getCurrentMove() {
        return currentMove;
    }

    public void setCurrentMove(KeyCode currentMove) {
        this.currentMove = currentMove;
    }
    @Override
    public void draw(Canvas canvas){
        int speed = 30;
        if(loops<speed) {
            GraphicsContext gC = canvas.getGraphicsContext2D();
            double height = canvas.getHeight();
            double heightVar = height/speed*loops;
            double width = canvas.getWidth();
            double widthVar = width/speed*loops;
            double lineSize = 40.0;
            double lineSizeVar = (lineSize/speed) * loops;
            gC.beginPath();
            gC.setStroke(Color.WHITE);
            //upRightCorner
            gC.moveTo(width-widthVar + getX() - (lineSize-lineSizeVar) , -(height-heightVar) + getY());
            gC.lineTo(width-widthVar + getX(), -(height-heightVar) + getY() + (lineSize-lineSizeVar));
            //downRightCorner
            gC.moveTo(width-widthVar + getX() - (lineSize-lineSizeVar) , height-heightVar + getY());
            gC.lineTo(width-widthVar + getX(), height-heightVar + getY() - (lineSize-lineSizeVar));
            //upLeftCorner
            gC.moveTo(-(width-widthVar) + getX() + (lineSize-lineSizeVar) , -(height-heightVar) + getY());
            gC.lineTo(-(width-widthVar) + getX(), -(height-heightVar) + getY() + (lineSize-lineSizeVar));
            //downLeftCorner
            gC.moveTo(-(width-widthVar) + getX() + (lineSize-lineSizeVar) , height-heightVar + getY());
            gC.lineTo(-(width-widthVar) + getX(), height-heightVar + getY() - (lineSize-lineSizeVar));
            gC.stroke();
            loops++;
        }
        canvas.getGraphicsContext2D().drawImage(sprite[spriteIndex],x-width/2,y-height/2,width,height);
        spriteIndex = (spriteIndex+1)%sprite.length;
    }
    public String toString() {
    	return "Cursor";
    }
}