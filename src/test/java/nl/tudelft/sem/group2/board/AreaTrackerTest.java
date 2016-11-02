package nl.tudelft.sem.group2.board;

import nl.tudelft.sem.group2.ScoreCounter;
import nl.tudelft.sem.group2.board.Coordinate;
import nl.tudelft.sem.group2.global.Globals;
import nl.tudelft.sem.group2.board.AreaState;
import nl.tudelft.sem.group2.board.AreaTracker;
import nl.tudelft.sem.group2.units.Stix;
import org.junit.Assert;
import org.junit.Test;

import java.awt.Point;

import static nl.tudelft.sem.group2.global.Globals.GRID_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.GRID_WIDTH;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Test class for the AreaTracker class.
 */
public class AreaTrackerTest {
    private static final int TEST_MAP_WIDTH = 5;
    private static final int TEST_MAP_HEIGHT = 5;

    private Stix stix;
    private ScoreCounter scoreCounter = mock(ScoreCounter.class);

    /**
     * Test method for the default constructor of AreaTracker.
     *
     * @throws Exception when the AreaTracker fails to do its job
     */
    @org.junit.Test
    public void testConstructor() throws Exception {
        stix = new Stix();
        AreaTracker areaTracker = new AreaTracker();

        AreaState[][] expectedGrid = new AreaState[GRID_WIDTH + 1][GRID_HEIGHT + 1];

        for (int x = 0; x < expectedGrid.length; x++) {
            for (int y = 0; y < expectedGrid[x].length; y++) {
                expectedGrid[x][y] = AreaState.UNCOVERED;
            }
        }

        if (expectedGrid.length > 0) {
            for (int i = 0; i < expectedGrid.length; i++) {
                expectedGrid[i][0] = AreaState.OUTERBORDER;
                expectedGrid[i][expectedGrid[0].length - 1] = AreaState.OUTERBORDER;
            }

            for (int i = 0; i < expectedGrid[0].length; i++) {
                expectedGrid[0][i] = AreaState.OUTERBORDER;
                expectedGrid[expectedGrid[0].length - 1][i] = AreaState.OUTERBORDER;
            }
        }

        AreaState[][] currentGrid = areaTracker.getBoardGrid();
        for (int i = 0; i < expectedGrid.length; i++) {
            for (int j = 0; j < expectedGrid[i].length; j++) {
                assertEquals(expectedGrid[i][j], currentGrid[j][i]);
            }
        }
    }

    /**
     * Test for the calculateNewArea method when fast area is created.
     *
     * @throws Exception when the AreaTracker fails to do its job
     */
    @org.junit.Test
    public void testCalculateNewFastArea() throws Exception {
        AreaTracker areaTracker = instantiateAreaTracker();

        areaTracker.calculateNewArea(new Coordinate(1, 2), true, stix, scoreCounter);

        AreaState[][] expectedGrid = createExpectedBoardGridQixAboveStix(true);

        AreaState[][] currentGrid = areaTracker.getBoardGrid();
        for (int i = 0; i < TEST_MAP_HEIGHT; i++) {
            for (int j = 0; j < TEST_MAP_WIDTH; j++) {
                assertEquals(expectedGrid[j][i], currentGrid[j][i]);
            }
        }
    }

    /**
     * Test for the calculateNewArea method when slow area is created.
     *
     * @throws Exception when the AreaTracker fails to do its job
     */
    @org.junit.Test
    public void testCalculateNewSlowArea() throws Exception {
        AreaTracker areaTracker = instantiateAreaTracker();

        areaTracker.calculateNewArea(new Coordinate(1, 2), false, stix, scoreCounter);

        AreaState[][] expectedGrid = createExpectedBoardGridQixAboveStix(false);

        AreaState[][] currentGrid = areaTracker.getBoardGrid();
        for (int i = 0; i < TEST_MAP_HEIGHT; i++) {
            for (int j = 0; j < TEST_MAP_WIDTH; j++) {
                assertEquals(expectedGrid[j][i], currentGrid[j][i]);
            }
        }
    }

    /**
     * Test for the calculateNewArea method when fast area is created and qix is on other side.
     *
     * @throws Exception when the AreaTracker fails to do its job
     */
    @org.junit.Test
    public void testCalculateNewFastAreaWithQixOnOtherSide() throws Exception {
        AreaTracker areaTracker = instantiateAreaTracker();

        areaTracker.calculateNewArea(new Coordinate(3, 2), true, stix, scoreCounter);

        AreaState[][] expectedGrid = createExpectedBoardGridQixUnderStix(true);


        AreaState[][] currentGrid = areaTracker.getBoardGrid();
        for (int i = 0; i < TEST_MAP_HEIGHT; i++) {
            for (int j = 0; j < TEST_MAP_WIDTH; j++) {
                assertEquals(expectedGrid[j][i], currentGrid[j][i]);
            }
        }
    }

    private AreaState[][] createExpectedBoardGridQixAboveStix(boolean fastArea) {
        AreaState[][] expectedGrid = new AreaState[TEST_MAP_WIDTH][TEST_MAP_HEIGHT];
        for (int y = 0; y < TEST_MAP_HEIGHT; y++) {
            for (int x = 0; x < TEST_MAP_WIDTH; x++) {
                if (x == 0) {
                    expectedGrid[x][y] = AreaState.OUTERBORDER;
                } else if ((x == 1 || x == 4) && (y == 0 || y == 4)) {
                    expectedGrid[x][y] = AreaState.OUTERBORDER;
                } else if (x == 1) {
                    expectedGrid[x][y] = AreaState.UNCOVERED;
                } else if (x == 2) {
                    expectedGrid[x][y] = AreaState.OUTERBORDER;
                } else if (x == 3 && (y == 0 || y == 4)) {
                    expectedGrid[x][y] = AreaState.INNERBORDER;
                } else if (x == 3 && fastArea) {
                    expectedGrid[x][y] = AreaState.FAST;
                } else if (x == 3) {
                    expectedGrid[x][y] = AreaState.SLOW;
                } else if (x == 4 && y > 0 && y < 4) {
                    expectedGrid[x][y] = AreaState.INNERBORDER;
                }
            }
        }
        return expectedGrid;
    }

    private AreaState[][] createExpectedBoardGridQixUnderStix(boolean fastArea) {
        AreaState[][] expectedGrid = new AreaState[TEST_MAP_WIDTH][TEST_MAP_HEIGHT];
        for (int y = 0; y < TEST_MAP_HEIGHT; y++) {
            for (int x = 0; x < TEST_MAP_WIDTH; x++) {
                if (x == 0 && (y == 0 || y == 4)) {
                    expectedGrid[x][y] = AreaState.OUTERBORDER;
                } else if (x == 0) {
                    expectedGrid[x][y] = AreaState.INNERBORDER;
                } else if (x == 1 && (y == 0 || y == 4)) {
                    expectedGrid[x][y] = AreaState.INNERBORDER;
                } else if (x == 1 && fastArea) {
                    expectedGrid[x][y] = AreaState.FAST;
                } else if (x == 1) {
                    expectedGrid[x][y] = AreaState.SLOW;
                } else if (x == 2) {
                    expectedGrid[x][y] = AreaState.OUTERBORDER;
                } else if (x == 3 && (y == 0 || y == 4)) {
                    expectedGrid[x][y] = AreaState.OUTERBORDER;
                } else if (x == 3) {
                    expectedGrid[x][y] = AreaState.UNCOVERED;
                } else if (x == 4) {
                    expectedGrid[x][y] = AreaState.OUTERBORDER;
                }
            }
        }
        return expectedGrid;
    }

    /**
     * Tests if the findPowerupLocation returns the corner if its not covered.
     */
    @Test
    public void findPowerupLocationAtCornerTest0() {
        AreaTracker areaTracker = new AreaTracker(Globals.BOARD_WIDTH, Globals.BOARD_HEIGHT);
        int[] coordinates = areaTracker.findPowerupLocation(0);
        assertEquals(0, coordinates[0]);
        assertEquals(0, coordinates[1]);
    }

    /**
     * Tests if the findPowerupLocation returns the corner if its not covered.
     */
    @Test
    public void findPowerupLocationAtCornerTest1() {
        AreaTracker areaTracker = new AreaTracker(Globals.BOARD_WIDTH, Globals.BOARD_HEIGHT);
        int[] coordinates = areaTracker.findPowerupLocation(1);
        assertEquals(Globals.BOARD_WIDTH / 2, coordinates[0]);
        assertEquals(0, coordinates[1]);
    }

    /**
     * Tests if the findPowerupLocation returns the corner if its not covered.
     */
    @Test
    public void findPowerupLocationAtCornerTest2() {
        AreaTracker areaTracker = new AreaTracker(Globals.BOARD_WIDTH, Globals.BOARD_HEIGHT);
        int[] coordinates = areaTracker.findPowerupLocation(2);
        assertEquals(Globals.BOARD_WIDTH / 2, coordinates[0]);
        assertEquals(Globals.BOARD_HEIGHT / 2, coordinates[1]);
    }

    /**
     * Tests if the findPowerupLocation returns the corner if its not covered.
     */
    @Test
    public void findPowerupLocationAtCornerTest3() {
        AreaTracker areaTracker = new AreaTracker();
        int[] coordinates = areaTracker.findPowerupLocation(3);
        assertEquals(0, coordinates[0]);
        assertEquals(Globals.BOARD_HEIGHT / 2, coordinates[1]);
    }

    /**
     * Tests if the corner gets set to innerborder when the area around it is covered.
     */
    @Test
    public void testCornerBorders0() {
        AreaTracker areaTracker = new AreaTracker();
        areaTracker.getBoardGrid()[1][1] = AreaState.FAST;
        areaTracker.findPowerupLocation(0);
        assertEquals(AreaState.INNERBORDER, areaTracker.getBoardGrid()[0][0]);
    }

    /**
     * Tests if the corner gets set to innerborder when the area around it is covered.
     */
    @Test
    public void testCornerBorders1() {
        AreaTracker areaTracker = new AreaTracker();
        areaTracker.getBoardGrid()[Globals.BOARD_WIDTH / 2 - 1][1] = AreaState.FAST;
        areaTracker.findPowerupLocation(1);
        assertEquals(AreaState.INNERBORDER, areaTracker.getBoardGrid()[Globals.BOARD_WIDTH / 2][0]);
    }

    /**
     * Tests if the corner gets set to innerborder when the area around it is covered.
     */
    @Test
    public void testCornerBorders2() {
        AreaTracker areaTracker = new AreaTracker();
        areaTracker.getBoardGrid()[Globals.BOARD_WIDTH / 2 - 1][Globals.BOARD_WIDTH / 2 - 1] = AreaState.FAST;
        areaTracker.findPowerupLocation(2);
        assertEquals(AreaState.INNERBORDER, areaTracker.getBoardGrid()[Globals.BOARD_WIDTH / 2][Globals.BOARD_WIDTH / 2]);
    }

    /**
     * Tests if the corner gets set to innerborder when the area around it is covered.
     */
    @Test
    public void testCornerBorders3() {
        AreaTracker areaTracker = new AreaTracker();
        areaTracker.getBoardGrid()[1][Globals.BOARD_HEIGHT / 2 - 1] = AreaState.FAST;
        areaTracker.findPowerupLocation(3);
        assertEquals(AreaState.INNERBORDER,
                areaTracker.getBoardGrid()[0][Globals.BOARD_WIDTH / 2]);
    }

    /**
     * Tests for a false quadrant input.
     */
    @Test
    public void testFalseQuadrant() {
        AreaTracker areaTracker = new AreaTracker();
        int[] coord = areaTracker.findPowerupLocation(4);
        Assert.assertEquals(0, coord[0]);
        Assert.assertEquals(0, coord[1]);
    }


    private AreaTracker instantiateAreaTracker() {
        stix = new Stix();
        stix.addToStix(new Point(2, 0));
        stix.addToStix(new Point(2, 1));
        stix.addToStix(new Point(2, 2));
        stix.addToStix(new Point(2, 3));
        stix.addToStix(new Point(2, 4));
        return new AreaTracker(TEST_MAP_WIDTH, TEST_MAP_HEIGHT);
    }
}