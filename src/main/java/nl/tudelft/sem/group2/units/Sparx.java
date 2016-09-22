package nl.tudelft.sem.group2.units;

import javafx.scene.image.Image;
import nl.tudelft.sem.group2.LaunchApp;
import nl.tudelft.sem.group2.Logger;

import java.util.logging.Level;

public class Sparx extends LineTraveller {
	private static final Logger LOGGER = LaunchApp.getLogger();
	private final SparxDirection sparxDirection;
    private int speed = 2;
    private int lastX = 0;
    private int lastY = 0;


    public Sparx(int x, int y, int width, int height, SparxDirection sparxDirection) {
        super(x, y, width, height);
        this.sprite = new Image[2];
        this.sprite[0] = new Image("/images/sparx-1.png");
        this.sprite[1] = new Image("/images/sparx-2.png");
        this.sparxDirection = sparxDirection;
    }

    @Override
    public void move() {
        switch (sparxDirection) {
            case LEFT:
                for (int i = 0; i < speed; i++) {
                    if (x > 0 && !(lastX == x - 1) && outerBorderOn(x - 1, y)) {
                        setLastCoordinates(x, y);
                        setX(x - 1);
                    } else if (y > 0 && !(lastY == y - 1) && outerBorderOn(x, y - 1)) {
                        setLastCoordinates(x, y);
                        setY(y - 1);
                    } else if (x < 150 && !(x + 1 == lastX) && outerBorderOn(x + 1, y)) {
                        setLastCoordinates(x, y);
                        setX(x + 1);
                    } else if (y < 150 && !(lastY == y + 1) && outerBorderOn(x, y + 1)) {
                        setLastCoordinates(x, y);
                        setY(y + 1);
                    } else if (x > 0 && !(lastX == x - 1) && innerBorderOn(x - 1, y)) {
                        setLastCoordinates(x, y);
                        setX(x - 1);
                    } else if (y > 0 && !(lastY == y - 1) && innerBorderOn(x, y - 1)) {
                        setLastCoordinates(x, y);
                        setY(y - 1);
                    } else if (x < 150 && !(x + 1 == lastX) && innerBorderOn(x + 1, y)) {
                        setLastCoordinates(x, y);
                        setX(x + 1);
                    } else if (y < 150 && !(lastY == y + 1) && innerBorderOn(x, y + 1)) {
                        setLastCoordinates(x, y);
                        setY(y + 1);
                    }
                }
                logCurrentMove();
                break;
            case RIGHT:
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
                    } else if (x < 150 && !(x + 1 == lastX) && innerBorderOn(x + 1, y)) {
                        setLastCoordinates(x, y);
                        setX(x + 1);
                    } else if (y < 150 && !(lastY == y + 1) && innerBorderOn(x, y + 1)) {
                        setLastCoordinates(x, y);
                        setY(y + 1);
                    } else if (x > 0 && !(lastX == x - 1) && innerBorderOn(x - 1, y)) {
                        setLastCoordinates(x, y);
                        setX(x - 1);
                    } else if (y > 0 && !(lastY == y - 1) && innerBorderOn(x, y - 1)) {
                        setLastCoordinates(x, y);
                        setY(y - 1);
                    }
                }
                logCurrentMove();
                break;
        }

    }

    private void setLastCoordinates(int x, int y) {
        this.lastX = x;
        this.lastY = y;
    }

    public String toString() {
        return "Sparx";
    }

	public void logCurrentMove(){
        LOGGER.log(Level.FINE, "Sparx moved to (" + x + "," + y + ")", this.getClass());
	}
}
