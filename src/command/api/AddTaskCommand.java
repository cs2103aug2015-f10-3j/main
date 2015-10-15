package command.api;

import java.util.ArrayList;

import common.data.Pair;
import task.api.*;
import task.entity.DeadlineTask;
import task.entity.FloatingTask;
import task.entity.Task;
import task.entity.TimedTask;

public class AddTaskCommand extends Command {
	
	private static final String KEYWORD_ADD = "add";
	private static final String KEYWORD_BY = "by";
	private static final String KEYWORD_BETWEEN = "between";
	private static final String KEYWORD_AND = "and";
	ArrayList<Task> taskList = new ArrayList<Task>();
	private Task userTask;
	
	public Pair<ArrayList<Task>,Boolean> execute() {
		assert(taskList != null);
		taskList.clear();
		userTask = createTask();
		TaskController taskController = TaskController.getInstance();
		boolean addTaskResult = taskController.addTask(userTask);
		addTaskResult &= taskList.add(userTask);
		Pair<ArrayList<Task>, Boolean> result = new Pair<ArrayList<Task>, Boolean>(taskList, addTaskResult); //will change to res class
		return result;
	}
	
	private Task createTask() {
		String description = getOption(KEYWORD_ADD).getStringValue();
		if (hasOption(KEYWORD_BY)) {
			userTask = new DeadlineTask(description, getOption(KEYWORD_BY).getDateValue());
		} else if (hasOption(KEYWORD_BETWEEN) && hasOption(KEYWORD_AND)) {
			userTask = new TimedTask(description, 
					getOption(KEYWORD_BETWEEN).getDateValue(), 
					getOption(KEYWORD_AND).getDateValue());
		} else {
			userTask = new FloatingTask(description);
		}
		return userTask;
	}

	@Override
	public Pair<ArrayList<Task>, Boolean> undo() {
		assert(userTask != null);
		boolean deleteTaskResult = TaskController.getInstance().deleteTask(userTask.getTaskId());
		Pair<ArrayList<Task>, Boolean> result = new Pair<ArrayList<Task>, Boolean>(taskList, deleteTaskResult);
		return result;
	}
}
