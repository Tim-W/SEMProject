package nl.tudelft.sem.group2;

import nl.tudelft.sem.group2.level.Level;
import nl.tudelft.sem.group2.level.LevelFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Test for level factory.
 */
public class LevelFactoryTest {
    private LevelFactory levelFactory;

    @Before
    public void setUp() throws Exception {
        levelFactory = new LevelFactory();
    }

    @Test
    public void testCreateFromXml() throws Exception {
        int id = 1;
        Level level = levelFactory.createFromXml(id, false);
        Assert.assertEquals(level.getLevelId(), id);
    }

    @Test
    public void testCreateFromXmlTwoPlayers() throws Exception {
        int id = 1;
        Level level = levelFactory.createFromXml(id, true);
        Assert.assertEquals(10, level.getPercentage());
    }

    @Test
    public void testCreateFromXmlNull() throws Exception {
        Level level = levelFactory.createFromXml(0, false);
        Assert.assertEquals(null, level);
    }
}