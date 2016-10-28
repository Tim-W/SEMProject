package nl.tudelft.sem.group2;

import javafx.embed.swing.JFXPanel;
import nl.tudelft.sem.group2.collisions.CollisionHandler;
import nl.tudelft.sem.group2.powerups.PowerUpType;
import nl.tudelft.sem.group2.powerups.Powerup;
import nl.tudelft.sem.group2.powerups.PowerupHandler;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Qix;
import nl.tudelft.sem.group2.units.Sparx;
import nl.tudelft.sem.group2.units.SparxDirection;
import nl.tudelft.sem.group2.units.Stix;
import nl.tudelft.sem.group2.units.Unit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for collisionHandler
 * Created by Dennis on 14-10-16.
 */
public class CollisionHandlerTest {

    private CollisionHandler handler;
    private HashSet<Unit> set;
    private Stix stix;
    private AreaTracker areaTracker;
    private AreaState[][] boardGrid = new AreaState[LaunchApp.getGridWidth() + 1][LaunchApp.getGridHeight() + 1];
    private Cursor cursor;
    private PowerupHandler powerupHandler;

    /**
     * Sets up the mocks and variables.
     */
    @Before
    public void setUp() {
        new JFXPanel();
        stix = new Stix();
        handler = new CollisionHandler();
        areaTracker = mock(AreaTracker.class);
        when(areaTracker.getBoardGrid()).thenReturn(boardGrid);
        set = new HashSet<>();
        cursor = mock(Cursor.class);
        powerupHandler = mock(PowerupHandler.class);
        when(powerupHandler.getCurrentPowerup()).thenReturn(PowerUpType.NONE);
        when(cursor.getPowerupHandler()).thenReturn(powerupHandler);
        set.add(cursor);
    }

    /**
     * tests an empty set.
     */
    @Test
    public void emptyTest() {
        set.clear();
        Assert.assertFalse(handler.collisions(set, stix));
    }

    /**
     * tests a null set.
     */
    @Test
    public void nullTest() {
        Assert.assertFalse(handler.collisions(null, stix));
    }

    /**
     * tests the collision between sparx and cursor.
     */
    @Test
    public void cursorSparxTest() {
        //Cursor cursor = new Cursor(0, 0, 1, 1, stix, areaTracker);
        Sparx sparx = new Sparx(0, 0, 1, 1, areaTracker, SparxDirection.LEFT);
        //set.add(cursor);
        set.add(sparx);
        when(cursor.intersect(sparx)).thenReturn(true);
        when(powerupHandler.getCurrentPowerup()).thenReturn(PowerUpType.NONE);
        Assert.assertTrue(handler.collisions(set, stix));
    }

    /**
     * tests the collision between qix and stix.
     */
    @Test
    public void qixStixTest() {
        Qix qix = mock(Qix.class);
        stix = mock(Stix.class);
        when(stix.intersect(qix)).thenReturn(true);
        set.add(qix);
        Assert.assertTrue(handler.collisions(set, stix));
    }

    /**
     * tests the collision between qix and cursor.
     */
    @Test
    public void qixCursorTest() {
        Qix qix = mock(Qix.class);
        set.add(qix);
        stix = mock(Stix.class);
        when(stix.intersect(qix)).thenReturn(false);
        when(qix.intersect(cursor)).thenReturn(true);
        when(cursor.uncoveredOn(anyInt(), anyInt())).thenReturn(true);
        Assert.assertTrue(handler.collisions(set, stix));
    }

    /**
     * Test for no collisions with just a cursor.
     */
    @Test
    public void noCollisionsTest() {
        Assert.assertFalse(handler.collisions(set, stix));
    }

    /**
     * Tests for no collisions between units.
     */
    @Test
    public void noCollisionsTest2() {
        Sparx sparx = mock(Sparx.class);
        set.add(sparx);
        when(cursor.intersect(sparx)).thenReturn(false);
        Assert.assertFalse(handler.collisions(set, stix));
    }

    /**
     * Tests a collision between cursor with eat powerup and sparx.
     */
    @Test
    public void eatPowerupTest() {
        Sparx sparx = mock(Sparx.class);
        set.add(sparx);
        when(cursor.intersect(sparx)).thenReturn(false);
        when(powerupHandler.getCurrentPowerup()).thenReturn(PowerUpType.EAT);
        Assert.assertFalse(handler.collisions(set, stix));
    }

    /**
     * Tests a cursor with powerup without collision.
     */
    @Test
    public void testNoCollisionPowerup() {
        Powerup powerup = mock(Powerup.class);
        set.add(powerup);
        when(cursor.intersect(powerup)).thenReturn(false);
        Assert.assertFalse(handler.collisions(set, stix));
    }
}

