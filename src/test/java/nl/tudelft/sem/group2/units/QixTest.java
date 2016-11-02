package nl.tudelft.sem.group2.units;

import javafx.embed.swing.JFXPanel;
import javafx.scene.canvas.Canvas;
import nl.tudelft.sem.group2.AreaState;
import nl.tudelft.sem.group2.AreaTracker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.LinkedList;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by gijs on 26-9-2016.
 */
public class QixTest {
    private Qix qix;
    private Canvas canvas = new Canvas(1, 1);
    private Qix spyQix;
    private AreaTracker areaTracker = mock(AreaTracker.class);
    private AreaState[][] boardGrid = new AreaState[1][1];

    @BeforeClass
    public static void BeforeClass() {
        new JFXPanel();
    }

    @Before
    public void setUp() throws Exception {
        qix = new Qix(areaTracker, 5);
        when(areaTracker.getBoardGrid()).thenReturn(boardGrid);
        qix.setAreaTracker(areaTracker);
        spyQix = spy(qix);
    }

    @Test
    public void draw2() throws Exception {
        for (int i = 0; i < 2; i++) {
            double[] colors = new double[3];
            for (int j = 0; j < colors.length; j++) {
                colors[j] = Math.random();
            }
            spyQix.getColorArray().addFirst(colors);
        }
        LinkedList<float[]> linkedList2 = new LinkedList<>();
        linkedList2.add(new float[] {1, 1});
        linkedList2.add(new float[] {1, 1});
        spyQix.setOldCoordinates(linkedList2);
        spyQix.setOldDirections(linkedList2);
        spyQix.draw(new Canvas(1, 1).getGraphicsContext2D());
        verify(spyQix, times(4 * spyQix.getOldDirections().size())).getOldCoordinate(anyInt());
        verify(spyQix, times(4 * spyQix.getOldDirections().size())).getOldDirection(anyInt());
    }

    @Test
    public void changeDirection() throws Exception {
        spyQix.draw(new Canvas(1, 1).getGraphicsContext2D());
        spyQix.setAnimationLoops(0);
        spyQix.move();
        verify(spyQix, atLeast(2)).setDirection(any(float.class), anyInt());
        verify(spyQix, atLeast(2)).getDirection(0);
        verify(spyQix, atLeast(2)).getDirection(1);
    }

    @Test
    public void changeDirectionLength0() throws Exception {
        //run while loop 2 times
        when(spyQix.getDirection(anyInt()))
                .thenReturn((float) 0)
                .thenReturn((float) 0)
                .thenReturn((float) 0)
                .thenReturn((float) 0)
                .thenReturn((float) 1);
        spyQix.move();
        verify(spyQix, times(4)).setDirection(any(float.class), anyInt());
    }

    @Test
    public void checkLineCollisionI() throws Exception {
        spyQix.setAnimationLoops(1);
        spyQix.getAreaTracker().getBoardGrid()[0][0] = AreaState.INNERBORDER;
        spyQix.move();
        verify(spyQix, times(2)).getCoordinate(anyInt());
    }

    @Test
    public void checkLineCollisionO() throws Exception {
        spyQix.getAreaTracker().getBoardGrid()[0][0] = AreaState.OUTERBORDER;
        spyQix.move();
        verify(spyQix, times(2)).getCoordinate(anyInt());
    }

    @Test
    public void checkLineCollision2() throws Exception {
        spyQix.getAreaTracker().getBoardGrid()[0][0] = AreaState.INNERBORDER;
        when(spyQix.getCoordinate(anyInt())).thenReturn((float) 10);
        spyQix.move();
        verify(spyQix, times(2)).setDirection(anyFloat(), anyInt());
    }

    @Test
    public void checkLineCollision3() throws Exception {
        spyQix.getAreaTracker().getBoardGrid()[0][0] = AreaState.INNERBORDER;
        when(spyQix.getCoordinate(anyInt())).thenReturn((float) 0);
        spyQix.move();
        verify(spyQix, times(4)).setDirection(anyFloat(), anyInt());
    }

    @Test
    public void moveOldDirectionSize() throws Exception {
        LinkedList<float[]> linkedList = new LinkedList<>();
        for (int i = 0; i < Qix.getLINESCOUNT() + 1; i++) {
            linkedList.add(new float[0]);
        }
        LinkedList<float[]> spyLinkedlist = spy(linkedList);
        spyQix.setOldDirections(spyLinkedlist);
        spyQix.move();
        verify(spyLinkedlist, times(1)).removeLast();
    }

    @Test
    public void toPolygon() throws Exception {
        when(spyQix.getOldCoordinates()).thenReturn(new LinkedList<float[]>());
        Assert.assertEquals(0, spyQix.toPolygon().npoints);
    }

    @Test
    public void toPolygon2() throws Exception {
        LinkedList<float[]> linkedlist = new LinkedList<float[]>();
        linkedlist.add(new float[] {1, 1});
        spyQix.setOldDirections(linkedlist);
        spyQix.setOldCoordinates(linkedlist);
        spyQix.toPolygon();
        verify(spyQix, times(6)).getOldCoordinates();
    }
}