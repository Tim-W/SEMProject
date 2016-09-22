package nl.tudelft.sem.group2.units;

import javafx.scene.image.Image;

import static nl.tudelft.sem.group2.global.Globals.BOARD_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.BOARD_WIDTH;

/**
 * An enemy unit which travels over the outerborders.
 * When a Sparx collides with a cursor, the game ends.
 */
public class Sparx extends LineTraveller {
    private final SparxDirection sparxDirection;
    private int speed = 2;
    private int lastX = 0;
    private int lastY = 0;

    /**
     * Create a new Sparx with its default sprites.
     *
     * @param x              x coord to start at
     * @param y              y coord to start at
     * @param width          width, used for collision
     * @param height         height, used for collision
     * @param sparxDirection direction in which the sparx starts moving,
     *                       which is either LEFT or RIGHT
     */
    public Sparx(int x, int y, int width, int height, SparxDirection sparxDirection) {
        super(x, y, width, height);
        Image[] sprite = new Image[2];
        sprite[0] = new Image("/images/sparx-1.png");
        sprite[1] = new Image("/images/sparx-2.png");
        setSprite(sprite);
        this.sparxDirection = sparxDirection;
    }

    @Override
    public void move() {
        switch (sparxDirection) {
            case LEFT:
                moveLeft();
                break;
            case RIGHT:
                moveRight();
                break;
            default:
                System.out.println("Warning: unknown move direction");
                break;
        }

    }

    private void moveRight() {
        for (int i = 0; i < speed; i++) {
            if (x < BOARD_WIDTH / 2 && !(x + 1 == lastX) && outerBorderOn(x + 1, y)) {
                setLastCoordinates(x, y);
                setX(x + 1);
            } else if (y < BOARD_HEIGHT / 2 && !(lastY == y + 1) && outerBorderOn(x, y + 1)) {
                setLastCoordinates(x, y);
                setY(y + 1);
            } else if (x > 0 && !(lastX == x - 1) && outerBorderOn(x - 1, y)) {
                setLastCoordinates(x, y);
                setX(x - 1);
            } else if (y > 0 && !(lastY == y - 1) && outerBorderOn(x, y - 1)) {
                setLastCoordinates(x, y);
                setY(y - 1);
            } else if (x < BOARD_WIDTH / 2 && !(x + 1 == lastX) && innerBorderOn(x + 1, y)) {
                setLastCoordinates(x, y);
                setX(x + 1);
            } else if (y < BOARD_HEIGHT / 2 && !(lastY == y + 1) && innerBorderOn(x, y + 1)) {
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
    }

    private void moveLeft() {
        for (int i = 0; i < speed; i++) {
            if (x > 0 && !(lastX == x - 1) && outerBorderOn(x - 1, y)) {
                setLastCoordinates(x, y);
                setX(x - 1);
            } else if (y > 0 && !(lastY == y - 1) && outerBorderOn(x, y - 1)) {
                setLastCoordinates(x, y);
                setY(y - 1);
            } else if (x < BOARD_WIDTH / 2 && !(x + 1 == lastX) && outerBorderOn(x + 1, y)) {
                setLastCoordinates(x, y);
                setX(x + 1);
            } else if (y < BOARD_HEIGHT / 2 && !(lastY == y + 1) && outerBorderOn(x, y + 1)) {
                setLastCoordinates(x, y);
                setY(y + 1);
            } else if (x > 0 && !(lastX == x - 1) && innerBorderOn(x - 1, y)) {
                setLastCoordinates(x, y);
                setX(x - 1);
            } else if (y > 0 && !(lastY == y - 1) && innerBorderOn(x, y - 1)) {
                setLastCoordinates(x, y);
                setY(y - 1);
            } else if (x < BOARD_WIDTH / 2 && !(x + 1 == lastX) && innerBorderOn(x + 1, y)) {
                setLastCoordinates(x, y);
                setX(x + 1);
            } else if (y < BOARD_HEIGHT / 2 && !(lastY == y + 1) && innerBorderOn(x, y + 1)) {
                setLastCoordinates(x, y);
                setY(y + 1);
            }
        }
    }

    private void setLastCoordinates(int x, int y) {
        this.lastX = x;
        this.lastY = y;
    }

    /**
     * @return string representation of a Sparx
     */
    public String toString() {
        return "Sparx";
    }
}
