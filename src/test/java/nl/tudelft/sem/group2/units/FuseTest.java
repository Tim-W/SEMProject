package nl.tudelft.sem.group2.units;

import javafx.embed.swing.JFXPanel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.Point;
import java.util.LinkedList;

import static nl.tudelft.sem.group2.global.Globals.BOARD_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.BOARD_WIDTH;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by gijs on 23-9-2016.
 */

public class FuseTest {
    private Fuse fuse;
    private LinkedList<Point> linkedList;
    private Stix stix = mock(Stix.class);

    @BeforeClass
    public static void beforeClass() {
        new JFXPanel();
    }

    @Before
    public void setUp() throws Exception {
        createFuse(new Fuse(3, 3, 3, 4, stix));
        fuse.setDelay(0);
    }

    private void createFuse(Fuse f) {
        fuse = f;
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
        when(stix.getStixCoordinates()).thenReturn(linkedList);
        fuse.move();
    }


    @Test
    public void testMoveRightLastX() {
        moveFuse(-1, 0);
        int oldx = fuse.getX();
        moveFuse(1, 0);
        Assert.assertEquals(fuse.getX(), oldx);
    }

    @Test
    public void testMoveLeftLastX() {
        createFuse(new Fuse(BOARD_WIDTH - 2, BOARD_HEIGHT - 1, 1, 1, stix));
        moveFuse(-1, 0);
        int oldx = fuse.getX();
        moveFuse(1, 0);
        Assert.assertEquals(oldx, fuse.getX());
    }

    @Test
    public void testNotMove() {
        int oldx = fuse.getX();
        fuse.notMoving();
        moveFuse(-1, 0);
        Assert.assertEquals(oldx, fuse.getX());
    }

    @Test
    public void testMoveR() {
        int oldx = fuse.getX();
        moveFuse(1, 0);
        Assert.assertEquals(oldx + 1, fuse.getX());
    }

    @Test
    public void testmoveD() {
        int oldy = fuse.getY();
        fuse.moving();
        moveFuse(0, 1);
        Assert.assertEquals(oldy + 1, fuse.getY());
    }

    @Test
    public void testmoveU() {
        int oldy = fuse.getY();
        fuse.moving();
        moveFuse(0, -1);
        Assert.assertEquals(oldy - 1, fuse.getY());
    }

    @Test
    public void testmoveL() {
        int oldx = fuse.getX();
        fuse.moving();
        moveFuse(-1, 0);
        Assert.assertEquals(oldx - 1, fuse.getX());
    }

    @Test
    public void testNotMoveL() {
        fuse.setLastX(fuse.getX() - 1);
        int oldx = fuse.getX();
        fuse.moving();
        moveFuse(-1, 0);
        Assert.assertEquals(oldx, fuse.getX());
    }

    @Test
    public void testNotMoveL2() {
        createFuse(new Fuse(0, 0, 1, 1, stix));
        int oldx = fuse.getX();
        fuse.moving();
        moveFuse(-1, 0);
        Assert.assertEquals(oldx, fuse.getX());
    }

    @Test
    public void testNotMoveD() {
        fuse.setLastY(fuse.getY() + 1);
        int oldy = fuse.getY();
        fuse.moving();
        moveFuse(0, 1);
        Assert.assertEquals(oldy, fuse.getY());
    }

    @Test
    public void testNotMoveU() {
        fuse.setLastY(fuse.getY() - 1);
        int oldy = fuse.getY();
        fuse.moving();
        moveFuse(0, -1);
        Assert.assertEquals(oldy, fuse.getY());
    }

    @org.junit.Test
    public void testToString() {
        Assert.assertEquals(fuse.toString(), "Fuse");
    }

    @Test
    public void onPointTest1() {
        fuse.setLastX(0);
        fuse.setLastY(0);

        Point p = new Point(0, 0);

        Assert.assertTrue(fuse.onPoint(p));
    }

    @Test
    public void onPointTest2() {
        fuse.setLastX(0);
        fuse.setLastY(0);

        Point p = new Point(1, 0);

        Assert.assertFalse(fuse.onPoint(p));
    }

    @Test
    public void onPointTest3() {
        fuse.setLastX(0);
        fuse.setLastY(0);

        Point p = new Point(0, 1);

        Assert.assertFalse(fuse.onPoint(p));
    }

    @Test
    public void onPointTest4() {
        fuse.setLastX(0);
        fuse.setLastY(0);

        Point p = new Point(1, 1);

        Assert.assertFalse(fuse.onPoint(p));
    }
}
