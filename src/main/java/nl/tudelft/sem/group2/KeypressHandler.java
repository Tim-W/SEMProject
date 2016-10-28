package nl.tudelft.sem.group2;

import nl.tudelft.sem.group2.board.BoardGrid;
import nl.tudelft.sem.group2.global.Globals;
import nl.tudelft.sem.group2.units.Cursor;

import java.awt.Point;
import java.awt.image.BandCombineOp;

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
        if (cursor.getX() + transX >= 0 && cursor.getX() + transX <= Globals.BOARD_WIDTH / 2
                && cursor.getY() + transY >= 0 && cursor.getY()
                + transY <= Globals.BOARD_WIDTH / 2) {
            if (BoardGrid.getInstance().isUncovered(cursor.getIntX() + transX, cursor.getIntY() + transY)
                    && cursor.isDrawing()) {
                if (!cursor.getStix().contains(new Point(cursor.getIntX() + transX, cursor.getIntY() + transY))
                        && !cursor.getStix().contains(
                        new Point(cursor.getIntX() + transX * 2, cursor.getIntY() + transY * 2))
                        && BoardGrid.getInstance().isUncovered(
                        cursor.getIntX() + transX + transY, cursor.getIntY() + transY + transX)
                        && BoardGrid.getInstance().isUncovered(
                        cursor.getIntX() + transX - transY, cursor.getIntY() + transY - transX)) {

                    if (BoardGrid.getInstance().isOuterborder(cursor.getIntX(), cursor.getIntY())) {
                        cursor.getStix().addToStix(new Point(cursor.getIntX(), cursor.getIntY()));
                    }
                    cursor.setX(cursor.getIntX() + transX);
                    cursor.setY(cursor.getIntY() + transY);
                    cursor.getStix().addToStix(new Point(cursor.getIntX(), cursor.getIntY()));
                }
            } else if (BoardGrid.getInstance().isOuterborder(cursor.getIntX() + transX, cursor.getIntY() + transY)) {
                cursor.setX(cursor.getIntX() + transX);
                cursor.setY(cursor.getIntY() + transY);
            }
        }
    }
}
