package nl.tudelft.sem.group2.powerups;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Enum for the power up types.
 */
public enum PowerUpType {
    EAT, LIFE, SPEED, NONE;

    /**
     * Returns a random power type.
     *
     * @return a random power type
     */
    public static PowerUpType randomType() {
        int rand = ThreadLocalRandom.current().nextInt(3);
        Map<Integer, PowerUpType> powerUpTypeMap = new HashMap<>();
        powerUpTypeMap.put(0, EAT);
        powerUpTypeMap.put(1, LIFE);
        powerUpTypeMap.put(2, SPEED);
        PowerUpType powerUpType = powerUpTypeMap.get(rand);
        if (powerUpType == null) {
            return NONE;
        }
        return powerUpType;
    }
}
