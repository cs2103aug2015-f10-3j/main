package logic.api;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import command.api.Command;
import common.exception.InvalidCommandFormatException;
import common.exception.NoTaskStateException;
import parser.api.CommandParser;
import task.entity.Task;

public class LogicController extends Observable {
	/*** Variable ***/
	private static final Logger LOGGER = Logger.getLogger(LogicController.class.getName());
	private static LogicController thisInstance;
	private static CommandParser commandParser;
	private static Observer observer;
	private static ArrayList<Task> deliveredTaskState;

	/*** Constructor ***/
	public LogicController() {
		LOGGER.info("Initialising Executor");
		commandParser = new CommandParser();
		deliveredTaskState = new ArrayList<Task>();
	}

	/*** Methods ***/

	public static LogicController getInstance(Observer mainObserver) {
		if (thisInstance == null) {
			observer = mainObserver;
			thisInstance = new LogicController();
		}
		return thisInstance;
	}

	public ArrayList<Task> processCommand(String userInput) {
		return handleCommand(userInput);
	}

	private Command parseCommand(String userInput) throws Exception {
		// Userinput should not be null after UI sends it to Executor
		assert (userInput != null);
		Command cmd;
		if (commandParser.isStatefulCommand(userInput)) {
			if (deliveredTaskState.isEmpty()) {
				LOGGER.log(Level.SEVERE, "LogicController:parseCommand(): Attempt to read state when there are no tasks");
				throw new NoTaskStateException("Please perform a view to retrieve a list of task so that you can perform this command!");
			} else {
				cmd = commandParser.parse(userInput, getStateTaskId());
			}
		}  else { 
			cmd = commandParser.parse(userInput);
		}
		
		return cmd;
	}

	private ArrayList<Task> executeCommand(Command command, String userInput) throws Exception {
		command.addObserver(observer);
		ArrayList<Task> executionResult = new ArrayList<Task>();
		if (commandParser.isSaveStateCommand(userInput)) {
			executionResult = command.execute();
			deliveredTaskState = executionResult;
		} else {
			executionResult = command.execute();
		}
		return executionResult;
	}

	private ArrayList<Task> handleCommand(String userInput) {
		ArrayList<Task> executionResult;
		Command command;
		
		try {
			command = parseCommand(userInput);
			executionResult = executeCommand(command,userInput);
		}	catch (Exception e) {
			setChanged();
			notifyObservers(e.getMessage());
			return null;
		}
		return executionResult;
	}
	
	private int[] getStateTaskId() {
		int numId = deliveredTaskState.size();
		int[] stateIndexes = new int[numId];
		for (int i=0; i<numId; i++) {
			stateIndexes[i] = deliveredTaskState.get(i).getTaskId();
		}
		return stateIndexes;
	}
}