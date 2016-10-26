package nl.tudelft.sem.group2.level;

/**
 * Created by gijs on 26-10-2016.
 */
public class Level {
    private int percentage;
    private int id;
    private int qixSize;
    // Boolean that states if the game is running
    private boolean isRunning = false;

    /**
     * creates a new level.
     *
     * @param percentage of level
     * @param id         of level
     * @param qixSize    of qix
     */
    public Level(int percentage, int id, int qixSize) {
        this.percentage = percentage;
        this.id = id;
        this.qixSize = qixSize;
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

    /**
     * get the linesize of the qix.
     *
     * @return int in linesize of qix
     */
    public int getQixSize() {
        return qixSize;
    }
}
