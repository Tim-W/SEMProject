package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.LinkedList;

import static nl.tudelft.sem.group2.game.Board.gridToCanvas;

public class Qix extends Unit {

    private int animationLoops=0;
    private int[] direction = new int[2];
    private LinkedList<int[]> oldDirections = new LinkedList<int[]>();
    private LinkedList<int[]> oldCoordinates = new LinkedList<int[]>();
    private int[] coordinate = new int[2];
    private static int SIZE = 1;
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
            do {
                direction[0] = (int) (Math.round(Math.random() * 6) - 3) * SIZE;
                direction[1] = (int) (Math.round(Math.random() * 6) - 3) * SIZE;
            }while (direction[0]==0&&direction[1]==0);
            animationLoops = (int) (Math.random() * 10);
        }
        oldDirections.addFirst(new int[]{direction[0],direction[1]});
        oldCoordinates.addFirst(new int[]{coordinate[0],coordinate[1]});
        if(oldDirections.size()>10){
            oldDirections.removeLast();
            oldCoordinates.removeLast();
        }
        this.x = coordinate[0]+direction[0];
        this.y = coordinate[1]+direction[1];
        animationLoops--;
    }
    @Override
    public void draw(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.RED);
        System.out.println(oldCoordinates.size());
        for (int i = 0; i < oldDirections.size(); i++) {
            System.out.println(oldCoordinates.get(i)[0]+" Y: "+oldCoordinates.get(i)[1]);
            gc.setFill(Color.PURPLE);
            gc.beginPath();
            int minimalSize = 15;
            double x1=gridToCanvas(oldCoordinates.get(i)[0])+oldDirections.get(i)[1];
            double y1=gridToCanvas(oldCoordinates.get(i)[1])-oldDirections.get(i)[0];
            double x2=gridToCanvas(oldCoordinates.get(i)[0])-oldDirections.get(i)[1];
            double y2=gridToCanvas(oldCoordinates.get(i)[1])+oldDirections.get(i)[0];
            double dx = Math.abs(x2-x1);
            double dy = Math.abs(y2-y1);
            double length = Math.sqrt(dx*dx+dy*dy);
            double scale = minimalSize/length;
            x1+=oldDirections.get(i)[1]*scale;
            y1-=oldDirections.get(i)[0]*scale;
            x2-=oldDirections.get(i)[1]*scale;
            y2+=oldDirections.get(i)[0]*scale;
            gc.moveTo(x1,y1);
            gc.lineTo(x2,y2);
            gc.stroke();
        }
    }
    
    public String toString() {
    	return "Qix";
    }
}
