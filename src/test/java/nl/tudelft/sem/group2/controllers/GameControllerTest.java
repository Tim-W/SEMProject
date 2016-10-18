package nl.tudelft.sem.group2.controllers;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.scenes.GameScene;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Fuse;
import nl.tudelft.sem.group2.units.Stix;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.Point;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by Gijs on 2-10-2016.
 */
@Ignore
public class GameControllerTest {
    GameController gameController;

    @Before
    public void setUp() throws Exception {
        Group root = new Group();
        Scene s = new Scene(root, 300, 300, Color.BLACK);
        GameController.deleteGameController();
        gameController = GameController.getInstance();
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
        Cursor cursor = new Cursor(1, 1, 1, 1, GameController.getInstance().getAreaTracker(), new Stix(), Color.RED, 3);
        GameController.getInstance().addCursor(cursor);
        cursor.setDrawing(true);
        gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.RIGHT, false, false,
                false, false));
        verify(GameController.getInstance().getCursors().get(0).getFuse(), times(1)).setMoving(false);
        //verify(fuse,times(2)).getUnits();
    }

    @Test
    public void keyPressedX() throws Exception {

        Cursor cursor = spy(new Cursor(1, 1, 1, 1, GameController.getInstance().getAreaTracker(), new Stix(), Color.RED, 3));
        gameController.addCursor(cursor);
        gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.O, false, false,
                false, false));
        verify(cursor, times(1)).setSpeed(1);
    }

    @Test
    public void keyPressedXNotFast() throws Exception {
        Stix stix = new Stix();
        stix.addToStix(new Point(1, 1));
        Cursor cursor = spy(new Cursor(1, 1, 1, 1, gameController.getAreaTracker(), stix, Color.RED, 3));
        gameController.addCursor(cursor);
        cursor.setFast(false);
        gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.O, false, false,
                false, false));
        //verify(cursor, times(1)).setSpeed(1);
        verify(cursor, times(1)).setSpeed(1);
    }

    @Test
    public void keyPressedXNotFastNothing() throws Exception {
        Stix stix = new Stix();
        stix.addToStix(new Point(1, 1));
        Cursor cursor = spy(new Cursor(1, 1, 1, 1, GameController.getInstance().getAreaTracker(), stix, Color.RED, 3));
        gameController.addCursor(cursor);
        cursor.setFast(true);
        gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.O, false, false,
                false, false));
        verify(cursor, times(0)).setSpeed(1);
    }

    @Test
    public void keyPressedI() throws Exception {

        Cursor cursor = spy(new Cursor(1, 1, 1, 1, GameController.getInstance().getAreaTracker(), new Stix(), Color.RED, 3));
        GameController.getInstance().addCursor(cursor);
        gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.I, false, false,
                false, false));
        verify(cursor, times(1)).setSpeed(2);
    }

    @Test
    public void keyPressedY() throws Exception {

        Cursor cursor = spy(new Cursor(1, 1, 1, 1, GameController.getInstance().getAreaTracker(), new Stix(), Color.RED, 3));
        GameController.getInstance().addCursor(cursor);
        gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.Y, false, false,
                false, false));
        verify(cursor, times(0)).setSpeed(2);
    }

    @Test
    public void keyReleasedCurrentMove() throws Exception {
        GameController.getInstance().getCursors().get(0).setCurrentMove(KeyCode.RIGHT);
        gameController.getCursors().get(0).getStix().addToStix(new Point(gameController.getCursors().get(0).getX(), gameController.getCursors().get(0).getY()));
        gameController.keyReleased(new KeyEvent(null, null, KeyEvent.KEY_RELEASED, " ", "", gameController.getCursors().get(0).getCurrentMove(), false, false,
                false, false));
        verify(gameController.getCursors().get(0).getFuse(), times(1)).setMoving(true);
    }

    @Test
    public void testHandle() throws Exception {
        GameScene mock = mock(GameScene.class);
        gameController.setGameScene(mock);
        int previoustime = 1;
        gameController.setPreviousTime(previoustime);
        gameController.getAnimationTimer().handle(previoustime + 200000000);
        verify(mock, times(1)).draw();
    }

    @Test
    public void keyReleasedCurrentMoveCreateFuse() throws Exception {
        spy(gameController.getCursors().get(0).getStix());
        GameController.getInstance().getCursors().get(0).setCurrentMove(KeyCode.RIGHT);
        gameController.getCursors().get(0).getStix().addToStix(new Point(gameController.getCursors().get(0).getX(), gameController.getCursors().get(0).getY()));
        gameController.keyReleased(new KeyEvent(null, null, KeyEvent.KEY_RELEASED, " ", "", gameController.getCursors().get(0).getCurrentMove(), false, false,
                false, false));
        verify(gameController.getCursors().get(0).getStix(), times(3)).getStixCoordinates();
    }

    @Test
    public void keyReleasedCurrentMoveNoCoordinates() throws Exception {
        Fuse fuse = spy(new Fuse(1, 1, 1, 1, GameController.getInstance().getAreaTracker(), new Stix()));
        GameController.getInstance().getUnits().add(fuse);
        gameController.getCursors().get(0).setCurrentMove(KeyCode.RIGHT);
        gameController.getCursors().get(0).getStix().addToStix(new Point(gameController.getCursors().get(0).getX() + 1, gameController.getCursors().get(0).getY()));
        gameController.keyReleased(new KeyEvent(null, null, KeyEvent.KEY_RELEASED, " ", "", gameController.getCursors().get(0).getCurrentMove(), false, false,
                false, false));
        verify(fuse, times(0)).setMoving(true);
    }

    @Test
    public void keyReleasedX() throws Exception {
        Cursor cursor = spy(new Cursor(1, 1, 1, 1, GameController.getInstance().getAreaTracker(), new Stix(), Color.RED, 3));
        GameController.getInstance().addCursor(cursor);
        GameController.getInstance().getCursors().get(0).setCurrentMove(KeyCode.RIGHT);
        gameController.getCursors().get(0).getStix().addToStix(new Point(gameController.getCursors().get(0).getX(), gameController.getCursors().get(0).getY()));
        gameController.keyReleased(new KeyEvent(null, null, KeyEvent.KEY_RELEASED, " ", "", KeyCode.X, false, false,
                false, false));
        verify(cursor, times(1)).setDrawing(false);
    }

    @Test
    public void keyReleasedZ() throws Exception {
        Cursor cursor = spy(new Cursor(1, 1, 1, 1, GameController.getInstance().getAreaTracker(), new Stix(), Color.RED, 3));
        GameController.getInstance().addCursor(cursor);
        GameController.getInstance().getCursors().get(0).setCurrentMove(KeyCode.RIGHT);
        gameController.getCursors().get(0).getStix().addToStix(new Point(gameController.getCursors().get(0).getX(), gameController.getCursors().get(0).getY()));
        gameController.keyReleased(new KeyEvent(null, null, KeyEvent.KEY_RELEASED, " ", "", KeyCode.Z, false, false,
                false, false));
        verify(cursor, times(1)).setDrawing(false);
    }

    @Test
    public void keyReleasedY() throws Exception {
        Cursor cursor = spy(new Cursor(1, 1, 1, 1, GameController.getInstance().getAreaTracker(), new Stix(), Color.RED, 3));
        GameController.getInstance().addCursor(cursor);
        GameController.getInstance().getCursors().get(0).setCurrentMove(KeyCode.RIGHT);
        gameController.getCursors().get(0).getStix().addToStix(new Point(gameController.getCursors().get(0).getX(), gameController.getCursors().get(0).getY()));
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

    /**
     * test addUnit not to add two fuses
     *
     * @throws Exception
     */
    @Test
    public void testAddUnit() throws Exception {
        int oldLength = gameController.getUnits().size();
        Fuse fuse = new Fuse(1, 1, 1, 1, gameController.getAreaTracker(), new Stix());
        gameController.getUnits().add(fuse);
        Assert.assertTrue(gameController.getUnits().contains(fuse));
        gameController.getUnits().add(fuse);
        Assert.assertEquals(oldLength + 1, gameController.getUnits().size());
    }
}