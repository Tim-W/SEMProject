package nl.tudelft.sem.group2.collisions;

import nl.tudelft.sem.group2.board.AreaTracker;
import nl.tudelft.sem.group2.JavaFXThreadingRule;
import nl.tudelft.sem.group2.collisions.CollisionHandler;
import nl.tudelft.sem.group2.powerups.PowerEat;
import nl.tudelft.sem.group2.powerups.PowerLife;
import nl.tudelft.sem.group2.powerups.PowerSpeed;
import nl.tudelft.sem.group2.powerups.PowerupEvent;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Sparx;
import nl.tudelft.sem.group2.units.SparxDirection;
import nl.tudelft.sem.group2.units.Unit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static nl.tudelft.sem.group2.powerups.PowerUpType.EAT;
import static nl.tudelft.sem.group2.powerups.PowerUpType.LIFE;
import static nl.tudelft.sem.group2.powerups.PowerUpType.SPEED;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Class that test the powerUpCollisions method of CollisionsHandler.
 */
public class PowerUpCollisionsTest {

    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
    private CollisionHandler handler;
    private HashSet<Unit> set;
    private Cursor cursor;
    private AreaTracker areaTracker;
    private ArrayList<Cursor> cursors;

    /**
     * Sets up the mocks and variables.
     */
    @Before
    public void setUp() {
        handler = new CollisionHandler();
        set = new HashSet<>();
        cursors = new ArrayList<>();
        cursor = mock(Cursor.class);
        set.add(cursor);
        cursors.add(cursor);
    }

    /**
     * Tests an empty set.
     */
    @Test
    public void testEmpty() {
        set.clear();
        Assert.assertEquals(null, handler.powerUpCollisions(set, cursors));
    }

    /**
     * Tests a null set.
     */
    @Test
    public void testNull() {
        Assert.assertEquals(null, handler.powerUpCollisions(null, cursors));
    }

    /**
     * Tests an eat powerup collision.
     */
    @Test
    public void testEat() {
        when(cursor.intersect(any())).thenReturn(true);
        PowerEat powerup = new PowerEat(cursor.getIntX(), cursor.getIntY(), 1, 1);
        set.add(powerup);
        PowerupEvent event = new PowerupEvent(cursor, EAT);
        Assert.assertEquals(event.getClass(), handler.powerUpCollisions(set, cursors).getClass());
    }

    /**
     * Tests a speed powerup collision.
     */
    @Test
    public void testSpeed() {
        when(cursor.intersect(any())).thenReturn(true);
        PowerSpeed powerup = new PowerSpeed(cursor.getIntX(), cursor.getIntY(), 1, 1);
        set.add(powerup);
        PowerupEvent event = new PowerupEvent(cursor, SPEED);
        Assert.assertEquals(event.getClass(), handler.powerUpCollisions(set, cursors).getClass());
    }

    /**
     * Tests a life powerup collision.
     */
    @Test
    public void testLife() {
        when(cursor.intersect(any())).thenReturn(true);
        PowerLife powerup = new PowerLife(cursor.getIntX(), cursor.getIntY(), 1, 1);
        set.add(powerup);
        PowerupEvent event = new PowerupEvent(cursor, LIFE);
        Assert.assertEquals(event.getClass(), handler.powerUpCollisions(set, cursors).getClass());
    }

    /**
     * Tests an eat powerup without collision.
     */
    @Test
    public void testEatNoIntersection() {
        PowerEat powerup = new PowerEat(cursor.getIntX() + 20, cursor.getIntY() + 20, 1, 1);
        set.add(powerup);
        Assert.assertEquals(null, handler.powerUpCollisions(set, cursors));
    }


    /**
     * Tests a speed powerup without collision.
     */
    @Test
    public void testSpeedNoIntersection() {
        PowerSpeed powerup = new PowerSpeed(cursor.getIntX() + 20, cursor.getIntY() + 20, 1, 1);
        set.add(powerup);
        Assert.assertEquals(null, handler.powerUpCollisions(set, cursors));
    }

    /**
     * Tests a life powerup without collision.
     */
    @Test
    public void testLifeNoIntersection() {
        PowerLife powerup = new PowerLife(cursor.getIntX() + 20, cursor.getIntY() + 20, 1, 1);
        set.add(powerup);
        Assert.assertEquals(null, handler.powerUpCollisions(set, cursors));
    }


    /**
     * Tests a set without cursor or powerups.
     */
    @Test
    public void testNoPowerupsNoCursor() {
        set.clear();
        Sparx sparx = new Sparx(cursor.getIntX(), cursor.getIntY(), 1, 1 , SparxDirection.RIGHT);
        set.add(sparx);
        Assert.assertEquals(null, handler.powerUpCollisions(set, cursors));
    }


    /**
     * Tests a powerup without subclass.
     */
    @Test
    public void testNoInstanceOf() {
        PowerLife powerup = new PowerLife(cursor.getIntX() + 20, cursor.getIntY() + 20, 1, 1);
        set.add(powerup);
        Assert.assertEquals(null, handler.powerUpCollisions(set, cursors));
    }
}
