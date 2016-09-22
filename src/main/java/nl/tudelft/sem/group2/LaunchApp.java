package nl.tudelft.sem.group2;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import nl.tudelft.sem.group2.scenes.GameScene;

/**
 * Starts the application.
 */
public class LaunchApp extends Application {

    private static final int BOARD_WIDTH = 300;
    private static final int BOARD_HEIGHT = 300;
    private static final int GAME_WIDTH = 340;
    private static final int GAME_HEIGHT = 420;
    private static Stage stage;
    private static MediaView mediaView;

    /**
     * @return board width
     */
    public static int getBoardWidth() {
        return BOARD_WIDTH;
    }

    /**
     * @return board height
     */
    public static int getBoardHeight() {
        return BOARD_HEIGHT;
    }

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
     * Plays a sound file.
     *
     * @param string - the path to the sound file
     * @param volume - the volume in decibels
     */
    public static synchronized void playSound(final String string, final double volume) {
        new Thread(new Runnable() {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            public void run() {
                try {
                    Media hit = new Media(getClass().getResource(string).toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(hit);
                    mediaPlayer.setVolume(volume);
                    mediaPlayer.play();
                    mediaView.setMediaPlayer(mediaPlayer);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    /**
     * Launches the application.
     * @param args - not used
     */
    public static void main(String[] args) {
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

        GameScene scene;
        Group root = new Group();

        scene = new GameScene(root, Color.BLACK);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();

        //Comment to mute empty sound
        playSound("/sounds/qix.mp3", 1);
        mediaView = new MediaView();
        ((Group) scene.getRoot()).getChildren().add(mediaView);

    }
}
