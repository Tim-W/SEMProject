package nl.tudelft.sem.group2.units;

import javafx.embed.swing.JFXPanel;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import nl.tudelft.sem.group2.AreaState;
import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.LaunchApp;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by gijs on 24-9-2016.
 */
public class CursorTest {


    private Cursor cursor;
    private AreaTracker areaTracker;
    private Canvas canvas;
    private AreaState[][] boardGrid = new AreaState[LaunchApp.getGridWidth() + 1][LaunchApp.getGridHeight() + 1];

    @Before
    public void setUp() throws Exception {
        new JFXPanel();
        createCursor(new Cursor(2, 2, 2, 2));
        cursor.setSpeed(1);
        canvas = new Canvas(50, 50);
    }

    public void createCursor(Cursor c) {
        cursor = c;
        areaTracker = mock(AreaTracker.class);
        cursor.setAreaTracker(areaTracker);
    }

    public void moveCursor(KeyCode k, int x, int y, boolean outerborder) {
        int horizontal = 0;
        int vertical = 0;
        if (k.equals(KeyCode.RIGHT) || k.equals(KeyCode.LEFT)) {
            horizontal = 1;
        } else {
            vertical = 1;
        }
        if (outerborder) {
            boardGrid[x][y] = AreaState.OUTERBORDER;
        } else {
            boardGrid[x][y] = AreaState.UNCOVERED;
            boardGrid[x - vertical][y - horizontal] = AreaState.UNCOVERED;
            boardGrid[x + vertical][y + horizontal] = AreaState.UNCOVERED;
            boardGrid[cursor.getX()][cursor.getY()] = AreaState.UNCOVERED;
        }
        when(areaTracker.getBoardGrid()).thenReturn(boardGrid);
        cursor.setCurrentMove(k);
        cursor.move();
    }

    @Test
    public void dontMove() throws Exception {
        int x = cursor.getX();
        cursor.setCurrentMove(null);
        cursor.move();
        Assert.assertEquals(x, cursor.getX());
    }

    @Test
    public void moveRight() throws Exception {
        cursor.setDrawing(true);
        int x = cursor.getX();
        int y = cursor.getY();
        moveCursor(KeyCode.RIGHT, x + 1, y, false);
        Assert.assertEquals(x + 1, cursor.getX());
    }

    @Test
    public void moveRightOuterBorder() throws Exception {
        int x = cursor.getX();
        int y = cursor.getY();
        moveCursor(KeyCode.RIGHT, x + 1, y, true);
        Assert.assertEquals(x + 1, cursor.getX());
    }

    @Test
    public void moveLeft() throws Exception {
        cursor.setDrawing(true);
        int x = cursor.getX();
        int y = cursor.getY();
        moveCursor(KeyCode.LEFT, x - 1, y, false);
        Assert.assertEquals(x - 1, cursor.getX());
    }

    @Test
    public void moveLeftOuterBorder() throws Exception {
        int x = cursor.getX();
        int y = cursor.getY();
        moveCursor(KeyCode.LEFT, x - 1, y, true);
        Assert.assertEquals(x - 1, cursor.getX());
    }

    @Test
    public void moveUp() throws Exception {
        cursor.setDrawing(true);
        int x = cursor.getX();
        int y = cursor.getY();
        moveCursor(KeyCode.UP, x, y - 1, false);
        Assert.assertEquals(y - 1, cursor.getY());
    }

    @Test
    public void moveUpOuterBorder() throws Exception {
        int x = cursor.getX();
        int y = cursor.getY();
        moveCursor(KeyCode.UP, x, y - 1, true);
        Assert.assertEquals(y - 1, cursor.getY());
    }

    @Test
    public void moveDown() throws Exception {
        cursor.setDrawing(true);
        int x = cursor.getX();
        int y = cursor.getY();
        moveCursor(KeyCode.DOWN, x, y + 1, false);
        Assert.assertEquals(y + 1, cursor.getY());
    }

    @Test
    public void moveDownOuterBorder() throws Exception {
        int x = cursor.getX();
        int y = cursor.getY();
        moveCursor(KeyCode.DOWN, x, y + 1, true);
        Assert.assertEquals(y + 1, cursor.getY());
    }

    @Test
    public void testGetCurrentMove() throws Exception {
        cursor.setCurrentMove(KeyCode.RIGHT);
        Assert.assertEquals(KeyCode.RIGHT, cursor.getCurrentMove());
    }

    @Test
    public void isFast() throws Exception {
        boolean oldValue = cursor.isFast();
        cursor.setFast(!oldValue);
        Assert.assertEquals(!oldValue, cursor.isFast());
    }

    @Test
    public void setFast() throws Exception {
        boolean oldValue = cursor.isFast();
        cursor.setFast(!oldValue);
        Assert.assertEquals(!oldValue, cursor.isFast());
    }


}