package nl.tudelft.sem.group2.scenes;

import javafx.embed.swing.JFXPanel;
import nl.tudelft.sem.group2.controllers.GameController;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Fuse;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.Point;

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
        new JFXPanel();
    }

    public void setUp() {

        //Group root = new Group();
        //Scene s = new Scene(root, 300, 300, Color.BLACK);
        removeGameController();
        gameController = GameController.getInstance();
        gameController.getAnimationTimer().stop();
        scene = gameController.getScene();
        spyCursor = spy(gameController.getCursor());
        gameController.setCursor(spyCursor);
    }

    private void removeGameController() {
         GameController.deleteGameController();
    }

    @Test
    public void testDrawStixAndFuseVerify() throws Exception {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setUp();
                gameController.getStix().addToStix(new Point(1, 1));
                gameController.getStix().addToStix(new Point(1, 2));
                scene.removeFuse();
                gameController.getUnits().add(new Fuse(1, 2, 1, 1, gameController.getStix(), gameController.getAreaTracker()));
                scene.draw();
                verify(spyCursor, times(1)).isFast();
            }
        };
        runnable.run();

    }

    @Test
    public void testDrawStixAndFuseVerifyNot() throws Exception {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setUp();
                gameController.getStix().addToStix(new Point(1, 1));
                gameController.getStix().addToStix(new Point(1, 3));
                scene.removeFuse();
                gameController.getUnits().add(new Fuse(1, 2, 1, 1, gameController.getStix(), gameController.getAreaTracker()));
                scene.draw();
                verify(spyCursor, times(0)).isFast();
            }
        };
        runnable.run();
    }
    @Test
    public void testRemoveFuse() throws Exception {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setUp();
                int oldSize = gameController.getUnits().size();
                scene.removeFuse();
                gameController.getUnits().add(new Fuse(1, 2, 1, 1, gameController.getStix(), gameController.getAreaTracker()));
                scene.removeFuse();
                Assert.assertEquals(oldSize, gameController.getUnits().size());
            }
        };
        runnable.run();

    }

}