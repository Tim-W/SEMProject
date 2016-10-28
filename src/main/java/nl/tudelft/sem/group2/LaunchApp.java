package nl.tudelft.sem.group2;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import nl.tudelft.sem.group2.scenes.StartScene;

import java.util.logging.Level;

import static nl.tudelft.sem.group2.global.Globals.BOARD_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.BOARD_WIDTH;
import static nl.tudelft.sem.group2.global.Globals.GAME_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.GAME_WIDTH;

/**
 * Starts the application.
 */
public class LaunchApp extends Application {

    private static final Logger LOGGER = Logger.getLogger();

    /**
     * Launches the application.
     *
     * @param args - not used
     */
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("detailedLogging")) {
            LOGGER.setLevel(Level.ALL);
        } else if (args.length > 0 && args[0].equals("loggingOff")) {
            LOGGER.setLevel(Level.OFF);
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Start stage
        primaryStage.setTitle("Qix");
        primaryStage.setWidth(GAME_WIDTH);
        primaryStage.setHeight(GAME_HEIGHT);
        primaryStage.getIcons().add(new Image("/images/stage-icon.png"));
        LOGGER.log(Level.INFO, "Stage Created, Application Icon loaded", this.getClass());

        LOGGER.log(Level.INFO, "GameScene created succesfully", this.getClass());
        primaryStage.setScene(new StartScene(new Group(), GAME_WIDTH, GAME_HEIGHT, Color.BLACK, primaryStage));
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();

        //Comment to mute empty sound
        //playSound("/sounds/background-music.mp3", 1);
        //((Group) scene.getRoot()).getChildren().add(mediaView);
        LOGGER.log(Level.INFO, "Audio Loaded succesfully", this.getClass());
    }
}
