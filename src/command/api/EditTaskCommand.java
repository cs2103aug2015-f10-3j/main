package command.api;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.time.LocalDateTime;

import task.api.TaskController;
import task.entity.DeadlineTask;
import task.entity.FloatingTask;
import task.entity.Task;
import task.entity.Task.RECUR_TYPE;
import task.entity.TimedTask;

import common.exception.InvalidCommandFormatException;
import common.exception.InvalidPriorityException;
import common.exception.NoSuchTaskException;
import common.exception.UpdateTaskException;
import common.util.DateTimeHelper;

public class EditTaskCommand extends Command {

    /*** Variables ***/
    private static final Logger LOGGER = Logger.getLogger(EditTaskCommand.class.getName());
    private static final String TASK_TYPE_DEADLINE = "deadline";
    private static final String TASK_TYPE_TIMED = "timed";
    private static final String TASK_TYPE_FLOATING = "floating";
    private static final String TASK_TYPE_INVALID = "invalid";
    
    private static final String ERROR_MSG_GENERIC = "Error has occurred due to unexpected behavior";
    private static final String ERROR_MSG_ADD_START_TO_FLOAT = "You cannot add only a start Time/Date to a Floating Task!";
    private static final String ERROR_MSG_NO_SUCH_TASK = "Task with the following Task ID does not exist!";
    private static final String ERROR_MSG_UPDATE_TASK_FAIL = "Failed to store updated Task to Storage";
    private static final String ERROR_MSG_INVALID_PRIORITY = "The priority specified is invalid!";
    
    private static final String LOG_MSG_INFO_STORE_TASK = "EditTaskCommand: Storing updated task to Storage\n";
    private static final String LOG_MSG_SEVERE_GET_TASK_FAIL = "Executing EditTaskCommand getTaskFromStorage(): Retrieve Task with taskId -> %1s failed";
    private static final String LOG_MSG_SEVERE_STORE_TASK_FAIL = "Executing EditTaskCommand storeTaskToStorage(): Storing Task with taskId -> %1s failed";
    private static final String LOG_MSG_SEVERE_ADD_START_TO_FLOAT = "Executing EditTaskCommand: User attempt to add only start date/time to Floating Task";
    
    private static final int PRIORITY_MIN_LEVEL = 1;
    private static final int PRIORITY_MAX_LEVEL = 3;
    private static final int PRIORITY_NONE = -1;
    
    private ArrayList<Task> _executionResult;
    private int _taskId;
    private int _newPriority;
    private boolean _isValidEditCommand;
    private boolean _newRecurStatus;
    private RECUR_TYPE _newRecurType;
    private Task _originalTask;
    private Task _editedTask;
    private String _originalTaskType;
    private String _newDescription;
    private LocalDateTime _newStart;
    private LocalDateTime _newEnd;
    private LocalDateTime _newReminder;

    public EditTaskCommand() {
        _originalTaskType = null;
        _newDescription = null;
        _newStart = null;
        _newEnd = null;
        _newReminder = null;
        _newPriority = -1;
        _newRecurStatus = false;
        _isValidEditCommand = true;
        _executionResult = new ArrayList<Task>();
    }

    /*** Methods ***/
    /**
     * This method modifies a Task based on user input for the edit command
     * 
     * @return an ArrayList of Task objects that contains the modified Task
     *         object
     * @throws Exception 
     */
    @Override
    public ArrayList<Task> execute() throws Exception {
        retrieveOptions();
        getTaskFromStorage(_taskId);
        determineOriginalTaskType();
        createEditedTask();
        prepareExecutionResult();
        
        return _executionResult;
    }

    /**
     * This method reverse the previous execute() of the previous
     * EditTaskCommand
     * 
     * @return an ArrayList of Task objects that contains the modified Task
     *         object
     * @throws UpdateTaskException
     */
    public ArrayList<Task> undo() throws UpdateTaskException {
        prepareUndoTask();
        prepareExecutionResult();
        
        return _executionResult;
    }

    /**
     * This method modifies a Task based on user input for the edit command
     * 
     * @throws UpdateTaskException
     */
    private void prepareExecutionResult() throws UpdateTaskException {
        if (_isValidEditCommand) {
            _executionResult.clear();
            _executionResult.add(_editedTask);
            storeTaskToStorage(_editedTask);
        } else {
            _executionResult = null;
        }
    }
    
    /**
     * This method retrieves the parsed parameters stored in the Option object
     * in this EditTaskCommand
     */
    private void retrieveOptions() {
        setTaskIdOption();
        setNewDescriptionOption();
        setNewStartOption();
        setNewEnd();
        setNewReminder();
        setNewPriority();
        setNewRecurStatus();
    }

    private void prepareUndoTask() {
        Task temp = _editedTask;
        _editedTask = _originalTask;
        _originalTask = temp;
    }
    
    private void setTaskIdOption() {
        _taskId = getOption("edit").getIntegerValue();
    }

    private void setNewPriority() {
        if (hasOption("priority")) {
            _newPriority = getOption("priority").getIntegerValue();
        }
    }

    private void setNewReminder() {
        if (hasOption("remind")) {
            _newReminder = getOption("remind").getDateValue();
        }
    }

    private void setNewEnd() {
        if (hasOption("end")) {
            _newEnd = getOption("end").getDateValue();
        }
    }

    private void setNewStartOption() {
        if (hasOption("start")) {
            _newStart = getOption("start").getDateValue();
        }
    }

    private void setNewDescriptionOption() {
        if (hasOption("desc")) {
            _newDescription = getOption("desc").getStringValue();
        }
    }

    private void setNewRecurStatus() {
        if (hasOption("every")) {
            _newRecurStatus = true;
            _newRecurType = Task.determineRecurType(getOption("every").getStringValue());
        }
    }
    
    private void determineOriginalTaskType() {
        switch (_originalTask.getType()) {
        case FLOATING :
            _originalTaskType = TASK_TYPE_FLOATING;
            break;
        case DEADLINE :
            _originalTaskType = TASK_TYPE_DEADLINE;
            break;
        case TIMED :
            _originalTaskType = TASK_TYPE_TIMED;
            break;
        default :
            _originalTaskType = TASK_TYPE_INVALID;
            _isValidEditCommand = false;
        }
    }

    /**
     * Instantiates the task object to be modified to the appropriate Task type
     * with the appropriate attributes
     * @throws Exception 
     */
    private void createEditedTask() throws Exception {
        switch (determineEditedTaskType()) {
            case TASK_TYPE_FLOATING :
                createNewFloatingTask();
                break;
            case TASK_TYPE_DEADLINE :
                createNewDeadlineTask();
                break;
            case TASK_TYPE_TIMED :
                createNewTimedTask();
                break;    
            case TASK_TYPE_INVALID :
                throw new Exception(ERROR_MSG_GENERIC);
        }
    }

    /**
     * Determines the resulting Task type of the Task object to be modified
     * based on user input from the edit command
     * 
     * @return a String that indicates the appropriate Task type for the Task
     *         object to be modified
     * @throws Exception 
     */
    private String determineEditedTaskType() throws Exception {
        switch (_originalTaskType) {
            case TASK_TYPE_FLOATING :
                // If no start/end date/time is specified, task.type is still Floating
                if (_newStart == null && _newEnd == null) {
                    return TASK_TYPE_FLOATING;
                 // If only an end date/time is specified, task.type is now a Deadline task
                } else if (_newStart == null) { 
                    return TASK_TYPE_DEADLINE;
                 // If both new and end date/time is specified, task.type is now a Timed task
                } else if (_newStart != null && _newEnd != null) {
                    return TASK_TYPE_TIMED;
                } else if (_newStart != null && _newEnd == null) {
                    LOGGER.log(Level.SEVERE, LOG_MSG_SEVERE_ADD_START_TO_FLOAT);
                    throw new InvalidCommandFormatException(ERROR_MSG_ADD_START_TO_FLOAT);
                }
    
            case TASK_TYPE_DEADLINE :
                if (_newStart == null) {
                    return TASK_TYPE_DEADLINE;
                } else {
                    return TASK_TYPE_TIMED;
                }
    
            case TASK_TYPE_TIMED :
                return TASK_TYPE_TIMED;
    
            default :
                throw new Exception(ERROR_MSG_GENERIC);
        }
    }
    
    private void createNewFloatingTask() throws InvalidPriorityException {
        _editedTask = new FloatingTask(_originalTask.getTaskId(), getEditedTaskDescription(),
                _originalTask.getCreatedAt(), _originalTask.isComplete(), getEditedTaskPriority(),
                _originalTask.getTags());
    }

    private void createNewDeadlineTask() throws Exception {
        _editedTask = new DeadlineTask(_originalTask.getTaskId(), getEditedTaskDescription(),
                _originalTask.getCreatedAt(), getEditedTaskEnd(), getEditedTaskReminder(), _originalTask.isComplete(),
                getEditedTaskPriority(), _originalTask.getTags(),getEditedTaskRecurStatus(), getEditedTaskRecurType());
    }

    private void createNewTimedTask() throws Exception {
        _editedTask = new TimedTask(_originalTask.getTaskId(), getEditedTaskDescription(), _originalTask.getCreatedAt(),
                getEditedTaskStart(), getEditedTaskEnd(), getEditedTaskReminder(), _originalTask.isComplete(),
                getEditedTaskPriority(), _originalTask.getTags(),getEditedTaskRecurStatus(), getEditedTaskRecurType());
    }

    /*** Setter and Getter Methods ***/
    
    private String getEditedTaskDescription() {
        // If user specified a new description, use this
        if (_newDescription != null) {
            return _newDescription;
        } else {
            // Otherwise, use the original description
            return _originalTask.getDescription();
        }
    }

    private LocalDateTime getEditedTaskStart() {
        // If user specified a new Start date/time, use this
        if (_newStart != null) {
            return _newStart;
        } else {
            // Otherwise, retrieve original Task casted to a TimedTask as only this Task
            // type has Start date/time
            return getTimedTaskCastedOriginalTask().getStart();
        }
    }

    private LocalDateTime getEditedTaskEnd() {
        LocalDateTime newEditedTaskEnd = null;
        // If user specified a new End date/time, use this
        if (_newEnd != null) {
            // If user did not specify a new Reminder, change it to a time that is 
            // 5 minutes before the new specified End/date time
            if (_newReminder == null) {
                _newReminder = DateTimeHelper.addMinutes(_newEnd, -5);
            }
            return _newEnd;
        } else if (isOriginalTaskDeadline()) {
            // Otherwise, if this Task is a DeadlineTask, get DeadlineTask casted original 
            // Task and retrieve the End date/time
            newEditedTaskEnd = getDeadlineTaskCastedOriginalTask().getEnd();
        } else if (isOriginalTaskTimed()) {
            // Otherwise, if this Task is a TimedTask, get TimedTask casted original 
            // Task and retrieve the End date/time
            newEditedTaskEnd = getTimedTaskCastedOriginalTask().getEnd();
        }
        
        return newEditedTaskEnd;
    }

    private LocalDateTime getEditedTaskReminder() {
        LocalDateTime newEditedTaskReminder = null;
        // If user specified a new Reminder LocalDateTime, use this
        if (_newReminder != null) {
            return _newReminder;
        } else if (isOriginalTaskFloating()) {
            // Otherwise, if this Task is a Floating Task, set a new Reminder that will
            // be 5 minutes before the End date/time
            newEditedTaskReminder = DateTimeHelper.addMinutes(getEditedTaskEnd(), -5);
        } else if (isOriginalTaskDeadline()) {
            // Otherwise, if this Task is a DeadlineTask, get the DeadlineTask casted
            // original Task and retrieve its original Reminder
            newEditedTaskReminder = getDeadlineTaskCastedOriginalTask().getReminder();
        } else if (isOriginalTaskTimed()) {
            // Otherwise, if this Task is a TimedTask, get the TimedTask casted
            // original Task and retrieve its original Reminder
            newEditedTaskReminder = getTimedTaskCastedOriginalTask().getReminder();
        }
        
        return newEditedTaskReminder;
    }

    private int getEditedTaskPriority() throws InvalidPriorityException {
        if (_newPriority == PRIORITY_NONE) {
            return _originalTask.getPriority();
        } else if (_newPriority < PRIORITY_MIN_LEVEL || _newPriority > PRIORITY_MAX_LEVEL) {
            // If new user specified priority is beyond the valid range [1..3], throw Error
            throw new InvalidPriorityException(ERROR_MSG_INVALID_PRIORITY);
        } else {
            return _newPriority;
        }
    }

    private boolean getEditedTaskRecurStatus() throws Exception {
        boolean newEditedRecurStatus;
        if (_newRecurStatus != false) {
            return true;
        } else if (isOriginalTaskFloating()) {
            newEditedRecurStatus = false;
        } else if (isOriginalTaskDeadline()) {
            newEditedRecurStatus = getDeadlineTaskCastedOriginalTask().isRecurring();
        } else if (isOriginalTaskTimed()) {
            newEditedRecurStatus = getTimedTaskCastedOriginalTask().isRecurring();
        } else {
            throw new Exception(ERROR_MSG_GENERIC);
        }
        return newEditedRecurStatus;
    }
    
    private RECUR_TYPE getEditedTaskRecurType() throws Exception {
        RECUR_TYPE newEditedRecurType;
        if (_newRecurStatus != false) {
            return _newRecurType;
        } else if (isOriginalTaskFloating()) {
            return newEditedRecurType = RECUR_TYPE.NULL;
        } else if (isOriginalTaskDeadline()) {
            newEditedRecurType = getDeadlineTaskCastedOriginalTask().getRecurPeriod();
        } else if (isOriginalTaskTimed()) {
            newEditedRecurType = getTimedTaskCastedOriginalTask().getRecurPeriod();
        } else {
            // Error has occur
            throw new Exception(ERROR_MSG_GENERIC);
        }
        return newEditedRecurType;
    }
    
    private DeadlineTask getDeadlineTaskCastedOriginalTask() {
        DeadlineTask castedOriginalTask = (DeadlineTask) _originalTask;
        
        return castedOriginalTask;
    }

    private TimedTask getTimedTaskCastedOriginalTask() {
        TimedTask castedOriginalTask = (TimedTask) _originalTask;
        
        return castedOriginalTask;
    }

    /**
     * This method calls the storageAPI to retrieve the Task object with this taskId
     * 
     * @param taskId
     *            the id of the Task object to retrieve
     * @throws NoSuchTaskException
     */
    private void getTaskFromStorage(int taskId) throws NoSuchTaskException {
        _originalTask = TaskController.getInstance().getTask(taskId);
        if (_originalTask == null) {
            LOGGER.log(Level.SEVERE,String.format(LOG_MSG_SEVERE_GET_TASK_FAIL, taskId));
            throw new NoSuchTaskException(ERROR_MSG_NO_SUCH_TASK);
        }
    }

    private boolean isOriginalTaskFloating() {
        return _originalTaskType.equals(TASK_TYPE_FLOATING);
    }
    
    private boolean isOriginalTaskDeadline() {
        return _originalTaskType.equals(TASK_TYPE_DEADLINE);
    }
    
    private boolean isOriginalTaskTimed() {
        return _originalTaskType.equals(TASK_TYPE_TIMED);
    }
    /**
     * This method calls the storageAPI to store this Task object
     * 
     * @param task
     *            the Task object that is updated
     * @throws UpdateTaskException
     */
    private void storeTaskToStorage(Task task) throws UpdateTaskException {
        LOGGER.info(LOG_MSG_INFO_STORE_TASK);
        if (!TaskController.getInstance().updateTask(task)) {
            LOGGER.log(Level.SEVERE, String.format(LOG_MSG_SEVERE_STORE_TASK_FAIL, _taskId));
            throw new UpdateTaskException(ERROR_MSG_UPDATE_TASK_FAIL);
        }
    }
}