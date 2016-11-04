package nl.tudelft.sem.group2.powerups;

import javafx.scene.image.Image;

/**
 * Eat PowerupUnit.
 */
public class PowerEat extends PowerupUnit {

    /**
     * Creates a new Eat powerup.
     *
     * @param x      x coord
     * @param y      y coord
     * @param width  width, used for collision
     * @param height height, used for collision
     */
    public PowerEat(int x, int y, int width, int height) {
        super(x, y, width, height);
        Image[] sprite = new Image[4];
        sprite[0] = new Image("/images/eat.png");
        sprite[1] = new Image("/images/eat.png");
        sprite[2] = new Image("/images/eat-1.png");
        sprite[3] = new Image("/images/eat-1.png");
        setSprite(sprite);
        this.type = PowerUpType.EAT;
    }

    /**
     * @return Powerup to string format
     */
    public String toString() {
        return "Eat Powerup";
    }
}
