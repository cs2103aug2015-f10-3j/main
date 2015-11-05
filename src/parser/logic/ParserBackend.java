package parser.logic;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.joestelmach.natty.DateGroup;

import common.data.ParserConstants;

class ParserBackend implements ParserConstants {

	public static final String EMPTY_STRING = "";
	public static final String SPACE = " ";
	public static final char SPACES = ' ';
	public static final char QUOTES = '"';
	
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
	
	
	protected static final Logger LOGGER = Logger.getLogger(ParseLogic.class.getName());
	private static HashMap<String, String> shortHandMap = new HashMap<String, String>();
	private static HashMap<COMMAND_TYPE, EnumMap<OPTIONS, TYPE>> optionsMap = new HashMap<COMMAND_TYPE, EnumMap<OPTIONS, TYPE>>();
	protected static com.joestelmach.natty.Parser dateParser = new com.joestelmach.natty.Parser();
	
	public ParserBackend() {
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
		setupHelpOption();
		setupSetDirectoryOption();
		setupTagOption();
		setupUntagOption();
		setupMoreOption();
		setupTrivialOptions();
		setUpShortHandMapping();
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
		shortHandMap.put(OPTIONS.SETDIRECTORY_SHORT.toString(), OPTIONS.SETDIRECTORY.toString());
		shortHandMap.put(OPTIONS.MORE_SHORT.toString(), OPTIONS.MORE.toString());
		shortHandMap.put(OPTIONS.TAG_SHORT.toString(), OPTIONS.TAG.toString());
		shortHandMap.put(OPTIONS.UNTAG_SHORT.toString(), OPTIONS.UNTAG.toString());
		shortHandMap.put(OPTIONS.PRIORITY_SHORT.toString(), OPTIONS.PRIORITY.toString());
	}
	
	private void setupTrivialOptions() {
		clearOptions.put(OPTIONS.CLEAR, TYPE.NONE);
		clearOptions.put(OPTIONS.CLEAR_SHORT, TYPE.NONE);
		optionsMap.put(COMMAND_TYPE.CLEAR, clearOptions);
		exitOptions.put(OPTIONS.EXIT, TYPE.NONE);
		exitOptions.put(OPTIONS.EXIT_SHORT, TYPE.NONE);
		optionsMap.put(COMMAND_TYPE.EXIT, exitOptions);
		redoOptions.put(OPTIONS.REDO, TYPE.NONE);
		redoOptions.put(OPTIONS.REDO_SHORT, TYPE.NONE);
		optionsMap.put(COMMAND_TYPE.REDO, redoOptions);
		undoOptions.put(OPTIONS.UNDO, TYPE.NONE);
		undoOptions.put(OPTIONS.UNDO_SHORT, TYPE.NONE);
		optionsMap.put(COMMAND_TYPE.UNDO, undoOptions);
	}

	private void setupMoreOption() {
		moreOptions.put(OPTIONS.MORE, TYPE.INTEGER);
		moreOptions.put(OPTIONS.MORE_SHORT, TYPE.INTEGER);
		optionsMap.put(COMMAND_TYPE.MORE, moreOptions);
	}

	private void setupUntagOption() {
		untagOptions.put(OPTIONS.UNTAG, TYPE.INTEGER);
		untagOptions.put(OPTIONS.UNTAG_SHORT, TYPE.INTEGER);
		optionsMap.put(COMMAND_TYPE.UNTAG, untagOptions);
	}

	private void setupTagOption() {
		tagOptions.put(OPTIONS.TAG, TYPE.INTEGER);
		tagOptions.put(OPTIONS.TAG_SHORT, TYPE.INTEGER);
		optionsMap.put(COMMAND_TYPE.TAG, tagOptions);
	}

	private void setupSetDirectoryOption() {
		setDirectoryOptions.put(OPTIONS.SETDIRECTORY, TYPE.STRING);
		setDirectoryOptions.put(OPTIONS.SETDIRECTORY_SHORT, TYPE.STRING);
		optionsMap.put(COMMAND_TYPE.SETDIRECTORY, setDirectoryOptions);
	}

	private void setupHelpOption() {
		helpOptions.put(OPTIONS.HELP, TYPE.STRING_OPT);
		helpOptions.put(OPTIONS.HELP_SHORT, TYPE.STRING_OPT);
		optionsMap.put(COMMAND_TYPE.HELP, helpOptions);
	}

	private void setupSearchOption() {
		searchOptions.put(OPTIONS.SEARCH, TYPE.STRING_ARRAY);
		searchOptions.put(OPTIONS.SEARCH_SHORT, TYPE.STRING_ARRAY);
		optionsMap.put(COMMAND_TYPE.SEARCH, searchOptions);
	}

	private void setupCompleteOption() {
		completeOptions.put(OPTIONS.COMPLETE, TYPE.INTEGER_ARRAY);
		completeOptions.put(OPTIONS.COMPLETE_SHORT, TYPE.INTEGER_ARRAY);
		optionsMap.put(COMMAND_TYPE.COMPLETE, completeOptions);
	}

	private void setupDeleteOption() {
		deleteOptions.put(OPTIONS.DELETE, TYPE.INTEGER_ARRAY);
		deleteOptions.put(OPTIONS.DELETE_SHORT, TYPE.INTEGER_ARRAY);
		optionsMap.put(COMMAND_TYPE.DELETE, deleteOptions);
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
		editOptions.put(OPTIONS.PRIORITY, TYPE.INTEGER);
		
		editOptions.put(OPTIONS.EDIT_SHORT, TYPE.INTEGER);
		editOptions.put(OPTIONS.DESC_SHORT, TYPE.STRING);
		editOptions.put(OPTIONS.BY_SHORT, TYPE.DATE);
		editOptions.put(OPTIONS.REMIND_SHORT, TYPE.DATE);
		editOptions.put(OPTIONS.BETWEEN_SHORT, TYPE.DATE);
		editOptions.put(OPTIONS.AND_SHORT, TYPE.DATE);
		editOptions.put(OPTIONS.START_SHORT, TYPE.DATE);
		editOptions.put(OPTIONS.END_SHORT, TYPE.DATE);
		editOptions.put(OPTIONS.PRIORITY_SHORT, TYPE.INTEGER);
		optionsMap.put(COMMAND_TYPE.EDIT, editOptions);
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
		optionsMap.put(COMMAND_TYPE.VIEW, viewOptions);
	}

	private void setupAddOption() {
		addOptions.put(OPTIONS.ADD, TYPE.STRING);
		addOptions.put(OPTIONS.BY, TYPE.DATE);
		addOptions.put(OPTIONS.REMIND, TYPE.DATE);
		addOptions.put(OPTIONS.BETWEEN, TYPE.DATE);
		addOptions.put(OPTIONS.AND, TYPE.DATE);
		addOptions.put(OPTIONS.START, TYPE.DATE);
		addOptions.put(OPTIONS.END, TYPE.DATE);
		addOptions.put(OPTIONS.PRIORITY, TYPE.INTEGER);

		addOptions.put(OPTIONS.ADD_SHORT, TYPE.STRING);
		addOptions.put(OPTIONS.BY_SHORT, TYPE.DATE);
		addOptions.put(OPTIONS.REMIND_SHORT, TYPE.DATE);
		addOptions.put(OPTIONS.BETWEEN_SHORT, TYPE.DATE);
		addOptions.put(OPTIONS.AND_SHORT, TYPE.DATE);
		addOptions.put(OPTIONS.START_SHORT, TYPE.DATE);
		addOptions.put(OPTIONS.END_SHORT, TYPE.DATE);
		addOptions.put(OPTIONS.PRIORITY_SHORT, TYPE.INTEGER);
		optionsMap.put(COMMAND_TYPE.ADD, addOptions);
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
	
	protected List<LocalDateTime> parseDates(String commandString) {
		List<DateGroup> groups = dateParser.parse(commandString);
		List<LocalDateTime> parsedDates = new ArrayList<LocalDateTime>();
		for(DateGroup group : groups) {
			List<Date> dates = group.getDates();
			while (!dates.isEmpty()) {
				parsedDates.add(LocalDateTime.ofInstant(dates.remove(0).toInstant(), ZoneId.systemDefault()));
			}
		}
		return parsedDates;
	}
	
	public Deque<String> breakDownCommand(String userCommand) {
		LOGGER.info("Attempt to breakdown user input into chunks of words.");
		assert(userCommand != null && userCommand.length() > 0);
		return preprocessUserCommand(userCommand);
	}
	
	protected String getMainCommand(String userCommand) {
		LOGGER.log(Level.INFO, "Get first word of user input: {0}", userCommand);
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
		if (!optionsMap.containsKey(commandType)) {
			LOGGER.severe("commandType is corrupted and not within expectations");
			throw new Error("Corrupted commandType");
		}
		return optionsMap.get(commandType);
	}
	
	private Deque<String> preprocessUserCommand(String userCommand) {
		Deque<String> processedCommand = new ArrayDeque<String>();
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
					addTo(processedCommand, userCommand, head, i);
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
	
	private void addTo(Deque<String> command, String s, int i, int j) {
		command.push(substring(s, i, j));
	}
}
