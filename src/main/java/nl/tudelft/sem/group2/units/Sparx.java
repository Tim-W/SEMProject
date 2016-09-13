package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

public class Sparx extends LineTraveller {
    public Sparx(int x, int y, int width, int height, Image[] sprite) {
        super(x, y, width, height, sprite);
    }

    @Override
    public void move(){


    }
    
    public String toString() {
    	return "Sparx";
    }
}
