package nl.tudelft.sem.group2.scenes;

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
import nl.tudelft.sem.group2.units.*;

import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

public class GameScene extends Scene {

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

		Sparx sparxRight = new Sparx(75, 0, 16, 16, SparxDirection.RIGHT);
		Sparx sparxLeft = new Sparx(75, 0, 16, 16, SparxDirection.LEFT);
		board.addUnit(sparxRight);
		board.addUnit(sparxLeft);

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

		final ArrayList<KeyCode> arrowKeys = new ArrayList<KeyCode>();
		arrowKeys.add(KeyCode.UP);
		arrowKeys.add(KeyCode.DOWN);
		arrowKeys.add(KeyCode.LEFT);
		arrowKeys.add(KeyCode.RIGHT);

		setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				if (e.getCode().equals(KeyCode.SPACE) && !isRunning) {
					animationTimer.start();
					isRunning = true;
					messageLabel.setText("");
				} else if (arrowKeys.contains(e.getCode())) {
					cursor.setCurrentMove(e.getCode());
				} else if (e.getCode().equals(KeyCode.X)) {
					cursor.setSpeed(1);
					cursor.setDrawing(true);
					cursor.setFast(false);
				} else if (e.getCode().equals(KeyCode.Z)) {
					cursor.setSpeed(2);
					cursor.setDrawing(true);
					cursor.setFast(true);
				}
			}
		});

		setOnKeyReleased(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				KeyCode keyCode = e.getCode();
				if (keyCode.equals(cursor.getCurrentMove())) {
					if (areaTracker.getStix().contains(new Point(cursor.getX(), cursor.getY()))) {
						board.addUnit(new Fuse((int) areaTracker.getStix().getFirst().getX(), (int) areaTracker.getStix().getFirst().getY(), 16, 16));
					}
					cursor.setCurrentMove(null);
				} else if (keyCode.equals(KeyCode.X)) {
					cursor.setDrawing(false);
					cursor.setSpeed(2);
				} else if (keyCode.equals(KeyCode.Z)) {
					cursor.setDrawing(false);
					cursor.setSpeed(2);
				}
			}
		});

		// animation timer for handling a loop
		animationTimer = new AnimationTimer() {
			public void handle(long now) {
				// 3333333.3 = 300 FPS
				if (now - previousTime > (long) 33333333.3) {
					if (scoreCounter.getTotalPercentage() >= scoreCounter.getTargetPercentage()) {
						gameWon();
					}
					previousTime = now;
					// draw
					board.draw();
					board.collisions();
					qixStixCollisions();
					scoreScene.setScore(scoreCounter.getTotalScore());
					scoreScene.setClaimedPercentage((int) scoreCounter.getTotalPercentage());
					calculateArea();
				}
			}

		};
	}

	private void qixStixCollisions() {
		Polygon qixP = qix.toPolygon();
		for (Point point : areaTracker.getStix()) {
			if (qixP.intersects(point.getX(), point.getY(), 1, 1)){
				gameOver();
			}
				
		}

	}
	protected void calculateArea() {
		if (areaTracker.getBoardGrid()[cursor.getX()][cursor.getY()] == AreaState.OUTERBORDER
				&& !areaTracker.getStix().isEmpty()) {
			System.out.println("ja");
			areaTracker.calculateNewArea(new Point(qix.getX(), qix.getY()),
					cursor.isFast());
			//Remove the Fuse from the board when completing an area
			board.removeFuse();
		}
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
