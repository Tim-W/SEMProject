package nl.tudelft.sem.group2.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.ScoreCounter;
import nl.tudelft.sem.group2.controllers.GameController;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;

import static edu.umd.cs.findbugs.FindBugs.LOGGER;

import static nl.tudelft.sem.group2.global.Globals.LIVES;

/**
 * Displays info about the current score and gained percentage.
 */
public class ScoreScene extends SubScene implements Observer {

    //standard target percentage
    private static final int CENTER_SPACING = -5;
    private static final int TITLE_FIT_HEIGHT = 40;
    private static final int TITLE_FIT_WIDTH = 100;
    private static final int TITLE_WIDTH = 113;
    private static final int TITLE_PADDING = 20;
    private static final int TITLE_VGAP = 10;
    private TilePane tilePane;
    private ArrayList<Label> scores;
    private ArrayList<Label> claimedPercentages;
    private ArrayList<Label> livesLabels;
    private ImageView title;
    private ArrayList<Color> colors;
    private ArrayList<Double> highClaimedPercentages;
    private VBox left;
    private VBox center;
    private VBox right;

    /**
     * Create a new ScoreScene.
     * Shows current score percentage,
     * title of the views,
     * and score count.
     *
     * @param root   the root this scene is placed on top of
     * @param width  width of the scene
     * @param height height of the scene
     */
    public ScoreScene(Group root, double width, double height) {
        super(root, width, height);
        createTitlePane();
        left = new VBox();
        center = new VBox();
        right = new VBox();
        center.setSpacing(CENTER_SPACING);
        right.setAlignment(Pos.CENTER_LEFT);

        //TODO Fix font
        //Font f = Font.loadFont(LaunchApp.class.getResource("qixfont.ttf").toExternalForm(),12);
        //title.setFont(f);
        scores = new ArrayList<>();
        highClaimedPercentages = new ArrayList<>();
        highClaimedPercentages.add(0.0);
        claimedPercentages = new ArrayList<>();
        livesLabels = new ArrayList<>();

        displayTitle();
        left.getChildren().add(title);
        colors = new ArrayList<>();
        colors.add(Color.BLUE);
        addPlayerScore(0);
        setLivesLabel(LIVES, 0);
        if (GameController.getInstance().isTwoPlayers()) {
            colors.add(Color.RED);
            highClaimedPercentages.add(0.0);
            addPlayerScore(1);
            setLivesLabel(LIVES, 1);
        }
        createScoreLabel();
        displayClaimedPercentage();
        createLivesLabel();
        tilePane.getChildren().add(left);
        tilePane.getChildren().add(center);
        tilePane.getChildren().add(right);


        root.getChildren().add(tilePane);
    }

    private void addPlayerScore(int ID) {
        center.getChildren().add(new Label("Player " + ID + 1));

        Label claimedPercentage = new Label();
        claimedPercentages.add(claimedPercentage);
        center.getChildren().add(claimedPercentage);

        Label livesLabel = new Label();
        livesLabels.add(livesLabel);
        center.getChildren().add(livesLabel);

        Label score = new Label("0");
        scores.add(score);
        right.getChildren().add(score);

    }
    private void displayTitle() {
        title = new ImageView("/images/logo.png");
        title.setFitWidth(TITLE_FIT_WIDTH);
        title.setFitHeight(TITLE_FIT_HEIGHT);
    }

    private void displayClaimedPercentage() {
        for (int i = 0; i < claimedPercentages.size(); i++) {
            Label label = claimedPercentages.get(i);
            setClaimedPercentage(0, i);
            label.setTextFill(colors.get(i));
            label.setStyle("-fx-font-size:12;");
        }
    }

    private void createScoreLabel() {
        for (int i = 0; i < scores.size(); i++) {
            Label label = scores.get(i);
            label.setTextFill(colors.get(i));
            label.setStyle("-fx-font-size:24;");
        }
    }/*

    private void setClaimedText() {

        claimed.setTextFill(color);
        claimed.setStyle("-fx-font-size:12;");
    }*/

    private void createLivesLabel() {
        for (int i = 0; i < livesLabels.size(); i++) {
            Label label = livesLabels.get(i);
            label.setTextFill(colors.get(i));
            label.setStyle("-fx-font-size:12;");
        }
    }

    private void createTitlePane() {
        tilePane = new TilePane();
        tilePane.prefTileWidthProperty().setValue(TITLE_WIDTH);
        tilePane.setPadding(new Insets(TITLE_PADDING));
        tilePane.setVgap(TITLE_VGAP);
    }

    /**
     * Set the current score amount.
     *
     * @param scoreInput the score to be shown - is displayed as-is
     */
    public void setScore(int scoreInput, int ID) {
        scores.get(ID).setText(String.valueOf(scoreInput));
    }

    /**
     * Display claimed percentage with a % sign.
     *
     * @param claimedPercentageInput the claimed percentage in XX%, so no decimals
     */
    private void setClaimedPercentage(double claimedPercentageInput, int ID) {
        claimedPercentages.get(ID).setText(
                Math.round(claimedPercentageInput) + "% of " + GameController.getInstance()
                        .getLevelHandler().getLevel().getPercentage() + "%"
        );
        highClaimedPercentages.set(ID, claimedPercentageInput);
    }

    /**
     * Update the info on the scorescene with actual info from scorecounter.
     *
     * @param o   the observable (ScoreCounter)
     * @param arg the argument in ID
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof ScoreCounter) {
            ScoreCounter scoreCounter = (ScoreCounter) o;
            int ID = scoreCounter.getid;
            if (scoreCounter.getTotalPercentage() >= highClaimedPercentages.get(ID)) {
                setScore(scoreCounter.getTotalScore(), ID);
                setClaimedPercentage(scoreCounter.getTotalPercentage(), ID);
                LOGGER.log(Level.WARNING, "Score updated "
                        + ID, this.getClass());
                setLivesLabel(scoreCounter.getLives(), ID);
                if (arg instanceof Integer) {
                    setLivesLabel((int) arg);
                }
            }
        }
    }

    /**
     * resets the percentage.
     */
    public void reset() {
        setClaimedPercentage(0, 0);
        setClaimedPercentage(0, 1);
    }
    /**
     * setter for the lives label.
     *
     * @param lives the amount of lives the player has left
     */
    public void setLivesLabel(int lives, int ID) {
        livesLabels.get(ID).setText("Lives: " + lives);
    }
}
