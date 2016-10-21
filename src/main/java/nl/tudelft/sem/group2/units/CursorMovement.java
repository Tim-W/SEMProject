package nl.tudelft.sem.group2.units;

/**
 * Describes where a cursor should move next.
 */
class CursorMovement {
    private int transX;
    private int transY;

    /**
     * Pass the direction to go to.
     *
     * @param transX the difference in pixels in the x direction
     * @param transY the difference in pixels in the y direction
     */
    CursorMovement(int transX, int transY) {
        this.transX = transX;
        this.transY = transY;
    }

    public int getTransX() {
        return transX;
    }

    public int getTransY() {
        return transY;
    }
}