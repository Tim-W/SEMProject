package nl.tudelft.sem.group2;

import nl.tudelft.sem.group2.scenes.GameScene;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LaunchApp extends Application {

    public static Stage stage;


    public static int getBoardWidth() {
        return boardWidth;
    }

    public static int getBoardHeight() {
        return boardHeight;
    }

    // a point on the boardgrid is 2x2 pixels, so a boardgrid contains 150x150
    public static int getGridHeight() {
        return boardHeight/2;
    }

    public static int getGridWidth() {
        return boardWidth/2;
    }

    private static int boardWidth = 300;

    private static int boardHeight = 300;

    @Override
    public void start(Stage primaryStage) throws Exception {


        // Init variables
        stage = primaryStage;

        // Start stage
        stage.setTitle("Qix");
        stage.setWidth(340);
        stage.setHeight(400);
        //TODO fix image path
        stage.getIcons().add(new Image("/res/images/stageIcon.png"));

        nl.tudelft.sem.group2.scenes.GameScene scene;
        Group root = new Group();

        //EXAMPLE ON HOW TO USE CANVAS
        Canvas canvas = new Canvas(340, 400);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        //BLUE SCREEN IS THE SIZE OF THE BOARD, 300x300
        gc.setFill(Color.BLUE);
        gc.fillRect(20, 80, boardWidth, boardHeight);
        root.getChildren().add(canvas);

        scene = new GameScene(root, Color.BLACK);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();

        AreaTracker areaState = new AreaTracker();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
