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
    //todo set stix to false when implementation is done
    private boolean stix = true;
    public Cursor(int x, int y, int width, int height, AreaTracker areaTracker) {
        super(x, y, width, height,areaTracker);
        this.sprite = new Image[1];
        this.sprite[0] = new Image("/images/cursor.png");
    }

    @Override
    public void move(){
        for (int i = 0; i < speed; i++) {
            if (currentMove != null) {
                if (currentMove.equals(KeyCode.LEFT) && x > 5) {
                    if(checkLine(x-1,y)) {
                        x--;
                    }
                } else if (currentMove.equals(KeyCode.RIGHT) && x < 150) {
                    if(checkLine(x+1,y)) {
                        x++;
                    }
                } else if (currentMove.equals(KeyCode.UP) && y > 5) {
                    if(checkLine(x,y-1)) {
                        y--;
                    }
                } else if (currentMove.equals(KeyCode.DOWN) && y < 150) {
                    if(checkLine(x,y+1)) {
                        y++;
                    }
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
        int animationSpeed = 30;
        int drawX = x*2;
        int drawY = y*2;
        if(loops<animationSpeed) {
            GraphicsContext gC = canvas.getGraphicsContext2D();
            double height = canvas.getHeight();
            double heightVar = height/animationSpeed*loops;
            double width = canvas.getWidth();
            double widthVar = width/animationSpeed*loops;
            double lineSize = 40.0;
            double lineSizeVar = (lineSize/animationSpeed) * loops;
            gC.beginPath();
            gC.setStroke(Color.WHITE);
            //upRightCorner
            gC.moveTo(width-widthVar + drawX - (lineSize-lineSizeVar) , -(height-heightVar) + drawY);
            gC.lineTo(width-widthVar + drawX, -(height-heightVar) + drawY + (lineSize-lineSizeVar));
            //downRightCorner
            gC.moveTo(width-widthVar + drawX - (lineSize-lineSizeVar) , height-heightVar + drawY);
            gC.lineTo(width-widthVar + drawX, height-heightVar + drawY - (lineSize-lineSizeVar));
            //upLeftCorner
            gC.moveTo(-(width-widthVar) + drawX + (lineSize-lineSizeVar) , -(height-heightVar) + drawY);
            gC.lineTo(-(width-widthVar) + drawX, -(height-heightVar) + drawY + (lineSize-lineSizeVar));
            //downLeftCorner
            gC.moveTo(-(width-widthVar) + drawX + (lineSize-lineSizeVar) , height-heightVar + drawY);
            gC.lineTo(-(width-widthVar) + drawX, height-heightVar + drawY - (lineSize-lineSizeVar));
            gC.stroke();
            loops++;
        }
        canvas.getGraphicsContext2D().drawImage(sprite[spriteIndex],drawX-width/2,drawY-height/2,width,height);
        spriteIndex = (spriteIndex+1)%sprite.length;
    }
    public String toString() {
    	return "Cursor";
    }
}