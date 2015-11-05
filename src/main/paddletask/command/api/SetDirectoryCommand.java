//@@author A0126332R
package main.paddletask.command.api;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import main.paddletask.storage.api.StorageController;
import main.paddletask.task.entity.Task;

public class SetDirectoryCommand extends Command {
    
    /*** Variables ***/
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
        
        // Set new path
        boolean success = storageController.setDirectory(newPath);
        if (success) {
            comments = "New path set: " + newPath;
        } else {
            comments = "Error in setting new path: " + newPath;
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
            comments = "New path set: " + _oldPath;
        } else {
            comments = "Error in setting new path: " + _oldPath;
        }
        setChanged();
        notifyObservers(comments);
        return null;
    }
    
}
