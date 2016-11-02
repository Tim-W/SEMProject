package nl.tudelft.sem.group2.units;

import javafx.embed.swing.JFXPanel;
import javafx.scene.canvas.Canvas;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.Point;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by gijs on 2-11-2016.
 */
public class StixTest {
    private Stix stix;
    private Fuse fuse;

    @BeforeClass
    public static void BeforeClass() {
        new JFXPanel();
    }

    @Before
    public void setUp() throws Exception {
        stix = new Stix();
        fuse = mock(Fuse.class);
    }

    @Test
    public void testDraw() throws Exception {
        stix.addToStix(new Point(1, 1));
        stix.draw(new Canvas(1, 1).getGraphicsContext2D(), fuse, false);
        verify(fuse, times(1)).onPoint(any());
    }

    @Test
    public void testDrawTwoStixNotOnPoint() throws Exception {
        stix.addToStix(new Point(1, 1));
        stix.addToStix(new Point(1, 2));
        when(fuse.onPoint(any())).thenReturn(false);
        stix.draw(new Canvas(1, 1).getGraphicsContext2D(), fuse, false);
        verify(fuse, times(2)).onPoint(any());
    }

    @Test
    public void testDrawStixOnPoint() throws Exception {
        stix.addToStix(new Point(1, 1));
        stix.addToStix(new Point(1, 2));
        when(fuse.onPoint(any())).thenReturn(true);
        stix.draw(new Canvas(1, 1).getGraphicsContext2D(), fuse, false);
        verify(fuse, times(1)).onPoint(any());
    }

}