package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import nl.tudelft.sem.group2.AreaTracker;

public class Qix extends Unit {

    public Qix(int x, int y, AreaTracker areaTracker) {
        super(x, y, areaTracker);
    }
    @Override
    public void move(){

    }
    @Override
    public void draw(Canvas canvas) {
    }
    
    public String toString() {
    	return "Qix";
    }
}
