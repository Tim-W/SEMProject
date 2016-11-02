package nl.tudelft.sem.group2.scenes;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.AreaState;
import nl.tudelft.sem.group2.powerups.PowerUpType;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Fuse;
import nl.tudelft.sem.group2.units.Stix;
import nl.tudelft.sem.group2.units.Unit;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.Point;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Created by gijs on 30-9-2016.
 */
public class gameSceneTest {
    //@Rule
    //public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
    private GameScene gameScene;
    private Set<Unit> units;
    private Cursor cursor;
    private Stix stix;
    private AreaState[][] areaStates;

    @BeforeClass
    public static void BeforeClass() {
        new JFXPanel();
    }

    @Before
    public void setUp() {
        units = new HashSet<>();
        gameScene = new GameScene(new Group(), Color.BLACK, null);
        cursor = mock(Cursor.class);
        stix = mock(Stix.class);
        when(cursor.getStix()).thenReturn(stix);
        when(cursor.getCurrentPowerup()).thenReturn(PowerUpType.EAT);
        units.add(cursor);
        areaStates = new AreaState[1][1];
    }

    @Test
    public void testDrawStixAndFuseVerifyNoFuse() throws Exception {
        when(cursor.getFuse()).thenReturn(null);
        when(stix.pointEqualsFirstPoint(any())).thenReturn(false);
        LinkedList<Point> points = new LinkedList<Point>();
        points.add(new Point(1, 1));
        when(stix.getStixCoordinates()).thenReturn(points);
        gameScene.draw(units, areaStates);
        verify(cursor, times(1)).isFast();

    }

    @Test
    public void testDrawStixAndFuseVerifyFuse() throws Exception {
        when(cursor.getFuse()).thenReturn(mock(Fuse.class));
        when(stix.pointEqualsFirstPoint(any())).thenReturn(false);
        LinkedList<Point> points = new LinkedList<>();
        Point point = mock(Point.class);
        points.add(point);
        when(stix.getStixCoordinates()).thenReturn(points);
        gameScene.draw(units, areaStates);
        verify(cursor, times(0)).isFast();
    }

}