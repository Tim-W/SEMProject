package nl.tudelft.sem.group2.game;

import javafx.embed.swing.JFXPanel;
import javafx.scene.canvas.Canvas;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Fuse;
import nl.tudelft.sem.group2.units.Sparx;
import nl.tudelft.sem.group2.units.Unit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashSet;

/**
 * Tests the Board class.
 */
public class BoardTest {
    private static final int MARGIN = 8;
    private Board board;

    /**
     * Setup a test canvas and board.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        new JFXPanel();
        Canvas canvas = new Canvas();
        board = new Board(canvas);
    }

    /**
     * @throws Exception
     */
    @Test
    public void gridToCanvas() throws Exception {
        int b = 100;
        Assert.assertEquals(Board.gridToCanvas(b), 2 * b + MARGIN - 1);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getUnits() throws Exception {
        HashSet<Unit> testUnitSet = new HashSet<Unit>();
        Assert.assertEquals(board.getUnits(), testUnitSet);
    }

    /**
     * @throws Exception
     */
    @Test
    public void addUnit() throws Exception {
        Unit testUnit = Mockito.mock(Unit.class, Mockito.CALLS_REAL_METHODS);
        HashSet<Unit> testUnitSet = new HashSet<Unit>();
        testUnitSet.add(testUnit);
        board.addUnit(testUnit);
        Assert.assertEquals(board.getUnits(), testUnitSet);

        //Cannot add more than one Fuse
        Fuse fuse = Mockito.mock(Fuse.class);
        board.addUnit(fuse);
        board.addUnit(fuse);
        Assert.assertEquals(board.getUnits().size(), 2);
    }

    /**
     * @throws Exception
     */
    @Test
    public void collisions() throws Exception {
        Cursor cursor = Mockito.mock(Cursor.class);
        Mockito.when(cursor.getX()).thenReturn(0);
        Mockito.when(cursor.getY()).thenReturn(0);
        Sparx sparx = Mockito.mock(Sparx.class);
        Mockito.when(sparx.getX()).thenReturn(0);
        Mockito.when(sparx.getY()).thenReturn(0);
        board.addUnit(cursor);
        board.addUnit(sparx);
    }

    /**
     * @throws Exception
     */
    @Test
    public void removeFuse() throws Exception {
        Fuse fuse = Mockito.mock(Fuse.class);
        board.addUnit(fuse);
        board.removeFuse();
        Assert.assertFalse(board.getUnits().contains(fuse));
    }

}