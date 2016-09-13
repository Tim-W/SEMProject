package nl.tudelft.sem.group2;

import nl.tudelft.sem.group2.scenes.GameScene;

import java.awt.*;
import java.util.LinkedList;

/**
 * Tracks the area of the current level, of which pixels are covered by the player.e
 */
public class AreaTracker {

    private LinkedList<Point> stix = new LinkedList<Point>();

    private AreaState[][] boardGrid = new AreaState[LaunchApp.getGridWidth()][LaunchApp.getGridHeight()];

    private LinkedList<Point> area1, area2, newArea;

    private ScoreCounter scoreCounter;

    /**
     * Constructor for the AreaTracker class
     * The constructor sets all the grid points to border and the rest to uncovered
     */
    public AreaTracker() {
        for (int i=0;i<boardGrid.length;i++) {
            for (int j=0;j<boardGrid[i].length;j++) {
                //If the current row is the first row set all grid points border on that row
                if (j==0) boardGrid[j][i] = AreaState.BORDER;
                //If the current row is the bottom row set all grid points border on that row
                else if (j==boardGrid[i].length-1) boardGrid[j][i] = AreaState.BORDER;
                //If current column is the last column set the grid point on that column and the current row border
                else if (i==0) boardGrid[j][i] = AreaState.BORDER;
                //If the current column is the last column set the grid point on that column and the current row border
                else if (i==boardGrid.length-1) boardGrid[j][i] = AreaState.BORDER;
                //If the current point is none of the above set that point to uncovered
                else boardGrid[j][i] = AreaState.UNCOVERED;
            }
        }
    }

    /**
     * Method that updates the grid when a stix is completed
     * @param qixCoordinates current qix coordinates
     * @param fastArea tells if stix was created fast or slow (double points if slow)
     */
    public void calculateNewArea (Point qixCoordinates, boolean fastArea) {
        //Set all the points from the current stix to border points on the grid
        for (Point current : stix) {
            boardGrid[(int) current.getX()][(int) current.getY()] = AreaState.BORDER;
        }

        //Obtain first and second point from the stix to determine beginposition for the floodfill algorithm
        Point start = stix.getFirst();
        Point dir = stix.get(1);

        //Instantiate the two temporary area trackers, these linkedlists accumulate all the points on one side of the stix
        //When the floodfill algorithm finds a qix however the linkedlist is set to null
        area1 = new LinkedList<Point>();
        area2 = new LinkedList<Point>();

        //Check in which direction the stix first started to move
        if (start.getX() != dir.getX()) {
            //If stix was first moving in X direction get points above and under the first stix point and start the floodfill algorithm from there
            Point beginPoint1 = new Point((int) dir.getX(), (int) dir.getY()-1);
            floodFill(beginPoint1, qixCoordinates, AreaState.UNCOVERED, true);
            Point beginPoint2 = new Point((int) dir.getX(), (int) dir.getY()+1);
            floodFill(beginPoint2, qixCoordinates, AreaState.UNCOVERED, true);
        }
        else if (start.getY() != dir.getY()) {
            //If stix was first moving in Y direction get points left and right the first stix point and start the floodfill algorithm from there
            Point beginPoint1 = new Point((int) dir.getX()-1, (int) dir.getY());
            floodFill(beginPoint1, qixCoordinates, AreaState.UNCOVERED, false);
            Point beginPoint2 = new Point((int) dir.getX()+1, (int) dir.getY());
            floodFill(beginPoint2, qixCoordinates, AreaState.UNCOVERED, false);
        }

        //Check in which of the two area the qix was found and set the other one to the newly created area
        if (area1 == null) { newArea = area2; }
        else if (area2 == null) {newArea = area1; }

        scoreCounter = GameScene.getScoreCounter();
        //Update score and percentage with newly created area, thefore it's needed to know the stix was created fast or slow
        scoreCounter.updateScore(newArea.size()+stix.size(), fastArea);

        //Update the grid with the newly created area
        for (Point current : newArea) {
            if (fastArea) { boardGrid[(int) current.getX()][(int) current.getY()] = AreaState.FAST; }
            else { boardGrid[(int) current.getX()][(int) current.getY()] = AreaState.SLOW; }
        }

        //Reset the temporary area tracker
        area1 = null;
        area2 = null;
    }

    /**
     * Floodfill algorithm, this algorithm checks one point if it's a point that gets added to new area and if the qix is found and adds that to one of the two area trackers
     * @param pointToCheck a Point which is either the starting point determined in the calculateNewArea method or a recursively determined point
     * @param qixCoorinates the current qix coordinates
     * @param chosenState begin state of the area that gets added to the new area, practically always AreaStates.UNCOVERED
     * @param addToArea1 boolean that keeps thrack of which temporary AreaTracker to use.
     */
    public void floodFill (Point pointToCheck, Point qixCoorinates, AreaState chosenState, boolean addToArea1) {
        // Check if the current point on the grid is the chosen beginstate
        if (boardGrid[(int)pointToCheck.getX()][(int)pointToCheck.getY()]==chosenState) {
            // Check if that point is the coordinate of the qix
            if (pointToCheck.equals(qixCoorinates)) {
                // If that point was the coordinate of the qix set the temporary area tracker that is currently in use to null
                if (addToArea1) { area1=null; }
                else { area2=null; }
            }
            else {
                // If that point was not the coordinate of the qix add that point to the right temporary area tracker
                if (addToArea1) { area1.add(pointToCheck); }
                else { area2.add(pointToCheck); }
                // Check all the four neighbours of the current point recursively
                Point point1 = new Point((int) pointToCheck.getX(), (int) pointToCheck.getY()-1);
                Point point2 = new Point((int) pointToCheck.getX(), (int) pointToCheck.getY()+1);
                Point point3 = new Point((int) pointToCheck.getX()-1, (int) pointToCheck.getY());
                Point point4 = new Point((int) pointToCheck.getX()+1, (int) pointToCheck.getY());
                floodFill(point1, qixCoorinates, chosenState, addToArea1);
                floodFill(point2, qixCoorinates, chosenState, addToArea1);
                floodFill(point3, qixCoorinates, chosenState, addToArea1);
                floodFill(point4, qixCoorinates, chosenState, addToArea1);
            }
        }
    }

    /**
     * Method that adds a point to the current stix
     * @param coordinates point that gets added to the stix
     */
    public void addToStix (Point coordinates) {
        stix.add(coordinates);
    }

    /**
     * Getter for the stix
     * @return the current stix
     */
    public LinkedList<Point> getStix() {
        return stix;
    }

    /**
     * Getter for the boardGrid
     * @return the boardGrid
     */
    public AreaState[][] getBoardGrid() {
        return boardGrid;
    }
}
