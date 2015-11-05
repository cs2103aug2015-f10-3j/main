package main.paddletask.command.api;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.paddletask.common.exception.NoMatchException;
import main.paddletask.common.util.DateTimeHelper;
import main.paddletask.common.data.SearchResult;
import main.paddletask.task.entity.Task;
import main.paddletask.task.entity.Task.TASK_TYPE;
import main.paddletask.task.api.TaskController;
import main.paddletask.task.entity.DeadlineTask;
import main.paddletask.task.entity.TimedTask;

public class SearchTaskCommand extends Command{

	private static final Logger LOGGER = Logger.getLogger(SearchTaskCommand.class.getName());
	private TaskController taskController = TaskController.getInstance();
	private ArrayList<String> searchSequences;
	private ArrayList<LocalDateTime> searchDates;
	private ArrayList<Task> executionResult;

	public SearchTaskCommand() {
		executionResult = new ArrayList<Task>();
		searchSequences = new ArrayList<String>();
		searchDates = new ArrayList<LocalDateTime>();
	}

	@Override
	public ArrayList<Task> execute() throws Exception {
		retrieveOptions();
		prepareExecutionResult();
		setChanged();
		notifyObservers(null);
		return executionResult;
	}

	@Override
	public ArrayList<Task> undo() throws Exception {
		return null;
	}

	public void retrieveOptions() {
		if (hasOption("search")) {
			int numSearchString = getOption("search").getValuesCount();
			for (int i=0; i<numSearchString; i++) {
				searchSequences.add(getOption("search").getStringValue(i));
			}
		}
		if (hasOption("searchDates")) {
			int numSearchDates = getOption("searchDates").getValuesCount();
			for (int i=0; i<numSearchDates; i++) {
				searchDates.add(getOption("searchDates").getDateValue(i));
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
		boolean hasSearchDates = !searchDates.isEmpty();
		// ArrayList of all Task should not be null
		assert allTasks != null: "Retrieving all tasks";

		for (Task task: allTasks) {
			matchDegree = 0;
			for (String searchSequence: searchSequences) {
				if (task.getDescription().toLowerCase().contains(searchSequence.toLowerCase())) {
					matchDegree++;
				}
			}
			if (hasSearchDates && isTaskWithDate(task)) {
				for (String taskDate: getTaskDates(task)) {
					for (LocalDateTime searchDate: searchDates) {
						if (DateTimeHelper.getDate(searchDate).equals(taskDate)) {
							matchDegree++;
						}
					}
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

	private ArrayList<String> getTaskDates(Task task) {
		ArrayList<String> taskDates = new ArrayList<String>();
		if (task.getType().equals(TASK_TYPE.DEADLINE)) {
			String endDate = DateTimeHelper.getDate(castToDeadlineTask(task).getEnd());
			taskDates.add(endDate);
		} else if (task.getType().equals(TASK_TYPE.TIMED)) {
			String startDate = DateTimeHelper.getDate(castToTimedTask(task).getStart());
			String endDate = DateTimeHelper.getDate(castToTimedTask(task).getEnd());
			taskDates.add(startDate);
			taskDates.add(endDate);
		}
		return taskDates;
	}

	private boolean isTaskWithDate(Task task) {
		TASK_TYPE type = task.getType();
		if (type != TASK_TYPE.FLOATING) {
			return true;
		} else {
			return false;
		}
	}

	private DeadlineTask castToDeadlineTask(Task task) {
		assert task.getType() == TASK_TYPE.DEADLINE: "Ensure task type is Deadline";
		return (DeadlineTask) task;
	}

	private TimedTask castToTimedTask(Task task) {
		assert task.getType() == TASK_TYPE.TIMED: "Ensure task type is Timed";
		return (TimedTask) task;
	}
}
