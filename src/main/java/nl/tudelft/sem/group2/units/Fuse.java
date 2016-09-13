package nl.tudelft.sem.group2.units;

import nl.tudelft.sem.group2.AreaTracker;

public class Fuse extends LineTraveller {
    public Fuse(int x, int y, int width, int height, AreaTracker areaTracker) {
        super(x, y, width, height, areaTracker);
    }

    @Override
    public void move(){
    }
    
    public String toString() {
    	return "Fuse";
    }
}
