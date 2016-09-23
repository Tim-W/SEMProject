package nl.tudelft.sem.group2;

import java.awt.Point;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the AreaTracker class.
 */
public class AreaTrackerTest {
    private static final int TEST_MAP_WIDTH = 5;
    private static final int TEST_MAP_HEIGHT = 5;

    /**
     * Test for the calculateNewArea method.
     *
     * @throws Exception when the AreaTracker fails to do its job
     */
    @org.junit.Test
    public void testCalculateNewArea() throws Exception {
        AreaTracker areaTracker = new AreaTracker(TEST_MAP_WIDTH, TEST_MAP_HEIGHT);
        areaTracker.addToStix(new Point(2, 0));
        areaTracker.addToStix(new Point(2, 1));
        areaTracker.addToStix(new Point(2, 2));
        areaTracker.addToStix(new Point(2, 3));
        areaTracker.addToStix(new Point(2, 4));
        areaTracker.calculateNewArea(new Point(1, 2), true);

        AreaState[][] expectedGrid = createExpectedBoardGrid();

        AreaState[][] currentGrid = areaTracker.getBoardGrid();
        for (int i = 0; i < TEST_MAP_HEIGHT; i++) {
            for (int j = 0; j < TEST_MAP_WIDTH; j++) {
                assertEquals(expectedGrid[j][i], currentGrid[j][i]);
            }
        }

    }

    private AreaState[][] createExpectedBoardGrid() {
        AreaState[][] expectedGrid = new AreaState[TEST_MAP_WIDTH][TEST_MAP_HEIGHT];
        for (int y = 0; y < TEST_MAP_HEIGHT; y++) {
            for (int x = 0; x < TEST_MAP_WIDTH; x++) {
                if (x == 0) {
                    expectedGrid[x][y] = AreaState.OUTERBORDER;
                }
                else if ((x == 1 || x == 4) && (y == 0 || y == 4)) {
                    expectedGrid[x][y] = AreaState.OUTERBORDER;
                }
                else if (x == 1) {
                    expectedGrid[x][y] = AreaState.UNCOVERED;
                }
                else if (x == 2) {
                    expectedGrid[x][y] = AreaState.OUTERBORDER;
                }
                else if (x == 3 && (y == 0 || y == 4)) {
                    expectedGrid[x][y] = AreaState.INNERBORDER;
                }
                else if (x == 3) {
                    expectedGrid[x][y] = AreaState.FAST;
                }
                else if (x == 4 && y > 0 && y < 4) {
                    expectedGrid[x][y] = AreaState.INNERBORDER;
                }
            }
        }
        return  expectedGrid;
    }
}