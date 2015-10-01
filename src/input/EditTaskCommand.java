package input;

import java.time.LocalDateTime;
import java.util.ArrayList;
import logic.TaskStub;
import logic.TimedTaskStub;
import logic.data.Task;
import logic.DeadlineTaskStub;
import logic.StorageAPIStub;
import storage.StorageController;
import util.Pair;

public class EditTaskCommand implements Command {

	/*** Variables ***/
	private static final String TASK_TYPE_DEADLINE = "deadline";
	private static final String TASK_TYPE_TIMED = "timed";
	private static final String TASK_TYPE_FLOATING = "floating";
	private static final String TASK_TYPE_INVALID = "invalid";

	private static final StorageAPIStub STORAGE_API = new StorageAPIStub();

	private Pair<ArrayList<Task>,Boolean> executionResult;
	private ArrayList<Task> taskListToReturn;
	private TaskStub originalTask;
	private TaskStub editedTask;
	private String newDescription;
	private int taskId;
	private LocalDateTime newStart;
	private LocalDateTime newEnd;

	/*** Constructor ***/
	public EditTaskCommand(int _taskId, String _description, LocalDateTime _start, LocalDateTime _end) {
		taskListToReturn = new ArrayList<Task>();
		taskId = _taskId;
		newDescription = _description;
		newStart = _start;
		newEnd = _end;
	}	

	/*** Methods ***/
	/**
	 * This method modifies a Task based on user input for the edit command
	 * 
	 * @param 
	 * @return		an ArrayList of Task objects that contains the modified Task object
	 */
	@Override
	public Pair<ArrayList<Task>,Boolean> execute() {
		getTaskFromStorage(originalTask);
		createEditedTask();
		taskListToReturn.add(editedTask);
		executionResult = new Pair<ArrayList<Task>,Boolean>(taskListToReturn,storeTaskToStorage(editedTask));
		
		return executionResult;
	}

	/**
	 * Determines the resulting Task type of the Task object to be modified based on
	 * user input from the edit command
	 * 
	 * @return		a String that indicates the appropriate Task type for the Task object to be modified
	 */
	private String determineEditedTaskType() {
		switch(originalTask.getType()) {
		case TASK_TYPE_FLOATING :
			if (newStart == null && newEnd == null) { // If no start/end date/time is specified, task.type is still floating
				return TASK_TYPE_FLOATING;
			} else if (newStart == null) { // If only an end date/time is specified, task.type is now a deadline task
				return TASK_TYPE_DEADLINE;
			} else { // If both new and end date/time is specified, task.type is now a timed task
				return TASK_TYPE_TIMED;
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
		}
	}

	private void createNewFloatingTask() {
		/*
		editedTask = new FloatingTaskStub(
				originalTask.getTaskStubId(),
				getEditedTaskDescription(),
				originalTask.getCreatedAt());
		*/
	}
	
	private void createNewDeadlineTask() {
		editedTask = new DeadlineTaskStub(
				originalTask.getTaskStubId(),
				getEditedTaskDescription(),
				originalTask.getCreatedAt(),
				getEditedTaskEnd(),
				originalTask.isComplete());
	}

	private void createNewTimedTask() {
		editedTask = new TimedTaskStub(
				originalTask.getTaskStubId(),
				getEditedTaskDescription(),
				originalTask.getCreatedAt(),
				getEditedTaskStart(),
				getEditedTaskEnd());
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
			TimedTaskStub castedOriginalTask = (TimedTaskStub) originalTask;
			return castedOriginalTask.getStart();
		}
	}

	private LocalDateTime getEditedTaskEnd(){
		if (newEnd != null) {
			return newEnd;
		} else {
			DeadlineTaskStub castedOriginalTask = (DeadlineTaskStub) originalTask;
			return castedOriginalTask.getEnd();
		}
	}

	private void getTaskFromStorage(Task task) {
		task = STORAGE_API.viewTask(taskId);
	}

	/**
	 * This method calls the storageAPI to store the updated task object
	 * 
	 * @param task the Task object that is updated
	 * @return returns <code>True</code> if the operation is a success,
	 * 		   returns <code>False</code> otherwise
	 */
	private boolean storeTaskToStorage(TaskStub task) {
		return STORAGE_API.updateTask(task);
	}
}