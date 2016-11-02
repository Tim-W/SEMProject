package nl.tudelft.sem.group2.units;

import javafx.embed.swing.JFXPanel;
import nl.tudelft.sem.group2.board.AreaState;
import nl.tudelft.sem.group2.board.BoardGrid;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.awt.Point;

import static nl.tudelft.sem.group2.global.Globals.BOARD_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.BOARD_WIDTH;
import static nl.tudelft.sem.group2.global.Globals.GRID_HEIGHT;
import static nl.tudelft.sem.group2.global.Globals.GRID_WIDTH;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by gijs on 24-9-2016.
 */
//TODO fix tests
@Ignore
public class SparxTest {
    private Sparx sparx;
    private BoardGrid grid;
    private AreaState[][] boardGrid = new AreaState[GRID_WIDTH + 2][GRID_HEIGHT + 2];

    @BeforeClass
    public static void BeforeClass() {
        new JFXPanel();
    }

    @Before
    public void setUp() throws Exception {
        grid = mock(BoardGrid.class);
        when(grid.getState(new Point(anyInt(), anyInt()))).thenReturn(AreaState.UNCOVERED);
    }

    public void createSparx(int x, int y, int width, int height, SparxDirection e) {
        sparx = new Sparx(x, y, width, height, e);
    }

    public void moveOuter(int x, int y) {
        when(grid.getState(new Point(sparx.getIntX() + x, sparx.getIntY() + y))).thenReturn(AreaState.OUTERBORDER);
        int newx = sparx.getIntX() + x * 2;
        int newy = sparx.getIntY() + y * 2;
        if (newx >= 0 && newx <= BOARD_WIDTH / 2 && newy >= 0 && newy <= BOARD_HEIGHT / 2) {
            boardGrid[newx][newy] = AreaState.OUTERBORDER;
        }
        sparx.move();
    }

    public void moveInner(int x, int y) {
        when(grid.getState(new Point(sparx.getIntX() + x, sparx.getIntY() + y))).thenReturn(AreaState.INNERBORDER);
        int newx = sparx.getIntX() + x * 2;
        int newy = sparx.getIntY() + y * 2;
        if (newx >= 0 && newx <= BOARD_WIDTH / 2 && newy >= 0 && newy <= BOARD_HEIGHT / 2) {
            boardGrid[newx][newy] = AreaState.INNERBORDER;
        }
        sparx.move();
    }

    @Test
    public void moveRightOuterR() throws Exception {
        createSparx(2, 2, 2, 2, SparxDirection.RIGHT);
        int x = sparx.getIntX();
        moveOuter(1, 0);
        Assert.assertEquals(x + 2, sparx.getIntX());
    }

    @Test
    public void moveRightOuterD() throws Exception {
        createSparx(2, 2, 2, 2, SparxDirection.RIGHT);
        int y = sparx.getIntY();
        moveOuter(0, 1);
        Assert.assertEquals(y + 2, sparx.getIntY());
    }

    @Test
    public void moveRightOuterL() throws Exception {
        createSparx(3, 3, 2, 2, SparxDirection.RIGHT);
        int x = sparx.getIntX();
        moveOuter(-1, 0);
        Assert.assertEquals(x - 2, sparx.getIntX());
    }

    @Test
    public void moveRightOuterU() throws Exception {
        createSparx(3, 3, 2, 2, SparxDirection.RIGHT);
        int y = sparx.getIntY();
        moveOuter(0, -1);
        Assert.assertEquals(y - 2, sparx.getIntY());
    }

    @Test
    public void moveRightInnerR() throws Exception {
        createSparx(2, 2, 2, 2, SparxDirection.RIGHT);
        int x = sparx.getIntX();
        moveInner(1, 0);
        Assert.assertEquals(x + 2, sparx.getIntX());
    }

    @Test
    public void moveRightInnerD() throws Exception {
        createSparx(2, 2, 2, 2, SparxDirection.RIGHT);
        int y = sparx.getIntY();
        moveInner(0, 1);
        Assert.assertEquals(y + 2, sparx.getIntY());
    }

    @Test
    public void moveRightInnerL() throws Exception {
        createSparx(3, 3, 2, 2, SparxDirection.RIGHT);
        int x = sparx.getIntX();
        moveInner(-1, 0);
        Assert.assertEquals(x - 2, sparx.getIntX());
    }

    @Test
    public void moveRightInnerU() throws Exception {
        createSparx(3, 3, 2, 2, SparxDirection.RIGHT);
        int y = sparx.getIntY();
        moveInner(0, -1);
        Assert.assertEquals(y - 2, sparx.getIntY());
    }

    @Test
    public void moveLeftOuterR() throws Exception {
        createSparx(2, 2, 2, 2, SparxDirection.LEFT);
        int x = sparx.getIntX();
        moveOuter(1, 0);
        Assert.assertEquals(x + 2, sparx.getIntX());
    }

    @Test
    public void moveLeftOuterD() throws Exception {
        createSparx(3, 3, 2, 2, SparxDirection.LEFT);
        int y = sparx.getIntY();
        moveOuter(0, 1);
        Assert.assertEquals(y + 2, sparx.getIntY());
    }

    @Test
    public void moveLeftOuterL() throws Exception {
        createSparx(3, 3, 2, 2, SparxDirection.LEFT);
        int x = sparx.getIntX();
        moveOuter(-1, 0);
        Assert.assertEquals(x - 2, sparx.getIntX());
    }

    @Test
    public void moveLeftOuterU() throws Exception {
        createSparx(3, 3, 2, 2, SparxDirection.LEFT);
        int y = sparx.getIntY();
        moveOuter(0, -1);
        Assert.assertEquals(y - 2, sparx.getIntY());
    }

    @Test
    public void moveLeftInnerR() throws Exception {
        createSparx(3, 3, 2, 2, SparxDirection.LEFT);
        int x = sparx.getIntX();
        moveInner(1, 0);
        Assert.assertEquals(x + 2, sparx.getIntX());
    }

    @Test
    public void moveLeftInnerD() throws Exception {
        createSparx(3, 3, 2, 2, SparxDirection.LEFT);
        int y = sparx.getIntY();
        moveInner(0, 1);
        Assert.assertEquals(y + 2, sparx.getIntY());
    }

    @Test
    public void moveLeftInnerL() throws Exception {
        createSparx(3, 3, 2, 2, SparxDirection.LEFT);
        int x = sparx.getIntX();
        moveInner(-1, 0);
        Assert.assertEquals(x - 2, sparx.getIntX());
    }

    @Test
    public void moveLeftInnerU() throws Exception {
        createSparx(3, 3, 2, 2, SparxDirection.LEFT);
        int y = sparx.getIntY();
        moveInner(0, -1);
        Assert.assertEquals(y - 2, sparx.getIntY());
    }

    @Test
    public void moveOneRR() throws Exception {
        createSparx(BOARD_WIDTH / 2 - 1, BOARD_HEIGHT / 2 - 1, 2, 2, SparxDirection.RIGHT);
        int dim = sparx.getIntX();
        moveOuter(1, 0);
        Assert.assertEquals(dim + 1, sparx.getIntX());
    }

    @Test
    public void moveOneRD() throws Exception {
        createSparx(BOARD_WIDTH / 2 - 1, BOARD_HEIGHT / 2 - 1, 2, 2, SparxDirection.RIGHT);
        int dim = sparx.getIntY();
        moveOuter(0, 1);
        Assert.assertEquals(dim + 1, sparx.getIntY());
    }

    @Test
    public void moveOneRL() throws Exception {
        createSparx(1, 1, 2, 2, SparxDirection.RIGHT);
        int dim = sparx.getIntX();
        moveOuter(-1, 0);
        Assert.assertEquals(dim - 1, sparx.getIntX());
    }

    @Test
    public void moveOneRU() throws Exception {
        createSparx(1, 1, 2, 2, SparxDirection.RIGHT);
        int dim = sparx.getIntY();
        moveOuter(0, -1);
        Assert.assertEquals(dim - 1, sparx.getIntY());
    }

    @Test
    public void moveOneLR() throws Exception {
        createSparx(BOARD_WIDTH / 2 - 1, BOARD_HEIGHT / 2 - 1, 2, 2, SparxDirection.LEFT);
        int dim = sparx.getIntX();
        moveOuter(1, 0);
        Assert.assertEquals(dim + 1, sparx.getIntX());
    }

    @Test
    public void moveOneLD() throws Exception {
        createSparx(BOARD_WIDTH / 2 - 1, BOARD_HEIGHT / 2 - 1, 2, 2, SparxDirection.LEFT);
        int dim = sparx.getIntY();
        moveOuter(0, 1);
        Assert.assertEquals(dim + 1, sparx.getIntY());
    }

    @Test
    public void moveOneLL() throws Exception {
        createSparx(1, 1, 2, 2, SparxDirection.LEFT);
        int dim = sparx.getIntX();
        moveOuter(-1, 0);
        Assert.assertEquals(dim - 1, sparx.getIntX());
    }

    @Test
    public void moveOneLU() throws Exception {
        createSparx(1, 1, 2, 2, SparxDirection.LEFT);
        int dim = sparx.getIntY();
        moveOuter(0, -1);
        Assert.assertEquals(dim - 1, sparx.getIntY());
    }
}