package nl.tudelft.sem.group2.powerups;

import javafx.scene.image.Image;
import nl.tudelft.sem.group2.AreaTracker;

/**
 * Created by dennis on 17-10-16.
 */
public class PowerLife extends Powerup {


    public PowerLife(int x, int y, int width, int height, AreaTracker areaTracker) {
        super(x, y, width, height, areaTracker);
        Image[] sprite = new Image[4];
        sprite[0] = new Image("/images/heart.png");
        sprite[1] = new Image("/images/heart.png");
        sprite[2] = new Image("/images/heart-1.png");
        sprite[3] = new Image("/images/heart-1.png");
        setSprite(sprite);
    }

    /**
     * @return PowerUp to string format
     */
    public String toString() {
        return "Life PowerUp";
    }
}
