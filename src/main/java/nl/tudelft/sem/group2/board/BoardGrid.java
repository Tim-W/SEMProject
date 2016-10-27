package nl.tudelft.sem.group2.board;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import static nl.tudelft.sem.group2.global.Globals.BOARD_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.BOARD_WIDTH;

/**
 * Created by Erik on 25-10-2016.
 */
public class BoardGrid {

    private AreaState[][] boardGrid;
    private int width;
    private int height;

    /**
     * Constructor for the BoardGrid class.
     * The constructor sets all the grid points to border and the rest to uncovered
     */
    public BoardGrid() {
        this(getGridWidth() + 1, getGridHeight() + 1);

    }

    /**
     * Constructor for the BoardGrid class.
     * The constructor sets all the grid points to border and the rest to uncovered
     *
     * @param gridWidth the gridWidth
     * @param gridHeight the gridHeight
     */
    public BoardGrid(int gridWidth, int gridHeight) {

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
     *
     * @param cor
     * @param state
     */
    public void setState(Point cor, AreaState state) {
        if (inBound(cor)) {
            this.boardGrid[cor.x][cor.y] = state;
        }
    }

    /**
     *
     * @param cor
     * @return
     */
    public AreaState getState(Point cor) {
        if (inBound(cor)) {
            return this.boardGrid[cor.x][cor.y];
        }
        return null;
    }

    /**
     *
     * @param cor
     * @return
     */
    private boolean inBound(Point cor) {
        if (!(cor instanceof Coordinate)) {
            cor = new Coordinate(cor.x, cor.y);
        }
        return ((Coordinate) cor).xBetween(0, width) && ((Coordinate) cor).yBetween(0, height);
    }

    /**
     *
     * @param cor
     * @return true if the tile at (x,y) has an OUTERBORDER AreaState
     */
    public boolean isOuterborder(Point cor) {
        return AreaState.OUTERBORDER.equals(getState(cor));
    }

    /**
     *
     * @param cor
     * @return true if the tile at (x,y) has an UNCOVERED AreaState
     */
    public boolean isUncovered(Point cor) {
        return AreaState.UNCOVERED.equals(getState(cor));
    }


    /**
     *
     * @param cor
     * @return true if the tile at (x,y) has an INNERBORDER AreaState
     */
    public boolean isInnerborder(Point cor) {
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
     * Shows a log which visualise the current board grid state.
     */
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
