package main.java.nl.tudelft.sem.group2;


import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LaunchApp extends Application{

	public static Stage stage;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
	
		
		// Init variables
		stage = primaryStage;

		// Start stage
		stage.setTitle("Qix");
		stage.setResizable(false);
		stage.setWidth(340);
		stage.setHeight(400);
		stage.getIcons().add(new Image("http://www.arcadeboss.com/games/qix/qix.gif"));
		
		Scene scene;
		Group root = new Group();
		
		scene = new Scene(root,340,400, Color.BLACK);

		//EXAMPLE ON HOW TO USE CANVAS
//		final Canvas canvas = new Canvas(300,300);	
//		GraphicsContext gc = canvas.getGraphicsContext2D();
//		 
//		gc.setFill(Color.BLUE);
//		gc.fillRect(75,75,100,100);
//		root.getChildren().add(canvas);
		

		stage.setScene(scene);
		stage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
