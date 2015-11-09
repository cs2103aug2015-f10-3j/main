//@@author A0125528E
package main.paddletask.common.data;

public class HelpManual {
	
	/*** Variables ***/
	private static HelpManual manual = null;
	private static final String OVERALL_MSG = "PADDLETASK HELP MANUAL\n";
	private static final String ADD_MSG ="ADD COMMAND \n"
			+ "This command adds a new task into the system. Tasks' attributes are described as follows.\n"
			+ "add task name - Add new floating task \n"
			+ "\t- Eg. add buy clothing\n"
			+ "add task name [by] date=dd/mm/yyyy time=hh:mm - Add new deadline/timed task \n"
			+ "\t- Eg. add voting by 11/09/2015\n"
			+ "\t- Eg. add voting by 11/09/2015 11:00\n"
			+ "add task name [between] date=dd/mm/yyyy time=hh:mm [and] date=dd/mm/yyyy time=hh:mm - Add new period task\n"
			+ "\t- Eg. add study for exams between 12/09/2015 and 15/09/2015\n"
			+ "\t- Eg. add study for exams between 12/09/2015 11:00 and 15/09/2015 12:00";
	private static final String EDIT_MSG ="EDIT COMMAND \n"
			+ "edit task id [desc | -d] new task name "
			+ "[start | -s] new start date=hh:mm new start time=dd/mm/yyyy "
			+ "[end | -e] new end time=hh:mm new end date=dd/mm/yyyy "
			+ "- Modifies the task name/start date/start time/end date/end time specified by the task id to the new values. "
			+ "In order to get the task id, see the disclaimer under \"viewing tasks\"\n"
			+ "\t- Eg. edit 1 desc eat lunch\n"
			+ "\t- Eg. edit 1 -d eat lunch\n"
			+ "\t- Eg. edit 2 start 12:00 11/09/2015\n"
			+ "\t- Eg. edit 2 -s 12:00 11/09/2015\n"
			+ "\t- Eg. edit 3 end 12/09/2015 14:00\n"
			+ "\t- Eg. edit 3 -e 12/09/2015 14:00\n"
			+ "\t- Eg. edit 4 desc play piano start 12/09/2015 14:00 end 12/09/2015 16:00\n"
			+ "\t- Eg. edit 4 -d play piano -s 12/09/2015 14:00 -e 12/09/2015 16:00\n"
			+ "\t- Eg. edit 5 end 18:00\n"
			+ "\t- Eg. edit 5 -e 18:00";
	private static final String VIEW_MSG ="VIEW COMMAND \n"
			+ "view [all | floating | deadline | timed | complete] [today | tomorrow | week | month]"
			+ "- Display a list of tasks in categories of type and the deadline in the categories of time. \n"
			+ "\t- Eg. view all today\n"
			+ "\t- Eg. view deadline\n"
			+ "\t- Eg. view week\n"
			+ "\t- Eg. view complete";
	private static final String DELETE_MSG ="DELETE COMMAND \n"
			+ "delete task id/s... - Deletes the task as specified by the task id permanently. "
			+ "All related information will be deleted as well. You can choose to enter one or more tasks.\n"
			+ "\t- Eg. delete 1\n"
			+ "\t- Eg. delete 1 2\n"
			+ "delete [between] date=dd/mm/yyyy time=hh:mm [and] date=dd/mm/yyyy time=hh:mm - "
			+ "Deletes all tasks that falls between the period specified.\n"
			+ "\t- Eg. delete between 12/09/2015 16:00 and 15/09/2015 17:00";
	private static final String COMPLETE_MSG ="COMPLETE COMMAND \n"
			+ "complete task id/s - Mark the task as specified by the task id as completed, "
			+ "so that any reminders will be turned off. You can choose to enter one or more tasks. \n"
			+ "\t- Eg. complete 1"
			+ "\t- Eg. complete 1 2";
	private static final String SEARCH_MSG ="SEARCH COMMAND\n"
			+ "search | /s search sequence/dates \n"
			+ "Searches the whole Task list for Tasks that has description or dates that matches the search sequence or search dates.\n"
			+ "Multiple search sequence/dates are allowed. \n"
			+ "\t- Eg. search assignments \n"
			+ "\t- Eg. search assignments 12/12/2015\n"
			+ "\t- Eg. search assignments quizzes\n"
			+ "\t- Eg. search 12/12/2015 13/12/2015";

	private static final String UNDO_MSG ="UNDO COMMAND\n"
			+ "undo - To undo a previous operation that modifies the data file, such as setdirectory, \n"
			+ "add, edit, delete and complete, type undo. This will undo the effects of the previous operations.";
	private static final String REDO_MSG ="REDO COMMAND\n"
			+ "redo - To redo a previous undo, an undo command have to be done previously. \n"
			+ "If there is no undo command performed, this redo command will fail.";
	private static final String EXIT_MSG ="EXIT COMMAND \n"
			+ "exit - Close and exit PaddleTask";
	private static final String CLEAR_MSG ="CLEAR COMMAND \n"
			+ "clear - Clears output area of PaddleTask";
	private static final String HELP_MSG ="HELP COMMAND \n"
			+ "help command - Display help message for command. If no command is keyed, all help messages will show.\n"
			+ "\t- Eg. help\n"
			+ "\t- Eg. help add";
	private static final String MORE_MSG = "MORE COMMAND \n"
			+ "More task id - This command will display the selected task in full details for the user. \n"
			+ "\t- Eg. more 1\n"
			+ "\t- Eg. more 2";
	private static final String TAG_MSG = "TAG/UNTAG COMMAND \n"
			+ "To tag or untag an existing task, type tag/untag, followed by the running index of the task and the tags to add/remove.\n"
			+ "You may enter 1 or more tags. Note that the tags have to be preceded with a ‘#’. \n"
			+ "\t- Eg. tag 1 #yolo\n"
			+ "\t- Eg. untag 1 #yolo";
	private static final String SET_DIR_MSG = "SET DIRECTORY COMMAND \n"
			+ "set directory command - , you can change the directory of where "
			+ "the data file will be stored instead of using the default location. \n"
			+ "\t- Eg. setdirectory	C:\\Users\\Admin\\Desktop";
			
	private static final String ERROR_MSG = "No such command for Help found";
	private static final String NEXT_LINE = "\n";
	
	/*** Constructor ***/
	private HelpManual(){
	}
	
	/*** Methods ***/
	/**
	 * This method retrieves or create a HelpManual object
	 * and returns it.
	 * 
	 * @return manual
	 *            the singleton object of manual
	 * 
	 */
	public static HelpManual getInstance(){
		if(manual == null){
			manual = new HelpManual();
		}
		return manual;
	}
	
	/**
	 * This method uses the given command
	 * and match it according to everything
	 * in this manual. It will return
	 * the string statement for the help section
	 * of the selected section.
	 * 
	 * @return string
	 *            help section for the section
	 * 
	 */
	public String getHelp(String command) {
		if (command.equals(ParserConstants.COMMANDS.ADD.toString())) {
			return ADD_MSG;
		} else if (command.equals(ParserConstants.COMMANDS.EDIT.toString())) {
			return EDIT_MSG;
		} else if (command.equals(ParserConstants.COMMANDS.VIEW.toString())) {
			return VIEW_MSG;
		} else if (command.equals(ParserConstants.COMMANDS.DELETE.toString())) {
			return DELETE_MSG;
		} else if (command.equals(ParserConstants.COMMANDS.COMPLETE.toString())) {
            return COMPLETE_MSG;
        } else if (command.equals(ParserConstants.COMMANDS.SEARCH.toString())) {
			return SEARCH_MSG;
		} else if (command.equals(ParserConstants.COMMANDS.UNDO.toString())) {
			return UNDO_MSG;
		} else if (command.equals(ParserConstants.COMMANDS.REDO.toString())) {
			return REDO_MSG;
		} else if (command.equals(ParserConstants.COMMANDS.CLEAR.toString())) {
			return CLEAR_MSG;
		} else if (command.equals(ParserConstants.COMMANDS.HELP.toString())) {
			return HELP_MSG;
		} else if (command.equals(ParserConstants.COMMANDS.TAG.toString())|| command.equals(ParserConstants.COMMANDS.UNTAG.toString())) {
			return TAG_MSG;
		} else if (command.equals(ParserConstants.COMMANDS.MORE.toString())) {
			return MORE_MSG;
		} else if (command.equals(ParserConstants.COMMANDS.SETDIRECTORY.toString())) {
			return SET_DIR_MSG;
		} else if (command.equals(ParserConstants.COMMANDS.EXIT.toString())) {
			return EXIT_MSG;
		} else if (command.equals(null)|| command.equals("") || command.equals("all") ) {
			return OVERALL_MSG + NEXT_LINE + ADD_MSG + NEXT_LINE + EDIT_MSG + NEXT_LINE + VIEW_MSG + NEXT_LINE + 
					DELETE_MSG + NEXT_LINE + COMPLETE_MSG + NEXT_LINE + SEARCH_MSG + NEXT_LINE + UNDO_MSG + NEXT_LINE 
					+ REDO_MSG + NEXT_LINE + CLEAR_MSG + NEXT_LINE + HELP_MSG + TAG_MSG +
					MORE_MSG + SET_DIR_MSG + NEXT_LINE + EXIT_MSG;
		} else {
			return ERROR_MSG;
		}

	}

	
	
}
