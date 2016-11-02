package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import nl.tudelft.sem.group2.JavaFXThreadingRule;
import nl.tudelft.sem.group2.board.AreaState;
import nl.tudelft.sem.group2.board.BoardGrid;
import nl.tudelft.sem.group2.controllers.GameController;
import nl.tudelft.sem.group2.global.Globals;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.awt.Point;

import static edu.emory.mathcs.backport.java.util.Arrays.asList;
import static nl.tudelft.sem.group2.global.Globals.GRID_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.GRID_WIDTH;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for cursors.
 */
//TODO fix tests
@Ignore
public class CursorTest {

    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();

    private Cursor cursor;
    private Stix stix = mock(Stix.class);
    private BoardGrid grid = mock(BoardGrid.class);
    private int x;
    private int y;

    @Before
    public void setUp() throws Exception {
        when(GameController.getInstance().getGrid()).thenReturn(grid);
    }

    public void createCursor() {
        Cursor cursor1 = new Cursor(new Point(0, 0), 2,
                2, stix, Globals.LIVES, 0);
        KeyCode[] keys = new KeyCode[] {KeyCode.UP, KeyCode.DOWN, KeyCode.LEFT, KeyCode.RIGHT};
        cursor1.addKeys(asList(keys));
        cursor1.setFastMoveKey(KeyCode.O);
        cursor1.setSlowMoveKey(KeyCode.I);
        cursor.setSpeed(1);
        x = cursor.getX();
        y = cursor.getY();
        cursor.setStix(stix);
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
            when(grid.getState(new Point(x, y))).thenReturn(AreaState.OUTERBORDER);
        } else {
            when(grid.getState(new Point(x - horizontal, y - vertical))).thenReturn(AreaState.UNCOVERED);
            when(grid.getState(new Point(x + horizontal, y + vertical))).thenReturn(AreaState.UNCOVERED);
            when(grid.getState(new Point(cursor.getX(), cursor.getY()))).thenReturn(AreaState.UNCOVERED);
        }
        cursor.setCurrentMove(k);
        cursor.move();
    }

    @Test
    public void dontMove() throws Exception {
        createCursor();
        cursor.setCurrentMove(null);
        cursor.move();
        Assert.assertEquals(x, cursor.getX());
    }

    @Test
    public void dontMoveL() throws Exception {
        createCursor();
        cursor.setX(0);
        x = cursor.getX();
        cursor.setCurrentMove(KeyCode.LEFT);
        cursor.move();
        Assert.assertEquals(x, cursor.getX());
    }

    @Test
    public void dontMoveR() throws Exception {
        createCursor();
        cursor.setX(GRID_WIDTH);
        x = cursor.getX();
        cursor.setCurrentMove(KeyCode.RIGHT);
        cursor.move();
        Assert.assertEquals(x, cursor.getX());
    }

    @Test
    public void dontMoveU() throws Exception {
        createCursor();
        cursor.setY(0);
        int dim = cursor.getY();
        cursor.setCurrentMove(KeyCode.UP);
        cursor.move();
        Assert.assertEquals(dim, cursor.getY());
    }

    @Test
    public void dontMoveD() throws Exception {
        createCursor();
        cursor.setY(GRID_HEIGHT);
        int dim = cursor.getY();
        cursor.setCurrentMove(KeyCode.DOWN);
        cursor.move();
        Assert.assertEquals(dim, cursor.getY());
    }

    @Test
    public void moveRight() throws Exception {
        createCursor();
        cursor.setDrawing(true);
        moveCursor(KeyCode.RIGHT, x + 1, y, false);
        Assert.assertEquals(x + 1, cursor.getX());
    }

    /*@Test
    public void moveRightDraw() throws Exception {
        createCursor();
        cursor.setDrawing(true);
        cursor.setX(x-1);
        int horizontal = 0;
        int vertical = 1;
        areaTracker.getBoardGrid()[x][y] = AreaState.OUTERBORDER;
        areaTracker.getBoardGrid()[x - vertical][y - horizontal] = AreaState.UNCOVERED;
        areaTracker.getBoardGrid()[x + vertical][y + horizontal] = AreaState.UNCOVERED;
        cursor.setCurrentMove(KeyCode.RIGHT);
        cursor.move();
        verify(stix, times(2)).addToStix(any());
    }*/

    @Test
    public void moveRightOuterBorder() throws Exception {
        createCursor();
        moveCursor(KeyCode.RIGHT, x + 1, y, true);
        Assert.assertEquals(x + 1, cursor.getX());
    }

    @Test
    public void dontMoveR2() throws Exception {
        createCursor();
        moveCursor(KeyCode.RIGHT, x, y, false);
        Assert.assertEquals(x, cursor.getX());
    }

    @Test
    public void dontMoveR3() throws Exception {
        createCursor();
        cursor.setDrawing(true);
        cursor.setX(cursor.getX() - 1);
        //stixCoordinates.add(new Point(x + 1, y));
        //areaTracker.getBoardGrid()[x][y] = AreaState.UNCOVERED;
        moveCursor(KeyCode.RIGHT, x, y, false);
        Assert.assertEquals(x, cursor.getX());
    }

   /* @Test
    public void dontMoveR4() throws Exception {
        createCursor();
        cursor.setDrawing(true);
        stixCoordinates.add(new Point(x + 2, y));
        moveCursor(KeyCode.RIGHT, x + 1, y, false);
        Assert.assertEquals(x, cursor.getX());
    }

    @Test
    public void dontMoveR5() throws Exception {
        createCursor();
        cursor.setDrawing(true);
        int horizontal = 1;
        int vertical = 0;
        areaTracker.getBoardGrid()[x + horizontal + vertical][y + horizontal + vertical] = AreaState.OUTERBORDER;
        when(areaTracker.getBoardGrid()).thenReturn(boardGrid);
        cursor.setCurrentMove(KeyCode.RIGHT);
        cursor.move();
        Assert.assertEquals(x, cursor.getX());
    }*/

    /*@Test
    public void dontMoveR6() throws Exception {
        createCursor();
        cursor.setDrawing(true);
        int horizontal = 1;
        int vertical = 0;
        areaTracker.getBoardGrid()[x + horizontal - vertical][y - horizontal + vertical] = AreaState.OUTERBORDER;
        //when(areaTracker.getBoardGrid()).thenReturn(boardGrid);
        cursor.setCurrentMove(KeyCode.RIGHT);
        cursor.move();
        Assert.assertEquals(x, cursor.getX());
    }*/

    @Test
    public void testGetCurrentMove() throws Exception {
        createCursor();
        cursor.setCurrentMove(KeyCode.RIGHT);
        Assert.assertEquals(KeyCode.RIGHT, cursor.getCurrentMove());
    }

    @Test
    public void isFast() throws Exception {
        createCursor();
        boolean oldValue = cursor.isFast();
        if (oldValue) {
            cursor.setSpeed(1);
        } else {
            cursor.setSpeed(2);
        }
        Assert.assertEquals(!oldValue, cursor.isFast());
    }


    @Test
    public void draw() throws Exception {
        Cursor spy = spy(new Cursor(new Point(1, 1), 1, 1, stix, 3, 1));
        spy.draw(new Canvas(1, 1).getGraphicsContext2D());
        verify(spy).getSpriteIndex();
    }


}