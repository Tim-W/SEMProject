package nl.tudelft.sem.group2;

import nl.tudelft.sem.group2.controllers.GameController;
import nl.tudelft.sem.group2.global.Globals;
import nl.tudelft.sem.group2.units.Cursor;

import java.awt.Point;

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
     *
     * @param cursor the Cursor to be moved
     * @param transX the translation in X direction
     * @param transY the translation in Y direction
     */
    public static void cursorAssertMove(Cursor cursor, int transX, int transY) {
        if (cursor.getX() + transX >= 0 && cursor.getX() + transX <= Globals.GRID_WIDTH
                && cursor.getY() + transY >= 0 && cursor.getY()
                + transY <= Globals.GRID_WIDTH) {
            if (GameController.getInstance().getGrid().isUncovered(cursor.getX() + transX, cursor.getY() + transY)
                    && cursor.isDrawing()) {
                if (!cursor.getStix().contains(new Point(cursor.getX() + transX, cursor.getY() + transY))
                        && !cursor.getStix().contains(
                        new Point(cursor.getX() + transX * 2, cursor.getY() + transY * 2))
                        && GameController.getInstance().getGrid().isUncovered(
                        cursor.getX() + transX + transY, cursor.getY() + transY + transX)
                        && GameController.getInstance().getGrid().isUncovered(
                        cursor.getX() + transX - transY, cursor.getY() + transY - transX)) {

                    if (GameController.getInstance().getGrid().isOuterborder(cursor.getX(), cursor.getY())) {
                        cursor.getStix().addToStix(new Point(cursor.getX(), cursor.getY()));
                    }
                    cursor.setX(cursor.getX() + transX);
                    cursor.setY(cursor.getY() + transY);
                    cursor.getStix().addToStix(new Point(cursor.getX(), cursor.getY()));
                }
            } else if (GameController.getInstance().getGrid()
                    .isOuterborder(cursor.getX() + transX, cursor.getY() + transY)) {
                cursor.setX(cursor.getX() + transX);
                cursor.setY(cursor.getY() + transY);
            }
        }
    }
}
