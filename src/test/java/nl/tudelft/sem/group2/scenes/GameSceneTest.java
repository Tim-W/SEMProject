package nl.tudelft.sem.group2.scenes;

import nl.tudelft.sem.group2.LaunchApp;
import nl.tudelft.sem.group2.controllers.GameController;
import nl.tudelft.sem.group2.units.Cursor;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.Point;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


/**
 * Created by gijs on 30-9-2016.
 */
public class GameSceneTest {
    private GameScene scene;
    private GameController gameController;
    private Cursor spyCursor;

    @BeforeClass
    public static void BeforeClass() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                LaunchApp.launch();
            }
        });
        thread.start();
    }

    public void setUp() {

        //Group root = new Group();
        //Scene s = new Scene(root, 300, 300, Color.BLACK);
        removeGameController();
        gameController = GameController.getInstance();
        gameController.makeCursor();
        gameController.getAnimationTimer().stop();
        scene = gameController.getScene();
        spyCursor = spy(gameController.getCursors().get(0));
        gameController.getCursors().set(0, spyCursor);
        gameController.getUnits().clear();
        gameController.getUnits().add(spyCursor);
    }

    private void removeGameController() {
        GameController.deleteGameController();
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