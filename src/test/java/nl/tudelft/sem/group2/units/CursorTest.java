package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import nl.tudelft.sem.group2.JavaFXThreadingRule;
import nl.tudelft.sem.group2.ScoreCounter;
import nl.tudelft.sem.group2.global.Globals;
import nl.tudelft.sem.group2.powerups.PowerUpType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.awt.Point;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
    private KeyEvent keyEventMock;
    private ScoreCounter scoreCounter;

    @Before
    public void setUp() throws Exception {
        keyEventMock = mock(KeyEvent.class);
        when(keyEventMock.getCode()).thenReturn(KeyCode.A);
    }

    private void createCursor() {
        cursor = new Cursor(new Point(0, 0), 2,
                2, stix, Globals.LIVES, 0);
        cursor.setSpeed(1);
        scoreCounter = new ScoreCounter(cursor.getId(), 20);
        cursor.setScoreCounter(scoreCounter);
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
        cursor.getCursorPowerupHandler().setCurrentPowerup(PowerUpType.SPEED);
        cursor.setSpeed(1);

        KeyEvent keyEvent = mock(KeyEvent.class);
        when(keyEvent.getCode()).thenReturn(KeyCode.A);
        cursor.getCursorKeypressHandler().keyPressed(keyEvent);
        Assert.assertEquals(2, cursor.getSpeed());
    }

    @Test
    public void speedPowerupTestFromFast() {
        cursor = new Cursor(new Point(0, 0), 0, 0, stix, 1, 1);
        cursor.getCursorPowerupHandler().setCurrentPowerup(PowerUpType.SPEED);
        cursor.setSpeed(2);

        cursor.getCursorKeypressHandler().keyPressed(keyEventMock);
        Assert.assertEquals(3, cursor.getSpeed());
    }

    @Test
    public void stopFuseDrawingTest() {
        cursor = new Cursor(new Point(0, 0), 0, 0, stix, 1, 1);
        cursor.getCursorKeypressHandler().addKey(keyEventMock.getCode());
        cursor.setDrawing(true);

        FuseHandler fuseHandler = new FuseHandler(cursor);
        Fuse fuse = new Fuse(0, 0, 1, 1, stix);
        fuse.notMoving();
        fuseHandler.setFuse(fuse);
        cursor.setFuseHandler(fuseHandler);

        cursor.getCursorKeypressHandler().keyPressed(keyEventMock);
        Assert.assertFalse(fuse.isMoving());
    }

    @Test
    public void keyPressedContainsNotDrawing() {
        cursor = new Cursor(new Point(0, 0), 0, 0, stix, 1, 1);
        cursor.getCursorKeypressHandler().getArrowKeys().add(keyEventMock.getCode());
        cursor.setDrawing(false);

        FuseHandler fuseHandler = new FuseHandler(cursor);
        Fuse fuse = new Fuse(0, 0, 1, 1, stix);
        fuse.notMoving();
        fuseHandler.setFuse(fuse);
        cursor.setFuseHandler(fuseHandler);

        cursor.getCursorKeypressHandler().keyPressed(keyEventMock);
        Assert.assertTrue(!fuse.isMoving());
    }

    @Test
    public void keyPressedContainsNoFuse() {
        cursor = new Cursor(new Point(0, 0), 0, 0, stix, 1, 1);
        cursor.getCursorKeypressHandler().getArrowKeys().add(keyEventMock.getCode());
        cursor.setDrawing(false);

        FuseHandler fuseHandler = mock(FuseHandler.class);
        cursor.setFuseHandler(fuseHandler);

        cursor.getCursorKeypressHandler().keyPressed(keyEventMock);
        verify(fuseHandler, times(0)).getFuse();
    }

    @Test
    public void keyPressedDrawingContainsNoFuse() {
        cursor = new Cursor(new Point(0, 0), 0, 0, stix, 1, 1);
        cursor.getCursorKeypressHandler().getArrowKeys().add(keyEventMock.getCode());
        cursor.setDrawing(true);

        FuseHandler fuseHandler = mock(FuseHandler.class);
        when(fuseHandler.hasFuse()).thenReturn(false);
        cursor.setFuseHandler(fuseHandler);

        cursor.getCursorKeypressHandler().keyPressed(keyEventMock);
        verify(fuseHandler, times(0)).getFuse();
    }

    @Test
    public void keyReleasedCurrentMove() {
        cursor = new Cursor(new Point(1, 1), 1, 1, stix, 1, 1);
        cursor.getCursorKeypressHandler().setCurrentMove(keyEventMock.getCode());
        FuseHandler fuseHandler = mock(FuseHandler.class);
        cursor.setFuseHandler(fuseHandler);

        cursor.getCursorKeypressHandler().keyReleased(KeyCode.A);

        verify(fuseHandler, times(1)).handleFuse();
        Assert.assertNull(cursor.getCursorKeypressHandler().getCurrentMove());
    }

    @Test
    public void keyReleasedFastMoveKey() {
        cursor = new Cursor(new Point(1, 1), 1, 1, stix, 1, 1);
        cursor.getCursorKeypressHandler().setCurrentMove(keyEventMock.getCode());
        cursor.setDrawing(true);
        cursor.setSpeed(3);
        FuseHandler fuseHandler = mock(FuseHandler.class);
        cursor.setFuseHandler(fuseHandler);
        cursor.getCursorKeypressHandler().setFastMoveKey(KeyCode.K);

        cursor.getCursorKeypressHandler().keyReleased(KeyCode.K);

        verify(fuseHandler, times(1)).handleFuse();
        Assert.assertFalse(cursor.isDrawing());
        Assert.assertEquals(2, cursor.getSpeed());
    }

    @Test
    public void keyReleasedSlowMoveKey() {
        cursor = new Cursor(new Point(1, 1), 1, 1, stix, 1, 1);
        cursor.getCursorKeypressHandler().setCurrentMove(keyEventMock.getCode());
        cursor.setDrawing(true);
        cursor.setSpeed(3);
        FuseHandler fuseHandler = mock(FuseHandler.class);
        cursor.setFuseHandler(fuseHandler);
        cursor.getCursorKeypressHandler().setSlowMoveKey(KeyCode.K);

        cursor.getCursorKeypressHandler().keyReleased(KeyCode.K);

        verify(fuseHandler, times(1)).handleFuse();
        Assert.assertFalse(cursor.isDrawing());
        Assert.assertEquals(2, cursor.getSpeed());
    }

    @Test
    public void keyReleasedBothMoveKeys() {
        cursor = new Cursor(new Point(1, 1), 1, 1, stix, 1, 1);
        cursor.getCursorKeypressHandler().setCurrentMove(keyEventMock.getCode());
        cursor.setDrawing(true);
        cursor.setSpeed(3);
        FuseHandler fuseHandler = mock(FuseHandler.class);
        cursor.setFuseHandler(fuseHandler);
        cursor.getCursorKeypressHandler().setSlowMoveKey(KeyCode.K);
        cursor.getCursorKeypressHandler().setFastMoveKey(KeyCode.K);
        cursor.getCursorKeypressHandler().keyReleased(KeyCode.K);

        verify(fuseHandler, times(1)).handleFuse();
        Assert.assertFalse(cursor.isDrawing());
        Assert.assertEquals(2, cursor.getSpeed());
    }

    @Test
    public void keyReleasedNoKey() {
        cursor = new Cursor(new Point(1, 1), 1, 1, stix, 1, 1);
        cursor.getCursorKeypressHandler().setCurrentMove(keyEventMock.getCode());
        cursor.setDrawing(true);
        cursor.setSpeed(3);

        FuseHandler fuseHandler = mock(FuseHandler.class);
        cursor.setFuseHandler(fuseHandler);
        cursor.getCursorKeypressHandler().setSlowMoveKey(KeyCode.K);

        cursor.getCursorKeypressHandler().keyReleased(KeyCode.D);

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
        cursor.getCursorKeypressHandler().getArrowKeys().clear();
        cursor.getCursorKeypressHandler().addKey(KeyCode.A);

        Assert.assertTrue(cursor.getCursorKeypressHandler().getArrowKeys().contains(KeyCode.A));
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


    @Test
    public void quadrantTest1() {
        cursor = new Cursor(new Point(0, 0), 2, 2, stix, 1, 1);

        Assert.assertEquals(0, cursor.quadrant());
    }

    @Test
    public void quadrantTest2() {
        cursor = new Cursor(new Point(149, 0), 2, 2, stix, 1, 1);

        Assert.assertEquals(1, cursor.quadrant());
    }

    @Test
    public void quadrantTest3() {
        cursor = new Cursor(new Point(0, 149), 2, 2, stix, 1, 1);

        Assert.assertEquals(3, cursor.quadrant());
    }

    @Test
    public void quadrantTest4() {
        cursor = new Cursor(new Point(149, 149), 2, 2, stix, 1, 1);

        Assert.assertEquals(2, cursor.quadrant());
    }

    @Test
    public void oppositeQuadrantTest1() {
        cursor = new Cursor(new Point(0, 0), 2, 2, stix, 1, 1);

        Assert.assertEquals(2, cursor.oppositeQuadrant());
    }

    @Test
    public void oppositeQuadrantTest2() {
        cursor = new Cursor(new Point(149, 0), 2, 2, stix, 1, 1);

        Assert.assertEquals(3, cursor.oppositeQuadrant());
    }

    @Test
    public void oppositeQuadrantTest3() {
        cursor = new Cursor(new Point(0, 149), 2, 2, stix, 1, 1);

        Assert.assertEquals(1, cursor.oppositeQuadrant());
    }

    @Test
    public void oppositeQuadrantTest4() {
        cursor = new Cursor(new Point(149, 149), 2, 2, stix, 1, 1);

        Assert.assertEquals(0, cursor.oppositeQuadrant());
    }

    /**
     * Verify loops hasnt changed (as would happen when the animation is done).
     */
    @Test
    public void testAnimationDone() {
        cursor = new Cursor(new Point(149, 149), 2, 2, stix, 1, 1);
        cursor.setLoops(Integer.MAX_VALUE);
        cursor.draw(mock(GraphicsContext.class));

        Assert.assertEquals(Integer.MAX_VALUE, cursor.getLoops());
    }

    @Test
    public void testCursorDiedNoLivesStixDrawn() {
        createCursor();
        Cursor spyCursor = spy(cursor);
        spyCursor.setLives(0);
        stix = new Stix();
        stix.addToStix(new Point(1, 1));
        spyCursor.setStix(stix);

        spyCursor.cursorDied();

        Assert.assertEquals(0, spyCursor.getLives());
        verify(spyCursor, times(0)).subtractLife();
    }

    @Test
    public void testCursorDiedEnoughLivesStixDrawn() {
        createCursor();
        Cursor spyCursor = spy(cursor);
        spyCursor.setLives(1);
        stix = new Stix();
        stix.addToStix(new Point(1, 1));
        spyCursor.setStix(stix);

        spyCursor.cursorDied();

        Assert.assertEquals(0, spyCursor.getLives());
        verify(spyCursor, times(1)).subtractLife();
    }

    @Test
    public void testCursorDiedNoLivesStixNull() {
        createCursor();
        Cursor spyCursor = spy(cursor);
        spyCursor.setLives(0);
        spyCursor.setStix(null);

        spyCursor.cursorDied();

        Assert.assertEquals(0, spyCursor.getLives());
        verify(spyCursor, times(0)).setX(anyInt());
    }

    @Test
    public void testCursorDiedEnoughLivesStixNull() {
        createCursor();
        Cursor spyCursor = spy(cursor);
        spyCursor.setLives(1);
        spyCursor.setStix(null);

        spyCursor.cursorDied();

        Assert.assertEquals(0, spyCursor.getLives());
        verify(spyCursor, times(1)).subtractLife();
        verify(spyCursor, times(0)).setX(anyInt());
    }

    @Test
    public void testMoveFuseNull() {
        createCursor();
        FuseHandler fuseHandler = mock(FuseHandler.class);
        when(fuseHandler.getFuse()).thenReturn(null);
        cursor.setFuseHandler(fuseHandler);

        cursor.move();

        verify(fuseHandler, times(1)).getFuse();
    }

    @Test
    public void drawStixAndFuseNull() {
        createCursor();
        cursor.setStix(mock(Stix.class));
        FuseHandler fuseHandler = mock(FuseHandler.class);
        when(fuseHandler.getFuse()).thenReturn(null);
        cursor.setFuseHandler(fuseHandler);
        cursor.setLoops(Integer.MAX_VALUE);

        cursor.draw(mock(GraphicsContext.class));

        verify(fuseHandler, times(2)).getFuse();
    }
}