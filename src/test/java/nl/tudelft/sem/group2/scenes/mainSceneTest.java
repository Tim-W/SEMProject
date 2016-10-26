package nl.tudelft.sem.group2.scenes;

import javafx.embed.swing.JFXPanel;
import nl.tudelft.sem.group2.JavaFXThreadingRule;
import nl.tudelft.sem.group2.controllers.GameController;
import nl.tudelft.sem.group2.units.Cursor;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.awt.Point;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


/**
 * Created by gijs on 30-9-2016.
 */
public class mainSceneTest {
    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
    private GameScene scene;
    private GameController gameController;
    private Cursor spyCursor;

    @BeforeClass
    public static void BeforeClass() {
        new JFXPanel();
    }

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

    @Test
    public void testDrawStixAndFuseVerify() throws Exception {
        setUp();
        spyCursor.getStix().addToStix(new Point(spyCursor.getX(), spyCursor.getY()));
        gameController.getCursors().get(0).getStix();
        spyCursor.getStix().addToStix(new Point(spyCursor.getX(), spyCursor.getY() + 1));
        scene.draw();
        verify(spyCursor, times(1)).isFast();

    }

    @Test
    public void testDrawStixAndFuseVerifyNot() throws Exception {
        setUp();
        spyCursor.getStix().addToStix(new Point(spyCursor.getX(), spyCursor.getY()));
        spyCursor.handleFuse();
        scene.draw();
        verify(spyCursor, times(0)).isFast();
    }

}