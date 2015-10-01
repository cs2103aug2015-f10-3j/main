package storage;

import java.io.File;
import java.util.ArrayList;

import logic.data.Task;

import org.w3c.dom.Document;

public class StorageAPI {
    /*** Variables ***/
    protected StorageController sController;
    
    /*** Constructor ***/
    public StorageAPI() {
        sController = new StorageController();
    }
    
    /*** Methods ***/
    /**
     * This method check if the XML file exist and create it if not
     * 
     * @return       file containing Task objects
     */
    public File getFile() {
        return sController.getFile();
    }
    
    /**
     * This method reads all the Task objects to an ArrayList
     * 
     * @return       ArrayList of all Task objects
     */
    public ArrayList<Task> readTask() {
        return sController.readTask();
    }
    
    /**
     * This method returns a string representation of the XML version of the Task objects
     * 
     * @param  task  an ArrayList of all Task objects
     * @return       a Document representing the XML document
     */
    public Document parseTask(ArrayList<Task> taskList) {
        return sController.parseTask(taskList);
    }
    
    /**
     * This method parse the raw XML file to XML Document of Nodes
     * 
     * @return       XML Document of Nodes
     */
    public Document parseXml() {
        return sController.parseXml();
    }
    
    /**
     * This method writes all Task objects to file
     * 
     * @param  doc  a Document representing the XML document
     * @return      <code>true</code> if the tasks are successfully stored; 
     *              <code>false</code> otherwise.
     */
    public boolean writeXml(Document doc) {
        return sController.writeXml(doc);
    }
}
