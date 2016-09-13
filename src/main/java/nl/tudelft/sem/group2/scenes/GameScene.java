package nl.tudelft.sem.group2.scenes;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.ScoreCounter;
import nl.tudelft.sem.group2.game.Board;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Sparx;

public class GameScene extends Scene {

	private int score;
	private int claimedPercentage;
	private int targetPercentage;
	private long previousTime;
	private Cursor cursor;
	private Board board;
	private KeyCode currentMove = null;
	private static AreaTracker areaTracker;
	private static ScoreCounter scoreCounter;
	private boolean isRunning = false;
	private Label pressSpaceLabel = new Label("Press Space to begin!");
	private static Label gameOverLabel = new Label("");

	static AnimationTimer animationTimer;

	// TODO implement life system
	// private int lifes;

	public GameScene(final Group root, Color black) {
		super(root, black);
		Canvas canvas = new Canvas(316, 316);
		canvas.setLayoutX(15);
		canvas.setLayoutY(75);
		areaTracker = new AreaTracker();
		board = new Board(canvas);

		scoreCounter = new ScoreCounter();

		cursor = new Cursor(12,10,16,16);
		
		Sparx sparx = new Sparx(10,10,16,16);
		board.addUnit(sparx);
		
		//Hacky way to create black bottom border
		Canvas bottomBorder = new Canvas(300,20);
		bottomBorder.setLayoutY(380);
		board.addUnit(cursor);

		areaTracker = new AreaTracker();
		scoreCounter = new ScoreCounter();

		pressSpaceLabel.setLayoutX(60);
		pressSpaceLabel.setLayoutY(200);
		pressSpaceLabel.setStyle("-fx-font-size: 24px");
		pressSpaceLabel.setTextFill(Color.YELLOW);
		
		gameOverLabel.setLayoutX(100);
		gameOverLabel.setLayoutY(200);
		gameOverLabel.setStyle("-fx-font-size: 24px; -fx-effect: dropshadow(gaussian, rgba(255,255,255,0), 0,0,0,1);");
		gameOverLabel.setTextFill(Color.YELLOW);

		// lifes = 0;

		Group group = new Group();
		ScoreScene scoreScene = new ScoreScene(group, 340, 60);

		// TODO shift this to a game class and save/load score
		scoreScene.setScore(0);
		scoreScene.setClaimedPercentage(0);
		root.getChildren().add(scoreScene);
		root.getChildren().add(canvas);
		root.getChildren().add(bottomBorder);
		root.getChildren().add(pressSpaceLabel);
		root.getChildren().add(gameOverLabel);
		previousTime = System.nanoTime();
		board.draw();

		setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				if (e.getCode().equals(KeyCode.SPACE) && !isRunning  ) {
					// TODO remove this start and start using game
					animationTimer.start();
					isRunning = true;
					root.getChildren().remove(pressSpaceLabel);
				} else {
					cursor.setCurrentMove(e.getCode());
				}
			}
		});

		setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				cursor.setCurrentMove(null);
			}
		});

		// animation timer for handling a loop
		animationTimer = new AnimationTimer() {
			public void handle(long now) {
				// 3333333.3 = 300 FPS
				if (now - previousTime > (long) 33333333.3) {
					previousTime = now;
					// draw
					board.draw();
					board.collisions();
				}
			}
		};

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

	public static void animationTimerStart() {
		animationTimer.start();
	}

	public static void animationTimerStop() {
		animationTimer.stop();
	}

	public static AreaTracker getAreaTracker() {
		return areaTracker;
	}

	public static ScoreCounter getScoreCounter() {
		return scoreCounter;
	}

	public boolean isRunning(){
		return isRunning;
	}
	
	public static void gameOver(){
		//TODO add code for gameover
		animationTimerStop();
		gameOverLabel.setText("Game Over!");
	}
}
