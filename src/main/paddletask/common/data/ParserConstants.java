//@@author A0125473H
package main.paddletask.common.data;
public interface ParserConstants {

	/*** Main command enums ***/
	public static enum COMMAND_TYPE {
		ADD, VIEW, EDIT, DELETE, 
		COMPLETE, SEARCH, UNDO, REDO, 
		INVALID, EXIT, CLEAR, HELP, SETDIRECTORY,
		MORE, TAG, UNTAG
	}

	public static enum COMMANDS {
		ADD("add"), VIEW("view"), EDIT("edit"), DELETE("delete"), 
		COMPLETE("complete"), SEARCH("search"), UNDO("undo"), 
		REDO("redo"), EXIT("exit"), CLEAR("clear"), HELP("help"),
		SETDIRECTORY("setdirectory"), MORE("more"), TAG("tag"), 
		UNTAG("untag"),

		ADD_SHORT("/a"), VIEW_SHORT("/v"), EDIT_SHORT("/e"), DELETE_SHORT("/d"), 
		COMPLETE_SHORT("/c"), SEARCH_SHORT("/s"), UNDO_SHORT("/u"), 
		REDO_SHORT("/r"), EXIT_SHORT("/ex"), CLEAR_SHORT("/cl"), HELP_SHORT("/h"),
		SETDIRECTORY_SHORT("/sd"), MORE_SHORT("/m"), TAG_SHORT("/t"), UNTAG_SHORT("/ut");

		private final String commandText;
		
		private COMMANDS(final String commandText) {
			this.commandText = commandText;
		}

		@Override
		public String toString() {
			return commandText;
		}
	}

	/*** Sub command enums ***/
	public static enum OPTIONS {
		ADD("add"), VIEW("view"), EDIT("edit"), DELETE("delete"), 
		COMPLETE("complete"), SEARCH("search"), BY("by"), UNDO("undo"), 
		REDO("redo"), REMIND("remind"), CLEAR("clear"), EXIT("exit"), 
		BETWEEN("between"), AND("and"), DESC("desc"), STARTDATE("sdate"), 
		STARTTIME("stime"), ENDDATE("edate"), ENDTIME("etime"),
		ALL("all"), TODAY("today"), TOMORROW("tomorrow"), WEEK("week"), 
		MONTH("month"), HELP("help"), SETDIRECTORY("setdirectory"), MORE("more"),
		TAG("tag"), UNTAG("untag"), PRIORITY("priority"), REPEAT("repeat"),
		
		ADD_SHORT("/a"), VIEW_SHORT("/v"), EDIT_SHORT("/e"), DELETE_SHORT("/d"), 
		COMPLETE_SHORT("/c"), SEARCH_SHORT("/s"), BY_SHORT("-b"), UNDO_SHORT("/u"), 
		REDO_SHORT("/r"), REMIND_SHORT("-r"), CLEAR_SHORT("/cl"), EXIT_SHORT("/ex"), 
		BETWEEN_SHORT("-bt"), AND_SHORT("-a"), DESC_SHORT("-d"), STARTDATE_SHORT("-sD"), 
		STARTTIME_SHORT("-sT"), ENDDATE_SHORT("-eD"), ENDTIME_SHORT("-eT"), 
		ALL_SHORT("-a"), VIEW_COMPLETE_SHORT("-c"), TODAY_SHORT("-td"), TOMORROW_SHORT("-tm"), WEEK_SHORT("-w"), 
		MONTH_SHORT("-m"), HELP_SHORT("/h"), SETDIRECTORY_SHORT("/sd"), MORE_SHORT("/m"),
		TAG_SHORT("/t"), UNTAG_SHORT("/ut"), PRIORITY_SHORT("-p"), HASHTAG("#"), REPEAT_SHORT("-re");

		private final String optionText;
		
		private OPTIONS(final String commandText) {
			this.optionText = commandText;
		}

		@Override
		public String toString() {
			return optionText;
		}
	}
	
	/*** Helper enums ***/
	public static enum DAY {
		DAY("daily"), MONTH("monthly"), WEEK("weekly"), YEAR("yearly");

		private final String commandText;
		
		private DAY(final String commandText) {
			this.commandText = commandText;
		}

		@Override
		public String toString() {
			return commandText;
		}
	}
	
	public static enum TYPE {
		STRING, STRING_ARRAY, INTEGER, INTEGER_ARRAY, DATE,
		STRING_OPT, STRING_ARRAY_OPT, INTEGER_OPT, INTEGER_ARRAY_OPT,
		DATE_OPT, DAY, NONE;
	}

	/*** used for suggestions ***/
	public static enum ADD_OPTIONS {
		BY("by"), REMIND("remind"), BETWEEN("between"), AND("and"), 
		PRIORITY("priority"), EVERY("every"),
		
		BY_SHORT("-b"), REMIND_SHORT("-r"), BETWEEN_SHORT("-bt"), AND_SHORT("-a"), 
		PRIORITY_SHORT("-p"), EVERY_SHORT("-re");

		private final String commandText;
		
		private ADD_OPTIONS(final String commandText) {
			this.commandText = commandText;
		}

		@Override
		public String toString() {
			return commandText;
		}
	}
	
	public static enum VIEW_OPTIONS {
		COMPLETE("complete"), ALL("all"), TODAY("today"), TOMORROW("tomorrow"), WEEK("week"), 
		MONTH("month"), 
		
		COMPLETE_SHORT("-c"), ALL_SHORT("-a"), TODAY_SHORT("-tD"), TOMORROW_SHORT("-tM"), WEEK_SHORT("-w"), 
		MONTH_SHORT("-m");

		private final String commandText;
		
		private VIEW_OPTIONS(final String commandText) {
			this.commandText = commandText;
		}

		@Override
		public String toString() {
			return commandText;
		}
	}
	
	public static enum EDIT_OPTIONS {
		REMIND("remind"), STARTDATE("sdate"), STARTTIME("stime"), DESC("desc"),
		ENDDATE("edate"), ENDTIME("etime"), PRIORITY("priority"), REPEAT("repeat"),
		
		REMIND_SHORT("-r"), DESC_SHORT("-d"), STARTDATE_SHORT("-sD"), 
		STARTTIME_SHORT("-sT"), ENDDATE_SHORT("-eD"), ENDTIME_SHORT("-eT"), 
		PRIORITY_SHORT("-p"), REPEAT_SHORT("-re");

		private final String commandText;
		
		private EDIT_OPTIONS(final String commandText) {
			this.commandText = commandText;
		}

		@Override
		public String toString() {
			return commandText;
		}
	}
	
	public static enum TAGUNTAG_OPTIONS {
		HASHTAG("#");

		private final String commandText;
		
		private TAGUNTAG_OPTIONS(final String commandText) {
			this.commandText = commandText;
		}

		@Override
		public String toString() {
			return commandText;
		}
	}
}
