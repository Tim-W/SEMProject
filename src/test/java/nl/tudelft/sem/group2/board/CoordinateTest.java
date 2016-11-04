package nl.tudelft.sem.group2.board;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for Coordinate.
 */
public class CoordinateTest {

    private Coordinate coordinate;

    /**
     * sets up the coordinate.
     */
    @Before
    public void setUp() {
        coordinate = new Coordinate(0, 0);
    }

    /**
     * Tests the xBetween method.
     */
    @Test
    public void xBetweenTest1() {
        Assert.assertTrue(coordinate.xBetween(0, 1));
    }

    /**
     * Tests the xBetween method.
     */
    @Test
    public void xBetweenTest2() {
        Assert.assertFalse(coordinate.xBetween(1, 1));
    }

    /**
     * Tests the xBetween method.
     */
    @Test
    public void xBetweenTest3() {
        Assert.assertFalse(coordinate.xBetween(0, 0));
    }

    /**
     * Tests the xBetween method.
     */
    @Test
    public void xBetweenTest4() {
        Assert.assertFalse(coordinate.xBetween(1, 0));
    }


    /**
     * Tests the yBetween method.
     */
    @Test
    public void yBetweenTest1() {
        Assert.assertTrue(coordinate.yBetween(0, 1));
    }

    /**
     * Tests the yBetween method.
     */
    @Test
    public void yBetweenTest2() {
        Assert.assertFalse(coordinate.yBetween(1, 1));
    }

    /**
     * Tests the yBetween method.
     */
    @Test
    public void yBetweenTest3() {
        Assert.assertFalse(coordinate.yBetween(0, 0));
    }

    /**
     * Tests the yBetween method.
     */
    @Test
    public void yBetweenTest4() {
        Assert.assertFalse(coordinate.yBetween(1, 0));
    }
}
