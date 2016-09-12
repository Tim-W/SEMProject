package main.java.nl.tudelft.sem.group2.units;

import javafx.scene.image.Image;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;

public class Cursor extends LineTraveller {
    private KeyCode currentMove = null;
    private int loops = 0;
    public Cursor(int x, int y, int width, int height, Image[] sprite) {
        super(x, y, width, height, sprite);
    }

    @Override
    public void move(){
        if (currentMove != null) {
            if (currentMove.equals(KeyCode.LEFT)) {
                x--;
            } else if (currentMove.equals(KeyCode.RIGHT)) {
                x++;
            } else if (currentMove.equals(KeyCode.UP)) {
                y--;
            } else if (currentMove.equals(KeyCode.DOWN)) {
                y++;
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
        if(loops<60) {
            GraphicsContext gC = canvas.getGraphicsContext2D();
            double height = canvas.getHeight();
            double heightVar = height/60*loops;
            double width = canvas.getWidth();
            double widthVar = width/60*loops;
            double lineSize = 20 / 60 * loops;
            //upRightCorner
            gC.lineTo(width-widthVar + getX() - (20-lineSize) , -(height-heightVar) + getY());
            gC.lineTo(width-widthVar + getX(), -(height-heightVar) + getY() + (20-lineSize));
            //downRightCorner
            gC.lineTo(-(width-widthVar) + getX() - (20-lineSize) , height-heightVar + getY());
            gC.lineTo(-(width-widthVar) + getX(), heightVar + getY() - (20-lineSize));
            //upLeftCorner
            gC.lineTo(width-widthVar + getX() + (20-lineSize) , -(height-heightVar) + getY());
            gC.lineTo(width-widthVar + getX(), -(height-heightVar) + getY() + (20-lineSize));
            //downLeftCorner
            gC.lineTo(-(width-widthVar) + getX() + (20-lineSize) , height-heightVar + getY());
            gC.lineTo(-(width-widthVar) + getX(), height-heightVar + getY() - (20-lineSize));
            loops++;
        }
        canvas.getGraphicsContext2D().drawImage(sprite[spriteIndex],x-width/2,y-height/2,width,height);
        spriteIndex = (spriteIndex+1)%sprite.length;
    }
}