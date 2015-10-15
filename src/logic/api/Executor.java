package logic.api;

import java.util.ArrayList;

import command.api.Command;
import common.data.Pair;
import parser.api.CommandParser;
import task.entity.Task;
import ui.view.CommandLinePanel;

public class Executor {
	/*** Variable ***/
	private static Executor logicExecutor;
	private static CommandParser commandParser;
	private static CommandLinePanel mainCommandLinePanel;
	/*** API ***/
	public static Pair<ArrayList<Task>,Boolean> processCommand(CommandLinePanel panel, String userInput) {
		if (logicExecutor == null) {
			logicExecutor = new Executor();
		}
		mainCommandLinePanel = panel;
		commandParser = new CommandParser(panel);
		return logicExecutor.parseCommand(userInput);
	}
	
	/*** Methods ***/
	
	private Pair<ArrayList<Task>,Boolean> parseCommand(String userInput) {
		Command cmd = commandParser.tryParse(userInput);
		if (cmd == null) {
			return new Pair<ArrayList<Task>,Boolean>(null, false);
		} else {
			return executeCommand(cmd);
		}
	}
	
	private Pair<ArrayList<Task>,Boolean> executeCommand(Command cmd) {
		return cmd.execute();
	}
}