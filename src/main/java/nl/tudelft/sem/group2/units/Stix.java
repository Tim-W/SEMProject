package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.Logger;
import nl.tudelft.sem.group2.collisions.CollisionInterface;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.logging.Level;

import static nl.tudelft.sem.group2.scenes.GameScene.gridToCanvas;

/**
 * Class which keeps track of the current stix.
 * The stix is the line a cursor is drawing.
 * A stix can be created by holding one of the draw keys on the cursor and start moving into unclaimed territory.
 * When the cursor reaches a border again the stix is completed and the side without the qix will be set to claimed
 * territory.
 * A fuse will spawn on the stix when the cursor is not moving and the fuse will travel on the fuse until it reaches
 * the cursor at which point the player will lose a life and will need to start drawing again.
 */
public class Stix implements CollisionInterface {

    private static final Logger LOGGER = Logger.getLogger();
    private LinkedList<Point> stixCoordinates;

    /**
     * Constructor for stix class.
     */
    public Stix() {
        stixCoordinates = new LinkedList<>();
    }

    /**
     * Empties all the current coordinates out of current stix.
     */
    public void emptyStix() {
        //Empty the current stix
        stixCoordinates = null;
        stixCoordinates = new LinkedList<>();
    }

    /**
     * Checks if stix stix is empty.
     *
     * @return boolean isempty()
     */
    public boolean isStixEmpty() {
        return !(stixCoordinates != null && !stixCoordinates.isEmpty());
    }

    /**
     * Check if stix intersects with qix.
     *
     * @param unit the current unit casted to qix
     * @return if they intersect
     */
    public boolean intersect(Unit unit) {
        if (unit instanceof Qix) {

            Qix qix = (Qix) unit;
            if (!this.isStixEmpty()) {

                Polygon qixP = qix.toPolygon();
                for (Point point : this.getStixCoordinates()) {
                    if (qixP.intersects(point.getX(), point.getY(), 1, 1)) {
                        LOGGER.log(Level.INFO, qix.toString() + " collided with Stix at (" + point.getX()
                                + "," + point.getY() + ")", this.getClass());
                        return true;
                    }
                }
            }
        } else if (unit instanceof Cursor && !this.isStixEmpty()) {
            Rectangle collideeR = unit.toRectangle();
            for (Point point : this.getStixCoordinates()) {
                if (collideeR.intersects(point.getX(), point.getY(), 1, 1)) {

                    LOGGER.log(Level.INFO, unit.toString() + " collided with Stix at (" + point.getX()
                            + "," + point.getY() + ")", this.getClass());
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * draws stix.
     *
     * @param gc     graphicContext of the canvas
     * @param fuse   of the cursor
     * @param isFast boolean isFast of cursor
     */
    public void draw(GraphicsContext gc, Fuse fuse, boolean isFast) {
        boolean foundFuse = false;
        for (Point p : stixCoordinates) {
            if (foundFuse || fuse == null) {
                if (isFast) {
                    gc.setFill(Color.MEDIUMBLUE);
                } else {
                    gc.setFill(Color.DARKRED);
                }
            } else {
                if (fuse.onPoint(p)) {
                    foundFuse = true;
                }
                gc.setFill(Color.GRAY);
            }
            gc.fillRect(gridToCanvas(p.x), gridToCanvas(p.y), 2, 2);
        }
    }

    /**
     * Method that adds a point to the current stix.
     *
     * @param coordinate point that gets added to the stix
     */
    public void addToStix(Point coordinate) {
        stixCoordinates.add(coordinate);
    }

    /**
     *
     * @param point The point that gets checked
     * @return true if this Stix contains the point
     */
    public boolean contains(Point point) {
        return stixCoordinates.contains(point);
    }

    /**
     * @return true if this Stix doens't contain any points
     */
    public boolean isEmpty() {
        return stixCoordinates == null || stixCoordinates.isEmpty();
    }

    /**
     * Getter for the stix.
     *
     * @return the current stix
     */
    public LinkedList<Point> getStixCoordinates() {
        return stixCoordinates;
    }

    /**
     * checks if Point p equals the first point of the coordinates.
     *
     * @param p Point that is checked
     * @return boolean true if equals
     */
    public boolean pointEqualsFirstPoint(Point p) {
        return stixCoordinates.getFirst().equals(p);
    }
}
