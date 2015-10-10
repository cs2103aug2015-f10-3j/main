package logic.api;

import java.util.ArrayList;

import command.api.Command;
import common.data.Pair;
import parser.api.CommandParser;
import task.entity.Task;

public class Executor {
	/*** Variable ***/
	private static Executor logicExecutor;
	private static CommandParser commandParser;
	
	/*** API ***/
	public static Pair<ArrayList<Task>,Boolean> processCommand(String userInput) {
		if (logicExecutor == null) {
			logicExecutor = new Executor();
		}
		return logicExecutor.parseCommand(userInput);
	}
	
	/*** Methods ***/
	public Executor() {
		commandParser = new CommandParser();
	}
	
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