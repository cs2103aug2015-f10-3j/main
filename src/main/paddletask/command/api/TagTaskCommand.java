//@@author A0133662J
package main.paddletask.command.api;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.paddletask.common.exception.NoSuchTaskException;
import main.paddletask.common.exception.UpdateTaskException;

import main.paddletask.task.api.TaskController;
import main.paddletask.task.entity.Task;

public class TagTaskCommand extends Command {

    /*** Variables ***/
    private static final Logger LOGGER = Logger.getLogger(EditTaskCommand.class.getName());
    
    private static final String ERROR_MSG_NO_SUCH_TASK = "Task with the following Task ID does not exist!";
    private static final String ERROR_MSG_UPDATE_TASK_FAIL = "Failed to store updated Task to Storage";
    
    private static final String LOG_MSG_INFO_STORE_TASK = "TagTaskCommand: Storing updated task to Storage\n";
    private static final String LOG_MSG_SEVERE_GET_TASK_FAIL = "Executing TagTaskCommand: Retrieve Task with taskId -> %1s failed";
    private static final String LOG_MSG_SEVERE_STORE_TASK_FAIL = "Executing TaskTaskCommand: storeTaskToStorage(): Storing Task with taskId -> %1s failed\n";
    
    private ArrayList<String> _tagsToAdd;
    private ArrayList<String> _modifiedTaglist;
    private ArrayList<String> _originalTaglist;
    private ArrayList<Task> _executionResult;
    private Task _taskToEdit;
    private int _taskId;

    public TagTaskCommand() {
        _modifiedTaglist = new ArrayList<String>();
        _tagsToAdd = new ArrayList<String>();
        _executionResult = new ArrayList<Task>();
    }

    @Override
    public ArrayList<Task> execute() throws Exception {
        retrieveOptions();
        getTaskFromStorage(_taskId);
        processTags();
        prepareExecutionResult();
        return _executionResult;
    }

    @Override
    public ArrayList<Task> undo() throws Exception {
        prepareUndoTags();
        prepareExecutionResult();
        return _executionResult;
    }

    private void retrieveOptions() {
        setTaskIdOption();
        setTagsToAddOption();
    }

    private void prepareExecutionResult() throws UpdateTaskException {
        assert _modifiedTaglist != null;
        saveTagToTask(_modifiedTaglist);

        assert _taskToEdit != null;
        _executionResult.add(_taskToEdit);
    }

    private void prepareUndoTags() {
        ArrayList<String> temp = _originalTaglist;
        _originalTaglist = _modifiedTaglist;
        _modifiedTaglist = temp;
    }

    private void processTags() throws UpdateTaskException {
        archiveOriginalTags(getExistingTags());
        updateOriginalTags();
    }

    private void updateOriginalTags() {
        _modifiedTaglist = getExistingTags();
        
        for (String tagToAdd : _tagsToAdd) {
            boolean hasTag = false;
            for (String existingTag : _originalTaglist) {
                if (tagToAdd.equalsIgnoreCase(existingTag)) {
                    hasTag = true;
                }
            }
            
            if (!hasTag) {
                _modifiedTaglist.add(tagToAdd);
            }
        }
    }

    private void archiveOriginalTags(ArrayList<String> existingTags) {
        _originalTaglist = new ArrayList<String>();
        
        for (String existingTag : existingTags) {
            _originalTaglist.add(existingTag);
        }
    }

    private ArrayList<String> getExistingTags() {
        assert _taskToEdit != null;
        ArrayList<String> existingTags = _taskToEdit.getTags();
        
        return existingTags;
    }

    private void setTaskIdOption() {
        _taskId = getOption("tag").getIntegerValue();
    }
    
    private void setTagsToAddOption() {
        if (hasOption("#")) {
            int numTags = getOption("#").getValuesCount();
            for (int i = 0; i < numTags; i++) {
                _tagsToAdd.add((getOption("#").getStringValue(i)));
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
