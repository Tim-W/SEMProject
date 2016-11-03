package nl.tudelft.sem.group2.units;

import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import nl.tudelft.sem.group2.JavaFXThreadingRule;
import nl.tudelft.sem.group2.board.BoardGrid;
import nl.tudelft.sem.group2.controllers.GameController;
import nl.tudelft.sem.group2.global.Globals;
import nl.tudelft.sem.group2.level.Level;
import nl.tudelft.sem.group2.level.LevelHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.awt.Point;
import java.util.ArrayList;

import static edu.emory.mathcs.backport.java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for cursors.
 */
@Ignore
public class CursorTest {

    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();

    private Cursor cursor;
    private Stix stix = mock(Stix.class);
    private BoardGrid grid = mock(BoardGrid.class);
    private int x;
    private int y;

    @Before
    public void setUp() throws Exception {
        grid = mock(BoardGrid.class);
        LevelHandler levelHandler = mock(LevelHandler.class);
        GameController.getInstance().setLevelHandler(levelHandler);
        when(levelHandler.getLevel()).thenReturn(mock(Level.class));
        when(levelHandler.getLevel().getBoardGrid()).thenReturn(grid);
    }

    public void createCursor() {
        KeyCode[] keyCodes = new KeyCode[] {KeyCode.W, KeyCode.S, KeyCode.A, KeyCode.D, KeyCode.Z, KeyCode.X};
        cursor = new Cursor(new Point(0, 0), 2,
                2, stix, Globals.LIVES, 0, asList(keyCodes));
        cursor.setSpeed(1);
        x = cursor.getX();
        y = cursor.getY();
        cursor.setStix(stix);
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
        Cursor spy = spy(new Cursor(new Point(1, 1), 1, 1, stix, 3, 1, new ArrayList<KeyCode>()));
        spy.draw(new Canvas(1, 1).getGraphicsContext2D());
        verify(spy).getSpriteIndex();
    }


}