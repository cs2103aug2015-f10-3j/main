package command.api;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import common.exception.NoMatchException;
import task.entity.Task;
import task.entity.FloatingTask;
import task.api.TaskController;
import task.entity.DeadlineTask;
import task.entity.TimedTask;

public class SearchTaskCommand extends Command{
	
	private static final Logger LOGGER = Logger.getLogger(SearchTaskCommand.class.getName());
	private TaskController taskController = TaskController.getInstance();
	private String searchString;
	private ArrayList<Task> executionResult;

	public SearchTaskCommand() {
		executionResult = new ArrayList<Task>();
	}
	
	@Override
	public ArrayList<Task> execute() throws Exception {
		prepareExecutionResult();
		return executionResult;
	}
	
	@Override
	public ArrayList<Task> undo() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void retrieveOptions() {
		if (hasOption("search")) {
			searchString = getOption("search").getStringValue();
		}
	}
	
	public void prepareExecutionResult() throws NoMatchException {
		executionResult = retrieveMatchingTasks();
	}
	
	public ArrayList<Task> retrieveMatchingTasks() throws NoMatchException {
		ArrayList<Task> matchingTasks = new ArrayList<Task>();
		ArrayList<Task> allTasks = taskController.getTask();
		
		// ArrayList of all Task should not be null
		assert allTasks != null: "Retrieving all tasks";
		
		for (Task task: allTasks) {
			if (task.getDescription().contains(searchString)) {
				matchingTasks.add(task);
			}
		}
		
		if (matchingTasks.isEmpty()) {
			LOGGER.log(Level.INFO, "Executing SearchTaskCommand: No matching results found");
			throw new NoMatchException("No matching results");
		}
		return matchingTasks;
	}
}
