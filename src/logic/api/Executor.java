package logic.api;

import java.util.ArrayList;
import java.util.logging.Logger;

import command.api.Command;
import common.data.Pair;
import common.exception.InvalidCommandFormatException;
import parser.api.CommandParser;
import task.entity.Task;
import ui.view.Observer;

public class Executor {
	/*** Variable ***/
	private static final Logger LOGGER = Logger.getLogger(Executor.class.getName());
	private static Executor logicExecutor;
	private static CommandParser commandParser;
	private static Observer mainCommandLinePanel;
	
	/*** API ***/
	public Executor() {
		LOGGER.info("Initialising Executor\n");
	}
	
	public static Pair<ArrayList<Task>,Boolean> processCommand(Observer panel, String userInput) {
		if (logicExecutor == null) {
			logicExecutor = new Executor();
		}
		mainCommandLinePanel = panel;
		commandParser = new CommandParser(panel);
		return logicExecutor.parseCommand(userInput);
	}
	
	/*** Methods 
	 * @throws InvalidCommandFormatException ***/
	private Pair<ArrayList<Task>,Boolean> parseCommand(String userInput) {
		assert (userInput != null);
		
		try {
			Command cmd = commandParser.tryParse(userInput);
			return executeCommand(cmd);
		}
		catch (InvalidCommandFormatException e) {
			mainCommandLinePanel.print(e.getMessage());
			return null;
		}
	}
	
	private Pair<ArrayList<Task>,Boolean> executeCommand(Command cmd) {
		try {
			return cmd.execute();
		}
		catch (Exception e) {
			mainCommandLinePanel.print(e.getMessage());
			return null;
		}
	}
}