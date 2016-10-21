package nl.tudelft.sem.group2.units;

import javafx.embed.swing.JFXPanel;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.AreaState;
import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.LaunchApp;
import nl.tudelft.sem.group2.scenes.GameScene;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.Point;
import java.util.LinkedList;

import static nl.tudelft.sem.group2.global.Globals.BOARD_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.BOARD_WIDTH;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for cursors
 */

/**
 * ignore because
 * java.lang.IllegalStateException: Not on FX application thread; currentThread = main
 */
@Ignore
public class CursorTest {


    private Cursor cursor;
    private AreaTracker areaTracker;
    private GameScene gameScene;
    private Stix stix;
    private Canvas canvas;
    private AreaState[][] boardGrid = new AreaState[LaunchApp.getGridWidth() + 1][LaunchApp.getGridHeight() + 1];
    private LinkedList<Point> stixCoordinates;
    private int x;
    private int y;

    @Before
    public void setUp() throws Exception {
        new JFXPanel();
        stix = mock(Stix.class);
        createCursor(new Cursor(new Point(2, 2), 2, 2, areaTracker, stix, Color.RED, 3));
        cursor.setSpeed(1);
        canvas = new Canvas(50, 50);
        for (int i = 0; i < boardGrid.length; i++) {
            for (int j = 0; j < boardGrid[i].length; j++) {
                boardGrid[j][i] = AreaState.UNCOVERED;
            }
        }
        x = cursor.getX();
        y = cursor.getY();
    }

    public void createCursor(Cursor c) {
        cursor = c;
        areaTracker = mock(AreaTracker.class);
        gameScene = mock(GameScene.class);
        stixCoordinates = new LinkedList<>();
        when(stix.getStixCoordinates()).thenReturn(stixCoordinates);
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
            boardGrid[x - horizontal][y - vertical] = AreaState.UNCOVERED;
            boardGrid[x + horizontal][y + vertical] = AreaState.UNCOVERED;
            boardGrid[cursor.getX()][cursor.getY()] = AreaState.UNCOVERED;
        }
        when(areaTracker.getBoardGrid()).thenReturn(boardGrid);
        cursor.setCurrentMove(k);
        cursor.move();
    }

    @Test
    public void dontMove() throws Exception {
        cursor.setCurrentMove(null);
        cursor.move();
        Assert.assertEquals(x, cursor.getX());
    }

    @Test
    public void dontMoveL() throws Exception {
        createCursor(new Cursor(new Point(0, 2), 2, 2, areaTracker, stix, Color.RED, 3));
        x = cursor.getX();
        cursor.setCurrentMove(KeyCode.LEFT);
        cursor.move();
        Assert.assertEquals(x, cursor.getX());
    }

    @Test
    public void dontMoveR() throws Exception {
        createCursor(new Cursor(new Point(BOARD_WIDTH / 2, 2), 2, 2, areaTracker, stix, Color.RED, 3));
        x = cursor.getX();
        cursor.setCurrentMove(KeyCode.RIGHT);
        cursor.move();
        Assert.assertEquals(x, cursor.getX());
    }

    @Test
    public void dontMoveU() throws Exception {
        createCursor(new Cursor(new Point(2, 0), 2, 2, areaTracker, stix, Color.RED, 3));
        int dim = cursor.getY();
        cursor.setCurrentMove(KeyCode.UP);
        cursor.move();
        Assert.assertEquals(dim, cursor.getY());
    }

    @Test
    public void dontMoveD() throws Exception {
        createCursor(new Cursor(new Point(2, BOARD_HEIGHT / 2), 2, 2, areaTracker, stix, Color.RED, 3));
        int dim = cursor.getY();
        cursor.setCurrentMove(KeyCode.DOWN);
        cursor.move();
        Assert.assertEquals(dim, cursor.getY());
    }

    @Test
    public void moveRight() throws Exception {
        cursor.setDrawing(true);
        moveCursor(KeyCode.RIGHT, x + 1, y, false);
        Assert.assertEquals(x + 1, cursor.getX());
    }

    @Test
    public void moveRightDraw() throws Exception {
        cursor.setDrawing(true);
        int horizontal = 0;
        int vertical = 1;
        boardGrid[x][y] = AreaState.OUTERBORDER;
        boardGrid[x - vertical][y - horizontal] = AreaState.UNCOVERED;
        boardGrid[x + vertical][y + horizontal] = AreaState.UNCOVERED;
        when(areaTracker.getBoardGrid()).thenReturn(boardGrid);
        cursor.setCurrentMove(KeyCode.RIGHT);
        cursor.move();
        verify(stix, times(2)).addToStix(any());
    }

    @Test
    public void moveRightOuterBorder() throws Exception {
        moveCursor(KeyCode.RIGHT, x + 1, y, true);
        Assert.assertEquals(x + 1, cursor.getX());
    }

    @Test
    public void dontMoveR2() throws Exception {
        moveCursor(KeyCode.RIGHT, x, y, true);
        Assert.assertEquals(x, cursor.getX());
    }

    @Test
    public void dontMoveR3() throws Exception {
        cursor.setDrawing(true);
        stixCoordinates.add(new Point(x + 1, y));
        moveCursor(KeyCode.RIGHT, x + 1, y, false);
        Assert.assertEquals(x, cursor.getX());
    }

    @Test
    public void dontMoveR4() throws Exception {
        cursor.setDrawing(true);
        stixCoordinates.add(new Point(x + 2, y));
        moveCursor(KeyCode.RIGHT, x + 1, y, false);
        Assert.assertEquals(x, cursor.getX());
    }

    @Test
    public void dontMoveR5() throws Exception {
        cursor.setDrawing(true);
        int horizontal = 1;
        int vertical = 0;
        boardGrid[x + horizontal + vertical][y + horizontal + vertical] = AreaState.OUTERBORDER;
        when(areaTracker.getBoardGrid()).thenReturn(boardGrid);
        cursor.setCurrentMove(KeyCode.RIGHT);
        cursor.move();
        Assert.assertEquals(x, cursor.getX());
    }

    @Test
    public void dontMoveR6() throws Exception {
        cursor.setDrawing(true);
        int horizontal = 1;
        int vertical = 0;
        boardGrid[x + horizontal - vertical][y - horizontal + vertical] = AreaState.OUTERBORDER;
        when(areaTracker.getBoardGrid()).thenReturn(boardGrid);
        cursor.setCurrentMove(KeyCode.RIGHT);
        cursor.move();
        Assert.assertEquals(x, cursor.getX());
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

    @Test
    public void draw() throws Exception {
        Cursor spy = spy(new Cursor(new Point(1, 1), 1, 1, areaTracker, stix, Color.RED, 3));
        spy.draw(new Canvas(1, 1));
        verify(spy).getSpriteIndex();
    }

    @Test
    public void testCursorHasDied() throws Exception {
        Assert.assertEquals(2, cursor.getLives());
        cursor.cursorDied();
        Assert.assertEquals(1, cursor.getLives());
        cursor.cursorDied();
        Assert.assertEquals(0, cursor.getLives());
        cursor.cursorDied();
        Assert.assertEquals(0, cursor.getLives());
    }
}