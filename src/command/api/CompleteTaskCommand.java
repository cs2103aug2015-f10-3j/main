package command.api;

import java.util.ArrayList;
import task.api.TaskController;
import task.entity.Task;

public class CompleteTaskCommand extends Command {

    /*** Variables ***/
    private static final String KEYWORD_COMPLETE = "complete";
    
    private TaskController taskController;
    
	@Override
	public ArrayList<Task> execute() {
	    taskController = TaskController.getInstance();
	    if (hasOption(KEYWORD_COMPLETE)) {
	        return completeTask();
	    } else {
	        return null;
	    }
	}
	
	private ArrayList<Task> completeTask() {
	    ArrayList<Task> taskList = new ArrayList<Task>();
        boolean deleteTaskResult = false;
        int numOfValues = -1;
        
        numOfValues = getOption(KEYWORD_COMPLETE).getValuesCount();
        
        for (int i = 0; i < numOfValues; i++) {
            // Get params
            int taskId = -1;
            taskId = getOption(KEYWORD_COMPLETE).getIntegerValue(i);
            
            // Check if task exist
            Task task = taskController.getTask(taskId);
            
            // Update task
            if (task != null) {
                task.setComplete(true);
                deleteTaskResult = taskController.updateTask(task);
                
                if (deleteTaskResult == true) {
                    taskList.add(task);
                } else {
                    return taskList;
                }
            }
        }
        
        return taskList;
	}

	@Override
	public ArrayList<Task> undo() {
		// TODO Auto-generated method stub
		return null;
	}
}
