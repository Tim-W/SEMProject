package nl.tudelft.sem.group2.powerups;

import nl.tudelft.sem.group2.global.Globals;
import nl.tudelft.sem.group2.board.AreaState;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Erik on 27-10-2016.
 */
public class PowerUpFactory {

/*
    /**
     * Method that returns true if the corner is covered.
     *
     * @param quadrant the quadrant to be checked
     * @return true if the corner is covered
     *//*
    private boolean cornerIsCovered(int quadrant) {
        switch (quadrant) {
            case 0:
                if (boardGrid[1][1] != AreaState.UNCOVERED) {
                    boardGrid[0][0] = AreaState.INNERBORDER;
                    return true;
                }
                break;
            case 1:
                if (boardGrid[Globals.BOARD_WIDTH / 2 - 1][1] != AreaState.UNCOVERED) {
                    boardGrid[Globals.BOARD_WIDTH / 2][0] = AreaState.INNERBORDER;
                    return true;
                }
                break;
            case 2:
                if (boardGrid[Globals.BOARD_WIDTH / 2 - 1][Globals.BOARD_HEIGHT / 2 - 1] != AreaState.UNCOVERED) {
                    boardGrid[Globals.BOARD_WIDTH / 2][Globals.BOARD_HEIGHT / 2] = AreaState.INNERBORDER;
                    return true;
                }
                break;
            case 3:
                if (boardGrid[1][Globals.BOARD_HEIGHT / 2 - 1] != AreaState.UNCOVERED) {
                    boardGrid[0][Globals.BOARD_HEIGHT / 2] = AreaState.INNERBORDER;
                    return true;
                }
                break;
            default:
                return false;
        }
        return false;
    }


    /**
     * method that finds a suitable location for a new powerup drop.
     *
     * @param quadrant the quadrant to be working towards
     * @return an int[] containing the coordinates of the powerup drop location
     *//*
    public int[] findPowerupLocation(int quadrant) {
        int[] res = new int[2];
        int x = Globals.BOARD_WIDTH / 4;
        int y = Globals.BOARD_HEIGHT / 4;

        if (this.cornerIsCovered(quadrant)) {

            while (this.getBoardGrid()[x][y] != AreaState.OUTERBORDER) {

                int[] newLocation = permutateLocation(x, y, quadrant);
                x = newLocation[0];
                y = newLocation[1];

                if (x > Globals.BOARD_WIDTH / 2 || x < 0) {
                    x = Globals.BOARD_WIDTH / 4;
                }
                if (y > Globals.BOARD_HEIGHT / 2 || y < 0) {
                    y = Globals.BOARD_HEIGHT / 4;
                }
            }
            res[0] = x;
            res[1] = y;
        } else {
            res[0] = getCornerCoordinates(quadrant)[0];
            res[1] = getCornerCoordinates(quadrant)[1];
        }

        return res;
    }

    /**
     * Computes a new random location depending on the quadrant.
     *
     * @param x        the x of the old location
     * @param y        the y of the new location
     * @param quadrant the quadrant it needs to be in
     * @return an int[] containing a new random location
     *//*i
    private int[] permutateLocation(int x, int y, int quadrant) {
        int[] res = new int[2];

        Map<Integer, List<Integer>> locationMap = new HashMap<>();
        locationMap.put(0, Arrays.asList(-1, 1, -1, 1));
        locationMap.put(1, Arrays.asList(0, 2, -1, 1));
        locationMap.put(2, Arrays.asList(0, 2, 0, 2));
        locationMap.put(3, Arrays.asList(-1, 1, 0, 2));
        List<Integer> locations = locationMap.get(quadrant);
        if (locations != null) {
            x += threadLocalRandom.nextInt(locations.get(0), locations.get(1));
            y += threadLocalRandom.nextInt(locations.get(2), locations.get(3));
        } else {
            x += threadLocalRandom.nextInt(-1, 2);
            y += threadLocalRandom.nextInt(-1, 2);
        }

        res[0] = x;
        res[1] = y;
        return res;
    }

    /**
     * Gets the coordinates of the corner of a quadrant.
     *
     * @param quadrant the quadrant
     * @return an int[] containing the coordinates of the corner of the quadrant
     *//*
    private int[] getCornerCoordinates(int quadrant) {
        int x;
        int y;
        Map<Integer, List<Integer>> quadrantCornerMap = new HashMap<>();
        quadrantCornerMap.put(0, Arrays.asList(0, 0));
        quadrantCornerMap.put(1, Arrays.asList(Globals.BOARD_WIDTH / 2, 0));
        quadrantCornerMap.put(2, Arrays.asList(Globals.BOARD_WIDTH / 2, Globals.BOARD_HEIGHT / 2));
        quadrantCornerMap.put(3, Arrays.asList(0, Globals.BOARD_HEIGHT / 2));
        List<Integer> integers = quadrantCornerMap.get(quadrant);
        if (integers != null) {
            x = integers.get(0);
            y = integers.get(1);
        } else {
            x = 0;
            y = 0;
        }
        int[] res = new int[2];
        res[0] = x;
        res[1] = y;
        return res;
    }
*/

}
