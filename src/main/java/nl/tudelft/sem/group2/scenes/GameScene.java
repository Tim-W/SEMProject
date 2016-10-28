package nl.tudelft.sem.group2.scenes;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.board.BoardGrid;
import nl.tudelft.sem.group2.controllers.GameController;
import nl.tudelft.sem.group2.global.Globals;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Unit;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


/**
 * GameScene contains all the information about the current game.
 */
public class GameScene extends Scene {

    private static final int LAST_IMAGE = 5;
    private static final int FIRST_IMAGE = 2;
    private static final int MARGIN = 8;
    private static ScoreScene scoreScene;
    private Label messageLabel;
    private VBox messageBox;
    private Canvas canvas;
    private GraphicsContext gc;
    private Image image;

    /**
     * Create a new GameScene.
     * Sets up all listeners and default objects to play the game
     *
     * @param root  the root scene this scene is built on
     * @param color background color of the scene
     */
    public GameScene(final Group root, Color color) {
        super(root, color);

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

    private void createScoreScene() {
        Group group = new Group();
        scoreScene = new ScoreScene(group, Globals.GAME_WIDTH, Globals.SCORESCENE_POSITION_Y);
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
        if (GameController.getInstance().getUnits() != null) {
            drawStixAndFuse();
            for (Unit unit : GameController.getInstance().getUnits()) {
                unit.draw(canvas);
            }
            applyEffect();
        }
    }

    /**
     * move all units.
     */
    public void move() {
        if (GameController.getInstance().getUnits() != null) {
            for (Unit unit : GameController.getInstance().getUnits()) {
                unit.move();
            }
        }
    }

    private void applyEffect() {
        List<Cursor> cursors = GameController.getInstance().getCursors();
        for (Cursor cursor : cursors) {
            switch (cursor.getPowerupHandler().getCurrentPowerup()) {
                case EAT:
                    gc.applyEffect(new ColorAdjust(1, 0, 0, 0));
                    break;
                case SPEED:
                    gc.applyEffect(new ColorAdjust(0, Globals.HALF, 0, 0));
                    break;
                default:
                    break;
            }

        }
    }

    private void drawUncovered() {
        gc.setFill(Color.BLACK);
        for (int i = 0; i < BoardGrid.getInstance().getWidth(); i++) {
            for (int j = 0; j < BoardGrid.getInstance().getHeight(); j++) {
                if (BoardGrid.getInstance().isUncovered(i, j)) {
                    gc.fillRect(gridToCanvas(i), gridToCanvas(j), 2, 2);
                }
            }
        }
    }

    private void drawBorders() {
        gc.setFill(Color.WHITE);
        BoardGrid grid = BoardGrid.getInstance();
        for (int i = 0; i < grid.getWidth(); i++) {
            for (int j = 0; j < grid.getHeight(); j++) {
                if (grid.isOuterborder(i, j) || grid.isInnerborder(i, j)) {
                    gc.fillRect(gridToCanvas(i), gridToCanvas(j), 2, 2);
                }
            }
        }
    }

    /**
     * Draw current Stix and Fuse on screen.
     */
    private void drawStixAndFuse() {
        boolean foundFuse = false;
        Point fuse = new Point(-1, -1);
        List<Cursor> cursorList = new ArrayList<>();
        if (GameController.getInstance().getUnits() == null) {
            return;
        }
        cursorList.addAll(GameController.getInstance().getUnits().stream().
                filter(unit -> unit instanceof Cursor).map(unit -> (Cursor) unit).collect(Collectors.toList()));
        for (Cursor cursor : cursorList) {
            if (cursor.getFuseHandler().getFuse() != null) {
                fuse = new Point(cursor.getFuseHandler().getFuse().getIntX(), cursor.getFuseHandler().getFuse().getIntY());
                foundFuse = true;
            }
            for (Point p : cursor.getStix().getStixCoordinates()) {
                if (!p.equals(cursor.getStix().getStixCoordinates().getFirst())) {
                    if (!foundFuse) {
                        if (cursor.isFast()) {
                            gc.setFill(Color.MEDIUMBLUE);
                        } else {
                            gc.setFill(Color.DARKRED);
                        }
                    } else {
                        if (p.equals(fuse)) {
                            foundFuse = false;
                        }
                        gc.setFill(Color.GRAY);
                    }
                    gc.fillRect(gridToCanvas(p.x), gridToCanvas(p.y), 2, 2);
                }
            }
            if (cursor.getFuseHandler().getFuse() != null) {
                cursor.getFuseHandler().getFuse().move();
                cursor.getFuseHandler().getFuse().draw(canvas);
            }
            foundFuse = false;
            fuse = new Point(-1, -1);
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
}

