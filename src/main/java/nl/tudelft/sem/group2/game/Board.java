package nl.tudelft.sem.group2.game;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.AreaState;
import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.scenes.GameScene;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Fuse;
import nl.tudelft.sem.group2.units.Unit;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import static nl.tudelft.sem.group2.global.Globals.BOARD_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.BOARD_MARGIN;
import static nl.tudelft.sem.group2.global.Globals.BOARD_WIDTH;

/**
 * Contains a set of all units in the current game.
 */
public class Board {

    private static final int MARGIN = 8;
    private Image image;
    private Canvas canvas;
    private Set<Unit> units;
    private GraphicsContext gc;
    private AreaTracker areaTracker;
    private Cursor cursor;

    /**
     * Draws a new board on a canvas.
     *
     * @param canvas - the canvas to draw
     */
    public Board(Canvas canvas) {
        this.units = new HashSet<>();
        this.canvas = canvas;
        gc = canvas.getGraphicsContext2D();
        // BLUE SCREEN IS THE SIZE OF THE BOARD, 300x300
        gc.setFill(Color.BLUE);
        gc.fillRect(0, 0, BOARD_WIDTH + 2 * MARGIN, BOARD_HEIGHT + 2 * MARGIN);
        this.areaTracker = GameScene.getAreaTracker();
        this.cursor = GameScene.getQixCursor();
    }

    /**
     * Transforms grid to canvas coordinates.
     *
     * @param b an x or y coordinate
     * @return the coordinate to draw an image on
     */
    public static int gridToCanvas(int b) {
        return b * 2 + MARGIN - 1;
    }

    /**
     * Sets a background image to be drawn on all claimed areas.
     *
     * @param image the image to draw
     */
    public void setImage(Image image) {
        this.image = image;
        gc.drawImage(image, MARGIN, MARGIN);
    }

    /**
     * @return units of the board
     */
    public Set<Unit> getUnits() {
        return units;
    }

    /**
     * Add a unit.
     * @param unit unit to add
     */
    public void addUnit(Unit unit) {
        if (unit instanceof Fuse) {
            for (Unit unit1 : this.units) {
                if (unit1 instanceof Fuse) {
                    return;
                }
            }
        }
        this.units.add(unit);
    }

    /**
     * Draw all the units on the screen.
     */
    public void draw() {
        gc.clearRect(0, 0, BOARD_WIDTH + 2 * MARGIN, BOARD_HEIGHT + 2 * MARGIN);
        gc.drawImage(image, BOARD_MARGIN, BOARD_MARGIN);
        gc.setFill(Color.WHITE);
        drawUncovered();
        drawBorders();
        drawStixAndFuse();
        for (Unit unit : units) {
            unit.move();
            unit.draw(canvas);
        }
//        drawFastAreas();
//        drawSlowAreas();
    }

    private void drawUncovered() {
        gc.setFill(Color.BLACK);
        for (int i = 0; i < areaTracker.getBoardGrid().length; i++) {
            for (int j = 0; j < areaTracker.getBoardGrid()[i].length; j++) {
                if (areaTracker.getBoardGrid()[i][j] == AreaState.UNCOVERED) {
                    gc.fillRect(gridToCanvas(i), gridToCanvas(j), 2, 2);
                }
            }
        }
    }

    private void drawBorders() {
        gc.setFill(Color.WHITE);
        for (int i = 0; i < areaTracker.getBoardGrid().length; i++) {
            for (int j = 0; j < areaTracker.getBoardGrid()[i].length; j++) {
                if (areaTracker.getBoardGrid()[i][j] == AreaState.OUTERBORDER
                        || areaTracker.getBoardGrid()[i][j] == AreaState.INNERBORDER) {
                    gc.fillRect(gridToCanvas(i), gridToCanvas(j), 2, 2);
                }
            }
        }
    }

    /**
     * Draw current Stix and Fuse on screen.
     */
    private void drawStixAndFuse() {
        boolean foundFuse = true;
        Point fuse = new Point(-1, -1);
        for (Unit unit : units) {
            if (unit instanceof Fuse) {
                foundFuse = false;
                fuse = new Point(unit.getX(), unit.getY());
            }
        }
        for (Point p : GameScene.getStix().getStixCoordinates()) {
            if (!p.equals(GameScene.getStix().getStixCoordinates().getFirst())) {
                if (foundFuse) {
                    if (cursor.isFast()) {
                        gc.setFill(Color.MEDIUMBLUE);
                    } else {
                        gc.setFill(Color.DARKRED);
                    }
                } else {
                    if (p.equals(fuse)) {
                        foundFuse = true;
                        if (cursor.isFast()) {
                            gc.setFill(Color.MEDIUMBLUE);
                        } else {
                            gc.setFill(Color.DARKRED);
                        }
                    } else {
                        gc.setFill(Color.GRAY);
                    }
                }

                gc.fillRect(gridToCanvas(p.x), gridToCanvas(p.y), 2, 2);
            }
        }
    }


    /**
     * If there is a Fuse on the screen, remove it.
     */
    public void removeFuse() {
        Unit removingItem = null;
        for (Unit unit : units) {
            if (unit instanceof Fuse) {
                removingItem = unit;
            }
        }
        if (removingItem != null) {
            units.remove(removingItem);
        }
    }
}