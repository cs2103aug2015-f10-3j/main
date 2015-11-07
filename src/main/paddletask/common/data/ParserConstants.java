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
		BETWEEN("between"), AND("and"), DESC("desc"), START("start"), 
		END("end"), ALL("all"), FLOATING("floating"), DEADLINE("deadline"), 
		TIMED("timed"), TODAY("today"), TOMORROW("tomorrow"), WEEK("week"), 
		MONTH("month"), HELP("help"), SETDIRECTORY("setdirectory"), MORE("more"),
		TAG("tag"), UNTAG("untag"), PRIORITY("priority"), EVERY("every"),
		
		ADD_SHORT("/a"), VIEW_SHORT("/v"), EDIT_SHORT("/e"), DELETE_SHORT("/d"), 
		COMPLETE_SHORT("/c"), SEARCH_SHORT("/s"), BY_SHORT("-b"), UNDO_SHORT("/u"), 
		REDO_SHORT("/r"), REMIND_SHORT("-r"), CLEAR_SHORT("/cl"), EXIT_SHORT("/ex"), 
		BETWEEN_SHORT("-bt"), AND_SHORT("-a"), DESC_SHORT("-d"), START_SHORT("-s"), 
		END_SHORT("-e"), ALL_SHORT("-ta"), FLOATING_SHORT("-tf"), DEADLINE_SHORT("-td"), 
		TIMED_SHORT("-tt"), TODAY_SHORT("-pt"), TOMORROW_SHORT("-ptm"), WEEK_SHORT("-pw"), 
		MONTH_SHORT("-pm"), HELP_SHORT("/h"), SETDIRECTORY_SHORT("/sd"), MORE_SHORT("/m"),
		TAG_SHORT("/t"), UNTAG_SHORT("/ut"), PRIORITY_SHORT("-p"), HASHTAG("#"), EVERY_SHORT("-re");

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
		DAY("day"), MONTH("month"), WEEK("week"), YEAR("year");

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
}
