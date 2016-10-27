package nl.tudelft.sem.group2.powerups;

import nl.tudelft.sem.group2.board.AreaTracker;
import nl.tudelft.sem.group2.global.Globals;
import nl.tudelft.sem.group2.units.LineTraveller;

/**
 * Main PowerUp class.
 */
public class Powerup extends LineTraveller {

    private int duration;


    /**
     * Creates a new powerup.
     *
     * @param x           x coord
     * @param y           y coord
     * @param width       width, used for collision
     * @param height      height, used for collision
     */
    public Powerup(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.duration = Globals.POWERUP_LIFETIME;
    }

    /**
     * Powerups stand still.
     */
    @Override
    public void move() {
    }

    /**
     * decreases the lifetime duration of the powerup.
     *
     * @param n the decrease amount
     */
    public void decreaseDuration(int n) {
        duration -= n;
    }

    /**
     * decreases the lifetime duration of the powerup.
     */
    public void decreaseDuration() {
        decreaseDuration(1);
    }

    public int getDuration() {
        return this.duration;
    }

    /**
     * @return PowerUp to string format
     */
    public String toString() {
        return "PowerUp";
    }
}
