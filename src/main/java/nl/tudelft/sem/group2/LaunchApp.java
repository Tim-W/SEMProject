package nl.tudelft.sem.group2;

import nl.tudelft.sem.group2.scenes.GameScene;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LaunchApp extends Application {

    public static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception {


        // Init variables
        stage = primaryStage;

        // Start stage
        stage.setTitle("Qix");
        stage.setWidth(340);
        stage.setHeight(420);
        //TODO fix image path
        stage.getIcons().add(new Image("/images/stageIcon.png"));

        GameScene scene;
        Group root = new Group();

        scene = new GameScene(root, Color.BLACK);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
