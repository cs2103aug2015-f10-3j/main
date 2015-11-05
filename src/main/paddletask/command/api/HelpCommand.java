//@@author A0125528E
package main.paddletask.command.api;

import java.util.ArrayList;

import main.paddletask.common.data.HelpManual;
import main.paddletask.task.entity.Task;

public class HelpCommand extends Command{
	
	/*** Variables ***/
	private static final String KEYWORD_HELP = "help";

	private String commandType = "";
	private String helpComments = "";

	/*** Methods ***/
	/**
	 * This method is to execute the help command.
	 * It retrieves the necessary help statements from
	 * the option of the help command executed.
	 * UI:MainFrame observer will be notified 
	 * to display the necessary outputs.
	 * 
	 * @return taskList
	 * 				ArrayList of tasks
	 */
	@Override
	public ArrayList<Task> execute() {
		// TODO Auto-generated method stub
		HelpManual manual = HelpManual.getInstance();
		if(hasOption(KEYWORD_HELP)){
			if(getOption(KEYWORD_HELP)==null){
				commandType = "";
			} else{
				commandType = getOption(KEYWORD_HELP).getStringValue();
			}
		} else{
			assert false;
		}
		//System.out.println("commandtype = " + commandType);
		helpComments = manual.getHelp(commandType.trim());
		//System.out.println("help comments = " + helpComments);
		setChanged();
		notifyObservers(helpComments);
		return null;
	}

	/**
	 * This method is to get help comments of this instance
	 * of command.
	 * 
	 * @return helpComments
	 * 				String of help statements
	 */
	public String getHelpComments(){
		return helpComments;
	}

	//@@author A0125473H-reused
	@Override
	public ArrayList<Task> undo() {
		// TODO Auto-generated method stub
		return null;
	}

}
