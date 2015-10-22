package parser.logic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.logging.Level;

import command.api.*;
import command.data.Option;
import common.exception.InvalidCommandFormatException;

public class ParseLogic extends Parser {
	
	public ParseLogic() {
		LOGGER.info("Initiating ParseLogic");
	}

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
		else if (mainCommand.equalsIgnoreCase(COMMANDS.EXIT.toString())
				|| mainCommand.equalsIgnoreCase(COMMANDS.EXIT_SHORT.toString())) {
			return COMMAND_TYPE.EXIT;
		}
		else {
			return COMMAND_TYPE.INVALID;
		}
	}
		
	public Command createCommand(COMMAND_TYPE commandType) {
		LOGGER.log(Level.INFO, "Create command based on determined command type: {0}", commandType);
		assert(commandType != null);
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
			case EXIT:
				return new ExitCommand();
			case INVALID:
				return null;
			default:
				LOGGER.severe("commandType is corrupted and not within expectations");
				throw new Error("Corrupted commandType");
		}
	}

	public void addOptionsToCommand(COMMAND_TYPE commandType, Command command, List<String> commandList) throws Exception {
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
				if (newOption != null) {
					commandList.clear();
				} else {
					commandList.add(0, option);
				}
				return newOption;
			}
		}
		// failed to get anything
		LOGGER.severe("option keyword is not within expectations");
		throw new Error("corrupted variable: option");
	}
	
	private Option expectIntegerArray(List<String> commandList, boolean optional) throws Exception {
		LOGGER.log(Level.INFO, "Attempt to parse expected array of integers from user input");
		assert(commandList != null);
		Option commandOption = new Option();
		if (commandList.isEmpty() && optional) {
			return null;
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
		if (commandList.isEmpty() && optional) {
			return null;
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
		if (commandList.isEmpty() && optional) {
			return null;
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
		if (commandList.isEmpty() && optional) {
			return null;
		}
		for (int i = 0; i < commandList.size(); i++) {
			String expectedString = commandList.get(i);
			commandOption.addValue(expectedString);
		}
		return commandOption;
	}
	
	private Option expectDate(List<String> commandList, boolean optional) throws Exception {
		LOGGER.log(Level.INFO, "Attempt to parse expected string from user input");
		assert(commandList != null);
		Option commandOption = new Option();
		StringBuilder stringOption = new StringBuilder();
		LOGGER.log(Level.WARNING, "expectedString = commandList.get(0) may cause index out of bounds exception");
		if (commandList.isEmpty() && optional) {
			return null;
		}
		String expectedString = commandList.get(0);
		for (int i = 0; i < commandList.size(); i++) { 
			LOGGER.fine("Expecting a list of Strings");
			expectedString = commandList.get(i);
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
	
	/*
	private Option expectDate(List<String> commandList, boolean optional) throws Exception {
		LOGGER.log(Level.INFO, "Attempt to parse expected date time pair from user input");
		assert(commandList != null);
		Option commandOption = new Option();
		LOGGER.log(Level.WARNING, "expectedDate = commandList.get(0) may cause index out of bounds exception");
		if (commandList.isEmpty() && optional) {
			return null;
		}
		String expectedDate = commandList.get(0);
		String expectedTime = EMPTY_STRING;
		if (commandList.size() > 2) {
			expectedTime = commandList.get(1);
		}
		LocalDateTime date = convertToDate(expectedDate, expectedTime);
		if (date == null) {
			return null;
		}
		commandOption.addValue(date);
		return commandOption;
	}
	
	private LocalDateTime convertToDate(String date, String time) {
		if (!isDate(date)) {
			String temp = date;
			if (isDate(time)) {
				date = time;
				time = temp;
			} else {
				time = temp;
				date = DateTimeHelper.getDate(DateTimeHelper.now());
			}
		} else if (!isTime(time)) {
			time = "00:00";
		}
		LocalDateTime dateTime = DateTimeHelper.parseStringToDateTime(date + " " + time);
		return dateTime;
	}
	
	private boolean isDate(String date) {
		return date.matches("(\\d{2})/(\\d{2})/(\\d{4})");
	}
	
	private boolean isTime(String time) {
		return time.matches("(\\d{2}):(\\d{2})");
	}
	*/
	
	public boolean isInvalidTypeToAdd(COMMAND_TYPE commandType) {
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
	
	public boolean isStatefulCommand(COMMAND_TYPE commandType) {
		switch (commandType) {
			case EDIT:
			case DELETE:
			case COMPLETE:
				return true;
			default:
				return false;
		}
	}
	
	public boolean isSaveStateCommand(COMMAND_TYPE commandType) {
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
			commandTokens.add(userCommand.split(SPACE_REGEX)[1]);
		} else {
			commandTokens = breakDownCommand(userCommand);
		}
		for (int i = 0; i < commandTokens.size(); i++) {
			if ((taskID = tryParseInteger(commandTokens.get(i))) != null) {
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
		return userCommand;
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
