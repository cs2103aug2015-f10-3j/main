package parser.logic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import command.api.*;
import command.data.Option;
import common.exception.InvalidCommandFormatException;
import common.util.DateTimeHelper;

public class ParseLogic {
	
	private static final String SPACE_REGEX = "\\s+";
	private static final String EMPTY_STRING = "";
	private static final String SPACE = " ";
	
	private static final boolean OPTIONAL = true;
	private static final boolean NOT_OPTIONAL = false;
	
	private static final Logger LOGGER = Logger.getLogger(ParseLogic.class.getName());

	public static enum COMMAND_TYPE {
		ADD, VIEW, EDIT, DELETE, COMPLETE,
		SEARCH, UNDO, REDO, 
		INVALID, EXIT, CLEAR
	}

	protected static enum COMMANDS {
		ADD("add"), VIEW("view"), EDIT("edit"), DELETE("delete"), 
		COMPLETE("complete"), SEARCH("search"), UNDO("undo"), REDO("redo"),
		EXIT("exit"), CLEAR("clear");

		private final String commandText;
		
		private COMMANDS(final String commandText) {
			this.commandText = commandText;
		}

		@Override
		public String toString() {
			return commandText;
		}
	}

	protected static enum OPTIONS {
		ADD("add"), VIEW("view"), EDIT("edit"), DELETE("delete"), 
		COMPLETE("complete"), SEARCH("search"), BY("by"), UNDO("undo"), 
		REDO("redo"), REMIND("remind"), CLEAR("clear"), EXIT("exit"), BETWEEN("between"), AND("and"),
		NAME("name"), START("start"), END("end"), ALL("all"),
		FLOATING("floating"), DEADLINE("deadline"), TIMED("timed"),
		TODAY("today"), TOMORROW("tomorrow"), WEEK("week"), MONTH("month");

		private final String optionText;
		
		private OPTIONS(final String commandText) {
			this.optionText = commandText;
		}

		@Override
		public String toString() {
			return optionText;
		}
	}
	
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
			case EXIT:
				return new ExitCommand();
			case INVALID:
				return null;
			default:
				LOGGER.severe("commandType is corrupted and not within expectations");
				throw new Error("Corrupted commandType");
		}
	}

	public void addOptionsToCommand(Command command, List<String> commandList) throws Exception {
		LOGGER.info("Attempt to add list of options to Command specified");
		assert(command != null && commandList != null);
		while (!commandList.isEmpty()) {
			LOGGER.fine("Retrieve head of list to check if the word is a keyword");
			LOGGER.warning("May cause index out of bounds");
			String option = commandList.remove(0);
			if (!isOption(option)) {
				throw new InvalidCommandFormatException("Keyword was expected but not found.");
			}
			Option commandOption = getOption(option, commandList);
			if (!command.addOption(option, commandOption)) {
				LOGGER.severe("Unable to add option into command due to unknown reasons.");
				throw new Error("Unknown error has occured.");
			}
		}
	}
	
	private Option getOption(String option, List<String> commandList) throws Exception {
		LOGGER.log(Level.INFO, "Retrieve expected value of specified option: {0}", option);
		assert(option != null && option.length() > 0 && commandList != null);
		if (option.equals(OPTIONS.ADD.toString())) {
			return expectString(commandList, NOT_OPTIONAL);
		} else if (option.equals(OPTIONS.VIEW.toString())) {
			return null;
		} else if (option.equals(OPTIONS.EDIT.toString())) {
			return expectInteger(commandList, NOT_OPTIONAL);
		} else if (option.equals(OPTIONS.DELETE.toString())) {
			return expectIntegerArray(commandList, OPTIONAL);
		} else if (option.equals(OPTIONS.COMPLETE.toString())) {
			return expectIntegerArray(commandList, NOT_OPTIONAL);
		} else if (option.equals(OPTIONS.SEARCH.toString())) {
			return expectString(commandList, NOT_OPTIONAL);
		} else if (option.equals(OPTIONS.BY.toString())) {
			return expectDate(commandList, NOT_OPTIONAL);
		} else if (option.equals(OPTIONS.CLEAR.toString())) {
			return null;
		} else if (option.equals(OPTIONS.UNDO.toString())) {
			return null;
		} else if (option.equals(OPTIONS.REDO.toString())) {
			return null;
		} else if (option.equals(OPTIONS.EXIT.toString())) {
			return null;
		} else if (option.equals(OPTIONS.BETWEEN.toString())) {
			return expectDate(commandList, NOT_OPTIONAL);
		} else if (option.equals(OPTIONS.AND.toString())) {
			return expectDate(commandList, NOT_OPTIONAL);
		} else if (option.equals(OPTIONS.NAME.toString())) {
			return expectString(commandList, NOT_OPTIONAL);
		} else if (option.equals(OPTIONS.START.toString())) {
			return expectDate(commandList, NOT_OPTIONAL);
		} else if (option.equals(OPTIONS.END.toString())) {
			return expectDate(commandList, NOT_OPTIONAL);
		} else if (option.equals(OPTIONS.ALL.toString())) {
			return null;
		} else if (option.equals(OPTIONS.FLOATING.toString())) {
			return null;
		} else if (option.equals(OPTIONS.DEADLINE.toString())) {
			return null;
		} else if (option.equals(OPTIONS.TIMED.toString())) {
			return null;
		} else if (option.equals(OPTIONS.TODAY.toString())) {
			return null;
		} else if (option.equals(OPTIONS.TOMORROW.toString())) {
			return null;
		} else if (option.equals(OPTIONS.WEEK.toString())) {
			return null;
		} else if (option.equals(OPTIONS.MONTH.toString())) {
			return null;
		} else if (option.equals(OPTIONS.REMIND.toString())) {
			return expectDate(commandList, NOT_OPTIONAL);
		} else {
			LOGGER.severe("option keyword is not within expectations");
			throw new Error("corrupted variable: option");
		}
	}
	
	private Option expectIntegerArray(List<String> commandList, boolean optional) throws Exception {
		LOGGER.log(Level.INFO, "Attempt to parse expected array of integers from user input");
		assert(commandList != null);
		Option commandOption = new Option();
		LOGGER.log(Level.WARNING, "expectedInt = commandList.remove(0) may cause index out of bounds exception");
		String expectedInt = commandList.remove(0);
		if (isOption(expectedInt)) {
			if (optional) {
				commandList.add(0, expectedInt);
				return null;
			}
			LOGGER.severe("Expected unoptional integer input but integer was not found");
			throw new InvalidCommandFormatException("Integers were expected but not found.");
		}
		commandOption.addValue(Integer.parseInt(expectedInt));
		while (!commandList.isEmpty() && !isOption(commandList.get(0))) {
			LOGGER.fine("Continue expecting a list of integers");
			LOGGER.warning("May cause NumberFormatException when string is not an integer");
			commandOption.addValue(Integer.parseInt(commandList.remove(0)));
		}
		return commandOption;
	}
	
	private Option expectString(List<String> commandList, boolean optional) throws Exception {
		LOGGER.log(Level.INFO, "Attempt to parse expected string from user input");
		assert(commandList != null);
		Option commandOption = new Option();
		StringBuilder stringOption = new StringBuilder();
		LOGGER.log(Level.WARNING, "expectedString = commandList.get(0) may cause index out of bounds exception");
		String expectedString = commandList.get(0);
		if (isOption(expectedString)) {
			if (optional) {
				return null;
			}
			LOGGER.severe("Expected unoptional string input but the string was not found");
			throw new InvalidCommandFormatException("A string was expected but not found.");
		}
		do {
			LOGGER.fine("Continue expecting a list of Strings");
			expectedString = commandList.get(0);
			if  (isOption(expectedString)) {
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
	
	private Option expectInteger(List<String> commandList, boolean optional) throws Exception {
		LOGGER.log(Level.INFO, "Attempt to parse single expected integer from user input");
		assert(commandList != null);
		Option commandOption = new Option();
		LOGGER.log(Level.WARNING, "expectedInt = commandList.get(0) may cause index out of bounds exception");
		String expectedInt = commandList.remove(0);
		if (isOption(expectedInt)) {
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
	
	private Option expectDate(List<String> commandList, boolean optional) throws Exception {
		LOGGER.log(Level.INFO, "Attempt to parse expected date time pair from user input");
		assert(commandList != null);
		Option commandOption = new Option();
		LOGGER.log(Level.WARNING, "expectedDate = commandList.get(0) may cause index out of bounds exception");
		String expectedDate = commandList.remove(0);
		if (isOption(expectedDate)) {
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
		if (isOption(expectedTime)) {
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
	
	private boolean isOption(String option) {
		for (OPTIONS value : OPTIONS.values()) {
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
				return true;
			default:
				return false;
		}
	}
	
	public boolean isEditDeleteCommand(COMMAND_TYPE commandType) {
		switch (commandType) {
			case EDIT:
			case DELETE:
				return true;
			default:
				return false;
		}
	}
	
}
