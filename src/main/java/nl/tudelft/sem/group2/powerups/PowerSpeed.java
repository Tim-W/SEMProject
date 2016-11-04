package nl.tudelft.sem.group2.powerups;

import javafx.scene.image.Image;

/**
 * Speed PowerupUnit.
 */
public class PowerSpeed extends PowerupUnit {

    /**
     * Creates a new Speed powerup.
     *
     * @param x           x coord
     * @param y           y coord
     * @param width       width, used for collision
     * @param height      height, used for collision
     */
    public PowerSpeed(int x, int y, int width, int height) {
        super(x, y, width, height);
        Image[] sprite = new Image[4];
        sprite[0] = new Image("/images/speed.png");
        sprite[1] = new Image("/images/speed.png");
        sprite[2] = new Image("/images/speed-2.png");
        sprite[3] = new Image("/images/speed-2.png");
        setSprite(sprite);
        this.type = PowerUpType.SPEED;
    }

    /**
     * @return Powerup to string format
     */
    public String toString() {
        return "Speed Powerup";
    }
}
