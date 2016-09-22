package nl.tudelft.sem.group2;

import java.util.logging.Level;

public class ScoreCounter {

	private static final Logger LOGGER = LaunchApp.getLogger();

	// Total score accumulated by player.
	private int totalScore;

	// Total percentage of current level is covered by player.
	private double totalPercentage;

	private double targetPercentage;

	/**
	 * Method to update current score and percentage
	 * 
	 * @param completedArea
	 *            the area (in pixels) that the player newly covered
	 * @param fastArea
	 *            boolean that tells if the just covered area was created slow
	 *            (double points) or fast (normal points)
	 */
	public void updateScore(int completedArea, boolean fastArea) {
		int totalArea = LaunchApp.getGridWidth() * LaunchApp.getGridHeight();
		double percentageIncrease = (double) completedArea / ((double) totalArea * 2);
		totalPercentage += percentageIncrease;
		LOGGER.log(Level.INFO, "Percentage increased with " + Math.round(percentageIncrease * 10000.0) / 100.0 + " to "
				+ Math.round(totalPercentage * 10000.0) / 100.0, this.getClass());

		if (fastArea) {
			totalScore += percentageIncrease * 10000;
			LOGGER.log(Level.INFO, "Score increased with " + Math.round(percentageIncrease * 10000), this.getClass());
		} else {
			totalScore += percentageIncrease * 20000;
			LOGGER.log(Level.INFO, "Score updated with " + Math.round(percentageIncrease * 20000), this.getClass());

		}

	}

	public ScoreCounter() {
		this.totalPercentage = 0;
		this.totalScore = 0;
		this.targetPercentage = .65;
	}

	public ScoreCounter(double totalPercentage, double targetPercentage, int totalScore) {
		this.totalPercentage = totalPercentage;
		this.targetPercentage = targetPercentage;
		this.totalScore = totalScore;
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