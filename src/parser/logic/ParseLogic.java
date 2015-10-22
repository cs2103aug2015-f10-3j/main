package parser.logic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import command.api.*;
import command.data.Option;
import common.exception.InvalidCommandFormatException;
import common.util.DateTimeHelper;

public class ParseLogic extends Parser {
	
	private static final Logger LOGGER = Logger.getLogger(ParseLogic.class.getName());
	
	public ParseLogic() {
		LOGGER.info("Initiating ParseLogic");
	}

	public COMMAND_TYPE determineCommandType(String userCommand) {
		LOGGER.log(Level.INFO, "Attempt to determine command type from user input: {0}", userCommand);
		assert(userCommand != null);
		String mainCommand = getMainCommand(userCommand).toLowerCase();
		if (mainCommand.equals(COMMANDS.ADD.toString())) {
			return COMMAND_TYPE.ADD;
		}
		else if (mainCommand.equals(COMMANDS.VIEW.toString())) {
			return COMMAND_TYPE.VIEW;
		}
		else if (mainCommand.equals(COMMANDS.EDIT.toString())) {
			return COMMAND_TYPE.EDIT;
		}
		else if (mainCommand.equals(COMMANDS.DELETE.toString())) {
			return COMMAND_TYPE.DELETE;
		}
		else if (mainCommand.equals(COMMANDS.COMPLETE.toString())) {
            return COMMAND_TYPE.COMPLETE;
        }
		else if (mainCommand.equals(COMMANDS.SEARCH.toString())) {
			return COMMAND_TYPE.SEARCH;
		}
		else if (mainCommand.equals(COMMANDS.UNDO.toString())) {
			return COMMAND_TYPE.UNDO;
		}
		else if (mainCommand.equals(COMMANDS.REDO.toString())) {
			return COMMAND_TYPE.REDO;
		}
		else if (mainCommand.equals(COMMANDS.CLEAR.toString())) {
			return COMMAND_TYPE.CLEAR;
		}
		else if (mainCommand.equals(COMMANDS.HELP.toString())) {
			return COMMAND_TYPE.HELP;
		}
		else if (mainCommand.equals(COMMANDS.EXIT.toString())) {
			return COMMAND_TYPE.EXIT;
		}
		else {
			return COMMAND_TYPE.INVALID;
		}
	}
	
	public List<String> breakDownCommand(String userCommand) {
		LOGGER.info("Attempt to breakdown user input into chunks of words.");
		assert(userCommand != null && userCommand.length() > 0);
		List<String> commandLine = new ArrayList<String>();
		commandLine.addAll(Arrays.asList(userCommand.split(SPACE_REGEX)));
		return commandLine;
	}
	
	private String getMainCommand(String userCommand) {
		LOGGER.log(Level.INFO, "Get first word of user input: {0}", userCommand);
		assert(userCommand != null && userCommand.length() > 0);
		return userCommand.split(SPACE_REGEX)[0];
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
		while (!commandList.isEmpty()) {
			LOGGER.fine("Retrieve head of list to check if the word is a keyword");
			LOGGER.warning("May cause index out of bounds");
			String option = commandList.remove(0);
			if (!isOption(optionMap, option)) {
				throw new InvalidCommandFormatException("Keyword was expected but not found.");
			}
			Option commandOption = getOption(option, commandList, optionMap);
			if (!command.addOption(option, commandOption)) {
				LOGGER.severe("Unable to add option into command due to unknown reasons.");
				throw new Error("Unknown error has occured.");
			}
		}
	}
	
	private EnumMap<OPTIONS, TYPE> getOptionMap(COMMAND_TYPE commandType) {
		switch (commandType) {
			case ADD:
				return addOptions;
			case VIEW:
				return viewOptions;
			case EDIT:
				return editOptions;
			case DELETE:
				return deleteOptions;
			case COMPLETE:
				return completeOptions;
			case SEARCH:
				return searchOptions;
			case UNDO:
				return undoOptions;
			case REDO:
				return redoOptions;
			case CLEAR:
				return clearOptions;
			case HELP:
				return helpOptions;
			case EXIT:
				return exitOptions;
			case INVALID:
				return null;
			default:
				LOGGER.severe("commandType is corrupted and not within expectations");
				throw new Error("Corrupted commandType");
		}
	}
	
	private Option getOption(String option, List<String> commandList, EnumMap<OPTIONS, TYPE> optionMap) throws Exception {
		LOGGER.log(Level.INFO, "Retrieve expected value of specified option: {0}", option);
		assert(option != null && option.length() > 0 && commandList != null);
		for (OPTIONS opt : optionMap.keySet()) {
			if (option.equals(opt.toString())) {
				switch (optionMap.get(opt)) {
				case STRING:
					return expectString(commandList, optionMap, false);
				case STRING_ARRAY:
					return expectStringArray(commandList, optionMap, false);
				case INTEGER:
					return expectInteger(commandList, optionMap, false);
				case INTEGER_ARRAY:
					return expectIntegerArray(commandList, optionMap, false);
				case DATE:
					return expectDate(commandList, optionMap, false);
				case STRING_OPT:
					return expectString(commandList, optionMap, true);
				case STRING_ARRAY_OPT:
					return expectStringArray(commandList, optionMap, true);
				case INTEGER_OPT:
					return expectInteger(commandList, optionMap, true);
				case INTEGER_ARRAY_OPT: 
					return expectIntegerArray(commandList, optionMap, true);
				case DATE_OPT:
					return expectDate(commandList, optionMap, true);
				case NONE:
					return null;
				}
			}
		}
		// failed to get anything
		LOGGER.severe("option keyword is not within expectations");
		throw new Error("corrupted variable: option");
	}
	
	private Option expectIntegerArray(List<String> commandList, EnumMap<OPTIONS, TYPE> optionMap, boolean optional) throws Exception {
		LOGGER.log(Level.INFO, "Attempt to parse expected array of integers from user input");
		assert(commandList != null);
		Option commandOption = new Option();
		LOGGER.log(Level.WARNING, "expectedInt = commandList.remove(0) may cause index out of bounds exception");
		String expectedInt = commandList.remove(0);
		if (isOption(optionMap, expectedInt)) {
			if (optional) {
				commandList.add(0, expectedInt);
				return null;
			}
			LOGGER.severe("Expected unoptional integer input but integer was not found");
			throw new InvalidCommandFormatException("Integers were expected but not found.");
		}
		commandOption.addValue(Integer.parseInt(expectedInt));
		while (!commandList.isEmpty() && !isOption(optionMap, commandList.get(0))) {
			LOGGER.fine("Continue expecting a list of integers");
			LOGGER.warning("May cause NumberFormatException when string is not an integer");
			commandOption.addValue(Integer.parseInt(commandList.remove(0)));
		}
		return commandOption;
	}
	
	private Option expectString(List<String> commandList, EnumMap<OPTIONS, TYPE> optionMap, boolean optional) throws Exception {
		LOGGER.log(Level.INFO, "Attempt to parse expected string from user input");
		assert(commandList != null);
		Option commandOption = new Option();
		StringBuilder stringOption = new StringBuilder();
		LOGGER.log(Level.WARNING, "expectedString = commandList.get(0) may cause index out of bounds exception");
		String expectedString = commandList.get(0);
		if (isOption(optionMap, expectedString)) {
			if (optional) {
				return null;
			}
			LOGGER.severe("Expected unoptional string input but the string was not found");
			throw new InvalidCommandFormatException("A string was expected but not found.");
		}
		do {
			LOGGER.fine("Continue expecting a list of Strings");
			expectedString = commandList.get(0);
			if  (isOption(optionMap, expectedString)) {
				break;
			}
			stringOption.append(expectedString);
			stringOption.append(SPACE);
			commandList.remove(0);
		} while (!commandList.isEmpty());
		expectedString = stringOption.toString().trim();
		commandOption.addValue(expectedString);
		return commandOption;
	}
	
	private Option expectInteger(List<String> commandList, EnumMap<OPTIONS, TYPE> optionMap, boolean optional) throws Exception {
		LOGGER.log(Level.INFO, "Attempt to parse single expected integer from user input");
		assert(commandList != null);
		Option commandOption = new Option();
		LOGGER.log(Level.WARNING, "expectedInt = commandList.get(0) may cause index out of bounds exception");
		String expectedInt = commandList.remove(0);
		if (isOption(optionMap, expectedInt)) {
			if (optional) {
				commandList.add(0, expectedInt);
				return null;
			}
			LOGGER.severe("Expected single unoptional integer input but the integer was not found");
			throw new InvalidCommandFormatException("A string was expected but not found.");
		}
		LOGGER.warning("May cause NumberFormatException when string is not an integer");
		commandOption.addValue(Integer.parseInt(expectedInt));
		return commandOption;
	}
	
	private Option expectStringArray(List<String> commandList, EnumMap<OPTIONS, TYPE> optionMap, boolean optional) throws Exception {
		LOGGER.log(Level.INFO, "Attempt to parse expected array of Strings from user input");
		assert(commandList != null);
		Option commandOption = new Option();
		LOGGER.log(Level.WARNING, "expectedInt = commandList.remove(0) may cause index out of bounds exception");
		String expectedString = commandList.remove(0);
		if (isOption(optionMap, expectedString)) {
			if (optional) {
				commandList.add(0, expectedString);
				return null;
			}
			LOGGER.severe("Expected unoptional integer input but integer was not found");
			throw new InvalidCommandFormatException("Integers were expected but not found.");
		}
		commandOption.addValue(expectedString);
		while (!commandList.isEmpty() && !isOption(optionMap, commandList.get(0))) {
			LOGGER.fine("Continue expecting a list of integers");
			LOGGER.warning("May cause NumberFormatException when string is not an integer");
			commandOption.addValue(commandList.remove(0));
		}
		return commandOption;
	}
	
	private Option expectDate(List<String> commandList, EnumMap<OPTIONS, TYPE> optionMap, boolean optional) throws Exception {
		LOGGER.log(Level.INFO, "Attempt to parse expected date time pair from user input");
		assert(commandList != null);
		Option commandOption = new Option();
		LOGGER.log(Level.WARNING, "expectedDate = commandList.get(0) may cause index out of bounds exception");
		String expectedDate = commandList.remove(0);
		if (isOption(optionMap, expectedDate)) {
			if (optional) {
				commandList.add(0, expectedDate);
				return null;
			}
			LOGGER.severe("Expected single unoptional Date/Time input but the value was not found");
			throw new InvalidCommandFormatException("A Date/Time String was expected but not found.");
		}
		String expectedTime = EMPTY_STRING;
		if (!commandList.isEmpty()) {
			expectedTime = commandList.remove(0);
		}
		if (isOption(optionMap, expectedTime)) {
			commandList.add(0, expectedTime);
			expectedTime = EMPTY_STRING;
		}
		commandOption.addValue(convertToDate(expectedDate, expectedTime));
		return commandOption;
	}
	
	private LocalDateTime convertToDate(String date, String time) throws Exception {
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
		if (dateTime == null) {
			throw new Exception();
		}
		return dateTime;
	}
	
	private boolean isDate(String date) {
		return date.matches("(\\d{2})/(\\d{2})/(\\d{4})");
	}
	
	private boolean isTime(String time) {
		return time.matches("(\\d{2}):(\\d{2})");
	}
	
	private boolean isOption(EnumMap<OPTIONS, TYPE> optionMap, String option) {
		for (OPTIONS value : optionMap.keySet()) {
			if (value.toString().equalsIgnoreCase(option)) {
				return true;
			}
		}
		return false;
	}
	
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
		List<String> commandTokens = breakDownCommand(userCommand);
		for (int i = 0; i < commandTokens.size(); i++) {
			Integer taskID;
			if ((taskID = tryParseInteger(commandTokens.get(i))) != null) {
				if (taskID <= 0) {
					String message = String.format("Failed to parse user input: %1$s", userCommand);
					LOGGER.log(Level.SEVERE, message, "Invalid ID provided");
					throw new InvalidCommandFormatException("User input supplied was in an invalid format");
				}
				String oldID = String.format("\\s+%1$d(\\s+|$)", taskID);
				String newID = String.format(" %1$d ", stateArray[taskID - 1]);
				userCommand = userCommand.replaceAll(oldID, newID);
			}
		}
		return userCommand;
	}
	
	private Integer tryParseInteger(String s1) {
		Integer parsedInt;
		try {
			parsedInt = Integer.parseInt(s1);
		} catch (NumberFormatException nfe) {
			parsedInt = null;
		}
		return parsedInt;
	}
}
