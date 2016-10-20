package nl.tudelft.sem.group2.units;

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
        switch (rand) {
            case 0:
                return LEFT;
            case 1:
                return RIGHT;
        }
        return LEFT;
    }
}
