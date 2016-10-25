package nl.tudelft.sem.group2.units;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.JavaFXThreadingRule;
import nl.tudelft.sem.group2.scenes.GameScene;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.Point;
import java.util.LinkedList;

import static nl.tudelft.sem.group2.global.Globals.QIX_START_X;
import static nl.tudelft.sem.group2.global.Globals.QIX_START_Y;
import static org.mockito.Mockito.spy;

/**
 * Tests Unit class.
 */
public class UnitTest {
    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
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
        areaTracker = new AreaTracker(0, 0);
        unit = Mockito.mock(Unit.class, Mockito.CALLS_REAL_METHODS);
        unit.setX(1);
        unit.setY(1);
        unit.setWidth(2);
        unit.setHeight(2);
        unit.setAreaTracker(areaTracker);
        //Initialize a new gamescene so when a new cursor gets created it can obtain the scorescene
        Group group = new Group();
        GameScene gameScene = new GameScene(group, Color.BLACK);
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
    public void intersectUnitUnit() throws Exception {
        ConcreteUnit concreteUnit = spy(new ConcreteUnit(1, 1, 5, 5, areaTracker));
        ConcreteUnit concreteUnit2 = spy(new ConcreteUnit(1, 1, 5, 5, areaTracker));
        Assert.assertTrue(concreteUnit.intersect(concreteUnit2));
    }

    @Test
    public void intersectQixCursor() throws Exception {
        Qix qix = spy(new Qix(areaTracker));
        LinkedList<float[]> linkedList = new LinkedList<>();
        linkedList.add(new float[] {QIX_START_X, QIX_START_Y});
        qix.setOldCoordinates(linkedList);
        qix.setOldDirections(linkedList);
        Cursor cursor = spy(new Cursor(new Point(QIX_START_X, QIX_START_Y), 10, 10, areaTracker, stix, Color.RED, 1));
        Assert.assertTrue(qix.intersect(cursor));
    }

    @Test
    public void intersectNotUnitUnit() throws Exception {
        ConcreteUnit concreteUnit = spy(new ConcreteUnit(20, 20, 5, 5, areaTracker));
        ConcreteUnit concreteUnit2 = spy(new ConcreteUnit(1, 1, 5, 5, areaTracker));
        Assert.assertFalse(concreteUnit.intersect(concreteUnit2));
    }

    @Test
    public void intersectNotQixCursor() throws Exception {
        Qix qix = spy(new Qix(areaTracker));
        LinkedList<float[]> linkedList = new LinkedList<>();
        linkedList.add(new float[] {1, 1});
        qix.setOldCoordinates(linkedList);
        qix.setOldDirections(linkedList);
        Cursor cursor = spy(new Cursor(new Point(100, 100), 10, 10, areaTracker, stix, Color.RED, 1));
        Assert.assertFalse(qix.intersect(cursor));
    }

    @Test
    public void intersectFuseCursor() throws Exception {
        Fuse fuse = spy(new Fuse(1, 1, 5, 5, areaTracker, stix));
        Cursor cursor = spy(new Cursor(new Point(1, 1), 5, 5, areaTracker, stix, Color.RED, 1));
        Assert.assertTrue(fuse.intersect(cursor));
    }

    @Test
    public void intersectNotFuseCursor() throws Exception {
        Fuse fuse = spy(new Fuse(20, 20, 5, 5, areaTracker, stix));
        Cursor cursor = spy(new Cursor(new Point(1, 1), 5, 5, areaTracker, stix, Color.RED, 1));
        Assert.assertFalse(fuse.intersect(cursor));
    }

    @Test
    public void intersectCursorQix() throws Exception {
        Qix qix = spy(new Qix(areaTracker));
        LinkedList<float[]> linkedList = new LinkedList<>();
        linkedList.add(new float[] {QIX_START_X, QIX_START_Y});
        qix.setOldCoordinates(linkedList);
        qix.setOldDirections(linkedList);
        Cursor cursor = spy(new Cursor(new Point(QIX_START_X, QIX_START_Y), 10, 10, areaTracker, stix, Color.BLACK, 1));
        Assert.assertTrue(cursor.intersect(qix));
    }

    @Test
    public void intersectNotCursorQix() throws Exception {
        Qix qix = spy(new Qix(areaTracker));
        LinkedList<float[]> linkedList = new LinkedList<>();
        linkedList.add(new float[] {1, 1});
        qix.setOldCoordinates(linkedList);
        qix.setOldDirections(linkedList);
        Cursor cursor = spy(new Cursor(new Point(100, 100), 10, 10, areaTracker, stix, Color.RED, 1));
        Assert.assertFalse(cursor.intersect(qix));
    }

    @Test
    public void getAreaTracker() throws Exception {

    }

    @Test
    public void setAreaTracker() throws Exception {

    }
}