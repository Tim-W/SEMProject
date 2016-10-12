package nl.tudelft.sem.group2.controllers;

import javafx.embed.swing.JFXPanel;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import nl.tudelft.sem.group2.LaunchApp;
import nl.tudelft.sem.group2.scenes.GameScene;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Fuse;
import nl.tudelft.sem.group2.units.Stix;
import nl.tudelft.sem.group2.units.Unit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import static nl.tudelft.sem.group2.controllers.GameController.getCursor;
import static nl.tudelft.sem.group2.controllers.GameController.setCursor;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by gijs on 2-10-2016.
 */
public class GameControllerTest {
    GameController gameController;

    @Before
    public void setUp() throws Exception {
        new JFXPanel();
        LaunchApp.scene = mock(GameScene.class);
        gameController = new GameController();
        Set<Unit> units = new HashSet<>();
        when(LaunchApp.scene.getUnits()).thenReturn(units);
    }

    @Test
    public void keyPressedSpace() throws Exception {
        boolean isRunning = gameController.isRunning();
        gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.SPACE, false, false,
                false, false));
        Assert.assertEquals(!isRunning, gameController.isRunning());
    }

    @Test
    public void keyPressedArrow() throws Exception {
        LaunchApp.scene.removeFuse();
        Fuse fuse = spy(new Fuse(1, 1, 1, 1, new Stix()));
        LaunchApp.scene.getUnits().add(fuse);
        Cursor cursor = new Cursor(1, 1, 1, 1, new Stix());
        setCursor(cursor);
        cursor.setDrawing(true);
        gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.RIGHT, false, false,
                false, false));
        verify(fuse, times(1)).setMoving(false);
        //verify(fuse,times(2)).getUnits();
    }

    @Test
    public void keyPressedX() throws Exception {

        Cursor cursor = spy(new Cursor(1, 1, 1, 1, new Stix()));
        setCursor(cursor);
        gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.X, false, false,
                false, false));
        verify(cursor, times(1)).setSpeed(1);
    }

    @Test
    public void keyPressedXNotFast() throws Exception {
        Stix stix = new Stix();
        stix.addToStix(new Point(1, 1));
        gameController.setStix(stix);
        Cursor cursor = spy(new Cursor(1, 1, 1, 1, stix));
        setCursor(cursor);
        cursor.setFast(false);
        gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.X, false, false,
                false, false));
        verify(cursor, times(1)).setSpeed(1);
    }

    @Test
    public void keyPressedXNotFastNothing() throws Exception {
        Stix stix = new Stix();
        stix.addToStix(new Point(1, 1));
        gameController.setStix(stix);
        Cursor cursor = spy(new Cursor(1, 1, 1, 1, stix));
        setCursor(cursor);
        cursor.setFast(true);
        gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.X, false, false,
                false, false));
        verify(cursor, times(0)).setSpeed(1);
    }

    @Test
    public void keyPressedZ() throws Exception {

        Cursor cursor = spy(new Cursor(1, 1, 1, 1, new Stix()));
        setCursor(cursor);
        gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.Z, false, false,
                false, false));
        verify(cursor, times(1)).setSpeed(2);
    }

    @Test
    public void keyPressedY() throws Exception {

        Cursor cursor = spy(new Cursor(1, 1, 1, 1, new Stix()));
        setCursor(cursor);
        gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.Y, false, false,
                false, false));
        verify(cursor, times(0)).setSpeed(2);
    }

    @Test
    public void keyReleasedCurrentMove() throws Exception {
        LaunchApp.scene.removeFuse();
        Fuse fuse = spy(new Fuse(1, 1, 1, 1, new Stix()));
        LaunchApp.scene.getUnits().add(fuse);
        getCursor().setCurrentMove(KeyCode.RIGHT);
        gameController.getStix().addToStix(new Point(getCursor().getX(), getCursor().getY()));
        gameController.keyReleased(new KeyEvent(null, null, KeyEvent.KEY_RELEASED, " ", "", getCursor().getCurrentMove(), false, false,
                false, false));
        verify(fuse, times(1)).setMoving(true);
    }

    @Test
    public void testHandle() throws Exception {

        GameScene mock = mock(GameScene.class);
        LaunchApp.scene = mock;
        int previoustime = 1;
        gameController.setPreviousTime(previoustime);
        gameController.getAnimationTimer().handle(previoustime + 200000000);
        verify(mock, times(1)).draw();
    }
    @Test
    public void keyReleasedCurrentMoveCreateFuse() throws Exception {
        LaunchApp.scene.removeFuse();
        gameController.setStix(spy(gameController.getStix()));
        getCursor().setCurrentMove(KeyCode.RIGHT);
        gameController.getStix().addToStix(new Point(getCursor().getX(), getCursor().getY()));
        gameController.keyReleased(new KeyEvent(null, null, KeyEvent.KEY_RELEASED, " ", "", getCursor().getCurrentMove(), false, false,
                false, false));
        verify(gameController.getStix(), times(3)).getStixCoordinates();
    }

    @Test
    public void keyReleasedCurrentMoveNoCoordinates() throws Exception {
        LaunchApp.scene.removeFuse();
        Fuse fuse = spy(new Fuse(1, 1, 1, 1, new Stix()));
        LaunchApp.scene.getUnits().add(fuse);
        getCursor().setCurrentMove(KeyCode.RIGHT);
        gameController.getStix().addToStix(new Point(getCursor().getX() + 1, getCursor().getY()));
        gameController.keyReleased(new KeyEvent(null, null, KeyEvent.KEY_RELEASED, " ", "", getCursor().getCurrentMove(), false, false,
                false, false));
        verify(fuse, times(0)).setMoving(true);
    }

    @Test
    public void keyReleasedX() throws Exception {
        LaunchApp.scene.removeFuse();
        Fuse fuse = spy(new Fuse(1, 1, 1, 1, new Stix()));
        LaunchApp.scene.getUnits().add(fuse);
        Cursor cursor = spy(new Cursor(1, 1, 1, 1, new Stix()));
        setCursor(cursor);
        getCursor().setCurrentMove(KeyCode.RIGHT);
        gameController.getStix().addToStix(new Point(getCursor().getX(), getCursor().getY()));
        gameController.keyReleased(new KeyEvent(null, null, KeyEvent.KEY_RELEASED, " ", "", KeyCode.X, false, false,
                false, false));
        verify(cursor, times(1)).setDrawing(false);
    }

    @Test
    public void keyReleasedZ() throws Exception {
        LaunchApp.scene.removeFuse();
        Fuse fuse = spy(new Fuse(1, 1, 1, 1, new Stix()));
        LaunchApp.scene.getUnits().add(fuse);
        Cursor cursor = spy(new Cursor(1, 1, 1, 1, new Stix()));
        setCursor(cursor);
        getCursor().setCurrentMove(KeyCode.RIGHT);
        gameController.getStix().addToStix(new Point(getCursor().getX(), getCursor().getY()));
        gameController.keyReleased(new KeyEvent(null, null, KeyEvent.KEY_RELEASED, " ", "", KeyCode.Z, false, false,
                false, false));
        verify(cursor, times(1)).setDrawing(false);
    }

    @Test
    public void keyReleasedY() throws Exception {
        Cursor cursor = spy(new Cursor(1, 1, 1, 1, new Stix()));
        setCursor(cursor);
        getCursor().setCurrentMove(KeyCode.RIGHT);
        gameController.getStix().addToStix(new Point(getCursor().getX(), getCursor().getY()));
        gameController.keyReleased(new KeyEvent(null, null, KeyEvent.KEY_RELEASED, " ", "", KeyCode.Y, false, false,
                false, false));
        verify(cursor, times(0)).setDrawing(false);
    }
    /*@Test
    public void gameWon() throws Exception {
        getScoreCounter().setTargetPercentage(1);
        getScoreCounter().setTotalPercentage(2);
        gameController = spy(new GameController());
        verify(gameController,times(1)).gameWon();
    }*/

}