package nl.tudelft.sem.group2;

import nl.tudelft.sem.group2.controllers.GameController;
import nl.tudelft.sem.group2.global.Globals;
import nl.tudelft.sem.group2.units.Cursor;

import java.awt.Point;
import java.util.LinkedList;

/**
 * Class for handling the results of keypresses.
 */
public final class KeypressHandler {


    /**
     * Basic cosntructor for collision handler class.
     */
    private KeypressHandler() {

    }

    /**
     * Handles the results of the key presses and sets the new move.
     * This first checks if the destination point is within the borders of the grid.
     * Then it checks if the cursor is drawing and if that is the case if it the cursor is trying to move onto uncovered
     * Next it checks if the cursor is not getting to close to the stix otherwise the floodfill algorithm would not work
     * properly.
     * Then it checks if the cursor is leaving the outerborder and if that is the case the last point on outerborder
     * will be added to the stix.
     *
     * If the cursor was not drawing it checks if the cursor was trying to move to outerborders
     *
     * @param cursor the Cursor to be moved
     * @param transX the translation in X direction
     * @param transY the translation in Y direction
     */
    public static void cursorAssertMove(Cursor cursor, int transX, int transY) {
        Point dest = new Point(cursor.getX() + transX, cursor.getY() + transY);
        if (dest.getX() >= 0 && dest.getX() <= Globals.GRID_WIDTH
                && dest.getY() >= 0 && dest.getY() <= Globals.GRID_HEIGHT) {
            if (GameController.getInstance().getGrid().isUncovered(dest.x, dest.y) && cursor
                    .isDrawing()) {
                LinkedList<Point> stix = cursor.getStix().getStixCoordinates();
                if (!stix.contains(new Point(cursor.getX() + transX * 2, cursor.getY() + transY * 2))) {
                    if (GameController.getInstance().getGrid().isOuterborder(cursor.getX(), cursor.getY())) {
                        cursor.getStix().addToStix(new Point(cursor.getX(), cursor.getY()));
                    }
                    cursor.setX((int) dest.getX());
                    cursor.setY((int) dest.getY());
                    cursor.getStix().addToStix(new Point(cursor.getX(), cursor.getY()));
                }
            } else if (GameController.getInstance().getGrid().isOuterborder(dest.x, dest.y)) {
                cursor.setX((int) dest.getX());
                cursor.setY((int) dest.getY());
                cursor.logCurrentMove();
            }
        }
    }
}
