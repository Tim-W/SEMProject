package nl.tudelft.sem.group2.scenes;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.units.Unit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static nl.tudelft.sem.group2.scenes.GameScene.getAreaTracker;
import static nl.tudelft.sem.group2.scenes.GameScene.getScoreCounter;

/**
 * Created by gijs on 30-9-2016.
 */
public class GameSceneTest {
    GameScene scene;
    @Before
    public void setUp() throws Exception {
        new JFXPanel();
        Group root = new Group();
        scene = new GameScene(root, Color.BLACK);
    }
    @Test
    public void constructorBoardSize() throws Exception {
       Set<Unit> set =  scene.getBoard().getUnits();
        Assert.assertEquals(4,set.size());
    }
    @Test
    public void constructorAreatracker() throws Exception {
        Assert.assertTrue(getAreaTracker()!=null);
    }
    @Test
    public void constructorScoreCounter() throws Exception {
        Assert.assertTrue(getScoreCounter()!=null);
    }
    /*@Test
    public void handle() throws Exception {
        KeyEvent key = new KeyEvent(scene, KeyEvent.KEY_PRESSED,"", "",  KeyEvent.KEY_PRESSED,'Z');
        scene.getOnKeyPressed().handle(key);
    }*/

    @Test
    public void getAnimationTimer() throws Exception {

    }

    /*@Test
    public void getAreaTracker() throws Exception {

    }*/
/*
    @Test
    public void getScoreCounter() throws Exception {

    }*/

    @Test
    public void getQixCursor() throws Exception {

    }

    @Test
    public void createAnimationTimer() throws Exception {

    }

    @Test
    public void gameOver() throws Exception {

    }

    @Test
    public void gameWon() throws Exception {

    }

    @Test
    public void isRunning() throws Exception {

    }

}