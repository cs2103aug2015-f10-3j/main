package parser;

import input.Command;

public final class CommandParser {

	public static Command tryParse(String userCommand) {
		try {
			return parse(userCommand);
		}
		catch (Throwable e) {
			return null;
		}
	}

	private static Command parse(String userCommand) throws Exception {
		userCommand = userCommand.trim();
		if (userCommand.length() <= 0) {
			return null;
		}
		return ParseLogic.createCommand(userCommand);
	}
}
