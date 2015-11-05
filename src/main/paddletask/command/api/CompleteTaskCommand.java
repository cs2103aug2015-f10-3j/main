//@@author A0126332R
package main.paddletask.command.api;

import java.util.ArrayList;
import main.paddletask.task.api.TaskController;
import main.paddletask.task.entity.Task;

public class CompleteTaskCommand extends Command {

    /*** Variables ***/
    private static final String KEYWORD_COMPLETE = "complete";
    private static ArrayList<Task> completedTaskList;
    
    private TaskController taskController;
    
    /*** Methods ***/
    /**
     * This method complete Tasks based on user input
     * 
     * @return an ArrayList of Task objects that contains the completed Task
     *         objects
     */
    @Override
    public ArrayList<Task> execute() {
        taskController = TaskController.getInstance();
        if (hasOption(KEYWORD_COMPLETE)) {
            completedTaskList = completeTask();
            return completedTaskList;
        } else {
            return null;
        }
    }
    
    /**
     * This method complete Tasks based on taskId
     * 
     * @return an ArrayList of Task objects that contains the completed Task
     *         objects
     */
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

    /**
     * This method reverse the previous execute() of the previous
     * CompleteTaskCommand
     * 
     * @return an ArrayList of Task objects that contains the completed Task
     *         objects
     */
    @Override
    public ArrayList<Task> undo() {
        if (completedTaskList != null) {
            for (Task task : completedTaskList) {
                task.setComplete(!task.isComplete());
                TaskController.getInstance().updateTask(task);
            }
        }
        return completedTaskList;
    }
}
