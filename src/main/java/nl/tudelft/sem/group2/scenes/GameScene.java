package main.java.nl.tudelft.sem.group2.scenes;

import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameScene extends Scene {

	private int score;
	private int claimedPercentage;
	private int targetPercentage;
	private long previousTime;
	private KeyCode currentMove = null;

	AnimationTimer animationTimer;

	// TODO implement life system
	// private int lifes;

	public GameScene(final Group root, Color black) {
		super(root, black);

		score = 0;
		claimedPercentage = 0;
		targetPercentage = 75;

		// lifes = 0;

		Group group = new Group();
		ScoreScene scoreScene = new ScoreScene(group, 340, 60);

		scoreScene.setScore(40);
		scoreScene.setClaimedPercentage(34);
		root.getChildren().add(scoreScene);

		final ArrayList<String> input = new ArrayList<String>();
		previousTime = System.nanoTime();

		setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				currentMove = e.getCode();
			}
		});

		setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				currentMove = null;
			}
		});

		animationTimer = new AnimationTimer() {
			public void handle(long now) {
				// 33333333.3 = 30 FPS
				if (now - previousTime > (long) 33333333.3) {
					previousTime = now;
					if (currentMove != null) {
						if (currentMove.equals(KeyCode.LEFT)) {
							// TODO code to move left
						} else if (currentMove.equals(KeyCode.RIGHT)) {
							// TODO code to move right
						} else if (currentMove.equals(KeyCode.UP)) {
							// TODO code to move up
						} else if (currentMove.equals(KeyCode.DOWN)) {
							// TODO code to move down
						}
					}
				}

			}
		};
		animationTimer.start();
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

	public void animationTimerStart() {
		animationTimer.start();
	}

	public void animationTimerStop() {
		animationTimer.stop();
	}

}
