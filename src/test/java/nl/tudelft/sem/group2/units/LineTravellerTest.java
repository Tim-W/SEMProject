package nl.tudelft.sem.group2.units;

import javafx.embed.swing.JFXPanel;
import nl.tudelft.sem.group2.AreaState;
import nl.tudelft.sem.group2.AreaTracker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by gijs on 25-9-2016.
 */
public class LineTravellerTest {
    private Cursor cursor;

    @Before
    public void setUp() throws Exception {
        new JFXPanel();
        createCursor(new Cursor(2, 2, 2, 2));
    }

    public void createCursor(Cursor c) {
        cursor = c;
    }

    @Test
    public void innerBorderOn() throws Exception {
        AreaTracker areaTracker = mock(AreaTracker.class);
        AreaState[][] boardGrid = new AreaState[1][1];
        boardGrid[0][0] = AreaState.INNERBORDER;
        when(areaTracker.getBoardGrid()).thenReturn(boardGrid);
        cursor.setAreaTracker(areaTracker);
        Assert.assertTrue(cursor.innerBorderOn(0, 0));

    }

    @Test
    public void setSpriteIndex() throws Exception {
        cursor.setSpriteIndex(1);
        Assert.assertEquals(1, cursor.getSpriteIndex());
    }

}