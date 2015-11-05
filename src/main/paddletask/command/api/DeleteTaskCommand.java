//@@author A0126332R
package main.paddletask.command.api;

import java.time.LocalDateTime;
import java.util.ArrayList;

import main.paddletask.task.api.TaskController;
import main.paddletask.task.entity.DeadlineTask;
import main.paddletask.task.entity.Task;
import main.paddletask.task.entity.TimedTask;

public class DeleteTaskCommand extends Command {

    /*** Variables ***/
    private static final String KEYWORD_DELETE = "delete";
    private static final String KEYWORD_BETWEEN = "between";
    private static final String KEYWORD_AND = "and";
    private static ArrayList<Task> _deletedTasks;
    
    private TaskController taskController;
    
    /*** Methods ***/
    /**
     * This method delete Tasks based on user input
     * 
     * @return an ArrayList of Task objects that contains the deleted Task
     *         objects
     */
    @Override
    public ArrayList<Task> execute() {
        taskController = TaskController.getInstance();
        if (hasOption(KEYWORD_DELETE) && getOption(KEYWORD_DELETE) != null) {
            _deletedTasks = deleteByTaskId();
            return _deletedTasks;
        } else if (hasOption(KEYWORD_BETWEEN) && hasOption(KEYWORD_AND)) {
            _deletedTasks = deleteByPeriod();
            return _deletedTasks;
        } else {
            return null;
        }
    }
    
    /**
     * This method delete Tasks based on taskId
     * 
     * @return an ArrayList of Task objects that contains the deleted Task
     *         objects
     */
    private ArrayList<Task> deleteByTaskId() {
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
                    break;
                }
            }
        }
        
        return taskList;
    }
    
    /**
     * This method delete Tasks based on a user defined period of time
     * 
     * @return an ArrayList of Task objects that contains the deleted Task
     *         objects
     */
    private ArrayList<Task> deleteByPeriod() {
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
                break;
            }
        }
        
        return taskList;
    }

    /**
     * This method retrieves a Task based on a user defined period of time
     * 
     * @param  start  start of the time period
     * @param  end    end of the time period
     * @return        an ArrayList of Task objects that is within the period
     */
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

    /**
     * This method reverse the previous execute() of the previous
     * DeleteTaskCommand
     * 
     * @return an ArrayList of Task objects that contains the deleted Task
     *         objects
     */
    @Override
    public ArrayList<Task> undo() {
        if (_deletedTasks != null) {
            for (Task task : _deletedTasks) {
                TaskController.getInstance().addTask(task);
            }
        }
        return _deletedTasks;
    }

}
