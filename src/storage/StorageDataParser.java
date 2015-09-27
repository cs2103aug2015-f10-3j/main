package storage;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import storage.data.DeadlineTask;
import storage.data.FloatingTask;
import storage.data.Task;
import storage.data.TimedTask;


public class StorageDataParser {
    /*** Variables ***/
    private static final String FILE_NAME = "tasks.xml";
    
    /*** Methods ***/
    /**
     * This method check if the XML file exist and create it if not
     * 
     * @return       file containing Task objects
     */
    protected File getFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            // Create file if it does not exist
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return file;
    }
    
    /**
     * This method reads all the Task objects to an ArrayList
     * 
     * @return       ArrayList of all Task objects
     */
    protected ArrayList<Task> readTask() {
        ArrayList<Task> taskList = new ArrayList<Task>();
        try {
            Document doc = parseXml();
            
            //~ System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("task");
            
            for (int temp = 0; temp < nodeList.getLength(); temp++) {
                Node nNode = nodeList.item(temp);
                Task task = null;
                //System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    String taskId = eElement.getAttribute("taskId");
                    int taskId_int = Integer.valueOf(taskId);
                    String description = eElement.getElementsByTagName("description").item(0).getTextContent();
                    String createdAt = eElement.getElementsByTagName("createdAt").item(0).getTextContent();
                    LocalDateTime createdAt_localdatetime = LocalDateTime.parse(createdAt, formatter);
                    String type = eElement.getElementsByTagName("type").item(0).getTextContent();
                    String start = eElement.getElementsByTagName("start").item(0).getTextContent();
                    LocalDateTime start_localdatetime = LocalDateTime.parse(start, formatter);
                    String end = eElement.getElementsByTagName("end").item(0).getTextContent();
                    LocalDateTime end_localdatetime = LocalDateTime.parse(end, formatter);
                    String complete = eElement.getElementsByTagName("complete").item(0).getTextContent();
                    boolean complete_boolean = Boolean.valueOf(complete);
                    
                    /*
                    System.out.println("taskId: " + taskId);
                    System.out.println("description: " + description);
                    System.out.println("createdAt: " + createdAt);
                    System.out.println("type: " + type);
                    System.out.println("start: " + start);
                    System.out.println("end: " + end);
                    System.out.println("complete: " + complete);
                    */
                    
                    switch (type) {
                        case "floating":
                            task = new FloatingTask(taskId_int, description, createdAt_localdatetime);
                            break;
                        case "timed":
                            task = new TimedTask(taskId_int, description, createdAt_localdatetime, start_localdatetime, end_localdatetime);
                            break;
                        case "deadline":
                            task = new DeadlineTask(taskId_int, description, createdAt_localdatetime, end_localdatetime, complete_boolean);
                            break;
                        default:
                            break;
                    }
                }
                
                if (task != null) {
                    taskList.add(task);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
        return taskList;
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
        Document doc = null;
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(getFile());
            
            // Ensures that the XML DOM view of a document is identical
            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return doc;
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
