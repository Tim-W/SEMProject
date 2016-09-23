package nl.tudelft.sem.group2;

import java.awt.Point;

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
        areaTracker.printBoardGrid();
        areaTracker.addToStix(new Point(2, 0));
        areaTracker.addToStix(new Point(2, 1));
        areaTracker.addToStix(new Point(2, 2));
        areaTracker.addToStix(new Point(2, 3));
        areaTracker.addToStix(new Point(2, 4));
        areaTracker.calculateNewArea(new Point(1, 2), true);
        areaTracker.printBoardGrid();
    }
}