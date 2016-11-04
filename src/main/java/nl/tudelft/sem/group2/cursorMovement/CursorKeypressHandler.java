package nl.tudelft.sem.group2.cursorMovement;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import nl.tudelft.sem.group2.gameController.GameController;
import nl.tudelft.sem.group2.global.Globals;
import nl.tudelft.sem.group2.powerups.PowerUpType;
import nl.tudelft.sem.group2.units.Cursor;

import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

/**
 * Class for handling the results of keypresses.
 */
public class CursorKeypressHandler {
    private List<KeyCode> keyCodes;
    private KeyCode currentMove = null;
    private Cursor cursor;
    private Map<KeyCode, CursorMovement> cursorMovementMap;
    private KeyCode fastMoveKey, slowMoveKey;

    /**
     * Basic cosntructor for collision handler class.
     *
     * @param cursor the cursor that is handled.
     */
    public CursorKeypressHandler(Cursor cursor) {
        this.cursor = cursor;
        KeyCode[] keyCodes = new KeyCode[] {KeyCode.UP, KeyCode.DOWN, KeyCode.LEFT, KeyCode.RIGHT,
        };
        this.fastMoveKey = KeyCode.O;
        this.slowMoveKey = KeyCode.I;
        if (cursor.getId() == 1) {
            keyCodes = new KeyCode[] {KeyCode.W, KeyCode.S, KeyCode.A, KeyCode.D};
            this.fastMoveKey = KeyCode.Z;
            this.slowMoveKey = KeyCode.X;
        }
        this.keyCodes = asList(keyCodes);
        cursorMovementMap = new HashMap<>();
        cursorMovementMap.put(this.keyCodes.get(2), new CursorMovement(-1, 0));
        cursorMovementMap.put(this.keyCodes.get(3), new CursorMovement(1, 0));
        cursorMovementMap.put(this.keyCodes.get(0), new CursorMovement(0, -1));
        cursorMovementMap.put(this.keyCodes.get(1), new CursorMovement(0, 1));


    }

    /**
     * Handles the results of the key presses and sets the new move.
     * This first checks if the destination point is within the borders of the grid.
     * Then it checks if the cursor is drawing and if that is the case if it the cursor is trying to move onto uncovered
     * Next it checks if the cursor is not getting to close to the stix otherwise the floodfill algorithm would not work
     * properly.
     * Then it checks if the cursor is leaving the outerborder and if that is the case the last point on outerborder
     * will be added to the stix.
     * If the cursor was not drawing it checks if the cursor was trying to move to outerborders
     */
    public void cursorAssertMove() {
        if (currentMove != null) {
            int transX = cursorMovementMap.get(currentMove).getTransX();
            int transY = cursorMovementMap.get(currentMove).getTransY();
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
                        cursor.setX(dest.x);
                        cursor.setY(dest.y);
                        cursor.getStix().addToStix(new Point(cursor.getX(), cursor.getY()));
                    }
                } else if (GameController.getInstance().getGrid().isOuterborder(dest.x, dest.y)) {
                    cursor.setX(dest.x);
                    cursor.setY(dest.y);
                    cursor.logCurrentMove();
                }
            }
        }
    }

    /**
     * Handles a key press for the cursor.
     *
     * @param e the keyEvent of the key pressed
     */
    public void keyPressed(KeyEvent e) {
        if (keyCodes.contains(e.getCode())) {
            if (cursor.isDrawing() && cursor.getFuseHandler().getFuse() != null) {
                cursor.getFuseHandler().getFuse().notMoving();
            }
            setCurrentMove(e.getCode());
        } else if (e.getCode().equals(slowMoveKey)) {
            if (!cursor.stixDrawn() || !cursor.isFast()) {
                cursor.setSpeed(1);
                cursor.setDrawing(true);
            }
        } else if (e.getCode().equals(fastMoveKey)) {
            cursor.setSpeed(2);
            cursor.setDrawing(true);
        }
        if (cursor.getCursorPowerupHandler().getCurrentPowerup() == PowerUpType.SPEED) {
            cursor.setSpeed(cursor.getSpeed() + 1);
        }
    }

    /**
     * Handles a key being released.
     *
     * @param keyCode the key being released
     */
    public void keyReleased(KeyCode keyCode) {
        if (keyCode.equals(getCurrentMove())) {
            cursor.getFuseHandler().handleFuse();
            setCurrentMove(null);
        } else if (keyCode.equals(fastMoveKey) || keyCode.equals(slowMoveKey)) {
            cursor.setDrawing(false);
            cursor.setSpeed(2);
            cursor.getFuseHandler().handleFuse();
        }
    }

    /**
     * @return the current move direction (up/down/left/right)
     */
    public KeyCode getCurrentMove() {
        return currentMove;
    }

    /**
     * @param currentMove the current move direction (up/down/left/right)
     */
    public void setCurrentMove(KeyCode currentMove) {
        this.currentMove = currentMove;
    }

    /**
     * @return this cursor specific keys.
     */
    public List<KeyCode> getArrowKeys() {
        return keyCodes;
    }

}
