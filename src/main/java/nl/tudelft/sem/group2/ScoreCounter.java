package nl.tudelft.sem.group2;

import javafx.scene.paint.Color;

import java.util.Observable;
import java.util.logging.Level;

import static nl.tudelft.sem.group2.global.Globals.FAST_AREA_MULTIPLIER;
import static nl.tudelft.sem.group2.global.Globals.SLOW_AREA_MULTIPLIER;
import static nl.tudelft.sem.group2.global.Globals.TARGET_PERCENTAGE;

/**
 * Class which keeps track of the current score of the player.
 *
 * @author Rheddes.
 */
public class ScoreCounter extends Observable {

    private static final Logger LOGGER = Logger.getLogger();

    // Total score accumulated by player.
    private int totalScore;

    // Total percentage of current level is covered by player.
    private double totalPercentage;

    // Percentage which player needs to achieve to win the level.
    private double targetPercentage;

    private Color color = Color.RED;

    /**
     * Default score counter constructor.
     *
     * @param color
     */
    public ScoreCounter(Color color) {
        this.totalPercentage = 0;
        this.totalScore = 0;
        this.targetPercentage = TARGET_PERCENTAGE;
        this.color = color;

    }


    /**
     * Method to update current score and percentage.
     *
     * @param completedArea the area (in pixels) that the player newly covered
     * @param fastArea      boolean that tells if the just covered area was created
     *                      slow (double points),
     *                      or fast (normal points)
     */
    public void updateScore(int completedArea, boolean fastArea) {
        int totalArea = LaunchApp.getGridWidth() * LaunchApp.getGridHeight();
        double percentageIncrease = (double) completedArea / ((double) totalArea * 2);
        totalPercentage += percentageIncrease;

        LOGGER.log(Level.INFO, "Percentage increased with "
                + Math.round(percentageIncrease * FAST_AREA_MULTIPLIER) / 100.0 + " to "
                + Math.round(totalPercentage * FAST_AREA_MULTIPLIER) / 100.0, this.getClass());

        if (fastArea) {
            totalScore += percentageIncrease * FAST_AREA_MULTIPLIER;
            LOGGER.log(Level.INFO, "Score increased with "
                    + Math.round(percentageIncrease * FAST_AREA_MULTIPLIER), this.getClass());
        } else {
            totalScore += percentageIncrease * SLOW_AREA_MULTIPLIER;
            LOGGER.log(Level.INFO, "Score updated with "
                    + Math.round(percentageIncrease * SLOW_AREA_MULTIPLIER), this.getClass());
        }

        setChanged();
        notifyObservers();

    }

    /**
     * @return true if the claimed percentage is high enough.
     */
    public boolean hasWon() {
        return getTotalPercentage() >= getTargetPercentage();
    }


    /**
     * @return the color of the cursor that matches this scorecounter
     */
    public Color getColor() {
        return this.color;
    }

    public double getTotalPercentage() {
        return totalPercentage;
    }

    public void setTotalPercentage(double totalPercentage) {
        this.totalPercentage = totalPercentage;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public double getTargetPercentage() {
        return targetPercentage;
    }

    public void setTargetPercentage(double targetPercentage) {
        this.targetPercentage = targetPercentage;
    }
}