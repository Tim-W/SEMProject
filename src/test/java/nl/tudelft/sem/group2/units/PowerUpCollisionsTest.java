package nl.tudelft.sem.group2.units;

import javafx.embed.swing.JFXPanel;
import nl.tudelft.sem.group2.collisions.CollisionHandler;
import nl.tudelft.sem.group2.powerups.PowerEat;
import nl.tudelft.sem.group2.powerups.PowerLife;
import nl.tudelft.sem.group2.powerups.PowerSpeed;
import nl.tudelft.sem.group2.powerups.Powerup;
import nl.tudelft.sem.group2.powerups.PowerupEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.EventQueue;
import java.util.HashSet;

import static nl.tudelft.sem.group2.powerups.PowerUpType.EAT;
import static nl.tudelft.sem.group2.powerups.PowerUpType.LIFE;
import static nl.tudelft.sem.group2.powerups.PowerUpType.SPEED;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Class that test the powerUpCollisions method of CollisionsHandler.
 */
@Ignore
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
        //gameController = GameController.getInstance();
        //gameController.addCursor(cursor);
    }

    /**
     * Tests an empty set.
     */
    @Test
    public void testEmpty() {
        set.clear();
        Assert.assertEquals(null, handler.powerUpCollisions(set));
    }

    /**
     * Tests a null set.
     */
    @Test
    public void testNull() {
        Assert.assertEquals(null, handler.powerUpCollisions(null));
    }

    /**
     * Tests an eat powerup collision.
     */
    @Test
    public void testEat() {
        EventQueue.invokeLater(() -> {
            PowerEat powerup = mock(PowerEat.class);
            set.add(powerup);
            when(cursor.intersect(powerup)).thenReturn(true);
            PowerupEvent event = new PowerupEvent(cursor, EAT);
            Assert.assertEquals(event, handler.powerUpCollisions(set));
        });
    }

    /**
     * Tests a speed powerup collision.
     */
    @Test
    public void testSpeed() {
        EventQueue.invokeLater(() -> {
            PowerSpeed powerup = mock(PowerSpeed.class);
            set.add(powerup);
            when(cursor.intersect(powerup)).thenReturn(true);
            PowerupEvent event = new PowerupEvent(cursor, SPEED);
            Assert.assertEquals(event, handler.powerUpCollisions(set));
        });
    }

    /**
     * Tests a life powerup collision.
     */
    @Test
    public void testLife() {
        EventQueue.invokeLater(() -> {
            PowerLife powerup = mock(PowerLife.class);
            set.add(powerup);
            when(cursor.intersect(powerup)).thenReturn(true);
            PowerupEvent event = new PowerupEvent(cursor, LIFE);
            Assert.assertEquals(event, handler.powerUpCollisions(set));
        });
    }

    /**
     * Tests an eat powerup without collision.
     */
    @Test
    public void testEatNoIntersection() {
        EventQueue.invokeLater(() -> {
            PowerEat powerup = mock(PowerEat.class);
            set.add(powerup);
            when(cursor.intersect(powerup)).thenReturn(false);
            Assert.assertEquals(null, handler.powerUpCollisions(set));
        });
    }


    /**
     * Tests a speed powerup without collision.
     */
    @Test
    public void testSpeedNoIntersection() {
        EventQueue.invokeLater(() -> {
            PowerSpeed powerup = mock(PowerSpeed.class);
            set.add(powerup);
            when(cursor.intersect(powerup)).thenReturn(false);
            Assert.assertEquals(null, handler.powerUpCollisions(set));
        });
    }

    /**
     * Tests a life powerup without collision.
     */
    @Test
    public void testLifeNoIntersection() {
        EventQueue.invokeLater(() -> {
            PowerLife powerup = mock(PowerLife.class);
            set.add(powerup);
            when(cursor.intersect(powerup)).thenReturn(false);
            Assert.assertEquals(null, handler.powerUpCollisions(set));
        });
    }


    /**
     * Tests a set without cursor or powerups.
     */
    @Test
    public void testNoPowerupsNoCursor() {
        set.clear();
        Sparx sparx = mock(Sparx.class);
        set.add(sparx);
        Assert.assertEquals(null, handler.powerUpCollisions(set));
    }


    /**
     * Tests a powerup without subclass.
     */
    @Test
    public void testNoInstanceOf() {
        EventQueue.invokeLater(() -> {
            Powerup powerup = mock(Powerup.class);
            set.add(powerup);
            Assert.assertEquals(null, handler.powerUpCollisions(set));
        });
    }
}
