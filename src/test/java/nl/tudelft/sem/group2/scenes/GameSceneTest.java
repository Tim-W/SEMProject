package nl.tudelft.sem.group2.scenes;

import java.awt.Point;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.board.AreaState;
import nl.tudelft.sem.group2.powerups.PowerUpType;
import nl.tudelft.sem.group2.powerups.PowerupHandler;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Fuse;
import nl.tudelft.sem.group2.units.FuseHandler;
import nl.tudelft.sem.group2.units.Stix;
import nl.tudelft.sem.group2.units.Unit;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Tests the GameScene.
 */
@Ignore
public class GameSceneTest {
    //@Rule
    //public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
    private GameScene gameScene;
    private Set<Unit> units;
    private Cursor cursor;
    private Stix stix;
    private AreaState[][] areaStates;
    private FuseHandler fuseHandler;

    @BeforeClass
    public static void beforeClass() {
        new JFXPanel();
    }

    @Before
    public void setUp() {
        units = new HashSet<>();
        gameScene = new GameScene(new Group(), Color.BLACK, null);
        cursor = mock(Cursor.class);
        stix = mock(Stix.class);
        when(cursor.getStix()).thenReturn(stix);
        fuseHandler = mock(FuseHandler.class);
        when(cursor.getFuseHandler()).thenReturn(fuseHandler);
        PowerupHandler powerupHandler = mock(PowerupHandler.class);
        when(powerupHandler.getCurrentPowerup()).thenReturn(PowerUpType.EAT);
        when(cursor.getPowerupHandler()).thenReturn(powerupHandler);
        units.add(cursor);
        areaStates = new AreaState[1][1];
    }

    @Test
    public void testDrawStixAndFuseVerifyNoFuse() throws Exception {
        when(fuseHandler.getFuse()).thenReturn(null);
        when(stix.pointEqualsFirstPoint(any())).thenReturn(false);
        LinkedList<Point> points = new LinkedList<>();
        points.add(new Point(1, 1));
        when(stix.getStixCoordinates()).thenReturn(points);
        gameScene.draw(units);
        verify(cursor, times(1)).isFast();

    }

    @Test
    public void testDrawStixAndFuseVerifyFuse() throws Exception {
        when(fuseHandler.getFuse()).thenReturn(mock(Fuse.class));
        when(stix.pointEqualsFirstPoint(any())).thenReturn(false);
        LinkedList<Point> points = new LinkedList<>();
        Point point = mock(Point.class);
        points.add(point);
        when(stix.getStixCoordinates()).thenReturn(points);
        gameScene.draw(units);
        verify(cursor, times(0)).isFast();
    }

}