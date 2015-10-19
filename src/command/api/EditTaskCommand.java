package command.api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import background.Reminder;
import common.exception.NoSuchTaskException;
import common.exception.UpdateTaskException;
import task.api.*;
import task.entity.DeadlineTask;
import task.entity.FloatingTask;
import task.entity.Task;
import task.entity.TimedTask;

public class EditTaskCommand extends Command {

	/*** Variables ***/
	private static final Logger LOGGER = Logger.getLogger(EditTaskCommand.class.getName());
	private static final String TASK_TYPE_DEADLINE = "deadline";
	private static final String TASK_TYPE_TIMED = "timed";
	private static final String TASK_TYPE_FLOATING = "floating";
	private static final String TASK_TYPE_INVALID = "invalid";
	
	private TaskController taskController = TaskController.getInstance();
	
	private ArrayList<Task> executionResult;
	private int taskId;
	private Task originalTask, editedTask;
	private String originalTaskType, newDescription = null;
	private LocalDateTime newStart, newEnd, newReminder = null;
	private boolean validity = true;

	/*** Methods ***/
	/**
	 * This method modifies a Task based on user input for the edit command
	 * 
	 * @param 
	 * @return		an ArrayList of Task objects that contains the modified Task object
	 * @throws UpdateTaskException 
	 * @throws NoSuchTaskException 
	 */
	@Override
	public ArrayList<Task> execute() throws NoSuchTaskException, UpdateTaskException{
		retrieveOptions();
		getTaskFromStorage(taskId);
		determineOriginalTaskType();
		createEditedTask();
		prepareExecutionResult();
		return executionResult;
	}
	
	/**
	 * This method reverse the previous execute() of the previous EditTaskCommand
	 *  
	 * @return		an ArrayList of Task objects that contains the modified Task object
	 * @throws UpdateTaskException 
	 */
	public ArrayList<Task> undo() throws UpdateTaskException {
		prepareUndoTask();
		prepareExecutionResult();
		return executionResult;
	}
	
	/**
	 * This method retrieves the parsed parameters stored in the Option object in
	 * this EditTaskCommand
	 */
	private void retrieveOptions() {
		executionResult = new ArrayList<Task>();
		taskId = getOption("edit").getIntegerValue();
		if (hasOption("name")) {
			newDescription = getOption("name").getStringValue();
		}
		if (hasOption("start")) {
			newStart = getOption("start").getDateValue();
		}
		if (hasOption("end")) {
			newEnd = getOption("end").getDateValue();
		}
		if (hasOption("remind")) {
			newReminder = getOption("remind").getDateValue();
		}
	}
	
	private void prepareUndoTask() {
		Task temp = editedTask;
		editedTask = originalTask;
		originalTask = temp;
	}
	
	/**
	 * Prepare execution result
	 * @throws UpdateTaskException 
	 */
	private void prepareExecutionResult() throws UpdateTaskException {
		if (validity) {
			executionResult.clear();
			executionResult.add(editedTask);
			storeTaskToStorage(editedTask);
		} else {
			executionResult = null;
		}
	}
	
	/**
	 * Determines the resulting Task type of the Task object to be modified based on
	 * user input from the edit command
	 * 
	 * @return		a String that indicates the appropriate Task type for the Task object to be modified
	 */
	private String determineEditedTaskType() {
		switch(originalTaskType) {
		case TASK_TYPE_FLOATING:
			if (newStart == null && newEnd == null) { // If no start/end date/time is specified, task.type is still floating
				return TASK_TYPE_FLOATING;
			} else if (newStart == null) { // If only an end date/time is specified, task.type is now a deadline task
				return TASK_TYPE_DEADLINE;
			} else if (newStart != null && newEnd != null) { // If both new and end date/time is specified, task.type is now a timed task
				return TASK_TYPE_TIMED;
			} else {
				return TASK_TYPE_INVALID;
			}

		case TASK_TYPE_DEADLINE :
			if (newStart == null) {
				return TASK_TYPE_DEADLINE;
			} else {
				return TASK_TYPE_TIMED;
			}

		case TASK_TYPE_TIMED :
			return TASK_TYPE_TIMED;

		default :
			return TASK_TYPE_INVALID;
		}
	}

	private void determineOriginalTaskType() {
		switch (originalTask.getType()) {
		case FLOATING:
			originalTaskType = TASK_TYPE_FLOATING;
			break;
		case DEADLINE:
			originalTaskType = TASK_TYPE_DEADLINE;
			break;
		case TIMED:
			originalTaskType = TASK_TYPE_TIMED;
			break;
		default:
			originalTaskType = TASK_TYPE_INVALID;
		}
	}
	
	/**
	 * Instantiates the task object to be modified to the appropriate Task type with
	 * the appropriate attributes
	 */
	private void createEditedTask() {
		switch(determineEditedTaskType()){
		case TASK_TYPE_FLOATING : 
			createNewFloatingTask();
			break;
		case TASK_TYPE_DEADLINE :
			createNewDeadlineTask();
			break;
		case TASK_TYPE_TIMED :
			createNewTimedTask();
			break;
		case TASK_TYPE_INVALID :
			validity = false;
			break;
		}
	}
	
	private void createNewFloatingTask() {
		editedTask = new FloatingTask(
				originalTask.getTaskId(),
				getEditedTaskDescription(),
				originalTask.getCreatedAt(),
				originalTask.isComplete());
	}
	
	private void createNewDeadlineTask() {
		editedTask = new DeadlineTask(
				originalTask.getTaskId(),
				getEditedTaskDescription(),
				originalTask.getCreatedAt(),
				getEditedTaskEnd(),
				newReminder,
				originalTask.isComplete());
	}

	private void createNewTimedTask() {
		editedTask = new TimedTask(
				originalTask.getTaskId(),
				getEditedTaskDescription(),
				originalTask.getCreatedAt(),
				getEditedTaskStart(),
				getEditedTaskEnd(),
				newReminder,
				originalTask.isComplete());
	}

	/*** Setter and Getter Methods ***/
	private String getEditedTaskDescription() {
		if (newDescription != null) {
			return newDescription;
		} else {
			return originalTask.getDescription();
		}
	}
	
	private LocalDateTime getEditedTaskStart() {
		if (newStart != null) {
			return newStart;
		} else {
			TimedTask castedOriginalTask = (TimedTask) originalTask;
			return castedOriginalTask.getStart();
		}
	}

	private LocalDateTime getEditedTaskEnd(){
		LocalDateTime newEditedTaskEnd = null;
		if (newEnd != null) {
			return newEnd;
		} else if (originalTaskType.equals(TASK_TYPE_DEADLINE)) {
			DeadlineTask castedOriginalTask = (DeadlineTask) originalTask;
			newEditedTaskEnd = castedOriginalTask.getEnd();
		} else if (originalTaskType.equals(TASK_TYPE_TIMED)) {
			TimedTask castedOriginalTask = (TimedTask) originalTask;
			newEditedTaskEnd = castedOriginalTask.getEnd();
		}
		return newEditedTaskEnd;
	}

	private void getTaskFromStorage(int taskId) throws NoSuchTaskException {
		originalTask = taskController.getTask(taskId);
		if (originalTask == null) {
			LOGGER.log(Level.SEVERE, "Executing EditTaskCommand: Retrieve Task with taskId -> {0} failed", taskId);
			throw new NoSuchTaskException("Task with the following Task ID does not exist!");
		}
		LOGGER.info("EditTaskCommand: Retrieved Task with taskId: " + taskId + "\n");
	}

	/**
	 * This method calls the storageAPI to store the updated task object
	 * 
	 * @param task the Task object that is updated
	 * @return returns <code>True</code> if the operation is a success,
	 * 		   returns <code>False</code> otherwise
	 */
	private boolean storeTaskToStorage(Task task)  {
		LOGGER.info("EditTaskCommand: Storing updated task to Storage\n");
		return taskController.updateTask(task);
	}
}