//@@author A0125473H
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
	/*** Variables ***/
	private ArrayList<Task> _taskList = new ArrayList<Task>();
	private TaskController _taskController = TaskController.getInstance();
	//stored to keep track of the created user task, supporting undo of function
	private Task _userTask = null;

	private static final int DEFAULT_PRIORITY = 3;
	private static final int FIVE_MINUTES_BEFORE = -5;

	/*** Methods ***/
    /**
     * This method adds a new Task based on user input for the add command
     * 
     * @return an ArrayList of Task objects that contains the new Task
     *         object
     * @throws TaskAddFailedException 
     */
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
	
	/**
     * This method is invoked to clear the
     * Arraylist of tasks used by execute
     */
	private void clearResult() {
		_taskList.clear();
	}
	
	/**
     * @return       <code>Task</code> if the task is successfully created;
     *               <code>Null</code> otherwise.
     */
	private Task createNewTask() {
		assert(hasOption(OPTIONS.ADD.toString()));
		Task userTask = createTaskByType();
		return userTask;
	}

	/**
     * This method is invoked to create a Task object
     * based on the type specified by the user
     * 
     * @return       <code>Task</code> if the task is successfully created;
     *               <code>Null</code> otherwise.
     */
	private Task createTaskByType() {
		if (hasOption(OPTIONS.BY.toString())) {
			return createDeadlineTask();
		} else if (hasOption(OPTIONS.BETWEEN.toString()) && hasOption(OPTIONS.AND.toString())) {
			return createTimedTask();
		} else {
			return createFloatingTask();
		}
	}

	/**
     * @return       <code>Task</code> if the task is successfully created;
     *               <code>Null</code> otherwise.
     */
	private Task createDeadlineTask() {
		String description = getTaskDescription();
		Integer priority = getTaskPriority();
		LocalDateTime deadline = getTaskDeadline();
		LocalDateTime reminder = calculateTaskReminderDate(deadline);
		boolean recurring = isTaskRecurring();
		RECUR_TYPE recurType = getTaskRecurrenceType(recurring);
		return new DeadlineTask(description, deadline, reminder, priority, recurring, recurType);
		
	}

	/**
     * @return       <code>Task</code> if the task is successfully created;
     *               <code>Null</code> otherwise.
     */
	private Task createTimedTask() {
		String description = getTaskDescription();
		Integer priority = getTaskPriority();
		LocalDateTime startDate = getTaskStartDate();
		LocalDateTime deadline = getTaskEndDate();
		LocalDateTime reminder = calculateTaskReminderDate(deadline);
		boolean recurring = isTaskRecurring();
		RECUR_TYPE recurType = getTaskRecurrenceType(recurring);
		return new TimedTask(description, startDate, deadline, reminder, priority, recurring, recurType);
	}

	/**
     * @return       <code>Task</code> if the task is successfully created;
     *               <code>Null</code> otherwise.
     */
	private Task createFloatingTask() {
		String description = getTaskDescription();
		Integer priority = getTaskPriority();
		return new FloatingTask(description, priority);
	}

	/**
     * @return       <code>Task</code> if the task is successfully created;
     *               <code>Null</code> otherwise.
     */
	private String getTaskDescription() {
		return getOption(OPTIONS.ADD.toString()).getStringValue();
	}

	/**
     * @return       <code>int priority</code> if the priority option exists;
     *               <code>DEFAULT_PRIORITY</code> otherwise.
     */
	private Integer getTaskPriority() {
		if (hasOption(OPTIONS.PRIORITY.toString())) {
			return getOption(OPTIONS.PRIORITY.toString()).getIntegerValue();
		} else {
			return DEFAULT_PRIORITY;
		}
	}

	/**
     * @return LocalDateTime representing the start date
     */
	private LocalDateTime getTaskStartDate() {
		return getOption(OPTIONS.BETWEEN.toString()).getDateValue();
	}

	/**
     * @return LocalDateTime representing the end date
     */
	private LocalDateTime getTaskEndDate() {
		return getOption(OPTIONS.AND.toString()).getDateValue();
	}

	/**
     * @return LocalDateTime representing the end date
     */
	private LocalDateTime getTaskDeadline() {
		return getOption(OPTIONS.BY.toString()).getDateValue();
	}

	/**
     * This method is invoked to calculate the default reminder time from
     * deadline
     * 
     * @param  LocalDateTime  deadline of the task
     * @return       <code>LocalDateTime</code> if date was specified by user input;
     *               <code>Deadline - 5 minutes</code> otherwise.
     */
	private LocalDateTime calculateTaskReminderDate(LocalDateTime deadline) {
		if (hasOption(OPTIONS.REMIND.toString())) {
			return getOption(OPTIONS.REMIND.toString()).getDateValue();
		} else {
			return DateTimeHelper.addMinutes(deadline, FIVE_MINUTES_BEFORE);
		}
	}

	/**
     * @return       <code>true</code> if the task is recurring;
     *               <code>false</code> otherwise.
     */
	private boolean isTaskRecurring() {
		return hasOption(OPTIONS.EVERY.toString());
	}

	/**
     * This method is invoked to retrieve the recurrence type of the task
     * 
     * @param  boolean  recurring
     * @return       <code>RECUR_TYPE.*</code> if the task is recurring;
     *               <code>RECUR_TYPE.NULL</code> otherwise.
     */
	private RECUR_TYPE getTaskRecurrenceType(boolean recurring) {
		if (recurring) {
			String recurranceType = getOption(OPTIONS.EVERY.toString()).getStringValue();
			return Task.determineRecurType(recurranceType);
		}
		return Task.RECUR_TYPE.NULL;
	}

	private void appendToResult() {
		_taskList.add(_userTask);
	}

	/**
     * This method adds the Task object to file
     */
	private void storeNewTask() {
		_taskController.addTask(_userTask);
	}

	/**
     * This method is invoked to undo changes made by execute()
     * 
     * @return an ArrayList of Task objects that contains the new Task
     *         object
     */
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
