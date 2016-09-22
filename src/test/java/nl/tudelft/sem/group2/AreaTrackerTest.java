package nl.tudelft.sem.group2;

import java.awt.*;

/**
 * Test class for the AreaTracker class
 */
public class AreaTrackerTest {

    /**
     * Test for the calculateNewArea method
     *
     * @throws Exception
     */
    @org.junit.Test
    public void testCalculateNewArea() throws Exception {
        AreaTracker areaTracker = new AreaTracker(5, 5);
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