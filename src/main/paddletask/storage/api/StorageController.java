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
    private static final String ERROR_INITIALIZING_TRANSFORMER = "Error initializing transformer";
    private static final String ERROR_OUTPUTTING_DOCUMENT = "Error outputting document";
    private static final String ERROR_WRITING_TO_FILE = "Error writing to file";
    private static final String YES = "yes";
    private static final String INDENT_AMOUNT = "4";
    private static final String INDENT_AMOUNT_DOCS = "{http://xml.apache.org/xslt}indent-amount";
    private static final String ENCODING = "ISO-8859-1";
    private static final String TASK = "task";
    private static final String RECURTYPE = "recurtype";
    private static final String RECURRING = "recurring";
    private static final String REMINDER = "reminder";
    private static final String END = "end";
    private static final String START = "start";
    private static final String TAG = "tag";
    private static final String PRIORITY = "priority";
    private static final String COMPLETE = "complete";
    private static final String TASKTYPE = "tasktype";
    private static final String CREATED_AT = "createdAt";
    private static final String DESCRIPTION = "description";
    private static final String TASK_ID = "taskId";
    private static final String ITEM = "item";
    private static final String DEFAULT_XML = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\" standalone=\"no\"?><task></task>";
    public static final String CONFIG_FILE = ".config";
    private static String TASK_XML = "task.xml";
    private static String DEFAULT_FILE = TASK_XML;
    private static StorageController _thisInstance;
    
    /*** Constructor ***/
    private StorageController() {
        try {
            DEFAULT_FILE = new File( "." ).getCanonicalPath() + TASK_XML;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
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
        newPath = newPath + File.separator + TASK_XML;
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
            NodeList nodeList = doc.getElementsByTagName(ITEM);
            
            for (int temp = 0; temp < nodeList.getLength(); temp++) {
                try {
                    Node node = nodeList.item(temp);
                    Task task = null;
                    //System.out.println("\nCurrent Element :" + nNode.getNodeName());
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;
                        
                        // taskId
                        String taskId = "";
                        int taskIdAsInt = -1;
                        if (element.getElementsByTagName(TASK_ID).item(0) != null) {
                            taskId = element.getElementsByTagName(TASK_ID).item(0).getTextContent();
                            taskIdAsInt = Integer.valueOf(taskId);
                        }
                        
                        // description
                        String description = "";
                        if (element.getElementsByTagName(DESCRIPTION).item(0) != null) {
                            description = element.getElementsByTagName(DESCRIPTION).item(0).getTextContent();
                        }
                        
                        // createdAt
                        String createdAt = "";
                        LocalDateTime createdAtAsLocalDateTime = LocalDateTime.MIN;
                        if (element.getElementsByTagName(CREATED_AT).item(0) != null) {
                            createdAt = element.getElementsByTagName(CREATED_AT).item(0).getTextContent();
                            createdAtAsLocalDateTime = DateTimeHelper.parseStringToDateTime(createdAt);
                        }
                        
                        // task type
                        String taskTypeAsString = "";
                        TASK_TYPE taskType = TASK_TYPE.ANY;
                        if (element.getElementsByTagName(TASKTYPE).item(0) != null) {
                            taskTypeAsString = element.getElementsByTagName(TASKTYPE).item(0).getTextContent();
                            taskType = Task.determineTaskType(taskTypeAsString);
                        }
                        
                        // complete
                        String complete = "";
                        boolean isCompleteAsBoolean = false;
                        if (element.getElementsByTagName(COMPLETE).item(0) != null) {
                            complete = element.getElementsByTagName(COMPLETE).item(0).getTextContent();
                            isCompleteAsBoolean = Boolean.valueOf(complete);
                        }
                        
                        // priority
                        String priority = "";
                        int priorityAsInt = -1;
                        if (element.getElementsByTagName(PRIORITY).item(0) != null) {
                            priority = element.getElementsByTagName(PRIORITY).item(0).getTextContent();
                            priorityAsInt = Integer.valueOf(priority);
                        }
                        
                        // tags
                        String tag = "";
                        ArrayList<String> tagsAsArray = new ArrayList<String>();
                        if (element.getElementsByTagName(TAG).item(0) != null) {
                            tag = element.getElementsByTagName(TAG).item(0).getTextContent();
                            tagsAsArray = getTagsInArrayList(tag);
                        }
                        
                        String start = "";
                        LocalDateTime startAsLocalDateTime = LocalDateTime.MIN;
                        String end = "";
                        LocalDateTime endAsLocalDateTime = LocalDateTime.MIN;
                        String reminder = "";
                        LocalDateTime reminderAsLocalDateTime = LocalDateTime.MIN;
                        String recurring = "";
                        boolean isRecurringAsBoolean = false;
                        String recurTypeAsString = "";
                        RECUR_TYPE recurType = RECUR_TYPE.NULL;
                        switch (taskType) {
                            case FLOATING:
                                task = new FloatingTask(taskIdAsInt, description, createdAtAsLocalDateTime, isCompleteAsBoolean, priorityAsInt, tagsAsArray);
                                break;
                            case TIMED:
                                // start
                                if (element.getElementsByTagName(START).item(0) != null) {
                                    start = element.getElementsByTagName(START).item(0).getTextContent();
                                    startAsLocalDateTime = DateTimeHelper.parseStringToDateTime(start);
                                }
                                
                                // end
                                if (element.getElementsByTagName(END).item(0) != null) {
                                    end = element.getElementsByTagName(END).item(0).getTextContent();
                                    endAsLocalDateTime = DateTimeHelper.parseStringToDateTime(end);
                                }
                                
                                // reminder
                                if (element.getElementsByTagName(REMINDER).item(0) != null) {
                                    reminder = element.getElementsByTagName(REMINDER).item(0).getTextContent();
                                    reminderAsLocalDateTime = DateTimeHelper.parseStringToDateTime(reminder);
                                }
                                
                                // recurring
                                if (element.getElementsByTagName(RECURRING).item(0) != null) {
                                    recurring = element.getElementsByTagName(RECURRING).item(0).getTextContent();
                                    isRecurringAsBoolean = Boolean.valueOf(recurring);
                                }
                                
                                // recur type
                                if (element.getElementsByTagName(RECURTYPE).item(0) != null) {
                                    recurTypeAsString = element.getElementsByTagName(RECURTYPE).item(0).getTextContent();
                                    recurType = Task.determineRecurType(recurTypeAsString);
                                }
                                
                                task = new TimedTask(taskIdAsInt, description, createdAtAsLocalDateTime, startAsLocalDateTime, endAsLocalDateTime, reminderAsLocalDateTime, isCompleteAsBoolean, priorityAsInt, tagsAsArray, isRecurringAsBoolean, recurType);
                                break;
                            case DEADLINE:
                                // end
                                if (element.getElementsByTagName(END).item(0) != null) {
                                    end = element.getElementsByTagName(END).item(0).getTextContent();
                                    endAsLocalDateTime = DateTimeHelper.parseStringToDateTime(end);
                                }
                                
                                // reminder
                                if (element.getElementsByTagName(REMINDER).item(0) != null) {
                                    reminder = element.getElementsByTagName(REMINDER).item(0).getTextContent();
                                    reminderAsLocalDateTime = DateTimeHelper.parseStringToDateTime(reminder);
                                }
                                
                                // recurring
                                if (element.getElementsByTagName(RECURRING).item(0) != null) {
                                    recurring = element.getElementsByTagName(RECURRING).item(0).getTextContent();
                                    isRecurringAsBoolean = Boolean.valueOf(recurring);
                                }
                        
                                // recur type
                                if (element.getElementsByTagName(RECURTYPE).item(0) != null) {
                                    recurTypeAsString = element.getElementsByTagName(RECURTYPE).item(0).getTextContent();
                                    recurType = Task.determineRecurType(recurTypeAsString);
                                }
                                
                                task = new DeadlineTask(taskIdAsInt, description, createdAtAsLocalDateTime, endAsLocalDateTime, reminderAsLocalDateTime, isCompleteAsBoolean, priorityAsInt, tagsAsArray, isRecurringAsBoolean, recurType);
                                break;
                            default:
                                break;
                        }
                    }
                    
                    if (task != null) {
                        tasks.add(task);
                    }
                } catch (Exception e) {
                    //e.printStackTrace();
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
            
            Element root = doc.createElement(TASK);
            doc.appendChild(root);
            
            //for (Task task : taskList) {
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                
                Element item = doc.createElement(ITEM);
                root.appendChild(item);

                // taskId
                Element taskId = doc.createElement(TASK_ID);
                taskId.appendChild(doc.createTextNode(Integer.toString(task.getTaskId())));
                item.appendChild(taskId);
                
                // description
                Element description = doc.createElement(DESCRIPTION);
                description.appendChild(doc.createTextNode(task.getDescription()));
                item.appendChild(description);
                
                // createdAt
                String formattedDateTime = DateTimeHelper.parseDateTimeToString(task.getCreatedAt());
                Element createdAt = doc.createElement(CREATED_AT);
                createdAt.appendChild(doc.createTextNode(formattedDateTime));
                item.appendChild(createdAt);
                
                // type
                Element taskType = doc.createElement(TASKTYPE);
                taskType.appendChild(doc.createTextNode(task.getType().toString()));
                item.appendChild(taskType);
                
                // complete
                Element complete = doc.createElement(COMPLETE);
                String complete_string = String.valueOf(task.isComplete());
                complete.appendChild(doc.createTextNode(complete_string));
                item.appendChild(complete);
                
                // priority
                Element priority = doc.createElement(PRIORITY);
                priority.appendChild(doc.createTextNode(Integer.toString(task.getPriority())));
                item.appendChild(priority);
                
                // tag
                Element tag = doc.createElement(TAG);
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
                        start = doc.createElement(START);
                        start.appendChild(doc.createTextNode(""));
                        item.appendChild(start);
                        
                        // end
                        end = doc.createElement(END);
                        end.appendChild(doc.createTextNode(""));
                        item.appendChild(end);
                        break;
                    case TIMED:
                        //start
                        start = doc.createElement(START);
                        formattedDateTime = DateTimeHelper.parseDateTimeToString(((TimedTask) task).getStart());
                        start.appendChild(doc.createTextNode(formattedDateTime));
                        item.appendChild(start);
                        
                        // end
                        end = doc.createElement(END);
                        formattedDateTime = DateTimeHelper.parseDateTimeToString(((TimedTask) task).getEnd());
                        end.appendChild(doc.createTextNode(formattedDateTime));
                        item.appendChild(end);
                        
                        // reminder
                        reminder = doc.createElement(REMINDER);
                        formattedDateTime = DateTimeHelper.parseDateTimeToString(((TimedTask) task).getReminder());
                        reminder.appendChild(doc.createTextNode(formattedDateTime));
                        item.appendChild(reminder);
                        
                        // recurring
                        recurring = doc.createElement(RECURRING);
                        recurringAsString = String.valueOf(((TimedTask) task).isRecurring());
                        recurring.appendChild(doc.createTextNode(recurringAsString));
                        item.appendChild(recurring);
                        
                        // type
                        recurType = doc.createElement(RECURTYPE);
                        recurType.appendChild(doc.createTextNode(((TimedTask) task).getRecurPeriod().toString()));
                        item.appendChild(recurType);
                        
                        break;
                    case DEADLINE:
                        // start
                        start = doc.createElement(START);
                        start.appendChild(doc.createTextNode(""));
                        item.appendChild(start);
                        
                        // end
                        end = doc.createElement(END);
                        formattedDateTime = DateTimeHelper.parseDateTimeToString(((DeadlineTask) task).getEnd());
                        end.appendChild(doc.createTextNode(formattedDateTime));
                        item.appendChild(end);
                        
                        // reminder
                        reminder = doc.createElement(REMINDER);
                        formattedDateTime = DateTimeHelper.parseDateTimeToString(((DeadlineTask) task).getReminder());
                        reminder.appendChild(doc.createTextNode(formattedDateTime));
                        item.appendChild(reminder);
                        
                        // recurring
                        recurring = doc.createElement(RECURRING);
                        recurringAsString = String.valueOf(((DeadlineTask) task).isRecurring());
                        recurring.appendChild(doc.createTextNode(recurringAsString));
                        item.appendChild(recurring);
                        
                        // type
                        recurType = doc.createElement(RECURTYPE);
                        recurType.appendChild(doc.createTextNode(((DeadlineTask) task).getRecurPeriod().toString()));
                        item.appendChild(recurType);
                        
                        break;
                    default:
                        break;
                }
            }
        } catch (ParserConfigurationException e) {
            String ERROR_BUILDING_DOCUMENT = "Error building document";
            System.out.println(ERROR_BUILDING_DOCUMENT);
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
            aTransformer.setOutputProperty(OutputKeys.ENCODING, ENCODING);

            aTransformer.setOutputProperty(
                    INDENT_AMOUNT_DOCS, INDENT_AMOUNT);
            aTransformer.setOutputProperty(OutputKeys.INDENT, YES);

            DOMSource source = new DOMSource(doc);
            
            FileWriter fileWriter = new FileWriter(DEFAULT_FILE);
            StreamResult result = new StreamResult(fileWriter);
            aTransformer.transform(source, result);

        } catch (IOException e) {
            System.out.println(ERROR_WRITING_TO_FILE);
            return false;
        } catch (TransformerConfigurationException e1) {
            System.out.println(ERROR_OUTPUTTING_DOCUMENT);
            return false;
        } catch (TransformerException e) {
            System.out.println(ERROR_INITIALIZING_TRANSFORMER);
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
