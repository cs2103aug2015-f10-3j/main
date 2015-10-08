package logic.command;

import java.time.LocalDateTime;
import java.util.ArrayList;

import commons.DateTimeCommon;
import commons.Pair;
import logic.TaskController;
import logic.data.DeadlineTask;
import logic.data.Task;
import logic.data.TimedTask;

public class CompleteTaskCommand extends Command {

    /*** Variables ***/
    private static final String KEYWORD_COMPLETE = "complete";
    
    private TaskController taskController;
    
	@Override
	public Pair<ArrayList<Task>,Boolean> execute() {
	    taskController = TaskController.getInstance();
	    if (hasOption(KEYWORD_COMPLETE)) {
	        return completeTask();
	    } else {
	        return new Pair<ArrayList<Task>, Boolean>(null, false);
	    }
	}
	
	private Pair<ArrayList<Task>,Boolean> completeTask() {
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
                    return new Pair<ArrayList<Task>, Boolean>(taskList, deleteTaskResult);
                }
            }
        }
        
        return new Pair<ArrayList<Task>, Boolean>(taskList, deleteTaskResult);
	}
}