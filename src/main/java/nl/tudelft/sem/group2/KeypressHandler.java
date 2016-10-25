package nl.tudelft.sem.group2;

import nl.tudelft.sem.group2.global.Globals;
import nl.tudelft.sem.group2.units.Cursor;

import java.awt.Point;

/**
 * Created by dennis on 25-10-16.
 */
public class KeypressHandler {


    public static void cursorAssertMove(Cursor cursor, int transX, int transY) {
        if (cursor.getX() + transX >= 0 && cursor.getX() + transX <= Globals.BOARD_WIDTH / 2 && cursor.getY() + transY >= 0 && cursor.getY()
                + transY <= Globals.BOARD_WIDTH / 2) {
            if (cursor.uncoveredOn(cursor.getX() + transX, cursor.getY() + transY) && cursor.isDrawing()) {
                if (!cursor.getStix().getStixCoordinates().contains(new Point(cursor.getX() + transX, cursor.getY() + transY))
                        && !cursor.getStix().getStixCoordinates().contains(new Point(cursor.getX() + transX * 2,
                        cursor.getY() + transY * 2))
                        && cursor.getAreaTracker().getBoardGrid()[cursor.getX() + transX + transY]
                        [cursor.getY() + transY + transX].equals(AreaState
                        .UNCOVERED)
                        && cursor.getAreaTracker().getBoardGrid()[cursor.getX() + transX - transY]
                        [cursor.getY() + transY - transX].equals(AreaState
                        .UNCOVERED)) {

                    if (cursor.outerBorderOn(cursor.getX(), cursor.getY())) {
                        cursor.getStix().addToStix(new Point(cursor.getX(), cursor.getY()));
                    }
                    cursor.setX(cursor.getX() + transX);
                    cursor.setY(cursor.getY() + transY);
                    cursor.getStix().addToStix(new Point(cursor.getX(), cursor.getY()));
                }
            } else if (cursor.outerBorderOn(cursor.getX() + transX, cursor.getY() + transY)) {
                cursor.setX(cursor.getX() + transX);
                cursor.setY(cursor.getY() + transY);
                cursor.logCurrentMove();
            }
        }
    }
}
