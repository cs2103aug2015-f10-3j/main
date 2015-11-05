//@@author A0126332R
package main.paddletask.storage.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import main.paddletask.common.util.DateTimeHelper;
import main.paddletask.task.entity.DeadlineTask;
import main.paddletask.task.entity.FloatingTask;
import main.paddletask.task.entity.Task;
import main.paddletask.task.entity.Task.RECUR_TYPE;
import main.paddletask.task.entity.TimedTask;
import main.paddletask.task.entity.Task.TASK_TYPE;

public class StorageController {
    private static final String DEFAULT_XML = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?><task></task>";
    /*** Variables ***/
    public static final String CONFIG_FILE = ".config";
    protected static String DEFAULT_FILE = "task.xml";
    private static StorageController _thisInstance;
    
    /*** Constructor ***/
    private StorageController() {
        setFileName();
    }
    
    public static StorageController getInstance() {
        if (_thisInstance == null) {
            _thisInstance = new StorageController();
        }
        return _thisInstance;
    }
    
    /*** Methods ***/
    /**
     * This method get the file location from the config file
     * returning the default file path if it does not exit
     * 
     * @return      file name
     */
    protected boolean setFileName() {
        File file = new File(CONFIG_FILE);
        if (!file.exists()) {
         // Create file if it does not exist
            try {
                file.createNewFile();
                FileWriter fileWriter = new FileWriter(CONFIG_FILE);
                fileWriter.write(DEFAULT_FILE);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            // Read the file name from file
            byte[] content = getFileInBytes(CONFIG_FILE);
            DEFAULT_FILE = new String(content, StandardCharsets.UTF_8);
        }
        return true;
    }
    
    /**
     * This method set the new path of the xml file
     * It will copy the existing xml file to the new location
     * 
     * @param  newPath  the new path of the xml file
     * @return          success status
     */
    public boolean setDirectory(String newPath) {
        boolean success;
        // Get existing content
        byte[] content = getFileInBytes(DEFAULT_FILE);
        
        // Copy data to new path
        success = writeBytesToFile(newPath, content, false);
        if (success) {
            // Write new path to CONFIG
            success = writeBytesToFile(CONFIG_FILE, newPath.getBytes(), false);
            if (success) {
                DEFAULT_FILE = newPath;
                Task.setTaskList(readTask());
                return success;
            } else {
                return success;
            }
        } else {
            return success;
        }
    }
    
    /**
     * This method retrieves a File object
     * 
     * @param  fileName  the full file path
     * @return           file object
     */
    protected File getFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }
        return file;
    }
    
    /**
     * This method retrieves the bytes of a File
     * 
     * @param  fileName  the full file path
     * @return           byte array of the file content
     */
    public byte[] getFileInBytes(String fileName) {
        byte[] content = null;
        try {
            Path filePath = Paths.get(fileName);
            content = Files.readAllBytes(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
    
    /**
     * This method writes bytes to a File
     * creating the file if it does not exist
     * 
     * @param  fileName  the full file path
     * @param  content   the bytes to be written
     * @param  append    whether the content should be appended or overwritten
     * @return           success status
     */
    public boolean writeBytesToFile(String fileName, byte[] content, boolean append) {
        File file = new File(fileName);
        if (!file.exists()) {
         // Create file if it does not exist
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName, append);
            fileOutputStream.write(content);
            fileOutputStream.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
        return true;
    }
    
    /**
     * This method creates a new XML file 
     * 
     * @param  fileName  the full file path
     * @return           file containing Task objects
     */
    protected File getXmlFile(String fileName) {
        File file = getFile(fileName);
        if (file == null) {
            // Create file if it does not exist
            try {
                file = new File(fileName);
                file.createNewFile();
                String xml = DEFAULT_XML;
                FileWriter fileWriter = new FileWriter(fileName);
                fileWriter.write(xml);
                fileWriter.close();
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
    public ArrayList<Task> readTask() {
        ArrayList<Task> tasks = new ArrayList<Task>();
        try {
            Document doc = parseXml();
            
            //~ System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("item");
            
            for (int temp = 0; temp < nodeList.getLength(); temp++) {
                Node node = nodeList.item(temp);
                Task task = null;
                //System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    
                    // taskId
                    String taskId = element.getElementsByTagName("taskId").item(0).getTextContent();
                    int taskIdAsInt = Integer.valueOf(taskId);
                    
                    // description
                    String description = element.getElementsByTagName("description").item(0).getTextContent();
                    
                    // createdAt
                    String createdAt = element.getElementsByTagName("createdAt").item(0).getTextContent();
                    LocalDateTime createdAtAsLocalDateTime = DateTimeHelper.parseStringToDateTime(createdAt);
                    
                    // task type
                    String taskTypeAsString = element.getElementsByTagName("tasktype").item(0).getTextContent();
                    TASK_TYPE taskType = Task.determineTaskType(taskTypeAsString);
                    
                    // complete
                    String complete = element.getElementsByTagName("complete").item(0).getTextContent();
                    boolean isCompleteAsBoolean = Boolean.valueOf(complete);
                    
                    // priority
                    String priority = element.getElementsByTagName("priority").item(0).getTextContent();
                    int priorityAsInt = Integer.valueOf(priority);
                    
                    // tags
                    String tag = element.getElementsByTagName("tag").item(0).getTextContent();
                    ArrayList<String> tagsAsArray = getTagsInArrayList(tag);
                    
                    String start;
                    LocalDateTime startAsLocalDateTime;
                    String end;
                    LocalDateTime endAsLocalDateTime;
                    String reminder;
                    LocalDateTime reminderAsLocalDateTime;
                    String recurring;
                    boolean isRecurringAsBoolean;
                    String recurTypeAsString;
                    RECUR_TYPE recurType;
                    switch (taskType) {
                        case FLOATING:
                            task = new FloatingTask(taskIdAsInt, description, createdAtAsLocalDateTime, isCompleteAsBoolean, priorityAsInt, tagsAsArray);
                            break;
                        case TIMED:
                            // start
                            start = element.getElementsByTagName("start").item(0).getTextContent();
                            startAsLocalDateTime = DateTimeHelper.parseStringToDateTime(start);
                            
                            // end
                            end = element.getElementsByTagName("end").item(0).getTextContent();
                            endAsLocalDateTime = DateTimeHelper.parseStringToDateTime(end);
                            
                            // reminder
                            reminder = element.getElementsByTagName("reminder").item(0).getTextContent();
                            reminderAsLocalDateTime = DateTimeHelper.parseStringToDateTime(reminder);
                            
                            // recurring
                            recurring = element.getElementsByTagName("recurring").item(0).getTextContent();
                            isRecurringAsBoolean = Boolean.valueOf(recurring);
                            
                            // recur type
                            recurTypeAsString = element.getElementsByTagName("recurtype").item(0).getTextContent();
                            recurType = Task.determineRecurType(recurTypeAsString);
                            
                            task = new TimedTask(taskIdAsInt, description, createdAtAsLocalDateTime, startAsLocalDateTime, endAsLocalDateTime, reminderAsLocalDateTime, isCompleteAsBoolean, priorityAsInt, tagsAsArray, isRecurringAsBoolean, recurType);
                            break;
                        case DEADLINE:
                            // end
                            end = element.getElementsByTagName("end").item(0).getTextContent();
                            endAsLocalDateTime = DateTimeHelper.parseStringToDateTime(end);
                            
                            // reminder
                            reminder = element.getElementsByTagName("reminder").item(0).getTextContent();
                            reminderAsLocalDateTime = DateTimeHelper.parseStringToDateTime(reminder);
                            
                            // recurring
                            recurring = element.getElementsByTagName("recurring").item(0).getTextContent();
                            isRecurringAsBoolean = Boolean.valueOf(recurring);
                            
                            // recur type
                            recurTypeAsString = element.getElementsByTagName("recurtype").item(0).getTextContent();
                            recurType = Task.determineRecurType(recurTypeAsString);
                            
                            task = new DeadlineTask(taskIdAsInt, description, createdAtAsLocalDateTime, endAsLocalDateTime, reminderAsLocalDateTime, isCompleteAsBoolean, priorityAsInt, tagsAsArray, isRecurringAsBoolean, recurType);
                            break;
                        default:
                            break;
                    }
                }
                
                if (task != null) {
                    tasks.add(task);
                }
            }
        } catch (Exception e) {
            //e.printStackTrace();
            //return null;
        }
        
        return tasks;
    }
    
    /**
     * This method returns a string representation of the XML version of the Task objects
     * 
     * @param  tasks  an ArrayList of all Task objects
     * @return        a Document representing the XML document
     */
    public Document parseTask(ArrayList<Task> tasks) {
        Document doc = null;
        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.newDocument();
            
            Element root = doc.createElement("task");
            doc.appendChild(root);
            
            //for (Task task : taskList) {
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                
                Element item = doc.createElement("item");
                root.appendChild(item);

                // taskId
                Element taskId = doc.createElement("taskId");
                taskId.appendChild(doc.createTextNode(Integer.toString(task.getTaskId())));
                item.appendChild(taskId);
                
                // description
                Element description = doc.createElement("description");
                description.appendChild(doc.createTextNode(task.getDescription()));
                item.appendChild(description);
                
                // createdAt
                String formattedDateTime = DateTimeHelper.parseDateTimeToString(task.getCreatedAt());
                Element createdAt = doc.createElement("createdAt");
                createdAt.appendChild(doc.createTextNode(formattedDateTime));
                item.appendChild(createdAt);
                
                // type
                Element taskType = doc.createElement("tasktype");
                taskType.appendChild(doc.createTextNode(task.getType().toString()));
                item.appendChild(taskType);
                
                // complete
                Element complete = doc.createElement("complete");
                String complete_string = String.valueOf(task.isComplete());
                complete.appendChild(doc.createTextNode(complete_string));
                item.appendChild(complete);
                
                // priority
                Element priority = doc.createElement("priority");
                priority.appendChild(doc.createTextNode(Integer.toString(task.getPriority())));
                item.appendChild(priority);
                
                // tag
                Element tag = doc.createElement("tag");
                tag.appendChild(doc.createTextNode(getTagsInString(task.getTags())));
                item.appendChild(tag);
                
                Element start;
                Element end;
                Element reminder;
                Element recurring;
                String recurringAsString;
                Element recurType;
                switch (task.getType()) {
                    case FLOATING:
                        // start
                        start = doc.createElement("start");
                        start.appendChild(doc.createTextNode(""));
                        item.appendChild(start);
                        
                        // end
                        end = doc.createElement("end");
                        end.appendChild(doc.createTextNode(""));
                        item.appendChild(end);
                        break;
                    case TIMED:
                        //start
                        start = doc.createElement("start");
                        formattedDateTime = DateTimeHelper.parseDateTimeToString(((TimedTask) task).getStart());
                        start.appendChild(doc.createTextNode(formattedDateTime));
                        item.appendChild(start);
                        
                        // end
                        end = doc.createElement("end");
                        formattedDateTime = DateTimeHelper.parseDateTimeToString(((TimedTask) task).getEnd());
                        end.appendChild(doc.createTextNode(formattedDateTime));
                        item.appendChild(end);
                        
                        // reminder
                        reminder = doc.createElement("reminder");
                        formattedDateTime = DateTimeHelper.parseDateTimeToString(((TimedTask) task).getReminder());
                        reminder.appendChild(doc.createTextNode(formattedDateTime));
                        item.appendChild(reminder);
                        
                        // recurring
                        recurring = doc.createElement("recurring");
                        recurringAsString = String.valueOf(((TimedTask) task).isRecurring());
                        recurring.appendChild(doc.createTextNode(recurringAsString));
                        item.appendChild(complete);
                        
                        // type
                        recurType = doc.createElement("recurtype");
                        recurType.appendChild(doc.createTextNode(((TimedTask) task).getRecurPeriod().toString()));
                        item.appendChild(recurType);
                        
                        break;
                    case DEADLINE:
                        // start
                        start = doc.createElement("start");
                        start.appendChild(doc.createTextNode(""));
                        item.appendChild(start);
                        
                        // end
                        end = doc.createElement("end");
                        formattedDateTime = DateTimeHelper.parseDateTimeToString(((DeadlineTask) task).getEnd());
                        end.appendChild(doc.createTextNode(formattedDateTime));
                        item.appendChild(end);
                        
                        // reminder
                        reminder = doc.createElement("reminder");
                        formattedDateTime = DateTimeHelper.parseDateTimeToString(((DeadlineTask) task).getReminder());
                        reminder.appendChild(doc.createTextNode(formattedDateTime));
                        item.appendChild(reminder);
                        
                        // recurring
                        recurring = doc.createElement("recurring");
                        recurringAsString = String.valueOf(((DeadlineTask) task).isRecurring());
                        recurring.appendChild(doc.createTextNode(recurringAsString));
                        item.appendChild(complete);
                        
                        // type
                        recurType = doc.createElement("recurtype");
                        recurType.appendChild(doc.createTextNode(((DeadlineTask) task).getRecurPeriod().toString()));
                        item.appendChild(recurType);
                        
                        break;
                    default:
                        break;
                }
            }
        } catch (ParserConfigurationException e) {
            System.out.println("Error building document");
        }
        return doc;
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
            doc = dBuilder.parse(getXmlFile(DEFAULT_FILE));
            
            // Ensures that the XML DOM view of a document is identical
            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            //e.printStackTrace();
        }
        
        return doc;
    }
    
    /**
     * This method writes all Task objects to file
     * 
     * @param  doc  a Document representing the XML document
     * @return      <code>true</code> if the tasks are successfully stored; 
     *              <code>false</code> otherwise.
     */
    public boolean writeXml(Document doc) {
        try {
            // Save the document to the disk file
            TransformerFactory tranFactory = TransformerFactory.newInstance();
            Transformer aTransformer = tranFactory.newTransformer();

            // format the XML nicely
            aTransformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");

            aTransformer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount", "4");
            aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);
            
            FileWriter fileWriter = new FileWriter(DEFAULT_FILE);
            StreamResult result = new StreamResult(fileWriter);
            aTransformer.transform(source, result);

        } catch (IOException e) {
            System.out.println("Error writing to file");
            return false;
        } catch (TransformerConfigurationException e1) {
            System.out.println("Error outputting document");
            return false;
        } catch (TransformerException e) {
            System.out.println("Error initializing transformer");
            e.printStackTrace();
            return false;
        }   
        
        return true;
    }
    
    /**
     * This method converts an arraylist of tags into string
     * 
     * @param  tags  an arraylist of tags
     * @return       a string representation
     */
    public String getTagsInString(ArrayList<String> tags) { 
        String tagsAsString = "";
        for (String tag : tags) {
            tagsAsString += tag + " ";
        }
        return tagsAsString.trim();
    }
    
    /**
     * This method converts a string of tags delimited by space,
     * into an arraylist
     * 
     * @param  tags  a string of tags
     * @return       an arraylist representation
     */
    public ArrayList<String> getTagsInArrayList(String tags) { 
         ArrayList<String> tagsAsArray = new ArrayList<String>();
         String[] splittedTags = tags.split(" ");
         for (String tag : splittedTags) {
             tagsAsArray.add(tag);
         }
         return tagsAsArray;
    }
}
