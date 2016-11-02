package nl.tudelft.sem.group2.units;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.Point;
import java.awt.Polygon;
import java.util.LinkedList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test suite for stix class.
 */
public class StixTest {

    private Stix stix;
    private LinkedList<Point> stixCoordinates;

    @Before
    public void setUp() {
        stix = new Stix();
        stixCoordinates = new LinkedList<>();
    }

    @Test
    public void emptyStix() {
        stix.addToStix(new Point(0, 0));
        Assert.assertFalse(stix.isStixEmpty());

        stix.emptyStix();
        Assert.assertTrue(stix.isStixEmpty());
    }

    @Test
    public void emptyStixnull() {
        stix = mock(Stix.class);
        stixCoordinates = null;
        when(stix.getStixCoordinates()).thenReturn(stixCoordinates);

        Assert.assertFalse(stix.isStixEmpty());
    }

    @Test
    public void addToStix() {
        stix.addToStix(new Point(0, 0));
        stixCoordinates.add(new Point(0, 0));

        Assert.assertEquals(stixCoordinates, stix.getStixCoordinates());
    }

    @Test
    public void intersectQix() {
        Qix qix = mock(Qix.class);
        int[] xCoords = new int[2];
        xCoords[0] = 0;
        xCoords[1] = 2;
        int[] yCoords = new int[2];
        yCoords[0] = 0;
        yCoords[1] = 2;
        when(qix.toPolygon()).thenReturn(new Polygon(xCoords, yCoords, 2));

        stix.addToStix(new Point(0, 0));

        Assert.assertTrue(stix.intersect(qix));
    }

    @Test
    public void noIntersectQix() {
        Qix qix = mock(Qix.class);
        int[] xCoords = new int[1];
        xCoords[0] = 0;
        int[] yCoords = new int[1];
        yCoords[0] = 0;
        when(qix.toPolygon()).thenReturn(new Polygon(xCoords, yCoords, 1));

        stix.addToStix(new Point(2, 2));

        Assert.assertFalse(stix.intersect(qix));
    }

    @Test
    public void intersectQixStixEmpty() {
        Qix qix = mock(Qix.class);
        stix.emptyStix();

        Assert.assertFalse(stix.intersect(qix));
    }

    @Test
    public void intersectCursorStixEmpty() {
        Cursor cursor = mock(Cursor.class);
        stix.emptyStix();

        Assert.assertFalse(stix.intersect(cursor));
    }

    @Test
    public void intersectCursor() {
        Cursor cursor = mock(Cursor.class);
        when(cursor.getX()).thenReturn(0);
        when(cursor.getY()).thenReturn(0);

        stix.addToStix(new Point(0, 0));
        Assert.assertTrue(stix.intersect(cursor));
    }

    @Test
    public void noIntersectCursor() {
        Cursor cursor = mock(Cursor.class);
        when(cursor.getX()).thenReturn(0);
        when(cursor.getY()).thenReturn(0);

        stix.addToStix(new Point(10, 10));
        Assert.assertFalse(stix.intersect(cursor));
    }
}
