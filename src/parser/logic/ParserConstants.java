package parser.logic;

import java.util.EnumMap;

public interface ParserConstants {
	
	public static final String SPACE_REGEX = "\\s+";
	public static final String EMPTY_STRING = "";
	public static final String SPACE = " ";
	
	public static final boolean OPTIONAL = true;
	public static final boolean NOT_OPTIONAL = false;

	public static enum COMMAND_TYPE {
		ADD, VIEW, EDIT, DELETE, 
		COMPLETE, SEARCH, UNDO, REDO, 
		INVALID, EXIT, CLEAR, HELP
	}

	public static enum COMMANDS {
		ADD("add"), VIEW("view"), EDIT("edit"), DELETE("delete"), 
		COMPLETE("complete"), SEARCH("search"), UNDO("undo"), 
		REDO("redo"), EXIT("exit"), CLEAR("clear"), HELP("help");

		private final String commandText;
		
		private COMMANDS(final String commandText) {
			this.commandText = commandText;
		}

		@Override
		public String toString() {
			return commandText;
		}
	}

	public static enum OPTIONS {
		ADD("add"), VIEW("view"), EDIT("edit"), DELETE("delete"), 
		COMPLETE("complete"), SEARCH("search"), BY("by"), UNDO("undo"), 
		REDO("redo"), REMIND("remind"), CLEAR("clear"), EXIT("exit"), 
		BETWEEN("between"), AND("and"), DESC("desc"), START("start"), 
		END("end"), ALL("all"), FLOATING("floating"), DEADLINE("deadline"), 
		TIMED("timed"), TODAY("today"), TOMORROW("tomorrow"), WEEK("week"), 
		MONTH("month"), HELP("help");

		private final String optionText;
		
		private OPTIONS(final String commandText) {
			this.optionText = commandText;
		}

		@Override
		public String toString() {
			return optionText;
		}
	}
	
	public static enum TYPE {
		STRING, STRING_ARRAY, INTEGER, INTEGER_ARRAY, DATE,
		STRING_OPT, STRING_ARRAY_OPT, INTEGER_OPT, INTEGER_ARRAY_OPT, DATE_OPT, NONE;
	}
	
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
}
