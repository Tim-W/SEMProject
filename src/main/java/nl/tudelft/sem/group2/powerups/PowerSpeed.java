package nl.tudelft.sem.group2.powerups;

import javafx.scene.image.Image;
import nl.tudelft.sem.group2.board.AreaTracker;

/**
 * Speed Powerup.
 */
public class PowerSpeed extends Powerup {

    /**
     * Creates a new Speed powerup.
     *
     * @param x           x coord
     * @param y           y coord
     * @param width       width, used for collision
     * @param height      height, used for collision
     * @param areaTracker the AreaTracker
     */
    public PowerSpeed(int x, int y, int width, int height, AreaTracker areaTracker) {
        super(x, y, width, height);
        Image[] sprite = new Image[4];
        sprite[0] = new Image("/images/speed.png");
        sprite[1] = new Image("/images/speed.png");
        sprite[2] = new Image("/images/speed-2.png");
        sprite[3] = new Image("/images/speed-2.png");
        setSprite(sprite);
    }

    /**
     * @return PowerUp to string format
     */
    public String toString() {
        return "Speed PowerUp";
    }
}
