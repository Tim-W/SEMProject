package main.java.nl.tudelft.sem.group2.scenes;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

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

		//TODO shift this to a game class and save/load score
		scoreScene.setScore(0);
		scoreScene.setClaimedPercentage(0);
		
		root.getChildren().add(scoreScene);

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

		//animation timer for handling a loop
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
		//TODO remove this start and start using game
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
