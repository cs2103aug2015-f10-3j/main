//@@author A0125528E
package main.paddletask.command.api;

import java.util.ArrayList;

import main.paddletask.task.api.TaskController;
import main.paddletask.task.entity.Task;

public class MoreCommand extends Command{
	
	/*** Variables ***/
    private TaskController taskController;
    
	/*** Methods ***/
    
	@Override
	/**
	 * This method is to execute the more command.
	 * It retrieves the attributes of the task selected
	 * and return the task to be displayed.
	 * 
	 * 
	 * @return taskList
	 * 				ArrayList of tasks
	 */
	public ArrayList<Task> execute() throws Exception {
		// TODO Auto-generated method stub
		ArrayList<Task> taskList = new ArrayList<Task>();
		int taskId = getOption("more").getIntegerValue();
		taskController = TaskController.getInstance();
		Task t = taskController.getTask(taskId);
		taskList.add(t);
		return taskList;
	}

	//@@author A0125473H-reused
	@Override
	public ArrayList<Task> undo() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
