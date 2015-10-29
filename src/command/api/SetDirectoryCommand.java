package command.api;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import storage.api.StorageController;
import task.entity.Task;

public class SetDirectoryCommand extends Command {
    private static final String KEYWORD_SET_DIRECTORY = "setdirectory";
    private static String oldPath = "";
    
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
        oldPath = new String(content, StandardCharsets.UTF_8);
        
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
    
    @Override
    public ArrayList<Task> undo() throws Exception {
        StorageController storageController = StorageController.getInstance();
        String comments = null;
        
        // Set new path
        boolean success = storageController.setDirectory(oldPath);
        if (success) {
            comments = "New path set: " + oldPath;
        } else {
            comments = "Error in setting new path: " + oldPath;
        }
        setChanged();
        notifyObservers(comments);
        return null;
    }
    
}
