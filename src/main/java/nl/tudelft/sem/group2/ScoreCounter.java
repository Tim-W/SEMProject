package nl.tudelft.sem.group2;

import java.util.logging.Level;

import static nl.tudelft.sem.group2.global.Globals.*;

public class ScoreCounter {

	private static final Logger LOGGER = LaunchApp.getLogger();

	// Total score accumulated by player.
	private int totalScore;

	// Total percentage of current level is covered by player.
	private double totalPercentage;

	// Percentage which player needs to achieve to win the level.
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

		LOGGER.log(Level.INFO, "Percentage increased with " + Math.round(percentageIncrease * FAST_AREA_MULTIPLIER) / 100.0 + " to "
				+ Math.round(totalPercentage * FAST_AREA_MULTIPLIER) / 100.0, this.getClass());

		if (fastArea) {
			totalScore += percentageIncrease * FAST_AREA_MULTIPLIER;
			LOGGER.log(Level.INFO, "Score increased with " + Math.round(percentageIncrease * FAST_AREA_MULTIPLIER), this.getClass());
		} else {
			totalScore += percentageIncrease * SLOW_AREA_MULTIPLIER;
			LOGGER.log(Level.INFO, "Score updated with " + Math.round(percentageIncrease * SLOW_AREA_MULTIPLIER), this.getClass());
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