package parser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import input.*;
import util.DateTimeCommon;

class ParseLogic {
	
	private static final String SPACES = "\\s+";
	private static final String CMD_PATTERN = "([^\"]\\S*|\".+?\")\\s*";
	private static final String EMPTY_STRING = "";

	protected static enum COMMAND_TYPE {
		ADD, VIEW, EDIT, DELETE,
		SEARCH, UNDO, REDO, 
		INVALID, EXIT
	}

	protected static enum COMMANDS {
		ADD("add"), VIEW("view"), EDIT("edit"), DELETE("delete"), 
		COMPLETE("complete"), SEARCH("search"), UNDO("undo"), REDO("redo"),
		EXIT("exit");

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
		COMPLETE("complete"), SEARCH("search"), UNDO("undo"), REDO("redo"),
		EXIT("exit"), BY("by"), BETWEEN("between"), AND("and"),
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

	protected COMMAND_TYPE determineCommandType(String userCommand) {
		String mainCommand = getMainCommand(userCommand);
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
		else if (mainCommand.equals(COMMANDS.SEARCH.toString())) {
			return COMMAND_TYPE.SEARCH;
		}
		else if (mainCommand.equals(COMMANDS.UNDO.toString())) {
			return COMMAND_TYPE.UNDO;
		}
		else if (mainCommand.equals(COMMANDS.REDO.toString())) {
			return COMMAND_TYPE.REDO;
		}
		else if (mainCommand.equals(COMMANDS.EXIT.toString())) {
			return COMMAND_TYPE.EXIT;
		}
		else {
			return COMMAND_TYPE.INVALID;
		}
	}
	
	protected List<String> breakDownCommand(String userCommand) {
		List<String> commandLine = new ArrayList<String>();
		Scanner sc = new Scanner(userCommand);
		while (sc.hasNext(CMD_PATTERN)) {
			commandLine.add(sc.next(CMD_PATTERN));
		}
		sc.close();
		return commandLine;
	}
	
	private String getMainCommand(String userCommand) {
		if (userCommand == null) {
			return null;
		}
		return userCommand.split(SPACES)[0];
	}
		
	protected Command createCommand(COMMAND_TYPE commandType) throws Exception {
		switch (commandType) {
			case ADD:
				return new AddTaskCommand();
			case VIEW:
				return new ViewTaskCommand();
			case EDIT:
				return new EditTaskCommand();
			case DELETE:
				return new DeleteTaskCommand();
			case SEARCH:
				return null;//new SearchTaskCommand();
			case UNDO:
				return null;//new UndoTaskCommand();
			case REDO:
				return null;//new RedoTaskCommand();
			case EXIT:
				return new ExitCommand();
			case INVALID:
				return null;
			default:
				throw new Exception();
		}
	}

	protected void addOptionsToCommand(Command command, List<String> commandList) throws Exception {
		while (!commandList.isEmpty()) {
			String option = commandList.remove(0);
			if (!isOption(option)) {
				throw new Exception();
			}
			Option commandOption = getOption(option, commandList);
			if (!command.addOption(option, commandOption)) {
				throw new Error();
			}
		}
	}
	
	private Option getOption(String option, List<String> commandList) throws Exception{
		if (option.equals(OPTIONS.ADD.toString())) {
			return expectString(commandList);
		} else if (option.equals(OPTIONS.VIEW.toString())) {
			return null;
		} else if (option.equals(OPTIONS.EDIT.toString())) {
			return expectInteger(commandList);
		} else if (option.equals(OPTIONS.DELETE.toString())) {
			return expectIntegerArray(commandList);
		} else if (option.equals(OPTIONS.COMPLETE.toString())) {
			return expectIntegerArray(commandList);
		} else if (option.equals(OPTIONS.SEARCH.toString())) {
			return null;
		} else if (option.equals(OPTIONS.UNDO.toString())) {
			return null;
		} else if (option.equals(OPTIONS.REDO.toString())) {
			return null;
		} else if (option.equals(OPTIONS.EXIT.toString())) {
			return null;
		} else if (option.equals(OPTIONS.BY.toString())) {
			return expectDate(commandList);
		} else if (option.equals(OPTIONS.BETWEEN.toString())) {
			return expectDate(commandList);
		} else if (option.equals(OPTIONS.AND.toString())) {
			return expectDate(commandList);
		} else if (option.equals(OPTIONS.NAME.toString())) {
			return expectString(commandList);
		} else if (option.equals(OPTIONS.START.toString())) {
			return expectDate(commandList);
		} else if (option.equals(OPTIONS.END.toString())) {
			return expectDate(commandList);
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
		} else {
			throw new Exception();
		}
	}
	
	private Option expectIntegerArray(List<String> commandList) throws Exception {
		Option commandOption = new Option();
		String expectedInt = commandList.remove(0);
		if (isOption(expectedInt)) {
			throw new Exception();
		}
		commandOption.addValue(Integer.parseInt(expectedInt));
		while (!isOption(commandList.get(0))) {
			commandOption.addValue(Integer.parseInt(commandList.remove(0)));
		}
		return commandOption;
	}
	
	private Option expectString(List<String> commandList) throws Exception {
		Option commandOption = new Option();
		String expectedString = commandList.remove(0);
		if (isOption(expectedString)) {
			throw new Exception();
		}
		commandOption.addValue(expectedString);
		return commandOption;
	}
	
	private Option expectInteger(List<String> commandList) throws Exception {
		Option commandOption = new Option();
		String expectedInt = commandList.remove(0);
		if (isOption(expectedInt)) {
			throw new Exception();
		}
		commandOption.addValue(Integer.parseInt(expectedInt));
		return commandOption;
	}
	
	private Option expectDate(List<String> commandList) throws Exception {
		Option commandOption = new Option();
		String expectedDate = commandList.remove(0);
		if (isOption(expectedDate)) {
			throw new Exception();
		}
		String expectedTime = commandList.remove(0);
		if (isOption(expectedTime)) {
			commandList.add(0, expectedTime);
			expectedTime = EMPTY_STRING;
		}
		commandOption.addValue(convertToDate(expectedDate, expectedTime));
		return commandOption;
	}
	
	private LocalDateTime convertToDate(String date, String time) throws Exception {
		if (!isDate(date)) {
			time = date;
			date = DateTimeCommon.getDate(DateTimeCommon.now());
		} else if (!isTime(time)) {
			time = "23:59";
		}
		LocalDateTime dateTime = DateTimeCommon.parseStringToDateTime(date + " " + time);
		if (dateTime == null) {
			throw new Exception();
		}
		return dateTime;
	}
	
	private boolean isDate(String date) {
		return date.matches("(\\d{4})/(\\d{2})/(\\d{2})");
	}
	
	private boolean isTime(String time) {
		return time.matches("(\\d{2}):(\\d{2})");
	}
	
	private boolean isOption(String option) {
		for (OPTIONS value : OPTIONS.values()) {
			if (value.toString().equals(option)) {
				return true;
			}
		}
		return false;
	}
}
