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
import nl.tudelft.sem.group2.Logger;
import nl.tudelft.sem.group2.ScoreCounter;
import nl.tudelft.sem.group2.global.Globals;

import java.util.Observable;
import java.util.Observer;

/**
 * Displays info about the current score and gained percentage.
 */
public class ScoreScene extends SubScene implements Observer {

    private static final Logger LOGGER = Logger.getLogger();

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
    private Color color = Color.YELLOW;
    private int highClaimedPercentage = 0;

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
        setLivesLabel(0);
    }

    private void displayTitle() {
        title = new ImageView("/images/logo.png");
        title.setFitWidth(TITLE_FIT_WIDTH);
        title.setFitHeight(TITLE_FIT_HEIGHT);
    }

    private void displayClaimedPercentage() {
        claimedPercentage = new Label();
        claimedPercentage.setTextFill(color);
        claimedPercentage.setStyle("-fx-font-size:14;");
    }

    private void createScoreLabel() {
        score = new Label("0");
        score.setTextFill(color);
        score.setStyle("-fx-font-size:24;");
    }

    private void setClaimedText() {
        claimed.setTextFill(color);
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

    private void setColor(Color color) {
        this.color = color;
        claimedPercentage.setTextFill(color);
        score.setTextFill(color);
        claimed.setTextFill(color);
    }

    /**
     * Update the info on the scorescene with actual info from scorecounter.
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {

        if (o instanceof ScoreCounter) {
            if ((int) (((ScoreCounter) o).getTotalPercentage() * 100) >= highClaimedPercentage) {

                setScore(((ScoreCounter) o).getTotalScore());
                setClaimedPercentage((int) (((ScoreCounter) o).getTotalPercentage() * 100));
                /**
                 LOGGER.log(Level.WARNING, "Score updated "
                 + color.toString(), this.getClass());
                 **/
                setColor(((ScoreCounter) o).getColor());
                setLivesLabel(((ScoreCounter) o).getLives());
            }
        }
    }

    /**
     * setter for the lives label.
     *
     * @param lives the amount of lives the player has left
     */
    private void setLivesLabel(int lives) {
        livesLabel.setText("Lives: " + lives);
    }
}
