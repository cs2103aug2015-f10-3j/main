package storage.api;

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

import common.util.DateTimeHelper;
import task.entity.DeadlineTask;
import task.entity.FloatingTask;
import task.entity.Task;
import task.entity.TimedTask;
import task.entity.Task.TASK_TYPE;

public class StorageController {
    /*** Variables ***/
    protected static final String CONFIG_FILE = ".config";
    protected static final String DEFAULT_FILE_NAME = "task.xml";
    private static StorageController thisInstance;
    
    /*** Constructor ***/
    private StorageController() {
        
    }
    
    public static StorageController getInstance() {
    	if (thisInstance == null) {
    		thisInstance = new StorageController();
    	}
    	return thisInstance;
    }
    
    /*** Methods ***/
    /**
     * This method check if Config file exist,
     * create it with the default file path if it is not
     * 
     * @param  fileName the full file path
     * @return          file objectzc
     */
    protected String getFileName() {
        String taskFileName = null;
        File file = new File(CONFIG_FILE);
        if (!file.exists()) {
         // Create file if it does not exist
            try {
                file.createNewFile();
                FileWriter fw = new FileWriter(CONFIG_FILE);
                fw.write(DEFAULT_FILE_NAME);
                fw.close();
                taskFileName = DEFAULT_FILE_NAME;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            // Read the file name from file
            byte[] content = getFileInBytes(CONFIG_FILE);
            taskFileName = new String(content, StandardCharsets.UTF_8);
        }
        return taskFileName;
    }
    
    /**
     * This method retrieves a File object
     * 
     * @param  fileName the full file path
     * @return          file object
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
     * @param fileName  the full file path
     * @return          byte array of the file content
     */
    protected byte[] getFileInBytes(String fileName) {
        byte[] content = null;
        try {
            Path filePath = Paths.get(fileName);
            content = Files.readAllBytes(filePath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return content;
    }
    
    /**
     * This method writes bytes to a File
     * 
     * @param fileName  the full file path
     * @param content   the bytes to be written
     * @param append    whether the content should be appended or overwritten
     * @return          success status
     */
    protected boolean writeBytesToFile(String fileName, byte[] content, boolean append) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName, append);
            fos.write(content);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
        return true;
    }
    
    /**
     * This method creates a new XML file 
     * 
     * @return       file containing Task objects
     */
    protected File getXmlFile(String fileName) {
        File file = getFile(fileName);
        if (file == null) {
            // Create file if it does not exist
            try {
                file = new File(fileName);
                file.createNewFile();
                String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?><task></task>";
                FileWriter fw = new FileWriter(fileName);
                fw.write(xml);
                fw.close();
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
        ArrayList<Task> taskList = new ArrayList<Task>();
        try {
            Document doc = parseXml();
            
            //~ System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("item");
            
            for (int temp = 0; temp < nodeList.getLength(); temp++) {
                Node nNode = nodeList.item(temp);
                Task task = null;
                //System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    // DateTimeFormatter
                    // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    
                    // taskId
                    String taskId = eElement.getElementsByTagName("taskId").item(0).getTextContent();
                    int taskId_int = Integer.valueOf(taskId);
                    
                    // description
                    String description = eElement.getElementsByTagName("description").item(0).getTextContent();
                    
                    // createdAt
                    String createdAt = eElement.getElementsByTagName("createdAt").item(0).getTextContent();
                    LocalDateTime createdAt_localdatetime = DateTimeHelper.parseStringToDateTime(createdAt);
                    
                    // type
                    String type_string = eElement.getElementsByTagName("type").item(0).getTextContent();
                    TASK_TYPE taskType = Task.determineType(type_string);
                    
                    // complete
                    String complete = eElement.getElementsByTagName("complete").item(0).getTextContent();
                    boolean complete_boolean = Boolean.valueOf(complete);
                    
                    // priority
                    String priority = eElement.getElementsByTagName("priority").item(0).getTextContent();
                    int priority_int = Integer.valueOf(priority);
                    
                    // tags
                    String tag = eElement.getElementsByTagName("tag").item(0).getTextContent();
                    ArrayList<String> tag_array = getTagsInArrayList(tag);
                    
                    /*
                    System.out.println("taskId: " + taskId);
                    System.out.println("description: " + description);
                    System.out.println("createdAt: " + createdAt);
                    System.out.println("type: " + type);
                    System.out.println("start: " + start);
                    System.out.println("end: " + end);
                    System.out.println("complete: " + complete);
                    */
                    
                    String start;
                    LocalDateTime start_localdatetime;
                    String end;
                    LocalDateTime end_localdatetime;
                    String reminder;
                    LocalDateTime reminder_localdatetime;
                    switch (taskType) {
                        case FLOATING:
                            task = new FloatingTask(taskId_int, description, createdAt_localdatetime, complete_boolean, priority_int, tag_array);
                            break;
                        case TIMED:
                            // start
                            start = eElement.getElementsByTagName("start").item(0).getTextContent();
                            start_localdatetime = DateTimeHelper.parseStringToDateTime(start);
                            
                            // end
                            end = eElement.getElementsByTagName("end").item(0).getTextContent();
                            end_localdatetime = DateTimeHelper.parseStringToDateTime(end);
                            
                            // reminder
                            reminder = eElement.getElementsByTagName("reminder").item(0).getTextContent();
                            reminder_localdatetime = DateTimeHelper.parseStringToDateTime(reminder);
                            
                            task = new TimedTask(taskId_int, description, createdAt_localdatetime, start_localdatetime, end_localdatetime, reminder_localdatetime, complete_boolean, priority_int, tag_array);
                            break;
                        case DEADLINE:
                            // end
                            end = eElement.getElementsByTagName("end").item(0).getTextContent();
                            end_localdatetime = DateTimeHelper.parseStringToDateTime(end);
                            
                            // reminder
                            reminder = eElement.getElementsByTagName("reminder").item(0).getTextContent();
                            reminder_localdatetime = DateTimeHelper.parseStringToDateTime(reminder);
                            
                            task = new DeadlineTask(taskId_int, description, createdAt_localdatetime, end_localdatetime, reminder_localdatetime, complete_boolean, priority_int, tag_array);
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
            //e.printStackTrace();
            //return null;
        }
        
        return taskList;
    }
    
    /**
     * This method returns a string representation of the XML version of the Task objects
     * 
     * @param  task  an ArrayList of all Task objects
     * @return       a Document representing the XML document
     */
    public Document parseTask(ArrayList<Task> taskList) {
        Document doc = null;
        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.newDocument();
            
            Element root = doc.createElement("task");
            doc.appendChild(root);
            
            //for (Task task : taskList) {
            for (int i = 0; i < taskList.size(); i++) {
                Task task = taskList.get(i);
                
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
                //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String formattedDateTime = DateTimeHelper.parseDateTimeToString(task.getCreatedAt());
                Element createdAt = doc.createElement("createdAt");
                createdAt.appendChild(doc.createTextNode(formattedDateTime));
                item.appendChild(createdAt);
                
                // type
                Element type = doc.createElement("type");
                type.appendChild(doc.createTextNode(task.getType().toString()));
                item.appendChild(type);
                
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
            doc = dBuilder.parse(getXmlFile(getFileName()));
            
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
            
            FileWriter fos = new FileWriter(getFileName());
            StreamResult result = new StreamResult(fos);
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
     * @return      a string representation
     */
    public String getTagsInString(ArrayList<String> tags) { 
        String tags_string = "";
        for (String tag : tags) {
            tags_string += tag + " ";
        }
        return tags_string.trim();
    }
    
    /**
     * This method converts a string of tags delimited by space,
     * into an arraylist
     * 
     * @param  tags  a string of tags
     * @return       an arraylist representation
     */
    public ArrayList<String> getTagsInArrayList(String tags) { 
         ArrayList<String> tags_array = new ArrayList<String>();
         String[] splittedTags = tags.split(" ");
         for (String tag : splittedTags) {
             tags_array.add(tag);
         }
         return tags_array;
    }
}
