package main.java.nl.tudelft.sem.group2;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

public class LaunchApp extends Application{

	public static Stage mainStage;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
	
		
		// Init variables
		mainStage = primaryStage;

		// Start stage
		mainStage.setTitle("Qix");
		mainStage.show();
		mainStage.setResizable(false);
		mainStage.setWidth(340);
		mainStage.setHeight(400);
		mainStage.getIcons().add(new Image("http://www.arcadeboss.com/games/qix/qix.gif"));

	}

	public static void main(String[] args) {
		launch(args);
	}

}
