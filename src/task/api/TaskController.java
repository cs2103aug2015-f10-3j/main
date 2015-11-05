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
    private static TaskController thisInstance;

    /*** Constructor ***/
    private TaskController() {
    	sController = StorageController.getInstance();
        Task.setTaskList(sController.readTask());
    }
    
    public static synchronized TaskController getInstance( ) {
        if (thisInstance == null)
        	thisInstance = new TaskController();
        return thisInstance;
     }

    /*** Methods ***/
    /**
     * This method adds the Task object to file
     * 
     * @param task
     *            Task entry to be added
     * @return <code>true</code> if the task is successfully added;
     *         <code>false</code> otherwise.
     */
    public boolean addTask(Task task) {
        // Get and set the smallest available tasId
        int taskId = getAvailableTaskId();
        task.setTaskId(taskId);

        // Add this task to our arraylist
        ArrayList<Task> taskList = Task.getTaskList();
        taskList.add(task);
        Task.setTaskList(taskList);

        // Store to file
        Document doc = sController.parseTask(taskList);
        boolean result = sController.writeXml(doc);

        return result;
    }

    /**
     * This method returns a list of all tasks
     * 
     * @return an ArrayList of Tasks
     */
    public ArrayList<Task> getTask() {
        ArrayList<Task> taskList = Task.getTaskList();
        return taskList;
    }

    /**
     * This method returns a list of the specified tasks
     * 
     * @param TASK_TYPE
     *            type of the Tasks to be selected
     * @return an ArrayList of Tasks
     */
    public ArrayList<Task> getTask(TASK_TYPE type) {
        ArrayList<Task> taskList = Task.getTaskList();
        
        if (type == TASK_TYPE.ANY) {
        	return taskList;
        }
        
        ArrayList<Task> filteredTaskList = new ArrayList<Task>();

        for (Task task : taskList) {
            if (task.getType() == type) {
                filteredTaskList.add(task);
            }
        }

        return filteredTaskList;
    }

    /**
     * This method returns a Task object
     * 
     * @param taskId
     *            the unique identifier of the Task object
     * @return the specified Task object
     */
    public Task getTask(int taskId) {
        ArrayList<Task> taskList = Task.getTaskList();
        for (Task task : taskList) {
            if (task.getTaskId() == taskId) {
                return task;
            }
        }
        return null;
    }

    /**
     * This method updates the Task object to file
     * 
     * @param task
     *            Task entry to be updated
     * @return <code>true</code> if the task is successfully updated;
     *         <code>false</code> otherwise.
     */
    public boolean updateTask(Task task) {
        ArrayList<Task> taskList = Task.getTaskList();
        boolean found = false;

        // Find task
        for (int i = 0; i < taskList.size(); i++) {
            Task existingTask = taskList.get(i);
            if (existingTask.getTaskId() == task.getTaskId()) {
                taskList.set(i, task);
                found = true;
                break;
            }
        }

        // Return if task is not found
        if (!found) {
            return false;
        }

        // Store to file
        Task.setTaskList(taskList);
        Document doc = sController.parseTask(taskList);
        boolean result = sController.writeXml(doc);

        return result;
    }

    /**
     * This method delete the Task from file
     * 
     * @param taskId
     *            the unique identifier of the Task object
     * @return <code>true</code> if the task is successfully updated;
     *         <code>false</code> otherwise.
     */
    public boolean deleteTask(int taskId) {
        ArrayList<Task> taskList = Task.getTaskList();
        boolean found = false;

        // Find task
        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);
            if (task.getTaskId() == taskId) {
                taskList.remove(i);
                found = true;
                break;
            }
        }

        // Return if task is not found
        if (!found) {
            return false;
        }

        // Store to file
        Task.setTaskList(taskList);
        Document doc = sController.parseTask(taskList);
        boolean result = sController.writeXml(doc);

        return result;
    }

    /**
     * This method marks a Task as completed
     * 
     * @param taskId
     *            the unique identifier of the Task object
     * @return <code>true</code> if the task is successfully marked as
     *         completed; <code>false</code> otherwise.
     */
    public boolean completeTask(int taskId) {
        ArrayList<Task> taskList = Task.getTaskList();
        Task task = null;
        boolean found = false;
        int index = -1;

        // Find task
        for (int i = 0; i < taskList.size(); i++) {
            task = taskList.get(i);
            if (task.getTaskId() == taskId) {
                index = i;
                found = true;
                break;
            }
        }

        // Return if task is not found
        if (!found) {
            return false;
        }

        // Modify complete status
        task.setComplete(true);
        taskList.set(index, task);

        // Store to file
        Task.setTaskList(taskList);
        Document doc = sController.parseTask(taskList);
        boolean result = sController.writeXml(doc);

        return result;
    }

    /**
     * This method overwrites the entire file with an new list of Tasks
     * 
     * @param taskList
     *            list of the new Task objects
     * @return <code>true</code> if the task is successfully marked as
     *         completed; <code>false</code> otherwise.
     */
    public boolean writeAllToFile(ArrayList<Task> taskList) {
        // Store to file
        Task.setTaskList(taskList);
        Document doc = sController.parseTask(taskList);
        boolean result = sController.writeXml(doc);

        return result;
    }

    /**
     * This method gets the next available taskId
     * 
     * @return an int as the next taskId
     */
    public int getAvailableTaskId() {
        ArrayList<Task> taskList = Task.getTaskList();
        
        if (taskList.size() == 0) {
            return 1;
        }
        
        int[] testArray = new int[taskList.size()];

        for (int i = 0; i < testArray.length; i++) {
            testArray[i] = taskList.get(i).getTaskId();
        }

        Arrays.sort(testArray);
        int smallest = testArray[0];
        int largest = testArray[testArray.length - 1];
        int smallestUnused = largest + 1;
        // System.out.println("smallest: "+smallest);
        // System.out.println("largest: "+largest);
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
        // System.out.println("Smallest unused: "+smallestUnused);

        return smallestUnused;
    }
    
    /**
     * This method checks recurring tasks
     * and updates their end dates if necessary
     * 
     * @param taskList
     *            list of the new Task objects
     * @return <code>true</code> if the check is successfully performed; 
     *         <code>false</code> otherwise.
     */
    public boolean checkRecurring() {
        ArrayList<Task> taskList = Task.getTaskList();

        // Loop through all task
        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);
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
                        taskList.set(i, task);
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
                        taskList.set(i, task);
                    }
                    break;
                default:
                    break;
            }
        }

        // Store to file
        Task.setTaskList(taskList);
        Document doc = sController.parseTask(taskList);
        boolean result = sController.writeXml(doc);

        return result;
    }
}
