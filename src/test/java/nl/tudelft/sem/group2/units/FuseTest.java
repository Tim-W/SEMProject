package nl.tudelft.sem.group2.units;

import javafx.embed.swing.JFXPanel;
import nl.tudelft.sem.group2.board.AreaTracker;
import nl.tudelft.sem.group2.board.Coordinate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

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
    private LinkedList<Coordinate> linkedList;
    private Stix stix = mock(Stix.class);
    private AreaTracker areaTracker = Mockito.mock(AreaTracker.class);

    @BeforeClass
    public static void BeforeClass() {
        new JFXPanel();
    }

    @Before
    public void setUp() throws Exception {
        createFuse(new Fuse(3, 3, 3, 4,stix));
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
        linkedList.add(new Coordinate(fuse.getIntX() + x, fuse.getIntY() + y));
        when(stix.getStixCoordinates()).thenReturn(linkedList);
        fuse.move();
    }


    @Test
    public void testMoveRightLastX() {
        moveFuse(-1, 0);
        int oldx = fuse.getIntX();
        moveFuse(1, 0);
        Assert.assertEquals(fuse.getX(), oldx);
    }

    @Test
    public void testMoveLeftLastX() {
        createFuse(new Fuse(BOARD_WIDTH - 2, BOARD_HEIGHT - 1, 1, 1, stix));
        moveFuse(-1, 0);
        int oldx = fuse.getIntX();
        moveFuse(1, 0);
        Assert.assertEquals(oldx, fuse.getX());
    }

    @Test
    public void testNotMove() {
        int oldx = fuse.getIntX();
        fuse.notMoving();
        moveFuse(-1, 0);
        Assert.assertEquals(oldx, fuse.getX());
    }

    @Test
    public void testMoveR() {
        int oldx = fuse.getIntX();
        moveFuse(1, 0);
        Assert.assertEquals(oldx + 1, fuse.getX());
    }

    @Test
    public void testmoveD() {
        int oldy = fuse.getIntY();
        fuse.moving();
        moveFuse(0, 1);
        Assert.assertEquals(oldy + 1, fuse.getY());
    }

    @Test
    public void testmoveU() {
        int oldy = fuse.getIntY();
        fuse.moving();
        moveFuse(0, -1);
        Assert.assertEquals(oldy - 1, fuse.getY());
    }

    @Test
    public void testmoveL() {
        int oldx = fuse.getIntX();
        fuse.moving();
        moveFuse(-1, 0);
        Assert.assertEquals(oldx - 1, fuse.getX());
    }

    @Test
    public void testNotMoveL() {
        fuse.setLastX(fuse.getIntX() - 1);
        int oldx = fuse.getIntX();
        fuse.moving();
        moveFuse(-1, 0);
        Assert.assertEquals(oldx, fuse.getX());
    }

    @Test
    public void testNotMoveL2() {
        createFuse(new Fuse(0, 0, 1, 1, stix));
        int oldx = fuse.getIntX();
        fuse.moving();
        moveFuse(-1, 0);
        Assert.assertEquals(oldx, fuse.getX());
    }

    @Test
    public void testNotMoveD() {
        fuse.setLastY(fuse.getIntY() + 1);
        int oldy = fuse.getIntY();
        fuse.moving();
        moveFuse(0, 1);
        Assert.assertEquals(oldy, fuse.getY());
    }

    @Test
    public void testNotMoveU() {
        fuse.setLastY(fuse.getIntY() - 1);
        int oldy = fuse.getIntY();
        fuse.moving();
        moveFuse(0, -1);
        Assert.assertEquals(oldy, fuse.getY());
    }

    @org.junit.Test
    public void testToString() {
        Assert.assertEquals(fuse.toString(), "Fuse");
    }
}