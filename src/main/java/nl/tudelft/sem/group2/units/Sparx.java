package nl.tudelft.sem.group2.units;

import javafx.scene.image.Image;

import java.awt.*;

public class Sparx extends LineTraveller {
    private int speed = 2;

    public Sparx(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.sprite = new Image[2];
        this.sprite[0] = new Image("/images/sparx-1.png");
        this.sprite[1] = new Image("/images/sparx-2.png");
    }

    @Override
    public void move() {
        for (int i = 0; i < speed; i++) {
            if (outerBorderOn(x + 1, y)) {
                setX(x + 1);
            } else if (outerBorderOn(x - 1, y)) {
                setX(x - 1);
            } else if (outerBorderOn(x, y + 1)) {
                setY(y + 1);
            } else if (outerBorderOn(x, y - 1)) {
                setY(y - 1);
            }
        }
    }

    public String toString() {
        return "Sparx";
    }
}
