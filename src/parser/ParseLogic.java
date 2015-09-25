package parser;

import input.*;

public class ParseLogic {
	
	private static final String SPACES = "\\s+";

	private static enum COMMAND_TYPE {
		ADD, VIEW, EDIT, DELETE,
		SEARCH, UNDO, REDO, 
		INVALID, EXIT
	}

	private static enum COMMANDS {
		ADD("add"), VIEW("view"), EDIT("edit"), DELETE("delete"), 
		SEARCH("search"), UNDO("undo"), REDO("redo"),
		EXIT("exit");

		private final String _commandText;
		
		private COMMANDS(final String commandText) {
			this._commandText = commandText;
		}

		@Override
		public String toString() {
			return _commandText;
		}
	}

	public static Command createCommand(String userCommand) {
		COMMAND_TYPE commandType = determineCommandType(userCommand);
		switch (commandType) {
		case ADD:
			return null;
		case VIEW:
			return null;
		case EDIT:
			return null;
		case DELETE:
			return null;
		case SEARCH:
			return null;
		case UNDO:
			return null;
		case REDO:
			return null;
		case INVALID:
			return null;
		case EXIT:
			return new ExitCommand();
		default:
			return null;
		}
	}
	
	private static COMMAND_TYPE determineCommandType(String userCommand) {
		if (userCommand == null) {
			throw new Error();
		}
		String commandString = getFirstWord(userCommand);
		if (isStringEqual(commandString, COMMANDS.ADD.toString())) {
			return COMMAND_TYPE.ADD;
		}
		else if (isStringEqual(commandString, COMMANDS.VIEW.toString())) {
			return COMMAND_TYPE.VIEW;
		}
		else if (isStringEqual(commandString, COMMANDS.EDIT.toString())) {
			return COMMAND_TYPE.EDIT;
		}
		else if (isStringEqual(commandString, COMMANDS.DELETE.toString())) {
			return COMMAND_TYPE.DELETE;
		}
		else if (isStringEqual(commandString, COMMANDS.SEARCH.toString())) {
			return COMMAND_TYPE.SEARCH;
		}
		else if (isStringEqual(commandString, COMMANDS.UNDO.toString())) {
			return COMMAND_TYPE.UNDO;
		}
		else if (isStringEqual(commandString, COMMANDS.REDO.toString())) {
			return COMMAND_TYPE.REDO;
		}
		else if (isStringEqual(commandString, COMMANDS.EXIT.toString())) {
			return COMMAND_TYPE.EXIT;
		}
		else {
			return COMMAND_TYPE.INVALID;
		}
	}
	
	private static boolean isStringEqual(String s1, String s2) {
		return s1.equals(s2);
	}

	private static String getFirstWord(String userCommand) {
		return userCommand.split(SPACES)[0];
	}
}
