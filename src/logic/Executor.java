package logic;

import java.util.ArrayList;
import input.Command;
import logic.data.Task;
import parser.CommandParser;

public class Executor {
	/*** Variables ***/
	
	private static Executor logicController;
	
	/**
     * This method calls the CommandParser API to parse the userInput
     * 
     * @param		userInput  the String specified by the user
     * @return		an ArrayList of Task object
     */
	public static ArrayList<Task> processCommand(String userInput) {
		if (logicController == null) {
			logicController = new Executor();
		} 
		return logicController.parseCommand(userInput);
	}
	
	private ArrayList<Task> parseCommand(String userInput) {
		Command cmd = CommandParser.tryParse(userInput);
		return executeCommand(cmd);
	}
	
	private ArrayList<Task> executeCommand(Command cmd) {
		return cmd.execute();
	}
}