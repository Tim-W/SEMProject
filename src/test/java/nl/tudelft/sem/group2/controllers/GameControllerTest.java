package nl.tudelft.sem.group2.controllers;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import nl.tudelft.sem.group2.JavaFXThreadingRule;
import nl.tudelft.sem.group2.scenes.GameScene;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Fuse;
import nl.tudelft.sem.group2.units.Stix;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

import java.awt.Point;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class GameControllerTest {
    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
    private GameController gameController;
    private Cursor spyCursor;
    public void setUp() {
        GameController.deleteGameController();
        gameController = GameController.getInstance();
        gameController.getAnimationTimer().stop();
        gameController.initializeSinglePlayer();
        spyCursor = spy(gameController.getCursors().get(0));
        gameController.getCursors().set(0, spyCursor);
        gameController.getUnits().clear();
        gameController.getUnits().add(spyCursor);

    }

    @Test
    public void keyPressedSpace() throws Exception {
        setUp();
        boolean isRunning = gameController.getLevelHandler().getLevel().isRunning();
        gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.SPACE, false, false,
                false, false));
        Assert.assertEquals(!isRunning, gameController.getLevelHandler().getLevel().isRunning());
    }

    @Test
    public void keyPressedArrow() throws Exception {
        setUp();
        spyCursor.setDrawing(true);
        spyCursor.setFuse(spy(new Fuse(1, 1, 1, 1, gameController.getAreaTracker(), spyCursor.getStix())));
        spyCursor.setDrawing(true);
        gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.RIGHT, false, false,
                false, false));
        verify(spyCursor.getFuse(), times(1)).setMoving(false);
    }

    @Test
    public void keyPressedI() throws Exception {
        setUp();
        gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.I, false,
                false,
                false, false));
        verify(spyCursor, times(1)).setSpeed(1);
    }

    @Test
    public void keyPressedINotFast() throws Exception {
        setUp();
        spyCursor.getStix().addToStix(new Point(1, 1));
        spyCursor.setFast(false);
        gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.I, false,
                false,
                false, false));
        verify(spyCursor, times(1)).setSpeed(1);
    }

    @Test
    public void keyPressedINotFastNothing() throws Exception {
        setUp();
        spyCursor.getStix().addToStix(new Point(1, 1));
        gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.I, false,
                false,
                false, false));
        verify(spyCursor, times(0)).setSpeed(1);
    }

    @Test
    public void keyPressedO() throws Exception {
        setUp();
        spyCursor.getStix().addToStix(new Point(1, 1));
        gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.O, false,
                false,
                false, false));
        verify(spyCursor, times(2)).setSpeed(2);
    }

    @Test
    public void keyPressedY() throws Exception {
        setUp();
        gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.Y, false, false,
                false, false));
        verify(spyCursor, times(1)).setSpeed(2);
    }

    @Test
    public void keyReleasedCurrentMove() throws Exception {
        setUp();
        spyCursor.setCurrentMove(KeyCode.RIGHT);
        spyCursor.getStix().addToStix(new Point(spyCursor.getX(), spyCursor.getY()));
        spyCursor.setFuse(spy(new Fuse(1, 1, 1, 1, gameController.getAreaTracker(), spyCursor.getStix())));
        gameController.keyReleased(new KeyEvent(null, null, KeyEvent.KEY_RELEASED, " ", "", gameController.getCursors().get(0).getCurrentMove(), false, false,
                false, false));
        verify(spyCursor.getFuse(), times(1)).setMoving(true);
    }

    @Test
    public void testHandle() throws Exception {
        setUp();
        GameScene mock = mock(GameScene.class);
        gameController.setGameScene(mock);
        int previoustime = 1;
        gameController.setPreviousTime(previoustime);
        gameController.getAnimationTimer().handle(previoustime + 200000000);
        verify(mock, times(1)).draw(any(), any());
    }

    @Test
    public void keyReleasedCurrentMoveCreateFuse() throws Exception {
        setUp();
        spyCursor.setStix(spy(spyCursor.getStix()));
        spyCursor.setCurrentMove(KeyCode.RIGHT);
        spyCursor.getStix().addToStix(new Point(spyCursor.getX(), spyCursor.getY()));
        gameController.keyReleased(new KeyEvent(null, null, KeyEvent.KEY_RELEASED, " ", "", gameController.getCursors().get(0).getCurrentMove(), false, false,
                false, false));
        verify(spyCursor.getStix(), times(3)).getStixCoordinates();
    }

    @Test
    public void keyReleasedCurrentMoveNoCoordinates() throws Exception {
        setUp();
        spyCursor.setFuse(spy(new Fuse(1, 1, 1, 1, gameController.getAreaTracker(), spyCursor.getStix())));
        spyCursor.setCurrentMove(KeyCode.RIGHT);
        spyCursor.getStix().addToStix(new Point(spyCursor.getX() + 1, spyCursor.getY()));
        gameController.keyReleased(new KeyEvent(null, null, KeyEvent.KEY_RELEASED, " ", "", gameController.getCursors().get(0).getCurrentMove(), false, false,
                false, false));
        verify(spyCursor.getFuse(), times(0)).setMoving(true);
    }

    /**
     * test addUnit not to add two fuses
     *
     * @throws Exception
     */
    @Test
    public void testAddUnit() throws Exception {
        setUp();
        int oldLength = gameController.getUnits().size();
        Fuse fuse = new Fuse(1, 1, 1, 1, gameController.getAreaTracker(), new Stix());
        gameController.getUnits().add(fuse);
        Assert.assertTrue(gameController.getUnits().contains(fuse));
        gameController.getUnits().add(fuse);
        Assert.assertEquals(oldLength + 1, gameController.getUnits().size());
    }
}