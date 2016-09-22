package nl.tudelft.sem.group2;

public class ScoreCounter {


    //Total score accumulated by player.
    private int totalScore;

    //Total percentage of current level is covered by player.
    private double totalPercentage;

    private double targetPercentage;

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

    /**
     * Method to update current score and percentage
     *
     * @param completedArea the area (in pixels) that the player newly covered
     * @param fastArea      boolean that tells if the just covered area was created slow (double points) or fast (normal points)
     */
    public void updateScore(int completedArea, boolean fastArea) {
        int totalArea = LaunchApp.getGridWidth() * LaunchApp.getGridHeight();
        double percentageIncrease = (double) completedArea / ((double) totalArea * 2);
        totalPercentage += percentageIncrease;

        if (fastArea) totalScore += percentageIncrease * 10000;
        else totalScore += percentageIncrease * 20000;


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