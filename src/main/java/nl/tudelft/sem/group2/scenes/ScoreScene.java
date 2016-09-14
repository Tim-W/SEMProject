package nl.tudelft.sem.group2.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ScoreScene extends SubScene{

	private Label score;
	private Label claimedPercentage;
	private ImageView title;
	//standard target percentage
	private int targetPercentage = 75;
	private Label claimed = new Label("Claimed");
	
	public ScoreScene(Group root, double width, double height){
		super(root, width, height);
		
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
		
		score = new Label("0");
		score.setTextFill(Color.WHITE);
		score.setStyle("-fx-font-size:24;");

		
		claimedPercentage = new Label();
		claimedPercentage.setTextFill(Color.YELLOW);
		claimedPercentage.setStyle("-fx-font-size:14;");

		
		title = new ImageView("/images/logo.png");
		title.setFitWidth(100);
		title.setFitHeight(40);

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
