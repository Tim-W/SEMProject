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
import org.junit.Test;

import java.awt.EventQueue;
import java.awt.Point;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class GameControllerTest {
    private GameController gameController;

    public void setUp() {
        Group root = new Group();
        Scene s = new Scene(root, 300, 300, Color.BLACK);
        GameController.setGameController(null);
        gameController = GameController.getInstance();
    }

    @Test
    public void keyPressedSpace() throws Exception {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setUp();
                boolean isRunning = gameController.isRunning();
                gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.SPACE, false, false,
                        false, false));
                Assert.assertEquals(!isRunning, gameController.isRunning());
            }
        });
    }

    @Test
    public void keyPressedArrow() throws Exception {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setUp();
                GameController.getInstance().getScene().removeFuse();
                Fuse fuse = spy(new Fuse(1, 1, 1, 1, new Stix(), GameController.getInstance().getAreaTracker()));
                GameController.getInstance().getUnits().add(fuse);
                Cursor cursor = new Cursor(1, 1, 1, 1, new Stix(), GameController.getInstance().getAreaTracker(), 1);
                GameController.getInstance().setCursor(cursor);
                cursor.setDrawing(true);
                gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.RIGHT, false, false,
                        false, false));
                verify(fuse, times(1)).setMoving(false);
                //verify(fuse,times(2)).getUnits();
            }
        });
    }

    @Test
    public void keyPressedX() throws Exception {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setUp();
                Cursor cursor = spy(new Cursor(1, 1, 1, 1, new Stix(), gameController.getAreaTracker(), 1));
                gameController.setCursor(cursor);
                gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.X, false, false,
                        false, false));
                verify(cursor, times(1)).setSpeed(1);
            }
        });
    }

    @Test
    public void keyPressedXNotFast() throws Exception {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setUp();
                Stix stix = new Stix();
                stix.addToStix(new Point(1, 1));
                gameController.setStix(stix);
                Cursor cursor = spy(new Cursor(1, 1, 1, 1, stix, gameController.getAreaTracker(), 1));
                gameController.setCursor(cursor);
                cursor.setFast(false);
                gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.X, false, false,
                        false, false));
                //verify(cursor, times(1)).setSpeed(1);
                verify(cursor, times(1)).setSpeed(1);
            }
        });
    }

    @Test
    public void keyPressedXNotFastNothing() throws Exception {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setUp();
                Stix stix = new Stix();
                stix.addToStix(new Point(1, 1));
                gameController.setStix(stix);
                Cursor cursor = spy(new Cursor(1, 1, 1, 1, stix, GameController.getInstance().getAreaTracker(), 1));
                gameController.setCursor(cursor);
                cursor.setFast(true);
                gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.X, false, false,
                        false, false));
                verify(cursor, times(0)).setSpeed(1);
            }
        });
    }

    @Test
    public void keyPressedZ() throws Exception {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setUp();
                Cursor cursor = spy(new Cursor(1, 1, 1, 1, new Stix(), GameController.getInstance().getAreaTracker(), 1));
                GameController.getInstance().setCursor(cursor);
                gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.Z, false, false,
                        false, false));
                verify(cursor, times(1)).setSpeed(2);
            }
        });
    }

    @Test
    public void keyPressedY() throws Exception {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setUp();
                Cursor cursor = spy(new Cursor(1, 1, 1, 1, new Stix(), GameController.getInstance().getAreaTracker(), 1));
                GameController.getInstance().setCursor(cursor);
                gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.Y, false, false,
                        false, false));
                verify(cursor, times(0)).setSpeed(2);
            }
        });
    }

    @Test
    public void keyReleasedCurrentMove() throws Exception {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setUp();
                GameController.getInstance().getScene().removeFuse();
                Fuse fuse = spy(new Fuse(1, 1, 1, 1, new Stix(), GameController.getInstance().getAreaTracker()));
                GameController.getInstance().getUnits().add(fuse);
                GameController.getInstance().getCursor().setCurrentMove(KeyCode.RIGHT);
                gameController.getStix().addToStix(new Point(GameController.getInstance().getCursor().getX(), GameController.getInstance().getCursor().getY()));
                gameController.keyReleased(new KeyEvent(null, null, KeyEvent.KEY_RELEASED, " ", "", GameController.getInstance().getCursor().getCurrentMove(), false, false,
                        false, false));
                verify(fuse, times(1)).setMoving(true);
            }
        });
    }

    @Test
    public void testHandle() throws Exception {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setUp();
                GameScene mock = mock(GameScene.class);
                gameController.setGameScene(mock);
                int previoustime = 1;
                gameController.setPreviousTime(previoustime);
                gameController.getAnimationTimer().handle(previoustime + 200000000);
                verify(mock, times(1)).draw();
            }
        });
    }

    @Test
    public void keyReleasedCurrentMoveCreateFuse() throws Exception {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setUp();
                GameController.getInstance().getScene().removeFuse();
                gameController.setStix(spy(gameController.getStix()));
                GameController.getInstance().getCursor().setCurrentMove(KeyCode.RIGHT);
                gameController.getStix().addToStix(new Point(GameController.getInstance().getCursor().getX(), GameController.getInstance().getCursor().getY()));
                gameController.keyReleased(new KeyEvent(null, null, KeyEvent.KEY_RELEASED, " ", "", GameController.getInstance().getCursor().getCurrentMove(), false, false,
                        false, false));
                verify(gameController.getStix(), times(3)).getStixCoordinates();
            }
        });
    }

    @Test
    public void keyReleasedCurrentMoveNoCoordinates() throws Exception {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setUp();
                GameController.getInstance().getScene().removeFuse();
                Fuse fuse = spy(new Fuse(1, 1, 1, 1, new Stix(), GameController.getInstance().getAreaTracker()));
                GameController.getInstance().getUnits().add(fuse);
                GameController.getInstance().getCursor().setCurrentMove(KeyCode.RIGHT);
                gameController.getStix().addToStix(new Point(GameController.getInstance().getCursor().getX() + 1, GameController.getInstance().getCursor().getY()));
                gameController.keyReleased(new KeyEvent(null, null, KeyEvent.KEY_RELEASED, " ", "", GameController.getInstance().getCursor().getCurrentMove(), false, false,
                        false, false));
                verify(fuse, times(0)).setMoving(true);
            }
        });
    }

    @Test
    public void keyReleasedX() throws Exception {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setUp();
                GameController.getInstance().getScene().removeFuse();
                Fuse fuse = spy(new Fuse(1, 1, 1, 1, new Stix(), GameController.getInstance().getAreaTracker()));
                GameController.getInstance().getUnits().add(fuse);
                Cursor cursor = spy(new Cursor(1, 1, 1, 1, new Stix(), GameController.getInstance().getAreaTracker(), 1));
                GameController.getInstance().setCursor(cursor);
                GameController.getInstance().getCursor().setCurrentMove(KeyCode.RIGHT);
                gameController.getStix().addToStix(new Point(GameController.getInstance().getCursor().getX(), GameController.getInstance().getCursor().getY()));
                gameController.keyReleased(new KeyEvent(null, null, KeyEvent.KEY_RELEASED, " ", "", KeyCode.X, false, false,
                        false, false));
                verify(cursor, times(1)).setDrawing(false);
            }
        });
    }

    @Test
    public void keyReleasedZ() throws Exception {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setUp();
                GameController.getInstance().getScene().removeFuse();
                Fuse fuse = spy(new Fuse(1, 1, 1, 1, new Stix(), GameController.getInstance().getAreaTracker()));
                GameController.getInstance().getUnits().add(fuse);
                Cursor cursor = spy(new Cursor(1, 1, 1, 1, new Stix(), GameController.getInstance().getAreaTracker(), 1));
                GameController.getInstance().setCursor(cursor);
                GameController.getInstance().getCursor().setCurrentMove(KeyCode.RIGHT);
                gameController.getStix().addToStix(new Point(GameController.getInstance().getCursor().getX(), GameController.getInstance().getCursor().getY()));
                gameController.keyReleased(new KeyEvent(null, null, KeyEvent.KEY_RELEASED, " ", "", KeyCode.Z, false, false,
                        false, false));
                verify(cursor, times(1)).setDrawing(false);
            }
        });
    }

    @Test
    public void keyReleasedY() throws Exception {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setUp();
                Cursor cursor = spy(new Cursor(1, 1, 1, 1, new Stix(), GameController.getInstance().getAreaTracker(), 1));
                GameController.getInstance().setCursor(cursor);
                GameController.getInstance().getCursor().setCurrentMove(KeyCode.RIGHT);
                gameController.getStix().addToStix(new Point(GameController.getInstance().getCursor().getX(), GameController.getInstance().getCursor().getY()));
                gameController.keyReleased(new KeyEvent(null, null, KeyEvent.KEY_RELEASED, " ", "", KeyCode.Y, false, false,
                        false, false));
                verify(cursor, times(0)).setDrawing(false);
            }
        });
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
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                setUp();
                int oldLength = gameController.getUnits().size();
                Fuse fuse = new Fuse(1, 1, 1, 1, new Stix(), gameController.getAreaTracker());
                gameController.getScene().removeFuse();
                gameController.getUnits().add(fuse);
                Assert.assertTrue(gameController.getUnits().contains(fuse));
                gameController.getUnits().add(fuse);
                Assert.assertEquals(oldLength + 1, gameController.getUnits().size());
            }
        });
    }
}