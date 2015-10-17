package logic.api;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Logger;

import command.api.Command;
import common.exception.InvalidCommandFormatException;
import parser.api.CommandParser;
import task.entity.Task;

public class Executor extends Observable {
	/*** Variable ***/
	private static final Logger LOGGER = Logger.getLogger(Executor.class.getName());
	private static Executor logicExecutor;
	private static CommandParser commandParser;
	private static Observer mainObserver;
	
	/*** API ***/
	public Executor() {
		LOGGER.info("Initialising Executor\n");
		commandParser = new CommandParser();
	}
	
	public ArrayList<Task> processCommand(Observer observer, String userInput) {
		if (logicExecutor == null) {
			logicExecutor = new Executor();
		}
		mainObserver = observer;
		return logicExecutor.parseCommand(userInput);
	}
	
	/*** Methods 
	 * @throws InvalidCommandFormatException ***/
	private ArrayList<Task> parseCommand(String userInput) {
		assert (userInput != null);
		
		try {
			Command cmd = commandParser.parse(userInput);
			ArrayList<Task> executionResult = executeCommand(cmd);
			cmd.addObserver(mainObserver);
			return executionResult;
		}
		catch (InvalidCommandFormatException e) {
			notifyObservers(e.getMessage());
			//mainCommandLinePanel.print(e.getMessage());
			return null;
		}
	}
	
	private ArrayList<Task> executeCommand(Command cmd) {
		try {
			return cmd.execute();
		}
		catch (Exception e) {
			notifyObservers(e.getMessage());
			//mainCommandLinePanel.print(e.getMessage());
			return null;
		}
	}
}