package nl.tudelft.sem.group2.units;

import javafx.embed.swing.JFXPanel;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import nl.tudelft.sem.group2.AreaState;
import nl.tudelft.sem.group2.AreaTracker;
import nl.tudelft.sem.group2.JavaFXThreadingRule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.Point;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * LineTraveller test class
 */
public class LineTravellerTest {
    @Rule
    public JavaFXThreadingRule javafxRule = new JavaFXThreadingRule();

    private Cursor cursor;
    private Stix stix;
    private AreaTracker areaTracker;

    @BeforeClass
    public static void BeforeClass() {
        new JFXPanel();
    }
    @Before
    public void setUp() throws Exception {
        areaTracker = Mockito.mock(AreaTracker.class);
        stix = new Stix();
        createCursor(new Cursor(new Point(2, 2), 2, 2, areaTracker, stix, Color.RED, 3));
    }

    public void createCursor(Cursor c) {
        cursor = c;
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void innerBorderOn() throws Exception {
        AreaTracker areaTracker = mock(AreaTracker.class);
        AreaState[][] boardGrid = new AreaState[1][1];
        boardGrid[0][0] = AreaState.INNERBORDER;
        when(areaTracker.getBoardGrid()).thenReturn(boardGrid);
        cursor.setAreaTracker(areaTracker);
        Assert.assertTrue(cursor.innerBorderOn(0, 0));
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void setSpriteIndex() throws Exception {
        cursor.setSpriteIndex(1);
        Assert.assertEquals(1, cursor.getSpriteIndex());
    }

    /**
     * draw method is not testable.
     * because all variables used are not accesible.
     * and there are no mockable objects.
     */
    /**
     *
     * @throws Exception
     */
    @Test
    public void incrementSpriteIndex() throws Exception {
        Fuse spyFuse = spy(new Fuse(2, 2, 2, 2, areaTracker, stix));
        spyFuse.draw(new Canvas(1, 1));
        verify(spyFuse).setSpriteIndex(anyInt());
    }

    /**
     *
     * @throws Exception
     */
    @Test
    public void getSpriteImage() throws Exception {
        Fuse fuse = new Fuse(2, 2, 2, 2, areaTracker, stix);
        Image[] sprite = new Image[1];
        sprite[0] = new Image("/images/fuse-1.png");
        fuse.setSprite(sprite);
        Assert.assertArrayEquals(sprite, fuse.getSprite());
    }
}