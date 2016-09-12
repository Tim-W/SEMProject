package main.java.nl.tudelft.sem.group2.scenes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import main.java.nl.tudelft.sem.group2.LaunchApp;

public class ScoreScene extends SubScene{

	private Label score;
	private Label claimedPercentage;
	private Label title;
	private int targetPercentage;
	private Label claimed = new Label("Claimed");
	

	
	public ScoreScene(Group root, double width, double height){
		super(root, width, height);
		targetPercentage = 75;
		claimed.setTextFill(Color.YELLOW);
		claimed.setStyle("-fx-font-size:14;");
		
		TilePane tilePane = new TilePane();
		tilePane.prefTileWidthProperty().setValue(113);
		tilePane.setPadding(new Insets(20));
		tilePane.setVgap(10);
		
		VBox left = new VBox();
		VBox center = new VBox();
		center.setSpacing(-5);
		VBox right = new VBox();
		right.setAlignment(Pos.CENTER_LEFT);
		
		score = new Label();
		score.setTextFill(Color.WHITE);
		score.setStyle("-fx-font-size:24;");

		
		claimedPercentage = new Label();
		claimedPercentage.setTextFill(Color.YELLOW);
		claimedPercentage.setStyle("-fx-font-size:14;");

		
		title = new Label("Qix");
		title.setTextFill(Color.YELLOW);
		title.setStyle("-fx-font-size:30;");

		//TODO Fix font
		//Font f = Font.loadFont(LaunchApp.class.getResource("qixfont.ttf").toExternalForm(),12);
		//title.setFont(f);

		left.getChildren().add(title);
		center.getChildren().add(claimed);
		center.getChildren().add(claimedPercentage);
		right.getChildren().add(score);
		
		tilePane.getChildren().add(left);
		tilePane.getChildren().add(center);
		tilePane.getChildren().add(right);

		
		root.getChildren().add(tilePane);
	}




	public void setScore(int scoreInput) {
		score.setText(String.valueOf(scoreInput));
	}




	public void setClaimedPercentage(int claimedPercentageInput) {
		claimedPercentage.setText(String.valueOf(claimedPercentageInput) + "% of " + String.valueOf(targetPercentage) + "%");
	}
	
	

	
	
}
