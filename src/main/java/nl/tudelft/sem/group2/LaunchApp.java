package nl.tudelft.sem.group2;

import java.util.logging.Level;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import nl.tudelft.sem.group2.controllers.GameController;

import static nl.tudelft.sem.group2.global.Globals.BOARD_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.BOARD_WIDTH;
import static nl.tudelft.sem.group2.global.Globals.GAME_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.GAME_WIDTH;

/**
 * Starts the application.
 */
public class LaunchApp extends Application {

    private static final Logger LOGGER = Logger.getLogger();
    private static Stage stage;
    private static MediaView mediaView;

    /**
     * @return grid height - a point on the boardgrid is 2x2 pixels,
     * so a boardgrid contains 150x150
     */
    public static int getGridHeight() {
        // a point on the boardgrid is 2x2 pixels, so a boardgrid contains 150x150
        return BOARD_HEIGHT / 2;
    }

    /**
     * @return grid width - a point on the boardgrid is 2x2 pixels,
     * so a boardgrid contains 150x150
     */
    public static int getGridWidth() {
        return BOARD_WIDTH / 2;
    }

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

        // Init variables
        stage = primaryStage;

        // Start stage
        stage.setTitle("Qix");
        stage.setWidth(GAME_WIDTH);
        stage.setHeight(GAME_HEIGHT);
        stage.getIcons().add(new Image("/images/stageIcon.png"));
        LOGGER.log(Level.INFO, "Stage Created, Application Icon loaded", this.getClass());

        LOGGER.log(Level.INFO, "GameScene created succesfully", this.getClass());
        GameController gameController = GameController.getInstance();
        stage.setScene(gameController.getScene());
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();

        //Comment to mute empty sound
        //playSound("/sounds/qix.mp3", 1);
        //((Group) scene.getRoot()).getChildren().add(mediaView);
        LOGGER.log(Level.INFO, "Audio Loaded succesfully", this.getClass());

    }

}
