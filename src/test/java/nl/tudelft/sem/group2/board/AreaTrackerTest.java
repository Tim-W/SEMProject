package nl.tudelft.sem.group2.board;

import nl.tudelft.sem.group2.ScoreCounter;
import nl.tudelft.sem.group2.controllers.GameController;
import nl.tudelft.sem.group2.level.Level;
import nl.tudelft.sem.group2.level.LevelHandler;
import nl.tudelft.sem.group2.units.Stix;
import org.junit.Before;

import java.awt.Point;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for the AreaTracker class.
 */
public class AreaTrackerTest {
    private static final int TEST_MAP_WIDTH = 5;
    private static final int TEST_MAP_HEIGHT = 5;

    private Stix stix;
    private ScoreCounter scoreCounter = mock(ScoreCounter.class);
    private BoardGrid grid;
    private LevelHandler levelHandler;

    @Before
    public void setUp() throws Exception {
        stix = new Stix();
        grid = mock(BoardGrid.class);
        levelHandler = mock(LevelHandler.class);
        GameController.getInstance().setLevelHandler(levelHandler);
        when(levelHandler.getLevel()).thenReturn(mock(Level.class));
        when(levelHandler.getLevel().getBoardGrid()).thenReturn(grid);
    }

    /**
     * Test method for the default constructor of AreaTracker.
     *
     * @throws Exception when the AreaTracker fails to do its job
     */
    @org.junit.Test
    public void testConstructor() throws Exception {
        assertNotNull(AreaTracker.getInstance());
    }

    /**
     * Test verify setOuterBorder is called.
     *
     * @throws Exception when the AreaTracker fails to do its job
     */
    @org.junit.Test
    public void testVerifySetOuterBorder() throws Exception {
        stix.addToStix(new Point(1, 1));
        stix.addToStix(new Point(1, 2));
        AreaTracker.getInstance().calculateNewArea(new Point(1, 2), true, stix, scoreCounter);
        verify(grid, times(1)).setState(new Point(1, 1), AreaState.OUTERBORDER);
    }

    /**
     * Test for the calculateNewArea method when slow area is created.
     *
     * @throws Exception when the AreaTracker fails to do its job
     */
    @org.junit.Test
    public void testCalculateNewSlowArea() throws Exception {
        /*
        AreaTracker areaTracker = instantiateAreaTracker();

        areaTracker.calculateNewArea(new Coordinate(1, 2), false, stix, scoreCounter);

        AreaState[][] expectedGrid = createExpectedBoardGridQixAboveStix(false);

        AreaState[][] currentGrid = areaTracker.getBoardGrid();
        for (int i = 0; i < TEST_MAP_HEIGHT; i++) {
            for (int j = 0; j < TEST_MAP_WIDTH; j++) {
                assertEquals(expectedGrid[j][i], currentGrid[j][i]);
            }
        }
        */
    }

    /**
     * Test for the calculateNewArea method when fast area is created and qix is on other side.
     *
     * @throws Exception when the AreaTracker fails to do its job
     */
    @org.junit.Test
    public void testCalculateNewFastAreaWithQixOnOtherSide() throws Exception {
        /*
        AreaTracker areaTracker = instantiateAreaTracker();

        areaTracker.calculateNewArea(new Coordinate(3, 2), true, stix, scoreCounter);

        AreaState[][] expectedGrid = createExpectedBoardGridQixUnderStix(true);


        AreaState[][] currentGrid = areaTracker.getBoardGrid();
        for (int i = 0; i < TEST_MAP_HEIGHT; i++) {
            for (int j = 0; j < TEST_MAP_WIDTH; j++) {
                assertEquals(expectedGrid[j][i], currentGrid[j][i]);
            }
        }
        */
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

    private void instantiateStix() {
        stix = new Stix();
        stix.addToStix(new Point(2, 0));
        stix.addToStix(new Point(2, 1));
        stix.addToStix(new Point(2, 2));
        stix.addToStix(new Point(2, 3));
        stix.addToStix(new Point(2, 4));
    }
}