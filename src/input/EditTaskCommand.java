package input;

import java.time.LocalDateTime;
import java.util.ArrayList;
import logic.TaskStub;
import logic.data.Task;
import logic.DeadlineTaskStub;
import logic.StorageAPIStub;
import storage.StorageController;

public class EditTaskCommand implements Command {

	/*** Variables ***/
	private static final String TASK_TYPE_DEADLINE = "deadline";
	private static final String TASK_TYPE_TIMED = "timed";
	private static final String TASK_TYPE_FLOATING = "floating";
	private static final String TASK_TYPE_INVALID = "invalid";
	
	private static final StorageAPIStub STORAGE_API = new StorageAPIStub();

	private ArrayList<Task> taskListToReturn;
	private TaskStub originalTask;
	private TaskStub modifiedTask;
	private String newDescription;
	private int taskId;
	private LocalDateTime newStart;
	private LocalDateTime newEnd;

	/*** Constructor ***/
	public EditTaskCommand(int _taskId, String _description, LocalDateTime _start, LocalDateTime _end) {
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
	public ArrayList<Task> execute() {
		boolean isSuccess;
		taskListToReturn = new ArrayList<Task>();
		retrieveTaskFromStorage(originalTask);
		createModifiedTask();
		isSuccess = storeTaskToStorage(modifiedTask);

		// If storing the modified task is successful, append the modified task to the ArrayList of Task to return
		if (isSuccess) {
			taskListToReturn.add(modifiedTask);
		} else {
			// Shouldn't occur? Sorry I forgot our discussion on how this is handled D:
		}
		return taskListToReturn;
	}

	/**
	 * Determines the resulting Task type of the Task object to be modified based on
	 * user input from the edit command
	 * 
	 * @return		a String that indicates the appropriate Task type for the Task object to be modified
	 */
	private String determineModifiedTaskType() {
		switch(originalTask.getType()) {
		case TASK_TYPE_FLOATING :
			if (newStart == null && newEnd == null) {
				return TASK_TYPE_FLOATING;
			} else if (newStart == null) {
				return TASK_TYPE_DEADLINE;
			} else {
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
	private void createModifiedTask() {
		String newType = determineModifiedTaskType();
		switch(newType){
		case TASK_TYPE_FLOATING : 
			createNewFloatTask();
			break;
		case TASK_TYPE_DEADLINE :
			createNewDeadlineTask();
			break;
		case TASK_TYPE_TIMED :
			createNewTimedTask();
			break;
		}
	}

	/**
	 * Verifies the appropriate task description and end date/time the modified task
	 * should have and call an overloaded method with the same name to instantiate the modified Task object
	 * to a DeadlineTask object
	 * 
	 */
	private void createNewDeadlineTask() {
		if (newDescription != null && newEnd != null) {
			createNewDeadlineTask(modifiedTask, newDescription, newEnd);
		} else if (newDescription != null && newEnd == null) {
			createNewDeadlineTask(modifiedTask, newDescription, getOriginalTaskEnd());
		} else if (newDescription == null && newEnd != null) {
			createNewDeadlineTask(modifiedTask, getOriginalTaskDescription(), newEnd);
		} else if (newDescription == null && newEnd == null) {
			createNewDeadlineTask(modifiedTask, getOriginalTaskDescription(), getOriginalTaskEnd());
		}
	}
	
	/**
	 * Instantiate the input Task object to a new DeadlineTask object with the input taskDescription
	 * and taskEnd
	 * 
	 * @param 		task the Task object to instantiate
	 * @param		taskDescription the description the input Task object will be set with
	 * @param		taskEnd the end LocalDateTime object the input Task object will be set with
	 */
	private void createNewDeadlineTask(Task task, String taskDescription, LocalDateTime taskEnd) {
		task = new DeadlineTaskStub(originalTask.getTaskStubId(),taskDescription,
				originalTask.getCreatedAt(),taskEnd,originalTask.isComplete());
	}

	private void createNewTimedTask() {

	}

	private void createNewFloatTask() {

	}

	/*** Setter and Getter Methods ***/

	private LocalDateTime getOriginalTaskEnd() {
		DeadlineTaskStub castedOriginalTask = (DeadlineTaskStub) originalTask;
		return castedOriginalTask.getEnd();
	}
	
	private String getOriginalTaskDescription() {
		return originalTask.getDescription();
	}
	
	private void retrieveTaskFromStorage(Task task) {
		task = STORAGE_API.viewTask(taskId);
	}

	private boolean storeTaskToStorage(TaskStub task) {
		return STORAGE_API.updateTask(task);
	}

	public void setNewDescription(String newDescription) {
		newDescription = newDescription;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public void set_newStart(LocalDateTime _newStart) {
		this.newStart = _newStart;
	}

	public void set_newEnd(LocalDateTime _newEnd) {
		this.newEnd = _newEnd;
	}

}