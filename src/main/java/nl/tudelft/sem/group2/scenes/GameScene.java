package main.java.nl.tudelft.sem.group2.scenes;

import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameScene extends Scene{

	private int score;
	private int claimedPercentage;
	private int targetPercentage;
	
	//TODO implement life system
	//private int lifes;
	
	public GameScene(Group root, Color black) {
		super(root,black);

		score = 0;
		claimedPercentage = 0;
		targetPercentage = 75;
		//lifes = 0;
		
		Group group = new Group();
		ScoreScene scoreScene = new ScoreScene(group, 340, 60);
		
		scoreScene.setScore(40);
		scoreScene.setClaimedPercentage(34);
		root.getChildren().add(scoreScene);

		
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getClaimedPercentage() {
		return claimedPercentage;
	}

	public void setClaimedPercentage(int claimedPercentage) {
		this.claimedPercentage = claimedPercentage;
	}

	public int getTargetPercentage() {
		return targetPercentage;
	}

	public void setTargetPercentage(int targetPercentage) {
		this.targetPercentage = targetPercentage;
	}

}
