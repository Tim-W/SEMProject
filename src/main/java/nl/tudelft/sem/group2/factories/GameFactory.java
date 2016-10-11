package nl.tudelft.sem.group2.factories;

import nl.tudelft.sem.group2.controllers.GameController;
import nl.tudelft.sem.group2.scenes.GameScene;

public interface GameFactory {
    GameScene createGameScene();

    GameController createGameController();
}
