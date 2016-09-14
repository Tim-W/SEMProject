package nl.tudelft.sem.group2.units;

import javafx.scene.image.Image;

import java.awt.*;

public class Sparx extends LineTraveller {
    public Sparx(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.sprite = new Image[2];
        this.sprite[0] = new Image("/images/sparx-1.png");
        this.sprite[1] = new Image("/images/sparx-2.png");
}

    @Override
    public void move(){

    }
    
    public String toString() {
    	return "Sparx";
    }
}
