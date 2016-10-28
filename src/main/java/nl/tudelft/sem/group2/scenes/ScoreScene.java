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
    private ArrayList<Label> scoreLabels;
    private ArrayList<Label> claimedPercentageLabels;
    private ArrayList<Label> livesLabels;
    private ArrayList<Color> colors;
    private ArrayList<Double> highClaimedPercentages;
    private Label totalClaimedPercentageLabel;
    private Label totalScoreLabel;
    private VBox left;
    private ArrayList<VBox> playerBoxes;
    private ArrayList<Integer> scores;
    private ArrayList<Double> claimedPercentages;
    private int totalScore;

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
        Initialize();
        createTitlePane();
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

    private void Initialize() {
        totalClaimedPercentageLabel = new Label("0");
        totalScoreLabel = new Label("0");
        left = new VBox();
        left.setAlignment(Pos.TOP_CENTER);
        playerBoxes = new ArrayList<>();
        scoreLabels = new ArrayList<>();
        highClaimedPercentages = new ArrayList<>();
        claimedPercentageLabels = new ArrayList<>();
        claimedPercentages = new ArrayList<>();
        scores = new ArrayList<>();
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
        scores.add(0);
        claimedPercentages.add(0.0);
        addPlayerScore(ID);
        setLivesLabel(LIVES, ID);
    }

    private void addPlayerScore(int ID) {
        playerBoxes.get(ID).getChildren().add(new Label("Player " + (ID + 1)));

        Label claimedPercentage = new Label();
        claimedPercentageLabels.add(claimedPercentage);
        playerBoxes.get(ID).getChildren().add(claimedPercentage);

        Label livesLabel = new Label();
        livesLabels.add(livesLabel);
        playerBoxes.get(ID).getChildren().add(livesLabel);

        Label score = new Label();
        scoreLabels.add(score);
        playerBoxes.get(ID).getChildren().add(score);

    }

    private void displayTitle() {
        ImageView title = new ImageView("/images/logo.png");
        title.setFitWidth(TITLE_FIT_WIDTH);
        title.setFitHeight(TITLE_FIT_HEIGHT);
        left.getChildren().add(title);
    }

    private void setClaimedPercentages() {
        setTotalClaimedPercentageLabel();
        totalClaimedPercentageLabel.setTextFill(Color.WHITE);
        totalClaimedPercentageLabel.setStyle("-fx-font-size:12;");
        left.getChildren().add(totalClaimedPercentageLabel);
        for (int i = 0; i < claimedPercentageLabels.size(); i++) {
            Label label = claimedPercentageLabels.get(i);
            addClaimedPercentage(0, i);
            label.setTextFill(colors.get(i));
            label.setStyle("-fx-font-size:12;");
        }
    }

    private void setScoreLabels() {
        totalScore = 0;
        setTotalScore(0);
        totalScoreLabel.setTextFill(Color.WHITE);
        totalScoreLabel.setStyle("-fx-font-size:12;");
        left.getChildren().add(totalScoreLabel);
        for (int i = 0; i < scoreLabels.size(); i++) {
            Label label = scoreLabels.get(i);
            label.setTextFill(colors.get(i));
            label.setStyle("-fx-font-size:12;");
            addScore(0, i);
        }
    }

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
        tilePane.getChildren().add(left);
    }

    /**
     * Set the current score amount.
     *
     * @param scoreInput the score to be shown - is displayed as-is
     */
    public void addScore(int scoreInput, int ID) {
        scores.set(ID, scores.get(ID) + scoreInput);
        setTotalScore(scoreInput);
        scoreLabels.get(ID).setText("Score: " + String.valueOf(scores.get(ID)));
    }

    /**
     * Set the current score amount.
     */
    public void setTotalScore(int scoreInput) {
        totalScore += scoreInput;
        totalScoreLabel.setText("Total score: " + String.valueOf(totalScore));
    }

    /**
     * Display claimed percentage with a % sign.
     *
     * @param claimedPercentageInput the claimed percentage in XX%, so no decimals
     */
    private void addClaimedPercentage(double claimedPercentageInput, int ID) {
        claimedPercentages.set(ID, claimedPercentages.get(ID) + claimedPercentageInput);
        setTotalClaimedPercentageLabel();
        claimedPercentageLabels.get(ID).setText(Math.round(claimedPercentages.get(ID)) + "%");
        highClaimedPercentages.set(ID, claimedPercentages.get(ID));
    }

    /**
     * Display claimed percentage with a % sign.
     *
     */
    private void setTotalClaimedPercentageLabel() {
        double percentage = 0;
        for (Double i : claimedPercentages) {
            percentage += i;
        }
        totalClaimedPercentageLabel.setText("Claimed: " + Math.round(percentage) +
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
            //if (scoreCounter.getTotalPercentage() >= highClaimedPercentages.get(ID)) {
            addScore(scoreCounter.getRecentScore(), ID);
            addClaimedPercentage(scoreCounter.getRecentPercentage(), ID);
                LOGGER.log(Level.WARNING, "Score updated "
                        + ID, this.getClass());
                if (arg instanceof Integer) {
                    setLivesLabel((int) arg, ID);
                }
            // }
        }
    }

    /**
     * resets the percentage.
     */
    public void reset() {
        for (int i = 0; i < claimedPercentages.size(); i++) {
            claimedPercentages.set(i, 0.0);
            scores.set(i, 0);
            addScore(0, i);
            addClaimedPercentage(0.0, i);
        }
        setTotalClaimedPercentageLabel();
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
