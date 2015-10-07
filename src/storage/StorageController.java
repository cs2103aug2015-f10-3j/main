package storage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import logic.data.DeadlineTask;
import logic.data.FloatingTask;
import logic.data.Task;
import logic.data.TimedTask;
import logic.data.Task.TASK_TYPE;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class StorageController {
    /*** Variables ***/
    protected static final String FILE_NAME = "task.xml";
    
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
                ArrayList<Task> taskList = new ArrayList<Task>();
                Document doc = parseTask(taskList);
                writeXml(doc);
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
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    
                    // taskId
                    String taskId = eElement.getElementsByTagName("taskId").item(0).getTextContent();
                    int taskId_int = Integer.valueOf(taskId);
                    
                    // description
                    String description = eElement.getElementsByTagName("description").item(0).getTextContent();
                    
                    // createdAt
                    String createdAt = eElement.getElementsByTagName("createdAt").item(0).getTextContent();
                    LocalDateTime createdAt_localdatetime = LocalDateTime.parse(createdAt, formatter);
                    
                    // type
                    String type_string = eElement.getElementsByTagName("type").item(0).getTextContent();
                    TASK_TYPE taskType = Task.determineType(type_string);
                    
                    // complete
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
                    
                    String start;
                    LocalDateTime start_localdatetime;
                    String end;
                    LocalDateTime end_localdatetime;
                    switch (taskType) {
                        case FLOATING:
                            task = new FloatingTask(taskId_int, description, createdAt_localdatetime, complete_boolean);
                            break;
                        case TIMED:
                            // start
                            start = eElement.getElementsByTagName("start").item(0).getTextContent();
                            start_localdatetime = LocalDateTime.parse(start, formatter);
                            
                            // end
                            end = eElement.getElementsByTagName("end").item(0).getTextContent();
                            end_localdatetime = LocalDateTime.parse(end, formatter);
                           
                            task = new TimedTask(taskId_int, description, createdAt_localdatetime, start_localdatetime, end_localdatetime, complete_boolean);
                            break;
                        case DEADLINE:
                            // end
                            end = eElement.getElementsByTagName("end").item(0).getTextContent();
                            end_localdatetime = LocalDateTime.parse(end, formatter);
                           
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
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                String formattedDateTime = task.getCreatedAt().format(formatter);
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
                
                Element start;
                Element end;
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
                        formattedDateTime = ((TimedTask) task).getStart().format(formatter);
                        start.appendChild(doc.createTextNode(formattedDateTime));
                        item.appendChild(start);
                        
                        // end
                        end = doc.createElement("end");
                        formattedDateTime = ((TimedTask) task).getEnd().format(formatter);
                        end.appendChild(doc.createTextNode(formattedDateTime));
                        item.appendChild(end);
                        break;
                    case DEADLINE:
                        // start
                        start = doc.createElement("start");
                        start.appendChild(doc.createTextNode(""));
                        item.appendChild(start);
                        
                        // end
                        end = doc.createElement("end");
                        formattedDateTime = ((DeadlineTask) task).getEnd().format(formatter);
                        end.appendChild(doc.createTextNode(formattedDateTime));
                        item.appendChild(end);
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
            
            FileWriter fos = new FileWriter(FILE_NAME);
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
            return false;
        }   
        
        return true;
    }
}
