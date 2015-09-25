package logic;

import java.util.ArrayList;
import parser.Command;
import logic.data.Task;

public class Logic {
	
	private ArrayList<Command> commandHistory;
	private ArrayList<Command> undoHistory;
	
	public Logic() {
		
	}
	
	public static ArrayList<Task> processCommand(String userInput) {
		return null;
	}
	
	private ArrayList<Task> parseCommand(String userInput) {
		return null;
	}
	
	private ArrayList<Task> executeCommand(Command cmd) {
		return null;
	}
	
	private void appendToCommandHistory(Command cmd) {
		commandHistory.add(cmd);
	}
	
	private void appendToUndoHistory(Command cmd) {
		undoHistory.add(cmd);
	}
	
	private Command getMostRecentCommand() {
		return commandHistory.get(commandHistory.size()-1);
	}
}

class CommandResults {
	
}
