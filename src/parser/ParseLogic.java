package parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import input.*;

public class ParseLogic {
	
	//private static final String SPACES = "\\s+";
	private static final String CMD_PATTERN = "([^\"]\\S*|\".+?\")\\s*";

	private static enum COMMAND_TYPE {
		ADD, VIEW, EDIT, DELETE,
		SEARCH, UNDO, REDO, 
		INVALID, EXIT
	}

	private static enum COMMANDS {
		ADD("add"), VIEW("view"), EDIT("edit"), DELETE("delete"), 
		SEARCH("search"), UNDO("undo"), REDO("redo"),
		EXIT("exit");

		private final String _commandText;
		
		private COMMANDS(final String commandText) {
			this._commandText = commandText;
		}

		@Override
		public String toString() {
			return _commandText;
		}
	}

	public static Command createCommand(String userCommand) {
		List<String> commandArgs = breakDownCommand(userCommand);
		if (commandArgs == null) {
			return null;
		}
		String mainCommand = getMainCommand(commandArgs);
		COMMAND_TYPE commandType = determineCommandType(mainCommand);
		switch (commandType) {
		case ADD:
			return createAddTaskCommand(commandArgs);
		case VIEW:
			return createViewTaskCommand(commandArgs);
		case EDIT:
			return null;
		case DELETE:
			return null;
		case SEARCH:
			return null;
		case UNDO:
			return null;
		case REDO:
			return null;
		case INVALID:
			return null;
		case EXIT:
			return createExitCommand();
		default:
			return null;
		}
	}
	
	private static COMMAND_TYPE determineCommandType(String mainCommand) {
		if (isStringEqual(mainCommand, COMMANDS.ADD.toString())) {
			return COMMAND_TYPE.ADD;
		}
		else if (isStringEqual(mainCommand, COMMANDS.VIEW.toString())) {
			return COMMAND_TYPE.VIEW;
		}
		else if (isStringEqual(mainCommand, COMMANDS.EDIT.toString())) {
			return COMMAND_TYPE.EDIT;
		}
		else if (isStringEqual(mainCommand, COMMANDS.DELETE.toString())) {
			return COMMAND_TYPE.DELETE;
		}
		else if (isStringEqual(mainCommand, COMMANDS.SEARCH.toString())) {
			return COMMAND_TYPE.SEARCH;
		}
		else if (isStringEqual(mainCommand, COMMANDS.UNDO.toString())) {
			return COMMAND_TYPE.UNDO;
		}
		else if (isStringEqual(mainCommand, COMMANDS.REDO.toString())) {
			return COMMAND_TYPE.REDO;
		}
		else if (isStringEqual(mainCommand, COMMANDS.EXIT.toString())) {
			return COMMAND_TYPE.EXIT;
		}
		else {
			return COMMAND_TYPE.INVALID;
		}
	}
	
	private static boolean isStringEqual(String s1, String s2) {
		return s1.equals(s2);
	}
	
	private static List<String> breakDownCommand(String userCommand) {
		List<String> commandList = new ArrayList<String>();
		Scanner sc = new Scanner(userCommand);
		while (sc.hasNext(CMD_PATTERN)) {
			commandList.add(sc.next(CMD_PATTERN));
		}
		sc.close();
		return commandList;
	}
	
	private static String getMainCommand(List<String> commandArgs) {
		if (commandArgs == null) {
			return null;
		}
		return commandArgs.remove(0);
	}
	
	private static Command createExitCommand() {
		return new ExitCommand();
	}
	
	private static Command createAddTaskCommand(List<String> commandArgs) {
		String taskName = commandArgs.get(1);
		for (int i = 0; i < commandArgs.size(); i++) {
			String commandArg = commandArgs.get(i);
			switch (commandArg) {
			case "between":
				//expect 0-2 parameters
				//add to start date/time
				break;
			case "and":
				//expect 0-2 parameters
				//add to end date/time
				break;
			case "by":
				//expect 0-2 parameters
				//add to end date/time
				break;
			default:
				return null;
			}
		}
		return new AddTaskCommand();
	}
	
	private static Command createViewTaskCommand(List<String> commandArgs) {
		for (int i = 1; i < commandArgs.size(); i++) {
			String commandArg = commandArgs.get(i);
			switch (commandArg) {
			case "all":
				break;
			case "floating":
				break;
			case "deadline":
				break;
			case "timed":
				break;
			case "today":
				break;
			case "tomorrow":
				break;
			case "week":
				break;
			case "month":
				break;
			default:
				return null;
			}
		}
		return new ViewTaskCommand();
	}
}
