package command.api;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import common.exception.NoMatchException;
import common.data.SearchResult;
import task.entity.Task;
import task.entity.FloatingTask;
import task.api.TaskController;
import task.entity.DeadlineTask;
import task.entity.TimedTask;

public class SearchTaskCommand extends Command{

	private static final Logger LOGGER = Logger.getLogger(SearchTaskCommand.class.getName());
	private TaskController taskController = TaskController.getInstance();
	private ArrayList<String> searchSequences;
	private ArrayList<Task> executionResult;

	public SearchTaskCommand() {
		executionResult = new ArrayList<Task>();
		searchSequences = new ArrayList<String>();
	}

	@Override
	public ArrayList<Task> execute() throws Exception {
		retrieveOptions();
		prepareExecutionResult();
		return executionResult;
	}

	@Override
	public ArrayList<Task> undo() throws Exception {
		return null;
	}

	public void retrieveOptions() {
		if (hasOption("search")) {
			int numSearchString = getOption("search").getValuesCount();
			while (numSearchString > 0) {
				searchSequences.add(getOption("search").getStringValue());
				numSearchString--;
			}
		}
	}

	public void prepareExecutionResult() throws NoMatchException {
		executionResult = retrieveMatchingTasks();
	}

	public ArrayList<Task> retrieveMatchingTasks() throws NoMatchException {
		PriorityQueue<SearchResult> matchedTaskWithRanking = new PriorityQueue<SearchResult>();
		ArrayList<Task> matchingTasks = new ArrayList<Task>();
		ArrayList<Task> allTasks = taskController.getTask();
		int matchDegree;

		// ArrayList of all Task should not be null
		assert allTasks != null: "Retrieving all tasks";

		for (Task task: allTasks) {
			matchDegree = 0;
			for (String searchSequence: searchSequences) {
				if (task.getDescription().toLowerCase().contains(searchSequence.toLowerCase())) {
					matchDegree++;
				}
			}
			if (matchDegree > 0) {
				matchedTaskWithRanking.add(new SearchResult(matchDegree,task));
			}
		}

		if (matchedTaskWithRanking.isEmpty()) {
			LOGGER.log(Level.INFO, "Executing SearchTaskCommand: No matching results found");
			throw new NoMatchException("No matching results");
		} else {
			while (!matchedTaskWithRanking.isEmpty()) {
				matchingTasks.add(matchedTaskWithRanking.poll().getMatchedTask());
			}
		}

		return matchingTasks;
	}
}
