package nl.tudelft.sem.group2.level;

/**
 * Created by gijs on 26-10-2016.
 */
public class Level {
    private int percentage;
    private int id;
    // Boolean that states if the game is running
    private boolean isRunning = false;

    /**
     * creates a new level.
     *
     * @param percentage of level
     * @param id         of level
     */
    public Level(int percentage, int id) {
        this.percentage = percentage;
        this.id = id;
    }

    /**
     * starts the level.
     */
    public void start() {
        isRunning = true;
    }

    /**
     * pause the level.
     */
    public void pause() {
        isRunning = false;
    }

    /**
     * get the percentage of the level.
     *
     * @return int in percentage
     */
    public int getPercentage() {
        return percentage;
    }

    /**
     * get the id of the level.
     *
     * @return int id of the level.
     */
    public int getID() {
        return id;
    }

    /**
     * returns if level is running.
     *
     * @return boolean.
     */
    public boolean isRunning() {
        return isRunning;
    }
}
