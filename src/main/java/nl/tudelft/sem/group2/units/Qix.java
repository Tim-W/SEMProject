package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import static nl.tudelft.sem.group2.AreaState.INNERBORDER;
import static nl.tudelft.sem.group2.AreaState.OUTERBORDER;
import static nl.tudelft.sem.group2.game.Board.gridToCanvas;

public class Qix extends Unit {

    private int animationLoops = 0;
    private float[] direction = new float[2];
    private LinkedList<float[]> oldDirections = new LinkedList<float[]>();
    private LinkedList<float[]> oldCoordinates = new LinkedList<float[]>();
    private LinkedList<double[]> colorArray = new LinkedList<double[]>();
    private float[] coordinate = new float[2];
    private static int LineLength = 5;
    private static int PositionLength = 4;

    public Qix() {
        super(30, 30);
    }

    @Override
    public void move() {
        coordinate[0] = x;
        coordinate[1] = y;
        if (animationLoops <= 0) {
            float length;
            do {
                direction[0] = Math.round(Math.random() * 6) - 3;
                direction[1] = Math.round(Math.random() * 6) - 3;
                length = (float) Math.sqrt(direction[0] * direction[0] + direction[1] * direction[1]);
            } while (length == 0);
            float random = (float) Math.random() * 4 - 2;
            float scale = (PositionLength + random) / length;
            direction[0] *= scale;
            direction[1] *= scale;
            animationLoops += (int) (Math.random() * 10);
        }

        checkLineCollision();
        this.x = (int) (coordinate[0] + direction[0]);
        this.y = (int) (coordinate[1] + direction[1]);
        coordinate[0] = x;
        coordinate[1] = y;
        float length = (float) Math.sqrt(direction[0] * direction[0] + direction[1] * direction[1]);
        float random = (float) Math.random() * 2 - 1;
        float scale = (LineLength + random) / length;
        direction[0] *= scale;
        direction[1] *= scale;
        double[] colors = new double[3];
        for (int i = 0; i < colors.length; i++) {
            colors[i] = Math.random()*0.7+0.3;
        }
        colorArray.addFirst(colors);
        oldDirections.addFirst(new float[]{direction[0], direction[1]});
        oldCoordinates.addFirst(new float[]{coordinate[0], coordinate[1]});
        if (oldDirections.size() > 10) {
            oldDirections.removeLast();
            oldCoordinates.removeLast();
            colorArray.removeLast();
        }
        animationLoops--;
    }

    @Override
    public void draw(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.RED);
        for (int i = 0; i < oldDirections.size(); i++) {
            gc.setStroke(Color.color(colorArray.get(i)[0],colorArray.get(i)[1],colorArray.get(i)[2]));
            gc.beginPath();
            float x1 = gridToCanvas((int) (oldCoordinates.get(i)[0] + oldDirections.get(i)[1]));
            float y1 = gridToCanvas((int) (oldCoordinates.get(i)[1] - oldDirections.get(i)[0]));
            float x2 = gridToCanvas((int) (oldCoordinates.get(i)[0] - oldDirections.get(i)[1]));
            float y2 = gridToCanvas((int) (oldCoordinates.get(i)[1] + oldDirections.get(i)[0]));
            gc.moveTo(x1, y1);
            gc.lineTo(x2, y2);
            gc.stroke();
        }
    }

    private void checkLineCollision() {
        int gridLength = areaTracker.getBoardGrid().length;
        float length = (float) Math.sqrt(direction[0] * direction[0] + direction[1] * direction[1]);
        for (int i = 0; i < gridLength; i++) {
            for (int j = 0; j < gridLength; j++) {
                if (areaTracker.getBoardGrid()[i][j] == INNERBORDER || areaTracker.getBoardGrid()[i][j] == OUTERBORDER) {
                    float dx = coordinate[0] - i;
                    float dy = coordinate[1] - j;
                    float lengthNew = (float) Math.sqrt(dx * dx + dy * dy);
                    if(lengthNew<10) {
                        dx/=lengthNew;
                        dy/=lengthNew;
                        dx*=length;
                        dy*=length;
                        direction[0]=dx;
                        direction[1]=dy;
                        return;
                    }
                }
            }
        }
    }
    
    public Polygon toPolygon(){
    	ArrayList<Integer> xCor = new ArrayList<Integer>();
		ArrayList<Integer> yCor = new ArrayList<Integer>();

		for (int i = 0; i < this.getOldCoordinates().size(); i++) {
			xCor.add(Math.round(this.getOldCoordinates().get(i)[0]
					+ this.getOldDirections().get(i)[1]));
			xCor.add(Math.round(this.getOldCoordinates().get(i)[0]
					- this.getOldDirections().get(i)[1]));

			yCor.add(Math.round(this.getOldCoordinates().get(i)[1]
					+ this.getOldDirections().get(i)[0]));
			yCor.add(Math.round(this.getOldCoordinates().get(i)[1]
					- this.getOldDirections().get(i)[0]));
		}

		int[] xArr = new int[xCor.size()];
		int[] yArr = new int[xCor.size()];

		Iterator<Integer> xIterator = xCor.iterator();
		Iterator<Integer> yIterator = yCor.iterator();

		for (int i = 0; i < xArr.length; i++) {
			xArr[i] = xIterator.next();
			yArr[i] = yIterator.next();
		}

		return new Polygon(xArr, yArr, xArr.length);
    }

    public String toString() {
        return "Qix";
    }
	public LinkedList<float[]> getOldCoordinates() {
		return oldCoordinates;
	}
	public float[] getCoordinate() {
		return coordinate;
	}
	public LinkedList<float[]> getOldDirections() {
		return oldDirections;
	}
}
