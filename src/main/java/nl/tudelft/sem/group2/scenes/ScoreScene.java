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
    private static final int TITLE_PADDING = 5;
    private static final int TITLE_VGAP = 5;
    private TilePane tilePane;
    private ArrayList<Label> scores;
    private ArrayList<Label> claimedPercentages;
    private ArrayList<Label> livesLabels;
    private ArrayList<Color> colors;
    private ArrayList<Double> highClaimedPercentages;
    private Label totalClaimedPercentageLabel;
    private Label totalScoreLabel;
    private VBox left;
    private ArrayList<VBox> playerBoxes;
    private int totalScore;
    private int totalPercentage;

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
        InitializeLabelsAndBoxes();
        createTitlePane();
        totalScore = 0;
        totalPercentage = 0;
        //TODO Fix font
        //Font f = Font.loadFont(LaunchApp.class.getResource("qixfont.ttf").toExternalForm(),12);
        //title.setFont(f);
        addPlayerBox(0);
        if (GameController.getInstance().isTwoPlayers()) {
            addPlayerBox(1);
        }
        displayTitle();
        setClaimedPercentages();
        setScoreLabels();
        createLivesLabel();
        for (int i = 0; i < playerBoxes.size(); i++) {
            tilePane.getChildren().add(playerBoxes.get(i));
        }
        root.getChildren().add(tilePane);
    }

    private void InitializeLabelsAndBoxes() {
        totalClaimedPercentageLabel = new Label();
        totalScoreLabel = new Label();
        left = new VBox();
        left.setAlignment(Pos.TOP_CENTER);
        playerBoxes = new ArrayList<>();
        scores = new ArrayList<>();
        highClaimedPercentages = new ArrayList<>();
        claimedPercentages = new ArrayList<>();
        livesLabels = new ArrayList<>();
        colors = new ArrayList<>();
    }

    private void addPlayerBox(int ID) {
        playerBoxes.add(new VBox());
        playerBoxes.get(ID).setAlignment(Pos.CENTER);
        if (ID == 0) {
            colors.add(Color.BLUE);
        } else {
            colors.add(Color.RED);
        }
        highClaimedPercentages.add(0.0);
        addPlayerScore(ID);
        setLivesLabel(LIVES, ID);
    }

    private void addPlayerScore(int ID) {
        playerBoxes.get(ID).getChildren().add(new Label("Player " + (ID + 1)));

        Label claimedPercentage = new Label();
        claimedPercentages.add(claimedPercentage);
        playerBoxes.get(ID).getChildren().add(claimedPercentage);

        Label livesLabel = new Label();
        livesLabels.add(livesLabel);
        playerBoxes.get(ID).getChildren().add(livesLabel);

        Label score = new Label("0");
        scores.add(score);
        playerBoxes.get(ID).getChildren().add(score);

    }

    private void displayTitle() {
        ImageView title = new ImageView("/images/logo.png");
        title.setFitWidth(TITLE_FIT_WIDTH);
        title.setFitHeight(TITLE_FIT_HEIGHT);
        left.getChildren().add(title);
    }

    private void setClaimedPercentages() {
        setTotalClaimedPercentageLabel(0);
        totalClaimedPercentageLabel.setTextFill(Color.WHITE);
        totalClaimedPercentageLabel.setStyle("-fx-font-size:12;");
        left.getChildren().add(totalClaimedPercentageLabel);
        for (int i = 0; i < claimedPercentages.size(); i++) {
            Label label = claimedPercentages.get(i);
            setClaimedPercentage(0, i);
            label.setTextFill(colors.get(i));
            label.setStyle("-fx-font-size:12;");
        }
    }

    private void setScoreLabels() {
        setTotalScore(0);
        totalScoreLabel.setTextFill(Color.WHITE);
        totalScoreLabel.setStyle("-fx-font-size:12;");
        left.getChildren().add(totalScoreLabel);
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
        tilePane.setMaxHeight(200);
        tilePane.setVgap(TITLE_VGAP);
        tilePane.getChildren().add(left);
    }

    /**
     * Set the current score amount.
     *
     * @param scoreInput the score to be shown - is displayed as-is
     */
    public void setScore(int scoreInput, int ID) {
        totalScore += scoreInput;
        setTotalScore(totalScore);
        scores.get(ID).setText(String.valueOf(scoreInput));
    }
    /**
     * Set the current score amount.
     *
     * @param scoreInput the score to be shown - is displayed as-is
     */
    public void setTotalScore(int scoreInput) {
        setTotalClaimedPercentageLabel(totalPercentage);
        totalScoreLabel.setText("Score: " + String.valueOf(scoreInput));
    }

    /**
     * Display claimed percentage with a % sign.
     *
     * @param claimedPercentageInput the claimed percentage in XX%, so no decimals
     */
    private void setClaimedPercentage(double claimedPercentageInput, int ID) {
        totalPercentage += claimedPercentageInput;
        setTotalClaimedPercentageLabel(totalPercentage);
        claimedPercentages.get(ID).setText(Math.round(claimedPercentageInput) + "%");
        highClaimedPercentages.set(ID, claimedPercentageInput);
    }

    /**
     * Display claimed percentage with a % sign.
     *
     * @param claimedTotalPercentageInput the total claimed percentage in XX%, so no decimals
     */
    private void setTotalClaimedPercentageLabel(double claimedTotalPercentageInput) {
        totalClaimedPercentageLabel.setText("Claimed: " + Math.round(claimedTotalPercentageInput) +
                "% of " + GameController.getInstance()
                .getLevelHandler().getLevel().getPercentage() + "%"
        );
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
            int ID = scoreCounter.getID();
            if (scoreCounter.getTotalPercentage() >= highClaimedPercentages.get(ID)) {
                setScore(scoreCounter.getTotalScore(), ID);
                setClaimedPercentage(scoreCounter.getTotalPercentage(), ID);
                LOGGER.log(Level.WARNING, "Score updated "
                        + ID, this.getClass());
                if (arg instanceof Integer) {
                    setLivesLabel((int) arg, ID);
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
