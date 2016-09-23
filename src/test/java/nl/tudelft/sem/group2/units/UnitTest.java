package nl.tudelft.sem.group2.units;

import nl.tudelft.sem.group2.AreaTracker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Tests Unit class.
 */
public class UnitTest {
    private Unit unit;

    /**
     * Setup test unit.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        AreaTracker areaTracker = new AreaTracker(0, 0);
        unit = Mockito.mock(Unit.class, Mockito.CALLS_REAL_METHODS);
        unit.setX(1);
        unit.setY(1);
        unit.setWidth(2);
        unit.setHeight(2);
        unit.setAreaTracker(areaTracker);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getX() throws Exception {
        Assert.assertEquals(unit.getX(), 1);
    }


    /**
     * @throws Exception
     */
    @Test
    public void getY() throws Exception {
        Assert.assertEquals(unit.getY(), 1);
    }

    /**
     * @throws Exception
     */
    @Test
    public void setX() throws Exception {
        unit.setX(0);
        Assert.assertEquals(unit.getX(), 0);
    }

    /**
     * @throws Exception
     */
    @Test
    public void setY() throws Exception {
        unit.setY(0);
        Assert.assertEquals(unit.getY(), 0);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getWidth() throws Exception {
        Assert.assertEquals(unit.getWidth(), 2);
    }

    /**
     * @throws Exception
     */
    @Test
    public void setWidth() throws Exception {
        unit.setWidth(1);
        Assert.assertEquals(unit.getWidth(), 1);
    }

    /**
     * @throws Exception
     */
    @Test
    public void getHeight() throws Exception {
        Assert.assertEquals(unit.getHeight(), 2);
    }

    /**
     * @throws Exception
     */
    @Test
    public void setHeight() throws Exception {
        unit.setHeight(1);
        Assert.assertEquals(unit.getHeight(), 1);
    }
//
//    @Test
//    public void intersect() throws Exception {
//        Qix qix = Mockito.mock(Qix.class);
//        Polygon colliderP = qix.toPolygon();
//    }
//
//    @Test
//    public void getAreaTracker() throws Exception {
//
//    }
//
//    @Test
//    public void setAreaTracker() throws Exception {
//
//    }

}