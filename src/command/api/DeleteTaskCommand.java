package command.api;

import java.time.LocalDateTime;
import java.util.ArrayList;

import common.data.Pair;
import common.util.DateTimeHelper;
import task.api.TaskController;
import task.entity.DeadlineTask;
import task.entity.Task;
import task.entity.TimedTask;

public class DeleteTaskCommand extends Command {

    /*** Variables ***/
    private static final String KEYWORD_DELETE = "delete";
    private static final String KEYWORD_BETWEEN = "between";
    private static final String KEYWORD_AND = "and";
    
    private TaskController taskController;
    
	@Override
	public Pair<ArrayList<Task>,Boolean> execute() {
	    taskController = TaskController.getInstance();
	    if (hasOption(KEYWORD_DELETE) && getOption(KEYWORD_DELETE) != null) {
	        return deleteByTaskId();
	    } else if (hasOption(KEYWORD_BETWEEN) && hasOption(KEYWORD_AND)) {
	        return deleteByPeriod();
	    } else {
	        return new Pair<ArrayList<Task>, Boolean>(null, false);
	    }
	}
	
	private Pair<ArrayList<Task>,Boolean> deleteByTaskId() {
	    ArrayList<Task> taskList = new ArrayList<Task>();
        boolean deleteTaskResult = false;
        int numOfValues = -1;
        
        numOfValues = getOption(KEYWORD_DELETE).getValuesCount();
        
        for (int i = 0; i < numOfValues; i++) {
            // Get params
            int taskId = -1;
            taskId = getOption(KEYWORD_DELETE).getIntegerValue(i);
            
            // Check if task exist
            Task task = taskController.getTask(taskId);
            
            // Delete task
            if (task != null) {
                deleteTaskResult = taskController.deleteTask(taskId);
                if (deleteTaskResult == true) {
                    taskList.add(task);
                } else {
                    return new Pair<ArrayList<Task>, Boolean>(taskList, deleteTaskResult);
                }
            }
        }
        
        return new Pair<ArrayList<Task>, Boolean>(taskList, deleteTaskResult);
	}
	
	private Pair<ArrayList<Task>,Boolean> deleteByPeriod() {
	    ArrayList<Task> taskList = new ArrayList<Task>();
        boolean deleteTaskResult = false;
        
        // Get period
        LocalDateTime start = getOption(KEYWORD_BETWEEN).getDateValue();
        LocalDateTime end = getOption(KEYWORD_AND).getDateValue();
        
        // Get task within period
        ArrayList<Task> filteredtaskList = getTaskByPeriod(start, end);
        
        // Delete task
        for (int i = 0; i < filteredtaskList.size(); i++) {
            Task task = filteredtaskList.get(i);
            deleteTaskResult = taskController.deleteTask(task.getTaskId());
            if (deleteTaskResult == true) {
                taskList.add(task);
            } else {
                return new Pair<ArrayList<Task>, Boolean>(taskList, deleteTaskResult);
            }
        }
        
        return new Pair<ArrayList<Task>,Boolean>(taskList, true);
	}

    private ArrayList<Task> getTaskByPeriod(LocalDateTime start, LocalDateTime end) {
        ArrayList<Task> allTask = taskController.getTask();
        ArrayList<Task> filteredTask = new ArrayList<Task>();
        
        for (Task task : allTask) {
            switch (task.getType()) {
                case FLOATING:
                    break;
                case TIMED:
                    if ((((TimedTask) task).getStart().compareTo(start) >= 0) &&
                            (((TimedTask) task).getEnd().compareTo(end) <= 0)) {
                        filteredTask.add(task);
                    }
                    break;
                case DEADLINE:
                    if ((((DeadlineTask) task).getEnd().compareTo(start) >= 0) && 
                            (((DeadlineTask) task).getEnd().compareTo(end) <= 0)) {
                        filteredTask.add(task);
                    }
                    break;
                default:
                    break;
            }
        }
        
        return filteredTask;
    }

}
