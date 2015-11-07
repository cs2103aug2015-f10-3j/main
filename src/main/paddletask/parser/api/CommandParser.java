//@@author A0125473H
package main.paddletask.parser.api;

import java.util.logging.Level;
import java.util.logging.Logger;

import main.paddletask.command.api.Command;
import main.paddletask.common.exception.InvalidCommandFormatException;
import main.paddletask.parser.logic.ParseLogic;

public final class CommandParser {

	/*** Variables ***/
	private ParseLogic _parserLogic = new ParseLogic();
	private static final Logger LOGGER = Logger.getLogger(CommandParser.class.getName());

	/*** Constructor ***/
	public CommandParser() {
		LOGGER.info("Initiating CommandParser");
	}

	/*** Methods ***/
	public Command parse(String userCommand) throws InvalidCommandFormatException {
		assert(userCommand != null);
		checkUserCommand(userCommand);
		try {
			return createCommand(userCommand);
		} catch (InvalidCommandFormatException e) {
			throw e;
		} catch (Throwable e) {
			String message = String.format("Failed to parse user input: %1$s", userCommand);
			LOGGER.log(Level.SEVERE, message, e);
			throw new InvalidCommandFormatException("User input supplied was in an invalid format");
		}
	}
	
	//Overloaded parse for commands that require running index
	public Command parse(String userCommand, int[] stateArray) throws InvalidCommandFormatException {
		assert(userCommand != null && stateArray != null);
		checkUserCommand(userCommand);
		try {
			userCommand = _parserLogic.replaceRunningIndex(userCommand,stateArray);
			return parse(userCommand);
		} catch (InvalidCommandFormatException e) {
			throw e;
		} catch (Throwable e) {
			String message = String.format("Failed to parse user input: %1$s with running index", userCommand);
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

	/**
	 * This method is invoked by logic to check if a command is stateful
	 * 
	 * @return <code>true</code> if command is stateful
	 * 		   <code>false</code> otherwise
	 */
	public boolean isStatefulCommand(String userCommand) throws InvalidCommandFormatException {
		checkUserCommand(userCommand);
		return _parserLogic.isStatefulCommand(userCommand);
	}

	/**
	 * This method is invoked by logic to check if a command requires
	 * a save state
	 * 
	 * @return <code>true</code> if command requires save state
	 * 		   <code>false</code> otherwise
	 */
	public boolean isSaveStateCommand(String userCommand) throws InvalidCommandFormatException {
		checkUserCommand(userCommand);
		return _parserLogic.isSaveStateCommand(userCommand);
	}

	private Command createCommand(String userCommand) throws Exception {
		assert(userCommand != null && _parserLogic != null);
		Command newCommand = _parserLogic.createCommand(userCommand);
		if (newCommand == null) {
			throw new InvalidCommandFormatException("Unable to create command with specified user input");
		}
		return newCommand;
	}
}
