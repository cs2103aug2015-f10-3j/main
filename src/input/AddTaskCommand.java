package input;

import java.util.ArrayList;

import logic.TaskController;
import logic.data.*;
import util.Pair;

public class AddTaskCommand extends Command {
	
	private static final String KEYWORD_ADD = "add";
	private static final String KEYWORD_BY = "by";
	private static final String KEYWORD_BETWEEN = "between";
	private static final String KEYWORD_AND = "and";
	
	public Pair<ArrayList<Task>,Boolean> execute() {
		Task userTask = createTask();
		TaskController taskController = TaskController.getInstance();
		boolean addTaskRes = taskController.addTask(userTask);
		ArrayList<Task> taskList = new ArrayList<Task>();
		taskList.add(userTask);
		Pair<ArrayList<Task>, Boolean> result = new Pair<ArrayList<Task>, Boolean>(taskList, addTaskRes); //will change to res class
		return result;
	}
	
	private Task createTask() {
		Task userTask;
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
}
