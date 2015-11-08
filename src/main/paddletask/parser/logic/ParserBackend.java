//@@author A0125473H
package main.paddletask.parser.logic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.joestelmach.natty.DateGroup;

import main.paddletask.common.data.ParserConstants;
import main.paddletask.common.util.DateTimeHelper;

class ParserBackend implements ParserConstants {

	/*** Variables ***/
	public static final String EMPTY_STRING = "";
	public static final String SPACE = " ";
	public static final char SPACES = ' ';
	public static final char QUOTES = '"';
	public static final String DATE_FORMAT = "%1$s/%2$s/%3$s";
	public static final String OLD_TASKID_FORMAT = "\\s+%1$d(\\s+|$)";
	public static final String NEW_TASKID_FORMAT = " %1$d ";
	
	public static final boolean OPTIONAL = true;
	public static final boolean NOT_OPTIONAL = false;
	
	private static final EnumMap<OPTIONS, TYPE> addOptions = new EnumMap<OPTIONS, TYPE>(OPTIONS.class);
	private static final EnumMap<OPTIONS, TYPE> viewOptions = new EnumMap<OPTIONS, TYPE>(OPTIONS.class);
	private static final EnumMap<OPTIONS, TYPE> editOptions = new EnumMap<OPTIONS, TYPE>(OPTIONS.class);
	private static final EnumMap<OPTIONS, TYPE> deleteOptions = new EnumMap<OPTIONS, TYPE>(OPTIONS.class);
	private static final EnumMap<OPTIONS, TYPE> completeOptions = new EnumMap<OPTIONS, TYPE>(OPTIONS.class);
	private static final EnumMap<OPTIONS, TYPE> searchOptions = new EnumMap<OPTIONS, TYPE>(OPTIONS.class);
	private static final EnumMap<OPTIONS, TYPE> helpOptions = new EnumMap<OPTIONS, TYPE>(OPTIONS.class);
	private static final EnumMap<OPTIONS, TYPE> setDirectoryOptions = new EnumMap<OPTIONS, TYPE>(OPTIONS.class);
	private static final EnumMap<OPTIONS, TYPE> moreOptions = new EnumMap<OPTIONS, TYPE>(OPTIONS.class);
	private static final EnumMap<OPTIONS, TYPE> tagOptions = new EnumMap<OPTIONS, TYPE>(OPTIONS.class);
	private static final EnumMap<OPTIONS, TYPE> untagOptions = new EnumMap<OPTIONS, TYPE>(OPTIONS.class);
	private static final EnumMap<OPTIONS, TYPE> clearOptions = new EnumMap<OPTIONS, TYPE>(OPTIONS.class);
	private static final EnumMap<OPTIONS, TYPE> exitOptions = new EnumMap<OPTIONS, TYPE>(OPTIONS.class);
	private static final EnumMap<OPTIONS, TYPE> redoOptions = new EnumMap<OPTIONS, TYPE>(OPTIONS.class);
	private static final EnumMap<OPTIONS, TYPE> undoOptions = new EnumMap<OPTIONS, TYPE>(OPTIONS.class);
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(ParseLogic.class);
	private static HashMap<String, String> _shortHandMap = new HashMap<String, String>();
	private static HashMap<COMMAND_TYPE, EnumMap<OPTIONS, TYPE>> _optionsMap = new HashMap<COMMAND_TYPE, EnumMap<OPTIONS, TYPE>>();
	private static com.joestelmach.natty.Parser _dateParser = new com.joestelmach.natty.Parser();
	
	/*** Constructor ***/
	public ParserBackend() {
		setupCommandEnums();
	}

	/*** Methods ***/
	private void setupCommandEnums() {
		setupAddOption();
		setupViewOption();
		setupEditOption();
		setupDeleteOption();
		setupCompleteOption();
		setupSearchOption();
		setupHelpOption();
		setupSetDirectoryOption();
		setupTagOption();
		setupUntagOption();
		setupMoreOption();
		setupTrivialOptions();
		setUp_shortHandMapping();
	}

	private void setUp_shortHandMapping() {
		_shortHandMap.put(OPTIONS.ADD_SHORT.toString(), OPTIONS.ADD.toString());
		_shortHandMap.put(OPTIONS.VIEW_SHORT.toString(), OPTIONS.VIEW.toString());
		_shortHandMap.put(OPTIONS.EDIT_SHORT.toString(), OPTIONS.EDIT.toString());
		_shortHandMap.put(OPTIONS.DELETE_SHORT.toString(), OPTIONS.DELETE.toString());
		_shortHandMap.put(OPTIONS.COMPLETE_SHORT.toString(), OPTIONS.COMPLETE.toString());
		_shortHandMap.put(OPTIONS.SEARCH_SHORT.toString(), OPTIONS.SEARCH.toString());
		_shortHandMap.put(OPTIONS.BY_SHORT.toString(), OPTIONS.BY.toString());
		_shortHandMap.put(OPTIONS.UNDO_SHORT.toString(), OPTIONS.UNDO.toString());
		_shortHandMap.put(OPTIONS.REDO_SHORT.toString(), OPTIONS.REDO.toString());
		_shortHandMap.put(OPTIONS.REMIND_SHORT.toString(), OPTIONS.REMIND.toString());
		_shortHandMap.put(OPTIONS.CLEAR_SHORT.toString(), OPTIONS.CLEAR.toString());
		_shortHandMap.put(OPTIONS.EXIT_SHORT.toString(), OPTIONS.EXIT.toString());
		_shortHandMap.put(OPTIONS.BETWEEN_SHORT.toString(), OPTIONS.BETWEEN.toString());
		_shortHandMap.put(OPTIONS.AND_SHORT.toString(), OPTIONS.AND.toString());
		_shortHandMap.put(OPTIONS.DESC_SHORT.toString(), OPTIONS.DESC.toString());
		_shortHandMap.put(OPTIONS.STARTDATE_SHORT.toString(), OPTIONS.STARTDATE.toString());
		_shortHandMap.put(OPTIONS.STARTTIME_SHORT.toString(), OPTIONS.STARTTIME.toString());
		_shortHandMap.put(OPTIONS.ENDDATE_SHORT.toString(), OPTIONS.ENDDATE.toString());
		_shortHandMap.put(OPTIONS.ENDTIME_SHORT.toString(), OPTIONS.ENDTIME.toString());
		_shortHandMap.put(OPTIONS.ALL_SHORT.toString(), OPTIONS.ALL.toString());
		_shortHandMap.put(OPTIONS.VIEW_COMPLETE_SHORT.toString(), OPTIONS.COMPLETE.toString());
		_shortHandMap.put(OPTIONS.TODAY_SHORT.toString(), OPTIONS.TODAY.toString());
		_shortHandMap.put(OPTIONS.TOMORROW_SHORT.toString(), OPTIONS.TOMORROW.toString());
		_shortHandMap.put(OPTIONS.WEEK_SHORT.toString(), OPTIONS.WEEK.toString());
		_shortHandMap.put(OPTIONS.MONTH_SHORT.toString(), OPTIONS.MONTH.toString());
		_shortHandMap.put(OPTIONS.HELP_SHORT.toString(), OPTIONS.HELP.toString());
		_shortHandMap.put(OPTIONS.SETDIRECTORY_SHORT.toString(), OPTIONS.SETDIRECTORY.toString());
		_shortHandMap.put(OPTIONS.MORE_SHORT.toString(), OPTIONS.MORE.toString());
		_shortHandMap.put(OPTIONS.TAG_SHORT.toString(), OPTIONS.TAG.toString());
		_shortHandMap.put(OPTIONS.UNTAG_SHORT.toString(), OPTIONS.UNTAG.toString());
		_shortHandMap.put(OPTIONS.PRIORITY_SHORT.toString(), OPTIONS.PRIORITY.toString());
		_shortHandMap.put(OPTIONS.REPEAT_SHORT.toString(), OPTIONS.REPEAT.toString());
	}
	
	private void setupTrivialOptions() {
		clearOptions.put(OPTIONS.CLEAR, TYPE.NONE);
		clearOptions.put(OPTIONS.CLEAR_SHORT, TYPE.NONE);
		_optionsMap.put(COMMAND_TYPE.CLEAR, clearOptions);
		exitOptions.put(OPTIONS.EXIT, TYPE.NONE);
		exitOptions.put(OPTIONS.EXIT_SHORT, TYPE.NONE);
		_optionsMap.put(COMMAND_TYPE.EXIT, exitOptions);
		redoOptions.put(OPTIONS.REDO, TYPE.NONE);
		redoOptions.put(OPTIONS.REDO_SHORT, TYPE.NONE);
		_optionsMap.put(COMMAND_TYPE.REDO, redoOptions);
		undoOptions.put(OPTIONS.UNDO, TYPE.NONE);
		undoOptions.put(OPTIONS.UNDO_SHORT, TYPE.NONE);
		_optionsMap.put(COMMAND_TYPE.UNDO, undoOptions);
	}

	private void setupMoreOption() {
		moreOptions.put(OPTIONS.MORE, TYPE.INTEGER);
		moreOptions.put(OPTIONS.MORE_SHORT, TYPE.INTEGER);
		_optionsMap.put(COMMAND_TYPE.MORE, moreOptions);
	}

	private void setupUntagOption() {
		untagOptions.put(OPTIONS.UNTAG, TYPE.INTEGER);
		untagOptions.put(OPTIONS.UNTAG_SHORT, TYPE.INTEGER);
		_optionsMap.put(COMMAND_TYPE.UNTAG, untagOptions);
	}

	private void setupTagOption() {
		tagOptions.put(OPTIONS.TAG, TYPE.INTEGER);
		tagOptions.put(OPTIONS.TAG_SHORT, TYPE.INTEGER);
		_optionsMap.put(COMMAND_TYPE.TAG, tagOptions);
	}

	private void setupSetDirectoryOption() {
		setDirectoryOptions.put(OPTIONS.SETDIRECTORY, TYPE.STRING);
		setDirectoryOptions.put(OPTIONS.SETDIRECTORY_SHORT, TYPE.STRING);
		_optionsMap.put(COMMAND_TYPE.SETDIRECTORY, setDirectoryOptions);
	}

	private void setupHelpOption() {
		helpOptions.put(OPTIONS.HELP, TYPE.STRING_OPT);
		helpOptions.put(OPTIONS.HELP_SHORT, TYPE.STRING_OPT);
		_optionsMap.put(COMMAND_TYPE.HELP, helpOptions);
	}

	private void setupSearchOption() {
		searchOptions.put(OPTIONS.SEARCH, TYPE.STRING_ARRAY);
		searchOptions.put(OPTIONS.SEARCH_SHORT, TYPE.STRING_ARRAY);
		_optionsMap.put(COMMAND_TYPE.SEARCH, searchOptions);
	}

	private void setupCompleteOption() {
		completeOptions.put(OPTIONS.COMPLETE, TYPE.INTEGER_ARRAY);
		completeOptions.put(OPTIONS.COMPLETE_SHORT, TYPE.INTEGER_ARRAY);
		_optionsMap.put(COMMAND_TYPE.COMPLETE, completeOptions);
	}

	private void setupDeleteOption() {
		deleteOptions.put(OPTIONS.DELETE, TYPE.INTEGER_ARRAY);
		deleteOptions.put(OPTIONS.DELETE_SHORT, TYPE.INTEGER_ARRAY);
		_optionsMap.put(COMMAND_TYPE.DELETE, deleteOptions);
	}

	private void setupEditOption() {
		editOptions.put(OPTIONS.EDIT, TYPE.INTEGER);
		editOptions.put(OPTIONS.DESC, TYPE.STRING);
		editOptions.put(OPTIONS.REMIND, TYPE.DATE);

		editOptions.put(OPTIONS.BETWEEN, TYPE.DATE);
		editOptions.put(OPTIONS.AND, TYPE.DATE);
		editOptions.put(OPTIONS.STARTDATE, TYPE.DATE);
		editOptions.put(OPTIONS.STARTTIME, TYPE.DATE);
		editOptions.put(OPTIONS.ENDDATE, TYPE.DATE);
		editOptions.put(OPTIONS.ENDTIME, TYPE.DATE);

		editOptions.put(OPTIONS.PRIORITY, TYPE.INTEGER);
		editOptions.put(OPTIONS.REPEAT, TYPE.DAY);
		
		editOptions.put(OPTIONS.EDIT_SHORT, TYPE.INTEGER);
		editOptions.put(OPTIONS.DESC_SHORT, TYPE.STRING);
		editOptions.put(OPTIONS.REMIND_SHORT, TYPE.DATE);

		editOptions.put(OPTIONS.BETWEEN_SHORT, TYPE.DATE);
		editOptions.put(OPTIONS.AND_SHORT, TYPE.DATE);
		editOptions.put(OPTIONS.STARTDATE_SHORT, TYPE.DATE);
		editOptions.put(OPTIONS.STARTTIME_SHORT, TYPE.DATE);
		editOptions.put(OPTIONS.ENDDATE_SHORT, TYPE.DATE);
		editOptions.put(OPTIONS.ENDTIME_SHORT, TYPE.DATE);

		editOptions.put(OPTIONS.PRIORITY_SHORT, TYPE.INTEGER);
		editOptions.put(OPTIONS.REPEAT, TYPE.DAY);
		_optionsMap.put(COMMAND_TYPE.EDIT, editOptions);
	}

	private void setupViewOption() {
		viewOptions.put(OPTIONS.VIEW, TYPE.INTEGER_ARRAY_OPT);
		viewOptions.put(OPTIONS.COMPLETE, TYPE.NONE);
		viewOptions.put(OPTIONS.ALL, TYPE.NONE);
		viewOptions.put(OPTIONS.TODAY, TYPE.NONE);
		viewOptions.put(OPTIONS.TOMORROW, TYPE.NONE);
		viewOptions.put(OPTIONS.WEEK, TYPE.NONE);
		viewOptions.put(OPTIONS.MONTH, TYPE.NONE);
		
		viewOptions.put(OPTIONS.VIEW_SHORT, TYPE.INTEGER_ARRAY_OPT);
		viewOptions.put(OPTIONS.VIEW_COMPLETE_SHORT, TYPE.NONE);
		viewOptions.put(OPTIONS.ALL_SHORT, TYPE.NONE);
		viewOptions.put(OPTIONS.TODAY_SHORT, TYPE.NONE);
		viewOptions.put(OPTIONS.TOMORROW_SHORT, TYPE.NONE);
		viewOptions.put(OPTIONS.WEEK_SHORT, TYPE.NONE);
		viewOptions.put(OPTIONS.MONTH_SHORT, TYPE.NONE);
		_optionsMap.put(COMMAND_TYPE.VIEW, viewOptions);
	}

	private void setupAddOption() {
		addOptions.put(OPTIONS.ADD, TYPE.STRING);
		addOptions.put(OPTIONS.BY, TYPE.DATE);
		addOptions.put(OPTIONS.REMIND, TYPE.DATE);
		addOptions.put(OPTIONS.BETWEEN, TYPE.DATE);
		addOptions.put(OPTIONS.AND, TYPE.DATE);
		addOptions.put(OPTIONS.PRIORITY, TYPE.INTEGER);
		addOptions.put(OPTIONS.REPEAT, TYPE.DAY);

		addOptions.put(OPTIONS.ADD_SHORT, TYPE.STRING);
		addOptions.put(OPTIONS.BY_SHORT, TYPE.DATE);
		addOptions.put(OPTIONS.REMIND_SHORT, TYPE.DATE);
		addOptions.put(OPTIONS.BETWEEN_SHORT, TYPE.DATE);
		addOptions.put(OPTIONS.AND_SHORT, TYPE.DATE);
		addOptions.put(OPTIONS.PRIORITY_SHORT, TYPE.INTEGER);
		addOptions.put(OPTIONS.REPEAT, TYPE.DAY);
		_optionsMap.put(COMMAND_TYPE.ADD, addOptions);
	}
	
	protected LocalDateTime parseDate(String expectedDate) {
		List<DateGroup> groups = _dateParser.parse(expectedDate);
		for(DateGroup group : groups) {
			List<Date> dates = group.getDates();
			if (!dates.isEmpty()) {
				return DateTimeHelper.setTimezoneForDate(dates.get(0));
			}
		}
		return null;
	}
	
	protected List<LocalDateTime> parseDates(String commandString) {
		List<DateGroup> groups = _dateParser.parse(commandString);
		List<LocalDateTime> parsedDates = new ArrayList<LocalDateTime>();
		for(DateGroup group : groups) {
			List<Date> dates = group.getDates();
			while (!dates.isEmpty()) {
				parsedDates.add(DateTimeHelper.setTimezoneForDate(dates.remove(0)));
			}
		}
		return parsedDates;
	}
	
	protected List<String> breakDownCommand(String userCommand) {
		LOGGER.info("Attempt to breakdown user input into chunks of words.");
		assert(userCommand != null && userCommand.length() > 0);
		return preprocessUserCommand(userCommand);
	}
	
	protected String getMainCommand(String userCommand) {
		LOGGER.info("Get first word of user input: {}", userCommand);
		assert(userCommand != null && userCommand.length() > 0);
		return userCommand.split(SPACE)[0];
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
	
	protected boolean isDay(String day) {
		for (DAY d : DAY.values()) {
			if (d.toString().equalsIgnoreCase(day)) {
				return true;
			}
		}
		return false;
	}
	
	protected String getFullOptionName(String option) {
		if (_shortHandMap.containsKey(option)) {
			return _shortHandMap.get(option);
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
		if (!_optionsMap.containsKey(commandType)) {
			LOGGER.error("commandType is corrupted and not within expectations");
			throw new Error("Corrupted commandType");
		}
		return _optionsMap.get(commandType);
	}
	
	private List<String> preprocessUserCommand(String userCommand) {
		List<String> processedCommand = new ArrayList<String>();
		int head = 0;
		for (int i = 0; !isEOL(userCommand, i); i++) {
			if (isEOL(userCommand, i + 1)) {
				addTo(processedCommand, userCommand, head, i + 1);
			} else if (isSpace(userCommand, i)) {
				addTo(processedCommand, userCommand, head, i);
				head = i + 1;
			} else if (isQuote(userCommand, i)) {
				int j = i + 1;
				while (!isEOL(userCommand, j) && !isQuote(userCommand, j)) {
					j++;
				}
				if (!isEOL(userCommand, j)) {
					i = j + 1;
					addTo(processedCommand, userCommand, head + 1, j);
					head = i + 1;
				}
			}
		}
		return processedCommand;
	}
	
	private boolean isQuote(String s, int i) {
		return s.charAt(i) == QUOTES;
	}
	
	private boolean isSpace(String s, int i) {
		return s.charAt(i) == SPACES;
	}
	
	private boolean isEOL(String s, int i) {
		return i >= s.length();
	}
	
	private String substring(String s, int i, int j) {
		return s.substring(i, j);
	}
	
	private void addTo(List<String> command, String s, int i, int j) {
		command.add(substring(s, i, j));
	}
}
