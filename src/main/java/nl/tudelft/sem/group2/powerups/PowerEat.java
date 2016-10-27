package nl.tudelft.sem.group2.powerups;

import javafx.scene.image.Image;
import nl.tudelft.sem.group2.board.AreaTracker;

/**
 * Eat Powerup.
 */
public class PowerEat extends Powerup {

    /**
     * Creates a new Eat powerup.
     *
     * @param x           x coord
     * @param y           y coord
     * @param width       width, used for collision
     * @param height      height, used for collision
     * @param areaTracker the AreaTracker
     */
    public PowerEat(int x, int y, int width, int height, AreaTracker areaTracker) {
        super(x, y, width, height, areaTracker);
        Image[] sprite = new Image[4];
        sprite[0] = new Image("/images/eat.png");
        sprite[1] = new Image("/images/eat.png");
        sprite[2] = new Image("/images/eat-1.png");
        sprite[3] = new Image("/images/eat-1.png");
        setSprite(sprite);
    }

    /**
     * @return PowerUp to string format
     */
    public String toString() {
        return "Eat PowerUp";
    }
}
