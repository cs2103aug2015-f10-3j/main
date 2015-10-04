package logic;

import java.util.ArrayList;
import input.Command;
import logic.data.Task;
import util.Pair;
import parser.CommandParser;

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
			return null;
		} else {
			return executeCommand(cmd);
		}
	}
	
	private Pair<ArrayList<Task>,Boolean> executeCommand(Command cmd) {
		return cmd.execute();
	}
}