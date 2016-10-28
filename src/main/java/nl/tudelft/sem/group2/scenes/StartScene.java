package nl.tudelft.sem.group2.scenes;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import nl.tudelft.sem.group2.controllers.GameController;
import nl.tudelft.sem.group2.global.Globals;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * StartScene that lets you pick single or multiplayer game.
 */
public class StartScene extends javafx.scene.Scene {

    private Button singleButton;
    private Button multiButton;
    private Button helpButton;
    private FlowPane helpTextWrapper;
    private Stage stage;


    /**
     * Constructor for StartScene.
     *
     * @param root            the root view
     * @param width           width of the scene
     * @param height          height of the scene
     * @param backgroundColor the background color of the scene
     * @param stage           primary stage where new scenes should be added to
     */
    public StartScene(Group root, double width, double height, Color backgroundColor, Stage stage) {
        super(root, width, height, backgroundColor);

        this.stage = stage;

        VBox vBox = new VBox(Globals.STARTSCENE_SPACING);
        vBox.setPrefWidth(width);
        vBox.setPrefHeight(height / 2);
        vBox.setAlignment(Pos.CENTER);
        vBox.setLayoutY(Globals.STARTSCENE_VBOX_LAYOUT_Y);

        HBox hBox = new HBox(Globals.STARTSCENE_HBOX_SPACING);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPrefWidth(width);
        hBox.setPrefHeight(height);

        ImageView image = new ImageView("/images/logo.png");

        handleSinglePlayerButton();
        handleMultiPlayerButton();
        handleHelpButton();
        handleHelpText(width, height);
        handleKeyPresses();

        VBox buttonsBox = new VBox(Globals.STARTSCENE_SPACING);
        hBox.getChildren().add(singleButton);
        hBox.getChildren().add(multiButton);

        HBox helpButtonWrapper = new HBox();
        helpButtonWrapper.getChildren().add(helpButton);
        helpButtonWrapper.setAlignment(Pos.CENTER);

        buttonsBox.getChildren().add(hBox);
        buttonsBox.getChildren().add(helpButtonWrapper);

        vBox.getChildren().add(image);
        vBox.getChildren().add(buttonsBox);
        root.getChildren().add(vBox);
        root.getChildren().add(helpTextWrapper);
    }

    private void handleSinglePlayerButton() {

        singleButton = new Button("1 player");
        singleButton.setTextFill(Color.YELLOW);
        singleButton.setStyle("-fx-background-color: #707070; -fx-font-size: 20px");
        singleButton.setPrefWidth(Globals.STARTSCENE_BUTTON_WIDTH);

        singleButton.setOnMouseEntered(event ->
                singleButton.setStyle("-fx-background-color: #5d5d5d; -fx-font-size: 20px"));
        singleButton.setOnMouseExited(event ->
                singleButton.setStyle("-fx-background-color: #707070; -fx-font-size: 20px"));

        singleButton.setOnMouseClicked(event -> {
            stage.setScene(GameController.getInstance().getScene());
            GameController.getInstance().makeCursors(false);
        });
    }

    private void handleMultiPlayerButton() {
        multiButton = new Button("2 players");
        multiButton.setTextFill(Color.YELLOW);
        multiButton.setStyle("-fx-background-color: #707070; -fx-font-size: 20px; ");
        multiButton.setPrefWidth(Globals.STARTSCENE_BUTTON_WIDTH);

        multiButton.setOnMouseEntered(event ->
                multiButton.setStyle("-fx-background-color: #5d5d5d; -fx-font-size: 20px"));
        multiButton.setOnMouseExited(event ->
                multiButton.setStyle("-fx-background-color: #707070; -fx-font-size: 20px"));

        multiButton.setOnMouseClicked(event -> {
            stage.setScene(GameController.getInstance().getScene());
            GameController.getInstance().makeCursors(true);
        });
    }

    private void handleHelpButton() {
        helpButton = new Button("Help");
        helpButton.setTextFill(Color.YELLOW);
        helpButton.setStyle("-fx-background-color: #707070; -fx-font-size: 20px; ");
        helpButton.setPrefWidth(Globals.STARTSCENE_BUTTON_WIDTH);

        helpButton.setOnMouseEntered(event ->
                helpButton.setStyle("-fx-background-color: #5d5d5d; -fx-font-size: 20px"));
        helpButton.setOnMouseExited(event ->
                helpButton.setStyle("-fx-background-color: #707070; -fx-font-size: 20px"));

        helpButton.setOnMouseClicked(event -> helpTextWrapper.setVisible(true));
    }

    private void handleHelpText(double width, double height) {

        Text helpText = new Text(Globals.HELP_SCREEN_MESSAGE);
        helpText.setFill(Color.YELLOW);
        helpText.setWrappingWidth(Globals.STARTSCENE_HELPTEXT_WRAPPING);
        helpText.setStyle("-fx-font-size: 16px");

        Hyperlink hyperlink = new Hyperlink("Click here to read more!");
        hyperlink.setOnMouseClicked(event -> {
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(new URI("http://strategywiki.org/wiki/Qix"));
                }
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
                createPopup();
            }
        });
        //hyperlink.setTextFill(Color.WHITE);
        hyperlink.setLayoutX(Globals.STARTSCENE_HBOX_SPACING);

        helpTextWrapper = new FlowPane();
        helpTextWrapper.setMinHeight(height);
        helpTextWrapper.setMinWidth(width);
        helpTextWrapper.setVisible(false);
        helpTextWrapper.setStyle("-fx-background-color: black");
        helpTextWrapper.getChildren().add(helpText);
        helpTextWrapper.getChildren().add(hyperlink);
        helpTextWrapper.setLayoutX(Globals.STARTSCENE_SPACING);
        helpTextWrapper.setLayoutY(Globals.STARTSCENE_SPACING);
    }

    private void createPopup() {
        final Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Unfortunately this feature is not working at your system");
        alert.setHeaderText("Can't open browser...");
        alert.initOwner(stage);
        alert.showAndWait();
    }

    private void handleKeyPresses() {
        this.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DIGIT1) {
                stage.setScene(GameController.getInstance().getScene());
                GameController.getInstance().makeCursors(false);
            } else if (event.getCode() == KeyCode.DIGIT2) {
                stage.setScene(GameController.getInstance().getScene());
                GameController.getInstance().makeCursors(true);
            } else if (event.getCode() == KeyCode.H) {
                helpTextWrapper.setVisible(true);
            } else if (event.getCode() == KeyCode.ESCAPE) {
                if (helpTextWrapper.isVisible()) {
                    helpTextWrapper.setVisible(false);
                } else {
                    System.exit(0);
                }
            }
        });
    }
}
