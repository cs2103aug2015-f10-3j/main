package logic;

import java.util.ArrayList;

import logic.data.Task;

public class LogicAPI {
	static Logic logic = new Logic();
	
	public static ArrayList<Task> processCommand(String userInput) {
		return logic.parseCommand(userInput);
	}	
}
