package nl.tudelft.sem.group2.units;

import javafx.embed.swing.JFXPanel;
import nl.tudelft.sem.group2.collisions.CollisionHandler;
import nl.tudelft.sem.group2.powerups.PowerEat;
import nl.tudelft.sem.group2.powerups.PowerLife;
import nl.tudelft.sem.group2.powerups.PowerSpeed;
import nl.tudelft.sem.group2.powerups.Powerup;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static nl.tudelft.sem.group2.powerups.PowerUpType.EAT;
import static nl.tudelft.sem.group2.powerups.PowerUpType.LIFE;
import static nl.tudelft.sem.group2.powerups.PowerUpType.NONE;
import static nl.tudelft.sem.group2.powerups.PowerUpType.SPEED;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Class that test the powerUpCollisions method of CollisionsHandler
 */
public class PowerUpCollisionsTest {

    private CollisionHandler handler;
    private HashSet<Unit> set;
    private Cursor cursor;

    /**
     * Sets up the mocks and variables.
     */
    @Before
    public void setUp() {
        new JFXPanel();
        handler = new CollisionHandler();
        set = new HashSet<>();
        cursor = mock(Cursor.class);
        set.add(cursor);
    }

    @Test
    public void testEmpty() {
        set.clear();
        Assert.assertEquals(NONE, handler.powerUpCollisions(set));
    }

    @Test
    public void testNull() {
        set = null;
        Assert.assertEquals(NONE, handler.powerUpCollisions(set));
    }

    @Test
    public void testEat() {
        PowerEat powerup = mock(PowerEat.class);
        set.add(powerup);
        when(cursor.intersect(powerup)).thenReturn(true);
        Assert.assertEquals(EAT, handler.powerUpCollisions(set));
    }

    @Test
    public void testSpeed() {
        PowerSpeed powerup = mock(PowerSpeed.class);
        set.add(powerup);
        when(cursor.intersect(powerup)).thenReturn(true);
        Assert.assertEquals(SPEED, handler.powerUpCollisions(set));
    }

    @Test
    public void testLife() {
        PowerLife powerup = mock(PowerLife.class);
        set.add(powerup);
        when(cursor.intersect(powerup)).thenReturn(true);
        Assert.assertEquals(LIFE, handler.powerUpCollisions(set));
    }

    @Test
    public void testEatNoIntersection() {
        PowerEat powerup = mock(PowerEat.class);
        set.add(powerup);
        when(cursor.intersect(powerup)).thenReturn(false);
        Assert.assertEquals(NONE, handler.powerUpCollisions(set));
    }

    @Test
    public void testSpeedNoIntersection() {
        PowerSpeed powerup = mock(PowerSpeed.class);
        set.add(powerup);
        when(cursor.intersect(powerup)).thenReturn(false);
        Assert.assertEquals(NONE, handler.powerUpCollisions(set));
    }

    @Test
    public void testLifeNoIntersection() {
        PowerLife powerup = mock(PowerLife.class);
        set.add(powerup);
        when(cursor.intersect(powerup)).thenReturn(false);
        Assert.assertEquals(NONE, handler.powerUpCollisions(set));
    }

    @Test
    public void testNoPowerupsNoCursor() {
        set.clear();
        Sparx sparx = mock(Sparx.class);
        set.add(sparx);
        Assert.assertEquals(NONE, handler.powerUpCollisions(set));
    }

    @Test
    public void testNoInstanceOf() {
        Powerup powerup = mock(Powerup.class);
        set.add(powerup);
        Assert.assertEquals(NONE, handler.powerUpCollisions(set));
    }

    @Test
    public void test() {

    }
}
