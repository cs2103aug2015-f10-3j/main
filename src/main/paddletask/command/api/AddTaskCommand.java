package main.paddletask.command.api;

import java.time.LocalDateTime;
import java.util.ArrayList;

import main.paddletask.common.util.DateTimeHelper;
import main.paddletask.task.api.*;
import main.paddletask.task.entity.*;
import main.paddletask.task.entity.FloatingTask;
import main.paddletask.task.entity.Task;
import main.paddletask.task.entity.TimedTask;
import main.paddletask.task.entity.Task.RECUR_TYPE;

public class AddTaskCommand extends Command {
	
	private static final String KEYWORD_ADD = "add";
	private static final String KEYWORD_BY = "by";
	private static final String KEYWORD_BETWEEN = "between";
	private static final String KEYWORD_AND = "and";
	private static final String KEYWORD_REMIND = "remind";
	private static final String KEYWORD_PRIORITY = "priority";
    private static final String KEYWORD_EVERY = "every";
	ArrayList<Task> taskList = new ArrayList<Task>();
	private Task userTask = null;

	@Override
	public ArrayList<Task> execute() {
		assert(taskList != null);
		taskList.clear();
		userTask = createTask();
		TaskController taskController = TaskController.getInstance();
		taskController.addTask(userTask);
		taskList.add(userTask);
		return taskList;
	}
	
	private Task createTask() {
		assert(hasOption(KEYWORD_ADD));
		String description = getOption(KEYWORD_ADD).getStringValue();
		Integer priority = getPriority();
		if (hasOption(KEYWORD_BY)) {
			LocalDateTime deadLineDate = getOption(KEYWORD_BY).getDateValue();
			LocalDateTime reminderDate = getReminderDate(deadLineDate);
			boolean recurring = false;
			RECUR_TYPE recurType = RECUR_TYPE.NULL;
			if (hasOption(KEYWORD_EVERY)) {
			    recurring = true;
			    recurType = Task.determineRecurType(getOption(KEYWORD_EVERY).getStringValue());
			}
			userTask = new DeadlineTask(description, deadLineDate, reminderDate, priority, recurring, recurType);
		} else if (hasOption(KEYWORD_BETWEEN) && hasOption(KEYWORD_AND)) {
			LocalDateTime startDate = getOption(KEYWORD_BETWEEN).getDateValue();
			LocalDateTime deadLineDate = getOption(KEYWORD_AND).getDateValue();
			LocalDateTime reminderDate = getReminderDate(deadLineDate);
			boolean recurring = false;
            RECUR_TYPE recurType = RECUR_TYPE.NULL;
            if (hasOption(KEYWORD_EVERY)) {
                recurring = true;
                recurType = Task.determineRecurType(getOption(KEYWORD_EVERY).getStringValue());
            }
			userTask = new TimedTask(description, startDate, deadLineDate, reminderDate, priority, recurring, recurType);
		} else {
			userTask = new FloatingTask(description, priority);
		}
		return userTask;
	}

	private LocalDateTime getReminderDate(LocalDateTime deadLineDate) {
		if (hasOption(KEYWORD_REMIND)) {
			return getOption(KEYWORD_REMIND).getDateValue();
		} else {
			return DateTimeHelper.addMinutes(deadLineDate, -5);
		}
	}

	private Integer getPriority() {
		if (hasOption(KEYWORD_PRIORITY)) {
			return getOption(KEYWORD_PRIORITY).getIntegerValue();
		} else {
			return 3;
		}
	}

	@Override
	public ArrayList<Task> undo() {
		assert(userTask != null);
		TaskController.getInstance().deleteTask(userTask.getTaskId());
		return taskList;
	}
}
