package nl.tudelft.sem.group2.units;

import javafx.embed.swing.JFXPanel;
import nl.tudelft.sem.group2.AreaTracker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.LinkedList;

import static nl.tudelft.sem.group2.global.Globals.QIX_START_X;
import static nl.tudelft.sem.group2.global.Globals.QIX_START_Y;
import static org.mockito.Mockito.spy;

/**
 * Tests Unit class.
 */
public class UnitTest {
    private Unit unit;
    private Stix stix;
    private AreaTracker areaTracker;

    /**
     * Setup test unit.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        new JFXPanel();
        stix = new Stix();
        areaTracker = new AreaTracker(0, 0, stix);
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

    @Test
    public void intersectQixCursor() throws Exception {
        Qix qix = spy(new Qix(areaTracker));
        LinkedList<float[]> linkedList = new LinkedList<>();
        linkedList.add(new float[] {QIX_START_X, QIX_START_Y});
        qix.setOldCoordinates(linkedList);
        qix.setOldDirections(linkedList);
        Cursor cursor = spy(new Cursor(QIX_START_X, QIX_START_Y, 10, 10, stix, areaTracker));
        Assert.assertTrue(qix.intersect(cursor));
    }

    @Test
    public void intersectNotQixCursor() throws Exception {
        Qix qix = spy(new Qix(areaTracker));
        LinkedList<float[]> linkedList = new LinkedList<>();
        linkedList.add(new float[] {1, 1});
        qix.setOldCoordinates(linkedList);
        qix.setOldDirections(linkedList);
        Cursor cursor = spy(new Cursor(100, 100, 10, 10, stix, areaTracker));
        Assert.assertFalse(qix.intersect(cursor));
    }

    @Test
    public void intersectFuseCursor() throws Exception {
        Fuse fuse = spy(new Fuse(1, 1, 5, 5, stix, areaTracker));
        Cursor cursor = spy(new Cursor(1, 1, 5, 5, stix, areaTracker));
        Assert.assertTrue(fuse.intersect(cursor));
    }

    @Test
    public void intersectNotFuseCursor() throws Exception {
        Fuse fuse = spy(new Fuse(20, 20, 5, 5, stix, areaTracker));
        Cursor cursor = spy(new Cursor(1, 1, 5, 5, stix, areaTracker));
        Assert.assertFalse(fuse.intersect(cursor));
    }

    @Test
    public void intersectCursorQix() throws Exception {
        Qix qix = spy(new Qix(areaTracker));
        LinkedList<float[]> linkedList = new LinkedList<>();
        linkedList.add(new float[] {QIX_START_X, QIX_START_Y});
        qix.setOldCoordinates(linkedList);
        qix.setOldDirections(linkedList);
        Cursor cursor = spy(new Cursor(QIX_START_X, QIX_START_Y, 10, 10, stix, areaTracker));
        Assert.assertTrue(cursor.intersect(qix));
    }

    @Test
    public void intersectNotCursorQix() throws Exception {
        Qix qix = spy(new Qix(areaTracker));
        LinkedList<float[]> linkedList = new LinkedList<>();
        linkedList.add(new float[] {1, 1});
        qix.setOldCoordinates(linkedList);
        qix.setOldDirections(linkedList);
        Cursor cursor = spy(new Cursor(100, 100, 10, 10, stix, areaTracker));
        Assert.assertFalse(cursor.intersect(qix));
    }

    @Test
    public void getAreaTracker() throws Exception {

    }

    @Test
    public void setAreaTracker() throws Exception {

    }

}