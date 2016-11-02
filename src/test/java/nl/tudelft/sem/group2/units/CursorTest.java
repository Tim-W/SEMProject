package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import nl.tudelft.sem.group2.AreaState;
import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.JavaFXThreadingRule;
import nl.tudelft.sem.group2.ScoreCounter;
import nl.tudelft.sem.group2.controllers.GameController;
import nl.tudelft.sem.group2.powerups.PowerUpType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.awt.Point;

import static nl.tudelft.sem.group2.global.Globals.BOARD_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.BOARD_WIDTH;
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
    private AreaTracker areaTracker;
    private GameController gameController;
    private Stix stix = mock(Stix.class);
    private int x;
    private int y;
    private KeyEvent keyEventMock;


    @Before
    public void setUp() throws Exception {
        GameController.deleteGameController();
        gameController = GameController.getInstance();
        gameController.getAnimationTimer().stop();
        areaTracker = gameController.getAreaTracker();
        keyEventMock = mock(KeyEvent.class);
        when(keyEventMock.getCode()).thenReturn(KeyCode.A);
    }

    public void createCursor() {
        gameController.initializeSinglePlayer();
        if (cursor != null) {
            gameController.getCursors().set(0, cursor);
        } else {
            cursor = gameController.getCursors().get(0);
        }
        cursor.setSpeed(1);
        x = cursor.getX();
        y = cursor.getY();
        cursor.setStix(stix);
        cursor.setAreaTracker(areaTracker);
    }

    public void moveCursor(KeyCode k, int x, int y, boolean outerborder) {
        int horizontal = 0;
        int vertical = 0;
        if (k.equals(KeyCode.RIGHT) || k.equals(KeyCode.LEFT)) {
            horizontal = 1;
        } else {
            vertical = 1;
        }
        if (outerborder) {
            areaTracker.getBoardGrid()[x][y] = AreaState.OUTERBORDER;
        } else {
            areaTracker.getBoardGrid()[x - horizontal][y - vertical] = AreaState.UNCOVERED;
            areaTracker.getBoardGrid()[x + horizontal][y + vertical] = AreaState.UNCOVERED;
            areaTracker.getBoardGrid()[cursor.getX()][cursor.getY()] = AreaState.UNCOVERED;
        }
        cursor.setCurrentMove(k);
        cursor.move();
    }

    @Test
    public void dontMove() throws Exception {
        createCursor();
        cursor.setCurrentMove(null);
        cursor.move();
        Assert.assertEquals(x, cursor.getX());
    }

    @Test
    public void dontMoveL() throws Exception {
        createCursor();
        cursor.setX(0);
        x = cursor.getX();
        cursor.setCurrentMove(KeyCode.LEFT);
        cursor.move();
        Assert.assertEquals(x, cursor.getX());
    }

    @Test
    public void dontMoveR() throws Exception {
        createCursor();
        cursor.setX(BOARD_WIDTH / 2);
        x = cursor.getX();
        cursor.setCurrentMove(KeyCode.RIGHT);
        cursor.move();
        Assert.assertEquals(x, cursor.getX());
    }

    @Test
    public void dontMoveU() throws Exception {
        createCursor();
        cursor.setY(0);
        int dim = cursor.getY();
        cursor.setCurrentMove(KeyCode.UP);
        cursor.move();
        Assert.assertEquals(dim, cursor.getY());
    }

    @Test
    public void dontMoveD() throws Exception {
        createCursor();
        cursor.setY(BOARD_HEIGHT / 2);
        int dim = cursor.getY();
        cursor.setCurrentMove(KeyCode.DOWN);
        cursor.move();
        Assert.assertEquals(dim, cursor.getY());
    }

    @Test
    public void moveRight() throws Exception {
        createCursor();
        cursor.setDrawing(true);
        moveCursor(KeyCode.RIGHT, x + 1, y, false);
        Assert.assertEquals(x + 1, cursor.getX());
    }

    /*@Test
    public void moveRightDraw() throws Exception {
        createCursor();
        cursor.setDrawing(true);
        cursor.setX(x-1);
        int horizontal = 0;
        int vertical = 1;
        areaTracker.getBoardGrid()[x][y] = AreaState.OUTERBORDER;
        areaTracker.getBoardGrid()[x - vertical][y - horizontal] = AreaState.UNCOVERED;
        areaTracker.getBoardGrid()[x + vertical][y + horizontal] = AreaState.UNCOVERED;
        cursor.setCurrentMove(KeyCode.RIGHT);
        cursor.move();
        verify(stix, times(2)).addToStix(any());
    }*/

    @Test
    public void moveRightOuterBorder() throws Exception {
        createCursor();
        moveCursor(KeyCode.RIGHT, x + 1, y, true);
        Assert.assertEquals(x + 1, cursor.getX());
    }

    @Test
    public void dontMoveR2() throws Exception {
        createCursor();
        moveCursor(KeyCode.RIGHT, x, y, false);
        Assert.assertEquals(x, cursor.getX());
    }

    @Test
    public void dontMoveR3() throws Exception {
        createCursor();
        cursor.setDrawing(true);
        cursor.setX(cursor.getX() - 1);
        //stixCoordinates.add(new Point(x + 1, y));
        //areaTracker.getBoardGrid()[x][y] = AreaState.UNCOVERED;
        moveCursor(KeyCode.RIGHT, x, y, false);
        Assert.assertEquals(x, cursor.getX());
    }

   /* @Test
    public void dontMoveR4() throws Exception {
        createCursor();
        cursor.setDrawing(true);
        stixCoordinates.add(new Point(x + 2, y));
        moveCursor(KeyCode.RIGHT, x + 1, y, false);
        Assert.assertEquals(x, cursor.getX());
    }

    @Test
    public void dontMoveR5() throws Exception {
        createCursor();
        cursor.setDrawing(true);
        int horizontal = 1;
        int vertical = 0;
        areaTracker.getBoardGrid()[x + horizontal + vertical][y + horizontal + vertical] = AreaState.OUTERBORDER;
        when(areaTracker.getBoardGrid()).thenReturn(boardGrid);
        cursor.setCurrentMove(KeyCode.RIGHT);
        cursor.move();
        Assert.assertEquals(x, cursor.getX());
    }*/

    /*@Test
    public void dontMoveR6() throws Exception {
        createCursor();
        cursor.setDrawing(true);
        int horizontal = 1;
        int vertical = 0;
        areaTracker.getBoardGrid()[x + horizontal - vertical][y - horizontal + vertical] = AreaState.OUTERBORDER;
        //when(areaTracker.getBoardGrid()).thenReturn(boardGrid);
        cursor.setCurrentMove(KeyCode.RIGHT);
        cursor.move();
        Assert.assertEquals(x, cursor.getX());
    }*/

    @Test
    public void testGetCurrentMove() throws Exception {
        createCursor();
        cursor.setCurrentMove(KeyCode.RIGHT);
        Assert.assertEquals(KeyCode.RIGHT, cursor.getCurrentMove());
    }

    @Test
    public void isFast() throws Exception {
        createCursor();
        boolean oldValue = cursor.isFast();
        cursor.setFast(!oldValue);
        Assert.assertEquals(!oldValue, cursor.isFast());
    }

    @Test
    public void setFast() throws Exception {
        createCursor();
        boolean oldValue = cursor.isFast();
        cursor.setFast(!oldValue);
        Assert.assertEquals(!oldValue, cursor.isFast());
    }

    @Test
    public void draw() throws Exception {
        Cursor spy = spy(new Cursor(new Point(1, 1), 1, 1, areaTracker, stix, 3, 1));
        spy.draw(new Canvas(1, 1));
        verify(spy).getSpriteIndex();
    }

    /*@Test
    public void testCursorHasDied() throws Exception {
        createCursor();
        Assert.assertEquals(2, cursor.getLives());
        cursor.cursorDied();
        Assert.assertEquals(1, cursor.getLives());
        cursor.cursorDied();
        Assert.assertEquals(0, cursor.getLives());
        cursor.cursorDied();
        Assert.assertEquals(0, cursor.getLives());
    }*/

    @Test
    public void speedPowerupTestFromSlow() {
        cursor = new Cursor(new Point(0, 0), 0, 0, areaTracker, stix, 1, 1);
        cursor.getPowerupHandler().setCurrentPowerup(PowerUpType.SPEED);
        cursor.setSpeed(1);

        KeyEvent keyEvent = mock(KeyEvent.class);
        when(keyEvent.getCode()).thenReturn(KeyCode.A);
        cursor.keyPressed(keyEvent);
        Assert.assertEquals(2, cursor.getSpeed());
    }

    @Test
    public void speedPowerupTestFromFast() {
        cursor = new Cursor(new Point(0, 0), 0, 0, areaTracker, stix, 1, 1);
        cursor.getPowerupHandler().setCurrentPowerup(PowerUpType.SPEED);
        cursor.setSpeed(2);

        cursor.keyPressed(keyEventMock);
        Assert.assertEquals(3, cursor.getSpeed());
    }

    @Test
    public void stopFuseDrawingTest() {
        cursor = new Cursor(new Point(0, 0), 0, 0, areaTracker, stix, 1, 1);
        cursor.getArrowKeys().add(keyEventMock.getCode());
        cursor.setDrawing(true);

        FuseHandler fuseHandler = new FuseHandler(cursor);
        Fuse fuse = new Fuse(0, 0, 1, 1, areaTracker, stix);
        fuse.setMoving(true);
        fuseHandler.setFuse(fuse);
        cursor.setFuseHandler(fuseHandler);

        cursor.keyPressed(keyEventMock);
        Assert.assertFalse(fuse.isMoving());
    }

    @Test
    public void keyPressedContainsNotDrawing() {
        cursor = new Cursor(new Point(0, 0), 0, 0, areaTracker, stix, 1, 1);
        cursor.getArrowKeys().add(keyEventMock.getCode());
        cursor.setDrawing(false);

        FuseHandler fuseHandler = new FuseHandler(cursor);
        Fuse fuse = new Fuse(0, 0, 1, 1, areaTracker, stix);
        fuse.setMoving(true);
        fuseHandler.setFuse(fuse);
        cursor.setFuseHandler(fuseHandler);

        cursor.keyPressed(keyEventMock);
        Assert.assertTrue(fuse.isMoving());
    }

    @Test
    public void keyPressedContainsNoFuse() {
        cursor = new Cursor(new Point(0, 0), 0, 0, areaTracker, stix, 1, 1);
        cursor.getArrowKeys().add(keyEventMock.getCode());
        cursor.setDrawing(false);

        FuseHandler fuseHandler = mock(FuseHandler.class);
        cursor.setFuseHandler(fuseHandler);

        cursor.keyPressed(keyEventMock);
        verify(fuseHandler, times(0)).getFuse();
    }

    @Test
    public void keyPressedDrawingContainsNoFuse() {
        cursor = new Cursor(new Point(0, 0), 0, 0, areaTracker, stix, 1, 1);
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
        cursor = new Cursor(new Point(1, 1), 1, 1, areaTracker, stix, 1, 1);
        cursor.setCurrentMove(keyEventMock.getCode());
        FuseHandler fuseHandler = mock(FuseHandler.class);
        cursor.setFuseHandler(fuseHandler);

        cursor.keyReleased(KeyCode.A);

        verify(fuseHandler, times(1)).handleFuse();
        Assert.assertNull(cursor.getCurrentMove());
    }

    @Test
    public void keyReleasedFastMoveKey() {
        cursor = new Cursor(new Point(1, 1), 1, 1, areaTracker, stix, 1, 1);
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
        cursor = new Cursor(new Point(1, 1), 1, 1, areaTracker, stix, 1, 1);
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
        cursor = new Cursor(new Point(1, 1), 1, 1, areaTracker, stix, 1, 1);
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
        cursor = new Cursor(new Point(1, 1), 1, 1, areaTracker, stix, 1, 1);
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
        cursor = new Cursor(new Point(0, 0), 1, 1, areaTracker, stix, 1, 1);

        FuseHandler fuseHandler = mock(FuseHandler.class);
        when(fuseHandler.hasFuse()).thenReturn(true);

        Fuse fuse = mock(Fuse.class);
        when(fuse.intersect(cursor)).thenReturn(true);
        when(fuseHandler.getFuse()).thenReturn(fuse);
        cursor.setFuseHandler(fuseHandler);

        Unit unit = mock(Unit.class);
        when(unit.getX()).thenReturn(10);
        when(unit.getY()).thenReturn(10);

        Assert.assertTrue(cursor.intersect(unit));
    }

    @Test
    public void intersectionFuseAndUnit() {
        cursor = new Cursor(new Point(0, 0), 1, 1, areaTracker, stix, 1, 1);

        FuseHandler fuseHandler = mock(FuseHandler.class);
        when(fuseHandler.hasFuse()).thenReturn(true);

        Fuse fuse = mock(Fuse.class);
        when(fuse.intersect(cursor)).thenReturn(true);
        when(fuseHandler.getFuse()).thenReturn(fuse);
        cursor.setFuseHandler(fuseHandler);

        Unit unit = mock(Unit.class);
        when(unit.getX()).thenReturn(1);
        when(unit.getY()).thenReturn(1);

        Assert.assertTrue(cursor.intersect(unit));
    }

    @Test
    public void intersectionUnitNoFuse() {
        cursor = new Cursor(new Point(0, 0), 1, 1, areaTracker, stix, 1, 1);

        FuseHandler fuseHandler = mock(FuseHandler.class);
        when(fuseHandler.hasFuse()).thenReturn(true);

        Fuse fuse = mock(Fuse.class);
        when(fuse.intersect(cursor)).thenReturn(false);
        when(fuseHandler.getFuse()).thenReturn(fuse);
        cursor.setFuseHandler(fuseHandler);

        Unit unit = mock(Unit.class);
        when(unit.getX()).thenReturn(1);
        when(unit.getY()).thenReturn(1);

        Assert.assertTrue(cursor.intersect(unit));
    }

    @Test
    public void intersectionUnitNullFuse() {
        cursor = new Cursor(new Point(0, 0), 1, 1, areaTracker, stix, 1, 1);

        FuseHandler fuseHandler = mock(FuseHandler.class);
        when(fuseHandler.hasFuse()).thenReturn(false);

        Unit unit = mock(Unit.class);
        when(unit.getX()).thenReturn(1);
        when(unit.getY()).thenReturn(1);

        Assert.assertTrue(cursor.intersect(unit));
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
        cursor = new Cursor(new Point(1, 1), 1, 1, areaTracker, stix, 1, 1);
        cursor.getArrowKeys().clear();
        cursor.addKey(KeyCode.A);

        Assert.assertTrue(cursor.getArrowKeys().contains(KeyCode.A));
    }

    @Test
    public void addLife() {
        cursor = new Cursor(new Point(1, 1), 1, 1, areaTracker, stix, 1, 1);

        ScoreCounter scoreCounter = mock(ScoreCounter.class);
        cursor.setScoreCounter(scoreCounter);

        Assert.assertEquals(1, cursor.getLives());
        cursor.addLife();

        Assert.assertEquals(2, cursor.getLives());
    }

    @Test
    public void subtractLife() {
        cursor = new Cursor(new Point(1, 1), 1, 1, areaTracker, stix, 1, 1);

        ScoreCounter scoreCounter = mock(ScoreCounter.class);
        cursor.setScoreCounter(scoreCounter);

        Assert.assertEquals(1, cursor.getLives());
        cursor.subtractLife();

        Assert.assertEquals(0, cursor.getLives());
    }


}