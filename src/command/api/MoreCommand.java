package command.api;

import java.util.ArrayList;

import task.api.TaskController;
import task.entity.Task;

public class MoreCommand extends Command{

    private TaskController taskController;
    
	@Override
	public ArrayList<Task> execute() throws Exception {
		// TODO Auto-generated method stub
		ArrayList<Task> taskList = new ArrayList<Task>();
		int taskId = getOption("more").getIntegerValue();
		taskController = TaskController.getInstance();
		Task t = taskController.getTask(taskId);
		taskList.add(t);
		return taskList;
	}

	@Override
	public ArrayList<Task> undo() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
