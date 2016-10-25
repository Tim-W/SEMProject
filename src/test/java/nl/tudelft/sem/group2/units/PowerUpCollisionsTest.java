package nl.tudelft.sem.group2.units;

import javafx.embed.swing.JFXPanel;
import nl.tudelft.sem.group2.JavaFXThreadingRule;
import nl.tudelft.sem.group2.collisions.CollisionHandler;
import nl.tudelft.sem.group2.controllers.GameController;
import nl.tudelft.sem.group2.powerups.PowerEat;
import nl.tudelft.sem.group2.powerups.PowerLife;
import nl.tudelft.sem.group2.powerups.PowerSpeed;
import nl.tudelft.sem.group2.powerups.PowerupEvent;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashSet;

import static nl.tudelft.sem.group2.powerups.PowerUpType.EAT;
import static nl.tudelft.sem.group2.powerups.PowerUpType.LIFE;
import static nl.tudelft.sem.group2.powerups.PowerUpType.SPEED;

/**
 * Class that test the powerUpCollisions method of CollisionsHandler.
 */
public class PowerUpCollisionsTest {

    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
    private CollisionHandler handler;
    private HashSet<Unit> set;
    private Cursor cursor;
    private GameController gameController;

    @BeforeClass
    public static void BeforeClass() {
        new JFXPanel();
    }
    /**
     * Sets up the mocks and variables.
     */
    @Before
    public void setUp() {
        handler = new CollisionHandler();
        set = new HashSet<>();
        gameController = GameController.getInstance();
        gameController.makeCursors(false);
        cursor = gameController.getCursors().get(0);
        set.add(cursor);
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
        PowerEat powerup = new PowerEat(cursor.getX(), cursor.getY(), 1, 1, gameController.getAreaTracker());
        set.add(powerup);
        PowerupEvent event = new PowerupEvent(cursor, EAT);
        Assert.assertEquals(event.getClass(), handler.powerUpCollisions(set).getClass());
    }

    /**
     * Tests a speed powerup collision.
     */
    @Test
    public void testSpeed() {
        PowerSpeed powerup = new PowerSpeed(cursor.getX(), cursor.getY(), 1, 1, gameController.getAreaTracker());
        set.add(powerup);
        PowerupEvent event = new PowerupEvent(cursor, SPEED);
        Assert.assertEquals(event.getClass(), handler.powerUpCollisions(set).getClass());
    }

    /**
     * Tests a life powerup collision.
     */
    @Test
    public void testLife() {
        PowerLife powerup = new PowerLife(cursor.getX(), cursor.getY(), 1, 1, gameController.getAreaTracker());
        set.add(powerup);
        PowerupEvent event = new PowerupEvent(cursor, LIFE);
        Assert.assertEquals(event.getClass(), handler.powerUpCollisions(set).getClass());
    }

    /**
     * Tests an eat powerup without collision.
     */
    @Test
    public void testEatNoIntersection() {
        PowerEat powerup = new PowerEat(cursor.getX() + 20, cursor.getY() + 20, 1, 1, gameController.getAreaTracker());
        set.add(powerup);
        Assert.assertEquals(null, handler.powerUpCollisions(set));
    }


    /**
     * Tests a speed powerup without collision.
     */
    @Test
    public void testSpeedNoIntersection() {
        PowerSpeed powerup = new PowerSpeed(cursor.getX() + 20, cursor.getY() + 20, 1, 1, gameController.getAreaTracker());
        set.add(powerup);
        Assert.assertEquals(null, handler.powerUpCollisions(set));
    }

    /**
     * Tests a life powerup without collision.
     */
    @Test
    public void testLifeNoIntersection() {
        PowerLife powerup = new PowerLife(cursor.getX() + 20, cursor.getY() + 20, 1, 1, gameController.getAreaTracker());
        set.add(powerup);
        Assert.assertEquals(null, handler.powerUpCollisions(set));
    }


    /**
     * Tests a set without cursor or powerups.
     */
    @Test
    public void testNoPowerupsNoCursor() {
        set.clear();
        Sparx sparx = new Sparx(cursor.getX(), cursor.getY(), 1, 1, gameController.getAreaTracker(), SparxDirection.RIGHT);
        set.add(sparx);
        Assert.assertEquals(null, handler.powerUpCollisions(set));
    }


    /**
     * Tests a powerup without subclass.
     */
    @Test
    public void testNoInstanceOf() {
        PowerLife powerup = new PowerLife(cursor.getX() + 20, cursor.getY() + 20, 1, 1, gameController.getAreaTracker());
        set.add(powerup);
        Assert.assertEquals(null, handler.powerUpCollisions(set));
    }
}
