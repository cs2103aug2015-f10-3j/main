package logic.api;

import java.util.ArrayList;

import command.api.Command;
import common.data.Pair;
import common.exception.InvalidCommandFormatException;
import parser.api.CommandParser;
import task.entity.Task;
import ui.view.Observer;

public class Executor {
	/*** Variable ***/
	private static Executor logicExecutor;
	private static CommandParser commandParser;
	private static Observer mainCommandLinePanel;
	/*** API ***/
	public static Pair<ArrayList<Task>,Boolean> processCommand(Observer panel, String userInput) {
		if (logicExecutor == null) {
			logicExecutor = new Executor();
		}
		mainCommandLinePanel = panel;
		commandParser = new CommandParser(panel);
		return logicExecutor.parseCommand(userInput);
	}
	
	/*** Methods ***/
	
	private Pair<ArrayList<Task>,Boolean> parseCommand(String userInput) {
		Command cmd;
		try {
			cmd = commandParser.parse(userInput);
			assert(cmd != null);
			return executeCommand(cmd);
		} catch (InvalidCommandFormatException e) {
			return new Pair<ArrayList<Task>,Boolean>(null, false);
		}
	}
	
	private Pair<ArrayList<Task>,Boolean> executeCommand(Command cmd) {
		return cmd.execute();
	}
}