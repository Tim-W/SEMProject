package nl.tudelft.sem.group2;

import nl.tudelft.sem.group2.level.Level;
import org.junit.Assert;
import org.junit.Test;

import static nl.tudelft.sem.group2.level.LevelFactory.createFromXml;

/**
 * Test for level factory.
 */
public class LevelFactoryTest {
    @Test
    public void testCreateFromXml() throws Exception {
        int id = 1;
        Level level = createFromXml(id, false);
        Assert.assertEquals(level.getLevelId(), id);
    }

    @Test
    public void testCreateFromXmlTwoPlayers() throws Exception {
        int id = 1;
        Level level = createFromXml(id, true);
        Assert.assertEquals(10, level.getPercentage());
    }

    @Test
    public void testCreateFromXmlNull() throws Exception {
        Level level = createFromXml(0, false);
        Assert.assertEquals(null, level);
    }

}