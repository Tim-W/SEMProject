package nl.tudelft.sem.group2;

import javafx.embed.swing.JFXPanel;
import javafx.scene.input.KeyCode;
import nl.tudelft.sem.group2.board.AreaState;
import nl.tudelft.sem.group2.board.BoardGrid;
import nl.tudelft.sem.group2.cursorMovement.CursorKeypressHandler;
import nl.tudelft.sem.group2.gameController.GameController;
import nl.tudelft.sem.group2.global.Globals;
import nl.tudelft.sem.group2.level.Level;
import nl.tudelft.sem.group2.level.LevelHandler;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Stix;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.Point;

import static junit.framework.TestCase.assertTrue;
import static nl.tudelft.sem.group2.global.Globals.GRID_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.GRID_WIDTH;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Class which tests the behaviour of the cursorKeypressHandler class.
 */
public class CursorKeypressHandlerTest {

    private Cursor cursor;
    private Stix stix;
    private BoardGrid grid;
    private int x;
    private int y;
    private CursorKeypressHandler cursorKeypressHandler;

    @BeforeClass
    public static void beforeClass() {
        new JFXPanel();
    }

    @Before
    public void setUp() {
        stix = new Stix();
        //Point spawnPoint = new Point(0, 0);
        //cursor = new Cursor(spawnPoint, 2, 2, stix);
        grid = new BoardGrid();
        LevelHandler levelHandler = mock(LevelHandler.class);
        GameController.getInstance().setLevelHandler(levelHandler);
        when(levelHandler.getLevel()).thenReturn(mock(Level.class));
        when(levelHandler.getLevel().getBoardGrid()).thenReturn(grid);
        createCursor();
        KeyCode[] keyCodes = new KeyCode[] {KeyCode.W, KeyCode.S, KeyCode.A, KeyCode.D, KeyCode.Z, KeyCode.X};
        cursorKeypressHandler = new CursorKeypressHandler(cursor);
    }

    private void createCursor() {
        KeyCode[] keyCodes = new KeyCode[] {KeyCode.W, KeyCode.S, KeyCode.A, KeyCode.D, KeyCode.Z, KeyCode.X};
        cursor = new Cursor(new Point(0, 0), 2,
                2, stix, Globals.LIVES, 1);
        cursor.setSpeed(1);
        x = cursor.getX();
        y = cursor.getY();
    }

    private void moveCursor(KeyCode k, int x, int y, boolean outerborder) {
        grid = mock(BoardGrid.class);
        int horizontal = 0;
        int vertical = 0;
        if (k.equals(KeyCode.D) || k.equals(KeyCode.A)) {
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
        cursor.getCursorKeypressHandler().setCurrentMove(k);
        cursor.move();
    }

    @Test
    public void testStartStix() {
        cursorKeypressHandler.setCurrentMove(KeyCode.S);
        cursorKeypressHandler.cursorAssertMove();
        cursor.setDrawing(true);

        cursorKeypressHandler.setCurrentMove(KeyCode.D);
        cursorKeypressHandler.cursorAssertMove();
        // cursorKeypressHandler.cursorAssertMove(cursor, 1, 0);
        assertEquals(cursor.getX(), 1);
        assertEquals(cursor.getY(), 1);
        assertEquals(stix.getStixCoordinates().contains(new Point(0, 1)), true);
        assertEquals(stix.getStixCoordinates().contains(new Point(1, 1)), true);
    }

    @Test
    public void testContinueStix() {
        cursorKeypressHandler.setCurrentMove(KeyCode.S);
        cursorKeypressHandler.cursorAssertMove();
        cursor.setDrawing(true);
        cursorKeypressHandler.setCurrentMove(KeyCode.D);
        cursorKeypressHandler.cursorAssertMove();
        cursorKeypressHandler.cursorAssertMove();
        assertEquals(cursor.getX(), 2);
        assertEquals(cursor.getY(), 1);
        assertTrue(stix.getStixCoordinates().contains(new Point(0, 1)));
        assertTrue(stix.getStixCoordinates().contains(new Point(1, 1)));
        assertTrue(stix.getStixCoordinates().contains(new Point(2, 1)));
    }

    @Test
    public void testTryMoveToCloseToStix() {
        cursorKeypressHandler.setCurrentMove(KeyCode.S);
        cursorKeypressHandler.cursorAssertMove();
        cursor.setDrawing(true);
        cursor.getStix().addToStix(new Point(2, 1));
        cursorKeypressHandler.setCurrentMove(KeyCode.D);
        cursorKeypressHandler.cursorAssertMove();
        assertEquals(cursor.getX(), 0);
        assertEquals(cursor.getY(), 1);
        assertEquals(stix.getStixCoordinates().size(), 1);
    }

    @Test
    public void testTryToMoveToCovered() {
        cursorKeypressHandler.setCurrentMove(KeyCode.S);
        cursorKeypressHandler.cursorAssertMove();
        cursorKeypressHandler.setCurrentMove(KeyCode.D);
        cursorKeypressHandler.cursorAssertMove();
        assertEquals(cursor.getX(), 0);
        assertEquals(cursor.getY(), 1);
        assertTrue(stix.getStixCoordinates().isEmpty());
    }

    @Test
    public void testTryToMoveOfBorderNotDrawing() {
        cursorKeypressHandler.setCurrentMove(KeyCode.S);
        cursorKeypressHandler.cursorAssertMove();
        cursorKeypressHandler.setCurrentMove(KeyCode.D);
        cursorKeypressHandler.cursorAssertMove();
        assertEquals(cursor.getX(), 0);
        assertEquals(cursor.getY(), 1);
        assertTrue(stix.getStixCoordinates().isEmpty());
    }

    @Test
    public void testMoveToOuterborder() {
        cursorKeypressHandler.setCurrentMove(KeyCode.S);
        cursorKeypressHandler.cursorAssertMove();
        assertEquals(cursor.getX(), 0);
        assertEquals(cursor.getY(), 1);
        assertTrue(stix.getStixCoordinates().isEmpty());
        assertTrue(stix.getStixCoordinates().isEmpty());
    }

    @Test
    public void testNegativeX() {
        cursorKeypressHandler.setCurrentMove(KeyCode.A);
        cursorKeypressHandler.cursorAssertMove();
        assertEquals(cursor.getX(), 0);
        assertEquals(cursor.getY(), 0);
    }

    @Test
    public void testNegativeY() {
        cursorKeypressHandler.setCurrentMove(KeyCode.W);
        cursorKeypressHandler.cursorAssertMove();
        assertEquals(cursor.getX(), 0);
        assertEquals(cursor.getY(), 0);
    }

    @Test
    public void testToBigX() {
        cursor.setX(GRID_WIDTH + 1);
        int position = cursor.getX();
        cursorKeypressHandler.setCurrentMove(KeyCode.D);
        cursorKeypressHandler.cursorAssertMove();
        assertEquals(cursor.getX(), position);
    }

    @Test
    public void testToBigY() {
        cursor.setY(GRID_HEIGHT + 1);
        int position = cursor.getY();
        cursorKeypressHandler.setCurrentMove(KeyCode.S);
        cursorKeypressHandler.cursorAssertMove();
        assertEquals(cursor.getY(), position);
    }


    @Test
    public void dontMove() throws Exception {
        cursor.getCursorKeypressHandler().setCurrentMove(null);
        cursor.move();
        Assert.assertEquals(x, cursor.getX());
    }

  /*  @Test
    public void dontMoveL() throws Exception {
        cursor.setX(0);
        cursor.getCursorKeypressHandler().setCurrentMove(KeyCode.A);
        cursor.move();
        Assert.assertEquals(x, cursor.getX());
    }
*/
   /* @Test
    public void dontMoveR() throws Exception {
        cursor.setX(GRID_WIDTH);
        cursor.getCursorKeypressHandler().setCurrentMove(KeyCode.D);
        cursor.move();
        Assert.assertEquals(x, cursor.getX());
    }*/
/*
    @Test
    public void dontMoveU() throws Exception {
        cursor.setY(0);
        cursor.getCursorKeypressHandler().setCurrentMove(KeyCode.W);
        cursor.move();
        Assert.assertEquals(y, cursor.getY());
    }
*//**/
/*
    @Test
    public void dontMoveD() throws Exception {
        cursor.setY(GRID_HEIGHT);
        cursor.getCursorKeypressHandler().setCurrentMove(KeyCode.S);
        cursor.move();
        Assert.assertEquals(y, cursor.getY());
    }
*/

    @Test
    public void moveRight() throws Exception {
        cursor.setDrawing(true);
        moveCursor(KeyCode.D, x + 1, y, false);
        Assert.assertEquals(x + 1, cursor.getX());
    }
/*
    @Test
    public void moveRightDraw() throws Exception {
        createCursor();
        cursor.setDrawing(true);
        cursor.setX(x-1);
        int horizontal = 0;
        int vertical = 1;
        areaTracker.getBoardGrid()[x][y] = AreaState.OUTERBORDER;
        areaTracker.getBoardGrid()[x - vertical][y - horizontal] = AreaState.UNCOVERED;
        areaTracker.getBoardGrid()[x + vertical][y + horizontal] = AreaState.UNCOVERED;
        cursor.setCurrentMove(KeyCode.D);
        cursor.move();
        verify(stix, times(2)).addToStix(any());
    }**/

    @Test
    public void moveRightOuterBorder() throws Exception {
        moveCursor(KeyCode.D, x + 1, y, true);
        Assert.assertEquals(x + 1, cursor.getX());
    }
/*

    @Test
    public void dontMoveR2() throws Exception {
        moveCursor(KeyCode.D, x, y, false);
        Assert.assertEquals(x, cursor.getX());
    }
*/

    @Test
    public void dontMoveR3() throws Exception {
        cursor.setDrawing(true);
        cursor.setX(cursor.getX() - 1);
        moveCursor(KeyCode.D, x, y, false);
        Assert.assertEquals(x, cursor.getX());
    }
/*
    @Test
    public void dontMoveR4() throws Exception {
        createCursor();
        cursor.setDrawing(true);
        stixCoordinates.add(new Point(x + 2, y));
        moveCursor(KeyCode.D, x + 1, y, false);
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
        cursor.setCurrentMove(KeyCode.D);
        cursor.move();
        Assert.assertEquals(x, cursor.getX());
    }

    @Test
    public void dontMoveR6() throws Exception {
        createCursor();
        cursor.setDrawing(true);
        int horizontal = 1;
        int vertical = 0;
        areaTracker.getBoardGrid()[x + horizontal - vertical][y - horizontal + vertical] = AreaState.OUTERBORDER;
        //when(areaTracker.getBoardGrid()).thenReturn(boardGrid);
        cursor.setCurrentMove(KeyCode.D);
        cursor.move();
        Assert.assertEquals(x, cursor.getX());
    }*/

    @Test
    public void testGetCurrentMove() throws Exception {
        cursorKeypressHandler.setCurrentMove(KeyCode.D);
        Assert.assertEquals(KeyCode.D, cursorKeypressHandler.getCurrentMove());
    }

}