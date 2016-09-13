package nl.tudelft.sem.group2.game;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.units.Unit;

import java.util.HashSet;
import java.util.Set;

public class Board {
    private Canvas canvas;
    private Set<Unit> units;
    private GraphicsContext gc;

    public Board(Canvas canvas) {
        this.units = new HashSet<Unit>();
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        //BLUE SCREEN IS THE SIZE OF THE BOARD, 300x300
        gc.setFill(Color.BLUE);
        gc.fillRect(0, 0, 300, 300);
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
        gc.fillRect(0, 0, 310, 310);
        for (Unit unit : units) {
            unit.move();
            unit.draw(canvas);
        }
    }
    
    public void collisions(){
    	for (Unit collider: units) {
    		for(Unit collidee : units){
    			if(collider != collidee){
    				if(collider.getX() == collidee.getX() && collider.getY() == collidee.getY()){
    					System.out.println(collider.toString() + " collided with " + collidee.toString());
    				}
    			}
    		}
    	}
    }
}