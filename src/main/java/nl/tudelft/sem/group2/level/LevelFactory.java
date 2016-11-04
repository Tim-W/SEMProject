package nl.tudelft.sem.group2.level;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Class creates levels of xml files.
 */
public class LevelFactory {
    /**
     * Basic contructor for collision handler class.
     */
    public LevelFactory() {
    }

    /**
     * Method that parses a XML-file into a level.
     *
     * @param levelNumber - number of the level.
     * @param twoPlayer true if game is multiplayer
     * @return - returns a level.
     */
    public Level createFromXml(int levelNumber, boolean twoPlayer)
            throws IOException, ParserConfigurationException, SAXException {
        String player = "singlePlayer";
        if (twoPlayer) {
            player = "twoPlayers";
        }
        String xmlPath = "/levels/" + player + "/level" + levelNumber + ".xml";
        Document xml = getFileReader(xmlPath);
        Level level = null;
        if (xml != null) {
            int percentage = Integer.parseInt(xml.getElementsByTagName("percentage").item(0).getTextContent());
            int id = Integer.parseInt(xml.getElementsByTagName("id").item(0).getTextContent());
            int qixSize = Integer.parseInt(xml.getElementsByTagName("qixSize").item(0).getTextContent());
            level = new Level(percentage, id, qixSize);
        }
        return level;
    }

    /**
     * Method to create a FileReader to read the XML-file.
     *
     * @param xmlFile - Path/name of the XML-file to be parsed.
     * @return - returns a new FileReader if no exception is thrown, else it will return null.
     */
    private Document getFileReader(String xmlFile)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        try (InputStream in = Level.class.getResourceAsStream(xmlFile)) {
            return documentBuilder.parse(in);
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}
