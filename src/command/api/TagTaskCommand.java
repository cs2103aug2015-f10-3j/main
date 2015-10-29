package command.api;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import common.exception.NoSuchTaskException;
import common.exception.UpdateTaskException;
import task.api.TaskController;
import task.entity.Task;

public class TagTaskCommand extends Command {

	/*** Variables ***/
	private static final Logger LOGGER = Logger.getLogger(EditTaskCommand.class.getName());
	private ArrayList<String> tagsToAdd;
	private ArrayList<String> modifiedTags;
	private ArrayList<String> originalTags;
	private ArrayList<Task> executionResult;
	private Task taskToEdit;
	private int taskId;

	public TagTaskCommand() {
		modifiedTags = new ArrayList<String>();
		originalTags = new ArrayList<String>();
		tagsToAdd = new ArrayList<String>();
		executionResult = new ArrayList<Task>();
	}
	
	@Override
	public ArrayList<Task> execute() throws Exception {
		retrieveOptions();
		getTaskFromStorage(taskId);
		processTags();
		prepareExecutionResult();
		return executionResult;
	}

	@Override
	public ArrayList<Task> undo() throws Exception {
		prepareUndoTags();
		prepareExecutionResult();
		return executionResult;
	}
	
	private void retrieveOptions() {
		taskId = getOption("tag").getIntegerValue();
		if (hasOption("#")) {
			int numTags = getOption("#").getValuesCount();
			for (int i=0; i<numTags; i++) {
				tagsToAdd.add((getOption("tag").getStringValue(i)));
			}
		}
	}
	
	private void prepareExecutionResult() throws UpdateTaskException {
		assert modifiedTags != null;
		saveTagToTask(modifiedTags);
		
		assert taskToEdit != null;
		executionResult.add(taskToEdit);
	}
	
	private void prepareUndoTags() {
		ArrayList<String> temp = originalTags;
		originalTags = modifiedTags;
		modifiedTags = temp;
	}
	
	private void processTags() throws UpdateTaskException {
		archiveOriginalTags(getExistingTags());
		updateOriginalTags();
		//saveTagToTask(updateTaskTags());
	}
	
	private void updateOriginalTags() {
		modifiedTags = getExistingTags();
		for (String tag : tagsToAdd) {
			modifiedTags.add(tag);
		}
	}
	
	private void archiveOriginalTags(ArrayList<String> existingTags) {
		originalTags = new ArrayList<String>();
		for (String existingTag: existingTags) {
			originalTags.add(existingTag);
		}
	}
	
	private ArrayList<String> getExistingTags() {
		assert taskToEdit != null;
		ArrayList<String> existingTags = taskToEdit.getTags();
		return existingTags;
	}
	
	
	
	private void saveTagToTask(ArrayList<String> updatedTaskTags) throws UpdateTaskException{
		assert taskToEdit != null;
		taskToEdit.setTags(updatedTaskTags);
		storeTaskToStorage(taskToEdit);
	}
	
	private void getTaskFromStorage(int taskId) throws NoSuchTaskException {
		taskToEdit = TaskController.getInstance().getTask(taskId);
		if (taskToEdit == null) {
			LOGGER.log(Level.SEVERE, "Executing TagTaskCommand: Retrieve Task with taskId -> {0} failed", taskId);
			throw new NoSuchTaskException("Task with the following Task ID does not exist!");
		}
		LOGGER.info("TagTaskCommand: Retrieved Task with taskId: " + taskId + "\n");
	}
	
	private void storeTaskToStorage(Task task) throws UpdateTaskException  {
		LOGGER.info("TagTaskCommand: Storing updated task to Storage\n");
		if (!TaskController.getInstance().updateTask(task)) {
			LOGGER.info("TagTaskCommand: Store Task with taskId: " + taskId + "\n");
			throw new UpdateTaskException("Failed to store updated Task to Storage");
		}
	}
}
