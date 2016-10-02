package nl.tudelft.sem.group2.controllers;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.scenes.GameScene;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Fuse;
import nl.tudelft.sem.group2.units.Stix;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.Point;

import static nl.tudelft.sem.group2.controllers.GameController.getCursor;
import static nl.tudelft.sem.group2.controllers.GameController.getScoreCounter;
import static nl.tudelft.sem.group2.controllers.GameController.getStix;
import static nl.tudelft.sem.group2.controllers.GameController.setCursor;
import static nl.tudelft.sem.group2.scenes.GameScene.getUnits;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by gijs on 2-10-2016.
 */
public class GameControllerTest {
    GameController gameController;
    GameScene gameScene;
    @Before
    public void setUp() throws Exception {
        new JFXPanel();
        gameScene = new GameScene(new Group(), Color.BLACK);
        gameController = new GameController();
    }

    @Test
    public void keyPressedSpace() throws Exception {
        boolean isRunning = gameController.isRunning();
        gameController.keyPressed(new KeyEvent(null,null,KeyEvent.KEY_PRESSED," ","", KeyCode.SPACE,false,false,
                false,false));
        Assert.assertEquals(!isRunning,gameController.isRunning());
    }
    @Test
    public void keyPressedArrow() throws Exception {
        GameScene.removeFuse();
        Fuse fuse = spy(new Fuse(1,1,1,1,new Stix()));
        getUnits().add(fuse);
        gameController.keyPressed(new KeyEvent(null,null,KeyEvent.KEY_PRESSED," ","", KeyCode.RIGHT,false,false,
                false,false));
        verify(fuse,times(1)).setMoving(false);
    }
    @Test
    public void keyPressedX() throws Exception {

        Cursor cursor = spy(new Cursor(1,1,1,1,new Stix()));
        setCursor(cursor);
        gameController.keyPressed(new KeyEvent(null,null,KeyEvent.KEY_PRESSED," ","", KeyCode.X,false,false,
                false,false));
        verify(cursor,times(1)).setSpeed(1);
    }
    @Test
    public void keyPressedZ() throws Exception {

        Cursor cursor = spy(new Cursor(1,1,1,1,new Stix()));
        setCursor(cursor);
        gameController.keyPressed(new KeyEvent(null,null,KeyEvent.KEY_PRESSED," ","", KeyCode.Z,false,false,
                false,false));
        verify(cursor,times(1)).setSpeed(2);
    }
    @Test
    public void keyPressedY() throws Exception {

        Cursor cursor = spy(new Cursor(1,1,1,1,new Stix()));
        setCursor(cursor);
        gameController.keyPressed(new KeyEvent(null,null,KeyEvent.KEY_PRESSED," ","", KeyCode.Y,false,false,
                false,false));
        verify(cursor,times(0)).setSpeed(2);
    }
    @Test
    public void keyReleasedCurrentMove() throws Exception {
        GameScene.removeFuse();
        Fuse fuse = spy(new Fuse(1,1,1,1,new Stix()));
        getUnits().add(fuse);
        getCursor().setCurrentMove(KeyCode.RIGHT);
        getStix().addToStix(new Point(getCursor().getX(),getCursor().getY()));
        gameController.keyReleased(new KeyEvent(null,null,KeyEvent.KEY_RELEASED," ","", getCursor().getCurrentMove(),false,false,
                false,false));
        verify(fuse,times(1)).setMoving(true);
    }
    @Test
    public void keyReleasedCurrentMoveCreateFuse() throws Exception {
        int size = getUnits().size();
        GameScene.removeFuse();
        getCursor().setCurrentMove(KeyCode.RIGHT);
        getStix().addToStix(new Point(getCursor().getX(),getCursor().getY()));
        gameController.keyReleased(new KeyEvent(null,null,KeyEvent.KEY_RELEASED," ","", getCursor().getCurrentMove(),false,false,
                false,false));
        Assert.assertEquals(size+1,getUnits().size());
    }
    @Test
    public void keyReleasedCurrentMoveNoCoordinates() throws Exception {
        GameScene.removeFuse();
        Fuse fuse = spy(new Fuse(1,1,1,1,new Stix()));
        getUnits().add(fuse);
        getCursor().setCurrentMove(KeyCode.RIGHT);
        getStix().addToStix(new Point(getCursor().getX()+1,getCursor().getY()));
        gameController.keyReleased(new KeyEvent(null,null,KeyEvent.KEY_RELEASED," ","", getCursor().getCurrentMove(),false,false,
                false,false));
        verify(fuse,times(0)).setMoving(true);
    }
    @Test
    public void keyReleasedX() throws Exception {
        GameScene.removeFuse();
        Fuse fuse = spy(new Fuse(1,1,1,1,new Stix()));
        getUnits().add(fuse);
        Cursor cursor = spy(new Cursor(1,1,1,1,new Stix()));
        setCursor(cursor);
        getCursor().setCurrentMove(KeyCode.RIGHT);
        getStix().addToStix(new Point(getCursor().getX(),getCursor().getY()));
        gameController.keyReleased(new KeyEvent(null,null,KeyEvent.KEY_RELEASED," ","", KeyCode.X,false,false,
                false,false));
        verify(cursor,times(1)).setDrawing(false);
    }
    @Test
    public void keyReleasedZ() throws Exception {
        GameScene.removeFuse();
        Fuse fuse = spy(new Fuse(1,1,1,1,new Stix()));
        getUnits().add(fuse);
        Cursor cursor = spy(new Cursor(1,1,1,1,new Stix()));
        setCursor(cursor);
        getCursor().setCurrentMove(KeyCode.RIGHT);
        getStix().addToStix(new Point(getCursor().getX(),getCursor().getY()));
        gameController.keyReleased(new KeyEvent(null,null,KeyEvent.KEY_RELEASED," ","", KeyCode.Z,false,false,
                false,false));
        verify(cursor,times(1)).setDrawing(false);
    }
    @Test
    public void keyReleasedY() throws Exception {
        Cursor cursor = spy(new Cursor(1,1,1,1,new Stix()));
        setCursor(cursor);
        getCursor().setCurrentMove(KeyCode.RIGHT);
        getStix().addToStix(new Point(getCursor().getX(),getCursor().getY()));
        gameController.keyReleased(new KeyEvent(null,null,KeyEvent.KEY_RELEASED," ","", KeyCode.Y,false,false,
                false,false));
        verify(cursor,times(0)).setDrawing(false);
    }
    @Test
    public void gameWon() throws Exception {
        getScoreCounter().setTargetPercentage(1);
        getScoreCounter().setTotalPercentage(2);
        gameController = spy(new GameController());
        verify(gameController,times(1)).gameWon();
    }

}