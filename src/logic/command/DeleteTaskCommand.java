package logic.command;

import java.util.ArrayList;

import commons.Pair;
import logic.TaskController;
import logic.data.Task;

public class DeleteTaskCommand extends Command {

    /*** Variables ***/
    private static final String KEYWORD_DELETE = "delete";
    //private static final String KEYWORD_BETWEEN = "between";
    //private static final String KEYWORD_AND = "and";
    
    private TaskController taskController;
    
	@Override
	public Pair<ArrayList<Task>,Boolean> execute() {
	    ArrayList<Task> taskList = new ArrayList<Task>();
	    boolean deleteTaskResult = false;
	    int numOfValues = -1;
	    
	    taskController = TaskController.getInstance();
	    numOfValues = getOption(KEYWORD_DELETE).getValuesCount();
	    
	    for (int i = 0; i < numOfValues; i++) {
	        // Get params
	        int taskId = -1;
	        taskId = getOption("delete").getIntegerValue(i);
	        
	        // Check if task exist
	        Task task = taskController.getTask(taskId);
	        
	        // Delete task
	        if (task != null) {
	            deleteTaskResult = taskController.deleteTask(taskId);
	            if (deleteTaskResult = true) {
	                taskList.add(task);
	            }
	        }
	    }
	    
		return new Pair<ArrayList<Task>, Boolean>(taskList, deleteTaskResult);
	}

}
