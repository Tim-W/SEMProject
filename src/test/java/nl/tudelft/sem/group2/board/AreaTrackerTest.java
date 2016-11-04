package nl.tudelft.sem.group2.board;

import nl.tudelft.sem.group2.ScoreCounter;
import nl.tudelft.sem.group2.gameController.GameController;
import nl.tudelft.sem.group2.global.Globals;
import nl.tudelft.sem.group2.level.Level;
import nl.tudelft.sem.group2.level.LevelHandler;
import nl.tudelft.sem.group2.units.Stix;

import java.awt.Point;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for the AreaTracker class.
 */
public class AreaTrackerTest {

    private Stix stix;
    private ScoreCounter scoreCounter = mock(ScoreCounter.class);
    private BoardGrid grid;


    private void setUpMocks() throws Exception {
        instantiateStix();
        grid = mock(BoardGrid.class);
        LevelHandler levelHandler = mock(LevelHandler.class);
        GameController.getInstance().setLevelHandler(levelHandler);
        when(levelHandler.getLevel()).thenReturn(mock(Level.class));
        when(levelHandler.getLevel().getBoardGrid()).thenReturn(grid);
    }

    /**
     * Test method for the default constructor of AreaTracker.
     *
     * @throws Exception when the AreaTracker fails to do its job
     */
    @org.junit.Test
    public void testConstructor() throws Exception {
        assertNotNull(AreaTracker.getInstance());
    }

    /**
     * Test method for the reset method.
     *
     * @throws Exception when the AreaTracker fails to do its job
     */
    @org.junit.Test
    public void testReset() throws Exception {
        assertNotNull(AreaTracker.getInstance());
        AreaTracker.reset();
        assertNull(AreaTracker.getInstanceUnchecked());
        assertNotNull(AreaTracker.getInstance());

    }


    /**
     * Test verify setOuterBorder is called.
     *
     * @throws Exception when the AreaTracker fails to do its job
     */
    @org.junit.Test
    public void testVerifySetOuterBorder() throws Exception {
        setUpMocks();
        stix.addToStix(new Point(1, 1));
        stix.addToStix(new Point(1, 2));
        AreaTracker.getInstance().calculateNewArea(new Point(1, 2), true, stix, scoreCounter, grid);
        verify(grid, times(1)).setState(new Point(1, 1), AreaState.OUTERBORDER);
    }

    /**
     * Test for the calculateNewArea method when slow area is created.
     *
     * @throws Exception when the AreaTracker fails to do its job
     */
    @org.junit.Test
    public void testCalculateNewSlowArea() throws Exception {
        instantiateBigStix();
        AreaTracker areaTracker = AreaTracker.getInstance();
        GameController gc = GameController.getInstance();
        gc.instantiateTestGame(false);

        BoardGrid actualGrid = new BoardGrid();
        areaTracker.calculateNewArea(new Point(1, 2), false, stix, scoreCounter, actualGrid);


        BoardGrid expectedGrid = createExpectedBoardGridQixAboveStix(false);

        for (int i = 0; i < expectedGrid.getWidth(); i++) {
            for (int j = 0; j < expectedGrid.getHeight(); j++) {
                Coordinate coor = new Coordinate(i, j);
                assertEquals(actualGrid.getState(coor), expectedGrid.getState(coor));
            }
        }
    }

    /**
     * Test for the calculateNewArea method when fast area is created and qix is on other side.
     *
     * @throws Exception when the AreaTracker fails to do its job
     */
    @org.junit.Test
    public void testCalculateNewFastAreaWithQixOnOtherSide() throws Exception {
        instantiateBigStix();
        AreaTracker areaTracker = AreaTracker.getInstance();

        BoardGrid actualGrid = new BoardGrid();
        areaTracker.calculateNewArea(new Point(3, 2), true, stix, scoreCounter, actualGrid);

        BoardGrid expectedGrid = createExpectedBoardGridQixUnderStix(true);

        for (int i = 0; i < expectedGrid.getWidth(); i++) {
            for (int j = 0; j < expectedGrid.getHeight(); j++) {
                Coordinate coor = new Coordinate(i, j);
                assertEquals(actualGrid.getState(coor), expectedGrid.getState(coor));
            }
        }
    }

    private BoardGrid createExpectedBoardGridQixAboveStix(boolean fastArea) {
        BoardGrid boardGrid = new BoardGrid();
        for (int x = 0; x < boardGrid.getWidth(); x++) {
            for (int y = 0; y < boardGrid.getHeight(); y++) {
                if (y == boardGrid.getHeight() - 1 && x != 0 && x != boardGrid.getWidth() - 1) {
                    boardGrid.setState(new Coordinate(y, x), AreaState.INNERBORDER);
                } else if (y == 2 && x != 0 && x != boardGrid.getWidth() - 1) {
                    boardGrid.setState(new Coordinate(y, x), AreaState.OUTERBORDER);
                } else if (y > 2 && y < boardGrid.getHeight() - 1) {
                    if (x != 0 && x != boardGrid.getWidth() - 1) {
                        if (fastArea) {
                            boardGrid.setState(new Coordinate(y, x), AreaState.FAST);
                        } else {
                            boardGrid.setState(new Coordinate(y, x), AreaState.SLOW);
                        }
                    } else {
                        boardGrid.setState(new Coordinate(y, x), AreaState.INNERBORDER);
                    }
                }
            }
        }
        return boardGrid;
    }

    private BoardGrid createExpectedBoardGridQixUnderStix(boolean fastArea) {
        BoardGrid boardGrid = new BoardGrid();
        for (int x = 0; x < boardGrid.getWidth(); x++) {
            for (int y = 0; y < boardGrid.getHeight(); y++) {
                if (y == 0 && x != 0 && x != boardGrid.getWidth() - 1) {
                    boardGrid.setState(new Coordinate(y, x), AreaState.INNERBORDER);
                } else if (y == 2 && x != 0 && x != boardGrid.getWidth() - 1) {
                    boardGrid.setState(new Coordinate(y, x), AreaState.OUTERBORDER);
                } else if (y == 1) {
                    if (x != 0 && x != boardGrid.getWidth() - 1) {
                        if (fastArea) {
                            boardGrid.setState(new Coordinate(y, x), AreaState.FAST);
                        } else {
                            boardGrid.setState(new Coordinate(y, x), AreaState.SLOW);
                        }
                    } else {
                        boardGrid.setState(new Coordinate(y, x), AreaState.INNERBORDER);
                    }
                }
            }
        }
        return boardGrid;
    }

    private void instantiateStix() {
        stix = new Stix();
        stix.addToStix(new Point(2, 0));
        stix.addToStix(new Point(2, 1));
        stix.addToStix(new Point(2, 2));
        stix.addToStix(new Point(2, 3));
        stix.addToStix(new Point(2, 4));
    }

    private void instantiateBigStix() {
        stix = new Stix();
        for (int i = 0; i <= Globals.GRID_WIDTH; i++) {
            stix.addToStix(new Point(2, i));
        }
    }
}