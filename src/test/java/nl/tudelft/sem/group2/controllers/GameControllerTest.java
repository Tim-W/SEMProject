package nl.tudelft.sem.group2.controllers;

import javafx.embed.swing.JFXPanel;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import nl.tudelft.sem.group2.scenes.GameScene;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Fuse;
import nl.tudelft.sem.group2.units.Stix;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.Point;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class GameControllerTest {
    private GameController gameController;
    private Cursor spyCursor;

    @BeforeClass
    public static void BeforeClass() {
        new JFXPanel();
    }

    public void setUp() {
        GameController.deleteGameController();
        gameController = GameController.getInstance();
        gameController.getAnimationTimer().stop();
        gameController.makeCursor();
        spyCursor = spy(gameController.getCursors().get(0));
        gameController.getCursors().set(0, spyCursor);
        gameController.getUnits().clear();
        gameController.getUnits().add(spyCursor);

    }

    @Test
    public void keyPressedSpace() throws Exception {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setUp();
                boolean isRunning = gameController.isRunning();
                gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.SPACE, false, false,
                        false, false));
                Assert.assertEquals(!isRunning, gameController.isRunning());
            }
        };
        runnable.run();
    }

    @Test
    public void keyPressedArrow() throws Exception {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setUp();
                spyCursor.setDrawing(true);
                spyCursor.setFuse(spy(new Fuse(1, 1, 1, 1, gameController.getAreaTracker(), spyCursor.getStix())));
                spyCursor.setDrawing(true);
                gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.RIGHT, false, false,
                        false, false));
                verify(spyCursor.getFuse(), times(1)).setMoving(false);
            }
        };
        runnable.run();
    }

    @Test
    public void keyPressedI() throws Exception {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setUp();
                gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.I, false,
                        false,
                        false, false));
                verify(spyCursor, times(1)).setSpeed(1);
            }
        };
        runnable.run();
    }

    @Test
    public void keyPressedINotFast() throws Exception {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setUp();
                spyCursor.getStix().addToStix(new Point(1, 1));
                spyCursor.setFast(false);
                gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.I, false,
                        false,
                        false, false));
                verify(spyCursor, times(1)).setSpeed(1);
            }
        };
        runnable.run();
    }

    @Test
    public void keyPressedINotFastNothing() throws Exception {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setUp();
                spyCursor.getStix().addToStix(new Point(1, 1));
                gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.I, false,
                        false,
                        false, false));
                verify(spyCursor, times(0)).setSpeed(1);
            }
        };
        runnable.run();

    }

    @Test
    public void keyPressedO() throws Exception {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setUp();
                spyCursor.getStix().addToStix(new Point(1, 1));
                gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.O, false,
                        false,
                        false, false));
                verify(spyCursor, times(1)).setSpeed(2);
            }
        };
        runnable.run();
    }

    @Test
    public void keyPressedY() throws Exception {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setUp();
                gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.Y, false, false,
                        false, false));
                verify(spyCursor, times(0)).setSpeed(2);
            }
        };
        runnable.run();
    }

    @Test
    public void keyReleasedCurrentMove() throws Exception {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setUp();
                spyCursor.setCurrentMove(KeyCode.RIGHT);
                spyCursor.getStix().addToStix(new Point(spyCursor.getX(), spyCursor.getY()));
                spyCursor.setFuse(spy(new Fuse(1, 1, 1, 1, gameController.getAreaTracker(), spyCursor.getStix())));
                gameController.keyReleased(new KeyEvent(null, null, KeyEvent.KEY_RELEASED, " ", "", gameController.getCursors().get(0).getCurrentMove(), false, false,
                        false, false));
                verify(spyCursor.getFuse(), times(1)).setMoving(true);
            }
        };
        runnable.run();
    }

    @Test
    public void testHandle() throws Exception {
        Runnable runnable = new Runnable() {
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
        };
        runnable.run();
    }

    @Test
    public void keyReleasedCurrentMoveCreateFuse() throws Exception {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setUp();
                spyCursor.setStix(spy(spyCursor.getStix()));
                spyCursor.setCurrentMove(KeyCode.RIGHT);
                spyCursor.getStix().addToStix(new Point(spyCursor.getX(), spyCursor.getY()));
                gameController.keyReleased(new KeyEvent(null, null, KeyEvent.KEY_RELEASED, " ", "", gameController.getCursors().get(0).getCurrentMove(), false, false,
                        false, false));
                verify(spyCursor.getStix(), times(3)).getStixCoordinates();
            }
        };
        runnable.run();
    }

    @Test
    public void keyReleasedCurrentMoveNoCoordinates() throws Exception {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setUp();
                spyCursor.setFuse(spy(new Fuse(1, 1, 1, 1, gameController.getAreaTracker(), spyCursor.getStix())));
                spyCursor.setCurrentMove(KeyCode.RIGHT);
                spyCursor.getStix().addToStix(new Point(spyCursor.getX() + 1, spyCursor.getY()));
                gameController.keyReleased(new KeyEvent(null, null, KeyEvent.KEY_RELEASED, " ", "", gameController.getCursors().get(0).getCurrentMove(), false, false,
                        false, false));
                verify(spyCursor.getFuse(), times(0)).setMoving(true);
            }
        };
        runnable.run();
    }

/*    @Test
    public void keyReleasedX() throws Exception {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setUp();
                spyCursor.setCurrentMove(KeyCode.RIGHT);
                spyCursor.getStix().addToStix(new Point(spyCursor.getX(), spyCursor.getY()));
                gameController.keyReleased(new KeyEvent(null, null, KeyEvent.KEY_RELEASED, " ", "", KeyCode.X, false, false,
                        false, false));
                verify(spyCursor, times(1)).setDrawing(false);
            }
        };
        runnable.run();
    }

    @Test
    public void keyReleasedZ() throws Exception {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                setUp();
                Cursor cursor = spy(new Cursor(new Point(1, 1), 1, 1, GameController.getInstance().getAreaTracker(), new Stix(), Color.RED, 3));
                GameController.getInstance().addCursor(cursor);
                GameController.getInstance().getCursors().get(0).setCurrentMove(KeyCode.RIGHT);
                gameController.getCursors().get(0).getStix().addToStix(new Point(gameController.getCursors().get(0).getX(), gameController.getCursors().get(0).getY()));
                gameController.keyReleased(new KeyEvent(null, null, KeyEvent.KEY_RELEASED, " ", "", KeyCode.Z, false, false,
                        false, false));
                verify(cursor, times(1)).setDrawing(false);
            }
        };
        runnable.run();
    }

    @Test
    public void keyReleasedY() throws Exception {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                setUp();
                Cursor cursor = spy(new Cursor(new Point(1, 1), 1, 1, GameController.getInstance().getAreaTracker(), new Stix(), Color.RED, 3));
                GameController.getInstance().addCursor(cursor);
                GameController.getInstance().getCursors().get(0).setCurrentMove(KeyCode.RIGHT);
                gameController.getCursors().get(0).getStix().addToStix(new Point(gameController.getCursors().get(0).getX(), gameController.getCursors().get(0).getY()));
                gameController.keyReleased(new KeyEvent(null, null, KeyEvent.KEY_RELEASED, " ", "", KeyCode.Y, false, false,
                        false, false));
                verify(cursor, times(0)).setDrawing(false);
            }
        };
        runnable.run();
    }*/

    /**
     * test addUnit not to add two fuses
     *
     * @throws Exception
     */
    @Test
    public void testAddUnit() throws Exception {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setUp();
                int oldLength = gameController.getUnits().size();
                Fuse fuse = new Fuse(1, 1, 1, 1, gameController.getAreaTracker(), new Stix());
                gameController.getUnits().add(fuse);
                Assert.assertTrue(gameController.getUnits().contains(fuse));
                gameController.getUnits().add(fuse);
                Assert.assertEquals(oldLength + 1, gameController.getUnits().size());
            }
        };
        runnable.run();
    }
}