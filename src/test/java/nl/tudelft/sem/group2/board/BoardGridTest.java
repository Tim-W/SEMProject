package nl.tudelft.sem.group2.board;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.Point;

import static nl.tudelft.sem.group2.global.Globals.GRID_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.GRID_WIDTH;

/**
 * Tests boardGrid.
 */
public class BoardGridTest {


    private BoardGrid boardGrid;

    /**
     * setUp method.
     *
     * @throws Exception when the BoardGrid fails to do its job.
     */
    @Before
    public void setUp() throws Exception {
        boardGrid = new BoardGrid();
    }

    /**
     * Test if the areastates are initialized good.
     *
     * @throws Exception when the BoardGrid fails to do its job
     */
    @Test
    public void testAssertState() throws Exception {
        Assert.assertEquals(boardGrid.getState(new Point(0, 0)), AreaState.OUTERBORDER);
    }

    /**
     * test if getstate returns null.
     *
     * @throws Exception
     */
    @Test
    public void testGetStateNull() throws Exception {
        Assert.assertEquals(null, boardGrid.getState(new Point(-1, 0)));
    }

    /**
     * Test nothing has changed when setting an invalid coordinate.
     */
    @Test
    public void setStateNull() {
        BoardGrid copy = boardGrid;
        boardGrid.setState(new Coordinate(-1, -1), AreaState.UNCOVERED);

        Assert.assertEquals(copy, boardGrid);
    }

    /**
     * test if right int[] is returned.
     *
     * @throws Exception
     */
    @Test
    public void findPowerupLocation() throws Exception {
        Assert.assertEquals(0, boardGrid.findPowerupLocation(0)[0]);
        Assert.assertEquals(0, boardGrid.findPowerupLocation(0)[1]);
    }

    /**
     * test switch in cornerCovered.
     *
     * @throws Exception
     */
    @Test
    public void cornerCovered0OUTERBORDER() throws Exception {
        boardGrid.setState(new Point(1, 1), AreaState.OUTERBORDER);
        boardGrid.findPowerupLocation(0);
        Assert.assertEquals(AreaState.INNERBORDER, boardGrid.getState(new Point(0, 0)));
    }

    /**
     * test switch in cornerCovered.
     *
     * @throws Exception
     */
    @Test
    public void cornerCovered1OUTERBORDER() throws Exception {
        boardGrid.setState(new Point(GRID_WIDTH - 1, 1), AreaState.OUTERBORDER);
        boardGrid.findPowerupLocation(1);
        Assert.assertEquals(AreaState.INNERBORDER, boardGrid.getState(new Point(GRID_WIDTH, 0)));
    }

    /**
     * test switch in cornerCovered.
     *
     * @throws Exception
     */
    @Test
    public void cornerCovered2OUTERBORDER() throws Exception {
        boardGrid.setState(new Point(GRID_WIDTH - 1, GRID_HEIGHT - 1), AreaState.OUTERBORDER);
        boardGrid.findPowerupLocation(2);
        Assert.assertEquals(AreaState.INNERBORDER, boardGrid.getState(new Point(GRID_WIDTH, GRID_HEIGHT)));
    }

    /**
     * test switch in cornerCovered.
     *
     * @throws Exception
     */
    @Test
    public void cornerCovered3OUTERBORDER() throws Exception {
        boardGrid.setState(new Point(1, GRID_HEIGHT - 1), AreaState.OUTERBORDER);
        boardGrid.findPowerupLocation(3);
        Assert.assertEquals(AreaState.INNERBORDER, boardGrid.getState(new Point(0, GRID_HEIGHT)));
    }

    /**
     * test switch in cornerCovered.
     *
     * @throws Exception
     */
    @Test
    public void cornerCovered0INNERBORDER() throws Exception {
        boardGrid.setState(new Point(1, 1), AreaState.INNERBORDER);
        boardGrid.findPowerupLocation(0);
        Assert.assertEquals(AreaState.INNERBORDER, boardGrid.getState(new Point(0, 0)));
    }

    /**
     * test switch in cornerCovered.
     *
     * @throws Exception
     */
    @Test
    public void cornerCovered1INNERBORDER() throws Exception {
        boardGrid.setState(new Point(GRID_WIDTH - 1, 1), AreaState.INNERBORDER);
        boardGrid.findPowerupLocation(1);
        Assert.assertEquals(AreaState.INNERBORDER, boardGrid.getState(new Point(GRID_WIDTH, 0)));
    }

    /**
     * test switch in cornerCovered.
     *
     * @throws Exception
     */
    @Test
    public void cornerCovered2INNERBORDER() throws Exception {
        boardGrid.setState(new Point(GRID_WIDTH - 1, GRID_HEIGHT - 1), AreaState.INNERBORDER);
        boardGrid.findPowerupLocation(2);
        Assert.assertEquals(AreaState.INNERBORDER, boardGrid.getState(new Point(GRID_WIDTH, GRID_HEIGHT)));
    }

    /**
     * test switch in cornerCovered.
     *
     * @throws Exception
     */
    @Test
    public void cornerCovered3INNERBORDER() throws Exception {
        boardGrid.setState(new Point(1, GRID_HEIGHT - 1), AreaState.INNERBORDER);
        boardGrid.findPowerupLocation(3);
        Assert.assertEquals(AreaState.INNERBORDER, boardGrid.getState(new Point(0, GRID_HEIGHT)));
    }

    /**
     * Test cornerCovered with false quadrant.
     */
    @Test
    public void cornerCoveredFalse() {
        Assert.assertEquals(0, boardGrid.findPowerupLocation(42)[0]);
        Assert.assertEquals(0, boardGrid.findPowerupLocation(42)[1]);
    }
}