package nl.tudelft.sem.group2.scenes;

import java.awt.Point;

import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.AreaState;
import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.ScoreCounter;
import nl.tudelft.sem.group2.game.Board;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Sparx;
import nl.tudelft.sem.group2.units.Qix;

public class GameScene extends Scene {

	private int score = 0;
	private int claimedPercentage = 0;
	private int targetPercentage = 75;
	private long previousTime;
	private Cursor cursor;
	private Board board;
	private static AreaTracker areaTracker;
	private static ScoreCounter scoreCounter;
	private boolean isRunning = false;
	private static Label messageLabel = new Label(" Press SPACE to begin! ");
	private static VBox messageBox = new VBox(10);
	private Qix qix;
	static AnimationTimer animationTimer;
	private ScoreScene scoreScene;

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

		cursor = new Cursor(75, 150, 16, 16);

		Sparx sparx = new Sparx(0, 0, 16, 16);
		board.addUnit(sparx);

		qix = new Qix();
		// Hacky way to create black bottom border
		Canvas bottomBorder = new Canvas(300, 20);
		bottomBorder.setLayoutY(380);
		board.addUnit(cursor);
		board.addUnit(qix);
		areaTracker = new AreaTracker();
		scoreCounter = new ScoreCounter();

		// Messagebox&label for displaying start and end messages
		messageBox.setAlignment(Pos.CENTER);
		messageBox.getChildren().add(messageLabel);
		messageBox.setStyle("-fx-background-color: #000000;");
		messageBox.setLayoutX(50);
		messageBox.setLayoutY(176);
		messageLabel.setStyle("-fx-font-size: 24px;");
		messageLabel.setTextFill(Color.YELLOW);

		// lifes = 0;

		Group group = new Group();
		scoreScene = new ScoreScene(group, 340, 60);

		// TODO shift this to a game class and save/load score
		scoreScene.setScore(0);
		scoreScene.setClaimedPercentage(0);

		root.getChildren().add(scoreScene);
		root.getChildren().add(canvas);
		root.getChildren().add(bottomBorder);
		root.getChildren().add(messageBox);

		previousTime = System.nanoTime();
		board.draw();

		setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				if (e.getCode().equals(KeyCode.SPACE) && !isRunning) {
					// TODO remove this start and start using game
					animationTimer.start();
					isRunning = true;
					messageLabel.setText("");
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
					if (claimedPercentage >= targetPercentage) {
						gameWon();
					}
					previousTime = now;
					// draw
					board.draw();
					board.collisions();
					scoreScene.setScore(score);
					scoreScene.setClaimedPercentage(claimedPercentage);
					// TODO turn this on for area calculation
					// calculateArea();
				}
			}
		};

	}

	protected void calculateArea() {
		// TODO turn on if isdrawing is implemented
		// if (cursor.isDrawing()) {
		if (areaTracker.getBoardGrid()[cursor.getX()][cursor.getY()] == AreaState.OUTERBORDER
				&& !areaTracker.getStix().isEmpty()) {
			System.out.println("ja");
			areaTracker.calculateNewArea(new Point(qix.getX(), qix.getY()),
					cursor.isFast());
		}
		// }
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

	public boolean isRunning() {
		return isRunning;
	}

	public static void gameOver() {
		// TODO add code for gameover
		animationTimerStop();
		messageBox.setLayoutX(103);
		messageLabel.setText(" Game Over! ");
	}

	public static void gameWon() {
		animationTimerStop();
		messageBox.setLayoutX(115);
		messageLabel.setText(" You Won! ");
	}

}
