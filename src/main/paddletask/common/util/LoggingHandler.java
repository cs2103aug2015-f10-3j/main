//@@author A0125473H
package main.paddletask.common.util;

import java.util.logging.Logger;

public class LoggingHandler {
	
	/*** Variables ***/
	private static final String EMPTY = "";
	
	private static final Logger LOGGER = Logger.getLogger(EMPTY);

	/*** Methods ***/
	public void setupLoggingHandler() {
		//suppress useless logs to console
		supressConsoleOutput();
	}

	private void supressConsoleOutput() {
		LOGGER.removeHandler(LOGGER.getHandlers()[0]);
	}
}
