package nl.tudelft.sem.group2.scenes;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.JavaFXThreadingRule;
import nl.tudelft.sem.group2.board.AreaState;
import nl.tudelft.sem.group2.board.BoardGrid;
import nl.tudelft.sem.group2.gameController.GameController;
import nl.tudelft.sem.group2.level.Level;
import nl.tudelft.sem.group2.level.LevelHandler;
import nl.tudelft.sem.group2.powerups.CursorPowerupHandler;
import nl.tudelft.sem.group2.powerups.PowerUpType;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.FuseHandler;
import nl.tudelft.sem.group2.units.Stix;
import nl.tudelft.sem.group2.units.Unit;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Tests the GameScene.
 */
public class GameSceneTest {
    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
    private GameScene gameScene;
    private Set<Unit> units;
    private Cursor cursor;
    private Stix stix;
    private AreaState[][] areaStates;
    private FuseHandler fuseHandler;
    private BoardGrid grid;

    @Before
    public void setUp() {
        units = new HashSet<>();
        gameScene = new GameScene(new Group(), Color.BLACK, null);
        cursor = mock(Cursor.class);
        stix = mock(Stix.class);
        grid = mock(BoardGrid.class);
        LevelHandler levelHandler = mock(LevelHandler.class);
        GameController.getInstance().setLevelHandler(levelHandler);
        when(levelHandler.getLevel()).thenReturn(mock(Level.class));
        when(levelHandler.getLevel().getBoardGrid()).thenReturn(grid);
        when(cursor.getStix()).thenReturn(stix);
        fuseHandler = mock(FuseHandler.class);
        when(cursor.getFuseHandler()).thenReturn(fuseHandler);
        CursorPowerupHandler cursorPowerupHandler = mock(CursorPowerupHandler.class);
        when(cursorPowerupHandler.getCurrentPowerup()).thenReturn(PowerUpType.EAT);
        when(cursor.getCursorPowerupHandler()).thenReturn(cursorPowerupHandler);
        units.add(cursor);
        areaStates = new AreaState[1][1];
    }

    /**
     * @throws Exception
     */
    @Test
    public void testDrawCursor() throws Exception {
        gameScene.draw(units);
        verify(cursor, times(1)).draw(any());

    }
/*
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
    }*/

}