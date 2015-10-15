package parser.api;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import command.api.Command;
import common.exception.InvalidCommandFormatException;
import parser.logic.ParseLogic;
import ui.view.Observer;

public final class CommandParser {

	private ParseLogic parserLogic;
	private Observer panel;
	private static final Logger LOGGER = Logger.getLogger(CommandParser.class.getName());
	
	private CommandParser() {
		LOGGER.info("Initiating CommandParser\n");
		parserLogic = new ParseLogic();
	}
	
	public CommandParser(Observer panel) {
		this();
		assert(panel != null);
		this.panel = panel;
	}
	
	public Command tryParse(String userCommand) throws InvalidCommandFormatException {
		try {
			return parse(userCommand);
		}
		catch (Throwable e) {
			LOGGER.log(Level.SEVERE, "Parsing of user command -> {0} failed", userCommand);
			throw new InvalidCommandFormatException("User input supplied was in an invalid format");
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
