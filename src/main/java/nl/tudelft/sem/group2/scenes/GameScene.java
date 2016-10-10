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
import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.ScoreCounter;
import nl.tudelft.sem.group2.controllers.GameController;
import nl.tudelft.sem.group2.global.Globals;
import nl.tudelft.sem.group2.units.Fuse;
import nl.tudelft.sem.group2.units.Unit;

import java.awt.Point;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import static nl.tudelft.sem.group2.controllers.GameController.getStix;
import static nl.tudelft.sem.group2.global.Globals.BOARD_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.BOARD_MARGIN;
import static nl.tudelft.sem.group2.global.Globals.BOARD_WIDTH;


/**
 * GameScene contains all the information about the current game.
 */
public class GameScene extends Scene {

    private static final int LAST_IMAGE = 5;
    private static final int FIRST_IMAGE = 2;

    private static Label messageLabel;
    private static VBox messageBox;
    private GameController gameController;

    private static ScoreScene scoreScene;

    private static final int MARGIN = 8;
    private static Canvas canvas;
    private static Set<Unit> units;
    private static GraphicsContext gc;
    private static AreaTracker areaTracker;
    private static Image image;

    // TODO implement life system
    // private int lifes;

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
        units = new HashSet<>();
        gameController = new GameController();
        initializeCanvas();
        // Get area tracker for area draw methods
        areaTracker = GameController.getAreaTracker();
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
        setImage(new Image("/images/" + image + ".png", BOARD_WIDTH, BOARD_HEIGHT, false, false));
        // Draw the board
        draw();
        // Initialize key pressed an key released actions
        registerKeyPressedHandler();
        registerKeyReleasedHandler();
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
        gc.fillRect(0, 0, BOARD_WIDTH + 2 * MARGIN, BOARD_HEIGHT + 2 * MARGIN);
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
        setOnKeyReleased((KeyEvent e) -> gameController.keyReleased(e));
    }

    /**
     * When a key is pressed send event to GameController.
     */
    private void registerKeyPressedHandler() {
        setOnKeyPressed((KeyEvent e) -> gameController.keyPressed(e));
    }

    private void createScoreScene() {
        Group group = new Group();
        scoreScene = new ScoreScene(group, Globals.GAME_WIDTH, Globals.SCORESCENE_POSITION_Y);

        // TODO shift this to a game class and save/load score
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
     * Transforms grid to canvas coordinates.
     *
     * @param b an x or y coordinate
     * @return the coordinate to draw an image on
     */
    public static int gridToCanvas(int b) {
        return b * 2 + MARGIN - 1;
    }

    /**
     * @return units of the board
     */
    public static Set<Unit> getUnits() {
        return units;
    }

    /**
     * Add a unit.
     * @param unit unit to add
     */
    public static void addUnit(Unit unit) {
        for (Unit u :
                getUnits()) {
            if(unit.getClass().equals(u.getClass())){
                return;
            }
        }
        units.add(unit);
    }

    /**
     * Draw all the units on the screen.
     */
    public static void draw() {
        // gc.setFill(Color.BLACK);
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
    }

    private static void drawUncovered() {
        gc.setFill(Color.BLACK);
        for (int i = 0; i < areaTracker.getBoardGrid().length; i++) {
            for (int j = 0; j < areaTracker.getBoardGrid()[i].length; j++) {
                if (areaTracker.getBoardGrid()[i][j] == AreaState.UNCOVERED) {
                    gc.fillRect(gridToCanvas(i), gridToCanvas(j), 2, 2);
                }
            }
        }
    }

    private static void drawBorders() {
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
    private static void drawStixAndFuse() {
        boolean foundFuse = false;
        Point fuse = new Point(-1, -1);
        for (Unit unit : units) {
            if (unit instanceof Fuse) {
                foundFuse = true;
                fuse = new Point(unit.getX(), unit.getY());
            }
        }
        LinkedList<Point> stixCoordinates = getStix().getStixCoordinates();
        for (int i = 1; i < stixCoordinates.size(); i++) {
            if (stixCoordinates.get(i).equals(fuse)) {
                foundFuse = false;
            }
            if (!foundFuse) {
                if (GameController.getCursor().isFast()) {
                    gc.setFill(Color.MEDIUMBLUE);
                } else {
                    gc.setFill(Color.DARKRED);
                }
            } else {
                gc.setFill(Color.GRAY);
            }
            gc.fillRect(gridToCanvas(stixCoordinates.get(i).x), gridToCanvas(stixCoordinates.get(i).y), 2, 2);
        }
    }

    /**
     * If there is a Fuse on the screen, remove it.
     */
    public static void removeFuse() {
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


    /**
     * Set the label for the message in the middle of the screen.
     * @param string string which the label should be
     */
    public static void setMessageLabel(String string) {
        GameScene.messageLabel.setText(string);
    }

    /**
     * Alters x-position of message on screen.
     * @param position new x-position
     */
    public static void setMessageBoxLayoutX(int position) {
        GameScene.messageBox.setLayoutX(position);
    }

    /**
     * Update the info on the scorescene with actual info from scorecounter.
     * @param scoreCounter scorecounter from GameController.
     */
    public static void updateScorescene(ScoreCounter scoreCounter) {
        scoreScene.setScore(scoreCounter.getTotalScore());
        scoreScene.setClaimedPercentage((int) (scoreCounter.getTotalPercentage() * 100));
    }

    /**
     * used only for testing
     * @return
     */
    public static GraphicsContext getGc() {
        return gc;
    }
}
