package nl.tudelft.sem.group2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static nl.tudelft.sem.group2.global.Globals.GRID_SURFACE;

/**
 * Created by gijs on 26-10-2016.
 */
public class ScoreCounterTest {
    private ScoreCounter scoreCounter;

    @Before
    public void setUp() throws Exception {
        scoreCounter = new ScoreCounter(1, 30);
    }

    @Test
    public void updateScore() throws Exception {
        double percentage = scoreCounter.getTotalPercentage();
        int completedArea = 10;
        scoreCounter.updateScore(completedArea, true);
        Assert.assertEquals(percentage + (double) completedArea / GRID_SURFACE * 100 / 2, scoreCounter.getTotalPercentage
                (), 0.01);
    }

    @Test
    public void hasWon() throws Exception {
        scoreCounter.setTargetPercentage(10);
        scoreCounter.setTotalPercentage(20);
        Assert.assertTrue(scoreCounter.hasWon());
    }

}