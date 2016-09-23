package nl.tudelft.sem.group2;

import static nl.tudelft.sem.group2.global.Globals.FAST_AREA_MULTIPLIER;
import static nl.tudelft.sem.group2.global.Globals.SLOW_AREA_MULTIPLIER;
import static nl.tudelft.sem.group2.global.Globals.TARGET_PERCENTAGE;

/**
 * Keeps track of current score and percentage.
 */
public class ScoreCounter {


    //Total score accumulated by player.
    private int totalScore;

    //Total percentage of current level is covered by player.
    private double totalPercentage;

    private double targetPercentage;

    /**
     * Default score counter constructor.
     */
    public ScoreCounter() {
        this.totalPercentage = 0;
        this.totalScore = 0;
        this.targetPercentage = TARGET_PERCENTAGE;
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

        if (fastArea) {
            totalScore += percentageIncrease * FAST_AREA_MULTIPLIER;
        } else {
            totalScore += percentageIncrease * SLOW_AREA_MULTIPLIER;
        }
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