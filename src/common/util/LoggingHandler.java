package common.util;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingHandler {
	
	private static final Logger LOGGER = Logger.getLogger("");
	
	public void setupLoggingHandler() {
		try{
			LOGGER.removeHandler(LOGGER.getHandlers()[0]);
			//Creating consoleHandler and fileHandler
			Handler fileHandler  = new FileHandler("./paddleTask.log");
			
			LOGGER.addHandler(fileHandler);
			
			//Setting levels to handlers and LOGGER
			fileHandler.setLevel(Level.INFO);
			LOGGER.setLevel(Level.INFO);
			
			LOGGER.config("Configuration done.");
			
		}catch(IOException exception){
			LOGGER.log(Level.SEVERE, "Error occur in FileHandler.", exception);
		}
	}
}
