package nl.tudelft.sem.group2.powerups;

import javafx.scene.image.Image;

/**
 * Life Powerup.
 */
public class PowerLife extends Powerup {

    /**
     * Creates a new Life powerup.
     *
     * @param x           x coord
     * @param y           y coord
     * @param width       width, used for collision
     * @param height      height, used for collision
     */
    public PowerLife(int x, int y, int width, int height) {
        super(x, y, width, height);
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
