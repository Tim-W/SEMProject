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
import nl.tudelft.sem.group2.global.Globals;

/**
 * Displays info about the current score and gained percentage.
 */
public class ScoreScene extends SubScene {

    //standard target percentage
    private static final int CENTER_SPACING = -5;
    private static final int TITLE_FIT_HEIGHT = 40;
    private static final int TITLE_FIT_WIDTH = 100;
    private static final int TITLE_WIDTH = 113;
    private static final int TITLE_PADDING = 20;
    private static final int TITLE_VGAP = 10;
    private TilePane tilePane;
    private Label score;
    private Label claimedPercentage;
    private Label livesLabel;
    private ImageView title;
    private Label claimed = new Label("Claimed");

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

        setClaimedText();
        createTitlePane();

        VBox left = new VBox();
        VBox center = new VBox();

        center.setSpacing(CENTER_SPACING);
        VBox right = new VBox();
        right.setAlignment(Pos.CENTER_LEFT);

        createScoreLabel();

        displayClaimedPercentage();

        createLivesLabel();

        displayTitle();
        //TODO Fix font
        //Font f = Font.loadFont(LaunchApp.class.getResource("qixfont.ttf").toExternalForm(),12);
        //title.setFont(f);

        left.getChildren().add(title);
        center.getChildren().add(claimed);
        center.getChildren().add(claimedPercentage);
        center.getChildren().add(livesLabel);
        right.getChildren().add(score);

        tilePane.getChildren().add(left);
        tilePane.getChildren().add(center);
        tilePane.getChildren().add(right);


        root.getChildren().add(tilePane);
    }

    private void displayTitle() {
        title = new ImageView("/images/logo.png");
        title.setFitWidth(TITLE_FIT_WIDTH);
        title.setFitHeight(TITLE_FIT_HEIGHT);
    }

    private void displayClaimedPercentage() {
        claimedPercentage = new Label();
        claimedPercentage.setTextFill(Color.YELLOW);
        claimedPercentage.setStyle("-fx-font-size:14;");
    }

    private void createScoreLabel() {
        score = new Label("0");
        score.setTextFill(Color.WHITE);
        score.setStyle("-fx-font-size:24;");
    }

    private void setClaimedText() {
        claimed.setTextFill(Color.YELLOW);
        claimed.setStyle("-fx-font-size:14;");
    }

    private void createLivesLabel() {
        livesLabel = new Label();
        livesLabel.setTextFill(Color.YELLOW);
        livesLabel.setStyle("-fx-font-size:14;");
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
    public void setScore(int scoreInput) {
        score.setText(String.valueOf(scoreInput));
    }

    /**
     * Display claimed percentage with a % sign.
     *
     * @param claimedPercentageInput the claimed percentage in XX%, so no decimals
     */
    public void setClaimedPercentage(int claimedPercentageInput) {
        claimedPercentage.setText(
                String.valueOf(claimedPercentageInput) + "% of " + String.valueOf(Globals.TARGET_PERCENTAGE * 100) + "%"
        );
    }

    /**
     * setter for the lives label.
     *
     * @param lives the amount of lives the player has left
     */
    public void setLivesLabel(int lives) {
        livesLabel.setText("Lives: " + lives);
    }
}
