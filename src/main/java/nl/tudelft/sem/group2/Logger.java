package nl.tudelft.sem.group2;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.logging.Level;

public class Logger {

	private Level level;

	public Logger() {
		level = Level.INFO;
	}

	public void log(Level logLevel, String message, Class<?> logClass) {
		if (level.intValue() <= logLevel.intValue()) {
			String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
			System.err.println("[" + logLevel.toString() + "] " + time + " " + logClass.getName() + "\n" + "\t" + message);
		}
	}

	public void setLevel(Level level) {
		this.level = level;
	}
}
