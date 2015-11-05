//@@A0125473H
package main.paddletask.command.api;

import java.time.LocalDateTime;
import java.util.ArrayList;

import main.paddletask.common.data.ParserConstants;
import main.paddletask.common.exception.TaskAddFailedException;
import main.paddletask.common.util.DateTimeHelper;
import main.paddletask.task.api.*;
import main.paddletask.task.entity.*;
import main.paddletask.task.entity.Task.RECUR_TYPE;

public class AddTaskCommand extends Command implements ParserConstants {
	
	private ArrayList<Task> _taskList = new ArrayList<Task>();
	private TaskController _taskController = TaskController.getInstance();
	private Task _userTask = null;

	private static final int DEFAULT_PRIORITY = 3;
	private static final int FIVE_MINUTES_BEFORE = -5;
	
	@Override
	public ArrayList<Task> execute() throws TaskAddFailedException {
		try {
			assert(_taskList != null);
			clearResult();
			_userTask = createNewTask();
			storeNewTask();
			appendToResult();
			return _taskList;
		} catch (Exception e) {
			throw new TaskAddFailedException("Adding of task was unsuccessful");
		}
	}

	private void clearResult() {
		_taskList.clear();
	}
	
	private Task createNewTask() {
		assert(hasOption(OPTIONS.ADD.toString()));
		Task userTask = createTaskByType();
		return userTask;
	}
	
	private Task createTaskByType() {
		if (hasOption(OPTIONS.BY.toString())) {
			return createDeadlineTask();
		} else if (hasOption(OPTIONS.BETWEEN.toString()) && hasOption(OPTIONS.AND.toString())) {
			return createTimedTask();
		} else {
			return createFloatingTask();
		}
	}
	
	private Task createDeadlineTask() {
		String description = getTaskDescription();
		Integer priority = getTaskPriority();
		LocalDateTime deadline = getTaskDeadline();
		LocalDateTime reminder = calculateTaskReminderDate(deadline);
		boolean recurring = isTaskRecurring();
		RECUR_TYPE recurType = getTaskRecurranceType(recurring);
		return new DeadlineTask(description, deadline, reminder, priority, recurring, recurType);
		
	}
	
	private Task createTimedTask() {
		String description = getTaskDescription();
		Integer priority = getTaskPriority();
		LocalDateTime startDate = getTaskStartDate();
		LocalDateTime deadline = getTaskDeadline();
		LocalDateTime reminder = calculateTaskReminderDate(deadline);
		boolean recurring = isTaskRecurring();
		RECUR_TYPE recurType = getTaskRecurranceType(recurring);
		return new TimedTask(description, startDate, deadline, reminder, priority, recurring, recurType);
	}

	private Task createFloatingTask() {
		String description = getTaskDescription();
		Integer priority = getTaskPriority();
		return new FloatingTask(description, priority);
	}

	private String getTaskDescription() {
		return getOption(OPTIONS.ADD.toString()).getStringValue();
	}

	private Integer getTaskPriority() {
		if (hasOption(OPTIONS.PRIORITY.toString())) {
			return getOption(OPTIONS.PRIORITY.toString()).getIntegerValue();
		} else {
			return DEFAULT_PRIORITY;
		}
	}
	
	private LocalDateTime getTaskStartDate() {
		return getOption(OPTIONS.BETWEEN.toString()).getDateValue();
	}

	private LocalDateTime getTaskDeadline() {
		return getOption(OPTIONS.BY.toString()).getDateValue();
	}

	private LocalDateTime calculateTaskReminderDate(LocalDateTime deadline) {
		if (hasOption(OPTIONS.REMIND.toString())) {
			return getOption(OPTIONS.REMIND.toString()).getDateValue();
		} else {
			return DateTimeHelper.addMinutes(deadline, FIVE_MINUTES_BEFORE);
		}
	}

	private boolean isTaskRecurring() {
		return hasOption(OPTIONS.EVERY.toString());
	}

	private RECUR_TYPE getTaskRecurranceType(boolean recurring) {
		if (recurring) {
			String recurranceType = getOption(OPTIONS.EVERY.toString()).getStringValue();
			return Task.determineRecurType(recurranceType);
		}
		return Task.RECUR_TYPE.NULL;
	}
	
	private void appendToResult() {
		_taskList.add(_userTask);
	}

	private void storeNewTask() {
		_taskController.addTask(_userTask);
	}

	@Override
	public ArrayList<Task> undo() {
		assert(_userTask != null);
		deleteTask();
		clearResult();
		appendToResult();
		return _taskList;
	}

	private void deleteTask() {
		int taskID = _userTask.getTaskId();
		_taskController.deleteTask(taskID);
	}
}
