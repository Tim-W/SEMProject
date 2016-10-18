package nl.tudelft.sem.group2.powerups;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Enum for the power up types.
 */
public enum PowerUpType {
    EAT, LIFE, SPEED, NONE;

    /**
     * Returs a random power type.
     *
     * @return a random power type
     */
    public static PowerUpType randomType() {
        int rand = ThreadLocalRandom.current().nextInt(3);
        switch (rand) {
            case 0:
                return EAT;
            case 1:
                return LIFE;
            case 2:
                return SPEED;
            default:
                return NONE;
        }
    }
}
