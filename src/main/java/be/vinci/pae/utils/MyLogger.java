package be.vinci.pae.utils;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {

  private static Logger logger = Logger.getLogger("log.txt");

  /**
   * initialize the logger with the complete date written in URLLOG file.
   */
  public static void init() {
    FileHandler fh;
    try {
      fh = new FileHandler(Config.getProperty("URLLOG"));

      logger.addHandler(fh);
      SimpleFormatter formatter = new SimpleFormatter();
      fh.setFormatter(formatter);

      logger.info("Logger Initialized");

    } catch (Exception e) {
      logger.log(Level.WARNING, "Exception ::", e);
    }
  }

  /**
   * add a logger to a specified method that return an exception.
   *
   * @param message String
   * @param e       Exception
   * @param level   Level
   */
  public static void addLogger(String message, Exception e, Level level) {
    logger.log(level, "Exception :: " + message, e);
  }

  /**
   * add a logger to a specified method.
   *
   * @param message String
   * @param level   Level
   */
  public static void addLogger(String message, Level level) {
    logger.log(level, message);
  }

}
