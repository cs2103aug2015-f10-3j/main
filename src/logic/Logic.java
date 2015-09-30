package logic;

import java.util.ArrayList;
import input.Command;
import logic.data.Task;
import parser.CommandParser;

public class Logic {
	/*** Variables ***/
	
	private static Logic logicController;
	private ArrayList<Command> commandHistory;
	private ArrayList<Command> undoHistory;
	
	/**
     * This method calls the CommandParser API to parse the userInput
     * 
     * @param		userInput  the String specified by the user
     * @return		an ArrayList of Task object
     */
	public static ArrayList<Task> processCommand(String userInput) {
		if (logicController == null) {
			logicController = new Logic();
		} 
		return logicController.parseCommand(userInput);
	}
	
	private ArrayList<Task> parseCommand(String userInput) {
		Command cmd = CommandParser.tryParse(userInput);
		return executeCommand(cmd);
	}
	
	/**
     * <description>
     * 
     * @param
     * @return
     */
	private ArrayList<Task> executeCommand(Command cmd) {
		appendToCommandHistory(cmd);
		return cmd.execute();
	}
	
	/**
     * <description>
     * 
     * @param
     * @return
     */
	private void appendToCommandHistory(Command cmd) {
		commandHistory.add(cmd);
	}
	
	/**
     * <description>
     * 
     * @param
     * @return
     */
	private void appendToUndoHistory(Command cmd) {
		undoHistory.add(cmd);
	}
	
	/**
     * <description>
     * 
     * @param
     * @return
     */
	private Command getMostRecentCommand() {
		return commandHistory.get(commandHistory.size()-1);
	}
}