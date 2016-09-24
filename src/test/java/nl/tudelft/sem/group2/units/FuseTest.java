package nl.tudelft.sem.group2.units;

import javafx.embed.swing.JFXPanel;
import nl.tudelft.sem.group2.AreaTracker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.Point;
import java.util.LinkedList;

import static nl.tudelft.sem.group2.global.Globals.BOARD_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.BOARD_WIDTH;
import static org.mockito.Mockito.when;

/**
 * Created by gijs on 23-9-2016.
 */
public class FuseTest {
    private Fuse fuse;
    private LinkedList<Point> linkedList;
    AreaTracker areaTracker;

    @Before
    public void setUp() throws Exception {
        new JFXPanel();
        createFuse(new Fuse(3, 3, 3, 4));
    }

    public void createFuse(Fuse f) {
        fuse = f;
        areaTracker = Mockito.mock(AreaTracker.class);
        fuse.setAreaTracker(areaTracker);
    }

    @Test
    public void testConstructor() {
        Assert.assertEquals(fuse.getX(), 3);
        Assert.assertEquals(fuse.getY(), 3);
        Assert.assertEquals(fuse.getWidth(), 3);
        Assert.assertEquals(fuse.getHeight(), 4);
    }

    public void moveFuse(int x, int y) {
        linkedList = new LinkedList<>();
        linkedList.add(new Point(fuse.getX() + x, fuse.getY() + y));
        when(areaTracker.getStix()).thenReturn(linkedList);
        fuse.move();
    }

    @Test
    public void testMoveRight() {
        int oldx = fuse.getX();
        moveFuse(1, 0);
        Assert.assertEquals(oldx + 1, fuse.getX());
    }

    @Test
    public void testMoveRightLastX() {
        moveFuse(-1, 0);
        int oldx = fuse.getX();
        moveFuse(1, 0);
        Assert.assertEquals(fuse.getX(), oldx);
    }

    @Test
    public void testMoveLeft() {
        createFuse(new Fuse(BOARD_WIDTH - 1, BOARD_HEIGHT - 1, 1, 1));
        int oldx = fuse.getX();
        moveFuse(-1, 0);
        Assert.assertEquals(oldx - 1, fuse.getX());
    }

    @Test
    public void testMoveLeftLastX() {
        createFuse(new Fuse(BOARD_WIDTH - 2, BOARD_HEIGHT - 1, 1, 1));
        moveFuse(-1, 0);
        int oldx = fuse.getX();
        moveFuse(1, 0);
        Assert.assertEquals(oldx, fuse.getX());
    }

    @Test
    public void testNotMove() {
        int oldx = fuse.getX();
        fuse.setMoving(false);
        moveFuse(-1, 0);
        Assert.assertEquals(oldx, fuse.getX());
    }

    @org.junit.Test
    public void testToString() {
        Assert.assertEquals(fuse.toString(), "Fuse");
    }

    @Test
    public void testSetMoving() throws Exception {

    }

}