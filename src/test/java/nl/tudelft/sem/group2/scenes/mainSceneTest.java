package nl.tudelft.sem.group2.scenes;

import nl.tudelft.sem.group2.JavaFXThreadingRule;
import nl.tudelft.sem.group2.controllers.GameController;
import nl.tudelft.sem.group2.units.Cursor;
import org.junit.Rule;

import static org.mockito.Mockito.spy;


/**
 * Created by gijs on 30-9-2016.
 */
public class mainSceneTest {
    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
    private GameScene scene;
    private GameController gameController;
    private Cursor spyCursor;

    public void setUp() {
        GameController.deleteGameController();
        gameController = GameController.getInstance();
        gameController.initializeSinglePlayer();
        gameController.getAnimationTimer().stop();
        scene = gameController.getGameScene();
        spyCursor = spy(gameController.getCursors().get(0));
        gameController.getCursors().set(0, spyCursor);
        gameController.getUnits().clear();
        gameController.getUnits().add(spyCursor);
    }


}