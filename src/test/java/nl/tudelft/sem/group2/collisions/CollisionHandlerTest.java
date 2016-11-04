package nl.tudelft.sem.group2.collisions;

import javafx.embed.swing.JFXPanel;
import nl.tudelft.sem.group2.powerups.CursorPowerupHandler;
import nl.tudelft.sem.group2.powerups.PowerUpType;
import nl.tudelft.sem.group2.powerups.Powerup;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Qix;
import nl.tudelft.sem.group2.units.Sparx;
import nl.tudelft.sem.group2.units.SparxDirection;
import nl.tudelft.sem.group2.units.Unit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashSet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for collisionHandler.
 * Created by Dennis on 14-10-16.
 */
public class CollisionHandlerTest {

    private CollisionHandler handler;
    private HashSet<Unit> set;
    private Cursor cursor;
    private CursorPowerupHandler cursorPowerupHandler;

    /**
     * Sets up the mocks and variables.
     */
    @Before
    public void setUp() {
        new JFXPanel();
        handler = new CollisionHandler();
        set = new HashSet<>();
        cursor = mock(Cursor.class);
        cursorPowerupHandler = mock(CursorPowerupHandler.class);
        when(cursorPowerupHandler.getCurrentPowerup()).thenReturn(PowerUpType.NONE);
        when(cursor.getCursorPowerupHandler()).thenReturn(cursorPowerupHandler);
        set.add(cursor);
    }

    /**
     * Tests an empty set.
     */
    @Test
    public void emptyTest() {
        set.clear();
        Assert.assertFalse(handler.collisions(set, cursor));
    }

    /**
     * Tests a null set.
     */
    @Test
    public void nullTest() {
        Assert.assertFalse(handler.collisions(null, cursor));
    }

    /**
     * Tests the collision between sparx and cursor.
     */
    @Test
    public void cursorSparxTest() {
        Sparx sparx = new Sparx(0, 0, 1, 1, SparxDirection.LEFT);
        set.add(sparx);
        when(cursor.intersect(sparx)).thenReturn(true);
        when(cursorPowerupHandler.getCurrentPowerup()).thenReturn(PowerUpType.NONE);
        Assert.assertTrue(handler.collisions(set, cursor));
    }

    /**
     * Tests the collision between qix and stix.
     */
    //TODO fix AssertionError
    @Ignore
    public void qixStixTest() {
        Qix qix = mock(Qix.class);
        set.add(qix);
        Assert.assertTrue(handler.collisions(set, cursor));
    }

    /**
     * Tests the collision between qix and cursor.
     */
    @Test
    public void qixCursorTest() {
        Qix qix = mock(Qix.class);
        set.add(qix);
        when(qix.intersect(cursor)).thenReturn(true);
        when(cursor.uncoveredOn()).thenReturn(true);
        Assert.assertTrue(handler.collisions(set, cursor));
    }

    /**
     * Test for no collisions with just a cursor.
     */
    @Test
    public void noCollisionsTest() {
        Assert.assertFalse(handler.collisions(set, cursor));
    }

    /**
     * Tests for no collisions between units.
     */
    @Test
    public void noCollisionsTest2() {
        Sparx sparx = mock(Sparx.class);
        set.add(sparx);
        when(cursor.intersect(sparx)).thenReturn(false);
        Assert.assertFalse(handler.collisions(set, cursor));
    }

    /**
     * Tests a collision between cursor with eat powerup and sparx.
     */
    @Test
    public void eatPowerupTest() {
        Sparx sparx = mock(Sparx.class);
        set.add(sparx);
        when(cursor.intersect(sparx)).thenReturn(false);
        when(cursorPowerupHandler.getCurrentPowerup()).thenReturn(PowerUpType.EAT);
        Assert.assertFalse(handler.collisions(set, cursor));
    }

    /**
     * Tests a cursor with powerup without collision.
     */
    @Test
    public void testNoCollisionPowerup() {
        Powerup powerup = mock(Powerup.class);
        set.add(powerup);
        when(cursor.intersect(powerup)).thenReturn(false);
        Assert.assertFalse(handler.collisions(set, cursor));
    }
}

