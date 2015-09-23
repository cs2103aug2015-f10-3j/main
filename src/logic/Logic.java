package logic;

public class Logic {
	private static LogicController mainController = new LogicController();
	
	public static CommandResults processCommand(String userInput) {
		return mainController.parseCommand(userInput);
	}
}
