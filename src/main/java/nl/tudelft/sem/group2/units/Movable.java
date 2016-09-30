package nl.tudelft.sem.group2.units;

/**
 * Movable interface for moving.
 */
public interface Movable {

    /**
     * Every frame, this method should be called.
     * The x and y coordinates may be changed using setX, setY,
     * after which the unit can take another position on the screen.
     */
    void move();
}
