package nl.tudelft.sem.group2.scenes;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Fuse;
import nl.tudelft.sem.group2.units.Stix;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.Point;

import static nl.tudelft.sem.group2.controllers.GameController.getCursor;
import static nl.tudelft.sem.group2.controllers.GameController.getStix;
import static nl.tudelft.sem.group2.controllers.GameController.setCursor;
import static nl.tudelft.sem.group2.scenes.GameScene.addUnit;
import static nl.tudelft.sem.group2.scenes.GameScene.draw;
import static nl.tudelft.sem.group2.scenes.GameScene.getUnits;
import static nl.tudelft.sem.group2.scenes.GameScene.removeFuse;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


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
    /*@Test
    public void constructorAreatracker() throws Exception {
        Assert.assertTrue(getAreaTracker()!=null);
    }
    @Test
    public void constructorScoreCounter() throws Exception {
        Assert.assertTrue(getScoreCounter()!=null);
    }*/

    /**
     * test addUnit not to add two fuses
     * @throws Exception
     */
    @Test
    public void testAddUnit() throws Exception {
        int oldLength = getUnits().size();
        Fuse fuse = new Fuse(1,1,1,1,new Stix());
        addUnit(fuse);
        Assert.assertTrue(getUnits().contains(fuse));
        addUnit(new Fuse(1,1,1,1,new Stix()));
        Assert.assertEquals(oldLength+1,getUnits().size());
    }
    @Test
    public void testDrawStixAndFuseVerify() throws Exception {
        Cursor spyCursor = spy(getCursor());
        setCursor(spyCursor);
        getStix().addToStix(new Point(1,1));
        getStix().addToStix(new Point(1,2));
        addUnit(new Fuse(1,2,1,1,getStix()));
        draw();
        verify(spyCursor,times(1)).isFast();
    }
    @Test
    public void testDrawStixAndFuseVerifyNot() throws Exception {
        Cursor spyCursor = spy(getCursor());
        setCursor(spyCursor);
        getStix().addToStix(new Point(1,1));
        getStix().addToStix(new Point(1,3));
        addUnit(new Fuse(1,2,1,1,getStix()));
        draw();
        verify(spyCursor,times(0)).isFast();
    }
    @Test
    public void testRemoveFuse() throws Exception {
        Fuse fuse = new Fuse(1,2,1,1,getStix());
        int oldSize = getUnits().size();
        addUnit(fuse);
        removeFuse();
        Assert.assertEquals(oldSize,getUnits().size());

    }

}