package nl.tudelft.sem.group2.board;


/**
 * Created by Erik on 25-10-2016.
 */
public class Coordinate {

    private int x;
    private int y;
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
        this.x = x;
        this.y = y;
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

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
