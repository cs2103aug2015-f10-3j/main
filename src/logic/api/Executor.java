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
	private static Executor thisInstance;
	private static CommandParser commandParser;
	private static Observer observer;
	private static ArrayList<Task> deliveredTaskState;

	/*** Constructor ***/
	public Executor() {
		LOGGER.info("Initialising Executor");
		commandParser = new CommandParser();
		deliveredTaskState = new ArrayList<Task>();
	}

	/*** API Methods ***/
	
	public static Executor getInstance(Observer mainObserver) {
		if (thisInstance == null) {
			observer = mainObserver;
			thisInstance = new Executor();
		}
		return thisInstance;
	}
	
	public ArrayList<Task> processCommand(String userInput) {
		return handleCommand(userInput);
	}

	private Command parseCommand(String userInput) throws InvalidCommandFormatException {
		// Userinput should not be null after UI sends it to Executor
		assert (userInput != null);
		return commandParser.parse(userInput);
	}
	
	private ArrayList<Task> executeCommand(Command cmd) throws Exception {
		cmd.addObserver(observer);
		return cmd.execute();
	}
	
	// For running index || Get Task ID 
	public int getTaskIdWithStateIndex(int stateIndex) {
		return deliveredTaskState.get(stateIndex).getTaskId();
	}
	
	/*** Helper Methods ***/

	private ArrayList<Task> handleCommand(String userInput) {
		try {
			Command cmd = parseCommand(userInput);
			deliveredTaskState = executeCommand(cmd);
		}	catch (Exception e) {
			setChanged();
			notifyObservers(e.getMessage());
			return null;
		}
		return deliveredTaskState;
	}
}