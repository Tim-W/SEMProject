package nl.tudelft.sem.group2.units;

import javafx.scene.image.Image;

import java.awt.*;

public class Sparx extends LineTraveller {
    private int speed = 5;
    private int lastX = 0;
    private int lastY = 0;

    public Sparx(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.sprite = new Image[2];
        this.sprite[0] = new Image("/images/sparx-1.png");
        this.sprite[1] = new Image("/images/sparx-2.png");
    }

    @Override
    public void move() {
        for (int i = 0; i < speed; i++) {
            if (x < 150 && !(x + 1 == lastX) && outerBorderOn(x + 1, y)) {
                setLastCoordinates(x, y);
                setX(x + 1);
            } else if (y < 150 && !(lastY == y + 1) && outerBorderOn(x, y + 1)) {
                setLastCoordinates(x, y);
                setY(y + 1);
            } else if (x > 0 && !(lastX == x - 1) && outerBorderOn(x - 1, y)) {
                setLastCoordinates(x, y);
                setX(x - 1);
            } else if (y > 0 && !(lastY == y - 1) && outerBorderOn(x, y - 1)) {
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
        return "Sparx";
    }
}
