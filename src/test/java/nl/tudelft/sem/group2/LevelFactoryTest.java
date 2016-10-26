package nl.tudelft.sem.group2;

import nl.tudelft.sem.group2.level.Level;
import org.junit.Assert;
import org.junit.Test;

import static nl.tudelft.sem.group2.level.LevelFactory.createFromXml;

/**
 * Created by gijs on 26-10-2016.
 */
public class LevelFactoryTest {
    @Test
    public void testCreateFromXml() throws Exception {
        int id = 1;
        Level level = createFromXml(id);
        Assert.assertEquals(level.getID(), id);
    }

}