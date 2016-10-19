package nl.tudelft.sem.group2.keyboard;

import javafx.scene.input.KeyCode;
import nl.tudelft.sem.group2.controllers.GameController;

public class SpaceHandler implements KeyboardHandler {

    @Override
    public void execute(KeyCode code) {
        GameController.getInstance().startGame();
    }
}
