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

    private areaStates[][] totalArea = new areaStates[LaunchApp.getBoardWidth()][LaunchApp.getBoardHeight()];

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

    public void calculateNewArea(Point qixCoordinates) {
        Point previous = stix.getFirst();
        Point current;
        boolean inXDirection;
        //TODO create to areaTracker units dingen je weet zelf van die die de area van de current stix berekenen
        for (int i=0;i<stix.size();i++){
            current = stix.get(i);
            if (i!=0) {
                //Check if current stix straight is moving in x direction
                if (previous.getX()!=current.getX()) {
                    //TODO check both y-directions for border  include all coordinates that are not border and if qix coordinate found delete that area and only count other one
                }
                //TODO do same for y-directions moving
            }
        }
    }

    public void addToStix(Point coordinates) {
        stix.add(coordinates);
    }

    public LinkedList<Point> getStix() {
        return stix;
    }

    public areaStates[][] getTotalArea() {
        return totalArea;
    }
}
