package nl.tudelft.sem.group2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

/**
 * Logger class that prints output to the console.
 *
 * @author Dennis
 */
public final class Logger {

    private Level level;
    private static volatile Logger logger;

    /**
     * Constructor for the logger.
     * Since it is a singleton class constructor needs to be private.
     */
    private Logger() {
        level = Level.INFO;
    }

    /**
     * Getter for the logger this is a singleton class so everywhere the logger is used it is the same instance
     * This method allows getting of that instance and instantiates it when it is not instantiated yet.
     * @return the only one instance of Logger.
     */
    public static Logger getLogger() {
        if (logger == null) {
            // Put lock on class since it we do not want to instantiate it twice
            synchronized (Logger.class) {
                // Check if logger is in the meanwhile not already instantiated.
                if (logger == null) {
                    logger = new Logger();
                }
            }
        }
        return logger;
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
