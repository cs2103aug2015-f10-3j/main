package parser.api;

import java.util.List;

import command.api.Command;
import parser.logic.ParseLogic;

public final class CommandParser {

	private ParseLogic parserLogic;
	
	public CommandParser() {
		parserLogic = new ParseLogic();
	}
	
	public Command tryParse(String userCommand) {
		try {
			return parse(userCommand);
		}
		catch (Throwable e) {
			return null;
		}
	}

	private Command parse(String userCommand) throws Exception {
		userCommand = userCommand.trim();
		if (userCommand.length() <= 0) {
			return null;
		}
		return createCommand(userCommand);
	}
	
	private Command createCommand(String userCommand) throws Exception {
		ParseLogic.COMMAND_TYPE commandType = parserLogic.determineCommandType(userCommand);
		Command newCommand = parserLogic.createCommand(commandType);
		addOptions(newCommand, userCommand);
		addCommandToList(newCommand, commandType);
		return newCommand;
	}
	
	private void addOptions(Command newCommand, String userCommand) throws Exception {
		List<String> commandList = parserLogic.breakDownCommand(userCommand);
		parserLogic.addOptionsToCommand(newCommand, commandList);
	}
	
	private void addCommandToList(Command newCommand, ParseLogic.COMMAND_TYPE commandType) {
		if (!parserLogic.isInvalidTypeToAdd(commandType)) {
			Command.getCommandList().add(newCommand);
		}
	}
}
