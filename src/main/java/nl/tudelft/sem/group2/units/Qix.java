package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.LinkedList;

import static nl.tudelft.sem.group2.game.Board.gridToCanvas;

public class Qix extends Unit {

    private int animationLoops=0;
    private float[] direction = new float[2];
    private LinkedList<float[]> oldDirections = new LinkedList<float[]>();
    private LinkedList<float[]> oldCoordinates = new LinkedList<float[]>();
    private float[] coordinate = new float[2];
    private static int LineLength = 5;
    private static int PositionLength = 4;
    public Qix() {
        super(30, 30);
    }
    @Override
    public void move(){
        coordinate[0]=x;
        coordinate[1]=y;
        for (int i = 0; i < direction.length; i++) {
            if(coordinate[i]<0&&direction[i]<0)
                direction[i]*=-1;
        }
        if(animationLoops<=0) {
            direction[0] = Math.round(Math.random() * 6) - 3;
            direction[1] = Math.round(Math.random() * 6) - 3;
            float length = (float) Math.sqrt(direction[0]*direction[0]+direction[1]*direction[1]);
            float random = (float) Math.random() * 4 -2;
            float scale = (PositionLength+random)/length;
            direction[0]*=scale;
            direction[1]*=scale;
            animationLoops = (int) (Math.random() * 10);
        }
        this.x = (int)(coordinate[0]+direction[0]);
        this.y = (int)(coordinate[1]+direction[1]);
        coordinate[0]=x;
        coordinate[1]=y;
        float length = (float) Math.sqrt(direction[0]*direction[0]+direction[1]*direction[1]);
        float random = (float) Math.random() * 2 -1;
        float scale = (LineLength+random)/length;
        direction[0]*=scale;
        direction[1]*=scale;
        oldDirections.addFirst(new float[]{direction[0],direction[1]});
        oldCoordinates.addFirst(new float[]{coordinate[0],coordinate[1]});
        if(oldDirections.size()>10){
            oldDirections.removeLast();
            oldCoordinates.removeLast();
        }
        animationLoops--;
    }
    @Override
    public void draw(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.RED);
        for (int i = 0; i < oldDirections.size(); i++) {
            gc.setFill(Color.PURPLE);
            gc.beginPath();
            float x1=gridToCanvas((int)(oldCoordinates.get(i)[0]+oldDirections.get(i)[1]));
            float y1=gridToCanvas((int)(oldCoordinates.get(i)[1]-oldDirections.get(i)[0]));
            float x2=gridToCanvas((int)(oldCoordinates.get(i)[0]-oldDirections.get(i)[1]));
            float y2=gridToCanvas((int)(oldCoordinates.get(i)[1]+oldDirections.get(i)[0]));
            gc.moveTo(x1,y1);
            gc.lineTo(x2,y2);
            gc.stroke();
        }
    }
    
    public String toString() {
    	return "Qix";
    }
}
