package nl.tudelft.sem.group2.powerups;

import javafx.embed.swing.JFXPanel;
import nl.tudelft.sem.group2.board.BoardGrid;
import nl.tudelft.sem.group2.collisions.CollisionHandler;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Unit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by gijs on 3-11-2016.
 */
public class PowerupHandlerTest {
    private PowerupHandler powerupHandler;
    private Powerup powerup;
    private CollisionHandler collisionHandler;
    private Set<Unit> units;
    private CursorPowerupHandler cursorPowerupHandler;
    private ArrayList<Cursor> cursors;
    private BoardGrid grid;

    @BeforeClass
    public static void beforeClass() {
        new JFXPanel();
    }

    @Before
    public void setUp() throws Exception {
        powerupHandler = spy(new PowerupHandler());
        powerup = mock(Powerup.class);
        collisionHandler = mock(CollisionHandler.class);
        units = new HashSet<>();
        cursorPowerupHandler = mock(CursorPowerupHandler.class);
        cursors = new ArrayList<>();
    }

    /**
     * @throws Exception
     */
    @Test
    public void applyPowerups() throws Exception {
        units.add(powerup);
        Cursor cursor = mock(Cursor.class);
        cursors.add(cursor);
        CursorPowerupHandler cursorPowerupHandler = mock(CursorPowerupHandler.class);
        when(cursor.getCursorPowerupHandler()).thenReturn(cursorPowerupHandler);
        when(powerup.getDuration()).thenReturn(0);
        powerupHandler.handlePowerups(units, collisionHandler, cursors);
        verify(cursorPowerupHandler, times(1)).updatePowerUp();
    }

    /**
     * delete if duration is 0.
     *
     * @throws Exception
     */
    @Test
    public void handlePowerupsDuration0() throws Exception {
        units.add(powerup);
        int size = units.size();
        when(powerup.getDuration()).thenReturn(0);
        powerupHandler.handlePowerups(units, collisionHandler, cursors);
        Assert.assertEquals(size - 1, units.size());
    }

    /**
     * delete not if duration is 1.
     *
     * @throws Exception
     */
    @Test
    public void handlePowerupsDuration1() throws Exception {
        units.add(powerup);
        int size = units.size();
        when(powerup.getDuration()).thenReturn(1);
        powerupHandler.handlePowerups(units, collisionHandler, cursors);
        Assert.assertEquals(size, units.size());
    }

    /**
     * delete not if duration is not powerup.
     *
     * @throws Exception
     */
    @Test
    public void handlePowerupsNotPowerup() throws Exception {
        units.add(mock(Cursor.class));
        int size = units.size();
        powerupHandler.handlePowerups(units, collisionHandler, cursors);
        Assert.assertEquals(size, units.size());
    }

    /**
     * add life to cursor if powerup is life.
     *
     * @throws Exception
     */
    @Test
    public void handlePowerupsAddLife() throws Exception {
        Cursor cursor = mock(Cursor.class);
        when(collisionHandler.powerUpCollisions(any(), any())).thenReturn(new PowerupEvent(cursor, PowerUpType.LIFE));
        powerupHandler.handlePowerups(units, collisionHandler, cursors);
        verify(cursor, times(1)).addLife();
    }

    /**
     * set powerup to eat.
     *
     * @throws Exception
     */
    @Test
    public void handlePowerupsEat() throws Exception {
        Cursor cursor = mock(Cursor.class);
        CursorPowerupHandler cursorPowerupHandler = mock(CursorPowerupHandler.class);
        when(cursor.getCursorPowerupHandler()).thenReturn(cursorPowerupHandler);
        when(collisionHandler.powerUpCollisions(any(), any())).thenReturn(new PowerupEvent(cursor, PowerUpType.EAT));
        powerupHandler.handlePowerups(units, collisionHandler, cursors);
        verify(cursorPowerupHandler, times(1)).setCurrentPowerup(PowerUpType.EAT);
    }

    /**
     * set powerup to eat.
     *
     * @throws Exception
     */
    @Test
    public void handlePowerupsSpeed() throws Exception {
        Cursor cursor = mock(Cursor.class);
        when(cursor.getCursorPowerupHandler()).thenReturn(cursorPowerupHandler);
        when(collisionHandler.powerUpCollisions(any(), any())).thenReturn(new PowerupEvent(cursor, PowerUpType.SPEED));
        powerupHandler.handlePowerups(units, collisionHandler, cursors);
        verify(cursorPowerupHandler, times(1)).setCurrentPowerup(PowerUpType.SPEED);
    }


    /**
     * check if oppositeQuadrant is called.
     *
     * @throws Exception
     */
    @Test
    public void spawnPowerupPowerUpActive() throws Exception {
        when(powerupHandler.rand()).thenReturn(0.0);
        when(cursorPowerupHandler.getCurrentPowerup()).thenReturn(PowerUpType.NONE);
        Cursor cursor = mock(Cursor.class);
        cursors.add(cursor);
        spawnPowerup(cursor);
        verify(cursor, times(1)).oppositeQuadrant();
    }

    /**
     * @throws Exception
     */
    @Test
    public void spawnPowerupNotPowerUpActiveNone() throws Exception {
        when(powerupHandler.rand()).thenReturn(1.0);
        when(cursorPowerupHandler.getCurrentPowerup()).thenReturn(PowerUpType.NONE);
        Cursor cursor = mock(Cursor.class);
        cursors.add(cursor);
        spawnPowerup(cursor);
        verify(cursor, times(0)).oppositeQuadrant();
    }

    /**
     * @throws Exception
     */
    @Test
    public void spawnPowerupNotPowerUpActiveRand() throws Exception {
        when(powerupHandler.rand()).thenReturn(1.0);
        when(cursorPowerupHandler.getCurrentPowerup()).thenReturn(PowerUpType.EAT);
        Cursor cursor = mock(Cursor.class);
        cursors.add(cursor);
        spawnPowerup(cursor);
        verify(cursor, times(0)).oppositeQuadrant();
    }

    /**
     * @throws Exception
     */
    @Test
    public void spawnPowerupNotPowerUpActivePowerupType() throws Exception {
        when(powerupHandler.rand()).thenReturn(0.0);
        when(cursorPowerupHandler.getCurrentPowerup()).thenReturn(PowerUpType.EAT);
        Cursor cursor = mock(Cursor.class);
        cursors.add(cursor);
        spawnPowerup(cursor);
        verify(cursor, times(0)).oppositeQuadrant();
    }

    /**
     * @throws Exception
     */
    @Test
    public void spawnPowerupNotPowerUpActivePowerup() throws Exception {
        when(powerupHandler.rand()).thenReturn(0.0);
        when(cursorPowerupHandler.getCurrentPowerup()).thenReturn(PowerUpType.EAT);
        Cursor cursor = mock(Cursor.class);
        units.add(mock(PowerEat.class));
        spawnPowerup(cursor);
        verify(cursor, times(0)).oppositeQuadrant();
    }

    private void spawnPowerup(Cursor cursor) {
        grid = mock(BoardGrid.class);
        int[] ints = new int[2];
        when(grid.findPowerupLocation(anyInt())).thenReturn(ints);
        when(cursor.getCursorPowerupHandler()).thenReturn(cursorPowerupHandler);
        powerupHandler.spawnPowerup(units, cursors, grid);
    }
}