package main.java.nl.tudelft.sem.group2;

import java.awt.*;
import java.util.LinkedList;

/**
 * Enum for all the posible states of a pixel on the board
 */
enum areaStates {
    UNCOVERED, FAST, SLOW, BORDER
}

/**
 * Tracks the area of the current level, of which pixels are covered by the player.e
 */
public class areaTracker {

    private LinkedList<Point> stix = new LinkedList<Point>();

    private areaStates[][] totalArea = new areaStates[LaunchApp.getGridWidth()][LaunchApp.getGridHeight()];

    private LinkedList<Point> area1, area2, newArea;

    private ScoreCounter scoreCounter = new ScoreCounter();

    public areaTracker () {
        for (int i=0;i<totalArea.length;i++) {
            for (int j=0;j<totalArea[i].length;j++) {
                if (j==0) totalArea[j][i] = areaStates.BORDER;
                else if (j==totalArea[i].length-1) totalArea[j][i] = areaStates.BORDER;
                else if (i==0) totalArea[j][i] = areaStates.BORDER;
                else if (i==totalArea.length-1) totalArea[j][i] = areaStates.BORDER;
                if (totalArea[j][i] == areaStates.BORDER) System.out.print("[*]");
                else System.out.print("[ ]");
            }
            System.out.println();
        }
    }

    public void calculateNewArea (Point qixCoordinates, boolean fastArea) {
        for (Point current : stix) {
            totalArea[(int) current.getX()][(int) current.getY()] = areaStates.BORDER;
        }

        Point start = stix.getFirst();
        Point dir = stix.get(1);

        area1 = new LinkedList<Point>();
        area2 = new LinkedList<Point>();

        if (start.getX() != dir.getX()) {
            Point beginPoint1 = new Point((int) dir.getX(), (int) dir.getY()-1);
            floodFill(beginPoint1, qixCoordinates, areaStates.UNCOVERED, true);
            Point beginPoint2 = new Point((int) dir.getX(), (int) dir.getY()+1);
            floodFill(beginPoint2, qixCoordinates, areaStates.UNCOVERED, true);
        }
        else if (start.getY() != dir.getY()) {
            Point beginPoint1 = new Point((int) dir.getX()-1, (int) dir.getY());
            floodFill(beginPoint1, qixCoordinates, areaStates.UNCOVERED, false);
            Point beginPoint2 = new Point((int) dir.getX()+1, (int) dir.getY());
            floodFill(beginPoint2, qixCoordinates, areaStates.UNCOVERED, false);
        }

        if (area1 == null) { newArea = area2; }
        else if (area2 == null) {newArea = area1; }

        scoreCounter.updateScore(newArea.size()+stix.size(), fastArea);

        for (Point current : newArea) {
            if (fastArea) { totalArea[(int) current.getX()][(int) current.getY()] = areaStates.FAST; }
            else { totalArea[(int) current.getX()][(int) current.getY()] = areaStates.SLOW; }
        }
    }

    public void floodFill (Point pointToCheck, Point qixCoorinates, areaStates chosenState, boolean addToArea1) {
        if (totalArea[(int)pointToCheck.getX()][(int)pointToCheck.getY()]==chosenState) {
            if (pointToCheck.equals(qixCoorinates)) {
                if (addToArea1) { area1=null; }
                else { area2=null; }
            }
            else {
                if (addToArea1) { area1.add(pointToCheck); }
                else { area2.add(pointToCheck); }
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

    public void addToStix (Point coordinates) {
        stix.add(coordinates);
    }

    public LinkedList<Point> getStix() {
        return stix;
    }

    public areaStates[][] getTotalArea() {
        return totalArea;
    }
}
