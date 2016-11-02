package nl.tudelft.sem.group2.board;

import java.awt.Point;

/**
 * Created by Erik on 25-10-2016.
 */
public class Coordinate extends Point {

    private int width;
    private int height;

    /**
     * Create a Coordinate at (x,y) position.
     *
     * @param x           x coord
     * @param y           y coord
     * @param width       width
     * @param height      height
     */
    public Coordinate(int x, int y, int width, int height) {
        super(x, y);
        this.width = width;
        this.height = height;
    }

    /**
     * Create a Coordinate at (x,y) position.
     *
     * @param x           x coord
     * @param y           y coord
     */
    public Coordinate(int x, int y) {
        this(x, y, 2, 2);
    }

    /**
     * Check whether a point lies between a x range with [low,up).
     * @param low lower bound
     * @param up upper bound (non-inclusive)
     * @return boolean whether point lies between two coordinates
     */
    public boolean xBetween(int low, int up) {
        return low <= x && x < up;
    }

    /**
     * Check whether a point lies between a y range with [low,up).
     * @param low lower bound
     * @param up upper bound (non-inclusive)
     * @return boolean whether point lies between two coordinates
     */
    public boolean yBetween(int low, int up) {
        return low <= y && y < up;
    }

    public int getIntX() {
        return (int) super.getX();
    }

    public int getIntY() {
        return (int) super.getY();
    }

    public void setX(int x) {
        this.x = x; }

    public void setY(int y) {
        this.y = y; }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


}
