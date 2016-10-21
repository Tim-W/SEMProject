package nl.tudelft.sem.group2.units;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A Sparx starts on a line, moving either east (right) or west (left)
 * after which it keeps on moving.
 */
public enum SparxDirection {
    LEFT,
    RIGHT;

    /**
     * Returns a random sparx direction.
     *
     * @return a random sparx direction
     */
    public static SparxDirection randomDirection() {
        int rand = ThreadLocalRandom.current().nextInt(2);
        Map<Integer, SparxDirection> sparxDirectionMap = new HashMap<>();
        sparxDirectionMap.put(0, LEFT);
        sparxDirectionMap.put(1, RIGHT);
        SparxDirection sparxDirection = sparxDirectionMap.get(rand);
        if (sparxDirection == null) {
            return LEFT;
        }
        return sparxDirection;
    }
}
