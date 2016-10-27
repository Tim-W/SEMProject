package nl.tudelft.sem.group2.board;

import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.Logger;
import nl.tudelft.sem.group2.ScoreCounter;
import nl.tudelft.sem.group2.units.Stix;

import java.awt.Point;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;

/**
 * Tracks the area of the current level, of which pixels are covered by the player.
 */
public class AreaTracker {

    private static final Logger LOGGER = Logger.getLogger();
    private static volatile AreaTracker instance;

    private BoardGrid grid;
    private Stack<Coordinate> visiting = new Stack<>();
    private LinkedList<Coordinate> area1, area2, border1, border2, newBorder, newArea;
    private Set<Point> visited;
    private boolean foundQix;

    /**
     * Constructor for the AreaTracker class.
     * The constructor sets all the grid points to border and the rest to uncovered
     */
    public AreaTracker() {

    }

    /**
     * Custom constructor mainly created for testing purposes.
     *
     * @param width  width of the boardGrid
     * @param height height of the boardGrid
     */
    public AreaTracker(int width, int height) {
    }

    /**
     * Getter for the logger this is a singleton class so everywhere the logger is used it is the same instance
     * This method allows getting of that instance and instantiates it when it is not instantiated yet.
     *
     * @return the only one instance of Logger.
     */
    public static AreaTracker getInstance() {
        if (instance == null) {
            // Put lock on class since it we do not want to instantiate it twice
            synchronized (AreaTracker.class) {
                // Check if logger is in the meanwhile not already instantiated.
                if (instance == null) {
                    instance = new AreaTracker();
                }
            }
        }
        return instance;
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
    public synchronized void calculateNewArea(Coordinate qixCoordinates, boolean fastArea, Stix stix, BoardGrid grid, ScoreCounter scoreCounter) {
        this.grid = grid;
        setOuterBorders(stix);
        // Obtain first and second point from the stix to determine
        // beginposition for the floodfill algorithm
        Coordinate start = stix.getStixCoordinates().getFirst();
        Coordinate dir = stix.getStixCoordinates().get(1);
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
        for (Coordinate current : newArea) {
            if (fastArea) {
                grid.setState(current, AreaState.FAST);
            } else {
                grid.setState(current, AreaState.SLOW);
            }
        }
        //Update the grid with the new inner borders
        for (Coordinate current : newBorder) {
            grid.setState(current, AreaState.INNERBORDER);
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
        for (Coordinate current : stix.getStixCoordinates()) {
            grid.setState(current, AreaState.OUTERBORDER);
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

    private void checkDirections(Coordinate qixCoordinates, Coordinate start, Coordinate dir, Stix stix) {
        //Check in which direction the stix first started to move
        if (start.getX() != dir.getX() || start.getY() != dir.getY()) {
            //If stix was first moving in X direction get points above and under the first stix point,
            // start the floodfill algorithm from there
            Coordinate beginPoint1 = new Coordinate((int) dir.getX(), (int) dir.getY() - 1);
            Coordinate beginPoint2 = new Coordinate((int) dir.getX(), (int) dir.getY() + 1);
            if (start.getY() != dir.getY()) {
                //If stix was first moving in Y direction get points left and right the first stix point,
                // start the floodfill algorithm from there
                beginPoint1 = new Coordinate((int) dir.getX() - 1, (int) dir.getY());
                beginPoint2 = new Coordinate((int) dir.getX() + 1, (int) dir.getY());
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
     * @param pointToCheck   The first point to begin checking if it has to be added to area/border
     *                       or if the qix is on that pint.
     * @param qixCoordinates The coordinates of the qix.
     * @param chosenState    The state of points which get added to the new area.
     * @param addToArea1     Boolean which describes if points should be added to area 1 or 2 and border 1 or 2.
     * @param stix           current stix to use
     */
    private void floodFill(Coordinate pointToCheck, Point qixCoordinates, AreaState chosenState,
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
    private void floodFill(Point qixCoorinates, AreaState chosenState, boolean addToArea1, Stix stix) {
        Coordinate pointToCheck = visiting.pop();
        if (foundQix) {
            return;
        }
        // Check if the current point on the grid is the chosen beginstate
        if (chosenState.equals(grid.getState(pointToCheck))) {
            // Check if that point is the coordinate of the qix
            if (pointToCheck.equals(qixCoorinates)) {
                hitQix(addToArea1);
            } else {
                // If that point was not the coordinate of the qix,
                // add that point to the right temporary area tracker
                addPointToAreaTracker(addToArea1, pointToCheck);
            }
        } else if (grid.isOuterborder(pointToCheck)
                && !stix.getStixCoordinates().contains(pointToCheck)) {
            if (addToArea1) {
                border1.add(pointToCheck);
            } else {
                border2.add(pointToCheck);
            }
            visited.add(pointToCheck);
        } else if (grid.isInnerborder(pointToCheck)) {
            visited.add(pointToCheck);
        }
    }

    private void addPointToAreaTracker(boolean addToArea1, Coordinate pointToCheck) {
        if (addToArea1) {
            area1.add(pointToCheck);
        } else {
            area2.add(pointToCheck);
        }
        visited.add(pointToCheck);
        // Check all the four neighbours of the current point recursively
        Coordinate[] points = new Coordinate[4];
        points[0] = new Coordinate((int) pointToCheck.getX(), (int) pointToCheck.getY() - 1);
        points[1] = new Coordinate((int) pointToCheck.getX(), (int) pointToCheck.getY() + 1);
        points[2] = new Coordinate((int) pointToCheck.getX() - 1, (int) pointToCheck.getY());
        points[3] = new Coordinate((int) pointToCheck.getX() + 1, (int) pointToCheck.getY());
        for (Coordinate point : points) {
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




}
