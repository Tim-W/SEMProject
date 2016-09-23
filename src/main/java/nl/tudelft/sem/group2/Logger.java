package nl.tudelft.sem.group2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

/**
 * Logger class that prints output to the console.
 *
 * @author Dennis
 */
public class Logger {

    private Level level;

    /**
     * Constructor for the logger.
     */
    public Logger() {
        level = Level.INFO;
    }

    /**
     * Logs output to the console depending on logLevel.
     *
     * @param logLevel the level of precision a log has.
     * @param message  the message of the log.
     * @param logClass the class calling the log.
     */
    public void log(Level logLevel, String message, Class<?> logClass) {
        if (level.intValue() <= logLevel.intValue()) {
            String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
            System.err.println("[" + logLevel.toString() + "] "
                    + time + " " + logClass.getName() + "\n" + "\t" + message);
        }
    }

    /**
     * Sets the log level of the Logger.
     *
     * @param level the new log level.
     */
    public void setLevel(Level level) {
        this.level = level;
    }
}
