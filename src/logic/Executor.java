package logic;

import java.util.ArrayList;
import input.Command;
import logic.data.Task;
import parser.CommandParser;

public class Executor {
	/*** Variable ***/
	private static Executor logicExecutor;
	
	/*** API ***/
	public static ArrayList<Task> processCommand(String userInput) {
		if (logicExecutor == null) {
			logicExecutor = new Executor();
		} 
		return logicExecutor.parseCommand(userInput);
	}
	
	/*** Methods ***/
	private ArrayList<Task> parseCommand(String userInput) {
		Command cmd = CommandParser.tryParse(userInput);
		return executeCommand(cmd);
	}
	
	private ArrayList<Task> executeCommand(Command cmd) {
		return cmd.execute();
	}
}