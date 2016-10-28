package nl.tudelft.sem.group2.level;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Class creates levels of xml files.
 */
public class LevelHandler {
    private int levelID;
    private Level level;
    /**
     * Basic contructor for collision handler class.
     */
    public LevelHandler() {
        levelID = 0;
    }

    /**
     * iterates to next level.
     * @param twoPlayer true if game is multiplayer
     */
    public void nextLevel(boolean twoPlayer) {
        levelID++;
        try {
            level = LevelFactory.createFromXml(levelID, twoPlayer);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }

    /**
     * gets the id of currentLevel.
     *
     * @return int id of level
     */
    public int getLevelID() {
        return levelID;
    }

    /**
     * get the current level.
     *
     * @return currentLevel
     */
    public Level getLevel() {
        return level;
    }

}
