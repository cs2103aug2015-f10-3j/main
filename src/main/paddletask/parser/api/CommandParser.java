//@@A0125473H
package main.paddletask.parser.api;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.paddletask.command.api.Command;
import main.paddletask.common.data.ParserConstants.COMMAND_TYPE;
import main.paddletask.common.exception.InvalidCommandFormatException;
import main.paddletask.parser.logic.ParseLogic;

public final class CommandParser {

	private ParseLogic parserLogic;

	private static final Logger LOGGER = Logger.getLogger(CommandParser.class.getName());

	public CommandParser() {
		LOGGER.info("Initiating CommandParser");
		parserLogic = new ParseLogic();
	}

	public Command parse(String userCommand, int[] stateArray) throws InvalidCommandFormatException {
		assert(userCommand != null && stateArray != null);
		checkUserCommand(userCommand);
		try {
			userCommand = parserLogic.replaceRunningIndex(userCommand,stateArray);
			return parse(userCommand);
		} catch (Throwable e) {
			String message = String.format("Failed to parse user input: %1$s", userCommand);
			LOGGER.log(Level.SEVERE, message, e);
			throw new InvalidCommandFormatException("User input supplied was in an invalid format");
		}
	}

	public Command parse(String userCommand) throws InvalidCommandFormatException {
		assert(userCommand != null);
		checkUserCommand(userCommand);
		try {
			return createCommand(userCommand);
		} catch (Throwable e) {
			String message = String.format("Failed to parse user input: %1$s", userCommand);
			LOGGER.log(Level.SEVERE, message, e);
			throw new InvalidCommandFormatException("User input supplied was in an invalid format");
		}
	}
	
	private void checkUserCommand(String userCommand) throws InvalidCommandFormatException {
		userCommand = userCommand.trim();
		if (userCommand.length() <= 0) {
			LOGGER.severe("User input string is of 0 length");
			throw new InvalidCommandFormatException("User input is blank");
		}
	}

	public boolean isStatefulCommand(String userCommand) throws InvalidCommandFormatException {
		checkUserCommand(userCommand);
		ParseLogic.COMMAND_TYPE commandType = parserLogic.determineCommandType(userCommand);
		return parserLogic.isStatefulCommand(commandType);
	}

	public boolean isSaveStateCommand(String userCommand) throws InvalidCommandFormatException {
		checkUserCommand(userCommand);
		ParseLogic.COMMAND_TYPE commandType = parserLogic.determineCommandType(userCommand);
		return parserLogic.isSaveStateCommand(commandType);
	}

	private Command createCommand(String userCommand) throws Exception {
		assert(userCommand != null && parserLogic != null);
		ParseLogic.COMMAND_TYPE commandType = parserLogic.determineCommandType(userCommand);
		Command newCommand = parserLogic.createCommand(commandType);
		if (newCommand == null) {
			throw new InvalidCommandFormatException("Unable to create command with specified user input");
		}
		addOptions(commandType, newCommand, userCommand);
		addCommandToList(newCommand, commandType);
		return newCommand;
	}

	private void addOptions(ParseLogic.COMMAND_TYPE commandType, Command newCommand, String userCommand) throws Exception {
		assert(newCommand != null && userCommand != null && parserLogic != null);
		List<String> commandList = parserLogic.breakDownCommand(userCommand);
		if (commandType == COMMAND_TYPE.SEARCH) {
			parserLogic.addPossibleDates(newCommand, commandList);
		} else if (commandType == COMMAND_TYPE.TAG || commandType == COMMAND_TYPE.UNTAG) {
			parserLogic.addTags(newCommand, commandList);
		}
		parserLogic.addOptionsToCommand(commandType, newCommand, commandList);
	}

	private void addCommandToList(Command newCommand, ParseLogic.COMMAND_TYPE commandType) {
		assert(newCommand != null && parserLogic != null);
		if (!parserLogic.isInvalidTypeToAdd(commandType)) {
			Command.getCommandList().add(newCommand);
		}
	}
}
