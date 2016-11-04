package nl.tudelft.sem.group2.powerups;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.ColorAdjust;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by dennis on 04-11-16.
 */
public class CursorPowerupHandlerTest {

    private CursorPowerupHandler powerupHandler;


    @Before
    public void setUp() {
        powerupHandler = new CursorPowerupHandler();
    }

    @Test
    public void testApplyEat() {
        GraphicsContext gc = mock(GraphicsContext.class);
        powerupHandler.setCurrentPowerup(PowerUpType.EAT);

        powerupHandler.applyEffect(gc);

        verify(gc, times(1)).applyEffect(any(ColorAdjust.class));
    }

    @Test
    public void testApplySpeed() {
        GraphicsContext gc = mock(GraphicsContext.class);
        powerupHandler.setCurrentPowerup(PowerUpType.SPEED);

        powerupHandler.applyEffect(gc);
        verify(gc, times(1)).applyEffect(any(ColorAdjust.class));
    }

    @Test
    public void testApplyNone() {
        GraphicsContext gc = mock(GraphicsContext.class);
        powerupHandler.setCurrentPowerup(PowerUpType.NONE);

        powerupHandler.applyEffect(gc);

        verify(gc, times(0)).applyEffect(any(ColorAdjust.class));
    }

    @Test
    public void updatePowerupZeroTime() {
        powerupHandler.setCurrentPowerup(PowerUpType.EAT);
        powerupHandler.setPowerUpDuration(0);

        powerupHandler.updatePowerUp();

        Assert.assertEquals(PowerUpType.NONE, powerupHandler.getCurrentPowerup());
        Assert.assertEquals(0, powerupHandler.getPowerUpDuration());
    }

    @Test
    public void updatePowerupDecreaseTime() {
        powerupHandler.setCurrentPowerup(PowerUpType.EAT);
        powerupHandler.setPowerUpDuration(1);

        powerupHandler.updatePowerUp();

        Assert.assertEquals(PowerUpType.EAT, powerupHandler.getCurrentPowerup());
        Assert.assertEquals(0, powerupHandler.getPowerUpDuration());
    }

    @Test
    public void updateNoPowerupNoTime() {
        powerupHandler = mock(CursorPowerupHandler.class);
        when(powerupHandler.hasPowerup()).thenReturn(false);
        when(powerupHandler.getPowerUpDuration()).thenReturn(0);

        verify(powerupHandler, times(0)).setCurrentPowerup(any(PowerUpType.class));
        verify(powerupHandler, times(0)).decrementDuration();
    }

    @Test
    public void updateNoPowerupTime() {
        powerupHandler = mock(CursorPowerupHandler.class);
        when(powerupHandler.hasPowerup()).thenReturn(false);
        when(powerupHandler.getPowerUpDuration()).thenReturn(1);

        verify(powerupHandler, times(0)).setCurrentPowerup(any(PowerUpType.class));
        verify(powerupHandler, times(0)).decrementDuration();
    }
}
