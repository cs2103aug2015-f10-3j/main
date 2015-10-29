package parser.logic;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.joestelmach.natty.DateGroup;

import common.data.ParserConstants;

class Parser implements ParserConstants {

	
	public static final String SPACE_REGEX = "\\s+";
	public static final String EMPTY_STRING = "";
	public static final String SPACE = " ";
	
	public static final boolean OPTIONAL = true;
	public static final boolean NOT_OPTIONAL = false;
	
	public static final EnumMap<OPTIONS, TYPE> addOptions = new EnumMap<OPTIONS, TYPE>(OPTIONS.class);
	public static final EnumMap<OPTIONS, TYPE> viewOptions = new EnumMap<OPTIONS, TYPE>(OPTIONS.class);
	public static final EnumMap<OPTIONS, TYPE> editOptions = new EnumMap<OPTIONS, TYPE>(OPTIONS.class);
	public static final EnumMap<OPTIONS, TYPE> deleteOptions = new EnumMap<OPTIONS, TYPE>(OPTIONS.class);
	public static final EnumMap<OPTIONS, TYPE> completeOptions = new EnumMap<OPTIONS, TYPE>(OPTIONS.class);
	public static final EnumMap<OPTIONS, TYPE> searchOptions = new EnumMap<OPTIONS, TYPE>(OPTIONS.class);
	public static final EnumMap<OPTIONS, TYPE> undoOptions = new EnumMap<OPTIONS, TYPE>(OPTIONS.class);
	public static final EnumMap<OPTIONS, TYPE> redoOptions = new EnumMap<OPTIONS, TYPE>(OPTIONS.class);
	public static final EnumMap<OPTIONS, TYPE> exitOptions = new EnumMap<OPTIONS, TYPE>(OPTIONS.class);
	public static final EnumMap<OPTIONS, TYPE> clearOptions = new EnumMap<OPTIONS, TYPE>(OPTIONS.class);
	public static final EnumMap<OPTIONS, TYPE> helpOptions = new EnumMap<OPTIONS, TYPE>(OPTIONS.class);
	public static final EnumMap<OPTIONS, TYPE> setDirectoryOptions = new EnumMap<OPTIONS, TYPE>(OPTIONS.class);

	protected static final Logger LOGGER = Logger.getLogger(ParseLogic.class.getName());
	private static HashMap<String, String> shortHandMap = new HashMap<String, String>();
	protected static com.joestelmach.natty.Parser dateParser = new com.joestelmach.natty.Parser();
	
	public Parser() {
		parseDate("warmup");
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
		setupSetDirectoryOption();
		setUpShortHandMapping();
	}

	private void setupSetDirectoryOption() {
		setDirectoryOptions.put(OPTIONS.SETDIRECTORY, TYPE.STRING);

		setDirectoryOptions.put(OPTIONS.SETDIRECTORY_SHORT, TYPE.STRING);
	}

	private void setUpShortHandMapping() {
		shortHandMap.put(OPTIONS.ADD_SHORT.toString(), OPTIONS.ADD.toString());
		shortHandMap.put(OPTIONS.VIEW_SHORT.toString(), OPTIONS.VIEW.toString());
		shortHandMap.put(OPTIONS.EDIT_SHORT.toString(), OPTIONS.EDIT.toString());
		shortHandMap.put(OPTIONS.DELETE_SHORT.toString(), OPTIONS.DELETE.toString());
		shortHandMap.put(OPTIONS.COMPLETE_SHORT.toString(), OPTIONS.COMPLETE.toString());
		shortHandMap.put(OPTIONS.SEARCH_SHORT.toString(), OPTIONS.SEARCH.toString());
		shortHandMap.put(OPTIONS.BY_SHORT.toString(), OPTIONS.BY.toString());
		shortHandMap.put(OPTIONS.UNDO_SHORT.toString(), OPTIONS.UNDO.toString());
		shortHandMap.put(OPTIONS.REDO_SHORT.toString(), OPTIONS.REDO.toString());
		shortHandMap.put(OPTIONS.REMIND_SHORT.toString(), OPTIONS.REMIND.toString());
		shortHandMap.put(OPTIONS.CLEAR_SHORT.toString(), OPTIONS.CLEAR.toString());
		shortHandMap.put(OPTIONS.EXIT_SHORT.toString(), OPTIONS.EXIT.toString());
		shortHandMap.put(OPTIONS.BETWEEN_SHORT.toString(), OPTIONS.BETWEEN.toString());
		shortHandMap.put(OPTIONS.AND_SHORT.toString(), OPTIONS.AND.toString());
		shortHandMap.put(OPTIONS.DESC_SHORT.toString(), OPTIONS.DESC.toString());
		shortHandMap.put(OPTIONS.START_SHORT.toString(), OPTIONS.START.toString());
		shortHandMap.put(OPTIONS.END_SHORT.toString(), OPTIONS.END.toString());
		shortHandMap.put(OPTIONS.ALL_SHORT.toString(), OPTIONS.ALL.toString());
		shortHandMap.put(OPTIONS.FLOATING_SHORT.toString(), OPTIONS.FLOATING.toString());
		shortHandMap.put(OPTIONS.DEADLINE_SHORT.toString(), OPTIONS.DEADLINE.toString());
		shortHandMap.put(OPTIONS.TIMED_SHORT.toString(), OPTIONS.TIMED.toString());
		shortHandMap.put(OPTIONS.TODAY_SHORT.toString(), OPTIONS.TODAY.toString());
		shortHandMap.put(OPTIONS.TOMORROW_SHORT.toString(), OPTIONS.TOMORROW.toString());
		shortHandMap.put(OPTIONS.WEEK_SHORT.toString(), OPTIONS.WEEK.toString());
		shortHandMap.put(OPTIONS.MONTH_SHORT.toString(), OPTIONS.MONTH.toString());
		shortHandMap.put(OPTIONS.HELP_SHORT.toString(), OPTIONS.HELP.toString());
	}

	private void setupHelpOption() {
		helpOptions.put(OPTIONS.HELP, TYPE.STRING_OPT);

		helpOptions.put(OPTIONS.HELP_SHORT, TYPE.STRING_OPT);
	}

	private void setupExitOption() {
		exitOptions.put(OPTIONS.EXIT, TYPE.NONE);

		exitOptions.put(OPTIONS.EXIT_SHORT, TYPE.NONE);
	}

	private void setupRedoOption() {
		redoOptions.put(OPTIONS.REDO, TYPE.NONE);

		redoOptions.put(OPTIONS.REDO_SHORT, TYPE.NONE);
	}

	private void setupUndoOption() {
		undoOptions.put(OPTIONS.UNDO, TYPE.NONE);

		undoOptions.put(OPTIONS.UNDO_SHORT, TYPE.NONE);
	}

	private void setupSearchOption() {
		searchOptions.put(OPTIONS.SEARCH, TYPE.STRING_ARRAY);

		searchOptions.put(OPTIONS.SEARCH_SHORT, TYPE.STRING_ARRAY);
	}

	private void setupCompleteOption() {
		completeOptions.put(OPTIONS.COMPLETE, TYPE.INTEGER_ARRAY);

		completeOptions.put(OPTIONS.COMPLETE_SHORT, TYPE.INTEGER_ARRAY);
	}

	private void setupDeleteOption() {
		deleteOptions.put(OPTIONS.DELETE, TYPE.INTEGER_ARRAY);

		deleteOptions.put(OPTIONS.DELETE_SHORT, TYPE.INTEGER_ARRAY);
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
		
		editOptions.put(OPTIONS.EDIT_SHORT, TYPE.INTEGER);
		editOptions.put(OPTIONS.DESC_SHORT, TYPE.STRING);
		editOptions.put(OPTIONS.BY_SHORT, TYPE.DATE);
		editOptions.put(OPTIONS.REMIND_SHORT, TYPE.DATE);
		editOptions.put(OPTIONS.BETWEEN_SHORT, TYPE.DATE);
		editOptions.put(OPTIONS.AND_SHORT, TYPE.DATE);
		editOptions.put(OPTIONS.START_SHORT, TYPE.DATE);
		editOptions.put(OPTIONS.END_SHORT, TYPE.DATE);
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
		
		viewOptions.put(OPTIONS.VIEW_SHORT, TYPE.INTEGER_ARRAY_OPT);
		viewOptions.put(OPTIONS.COMPLETE_SHORT, TYPE.NONE);
		viewOptions.put(OPTIONS.ALL_SHORT, TYPE.NONE);
		viewOptions.put(OPTIONS.FLOATING_SHORT, TYPE.NONE);
		viewOptions.put(OPTIONS.TODAY_SHORT, TYPE.NONE);
		viewOptions.put(OPTIONS.TOMORROW_SHORT, TYPE.NONE);
		viewOptions.put(OPTIONS.WEEK_SHORT, TYPE.NONE);
		viewOptions.put(OPTIONS.MONTH_SHORT, TYPE.NONE);
	}

	private void setupAddOption() {
		addOptions.put(OPTIONS.ADD, TYPE.STRING);
		addOptions.put(OPTIONS.BY, TYPE.DATE);
		addOptions.put(OPTIONS.REMIND, TYPE.DATE);
		addOptions.put(OPTIONS.BETWEEN, TYPE.DATE);
		addOptions.put(OPTIONS.AND, TYPE.DATE);
		addOptions.put(OPTIONS.START, TYPE.DATE);
		addOptions.put(OPTIONS.END, TYPE.DATE);

		addOptions.put(OPTIONS.ADD_SHORT, TYPE.STRING);
		addOptions.put(OPTIONS.BY_SHORT, TYPE.DATE);
		addOptions.put(OPTIONS.REMIND_SHORT, TYPE.DATE);
		addOptions.put(OPTIONS.BETWEEN_SHORT, TYPE.DATE);
		addOptions.put(OPTIONS.AND_SHORT, TYPE.DATE);
		addOptions.put(OPTIONS.START_SHORT, TYPE.DATE);
		addOptions.put(OPTIONS.END_SHORT, TYPE.DATE);
	}
	
	protected LocalDateTime parseDate(String expectedDate) {
		List<DateGroup> groups = dateParser.parse(expectedDate);
		for(DateGroup group : groups) {
			List<Date> dates = group.getDates();
			if (!dates.isEmpty()) {
				return LocalDateTime.ofInstant(dates.get(0).toInstant(), ZoneId.systemDefault());
			}
		}
		return null;
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
	
	protected String getFullOptionName(String option) {
		if (shortHandMap.containsKey(option)) {
			return shortHandMap.get(option);
		} else {
			return option;
		}
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
