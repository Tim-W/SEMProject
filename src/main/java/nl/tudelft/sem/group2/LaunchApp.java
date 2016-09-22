package nl.tudelft.sem.group2;

import java.util.logging.Level;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import nl.tudelft.sem.group2.scenes.GameScene;

public class LaunchApp extends Application {

    public static Stage stage;
    
    //private static final Logger LOGGER = Logger.getLogger(LaunchApp.class.getName());

    private static final Logger LOGGER = new Logger();
    
    public static Logger getLogger(){
    	return LOGGER;
    }

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

    public static MediaView mediaView;

    private static int boardWidth = 300;

    private static int boardHeight = 300;

    @Override
    public void start(Stage primaryStage) throws Exception {

        // Init variables
        stage = primaryStage;

        // Start stage
        stage.setTitle("Qix");
        stage.setWidth(340);
        stage.setHeight(420);
        stage.getIcons().add(new Image("/images/stageIcon.png"));
        LOGGER.log(Level.INFO, "Stage Created, Application Icon loaded", this.getClass());

        GameScene scene;
        Group root = new Group();

        scene = new GameScene(root, Color.BLACK);
        LOGGER.log(Level.INFO, "GameScene created succesfully", this.getClass());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
        
        //Comment to mute empty sound
        playSound("/sounds/qix.mp3",1);
        mediaView = new MediaView();
        ((Group)scene.getRoot()).getChildren().add(mediaView);
        LOGGER.log(Level.INFO, "Audio Loaded succesfully", this.getClass());

    }
    
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

    public static void main(String[] args) {
    	if(args.length > 0 && args[0].equals("detailedLogging")){
    		LOGGER.setLevel(Level.ALL);
    	} else if (args.length > 0 && args[0].equals("loggingOff")){
    		LOGGER.setLevel(Level.OFF);
    	}
        launch(args);
    }
}
