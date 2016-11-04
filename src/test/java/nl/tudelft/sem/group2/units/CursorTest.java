package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import nl.tudelft.sem.group2.JavaFXThreadingRule;
import nl.tudelft.sem.group2.ScoreCounter;
import nl.tudelft.sem.group2.board.AreaState;
import nl.tudelft.sem.group2.board.BoardGrid;
import nl.tudelft.sem.group2.controllers.GameController;
import nl.tudelft.sem.group2.global.Globals;
import nl.tudelft.sem.group2.level.Level;
import nl.tudelft.sem.group2.level.LevelHandler;
import nl.tudelft.sem.group2.powerups.PowerUpType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.awt.Point;

import static edu.emory.mathcs.backport.java.util.Arrays.asList;
import static nl.tudelft.sem.group2.global.Globals.GRID_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.GRID_WIDTH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for cursors.
 */
public class CursorTest {

    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();

    private Cursor cursor;
    private Stix stix = mock(Stix.class);
    private BoardGrid grid = mock(BoardGrid.class);
    private KeyEvent keyEventMock;


    @Before
    public void setUp() throws Exception {
        grid = mock(BoardGrid.class);
        LevelHandler levelHandler = mock(LevelHandler.class);
        GameController.getInstance().setLevelHandler(levelHandler);
        when(levelHandler.getLevel()).thenReturn(mock(Level.class));
        when(levelHandler.getLevel().getBoardGrid()).thenReturn(grid);
        keyEventMock = mock(KeyEvent.class);
        when(keyEventMock.getCode()).thenReturn(KeyCode.A);
    }

    public void createCursor() {
        cursor = new Cursor(new Point(0, 0), 2,
                2, stix, Globals.LIVES, 0);
        cursor.setSpeed(1);
    }


    @Test
    public void isFast() throws Exception {
        createCursor();
        boolean oldValue = cursor.isFast();
        if (oldValue) {
            cursor.setSpeed(1);
        } else {
            cursor.setSpeed(2);
        }
        Assert.assertEquals(!oldValue, cursor.isFast());
    }


    @Test
    public void draw() throws Exception {
        Cursor spy = spy(new Cursor(new Point(1, 1), 1, 1, stix, 3, 1));
        spy.draw(new Canvas(1, 1).getGraphicsContext2D());
        verify(spy).getSpriteIndex();
    }


    @Test
    public void speedPowerupTestFromSlow() {
        cursor = new Cursor(new Point(0, 0), 0, 0, stix, 1, 1);
        cursor.getPowerupHandler().setCurrentPowerup(PowerUpType.SPEED);
        cursor.setSpeed(1);

        KeyEvent keyEvent = mock(KeyEvent.class);
        when(keyEvent.getCode()).thenReturn(KeyCode.A);
        cursor.keyPressed(keyEvent);
        Assert.assertEquals(2, cursor.getSpeed());
    }

    @Test
    public void speedPowerupTestFromFast() {
        cursor = new Cursor(new Point(0, 0), 0, 0, stix, 1, 1);
        cursor.getPowerupHandler().setCurrentPowerup(PowerUpType.SPEED);
        cursor.setSpeed(2);

        cursor.keyPressed(keyEventMock);
        Assert.assertEquals(3, cursor.getSpeed());
    }

    @Test
    public void stopFuseDrawingTest() {
        cursor = new Cursor(new Point(0, 0), 0, 0, stix, 1, 1);
        cursor.getArrowKeys().add(keyEventMock.getCode());
        cursor.setDrawing(true);

        FuseHandler fuseHandler = new FuseHandler(cursor);
        Fuse fuse = new Fuse(0, 0, 1, 1, stix);
        fuse.notMoving();
        fuseHandler.setFuse(fuse);
        cursor.setFuseHandler(fuseHandler);

        cursor.keyPressed(keyEventMock);
        Assert.assertFalse(fuse.isMoving());
    }

    @Test
    public void keyPressedContainsNotDrawing() {
        cursor = new Cursor(new Point(0, 0), 0, 0, stix, 1, 1);
        cursor.getArrowKeys().add(keyEventMock.getCode());
        cursor.setDrawing(false);

        FuseHandler fuseHandler = new FuseHandler(cursor);
        Fuse fuse = new Fuse(0, 0, 1, 1, stix);
        fuse.notMoving();
        fuseHandler.setFuse(fuse);
        cursor.setFuseHandler(fuseHandler);

        cursor.keyPressed(keyEventMock);
        Assert.assertTrue(!fuse.isMoving());
    }

    @Test
    public void keyPressedContainsNoFuse() {
        cursor = new Cursor(new Point(0, 0), 0, 0, stix, 1, 1);
        cursor.getArrowKeys().add(keyEventMock.getCode());
        cursor.setDrawing(false);

        FuseHandler fuseHandler = mock(FuseHandler.class);
        cursor.setFuseHandler(fuseHandler);

        cursor.keyPressed(keyEventMock);
        verify(fuseHandler, times(0)).getFuse();
    }

    @Test
    public void keyPressedDrawingContainsNoFuse() {
        cursor = new Cursor(new Point(0, 0), 0, 0, stix, 1, 1);
        cursor.getArrowKeys().add(keyEventMock.getCode());
        cursor.setDrawing(true);

        FuseHandler fuseHandler = mock(FuseHandler.class);
        when(fuseHandler.hasFuse()).thenReturn(false);
        cursor.setFuseHandler(fuseHandler);

        cursor.keyPressed(keyEventMock);
        verify(fuseHandler, times(0)).getFuse();
    }

    @Test
    public void keyReleasedCurrentMove() {
        cursor = new Cursor(new Point(1, 1), 1, 1, stix, 1, 1);
        cursor.setCurrentMove(keyEventMock.getCode());
        FuseHandler fuseHandler = mock(FuseHandler.class);
        cursor.setFuseHandler(fuseHandler);

        cursor.keyReleased(KeyCode.A);

        verify(fuseHandler, times(1)).handleFuse();
        Assert.assertNull(cursor.getCurrentMove());
    }

    @Test
    public void keyReleasedFastMoveKey() {
        cursor = new Cursor(new Point(1, 1), 1, 1, stix, 1, 1);
        cursor.setCurrentMove(keyEventMock.getCode());
        cursor.setDrawing(true);
        cursor.setSpeed(3);
        FuseHandler fuseHandler = mock(FuseHandler.class);
        cursor.setFuseHandler(fuseHandler);
        cursor.setFastMoveKey(KeyCode.K);

        cursor.keyReleased(KeyCode.K);

        verify(fuseHandler, times(1)).handleFuse();
        Assert.assertFalse(cursor.isDrawing());
        Assert.assertEquals(2, cursor.getSpeed());
    }

    @Test
    public void keyReleasedSlowMoveKey() {
        cursor = new Cursor(new Point(1, 1), 1, 1, stix, 1, 1);
        cursor.setCurrentMove(keyEventMock.getCode());
        cursor.setDrawing(true);
        cursor.setSpeed(3);
        FuseHandler fuseHandler = mock(FuseHandler.class);
        cursor.setFuseHandler(fuseHandler);
        cursor.setSlowMoveKey(KeyCode.K);

        cursor.keyReleased(KeyCode.K);

        verify(fuseHandler, times(1)).handleFuse();
        Assert.assertFalse(cursor.isDrawing());
        Assert.assertEquals(2, cursor.getSpeed());
    }

    @Test
    public void keyReleasedBothMoveKeys() {
        cursor = new Cursor(new Point(1, 1), 1, 1, stix, 1, 1);
        cursor.setCurrentMove(keyEventMock.getCode());
        cursor.setDrawing(true);
        cursor.setSpeed(3);
        FuseHandler fuseHandler = mock(FuseHandler.class);
        cursor.setFuseHandler(fuseHandler);
        cursor.setSlowMoveKey(KeyCode.K);
        cursor.setFastMoveKey(KeyCode.K);
        cursor.keyReleased(KeyCode.K);

        verify(fuseHandler, times(1)).handleFuse();
        Assert.assertFalse(cursor.isDrawing());
        Assert.assertEquals(2, cursor.getSpeed());
    }

    @Test
    public void keyReleasedNoKey() {
        cursor = new Cursor(new Point(1, 1), 1, 1, stix, 1, 1);
        cursor.setCurrentMove(keyEventMock.getCode());
        cursor.setDrawing(true);
        cursor.setSpeed(3);

        FuseHandler fuseHandler = mock(FuseHandler.class);
        cursor.setFuseHandler(fuseHandler);
        cursor.setSlowMoveKey(KeyCode.K);

        cursor.keyReleased(KeyCode.D);

        verify(fuseHandler, times(0)).handleFuse();
        Assert.assertTrue(cursor.isDrawing());
        Assert.assertEquals(3, cursor.getSpeed());
    }


    @Test
    public void intersectionFuseNoUnit() {
        cursor = new Cursor(new Point(0, 0), 1, 1, stix, 1, 1);

        FuseHandler fuseHandler = mock(FuseHandler.class);
        when(fuseHandler.hasFuse()).thenReturn(true);

        Fuse fuse = mock(Fuse.class);
        when(fuse.intersect(cursor)).thenReturn(true);
        when(fuseHandler.getFuse()).thenReturn(fuse);
        cursor.setFuseHandler(fuseHandler);

        Sparx sparx = new Sparx(10, 10, 2, 2, SparxDirection.LEFT);

        Assert.assertTrue(cursor.intersect(sparx));
    }

    @Test
    public void intersectionFuseAndUnit() {
        cursor = new Cursor(new Point(0, 0), 1, 1, stix, 1, 1);

        FuseHandler fuseHandler = mock(FuseHandler.class);
        when(fuseHandler.hasFuse()).thenReturn(true);

        Fuse fuse = mock(Fuse.class);
        when(fuse.intersect(cursor)).thenReturn(true);
        when(fuseHandler.getFuse()).thenReturn(fuse);
        cursor.setFuseHandler(fuseHandler);

        Sparx sparx = new Sparx(0, 0, 2, 2, SparxDirection.LEFT);

        Assert.assertTrue(cursor.intersect(sparx));
    }

    @Test
    public void intersectionUnitNoFuse() {
        cursor = new Cursor(new Point(0, 0), 2, 2, stix, 1, 1);

        FuseHandler fuseHandler = mock(FuseHandler.class);
        when(fuseHandler.hasFuse()).thenReturn(true);

        Fuse fuse = mock(Fuse.class);
        when(fuse.intersect(cursor)).thenReturn(false);
        when(fuseHandler.getFuse()).thenReturn(fuse);
        cursor.setFuseHandler(fuseHandler);

        Sparx sparx = new Sparx(0, 0, 2, 2, SparxDirection.LEFT);


        Assert.assertTrue(cursor.intersect(sparx));
    }

    @Test
    public void intersectionUnitNullFuse() {
        cursor = new Cursor(new Point(0, 0), 2, 2, stix, 1, 1);

        FuseHandler fuseHandler = mock(FuseHandler.class);
        when(fuseHandler.hasFuse()).thenReturn(false);

        Sparx sparx = new Sparx(0, 0, 2, 2, SparxDirection.LEFT);

        Assert.assertTrue(cursor.intersect(sparx));
    }

    @Test
    public void noIntersectionFuse() {
        cursor = mock(Cursor.class);
        when(cursor.intersect(any(Unit.class))).thenReturn(false);

        FuseHandler fuseHandler = new FuseHandler(cursor);
        Fuse fuse = mock(Fuse.class);
        fuseHandler.setFuse(fuse);
        cursor.setFuseHandler(fuseHandler);
        when(fuse.intersect(cursor)).thenReturn(false);

        Assert.assertFalse(cursor.intersect(fuse));
    }

    @Test
    public void addArrowKeys() {
        cursor = new Cursor(new Point(1, 1), 1, 1, stix, 1, 1);
        cursor.getArrowKeys().clear();
        cursor.addKey(KeyCode.A);

        Assert.assertTrue(cursor.getArrowKeys().contains(KeyCode.A));
    }

    @Test
    public void addLife() {
        cursor = new Cursor(new Point(1, 1), 1, 1, stix, 1, 1);

        ScoreCounter scoreCounter = mock(ScoreCounter.class);
        cursor.setScoreCounter(scoreCounter);

        Assert.assertEquals(1, cursor.getLives());
        cursor.addLife();

        Assert.assertEquals(2, cursor.getLives());
    }

    @Test
    public void subtractLife() {
        cursor = new Cursor(new Point(1, 1), 1, 1, stix, 1, 1);

        ScoreCounter scoreCounter = mock(ScoreCounter.class);
        cursor.setScoreCounter(scoreCounter);

        Assert.assertEquals(1, cursor.getLives());
        cursor.subtractLife();

        Assert.assertEquals(0, cursor.getLives());
    }
}