package Logic;

public class LogicController {
	private static final String MESSAGE_UNRECOGNIZED_COMMAND = "";
	
	LogicExecutor mainExecutor;
	
	public LogicController() {
		mainExecutor = new LogicExecutor();
	}
	
	public CommandResults parseCommand(String userInput) {
		// Joel's API that returns a Command object
		CommandStub command = ParserStub.tryParse(userInput);
		return executeCommand(command);
	}
	
	public CommandResults executeCommand(CommandStub command) {
		switch (command.getCommandType()) {
			case ADD :
				return mainExecutor.handleAdd(command);
			case EDIT :
				return mainExecutor.handleEdit(command);
			case VIEW :
				return mainExecutor.handleView(command);
			case DELETE :
				return mainExecutor.handleDelete(command);
			case INVALID:
				return mainExecutor.handleInvalid(command);
			default :
				throw new Error(MESSAGE_UNRECOGNIZED_COMMAND);
		}
	}
}

class CommandResults {
	
}