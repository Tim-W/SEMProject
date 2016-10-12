package nl.tudelft.sem.group2.factories;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.controllers.GameController;
import nl.tudelft.sem.group2.scenes.GameScene;

public class SinglePlayerGameFactory implements GameFactory {
    @Override
    public GameScene createGameScene() {
        return null;
    }

    @Override
    public GameController createGameController() {
        return null;
    }
}
