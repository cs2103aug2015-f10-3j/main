//@@author A0126332R
package main.paddletask.command.api;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import main.paddletask.storage.api.StorageController;
import main.paddletask.task.entity.Task;

public class SetDirectoryCommand extends Command {

    /*** Variables ***/
    private static final String NEW_PATH_ERROR = "Error in setting new path: ";
    private static final String NEW_PATH_SET = "New directory set: ";
    private static final String KEYWORD_SET_DIRECTORY = "setdirectory";
    private static String _oldPath = "";
    
    /*** Methods ***/
    /**
     * This method set the xml data file path based on user input,
     * copy the existing file to the new path
     * and call the observer to display the status of the operation
     * 
     */
    @Override
    public ArrayList<Task> execute() throws Exception {
        StorageController storageController = StorageController.getInstance();
        String newPath = null;
        String comments = null;
        
        // Get new path
        if (hasOption(KEYWORD_SET_DIRECTORY)) {
            newPath = getOption(KEYWORD_SET_DIRECTORY).getStringValue();
        }
        
        // Store old path for undoing
        byte[] content = storageController.getFileInBytes(StorageController.CONFIG_FILE);
        _oldPath = new String(content, StandardCharsets.UTF_8);
        int endIndex = _oldPath.lastIndexOf(File.separator);
        if (endIndex != -1) {
            _oldPath = _oldPath.substring(0, endIndex);
        }
        
        // Set new path
        boolean success = storageController.setDirectory(newPath);
        if (success) {
            comments = NEW_PATH_SET + newPath;
        } else {
            comments = NEW_PATH_ERROR + newPath;
        }
        setChanged();
        notifyObservers(comments);
        return null;
    }
    
    /**
     * This method undo the setting of the new file path
     * and revert to the existing file path
     * 
     */
    @Override
    public ArrayList<Task> undo() throws Exception {
        StorageController storageController = StorageController.getInstance();
        String comments = null;
        
        // Set new path
        boolean success = storageController.setDirectory(_oldPath);
        if (success) {
            comments = NEW_PATH_SET + _oldPath;
        } else {
            comments = NEW_PATH_ERROR + _oldPath;
        }
        setChanged();
        notifyObservers(comments);
        return null;
    }
    
}
