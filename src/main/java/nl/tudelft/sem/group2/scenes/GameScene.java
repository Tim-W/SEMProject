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
import nl.tudelft.sem.group2.units.Unit;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * GameScene contains all the information about the current game.
 */
public class GameScene extends Scene {

    private static final int LAST_IMAGE = 5;
    private static final int FIRST_IMAGE = 2;
    private static final int MARGIN = 8;
    private static Label messageLabel;
    private static VBox messageBox;
    private static ScoreScene scoreScene;
    private static Canvas canvas;
    private static GraphicsContext gc;
    private static Image image;

    /**
     * Create a new GameScene.
     * Sets up all listeners and default objects to play the game
     *
     * @param root  the root scene this scene is built on
     * @param color background color of the scene
     */
    public GameScene(final Group root, Color color) {
        super(root, color);
        // Initialize units set because it's necessary in GameController
        // Temporary until CollisionHandler is merged into this

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
        createScoreScene();
        root.getChildren().add(scoreScene);
        root.getChildren().add(canvas);
        root.getChildren().add(bottomBorder);
        root.getChildren().add(messageBox);
        Random random = new Random();
        //Choose random image
        int image = random.nextInt(LAST_IMAGE - FIRST_IMAGE) + FIRST_IMAGE;
        setImage(new Image("/images/" + image + ".png", Globals.BOARD_WIDTH, Globals.BOARD_HEIGHT, false, false));
        //Draw black rectangle over image to avoid spoilers
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, Globals.BOARD_WIDTH + 2 * Globals.BOARD_MARGIN, Globals.BOARD_HEIGHT + 2 * Globals.BOARD_MARGIN);
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
    public static ScoreScene getScoreScene() {
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
        // BLUE SCREEN IS THE SIZE OF THE BOARD, 300x300
        gc.setFill(Color.BLUE);
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

    private void createScoreScene() {
        Group group = new Group();
        scoreScene = new ScoreScene(group, Globals.GAME_WIDTH, Globals.SCORESCENE_POSITION_Y);

        scoreScene.setScore(0);
        scoreScene.setClaimedPercentage(0);
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
     */
    public void draw() {
        // gc.setFill(Color.BLACK);
        gc.clearRect(0, 0, Globals.BOARD_WIDTH + 2 * MARGIN, Globals.BOARD_HEIGHT + 2 * MARGIN);
        gc.drawImage(image, Globals.BOARD_MARGIN, Globals.BOARD_MARGIN);
        gc.setFill(Color.WHITE);
        drawUncovered();
        drawBorders();
        drawStixAndFuse();
        for (Unit unit : GameController.getInstance().getUnits()) {
            unit.move();
            unit.draw(canvas);
        }
    }

    private void drawUncovered() {
        gc.setFill(Color.BLACK);
        for (int i = 0; i < GameController.getInstance().getAreaTracker().getBoardGrid().length; i++) {
            for (int j = 0; j < GameController.getInstance().getAreaTracker().getBoardGrid()[i].length; j++) {
                if (GameController.getInstance().getAreaTracker().getBoardGrid()[i][j] == AreaState.UNCOVERED) {
                    gc.fillRect(gridToCanvas(i), gridToCanvas(j), 2, 2);
                }
            }
        }
    }

    private void drawBorders() {
        gc.setFill(Color.WHITE);
        for (int i = 0; i < GameController.getInstance().getAreaTracker().getBoardGrid().length; i++) {
            for (int j = 0; j < GameController.getInstance().getAreaTracker().getBoardGrid()[i].length; j++) {
                if (GameController.getInstance().getAreaTracker().getBoardGrid()[i][j] == AreaState.OUTERBORDER
                        || GameController.getInstance().getAreaTracker().getBoardGrid()[i][j] == AreaState.INNERBORDER) {
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

        List<Cursor> cursorList = new ArrayList<Cursor>();


        for (Unit unit : GameController.getInstance().getUnits()) {
            if (unit instanceof Cursor) {
                cursorList.add(((Cursor) unit));
            }
        }
        for (Cursor cursor : cursorList) {
            if (cursor.getFuse() != null) {
                fuse = new Point(cursor.getFuse().getX(), (cursor.getFuse().getY()));
                foundFuse = false;
            }
            for (Point p : cursor.getStix().getStixCoordinates()) {
                if (!p.equals(cursor.getStix().getStixCoordinates().getFirst())) {
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
            if (cursor.getFuse() != null) {
                cursor.getFuse().move();
                cursor.getFuse().draw(canvas);
            }
            foundFuse = true;
            fuse = new Point(-1, -1);
        }
    }

    /**
     * Set the label for the message in the middle of the screen.
     *
     * @param string string which the label should be
     */
    public void setMessageLabel(String string) {
        GameScene.messageLabel.setText(string);
    }

    /**
     * Alters x-position of message on screen.
     *
     * @param position new x-position
     */
    public void setMessageBoxLayoutX(int position) {
        GameScene.messageBox.setLayoutX(position);
    }
}