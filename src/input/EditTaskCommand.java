package input;

import java.time.LocalDateTime;
import java.util.ArrayList;
import logic.TaskStub;
import logic.data.Task;
import logic.StorageStub;
import storage.StorageController;

public class EditTaskCommand implements Command {
	
	private static final String TASK_TYPE_DEADLINE = "deadline";
	
	private TaskStub originalTask;
	private TaskStub modifiedTask;
	private String newDescription;
	private int taskId;
	private LocalDateTime newStart;
	private LocalDateTime newEnd;

	@Override
	public ArrayList<Task> execute() {
		
		/* note to self:
		 * 1. make a new task based on old value
		 * 2. modify the new task with the new values supplied by user
		 * 3. save into storage
		 */
		
		ArrayList<Task> taskListToReturn = new ArrayList<Task>();
		TaskStub editResult = new TaskStub();
		originalTask = StorageStub.getTaskById(taskId);
		modifiedTask = copyTaskInformation(originalTask);
		updateModifiedTask();
		boolean result = StorageStub.editTask(modifiedTask);
		
		if (result) {
			// So that when it's returned to KS, he can display the modified task values
			editResult = modifiedTask;
		}
		else {
			// Shouldn't occur? Sorry I forgot our discussion on how this is handled D:
			editResult.setDescription("Failed to edit");
		}
		taskListToReturn.add(editResult);
		return taskListToReturn;
	}
	
	private TaskStub copyTaskInformation(TaskStub task) {
		return new TaskStub(task.getTaskId(), task.getDescription(),
				task.getType(), task.getStart(),task.getEnd());
	}
	
	private void updateModifiedTask() {
		modifiedTask.setType(determineTaskType());
		if (newDescription != null) {
			modifiedTask.setDescription(newDescription);
		}
		if (newStart != null) {
			modifiedTask.setStart(newStart);
		}
		if (newEnd != null) {
			modifiedTask.setEnd(newEnd);
		}
	}
	
	private String determineTaskType() {
		// Check various information, and return the appropriate type of Task that the modified task should have
		if (originalTask.getStart() == null &&newStart == null && newEnd != null) {
			return TASK_TYPE_DEADLINE;
		}
		return null;
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