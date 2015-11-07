//@@author A0125473H
package main.paddletask.common.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingHandler {
	
	/*** Variables ***/
	private static final String EMPTY = "";
	private static final String LOG_NAME = "./Logs/paddleTask.log";
	private static final String CONFIG_MESSAGE = "Configuration done";
	private static final String ERROR_MESSAGE = "Error occur in FileHandler";
	
	private static final Logger LOGGER = Logger.getLogger(EMPTY);

	/*** Methods ***/
	public void setupLoggingHandler() {
		try{
			supressConsoleOutput();
			Handler fileHandler = createLogFile();
			setLoggingLevel(fileHandler);
			LOGGER.config(CONFIG_MESSAGE);
		}catch(IOException exception){
			LOGGER.log(Level.SEVERE, ERROR_MESSAGE, exception);
		}
	}

	private void supressConsoleOutput() {
		LOGGER.removeHandler(LOGGER.getHandlers()[0]);
	}

	private void setLoggingLevel(Handler fileHandler) {
		fileHandler.setLevel(Level.INFO);
		LOGGER.setLevel(Level.INFO);
	}

	private Handler createLogFile() throws IOException {
		Handler fileHandler  = new FileHandler(LOG_NAME);
		LOGGER.addHandler(fileHandler);
		return fileHandler;
	}
}
