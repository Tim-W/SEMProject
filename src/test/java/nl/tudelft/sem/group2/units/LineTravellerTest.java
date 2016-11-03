package nl.tudelft.sem.group2.units;

import javafx.embed.swing.JFXPanel;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import nl.tudelft.sem.group2.board.BoardGrid;
import nl.tudelft.sem.group2.controllers.GameController;
import nl.tudelft.sem.group2.level.Level;
import nl.tudelft.sem.group2.level.LevelHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test for the line traveller class.
 */
public class LineTravellerTest {
    private Fuse fuse;
    private Stix stix;
    private BoardGrid grid;
    private LevelHandler levelHandler;
    @BeforeClass
    public static void beforeClass() {
        new JFXPanel();
    }

    @Before
    public void setUp() throws Exception {
        stix = new Stix();
        fuse = new Fuse(0, 0, 2, 2, stix);
        grid = mock(BoardGrid.class);
        levelHandler = mock(LevelHandler.class);
        GameController.getInstance().setLevelHandler(levelHandler);
        when(levelHandler.getLevel()).thenReturn(mock(Level.class));
        when(levelHandler.getLevel().getBoardGrid()).thenReturn(grid);
    }

    /**
     * @throws Exception
     */
    @Test
    public void innerBorderOn() throws Exception {
        when(grid.isInnerborder(any())).thenReturn(true);
        Assert.assertTrue(fuse.innerBorderOn());
    }

    /**
     * @throws Exception
     */
    @Test
    public void setSpriteIndex() throws Exception {
        fuse.setSpriteIndex(1);
        Assert.assertEquals(1, fuse.getSpriteIndex());
    }

    /**
     * Draw method is not testable, because all variables used are not accesible and there are no mockable objects.
     */
    /**
     * @throws Exception
     */
    @Test
    public void incrementSpriteIndex() throws Exception {
        Fuse spyFuse = spy(new Fuse(2, 2, 2, 2, stix));
        spyFuse.draw(new Canvas(1, 1).getGraphicsContext2D());
        verify(spyFuse).setSpriteIndex(anyInt());
    }

    /**
     * @throws Exception
     */
    @Test
    public void getSpriteImage() throws Exception {
        Fuse fuse = new Fuse(2, 2, 2, 2, stix);
        Image[] sprite = new Image[1];
        sprite[0] = new Image("/images/fuse-1.png");
        fuse.setSprite(sprite);
        Assert.assertArrayEquals(sprite, fuse.getSprite());
    }
}