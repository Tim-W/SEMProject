package nl.tudelft.sem.group2.scenes;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.game.Board;
import nl.tudelft.sem.group2.units.Cursor;

import nl.tudelft.sem.group2.game.Board;
import nl.tudelft.sem.group2.units.Cursor;

public class GameScene extends Scene {

	private int score;
	private int claimedPercentage;
	private int targetPercentage;
	private long previousTime;
	private Cursor cursor;
	private Board board;
	private KeyCode currentMove = null;

	AnimationTimer animationTimer;

	// TODO implement life system
	// private int lifes;

	public GameScene(final Group root, Color black) {
		super(root, black);
		Canvas canvas = new Canvas(300, 300);
		canvas.setLayoutX(20);
		canvas.setLayoutY(80);
		board = new Board(canvas);

		//Hacky way to create black bottom border
		Canvas bottomBorder = new Canvas(300,20);
		bottomBorder.setLayoutY(380);
		

		Image[] cursorSprite = new Image[1];
		cursorSprite[0] = new Image("/res/images/cursor.png");
		cursor = new Cursor(300,300,20,20, cursorSprite);
		board.addUnit(cursor);
		
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
		root.getChildren().add(canvas);

		root.getChildren().add(bottomBorder);

		previousTime = System.nanoTime();

		setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				cursor.setCurrentMove(e.getCode());
			}
		});

		setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				cursor.setCurrentMove(null);			}
		});

		//animation timer for handling a loop
		animationTimer = new AnimationTimer() {
			public void handle(long now) {
				// 3333333.3 = 300 FPS
				if (now - previousTime > (long) 3333333.3) {
					previousTime = now;
					//draw
					board.draw();
					board.collisions();

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
