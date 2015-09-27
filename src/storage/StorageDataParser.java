package storage;

import java.util.ArrayList;

import org.w3c.dom.Document;

import storage.data.Task;


public class StorageDataParser {
    /*** Variables ***/
    private static final String FILE_NAME = "tasks.xml";
    
    /*** Methods ***/
    /**
     * This method check if the XML file exist and create it if not
     * 
     * @return       <code>true</code> if the task is exist, or is successfully created; 
     *               <code>false</code> otherwise.
     */
    protected boolean createFileIfNotExist() {
        //TODO
        return false;
    }
    
    /**
     * This method reads all the Task objects to an ArrayList
     * 
     * @return       ArrayList of all Task objects
     */
    protected ArrayList<Task> readTask() {
        //TODO
        return null;
    }
    
    /**
     * This method returns a string representation of the XML version of the Task objects
     * 
     * @param  task  an ArrayList of all Task objects
     * @return        a string representation of the XML file
     */
    protected String parseTasks(ArrayList<Task> taskList) {
        //TODO
        return null;
    }
    
    /**
     * This method parse the raw XML file to XML Document of Nodes
     * 
     * @return       XML Document of Nodes
     */
    protected Document parseXml() {
        //TODO
        return null;
    }
    
    /**
     * This method writes all Task objects to file
     * 
     * @param  string  string representation of the XML file
     * @return       <code>true</code> if the tasks are successfully stored; 
     *               <code>false</code> otherwise.
     */
    protected boolean writeXml(ArrayList<Task> taskList) {
        //TODO
        return false;
    }
}
