package task.api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import org.w3c.dom.Document;

import storage.api.StorageController;
import task.entity.DeadlineTask;
import task.entity.Task;
import task.entity.Task.TASK_TYPE;
import task.entity.TimedTask;

public class TaskController {
    /*** Variables ***/
    protected StorageController sController;
    private static TaskController _thisInstance;

    /*** Constructor ***/
    private TaskController() {
    	sController = StorageController.getInstance();
        Task.setTaskList(sController.readTask());
    }
    
    public static synchronized TaskController getInstance( ) {
        if (_thisInstance == null)
        	_thisInstance = new TaskController();
        return _thisInstance;
     }

    /*** Methods ***/
    /**
     * This method adds the Task object to file
     * 
     * @param  task  Task entry to be added
     * @return       <code>true</code> if the task is successfully added;
     *               <code>false</code> otherwise.
     */
    public boolean addTask(Task task) {
        // Get and set the smallest available tasId
        int taskId = getAvailableTaskId();
        task.setTaskId(taskId);

        // Add this task to our arraylist
        ArrayList<Task> tasks = Task.getTaskList();
        tasks.add(task);
        Task.setTaskList(tasks);

        // Store to file
        Document doc = sController.parseTask(tasks);
        boolean isSuccessful = sController.writeXml(doc);

        return isSuccessful;
    }

    /**
     * This method returns a list of all tasks
     * 
     * @return      an ArrayList of Tasks
     */
    public ArrayList<Task> getTask() {
        ArrayList<Task> tasks = Task.getTaskList();
        return tasks;
    }

    /**
     * This method returns a list of the specified tasks
     * 
     * @param  type  type of the Tasks to be selected
     * @return       an ArrayList of Tasks
     */
    public ArrayList<Task> getTask(TASK_TYPE type) {
        ArrayList<Task> tasks = Task.getTaskList();
        
        if (type == TASK_TYPE.ANY) {
        	return tasks;
        }
        
        ArrayList<Task> filteredTasks = new ArrayList<Task>();

        for (Task task : tasks) {
            if (task.getType() == type) {
                filteredTasks.add(task);
            }
        }

        return filteredTasks;
    }

    /**
     * This method returns a Task object
     * 
     * @param  taskId  the unique identifier of the Task object
     * @return         the specified Task object
     */
    public Task getTask(int taskId) {
        ArrayList<Task> tasks = Task.getTaskList();
        for (Task task : tasks) {
            if (task.getTaskId() == taskId) {
                return task;
            }
        }
        return null;
    }

    /**
     * This method updates the Task object to file
     * 
     * @param  task  Task entry to be updated
     * @return       <code>true</code> if the task is successfully updated;
     *               <code>false</code> otherwise.
     */
    public boolean updateTask(Task task) {
        ArrayList<Task> tasks = Task.getTaskList();
        boolean isFound = false;

        // Find task
        for (int i = 0; i < tasks.size(); i++) {
            Task existingTask = tasks.get(i);
            if (existingTask.getTaskId() == task.getTaskId()) {
                tasks.set(i, task);
                isFound = true;
                break;
            }
        }

        // Return if task is not found
        if (!isFound) {
            return false;
        }

        // Store to file
        Task.setTaskList(tasks);
        Document doc = sController.parseTask(tasks);
        boolean isSuccessful = sController.writeXml(doc);

        return isSuccessful;
    }

    /**
     * This method delete the Task from file
     * 
     * @param  taskId  the unique identifier of the Task object
     * @return         <code>true</code> if the task is successfully updated;
     *                 <code>false</code> otherwise.
     */
    public boolean deleteTask(int taskId) {
        ArrayList<Task> tasks = Task.getTaskList();
        boolean isFound = false;

        // Find task
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            if (task.getTaskId() == taskId) {
                tasks.remove(i);
                isFound = true;
                break;
            }
        }

        // Return if task is not found
        if (!isFound) {
            return false;
        }

        // Store to file
        Task.setTaskList(tasks);
        Document doc = sController.parseTask(tasks);
        boolean isSuccessful = sController.writeXml(doc);

        return isSuccessful;
    }

    /**
     * This method marks a Task as completed
     * 
     * @param  taskId  the unique identifier of the Task object
     * @return         <code>true</code> if the task is successfully marked as completed;
     *                 <code>false</code> otherwise.
     */
    public boolean completeTask(int taskId) {
        ArrayList<Task> tasks = Task.getTaskList();
        Task task = null;
        boolean isFound = false;
        int index = -1;

        // Find task
        for (int i = 0; i < tasks.size(); i++) {
            task = tasks.get(i);
            if (task.getTaskId() == taskId) {
                index = i;
                isFound = true;
                break;
            }
        }

        // Return if task is not found
        if (!isFound) {
            return false;
        }

        // Modify complete status
        task.setComplete(true);
        tasks.set(index, task);

        // Store to file
        Task.setTaskList(tasks);
        Document doc = sController.parseTask(tasks);
        boolean isSuccessful = sController.writeXml(doc);

        return isSuccessful;
    }

    /**
     * This method overwrites the entire file with an new list of Tasks
     * 
     * @param  tasks  list of the new Task objects
     * @return        <code>true</code> if the task is successfully marked as 
     *                completed;
     *                <code>false</code> otherwise.
     */
    public boolean writeAllToFile(ArrayList<Task> tasks) {
        // Store to file
        Task.setTaskList(tasks);
        Document doc = sController.parseTask(tasks);
        boolean isSuccessful = sController.writeXml(doc);

        return isSuccessful;
    }

    /**
     * This method gets the next available taskId
     * 
     * @return      an int as the next taskId
     */
    public int getAvailableTaskId() {
        ArrayList<Task> tasks = Task.getTaskList();
        
        if (tasks.size() == 0) {
            return 1;
        }
        
        int[] testArray = new int[tasks.size()];

        for (int i = 0; i < testArray.length; i++) {
            testArray[i] = tasks.get(i).getTaskId();
        }

        Arrays.sort(testArray);
        int smallest = testArray[0];
        int largest = testArray[testArray.length - 1];
        int smallestUnused = largest + 1;
        
        if (smallest > 1) {
            smallestUnused = 1;
        } else {
            for (int i = 2; i < largest; i++) {
                if (Arrays.binarySearch(testArray, i) < 0) {
                    smallestUnused = i;
                    break;
                }
            }
        }

        return smallestUnused;
    }
    
    /**
     * This method checks recurring tasks
     * and updates their end dates if necessary
     * 
     * @return <code>true</code> if the check is successfully performed; 
     *         <code>false</code> otherwise.
     */
    public boolean checkRecurring() {
        ArrayList<Task> tasks = Task.getTaskList();

        // Loop through all task
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            switch (task.getType()) {
                case DEADLINE:
                    if ((task.isComplete() == false) &&
                            ((DeadlineTask) task).getEnd().isBefore(LocalDateTime.now()) &&
                            (((DeadlineTask) task).isRecurring() == true)) {
                        LocalDateTime newEndDate = ((DeadlineTask) task).getEnd();
                        switch (((DeadlineTask) task).getRecurPeriod()) {
                            case DAY:
                                while (newEndDate.isBefore(LocalDateTime.now())) {
                                    newEndDate.plusDays(1);
                                }
                                break;
                            case WEEK:
                                while (newEndDate.isBefore(LocalDateTime.now())) {
                                    newEndDate.plusWeeks(1);
                                }
                                break;
                            case MONTH:
                                while (newEndDate.isBefore(LocalDateTime.now())) {
                                    newEndDate.plusMonths(1);
                                }
                                break;
                            case YEAR:
                                while (newEndDate.isBefore(LocalDateTime.now())) {
                                    newEndDate.plusYears(1);
                                }
                                break;
                            default:
                                break;
                        }
                        ((DeadlineTask) task).setEnd(newEndDate);
                        ((DeadlineTask) task).setReminder(newEndDate.minusMinutes(5));
                        tasks.set(i, task);
                    }
                    break;
                case TIMED:
                    if ((task.isComplete() == false) &&
                            ((TimedTask) task).getEnd().isBefore(LocalDateTime.now()) &&
                            (((TimedTask) task).isRecurring() == true)) {
                        LocalDateTime newEndDate = ((TimedTask) task).getEnd();
                        switch (((TimedTask) task).getRecurPeriod()) {
                            case DAY:
                                while (newEndDate.isBefore(LocalDateTime.now())) {
                                    newEndDate.plusDays(1);
                                }
                                break;
                            case WEEK:
                                while (newEndDate.isBefore(LocalDateTime.now())) {
                                    newEndDate.plusWeeks(1);
                                }
                                break;
                            case MONTH:
                                while (newEndDate.isBefore(LocalDateTime.now())) {
                                    newEndDate.plusMonths(1);
                                }
                                break;
                            case YEAR:
                                while (newEndDate.isBefore(LocalDateTime.now())) {
                                    newEndDate.plusYears(1);
                                }
                                break;
                            default:
                                break;
                        }
                        ((TimedTask) task).setEnd(newEndDate);
                        ((TimedTask) task).setReminder(newEndDate.minusMinutes(5));
                        tasks.set(i, task);
                    }
                    break;
                default:
                    break;
            }
        }

        // Store to file
        Task.setTaskList(tasks);
        Document doc = sController.parseTask(tasks);
        boolean isSuccessful = sController.writeXml(doc);

        return isSuccessful;
    }
}
