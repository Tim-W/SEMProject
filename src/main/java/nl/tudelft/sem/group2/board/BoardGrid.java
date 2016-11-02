package nl.tudelft.sem.group2.board;

import java.awt.Point;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static nl.tudelft.sem.group2.global.Globals.BOARD_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.BOARD_WIDTH;

/**
 * Created by Erik on 25-10-2016.
 */
public final class BoardGrid {

    private static volatile BoardGrid instance;

    private AreaState[][] boardGrid;
    private int width;
    private int height;
    private ThreadLocalRandom threadLocalRandom;

    /**
     * Constructor for the BoardGrid class.
     * The constructor sets all the grid points to border and the rest to uncovered
     */
    private BoardGrid() {
        this(getGridWidth() + 1, getGridHeight() + 1);

    }

    /**
     * Constructor for the BoardGrid class.
     * The constructor sets all the grid points to border and the rest to uncovered
     *
     * @param gridWidth the gridWidth
     * @param gridHeight the gridHeight
     */
    private BoardGrid(int gridWidth, int gridHeight) {

        threadLocalRandom = ThreadLocalRandom.current();

        if (gridWidth > 1 && gridHeight > 1) {
            width = gridWidth;
            height = gridHeight;
        } else {
            width = getGridWidth() + 1;
            height = getGridHeight() + 1;
        }
        boardGrid = new AreaState[width][height];


        for (int i = 0; i < boardGrid.length; i++) {
            for (int j = 0; j < boardGrid[i].length; j++) {
                //By default, all points are uncovered
                boardGrid[j][i] = AreaState.UNCOVERED;
            }
        }
        //If the current row is the first row set all grid points border on that row
        for (int i = 0; i < boardGrid.length; i++) {
            //If current column is the last column set the grid point on that column and the current row border
            boardGrid[0][i] = AreaState.OUTERBORDER;
            boardGrid[boardGrid[0].length - 1][i] = AreaState.OUTERBORDER;
        }

        for (int j = 0; j < boardGrid[0].length; j++) {
            //If the current row is the bottom row set all grid points border on that row
            boardGrid[j][0] = AreaState.OUTERBORDER;
            //If the current column is the last column set the grid point on that column and the current row border
            boardGrid[j][boardGrid.length - 1] = AreaState.OUTERBORDER;
        }
    }

    /**
     * Getter for the BoardGrid this is a singleton class so everywhere the BoardGrid is used it is the same instance
     * This method allows getting of that instance and instantiates it when it is not instantiated yet.
     *
     * @return the only one instance of BoardGrid.
     */
    public static BoardGrid getInstance() {
        if (instance == null) {
            // Put lock on class since it we do not want to instantiate it twice
            synchronized (BoardGrid.class) {
                // Check if boardgrid is in the meanwhile not already instantiated.
                if (instance == null) {
                    instance = new BoardGrid();
                }
            }
        }
        return instance;
    }

    /**
     * Resets the instance of the BoardGrid to null.
     */
    public static void resetBoardGrid() {
        instance = null;
    }

    /**
     * @return grid width - a point on the boardgrid is 2x2 pixels,
     * so a boardgrid contains 150x150
     */
    public static int getGridWidth() {
        return BOARD_WIDTH / 2;
    }

    /**
     * @return grid height - a point on the boardgrid is 2x2 pixels,
     * so a boardgrid contains 150x150
     */
    public static int getGridHeight() {
        // a point on the boardgrid is 2x2 pixels, so a boardgrid contains 150x150
        return BOARD_HEIGHT / 2;
    }

    /**
     *
     * @param cor the point/coordinate on the map for witch the state has to change
     * @param state the new state for the given point
     */
    public void setState(Coordinate cor, AreaState state) {
        if (inBound(cor)) {
            this.boardGrid[cor.getX()][cor.getY()] = state;
        }
    }
    /**
     *
     * @param p the point/coordinate on the map for witch the state has to change
     * @param state the new state for the given point
     */
    public void setState(Point p, AreaState state) {
        if (inBound(p)) {
            this.boardGrid[p.x][p.y] = state;
        }
    }

    /**
     *
     * @param cor the point/coordinate on the map for witch the state is needed
     * @return The state of the point, if the point is found on the map. null otherwise
     */
    public AreaState getState(Coordinate cor) {
        if (inBound(cor)) {
            return this.boardGrid[cor.getX()][cor.getY()];
        }
        return null;
    }
    /**
     *
     * @param p the point/coordinate on the map for witch the state is needed
     * @return The state of the point, if the point is found on the map. null otherwise
     */
    public AreaState getState(Point p) {
        if (inBound(p)) {
            return this.boardGrid[p.x][p.y];
        }
        return null;
    }

    /**
     *
     * @param cor the point/coordinate on the map to be checked
     * @return true if the point/coordinate is on the map
     */
    private boolean inBound(Coordinate cor) {
        return cor.xBetween(0, width) && cor.yBetween(0, height);
    }

    /**
     *
     * @param p the point/coordinate on the map to be checked
     * @return true if the point/coordinate is on the map
     */
    private boolean inBound(Point p) {
        Coordinate cor = new Coordinate(p.x, p.y);
        return cor.xBetween(0, width) && cor.yBetween(0, height);
    }

    /**
     *
     * @param cor the point/coordinate on the map to be checked
     * @return true if the tile at (x,y) has an OUTERBORDER AreaState
     */
    public boolean isOuterborder(Coordinate cor) {
        return AreaState.OUTERBORDER.equals(getState(cor));
    }

    /**
     *
     * @param cor the point/coordinate on the map to be checked
     * @return true if the tile at (x,y) has an UNCOVERED AreaState
     */
    public boolean isUncovered(Coordinate cor) {
        return AreaState.UNCOVERED.equals(getState(cor));
    }

    /**
     *
     * @param cor the point/coordinate on the map to be checked
     * @return true if the tile at (x,y) has an INNERBORDER AreaState
     */
    public boolean isInnerborder(Coordinate cor) {
        return AreaState.INNERBORDER.equals(getState(cor));
    }

    /**
     *
     * @param x x coord
     * @param y y coord
     * @return true if the tile at (x,y) has an OUTERBORDER AreaState
     */
    public boolean isOuterborder(int x, int y) {
        return isOuterborder(new Coordinate(x, y));
    }

    /**
     *
     * @param x x coord
     * @param y y coord
     * @return true if the tile at (x,y) has an UNCOVERED AreaState
     */
    public boolean isUncovered(int x, int y) {
        return isUncovered(new Coordinate(x, y));
    }

    /**
     *
     * @param x x coord
     * @param y y coord
     * @return true if the tile at (x,y) has an INNERBORDER AreaState
     */
    public boolean isInnerborder(int x, int y) {
        return isInnerborder(new Coordinate(x, y));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     * Method that returns true if the corner is covered.
     *
     * @param quadrant the quadrant to be checked
     * @return true if the corner is covered
     */
    private boolean cornerIsCovered(int quadrant) {
        switch (quadrant) {
            case 0:
                if (boardGrid[1][1] != AreaState.UNCOVERED) {
                    boardGrid[0][0] = AreaState.INNERBORDER;
                    return true;
                }
                break;
            case 1:
                if (boardGrid[BOARD_WIDTH / 2 - 1][1] != AreaState.UNCOVERED) {
                    boardGrid[BOARD_WIDTH / 2][0] = AreaState.INNERBORDER;
                    return true;
                }
                break;
            case 2:
                if (boardGrid[BOARD_WIDTH / 2 - 1][BOARD_HEIGHT / 2 - 1] != AreaState.UNCOVERED) {
                    boardGrid[BOARD_WIDTH / 2][BOARD_HEIGHT / 2] = AreaState.INNERBORDER;
                    return true;
                }
                break;
            case 3:
                if (boardGrid[1][BOARD_HEIGHT / 2 - 1] != AreaState.UNCOVERED) {
                    boardGrid[0][BOARD_HEIGHT / 2] = AreaState.INNERBORDER;
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
     */
    public int[] findPowerupLocation(int quadrant) {
        int[] res = new int[2];
        int x = BOARD_WIDTH / 4;
        int y = BOARD_HEIGHT / 4;

        if (this.cornerIsCovered(quadrant)) {

            while (boardGrid[x][y] != AreaState.OUTERBORDER) {

                int[] newLocation = permutateLocation(x, y, quadrant);
                x = newLocation[0];
                y = newLocation[1];

                if (x > BOARD_WIDTH / 2 || x < 0) {
                    x = BOARD_WIDTH / 4;
                }
                if (y > BOARD_HEIGHT / 2 || y < 0) {
                    y = BOARD_HEIGHT / 4;
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
     */
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
     */
    private int[] getCornerCoordinates(int quadrant) {
        int x;
        int y;
        Map<Integer, List<Integer>> quadrantCornerMap = new HashMap<>();
        quadrantCornerMap.put(0, Arrays.asList(0, 0));
        quadrantCornerMap.put(1, Arrays.asList(BOARD_WIDTH / 2, 0));
        quadrantCornerMap.put(2, Arrays.asList(BOARD_WIDTH / 2, BOARD_HEIGHT / 2));
        quadrantCornerMap.put(3, Arrays.asList(0, BOARD_HEIGHT / 2));
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

    /**
     * Shows a log which visualise the current board grid state.
     */
    //TODO rename to toString
    public void printBoardGrid() {
        // A map representing the relations between AreaStates and their String visualizations.
        Map<AreaState, String> areaStateVisualisation = new HashMap<>();
        areaStateVisualisation.put(AreaState.OUTERBORDER, "[X]");
        areaStateVisualisation.put(AreaState.INNERBORDER, "[*]");
        areaStateVisualisation.put(AreaState.UNCOVERED, "[ ]");
        areaStateVisualisation.put(AreaState.FAST, "[F]");
        areaStateVisualisation.put(AreaState.SLOW, "[S]");
        for (AreaState[] column : boardGrid) {
            for (AreaState state : column) {
                System.out.print(areaStateVisualisation.get(state));
            }
            System.out.println();
        }
    }
}
