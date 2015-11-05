package main.paddletask.command.api;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.paddletask.common.exception.NoSuchTaskException;
import main.paddletask.common.exception.UpdateTaskException;

import main.paddletask.task.api.TaskController;
import main.paddletask.task.entity.Task;

public class UntagTaskCommand extends Command {

    /*** Variables ***/
    private static final Logger LOGGER = Logger.getLogger(TagTaskCommand.class.getName());
    
    private static final String ERROR_MSG_NO_SUCH_TASK = "Task with the following Task ID does not exist!";
    private static final String ERROR_MSG_UPDATE_TASK_FAIL = "Failed to store updated Task to Storage";
    
    private static final String LOG_MSG_INFO_STORE_TASK = "UntagTaskCommand: Storing updated task to Storage\n";
    private static final String LOG_MSG_SEVERE_GET_TASK_FAIL = "Executing UntagTaskCommand: Retrieve Task with taskId -> %1s failed";
    private static final String LOG_MSG_SEVERE_STORE_TASK_FAIL = "Executing UntagTaskCommand: storeTaskToStorage(): Storing Task with taskId -> %1s failed\n";
    
    private ArrayList<String> _tagsToRemove;
    private ArrayList<String> _modifiedTaglist;
    private ArrayList<String> _originalTaglist;
    private ArrayList<Task> _executionResult;
    private Task _taskToEdit;
    private static int _taskId;

    public UntagTaskCommand() {
        _modifiedTaglist = new ArrayList<String>();
        _tagsToRemove = new ArrayList<String>();
        _executionResult = new ArrayList<Task>();
    }

    /*** Methods ***/

    @Override
    public ArrayList<Task> execute() throws Exception {
        retrieveOptions();
        getTaskFromStorage(_taskId);
        processTags();
        prepareExecutionResult();
        return _executionResult;
    }

    public void retrieveOptions() {
        setTaskIdOption();
        setTagsToRemoveOption();
    }

    public void prepareExecutionResult() throws UpdateTaskException {
        assert _modifiedTaglist != null;
        saveTagToTask(_modifiedTaglist);

        assert _taskToEdit != null;
        _executionResult.add(_taskToEdit);
    }

    @Override
    public ArrayList<Task> undo() throws Exception {
        prepareUndoTags();
        prepareExecutionResult();
        return _executionResult;
    }

    private void prepareUndoTags() {
        ArrayList<String> temp = _originalTaglist;
        _originalTaglist = _modifiedTaglist;
        _modifiedTaglist = temp;
    }

    private void processTags() throws UpdateTaskException {
        archiveOriginalTaglist(getExistingTaglist());
        updateOriginalTaglist();
    }

    private void updateOriginalTaglist() {
        _modifiedTaglist = getExistingTaglist();
        _modifiedTaglist.removeAll(_tagsToRemove);
    }

    private void archiveOriginalTaglist(ArrayList<String> existingTags) {
        _originalTaglist = new ArrayList<String>();
        
        for (String existingTag : existingTags) {
            _originalTaglist.add(existingTag);
        }
    }

    private ArrayList<String> getExistingTaglist() {
        assert _taskToEdit != null;
        ArrayList<String> existingTags = _taskToEdit.getTags();
        
        return existingTags;
    }

    private void setTaskIdOption() {
        _taskId = getOption("untag").getIntegerValue();
    }
    
    private void setTagsToRemoveOption() {
        if (hasOption("#")) {
            int numTags = getOption("#").getValuesCount();
            for (int i = 0; i < numTags; i++) {
                _tagsToRemove.add((getOption("#").getStringValue(i)));
            }
        }
    }
    
    private void saveTagToTask(ArrayList<String> updatedTaskTags) throws UpdateTaskException {
        assert _taskToEdit != null;
        _taskToEdit.setTags(updatedTaskTags);
        storeTaskToStorage(_taskToEdit);
    }

    private void getTaskFromStorage(int taskId) throws NoSuchTaskException {
        _taskToEdit = TaskController.getInstance().getTask(taskId);
        if (_taskToEdit == null) {
            LOGGER.log(Level.SEVERE, String.format(LOG_MSG_SEVERE_GET_TASK_FAIL, taskId));
            throw new NoSuchTaskException(ERROR_MSG_NO_SUCH_TASK);
        }
    }

    private void storeTaskToStorage(Task task) throws UpdateTaskException {
        LOGGER.info(LOG_MSG_INFO_STORE_TASK);
        if (!TaskController.getInstance().updateTask(task)) {
            LOGGER.log(Level.SEVERE, String.format(LOG_MSG_SEVERE_STORE_TASK_FAIL, _taskId));
            throw new UpdateTaskException(ERROR_MSG_UPDATE_TASK_FAIL);
        }
    }

}
