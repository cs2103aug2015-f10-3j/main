package parser;

import input.Command;

public final class CommandParser {
	
	private static final String SPACES = "\\s+";

	private static enum COMMAND_TYPE {
		ADD, EDIT, VIEW,
		DELETE, UNDO, REDO, 
		INVALID, EXIT
	}
	
	public static Command tryParse(String userCommand) {
		// return null if fail to parse
		try {
			return parse(userCommand);
		}
		catch (Throwable e) {
			return null;
		}
	}
	
	private static Command parse(String userCommand) throws Exception {
		String[] delimCmdStr = breakDownCommand(userCommand);
		return createCommand(delimCmdStr);
	}
	
	private static String[] breakDownCommand(String userCommand) {
		return userCommand.split(SPACES);
	}
	
	private static createCommand(String[] delimCmdStr) {
		determineCommandType(delimCmdStr);
		return createCommandWithType(add);
	}
	
	private static COMMAND_TYPE determineCommandType()
}
