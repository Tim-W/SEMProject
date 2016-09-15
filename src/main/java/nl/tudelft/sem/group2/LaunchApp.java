package nl.tudelft.sem.group2;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import nl.tudelft.sem.group2.scenes.GameScene;

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
        stage.setHeight(420);
        stage.getIcons().add(new Image("/images/stageIcon.png"));

        GameScene scene;
        Group root = new Group();

        scene = new GameScene(root, Color.BLACK);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
        
        //Comment to mute empty sound
        playSound();

    }
    
    public static synchronized void playSound() {
    	  new Thread(new Runnable() {
    	  // The wrapper thread is unnecessary, unless it blocks on the 
    	  // Clip finishing; see comments. 
    	    public void run() { 
    	      try { 
    	        Clip clip = AudioSystem.getClip();
    	        AudioInputStream inputStream = AudioSystem.getAudioInputStream(
    	          LaunchApp.class.getResourceAsStream("/sounds/qix.wav"));
    	        clip.open(inputStream);
    	        clip.start(); 
    	      } catch (Exception e) {
    	        System.err.println(e.getMessage());
    	      } 
    	    } 
    	  }).start();
    	} 

    public static void main(String[] args) {
        launch(args);
    }

}
