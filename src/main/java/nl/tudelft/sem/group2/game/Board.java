package nl.tudelft.sem.group2.game;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Fuse;
import nl.tudelft.sem.group2.units.Qix;
import nl.tudelft.sem.group2.units.Sparx;
import nl.tudelft.sem.group2.units.Unit;

import java.util.ArrayList;
import java.awt.*;
import nl.tudelft.sem.group2.AreaState;
import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.scenes.GameScene;
import nl.tudelft.sem.group2.units.Unit;

import java.util.HashSet;
import java.util.Set;

public class Board {

    private Canvas canvas;
    private Set<Unit> units;
    private GraphicsContext gc;
    private AreaTracker areaTracker;
    private final static int MARGIN = 8;
    public Board(Canvas canvas) {
        this.units = new HashSet<Unit>();
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        //BLUE SCREEN IS THE SIZE OF THE BOARD, 300x300
        gc.setFill(Color.BLUE);
        gc.fillRect(0, 0, 316, 316);
        this.areaTracker = GameScene.getAreaTracker();
    }

    public void setUnits(Set<Unit> units) {
        this.units = units;
    }

    public Set<Unit> getUnits() {
        return units;
    }

    public void addUnit(Unit unit) {
        this.units.add(unit);
    }

    public boolean removeUnit(Unit unit) {
        return this.units.remove(unit);
    }

    public void draw() {
        gc.setFill(Color.BLUE);
        gc.fillRect(0, 0, 316, 316);
        gc.setFill(Color.WHITE);
        for (int i=0;i<areaTracker.getBoardGrid().length;i++) {
            for (int j=0;j<areaTracker.getBoardGrid()[i].length;j++) {
                if(areaTracker.getBoardGrid()[i][j]== AreaState.OUTERBORDER||areaTracker.getBoardGrid()[i][j]== AreaState.INNERBORDER)
                    gc.fillRect(gridToCanvas(i),gridToCanvas(j),2,2);
            }
        }
        for (Point p: areaTracker.getStix()) {
            gc.fillRect(gridToCanvas(p.x),gridToCanvas(p.y),2,2);
        }
        for (Unit unit : units) {
            unit.move();
            unit.draw(canvas);
        }

    }

    public void collisions() {
		ArrayList<Unit> unitsList = new ArrayList<Unit>();
		unitsList.addAll(units);
		for (int i = 0; i < unitsList.size(); i++) {
			Unit collider = unitsList.get(i);
			unitsList.remove(i);
			for (Unit collidee : unitsList) {
				if (collider instanceof Qix || collidee instanceof Qix) {
					// TODO code for qix collisions/intersect
				} else if (collider instanceof Cursor) {
					if ((collidee instanceof Sparx || collidee instanceof Fuse) && collider.intersect(collidee)) {
						System.out.println("dood");
						// TODO code to start game over sequence
						GameScene.gameOver();
					}
				}
			}
		}
		}
    //transform grid to canvas coordinates
    public static int gridToCanvas(int b){
        return b*2+MARGIN-1;
    }
}