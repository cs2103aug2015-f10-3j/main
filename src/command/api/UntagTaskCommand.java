package command.api;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import common.exception.NoSuchTaskException;
import common.exception.UpdateTaskException;
import task.api.TaskController;
import task.entity.Task;

public class UntagTaskCommand extends Command{

	/*** Variables ***/
	private static final Logger LOGGER = Logger.getLogger(TagTaskCommand.class.getName());
	private ArrayList<String> tagsToRemove;
	private ArrayList<String> modifiedTaglist;
	private ArrayList<String> originalTaglist;
	private ArrayList<Task> executionResult;
	private Task taskToEdit;
	private static int taskId;
	
	public UntagTaskCommand() {
		modifiedTaglist = new ArrayList<String>();
		tagsToRemove = new ArrayList<String>();
		executionResult = new ArrayList<Task>();
	}
	
	/*** Methods ***/
	
	@Override
	public ArrayList<Task> execute() throws Exception {
		retrieveOptions();
		getTaskFromStorage(taskId);
		processTags();
		prepareExecutionResult();
		return executionResult;
	}

	public void retrieveOptions() {
		taskId = getOption("untag").getIntegerValue();
		if (hasOption("#")) {
			int numTags = getOption("#").getValuesCount();
			for (int i=0; i<numTags; i++) {
				tagsToRemove.add((getOption("#").getStringValue(i)));
			}
		}
	}
	
	public void prepareExecutionResult() throws UpdateTaskException {
		assert modifiedTaglist != null;
		saveTagToTask(modifiedTaglist);
		
		assert taskToEdit != null;
		executionResult.add(taskToEdit);
	}
	
	@Override
	public ArrayList<Task> undo() throws Exception {
		prepareUndoTags();
		prepareExecutionResult();
		return executionResult;
	}

	private void prepareUndoTags() {
		ArrayList<String> temp = originalTaglist;
		originalTaglist = modifiedTaglist;
		modifiedTaglist = temp;
	}
	
	private void processTags() throws UpdateTaskException {
		archiveOriginalTaglist(getExistingTaglist());
		updateOriginalTaglist();
	}
	
	private void updateOriginalTaglist() {
		modifiedTaglist = getExistingTaglist();
		modifiedTaglist.removeAll(tagsToRemove);
	}
	
	private void archiveOriginalTaglist(ArrayList<String> existingTags) {
		originalTaglist = new ArrayList<String>();
		for (String existingTag: existingTags) {
			originalTaglist.add(existingTag);
		}
	}
	
	private ArrayList<String> getExistingTaglist() {
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
			LOGGER.log(Level.SEVERE, "Executing UntagTaskCommand: Retrieve Task with taskId -> {0} failed", taskId);
			throw new NoSuchTaskException("Task with the following Task ID does not exist!");
		}
		LOGGER.info("TagTaskCommand: Retrieved Task with taskId: " + taskId + "\n");
	}
	
	private void storeTaskToStorage(Task task) throws UpdateTaskException  {
		LOGGER.info("UntagTaskCommand: Storing updated task to Storage\n");
		if (!TaskController.getInstance().updateTask(task)) {
			LOGGER.info("UntagTaskCommand: Store Task with taskId: " + taskId + "\n");
			throw new UpdateTaskException("Failed to store updated Task to Storage");
		}
	}
	
}
