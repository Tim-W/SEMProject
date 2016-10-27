package nl.tudelft.sem.group2;

import javafx.scene.paint.Color;

import java.util.Observable;
import java.util.logging.Level;

import static nl.tudelft.sem.group2.global.Globals.FAST_AREA_MULTIPLIER;
import static nl.tudelft.sem.group2.global.Globals.GRID_SURFACE;
import static nl.tudelft.sem.group2.global.Globals.SLOW_AREA_MULTIPLIER;

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

    private Color color;

    private int ID;

    /**
     * Default score counter constructor.
     *
     * @param ID specifying the cursor.
     */
    public ScoreCounter(int ID, int targetPercentage) {
        this.totalPercentage = 0;
        this.totalScore = 0;
        this.targetPercentage = targetPercentage;
        this.ID = ID;
        color = Color.BLUE;
        //if player 2
        if (ID == 1) {
            color = Color.RED;
        }

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
        double percentageIncrease = (double) completedArea / GRID_SURFACE * 100 / 2;
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
            LOGGER.log(Level.INFO, "Score increased with "
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

    public void setTotalPercentage(int totalPercentage) {
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

    public void setTargetPercentage(int targetPercentage) {
        this.targetPercentage = targetPercentage;
    }

    /**
     * get id of the cursor.
     *
     * @return int ID
     */
    public int getID() {
        return ID;
    }

    /**
     * notify life changed.
     * @param lives of cursor
     */
    public void notifyLife(int lives) {
        setChanged();
        notifyObservers(lives);
    }
}