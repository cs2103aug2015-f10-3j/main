package Logic;

public class Logic {
	private static LogicController mainController = new LogicController();
	
	public static Command receiveFromGui(String userInput) {
		return mainController.parseCommand(userInput);
	}
}
