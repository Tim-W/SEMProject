package nl.tudelft.sem.group2.units;

import javafx.embed.swing.JFXPanel;
import nl.tudelft.sem.group2.AreaState;
import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.LaunchApp;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static nl.tudelft.sem.group2.global.Globals.BOARD_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.BOARD_WIDTH;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for the sparx.
 */
public class SparxTest {
    /*@Test
    public void move() throws Exception {

    }

    @Test
    public void toString() throws Exception {

    }

    @Test
    public void logCurrentMove() throws Exception {

    }*/
    private Sparx sparx;
    private AreaTracker areaTracker;
    private AreaState[][] boardGrid = new AreaState[LaunchApp.getGridWidth() + 2][LaunchApp.getGridHeight() + 2];


    @Before
    public void setUp() throws Exception {
        new JFXPanel();
        areaTracker = mock(AreaTracker.class);
        when(areaTracker.getBoardGrid()).thenReturn(boardGrid);
        for (int i = 0; i < boardGrid.length; i++) {
            for (int j = 0; j < boardGrid[i].length; j++) {
                boardGrid[j][i] = AreaState.UNCOVERED;
            }
        }
    }

    public void createSparx(int x, int y, int width, int height, SparxDirection e) {
        sparx = new Sparx(x, y, width, height, areaTracker, e);
        sparx.setAreaTracker(areaTracker);
    }

    public void moveOuter(int x, int y) {
        boardGrid[sparx.getX() + x][sparx.getY() + y] = AreaState.OUTERBORDER;
        int newx = sparx.getX() + x * 2;
        int newy = sparx.getY() + y * 2;
        if (newx >= 0 && newx <= BOARD_WIDTH / 2 && newy >= 0 && newy <= BOARD_HEIGHT / 2) {
            boardGrid[newx][newy] = AreaState.OUTERBORDER;
        }
        sparx.move();
    }

    public void moveInner(int x, int y) {
        boardGrid[sparx.getX() + x][sparx.getY() + y] = AreaState.INNERBORDER;
        int newx = sparx.getX() + x * 2;
        int newy = sparx.getY() + y * 2;
        if (newx >= 0 && newx <= BOARD_WIDTH / 2 && newy >= 0 && newy <= BOARD_HEIGHT / 2) {
            boardGrid[newx][newy] = AreaState.INNERBORDER;
        }
        sparx.move();
    }

    @Test
    public void moveRightOuterR() throws Exception {
        createSparx(2, 2, 2, 2, SparxDirection.RIGHT);
        int x = sparx.getX();
        moveOuter(1, 0);
        Assert.assertEquals(x + 2, sparx.getX());
    }

    @Test
    public void moveRightOuterD() throws Exception {
        createSparx(2, 2, 2, 2, SparxDirection.RIGHT);
        int y = sparx.getY();
        moveOuter(0, 1);
        Assert.assertEquals(y + 2, sparx.getY());
    }

    @Test
    public void moveRightOuterL() throws Exception {
        createSparx(3, 3, 2, 2, SparxDirection.RIGHT);
        int x = sparx.getX();
        moveOuter(-1, 0);
        Assert.assertEquals(x - 2, sparx.getX());
    }

    @Test
    public void moveRightOuterU() throws Exception {
        createSparx(3, 3, 2, 2, SparxDirection.RIGHT);
        int y = sparx.getY();
        moveOuter(0, -1);
        Assert.assertEquals(y - 2, sparx.getY());
    }

    @Test
    public void moveRightInnerR() throws Exception {
        createSparx(2, 2, 2, 2, SparxDirection.RIGHT);
        int x = sparx.getX();
        moveInner(1, 0);
        Assert.assertEquals(x + 2, sparx.getX());
    }

    @Test
    public void moveRightInnerD() throws Exception {
        createSparx(2, 2, 2, 2, SparxDirection.RIGHT);
        int y = sparx.getY();
        moveInner(0, 1);
        Assert.assertEquals(y + 2, sparx.getY());
    }

    @Test
    public void moveRightInnerL() throws Exception {
        createSparx(3, 3, 2, 2, SparxDirection.RIGHT);
        int x = sparx.getX();
        moveInner(-1, 0);
        Assert.assertEquals(x - 2, sparx.getX());
    }

    @Test
    public void moveRightInnerU() throws Exception {
        createSparx(3, 3, 2, 2, SparxDirection.RIGHT);
        int y = sparx.getY();
        moveInner(0, -1);
        Assert.assertEquals(y - 2, sparx.getY());
    }

    @Test
    public void moveLeftOuterR() throws Exception {
        createSparx(2, 2, 2, 2, SparxDirection.LEFT);
        int x = sparx.getX();
        moveOuter(1, 0);
        Assert.assertEquals(x + 2, sparx.getX());
    }

    @Test
    public void moveLeftOuterD() throws Exception {
        createSparx(3, 3, 2, 2, SparxDirection.LEFT);
        int y = sparx.getY();
        moveOuter(0, 1);
        Assert.assertEquals(y + 2, sparx.getY());
    }

    @Test
    public void moveLeftOuterL() throws Exception {
        createSparx(3, 3, 2, 2, SparxDirection.LEFT);
        int x = sparx.getX();
        moveOuter(-1, 0);
        Assert.assertEquals(x - 2, sparx.getX());
    }

    @Test
    public void moveLeftOuterU() throws Exception {
        createSparx(3, 3, 2, 2, SparxDirection.LEFT);
        int y = sparx.getY();
        moveOuter(0, -1);
        Assert.assertEquals(y - 2, sparx.getY());
    }

    @Test
    public void moveLeftInnerR() throws Exception {
        createSparx(3, 3, 2, 2, SparxDirection.LEFT);
        int x = sparx.getX();
        moveInner(1, 0);
        Assert.assertEquals(x + 2, sparx.getX());
    }

    @Test
    public void moveLeftInnerD() throws Exception {
        createSparx(3, 3, 2, 2, SparxDirection.LEFT);
        int y = sparx.getY();
        moveInner(0, 1);
        Assert.assertEquals(y + 2, sparx.getY());
    }

    @Test
    public void moveLeftInnerL() throws Exception {
        createSparx(3, 3, 2, 2, SparxDirection.LEFT);
        int x = sparx.getX();
        moveInner(-1, 0);
        Assert.assertEquals(x - 2, sparx.getX());
    }

    @Test
    public void moveLeftInnerU() throws Exception {
        createSparx(3, 3, 2, 2, SparxDirection.LEFT);
        int y = sparx.getY();
        moveInner(0, -1);
        Assert.assertEquals(y - 2, sparx.getY());
    }

    @Test
    public void moveOneRR() throws Exception {
        createSparx(BOARD_WIDTH / 2 - 1, BOARD_HEIGHT / 2 - 1, 2, 2, SparxDirection.RIGHT);
        int dim = sparx.getX();
        moveOuter(1, 0);
        Assert.assertEquals(dim + 1, sparx.getX());
    }

    @Test
    public void moveOneRD() throws Exception {
        createSparx(BOARD_WIDTH / 2 - 1, BOARD_HEIGHT / 2 - 1, 2, 2, SparxDirection.RIGHT);
        int dim = sparx.getY();
        moveOuter(0, 1);
        Assert.assertEquals(dim + 1, sparx.getY());
    }

    @Test
    public void moveOneRL() throws Exception {
        createSparx(1, 1, 2, 2, SparxDirection.RIGHT);
        int dim = sparx.getX();
        moveOuter(-1, 0);
        Assert.assertEquals(dim - 1, sparx.getX());
    }

    @Test
    public void moveOneRU() throws Exception {
        createSparx(1, 1, 2, 2, SparxDirection.RIGHT);
        int dim = sparx.getY();
        moveOuter(0, -1);
        Assert.assertEquals(dim - 1, sparx.getY());
    }

    @Test
    public void moveOneLR() throws Exception {
        createSparx(BOARD_WIDTH / 2 - 1, BOARD_HEIGHT / 2 - 1, 2, 2, SparxDirection.LEFT);
        int dim = sparx.getX();
        moveOuter(1, 0);
        Assert.assertEquals(dim + 1, sparx.getX());
    }

    @Test
    public void moveOneLD() throws Exception {
        createSparx(BOARD_WIDTH / 2 - 1, BOARD_HEIGHT / 2 - 1, 2, 2, SparxDirection.LEFT);
        int dim = sparx.getY();
        moveOuter(0, 1);
        Assert.assertEquals(dim + 1, sparx.getY());
    }

    @Test
    public void moveOneLL() throws Exception {
        createSparx(1, 1, 2, 2, SparxDirection.LEFT);
        int dim = sparx.getX();
        moveOuter(-1, 0);
        Assert.assertEquals(dim - 1, sparx.getX());
    }

    @Test
    public void moveOneLU() throws Exception {
        createSparx(1, 1, 2, 2, SparxDirection.LEFT);
        int dim = sparx.getY();
        moveOuter(0, -1);
        Assert.assertEquals(dim - 1, sparx.getY());
    }
}