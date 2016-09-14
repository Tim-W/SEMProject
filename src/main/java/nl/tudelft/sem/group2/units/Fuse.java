package nl.tudelft.sem.group2.units;

import javafx.scene.image.*;

import java.awt.*;
import java.awt.Image;

public class Fuse extends LineTraveller {
    private int speed = 1;
    private int lastX = 75;
    private int lastY = 150;

    public Fuse(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.sprite = new javafx.scene.image.Image[2];
        this.sprite[0] = new javafx.scene.image.Image("/images/supersparx-1.png");
        this.sprite[1] = new javafx.scene.image.Image("/images/supersparx-2.png");
    }

    @Override
    public void move(){
        for (int i = 0; i < speed; i++) {
            if (x < 150 && !(x + 1 == lastX) && areaTracker.getStix().contains(new Point(x + 1, y))) {
                setLastCoordinates(x, y);
                setX(x + 1);
            } else if (y < 150 && !(lastY == y + 1) && areaTracker.getStix().contains(new Point(x, y + 1))) {
                setLastCoordinates(x, y);
                setY(y + 1);
            } else if (x > 0 && !(lastX == x - 1) && areaTracker.getStix().contains(new Point(x - 1, y))) {
                setLastCoordinates(x, y);
                setX(x - 1);
            } else if (y > 0 && !(lastY == y - 1) && areaTracker.getStix().contains(new Point(x, y - 1))) {
                setLastCoordinates(x, y);
                setY(y - 1);
            }
        }
    }

    private void setLastCoordinates(int x, int y) {
        this.lastX = x;
        this.lastY = y;
    }

    public String toString() {
    	return "Fuse";
    }
}
