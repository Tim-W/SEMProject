package nl.tudelft.sem.group2;

import nl.tudelft.sem.group2.board.BoardGrid;
import nl.tudelft.sem.group2.controllers.GameController;
import nl.tudelft.sem.group2.global.Globals;
import nl.tudelft.sem.group2.level.Level;
import nl.tudelft.sem.group2.level.LevelHandler;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Stix;
import org.junit.Before;
import org.junit.Test;

import java.awt.Point;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Class which tests the behaviour of the KeypressHandler class.
 */
public class KeypressHandlerTest {

    private Cursor cursor;
    private Stix stix;
    private BoardGrid grid;

    @Before
    public void setUp() {
        stix = new Stix();
        Point spawnPoint = new Point(0, 0);
        cursor = new Cursor(spawnPoint, 2, 2, stix);
        grid = new BoardGrid();
        LevelHandler levelHandler = mock(LevelHandler.class);
        GameController.getInstance().setLevelHandler(levelHandler);
        when(levelHandler.getLevel()).thenReturn(mock(Level.class));
        when(levelHandler.getLevel().getBoardGrid()).thenReturn(grid);
    }

    @Test
    public void testStartStix() {
        KeypressHandler.cursorAssertMove(cursor, 0, 1);
        cursor.setDrawing(true);
        KeypressHandler.cursorAssertMove(cursor, 1, 0);
        assertEquals(cursor.getX(), 1);
        assertEquals(cursor.getY(), 1);
        assertEquals(stix.getStixCoordinates().contains(new Point(0, 1)), true);
        assertEquals(stix.getStixCoordinates().contains(new Point(1, 1)), true);
    }

    @Test
    public void testContinueStix() {
        KeypressHandler.cursorAssertMove(cursor, 0, 1);
        cursor.setDrawing(true);
        KeypressHandler.cursorAssertMove(cursor, 1, 0);
        KeypressHandler.cursorAssertMove(cursor, 1, 0);
        assertEquals(cursor.getX(), 2);
        assertEquals(cursor.getY(), 1);
        assertTrue(stix.getStixCoordinates().contains(new Point(0, 1)));
        assertTrue(stix.getStixCoordinates().contains(new Point(1, 1)));
        assertTrue(stix.getStixCoordinates().contains(new Point(2, 1)));
    }

    @Test
    public void testTryMoveToCloseToStix() {
        KeypressHandler.cursorAssertMove(cursor, 0, 1);
        cursor.setDrawing(true);
        cursor.getStix().addToStix(new Point(2, 1));
        KeypressHandler.cursorAssertMove(cursor, 1, 0);
        assertEquals(cursor.getX(), 0);
        assertEquals(cursor.getY(), 1);
        assertEquals(stix.getStixCoordinates().size(), 1);
    }

    @Test
    public void testTryToMoveToCovered() {
        KeypressHandler.cursorAssertMove(cursor, 0, 1);
        KeypressHandler.cursorAssertMove(cursor, 1, 0);
        assertEquals(cursor.getX(), 0);
        assertEquals(cursor.getY(), 1);
        assertTrue(stix.getStixCoordinates().isEmpty());
    }

    @Test
    public void testTryToMoveOfBorderNotDrawing() {
        KeypressHandler.cursorAssertMove(cursor, 0, 1);
        KeypressHandler.cursorAssertMove(cursor, 1, 0);
        assertEquals(cursor.getX(), 0);
        assertEquals(cursor.getY(), 1);
        assertTrue(stix.getStixCoordinates().isEmpty());
    }

    @Test
    public void testMoveToOuterborder() {
        KeypressHandler.cursorAssertMove(cursor, 0, 1);
        assertEquals(cursor.getX(), 0);
        assertEquals(cursor.getY(), 1);
        assertTrue(stix.getStixCoordinates().isEmpty());
        assertTrue(stix.getStixCoordinates().isEmpty());
    }

    @Test
    public void testNegativeX() {
        KeypressHandler.cursorAssertMove(cursor, -1, 0);
        assertEquals(cursor.getX(), 0);
        assertEquals(cursor.getY(), 0);
    }

    @Test
    public void testNegativeY() {
        KeypressHandler.cursorAssertMove(cursor, 0, -1);
        assertEquals(cursor.getX(), 0);
        assertEquals(cursor.getY(), 0);
    }

    @Test
    public void testToBigX() {
        KeypressHandler.cursorAssertMove(cursor, Globals.GRID_WIDTH + 1, 0);
        assertEquals(cursor.getX(), 0);
        assertEquals(cursor.getY(), 0);
    }

    @Test
    public void testToBigY() {
        KeypressHandler.cursorAssertMove(cursor, 0, Globals.GRID_HEIGHT + 1);
        assertEquals(cursor.getX(), 0);
        assertEquals(cursor.getY(), 0);
    }


}