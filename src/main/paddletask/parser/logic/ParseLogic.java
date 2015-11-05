//@@A0125473H
package main.paddletask.parser.logic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.logging.Level;

import main.paddletask.command.api.*;
import main.paddletask.command.data.Option;
import main.paddletask.common.exception.InvalidCommandFormatException;
import main.paddletask.common.util.DateTimeHelper;

public class ParseLogic extends ParserBackend {
	
	/*** Constructor ***/
	public ParseLogic() {
		LOGGER.info("Initiating ParseLogic");
	}

	/*** Methods ***/
	public COMMAND_TYPE determineCommandType(String userCommand) {
		LOGGER.log(Level.INFO, "Attempt to determine command type from user input: {0}", userCommand);
		assert(userCommand != null);
		String mainCommand = getMainCommand(userCommand).toLowerCase();
		if (mainCommand.equalsIgnoreCase(COMMANDS.ADD.toString()) 
			|| mainCommand.equalsIgnoreCase(COMMANDS.ADD_SHORT.toString())) {
			return COMMAND_TYPE.ADD;
		}
		else if (mainCommand.equalsIgnoreCase(COMMANDS.VIEW.toString())
				|| mainCommand.equalsIgnoreCase(COMMANDS.VIEW_SHORT.toString())) {
			return COMMAND_TYPE.VIEW;
		}
		else if (mainCommand.equalsIgnoreCase(COMMANDS.EDIT.toString())
				|| mainCommand.equalsIgnoreCase(COMMANDS.EDIT_SHORT.toString())) {
			return COMMAND_TYPE.EDIT;
		}
		else if (mainCommand.equalsIgnoreCase(COMMANDS.DELETE.toString())
				|| mainCommand.equalsIgnoreCase(COMMANDS.DELETE_SHORT.toString())) {
			return COMMAND_TYPE.DELETE;
		}
		else if (mainCommand.equalsIgnoreCase(COMMANDS.COMPLETE.toString())
				|| mainCommand.equalsIgnoreCase(COMMANDS.COMPLETE_SHORT.toString())) {
            return COMMAND_TYPE.COMPLETE;
        }
		else if (mainCommand.equalsIgnoreCase(COMMANDS.SEARCH.toString())
				|| mainCommand.equalsIgnoreCase(COMMANDS.SEARCH_SHORT.toString())) {
			return COMMAND_TYPE.SEARCH;
		}
		else if (mainCommand.equalsIgnoreCase(COMMANDS.UNDO.toString())
				|| mainCommand.equalsIgnoreCase(COMMANDS.UNDO_SHORT.toString())) {
			return COMMAND_TYPE.UNDO;
		}
		else if (mainCommand.equalsIgnoreCase(COMMANDS.REDO.toString())
				|| mainCommand.equalsIgnoreCase(COMMANDS.REDO_SHORT.toString())) {
			return COMMAND_TYPE.REDO;
		}
		else if (mainCommand.equalsIgnoreCase(COMMANDS.CLEAR.toString())
				|| mainCommand.equalsIgnoreCase(COMMANDS.CLEAR_SHORT.toString())) {
			return COMMAND_TYPE.CLEAR;
		}
		else if (mainCommand.equalsIgnoreCase(COMMANDS.HELP.toString())
				|| mainCommand.equalsIgnoreCase(COMMANDS.HELP_SHORT.toString())) {
			return COMMAND_TYPE.HELP;
		}
		else if (mainCommand.equalsIgnoreCase(COMMANDS.SETDIRECTORY.toString())
				|| mainCommand.equalsIgnoreCase(COMMANDS.SETDIRECTORY_SHORT.toString())) {
			return COMMAND_TYPE.SETDIRECTORY;
		}
		else if (mainCommand.equalsIgnoreCase(COMMANDS.TAG.toString())
				|| mainCommand.equalsIgnoreCase(COMMANDS.TAG_SHORT.toString())) {
			return COMMAND_TYPE.TAG;
		}
		else if (mainCommand.equalsIgnoreCase(COMMANDS.UNTAG.toString())
				|| mainCommand.equalsIgnoreCase(COMMANDS.UNTAG_SHORT.toString())) {
			return COMMAND_TYPE.UNTAG;
		}
		else if (mainCommand.equalsIgnoreCase(COMMANDS.MORE.toString())
				|| mainCommand.equalsIgnoreCase(COMMANDS.MORE_SHORT.toString())) {
			return COMMAND_TYPE.MORE;
		}
		else if (mainCommand.equalsIgnoreCase(COMMANDS.EXIT.toString())
				|| mainCommand.equalsIgnoreCase(COMMANDS.EXIT_SHORT.toString())) {
			return COMMAND_TYPE.EXIT;
		}
		else {
			return COMMAND_TYPE.INVALID;
		}
	}
		
	public Command createCommand(String userCommand) throws Exception {
		LOGGER.log(Level.INFO, "Create command based on determined command type: {0}", userCommand);
		assert(userCommand != null);
		COMMAND_TYPE commandType = determineCommandType(userCommand);
		Command newCommand = createCommandByType(commandType);
		processOptions(commandType, newCommand, userCommand);
		addCommandToList(newCommand, commandType);
		return newCommand;
	}
	
	private Command createCommandByType(COMMAND_TYPE commandType){
		switch (commandType) {
			case ADD:
				return new AddTaskCommand();
			case VIEW:
				return new ViewTaskCommand();
			case EDIT:
				return new EditTaskCommand();
			case DELETE:
				return new DeleteTaskCommand();
			case COMPLETE:
				return new CompleteTaskCommand();
			case SEARCH:
				return new SearchTaskCommand();
			case UNDO:
				return new UndoCommand();
			case REDO:
				return new RedoCommand();
			case CLEAR:
				return new ClearCommand();
			case HELP:
				return new HelpCommand();
			case SETDIRECTORY:
				return new SetDirectoryCommand();
			case MORE:
				return new MoreCommand();
			case TAG:
				return new TagTaskCommand();
			case UNTAG:
				return new UntagTaskCommand();
			case EXIT:
				return new ExitCommand();
			case INVALID:
				return null;
			default:
				LOGGER.severe("commandType is corrupted and not within expectations");
				throw new Error("Corrupted commandType");
		}
	}

	private void addCommandToList(Command newCommand, ParseLogic.COMMAND_TYPE commandType) {
		assert(newCommand != null);
		if (!isInvalidTypeToAdd(commandType)) {
			Command.getCommandList().add(newCommand);
		}
	}

	private void processOptions(COMMAND_TYPE commandType, Command newCommand, String userCommand) throws Exception {
		assert(newCommand != null && userCommand != null);
		List<String> commandList = breakDownCommand(userCommand);
		if (commandType == COMMAND_TYPE.SEARCH) {
			addPossibleDates(newCommand, commandList);
		} else if (commandType == COMMAND_TYPE.TAG || commandType == COMMAND_TYPE.UNTAG) {
			addTags(newCommand, commandList);
		}
		addOptionsToCommand(commandType, newCommand, commandList);
	}

	private void addOptionsToCommand(COMMAND_TYPE commandType, Command command, List<String> commandList) throws Exception {
		LOGGER.info("Attempt to add list of options to Command specified");
		assert(command != null && commandList != null);
		EnumMap<OPTIONS, TYPE> optionMap = getOptionMap(commandType);
		for (int i = commandList.size() - 1; i > -1; i--) {
			if (isOption(optionMap, commandList.get(i))) {
				String option = commandList.get(i);
				List<String> subList = commandList.subList(i, commandList.size());
				Option commandOption = getOption(subList, optionMap);
				if (!isOptionalOrNoArgumentType(optionMap, option) && commandOption == null) {
					continue;
				}
				option = getFullOptionName(option);
				boolean isAddSuccess = command.addOption(option, commandOption);
				if (!isAddSuccess) {
					LOGGER.severe("Unable to add option into command due to unknown reasons.");
					throw new Error("Unknown error has occured.");
				}
				subList.clear();
			}
		}
	}
	
	private Option getOption(List<String> commandList, EnumMap<OPTIONS, TYPE> optionMap) throws Exception {
		assert(optionMap != null && commandList != null);
		Option newOption = null;
		String option = commandList.remove(0);
		LOGGER.log(Level.INFO, "Retrieve expected value of specified option: {0}", option);
		for (OPTIONS opt : optionMap.keySet()) {
			if (option.equalsIgnoreCase(opt.toString())) {
				switch (optionMap.get(opt)) {
				case STRING:
					newOption = expectString(commandList, false);
					break;
				case STRING_ARRAY:
					newOption = expectStringArray(commandList, false);
					break;
				case INTEGER:
					newOption = expectInteger(commandList, false);
					break;
				case INTEGER_ARRAY:
					newOption = expectIntegerArray(commandList, false);
					break;
				case DATE:
					newOption = expectDate(commandList, false);
					break;
				case DAY:
					newOption = expectDay(commandList, false);
					break;
				case STRING_OPT:
					newOption = expectString(commandList, true);
					break;
				case STRING_ARRAY_OPT:
					newOption = expectStringArray(commandList, true);
					break;
				case INTEGER_OPT:
					newOption = expectInteger(commandList, true);
					break;
				case INTEGER_ARRAY_OPT: 
					newOption = expectIntegerArray(commandList, true);
					break;
				case DATE_OPT:
					newOption = expectDate(commandList, true);
					break;
				case NONE:
					commandList.clear();
					return null;
				}
				return newOption;
			}
		}
		// failed to get anything
		LOGGER.severe("option keyword is not within expectations");
		throw new Error("corrupted variable: option");
	}
	
	public void addPossibleDates(Command command, List<String> commandList) throws Exception {
		Option dateOption = scanForDates(commandList, true);
		command.addOption("searchDates", dateOption);
	}
	
	private Option scanForDates(List<String> commandList, boolean optional) throws Exception {
		LOGGER.log(Level.INFO, "Attempt to parse expected string from user input");
		assert(commandList != null);
		Option commandOption = new Option();
		StringBuilder stringOption = new StringBuilder();
		LOGGER.log(Level.WARNING, "expectedString = commandList.get(0) may cause index out of bounds exception");
		if (commandList.isEmpty()) {
			if (optional) {
				return null;
			} else {
				LOGGER.log(Level.SEVERE, "expected input not found");
				throw new InvalidCommandFormatException("Expected input not found!");
			}
		}
		String expectedString = EMPTY_STRING;
		for (String s : commandList) { 
			LOGGER.fine("Expecting a list of Strings");
			expectedString = s;
			if (DateTimeHelper.isDate(expectedString)) {
				String[] testedString = expectedString.split("/");
				expectedString = String.format("%1$s/%2$s/%3$s", testedString[2], testedString[1], testedString[0]);
			}
			stringOption.append(expectedString);
			stringOption.append(SPACE);
		}
		expectedString = stringOption.toString().trim();
		List<LocalDateTime> dates = parseDates(expectedString);
		while (!dates.isEmpty()) {
			commandOption.addValue(dates.remove(0));
		}
		return commandOption;
	}

	private Option expectDay(List<String> commandList, boolean optional) throws Exception {
		LOGGER.log(Level.INFO, "Attempt to parse expected array of integers from user input");
		assert(commandList != null);
		Option commandOption = new Option();
		if (commandList.isEmpty()) {
			if (optional) {
				return null;
			} else {
				LOGGER.log(Level.SEVERE, "expected input not found");
				throw new InvalidCommandFormatException("Expected input not found!");
			}
		}
		for (int i = 0; i < commandList.size(); i++) {
			String expectedDay = commandList.get(i);
			if (!isDay(expectedDay)) {
				return null;
			}
			commandOption.addValue(expectedDay);
		}
		return commandOption;
	}

	private Option expectIntegerArray(List<String> commandList, boolean optional) throws Exception {
		LOGGER.log(Level.INFO, "Attempt to parse expected array of integers from user input");
		assert(commandList != null);
		Option commandOption = new Option();
		if (commandList.isEmpty()) {
			if (optional) {
				return null;
			} else {
				LOGGER.log(Level.SEVERE, "expected input not found");
				throw new InvalidCommandFormatException("Expected input not found!");
			}
		}
		for (int i = 0; i < commandList.size(); i++) {
			String expectedInt = commandList.get(i);
			Integer parsedInt = tryParseInteger(expectedInt);
			if (parsedInt == null || parsedInt < 0) {
				return null;
			}
			commandOption.addValue(parsedInt);
		}
		return commandOption;
	}
	
	private Option expectString(List<String> commandList, boolean optional) throws Exception {
		LOGGER.log(Level.INFO, "Attempt to parse expected string from user input");
		assert(commandList != null);
		Option commandOption = new Option();
		StringBuilder stringOption = new StringBuilder();
		LOGGER.log(Level.WARNING, "expectedString = commandList.get(0) may cause index out of bounds exception");
		if (commandList.isEmpty()) {
			if (optional) {
				return null;
			} else {
				LOGGER.log(Level.SEVERE, "expected input not found");
				throw new InvalidCommandFormatException("Expected input not found!");
			}
		}
		String expectedString = commandList.get(0);
		for (int i = 0; i < commandList.size(); i++) { 
			LOGGER.fine("Expecting a list of Strings");
			expectedString = commandList.get(i);
			stringOption.append(expectedString);
			stringOption.append(SPACE);
		}
		expectedString = stringOption.toString().trim();
		commandOption.addValue(expectedString);
		return commandOption;
	}
	
	private Option expectInteger(List<String> commandList, boolean optional) throws Exception {
		LOGGER.log(Level.INFO, "Attempt to parse single expected integer from user input");
		assert(commandList != null);
		Option commandOption = new Option();
		if (commandList.isEmpty()) {
			if (optional) {
				return null;
			} else {
				LOGGER.log(Level.SEVERE, "expected input not found");
				throw new InvalidCommandFormatException("Expected input not found!");
			}
		}
		LOGGER.log(Level.WARNING, "expectedInt = commandList.get(0) may cause index out of bounds exception");
		String expectedInt = commandList.get(0);
		Integer parsedInt = tryParseInteger(expectedInt);
		if (parsedInt == null || parsedInt < 0) {
			return null;
		}
		commandOption.addValue(parsedInt);
		return commandOption;
	}
	
	private Option expectStringArray(List<String> commandList, boolean optional) throws Exception {
		LOGGER.log(Level.INFO, "Attempt to parse expected array of Strings from user input");
		assert(commandList != null);
		Option commandOption = new Option();
		LOGGER.log(Level.WARNING, "expectedInt = commandList.remove(0) may cause index out of bounds exception");
		if (commandList.isEmpty()) {
			if (optional) {
				return null;
			} else {
				LOGGER.log(Level.SEVERE, "expected input not found");
				throw new InvalidCommandFormatException("Expected input not found!");
			}
		}
		for (int i = 0; i < commandList.size(); i++) {
			String expectedString = commandList.get(i);
			commandOption.addValue(expectedString);
		}
		return commandOption;
	}
	
	public void addTags(Command command, List<String> commandList) throws Exception {
		Option hashtags = expectHashtagArray(commandList, true);
		command.addOption(OPTIONS.HASHTAG.toString(), hashtags);
	}
	
	private Option expectHashtagArray(List<String> commandList, boolean optional) throws Exception {
		LOGGER.log(Level.INFO, "Attempt to parse expected array of Strings from user input");
		assert(commandList != null);
		Option commandOption = new Option();
		LOGGER.log(Level.WARNING, "expectedInt = commandList.remove(0) may cause index out of bounds exception");
		if (commandList.isEmpty()) {
			if (optional) {
				return null;
			} else {
				LOGGER.log(Level.SEVERE, "expected input not found");
				throw new InvalidCommandFormatException("Expected input not found!");
			}
		}
		for (String s : commandList) {
			String expectedString = s;
			if (expectedString.startsWith(OPTIONS.HASHTAG.toString())) {
				commandOption.addValue(expectedString);
			}
		}
		if (commandOption.getValuesCount() == 0) {
			LOGGER.log(Level.SEVERE, "expected input not found");
			throw new InvalidCommandFormatException("Expected input not found!");
		}
		return commandOption;
	}
	
	private Option expectDate(List<String> commandList, boolean optional) throws Exception {
		LOGGER.log(Level.INFO, "Attempt to parse expected string from user input");
		assert(commandList != null);
		Option commandOption = new Option();
		StringBuilder stringOption = new StringBuilder();
		LOGGER.log(Level.WARNING, "expectedString = commandList.get(0) may cause index out of bounds exception");
		if (commandList.isEmpty()) {
			if (optional) {
				return null;
			} else {
				LOGGER.log(Level.SEVERE, "expected input not found");
				throw new InvalidCommandFormatException("Expected input not found!");
			}
		}
		String expectedString = EMPTY_STRING;
		for (int i = 0; i < commandList.size(); i++) { 
			LOGGER.fine("Expecting a list of Strings");
			expectedString = commandList.get(i);
			if (DateTimeHelper.isDate(expectedString)) {
				String[] testedString = expectedString.split("/");
				expectedString = String.format("%1$s/%2$s/%3$s", testedString[2], testedString[1], testedString[0]);
			}
			stringOption.append(expectedString);
			stringOption.append(SPACE);
		}
		expectedString = stringOption.toString().trim();
		LocalDateTime date = parseDate(expectedString);
		if (date == null) {
			return null;
		}
		commandOption.addValue(date);
		return commandOption;
	}
	
	private boolean isInvalidTypeToAdd(COMMAND_TYPE commandType) {
		switch (commandType) {
			case UNDO:
			case REDO:
			case VIEW:
			case CLEAR:
			case HELP:
			case SEARCH:
				return true;
			default:
				return false;
		}
	}
	
	public boolean isStatefulCommand(String userCommand) {
		return isStatefulCommand(determineCommandType(userCommand));
	}
	
	public boolean isSaveStateCommand(String userCommand) {
		return isSaveStateCommand(determineCommandType(userCommand));
	}
	
	private boolean isStatefulCommand(COMMAND_TYPE commandType) {
		switch (commandType) {
			case EDIT:
			case DELETE:
			case COMPLETE:
			case MORE:
			case TAG:
			case UNTAG:
				return true;
			default:
				return false;
		}
	}
	
	private boolean isSaveStateCommand(COMMAND_TYPE commandType) {
		switch (commandType) {
			case VIEW:
			case SEARCH:
				return true;
			default:
				return false;
		}
	}
	
	public String replaceRunningIndex(String userCommand, int[] stateArray) throws Exception {
		COMMAND_TYPE commandType = determineCommandType(userCommand);
		Integer taskID = -1;
		List<String> commandTokens;
		if (commandType == COMMAND_TYPE.EDIT) {
			commandTokens = new ArrayList<String>();
			commandTokens.add(userCommand.split(SPACE)[1]);
		} else {
			commandTokens = breakDownCommand(userCommand);
		}
		for (String s : commandTokens) {
			if ((taskID = tryParseInteger(s)) != null) {
				if (taskID <= 0) {
					String message = String.format("Failed to parse user input: %1$s", userCommand);
					LOGGER.log(Level.SEVERE, message, "Invalid ID provided");
					throw new InvalidCommandFormatException("Task with the following Task ID does not exist!");
				}
				String oldID = String.format("\\s+%1$d(\\s+|$)", taskID);
				String newID = String.format(" %1$d ", stateArray[taskID - 1]);
				userCommand = userCommand.replaceFirst(oldID, newID);
			}
		}
		return userCommand.trim();
	}
	
	private Integer tryParseInteger(String s1) {
		for (int i = 0; i < s1.length(); i++) {
			if(!Character.isDigit(s1.charAt(i))) {
				return null;
			}
		}
		return Integer.parseInt(s1);
	}
}
