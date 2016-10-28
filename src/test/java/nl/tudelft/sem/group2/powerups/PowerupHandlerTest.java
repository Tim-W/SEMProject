package nl.tudelft.sem.group2.powerups;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for the powerupHandler test.
 */
public class PowerupHandlerTest {

    private PowerupHandler powerupHandler;

    @Before
    public void setUp() {
        powerupHandler = new PowerupHandler();
    }

    @Test
    public void testConstructor() {
        powerupHandler = new PowerupHandler();
        Assert.assertEquals(powerupHandler.getCurrentPowerup(), PowerUpType.NONE);
        Assert.assertEquals(0, powerupHandler.getPowerUpDuration());
    }

    @Test
    public void hasPowerupTest1() {
        Assert.assertFalse(powerupHandler.hasPowerup());
    }

    @Test
    public void hasPowerupTest2() {
        powerupHandler.setCurrentPowerup(PowerUpType.EAT);
        Assert.assertTrue(powerupHandler.hasPowerup());
    }

    @Test
    public void decrementDurationTest() {
        powerupHandler.setPowerUpDuration(1);
        powerupHandler.decrementDuration();
        Assert.assertEquals(0, powerupHandler.getPowerUpDuration());
    }
}
