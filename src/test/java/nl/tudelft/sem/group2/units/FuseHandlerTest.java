package nl.tudelft.sem.group2.units;

import nl.tudelft.sem.group2.JavaFXThreadingRule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.awt.Point;
import java.util.LinkedList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test suite for fusehandler.
 */
public class FuseHandlerTest {

    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
    private Cursor cursor;
    private Fuse fuse;
    private Stix stix;
    private LinkedList<Point> points;
    private FuseHandler fuseHandler;

    @Before
    public void setUp() {
        cursor = mock(Cursor.class);
        fuse = mock(Fuse.class);
        stix = mock(Stix.class);
        cursor.setStix(stix);
        points = new LinkedList<>();
        fuseHandler = new FuseHandler(cursor);
        fuseHandler.setFuse(fuse);
        when(stix.getStixCoordinates()).thenReturn(points);
        when(cursor.getStix()).thenReturn(stix);
    }


    @Test
    public void handleFuseSpawn() {
        points.add(new Point(0, 0));
        when(cursor.getX()).thenReturn(0);
        when(cursor.getY()).thenReturn(0);
        fuseHandler.setFuse(null);
        fuseHandler.handleFuse();
        Assert.assertNotNull(fuse);
    }

    /**
     * Creates a non-moving fuse and tests if it is moving after handling.
     */
    @Test
    public void handleFuseExisting() {
        points.add(new Point(0, 0));
        fuse = new Fuse(0, 0, 0, 0, null, null);
        fuse.setMoving(false);
        fuseHandler.setFuse(fuse);

        when(cursor.getX()).thenReturn(0);
        when(cursor.getY()).thenReturn(0);

        fuseHandler.handleFuse();
        Assert.assertTrue(fuse.isMoving());
    }

    @Test
    public void removeFustTest() {
        fuse = new Fuse(0, 0, 0, 0, null, null);
        fuseHandler.setFuse(fuse);
        Assert.assertNotNull(fuse);

        fuseHandler.removeFuse();

        Assert.assertNull(fuseHandler.getFuse());
    }
}
