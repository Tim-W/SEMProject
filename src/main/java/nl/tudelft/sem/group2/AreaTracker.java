package nl.tudelft.sem.group2;

import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.units.Stix;

import java.awt.Point;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;

/**
 * Tracks the area of the current level, of which pixels are covered by the player.
 */
public class AreaTracker {

    private static final Logger LOGGER = Logger.getLogger();

    private AreaState[][] boardGrid = new AreaState[LaunchApp.getGridWidth() + 1][LaunchApp.getGridHeight() + 1];
    private Stack<Point> visiting = new Stack<>();
    private LinkedList<Point> area1, area2, border1, border2, newBorder, newArea;
    private Set<Point> visited;
    private boolean foundQix;

    /**
     * Constructor for the AreaTracker class.
     * The constructor sets all the grid points to border and the rest to uncovered
     */
    public AreaTracker() {
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
     * Custom constructor mainly created for testing purposes.
     *
     * @param width  width of the boardGrid
     * @param height height of the boardGrid
     */
    public AreaTracker(int width, int height) {
        boardGrid = new AreaState[width][height];
        for (int i = 0; i < boardGrid.length; i++) {
            for (int j = 0; j < boardGrid[i].length; j++) {
                //By default, all points are uncovered
                boardGrid[j][i] = AreaState.UNCOVERED;
            }
        }

        for (int i = 0; i < boardGrid.length; i++) {
            //If current column is the last column set the grid point on that column and the current row border
            boardGrid[0][i] = AreaState.OUTERBORDER;
            boardGrid[boardGrid[0].length - 1][i] = AreaState.OUTERBORDER;
        }

        if (boardGrid.length > 0) {
            for (int j = 0; j < boardGrid[0].length; j++) {
                //If the current row is the bottom row set all grid points border on that row
                boardGrid[j][0] = AreaState.OUTERBORDER;
                boardGrid[j][boardGrid.length - 1] = AreaState.OUTERBORDER;
            }
        }
    }

    /**
     * /**
     * Method that updates the grid when a stix is completed.
     *
     * @param qixCoordinates current qix coordinates
     * @param fastArea       tells if stix was created fast or slow (double points if slow)
     * @param stix           current stix to use
     * @param scoreCounter   the counter that handles the score
     */
    public void calculateNewArea(Point qixCoordinates, boolean fastArea, Stix stix, ScoreCounter scoreCounter) {
        setOuterBorders(stix);
        // Obtain first and second point from the stix to determine
        // beginposition for the floodfill algorithm
        Point start = stix.getStixCoordinates().getFirst();
        Point dir = stix.getStixCoordinates().get(1);
        // Instantiate the two temporary area trackers, these linkedlists
        // accumulate all the points on one side of the stix
        // When the floodfill algorithm finds a qix however the linkedlist is
        // set to null
        resetAreas();
        resetBorders();
        // Initialize the set which contains the visited points for the
        // floodfill algorithm
        visited = new HashSet<>();
        checkDirections(qixCoordinates, start, dir, stix);
        //Check in which of the two areas the qix was found and set the other one to the newly created area
        setBorders();
        updateScoreCounter(fastArea, stix, scoreCounter);

        if (fastArea) {
            Logger.getLogger().log(Level.INFO, "New fast area claimed with size " + newArea.size(), this.getClass());
        } else {
            LOGGER.log(Level.INFO, "New slow area claimed with size " + newArea.size(), this.getClass());
        }
        //Update the grid with the newly created area
        for (Point current : newArea) {
            if (fastArea) {
                boardGrid[(int) current.getX()][(int) current.getY()] = AreaState.FAST;
            } else {
                boardGrid[(int) current.getX()][(int) current.getY()] = AreaState.SLOW;
            }
        }
        //Update the grid with the new inner borders
        for (Point current : newBorder) {
            boardGrid[(int) current.getX()][(int) current.getY()] = AreaState.INNERBORDER;
        }
        resetAreaTracker();
        stix.emptyStix();
    }

    private void resetBorders() {
        border1 = new LinkedList<>();
        border2 = new LinkedList<>();
    }

    private void resetAreas() {
        area1 = new LinkedList<>();
        area2 = new LinkedList<>();
    }

    private void setOuterBorders(Stix stix) {
        //Set all the points from the current stix to border points on the grid
        for (Point current : stix.getStixCoordinates()) {
            boardGrid[(int) current.getX()][(int) current.getY()] = AreaState.OUTERBORDER;
        }
    }

    private void resetAreaTracker() {
        //Reset the temporary area tracker
        area1 = null;
        area2 = null;
        border1 = null;
        border2 = null;
    }

    private void updateScoreCounter(boolean fastArea, Stix stix, ScoreCounter scoreCounter) {

        //When testing create own scoreCounter
        if (scoreCounter == null) {
            scoreCounter = new ScoreCounter(Color.RED);
        }

        //Update score and percentage with newly created area,
        // therefore it's needed to know the stix was created fast or slow
        scoreCounter.updateScore(newArea.size() + stix.getStixCoordinates().size(), fastArea);
    }

    private void setBorders() {
        if (area1 == null) {
            newArea = area2;
            newBorder = border2;
        } else {
            newArea = area1;
            newBorder = border1;
        }
    }

    private void checkDirections(Point qixCoordinates, Point start, Point dir, Stix stix) {
        //Check in which direction the stix first started to move
        if (start.getX() != dir.getX() || start.getY() != dir.getY()) {
            //If stix was first moving in X direction get points above and under the first stix point,
            // start the floodfill algorithm from there
            Point beginPoint1 = new Point((int) dir.getX(), (int) dir.getY() - 1);
            Point beginPoint2 = new Point((int) dir.getX(), (int) dir.getY() + 1);
            if (start.getY() != dir.getY()) {
                //If stix was first moving in Y direction get points left and right the first stix point,
                // start the floodfill algorithm from there
                beginPoint1 = new Point((int) dir.getX() - 1, (int) dir.getY());
                beginPoint2 = new Point((int) dir.getX() + 1, (int) dir.getY());
            }
            foundQix = false;
            floodFill(beginPoint1, qixCoordinates, AreaState.UNCOVERED, true, stix);
            visited.clear();
            foundQix = false;
            floodFill(beginPoint2, qixCoordinates, AreaState.UNCOVERED, false, stix);
        }
    }

    /**
     * The method which starts the recursive floodfill method
     * Floodfill algorithm accomodated to work for qix for more info on how floodfill algorithm works
     * please visit: https://en.wikipedia.org/wiki/Flood_fill.
     *
     * @param pointToCheck The first point to begin checking if it has to be added to area/border
     *                     or if the qix is on that pint.
     * @param qixCoordinates          The coordinates of the qix.
     * @param chosenState  The state of points which get added to the new area.
     * @param addToArea1   Boolean which describes if points should be added to area 1 or 2 and border 1 or 2.
     * @param stix         current stix to use
     */
    public void floodFill(Point pointToCheck, Point qixCoordinates, AreaState chosenState,
                          boolean addToArea1, Stix stix) {
        visiting.push(pointToCheck);
        while (!visiting.isEmpty()) {
            floodFill(qixCoordinates, chosenState, addToArea1, stix);
        }
    }

    /**
     * Floodfill algorithm, this algorithm checks one point.
     * if it's a point that gets added to new area,
     * if the qix is found and adds that to one of the two area trackers.
     * pointToCheck a Point,
     * which is either the starting point determined in the calculateNewArea method
     * or a recursively determined point.
     *
     * @param qixCoorinates the current qix coordinates
     * @param chosenState   begin state of the area,
     *                      that gets added to the new area,
     *                      practically always AreaStates.UNCOVERED
     * @param addToArea1    boolean that keeps thrack of which temporary AreaTracker to use.
     * @param stix          current stix to use
     */
    public void floodFill(Point qixCoorinates, AreaState chosenState, boolean addToArea1, Stix stix) {
        Point pointToCheck = visiting.pop();
        if (foundQix) {
            return;
        }
        // Check if the current point on the grid is the chosen beginstate
        if (boardGrid[(int) pointToCheck.getX()][(int) pointToCheck.getY()] == chosenState) {
            // Check if that point is the coordinate of the qix
            if (pointToCheck.equals(qixCoorinates)) {
                hitQix(addToArea1);
            } else {
                // If that point was not the coordinate of the qix,
                // add that point to the right temporary area tracker
                addPointToAreaTracker(addToArea1, pointToCheck);
            }
        } else if (boardGrid[(int) pointToCheck.getX()][(int) pointToCheck.getY()] == AreaState.OUTERBORDER
                && !stix.getStixCoordinates().contains(pointToCheck)) {
            if (addToArea1) {
                border1.add(pointToCheck);
            } else {
                border2.add(pointToCheck);
            }
            visited.add(pointToCheck);
        } else if (boardGrid[(int) pointToCheck.getX()][(int) pointToCheck.getY()] == AreaState.INNERBORDER) {
            visited.add(pointToCheck);
        }

    }

    private void addPointToAreaTracker(boolean addToArea1, Point pointToCheck) {
        if (addToArea1) {
            area1.add(pointToCheck);
        } else {
            area2.add(pointToCheck);
        }
        visited.add(pointToCheck);
        // Check all the four neighbours of the current point recursively
        Point[] points = new Point[4];
        points[0] = new Point((int) pointToCheck.getX(), (int) pointToCheck.getY() - 1);
        points[1] = new Point((int) pointToCheck.getX(), (int) pointToCheck.getY() + 1);
        points[2] = new Point((int) pointToCheck.getX() - 1, (int) pointToCheck.getY());
        points[3] = new Point((int) pointToCheck.getX() + 1, (int) pointToCheck.getY());
        for (Point point : points) {
            if (!visited.contains(point)) {
                visiting.push(point);
            }
        }
    }

    private void hitQix(boolean addToArea1) {
        // If that point was the coordinate of the qix set the temporary area tracker that is currently in use to null
        if (addToArea1) {
            area1 = null;
            border1 = null;
        } else {
            area2 = null;
            border2 = null;
        }
        foundQix = true;
    }

    /**
     * Getter for the boardGrid.
     *
     * @return the boardGrid
     */
    public AreaState[][] getBoardGrid() {
        return boardGrid;
    }

    /**
     * Shows a log which visualise the current board grid state.
     */
    public void printBoardGrid() {
        for (AreaState[] column : boardGrid) {
            for (AreaState state : column) {
                switch (state) {
                    case OUTERBORDER:
                        System.out.print("[X]");
                        break;
                    case INNERBORDER:
                        System.out.print("[*]");
                        break;
                    case UNCOVERED:
                        System.out.print("[ ]");
                        break;
                    case FAST:
                        System.out.print("[F]");
                        break;
                    case SLOW:
                        System.out.print("[S]");
                        break;
                    default:
                        break;
                }
            }
            System.out.println();
        }
    }
}
