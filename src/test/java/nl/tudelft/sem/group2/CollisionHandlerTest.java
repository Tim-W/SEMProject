package nl.tudelft.sem.group2;

import javafx.embed.swing.JFXPanel;
import nl.tudelft.sem.group2.collisions.CollisionHandler;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Qix;
import nl.tudelft.sem.group2.units.Sparx;
import nl.tudelft.sem.group2.units.SparxDirection;
import nl.tudelft.sem.group2.units.Stix;
import nl.tudelft.sem.group2.units.Unit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for collisionHandler
 * Created by Dennis on 14-10-16.
 */
public class CollisionHandlerTest {

    private CollisionHandler handler;
    private HashSet<Unit> set;
    private Stix stix;
    private AreaTracker areaTracker;
    private AreaState[][] boardGrid = new AreaState[LaunchApp.getGridWidth() + 1][LaunchApp.getGridHeight() + 1];
    private Cursor cursor;

    @Before
    public void setUp() {
        new JFXPanel();
        stix = new Stix();
        handler = new CollisionHandler();
        areaTracker = mock(AreaTracker.class);
        when(areaTracker.getBoardGrid()).thenReturn(boardGrid);
        set = new HashSet<>();
        cursor = mock(Cursor.class);
        set.add(cursor);
    }

    @Test
    public void emptyTest() {
        set.clear();
        Assert.assertFalse(handler.collisions(set, stix));
    }

    @Test
    public void nullTest() {
        set = null;
        Assert.assertFalse(handler.collisions(set, stix));
    }

    @Test
    public void cursorSparxTest() {
        //Cursor cursor = new Cursor(0, 0, 1, 1, stix, areaTracker);
        Sparx sparx = new Sparx(0, 0, 1, 1, SparxDirection.LEFT, areaTracker);
        //set.add(cursor);
        set.add(sparx);
        when(cursor.intersect(sparx)).thenReturn(true);
        Assert.assertTrue(handler.collisions(set, stix));
    }

    @Test
    public void QixStixTest() {
        Qix qix = mock(Qix.class);
        stix = mock(Stix.class);
        when(stix.intersect(qix)).thenReturn(true);
        set.add(qix);
        Assert.assertTrue(handler.collisions(set, stix));
    }


    @Test
    public void QixCursorTest() {
        Qix qix = mock(Qix.class);
        set.add(qix);
        stix = mock(Stix.class);
        when(stix.intersect(qix)).thenReturn(false);
        when(qix.intersect(cursor)).thenReturn(true);
        when(cursor.uncoveredOn(anyInt(), anyInt())).thenReturn(true);
        Assert.assertTrue(handler.collisions(set, stix));
    }

    @Test
    public void noCollisionsTest() {
        Assert.assertFalse(handler.collisions(set, stix));
    }

    @Test
    public void noCollisionsTest2() {
        Sparx sparx = mock(Sparx.class);
        set.add(sparx);
        when(cursor.intersect(sparx)).thenReturn(false);
        Assert.assertFalse(handler.collisions(set, stix));
    }
}

