package command.api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import background.Reminder;
import common.exception.InvalidCommandFormatException;
import common.exception.NoSuchTaskException;
import common.exception.UpdateTaskException;
import common.util.DateTimeHelper;
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
	private int newPriority;
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
	 * @throws InvalidCommandFormatException 
	 */
	@Override
	public ArrayList<Task> execute() throws NoSuchTaskException, UpdateTaskException, InvalidCommandFormatException{
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
		newPriority = -1;
		taskId = getOption("edit").getIntegerValue();
		if (hasOption("desc")) {
			newDescription = getOption("desc").getStringValue();
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
		if (hasOption("priority")) {
			newPriority = getOption("priority").getIntegerValue();
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
	 * @throws InvalidCommandFormatException 
	 */
	private String determineEditedTaskType() throws InvalidCommandFormatException {
		switch(originalTaskType) {
		case TASK_TYPE_FLOATING:
			if (newStart == null && newEnd == null) { // If no start/end date/time is specified, task.type is still floating
				return TASK_TYPE_FLOATING;
			} else if (newStart == null) { // If only an end date/time is specified, task.type is now a deadline task
				return TASK_TYPE_DEADLINE;
			} else if (newStart != null && newEnd != null) { // If both new and end date/time is specified, task.type is now a timed task
				return TASK_TYPE_TIMED;
			} else if (newStart != null && newEnd == null){
				LOGGER.log(Level.SEVERE, "Executing EditTaskCommand: User attempt to add only start date/time to Floating Task");
				throw new InvalidCommandFormatException("You cannot add only a start Time/Date to a Floating Task!");
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
	 * @throws InvalidCommandFormatException 
	 */
	private void createEditedTask() throws InvalidCommandFormatException {
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
				originalTask.isComplete(),
				getEditedTaskPriority(),
				originalTask.getTags());
	}
	
	private void createNewDeadlineTask() {
		editedTask = new DeadlineTask(
				originalTask.getTaskId(),
				getEditedTaskDescription(),
				originalTask.getCreatedAt(),
				getEditedTaskEnd(),
				getEditedTaskReminder(),
				originalTask.isComplete(),
				getEditedTaskPriority(),
				originalTask.getTags());
	}

	private void createNewTimedTask() {
		editedTask = new TimedTask(
				originalTask.getTaskId(),
				getEditedTaskDescription(),
				originalTask.getCreatedAt(),
				getEditedTaskStart(),
				getEditedTaskEnd(),
				getEditedTaskReminder(),
				originalTask.isComplete(),
				getEditedTaskPriority(),
				originalTask.getTags());
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
			// Retrieve Original Task casted to a TimedTask as only this Task type has Start date/time
			return getTimedTaskCastedOriginalTask().getStart();
		}
	}

	private LocalDateTime getEditedTaskEnd(){
		LocalDateTime newEditedTaskEnd = null;
		if (newEnd != null) {
			return newEnd;
		} else if (originalTaskType.equals(TASK_TYPE_DEADLINE)) {
			// Get DeadlineTask casted original Task and retrieve the End date/time
			newEditedTaskEnd = getDeadlineTaskCastedOriginalTask().getEnd();
		} else if (originalTaskType.equals(TASK_TYPE_TIMED)) {
			// Get TimedTask casted original Task and retrieve the End date/time
			newEditedTaskEnd = getTimedTaskCastedOriginalTask().getEnd();
		}
		return newEditedTaskEnd;
	}

	private LocalDateTime getEditedTaskReminder() {
		LocalDateTime newEditedTaskReminder = null;
		if (newReminder != null) {
			return newReminder;
		} else if (originalTaskType.equals(TASK_TYPE_FLOATING)){
			newEditedTaskReminder = DateTimeHelper.addMinutes(getEditedTaskEnd(), 5);
		} else if (originalTaskType.equals(TASK_TYPE_DEADLINE)) {
			// Get DeadlineTask casted original Task and retrieve the reminder LocalDateTime object
			newEditedTaskReminder = getDeadlineTaskCastedOriginalTask().getReminder();
		} else if (originalTaskType.equals(TASK_TYPE_TIMED)) {
			// Get TimedTask casted original Task and retrieve the reminder LocalDateTime object
			newEditedTaskReminder = getTimedTaskCastedOriginalTask().getReminder();
		}
		return newEditedTaskReminder;
	}
	
	private int getEditedTaskPriority() {
		int editedTaskPriority;
		if (newPriority != -1) {
			return newPriority;
		} else {
			return originalTask.getPriority();
		}
	}
	
	private DeadlineTask getDeadlineTaskCastedOriginalTask() {
		DeadlineTask castedOriginalTask = (DeadlineTask) originalTask;
		return castedOriginalTask;
	}
	
	private TimedTask getTimedTaskCastedOriginalTask() {
		TimedTask castedOriginalTask = (TimedTask) originalTask;
		return castedOriginalTask;
	}
	
	private void getTaskFromStorage(int taskId) throws NoSuchTaskException {
		originalTask = taskController.getTask(taskId);
		if (originalTask == null) {
			LOGGER.log(Level.SEVERE, "Executing EditTaskCommand getTaskFromStorage(): Retrieve Task with taskId -> {0} failed", taskId);
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
	 * @throws UpdateTaskException 
	 */
	private void storeTaskToStorage(Task task) throws UpdateTaskException  {
		LOGGER.info("EditTaskCommand: Storing updated task to Storage\n");
		if (!taskController.updateTask(task)) {
			LOGGER.log(Level.SEVERE, "Executing EditTaskCommand storeTaskToStorage(): Storing Task with taskId -> {0} failed", taskId);
			throw new UpdateTaskException("Failed to store updated Task to Storage");
		}
	}
}