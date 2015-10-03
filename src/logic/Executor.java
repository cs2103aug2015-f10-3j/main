package logic;

import java.util.ArrayList;
import input.Command;
import logic.data.Task;
import util.Pair;
import parser.CommandParser;

public class Executor {
	/*** Variable ***/
	private static Executor logicExecutor;
	
	/*** API ***/
	public static Pair<ArrayList<Task>,Boolean> processCommand(String userInput) {
		if (logicExecutor == null) {
			logicExecutor = new Executor();
		} 
		return logicExecutor.parseCommand(userInput);
	}
	
	/*** Methods ***/
	private Pair<ArrayList<Task>,Boolean> parseCommand(String userInput) {
		Command cmd = CommandParser.tryParse(userInput);
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