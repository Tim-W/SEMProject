package nl.tudelft.sem.group2.gameController;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import nl.tudelft.sem.group2.JavaFXThreadingRule;
import nl.tudelft.sem.group2.ScoreCounter;
import nl.tudelft.sem.group2.board.BoardGrid;
import nl.tudelft.sem.group2.collisions.CollisionHandler;
import nl.tudelft.sem.group2.level.Level;
import nl.tudelft.sem.group2.level.LevelHandler;
import nl.tudelft.sem.group2.scenes.GameScene;
import nl.tudelft.sem.group2.units.Cursor;
import nl.tudelft.sem.group2.units.Fuse;
import nl.tudelft.sem.group2.units.Sparx;
import nl.tudelft.sem.group2.units.Stix;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static nl.tudelft.sem.group2.global.Globals.CURSOR_FAST;
import static nl.tudelft.sem.group2.global.Globals.CURSOR_SLOW;
import static nl.tudelft.sem.group2.global.Globals.LEVELS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the GameController methods.
 */
public class GameControllerTest {


    private static final int PREVIOUSTIME = 1;
    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();
    private GameController gameController;
    private Cursor spyCursor;

    @After
    public void tearDown() throws Exception {
        gameController.getAnimationTimer().stop();

    }

    @Before
    public void setUp() {
        GameController.deleteGameController();
        gameController = GameController.getInstance();
        gameController.initializeSinglePlayer();
        spyCursor = spy(gameController.getCursors().get(0));
        gameController.getCursors().set(0, spyCursor);
        gameController.getUnits().clear();
        gameController.getUnits().add(spyCursor);

        gameController.setPreviousTime(PREVIOUSTIME);
    }

    @Test
    public void initializeMultiPlayer() throws Exception {
        GameController.deleteGameController();
        gameController = GameController.getInstance();
        gameController.initializeMultiPlayer();
        Assert.assertEquals(2, gameController.getCursors().size());
        gameController.setPreviousTime(PREVIOUSTIME);
    }

    @Test
    public void keyReleasedSpace() throws Exception {
        gameController.keyReleased(new KeyEvent(null, null, KeyEvent.KEY_RELEASED, " ", "", KeyCode.SPACE, false, false,
                false, false));
        gameController.getAnimationTimer().handle(PREVIOUSTIME + 200000000);
        verify(spyCursor, times(1)).getCursorKeypressHandler();
    }

    @Test
    public void keyPressedSpace() throws Exception {
        boolean isRunning = gameController.getLevelHandler().getLevel().isRunning();
        gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.SPACE, false, false,
                false, false));
        gameController.getAnimationTimer().handle(PREVIOUSTIME + 200000000);
        Assert.assertEquals(isRunning, !gameController.getLevelHandler().getLevel().isRunning());
    }

    @Test
    public void keyPressedArrow() throws Exception {
        gameController.getLevelHandler().getLevel().start();
        gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.D, false, false,
                false, false));
        gameController.getAnimationTimer().handle(PREVIOUSTIME + 200000000);
        verify(spyCursor, times(1)).getCursorKeypressHandler();
    }

    @Test
    public void keyPressedArrowSetCursorSpeedSlow() throws Exception {
        spyCursor.setSpeed(CURSOR_SLOW);
        spyCursor.setDrawing(true);
        gameController.getLevelHandler().getLevel().start();
        gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.D, false, false,
                false, false));
        gameController.getAnimationTimer().handle(PREVIOUSTIME + 200000000);
        verify(spyCursor, times(2)).setSpeed(CURSOR_SLOW);
    }
    @Test
    public void keyPressedArrowSetCursorSpeedFast() throws Exception {
        spyCursor.setDrawing(true);
        gameController.getLevelHandler().getLevel().start();
        gameController.keyPressed(new KeyEvent(null, null, KeyEvent.KEY_PRESSED, " ", "", KeyCode.D, false, false,
                false, false));
        gameController.getAnimationTimer().handle(PREVIOUSTIME + 200000000);
        verify(spyCursor, times(1)).setSpeed(CURSOR_FAST);
    }
    @Test
    public void testHandle() throws Exception {
        GameScene mock = mock(GameScene.class);
        gameController.setGameScene(mock);
        gameController.getAnimationTimer().handle(PREVIOUSTIME + 200000000);
        verify(mock, times(1)).draw(any());
    }

    @Test
    public void testNotHandle() throws Exception {
        GameScene mock = mock(GameScene.class);
        gameController.setGameScene(mock);
        gameController.getAnimationTimer().handle(PREVIOUSTIME);
        verify(mock, times(0)).draw(any());
    }

    @Test
    public void testHandleLevelRunning() throws Exception {
        GameScene mock = mock(GameScene.class);
        gameController.setGameScene(mock);
        gameController.getLevelHandler().getLevel().start();
        verify(mock, times(1)).move(any());
    }

    @Test
    public void testHandleLevelWon() throws Exception {
        LevelHandler levelHandler = mock(LevelHandler.class);
        gameController.setLevelHandler(levelHandler);
        Level level = mock(Level.class);
        when(levelHandler.getLevel()).thenReturn(level);
        when(level.isRunning()).thenReturn(true);
        when(level.getBoardGrid()).thenReturn(new BoardGrid());
        ScoreCounter scoreCounter = mock(ScoreCounter.class);
        spyCursor.setScoreCounter(scoreCounter);
        when(scoreCounter.hasWon()).thenReturn(true);
        when(levelHandler.getLevelID()).thenReturn(LEVELS);

        gameController.getAnimationTimer().handle(PREVIOUSTIME + 200000000);
        Assert.assertEquals("Game won!", gameController.getGameScene().getMessage());
    }

    @Test
    public void testHandleNextLevel() throws Exception {
        LevelHandler levelHandler = mock(LevelHandler.class);
        gameController.setLevelHandler(levelHandler);
        Level level = mock(Level.class);
        when(levelHandler.getLevel()).thenReturn(level);
        when(level.isRunning()).thenReturn(true);
        when(level.getBoardGrid()).thenReturn(new BoardGrid());
        ScoreCounter scoreCounter = mock(ScoreCounter.class);
        spyCursor.setScoreCounter(scoreCounter);
        when(scoreCounter.hasWon()).thenReturn(true);

        gameController.getAnimationTimer().handle(PREVIOUSTIME + 200000000);
        Assert.assertEquals("Press SPACE to go to the next level!", gameController.getGameScene().getMessage());
    }

    @Test
    public void testHandleGameOver() throws Exception {
        LevelHandler levelHandler = mock(LevelHandler.class);
        gameController.setLevelHandler(levelHandler);
        Level level = mock(Level.class);
        when(levelHandler.getLevel()).thenReturn(level);
        when(level.isRunning()).thenReturn(true);
        when(level.getBoardGrid()).thenReturn(new BoardGrid());
        ScoreCounter scoreCounter = mock(ScoreCounter.class);
        spyCursor.setScoreCounter(scoreCounter);
        CollisionHandler collisionHandler = mock(CollisionHandler.class);
        when(collisionHandler.collisions(any(), any())).thenReturn(true);
        gameController.setCollisionHandler(collisionHandler);
        when(scoreCounter.hasWon()).thenReturn(false);
        when(spyCursor.getLives()).thenReturn(0);

        gameController.getAnimationTimer().handle(PREVIOUSTIME + 200000000);
        Assert.assertEquals(" Game Over! ", gameController.getGameScene().getMessage());
    }

    /**
     * test addUnit not to add two fuses.
     *
     * @throws Exception
     */
    @Test
    public void testAddUnit() throws Exception {
        int oldLength = gameController.getUnits().size();
        Fuse fuse = new Fuse(1, 1, 1, 1, new Stix());
        gameController.getUnits().add(fuse);
        Assert.assertTrue(gameController.getUnits().contains(fuse));
        gameController.getUnits().add(fuse);
        Assert.assertEquals(oldLength + 1, gameController.getUnits().size());
    }

    @Test
    public void testRemoveUnitNull() throws Exception {
        int size = gameController.getUnits().size();
        gameController.removeUnit(null);
        Assert.assertEquals(size + 2, gameController.getUnits().size());

    }

    @Test
    public void testRemoveUnitTwoSparx() throws Exception {

        gameController.getUnits().add(mock(Sparx.class));
        gameController.getUnits().add(mock(Sparx.class));
        int size = gameController.getUnits().size();
        gameController.removeUnit(null);
        Assert.assertEquals(size, gameController.getUnits().size());

    }

    @Test
    public void testAdd3Cursor() throws Exception {
        int size = gameController.getCursors().size();
        gameController.addCursor(mock(Cursor.class));
        gameController.addCursor(mock(Cursor.class));
        gameController.addCursor(mock(Cursor.class));

        Assert.assertEquals(size + 1, gameController.getCursors().size());

    }

    @Test
    public void testAddCursorNull() throws Exception {
        int size = gameController.getCursors().size();
        gameController.addCursor(null);

        Assert.assertEquals(size, gameController.getCursors().size());

    }

    @Test
    public void testUnitFuse() throws Exception {
        int size = gameController.getUnits().size();
        gameController.addUnit(mock(Fuse.class));

        Assert.assertEquals(size, gameController.getUnits().size());

    }

    @Test
    public void testGetBoardGridNull() throws Exception {
        LevelHandler levelHandler = mock(LevelHandler.class);
        gameController.setLevelHandler(levelHandler);
        when(levelHandler.getLevel()).thenReturn(null);
        Assert.assertEquals(null, gameController.getGrid());

    }
}