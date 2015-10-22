package parser.logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import parser.logic.ParserConstants.COMMAND_TYPE;
import parser.logic.ParserConstants.OPTIONS;
import parser.logic.ParserConstants.TYPE;

class Parser implements ParserConstants {

	protected static final Logger LOGGER = Logger.getLogger(ParseLogic.class.getName());
	
	public Parser() {
		setupCommandEnums();
	}
	
	private void setupCommandEnums() {
		setupAddOption();
		setupViewOption();
		setupEditOption();
		setupDeleteOption();
		setupCompleteOption();
		setupSearchOption();
		setupUndoOption();
		setupRedoOption();
		setupExitOption();
		setupHelpOption();
	}

	private void setupHelpOption() {
		helpOptions.put(OPTIONS.HELP, TYPE.STRING);
	}

	private void setupExitOption() {
		exitOptions.put(OPTIONS.EXIT, TYPE.NONE);
	}

	private void setupRedoOption() {
		redoOptions.put(OPTIONS.REDO, TYPE.NONE);
	}

	private void setupUndoOption() {
		undoOptions.put(OPTIONS.UNDO, TYPE.NONE);
	}

	private void setupSearchOption() {
		searchOptions.put(OPTIONS.SEARCH, TYPE.STRING_ARRAY);
	}

	private void setupCompleteOption() {
		completeOptions.put(OPTIONS.COMPLETE, TYPE.INTEGER_ARRAY);
	}

	private void setupDeleteOption() {
		deleteOptions.put(OPTIONS.DELETE, TYPE.INTEGER_ARRAY);
	}

	private void setupEditOption() {
		editOptions.put(OPTIONS.EDIT, TYPE.INTEGER);
		editOptions.put(OPTIONS.DESC, TYPE.STRING);
		editOptions.put(OPTIONS.BY, TYPE.DATE);
		editOptions.put(OPTIONS.REMIND, TYPE.DATE);
		editOptions.put(OPTIONS.BETWEEN, TYPE.DATE);
		editOptions.put(OPTIONS.AND, TYPE.DATE);
		editOptions.put(OPTIONS.START, TYPE.DATE);
		editOptions.put(OPTIONS.END, TYPE.DATE);
	}

	private void setupViewOption() {
		viewOptions.put(OPTIONS.VIEW, TYPE.INTEGER_ARRAY_OPT);
		viewOptions.put(OPTIONS.COMPLETE, TYPE.NONE);
		viewOptions.put(OPTIONS.ALL, TYPE.NONE);
		viewOptions.put(OPTIONS.FLOATING, TYPE.NONE);
		viewOptions.put(OPTIONS.TODAY, TYPE.NONE);
		viewOptions.put(OPTIONS.TOMORROW, TYPE.NONE);
		viewOptions.put(OPTIONS.WEEK, TYPE.NONE);
		viewOptions.put(OPTIONS.MONTH, TYPE.NONE);
	}

	private void setupAddOption() {
		addOptions.put(OPTIONS.ADD, TYPE.STRING);
		addOptions.put(OPTIONS.BY, TYPE.DATE);
		addOptions.put(OPTIONS.REMIND, TYPE.DATE);
		addOptions.put(OPTIONS.BETWEEN, TYPE.DATE);
		addOptions.put(OPTIONS.AND, TYPE.DATE);
		addOptions.put(OPTIONS.START, TYPE.DATE);
		addOptions.put(OPTIONS.END, TYPE.DATE);
	}
	
	public List<String> breakDownCommand(String userCommand) {
		LOGGER.info("Attempt to breakdown user input into chunks of words.");
		assert(userCommand != null && userCommand.length() > 0);
		List<String> commandLine = new ArrayList<String>();
		commandLine.addAll(Arrays.asList(userCommand.split(SPACE_REGEX)));
		return commandLine;
	}
	
	protected String getMainCommand(String userCommand) {
		LOGGER.log(Level.INFO, "Get first word of user input: {0}", userCommand);
		assert(userCommand != null && userCommand.length() > 0);
		return userCommand.split(SPACE_REGEX)[0];
	}
	
	protected boolean isOption(EnumMap<OPTIONS, TYPE> optionMap, String option) {
		for (OPTIONS value : optionMap.keySet()) {
			if (value.toString().equalsIgnoreCase(option)) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean isOptionalOrNoArgumentType(EnumMap<OPTIONS, TYPE> optionMap, String option) {
		for (OPTIONS value : optionMap.keySet()) {
			if (value.toString().equalsIgnoreCase(option)) {
				return isTrivialOption(optionMap.get(value));
			}
		}
		return false;
	}
	
	private boolean isTrivialOption(TYPE optionType) {
		switch (optionType) {
			case NONE:
			case STRING_OPT:
			case INTEGER_OPT:
			case STRING_ARRAY_OPT:
			case INTEGER_ARRAY_OPT:
			case DATE_OPT:
				return true;
			default:
				return false;
		}
	}
	
	protected EnumMap<OPTIONS, TYPE> getOptionMap(COMMAND_TYPE commandType) {
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
}
