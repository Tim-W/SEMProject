package nl.tudelft.sem.group2.scenes;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.AreaState;
import nl.tudelft.sem.group2.controllers.GameController;
import nl.tudelft.sem.group2.global.Globals;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Fuse;
import nl.tudelft.sem.group2.units.Unit;

import java.awt.Point;
import java.util.Random;
import java.util.Set;


/**
 * GameScene contains all the information about the current game.
 */
public class GameScene extends Scene {

    private static final int LAST_IMAGE = 5;
    private static final int FIRST_IMAGE = 2;
    private static final int MARGIN = 8;
    private ScoreScene scoreScene;
    private Label messageLabel;
    private VBox messageBox;
    private Canvas canvas;
    private GraphicsContext gc;
    private Image image;

    /**
     * Create a new GameScene.
     * Sets up all listeners and default objects to play the game
     *
     * @param root       the root scene this scene is built on
     * @param color      background color of the scene
     * @param scoreScene the score scene
     */
    public GameScene(final Group root, Color color, ScoreScene scoreScene) {
        super(root, color);
        this.scoreScene = scoreScene;
        initializeCanvas();
        // Initialize label in middle of screen to display start message
        messageLabel = new Label("Press SPACE to begin!");
        final int messageBoxSpacing = 10;
        messageBox = new VBox(messageBoxSpacing);
        addMessageBox();
        // Hacky way to create black bottom border
        Canvas bottomBorder = new Canvas(Globals.BOARD_WIDTH, Globals.BORDER_BOTTOM_HEIGHT);
        bottomBorder.setLayoutY(Globals.BORDER_BOTTOM_POSITION_Y);
        // Add scene elements to root group
        if (scoreScene != null) {
            root.getChildren().add(scoreScene);
        }
        root.getChildren().add(canvas);
        root.getChildren().add(bottomBorder);
        root.getChildren().add(messageBox);
        Random random = new Random();
        //Choose random image
        int image = random.nextInt(LAST_IMAGE - FIRST_IMAGE) + FIRST_IMAGE;
        setImage(new Image(
                "/images/background-image-" + image + ".png",
                Globals.BOARD_WIDTH, Globals.BOARD_HEIGHT,
                false,
                false));
        //Draw black rectangle over image to avoid spoilers
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, Globals.BOARD_WIDTH + 2 * Globals.BOARD_MARGIN,
                Globals.BOARD_HEIGHT + 2 * Globals.BOARD_MARGIN);
        // Initialize key pressed an key released actions
        registerKeyPressedHandler();
        registerKeyReleasedHandler();
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
     * @return the scorescene
     */
    public ScoreScene getScoreScene() {
        return scoreScene;
    }

    /**
     * Initializes canvas and gc.
     */
    public void initializeCanvas() {
        // Initialize canvas
        canvas = new Canvas(Globals.BOARD_WIDTH + 2 * Globals.BOARD_MARGIN,
                Globals.BOARD_HEIGHT + 2 * Globals.BOARD_MARGIN);
        canvas.setLayoutX(Globals.GAME_OFFSET_X);
        canvas.setLayoutY(Globals.GAME_OFFSET_Y);

        // Obtain GraphicsContext2d from canvas
        gc = canvas.getGraphicsContext2D();
        gc.fillRect(0, 0, Globals.BOARD_WIDTH + 2 * MARGIN, Globals.BOARD_HEIGHT + 2 * MARGIN);
    }

    /**
     * Sets a background image to be drawn on all claimed areas.
     *
     * @param img the image to draw
     */
    public void setImage(Image img) {
        image = img;
        gc.drawImage(image, MARGIN, MARGIN);
    }

    /**
     * When a key is released send event to GameController.
     */
    private void registerKeyReleasedHandler() {
        setOnKeyReleased((KeyEvent e) -> GameController.getInstance().keyReleased(e));
    }

    /**
     * When a key is pressed send event to GameController.
     */
    private void registerKeyPressedHandler() {
        setOnKeyPressed((KeyEvent e) -> GameController.getInstance().keyPressed(e));
    }


    private void addMessageBox() {
        // Messagebox&label for displaying start and end messages
        messageBox.setAlignment(Pos.CENTER);
        messageBox.getChildren().add(messageLabel);
        messageBox.setStyle("-fx-background-color: #000000;");
        messageBox.setLayoutX(Globals.MESSAGEBOX_POSITION_X);
        messageBox.setLayoutY(Globals.MESSAGEBOX_POSITION_Y);
        messageLabel.setStyle("-fx-font-size: 24px;");
        messageLabel.setTextFill(Color.YELLOW);
    }

    /**
     * Draw all the units on the screen.
     *
     * @param units     units of the game
     * @param boardGrid boardGrid of the game
     */
    public void draw(Set<Unit> units, AreaState[][] boardGrid) {
        // gc.setFill(Color.BLACK);
        gc.clearRect(0, 0, Globals.BOARD_WIDTH + 2 * MARGIN, Globals.BOARD_HEIGHT + 2 * MARGIN);
        gc.drawImage(image, Globals.BOARD_MARGIN, Globals.BOARD_MARGIN);
        gc.setFill(Color.WHITE);
        drawUncovered(boardGrid);
        drawBorders(boardGrid);
        if (units != null) {
            drawStixAndFuse(units);
            for (Unit unit : units) {
                unit.draw(canvas);
            }
        }
    }

    /**
     * move all units.     *
     *
     * @param units units of the game
     */
    public void move(Set<Unit> units) {
        for (Unit unit : units) {
            unit.move();
        }
    }

    private void drawUncovered() {
        gc.setFill(Color.BLACK);
        for (int i = 0; i < boardGrid.length; i++) {
            for (int j = 0; j < boardGrid[i].length; j++) {
                if (boardGrid[i][j] == AreaState.UNCOVERED) {
                    gc.fillRect(gridToCanvas(i), gridToCanvas(j), 2, 2);
                }
            }
        }
    }

    private void drawBorders(AreaState[][] boardGrid) {
        gc.setFill(Color.WHITE);
        for (int i = 0; i < boardGrid.length; i++) {
            for (int j = 0; j < boardGrid[i].length; j++) {
                if (boardGrid[i][j] == AreaState.OUTERBORDER
                        || boardGrid[i][j] == AreaState.INNERBORDER) {
                    gc.fillRect(gridToCanvas(i), gridToCanvas(j), 2, 2);
                }
            }
        }
    }

    /**
     * Draw current Stix and Fuse on screen.
     */
    private void drawStixAndFuse(Set<Unit> units) {
        if (units == null) {
            return;
        }
        boolean foundFuse = false;
        Point fusePoint = new Point(-1, -1);
        for (Unit unit : units) {
            if (unit instanceof Cursor) {
                Cursor cursor = (Cursor) unit;
                Fuse fuse = cursor.getFuseHandler().getFuse();
                if (fuse != null) {
                    fusePoint = new Point(fuse.getX(), fuse.getY());
                    foundFuse = true;
                }
                for (Point p : cursor.getStix().getStixCoordinates()) {
                    if (!cursor.getStix().pointEqualsFirstPoint(p)) {
                        if (!foundFuse) {
                            if (cursor.isFast()) {
                                gc.setFill(Color.MEDIUMBLUE);
                            } else {
                                gc.setFill(Color.DARKRED);
                            }
                        } else {
                            if (p.equals(fusePoint)) {
                                foundFuse = false;
                            }
                            gc.setFill(Color.GRAY);
                        }
                        gc.fillRect(gridToCanvas(p.x), gridToCanvas(p.y), 2, 2);
                    }
                }
                if (fuse != null) {
                    fuse.move();
                    fuse.draw(canvas);
                }
                foundFuse = false;
                fusePoint = new Point(-1, -1);
            }
        }
    }

    /**
     * Set the label for the message in the middle of the screen.
     *
     * @param string string which the label should be
     */
    public void setMessage(String string) {
        messageLabel.setText(string);
    }

    /**
     * Alters x-position of message on screen.
     *
     * @param position new x-position
     */
    public void setMessageBoxLayoutX(int position) {
        messageBox.setLayoutX(position);
    }

    /**
     * only for testing.
     *
     * @param gc GraphicsContext
     */
    public void setGc(GraphicsContext gc) {
        this.gc = gc;
    }
}

