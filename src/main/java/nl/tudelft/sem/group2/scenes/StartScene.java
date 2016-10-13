package nl.tudelft.sem.group2.scenes;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.LaunchApp;
import nl.tudelft.sem.group2.controllers.GameController;
import nl.tudelft.sem.group2.global.Globals;

/**
 * StartScene that lets you pick single or multiplayer game.
 */
public class StartScene extends javafx.scene.Scene {

    private Button singleButton;
    private Button multiButton;


    /**
     * Constructor for StartScene.
     *
     * @param root            the root view
     * @param width           width of the scene
     * @param height          height of the scene
     * @param backgroundColor the background color of the scene
     */
    public StartScene(Group root, double width, double height, Color backgroundColor) {
        super(root, width, height, backgroundColor);

        VBox vBox = new VBox(0);
        vBox.setPrefWidth(width);
        vBox.setPrefHeight(height);
        vBox.setAlignment(Pos.CENTER);
        vBox.setLayoutY(Globals.STARTSCENE_VBOX_LAYOUT_Y);

        HBox hBox = new HBox(Globals.STARTSCENE_HBOX_SPACING);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPrefWidth(width);
        hBox.setPrefHeight(height);

        ImageView image = new ImageView("/images/logo.png");

        handleSinglePlayerButton();
        handleMultiplayerButton();

        hBox.getChildren().add(singleButton);
        hBox.getChildren().add(multiButton);

        this.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DIGIT1) {
                LaunchApp.setScene(GameController.getInstance().getScene());
            } else if (event.getCode() == KeyCode.DIGIT2) {
                //TODO change to multiplayer code
                LaunchApp.setScene(GameController.getInstance().getScene());
            }
        });

        vBox.getChildren().add(image);
        vBox.getChildren().add(hBox);
        root.getChildren().add(vBox);
    }

    private void handleMultiplayerButton() {
        multiButton = new Button("MultiPlayer");
        multiButton.setTextFill(Color.YELLOW);
        multiButton.setStyle("-fx-background-color: #707070; -fx-font-size: 20px");

        multiButton.setOnMouseClicked(event -> {
            //TODO code for multiplayer
        });
    }

    private void handleSinglePlayerButton() {

        singleButton = new Button("Single Player");
        singleButton.setTextFill(Color.YELLOW);
        singleButton.setStyle("-fx-background-color: #707070; -fx-font-size: 20px");

        singleButton.setOnMouseClicked(event -> LaunchApp.setScene(GameController.getInstance().getScene()));
    }
}
