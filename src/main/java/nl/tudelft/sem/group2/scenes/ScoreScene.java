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
        initialize();
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

    private void initialize() {
        totalClaimedPercentageLabel = new Label("0");
        totalScoreLabel = new Label("0");
        left = new VBox();
        left.setAlignment(Pos.TOP_CENTER);
        playerBoxes = new ArrayList<>();
        scoreLabels = new ArrayList<>();
        claimedPercentageLabels = new ArrayList<>();
        claimedPercentages = new ArrayList<>();
        scores = new ArrayList<>();
        livesLabels = new ArrayList<>();
        colors = new ArrayList<>();
    }

    private void addPlayerBox(int id) {
        playerBoxes.add(new VBox());
        playerBoxes.get(id).setAlignment(Pos.CENTER);
        if (id == 0) {
            colors.add(Color.BLUE);
        } else {
            colors.add(Color.RED);
        }
        scores.add(0);
        claimedPercentages.add(0.0);
        addPlayerScore(id);
        setLivesLabel(LIVES, id);
    }

    private void addPlayerScore(int id) {
        playerBoxes.get(id).getChildren().add(new Label("Player " + (id + 1)));

        Label claimedPercentage = new Label();
        claimedPercentageLabels.add(claimedPercentage);
        playerBoxes.get(id).getChildren().add(claimedPercentage);

        Label livesLabel = new Label();
        livesLabels.add(livesLabel);
        playerBoxes.get(id).getChildren().add(livesLabel);

        Label score = new Label();
        scoreLabels.add(score);
        playerBoxes.get(id).getChildren().add(score);

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
     * @param id         of the cursor
     */
    public void addScore(int scoreInput, int id) {
        scores.set(id, scores.get(id) + scoreInput);
        setTotalScore(scoreInput);
        scoreLabels.get(id).setText("Score: " + String.valueOf(scores.get(id)));
    }

    /**
     * Set the current score amount.
     *
     * @param scoreInput of the new area
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
    private void addClaimedPercentage(double claimedPercentageInput, int id) {
        claimedPercentages.set(id, claimedPercentages.get(id) + claimedPercentageInput);
        setTotalClaimedPercentageLabel();
        claimedPercentageLabels.get(id).setText(Math.round(claimedPercentages.get(id)) + "%");
    }

    /**
     * Display claimed percentage with a % sign.
     */
    private void setTotalClaimedPercentageLabel() {
        double percentage = 0;
        for (Double i : claimedPercentages) {
            percentage += i;
        }
        totalClaimedPercentageLabel.setText("Claimed: " + Math.round(percentage)
                + "% of " + GameController.getInstance()
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
            int id = scoreCounter.getId();
            addScore(scoreCounter.getRecentScore(), id);
            addClaimedPercentage(scoreCounter.getRecentPercentage(), id);
            LOGGER.log(Level.WARNING, "Score updated "
                    + id, this.getClass());
            if (arg instanceof Integer) {
                setLivesLabel((int) arg, id);
            }
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
     * @param id of the cursor
     */
    public void setLivesLabel(int lives, int id) {
        livesLabels.get(id).setText("Lives: " + lives);
    }
}
