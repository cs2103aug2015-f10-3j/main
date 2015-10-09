package test;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import logic.data.DeadlineTask;
import logic.data.FloatingTask;
import logic.data.Task;
import logic.data.TimedTask;
import storage.StorageController;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

public class StorageControllerTest {
    /*** Variables ***/
    protected static final String FILE_NAME = "task.xml";
    StorageController sdParser;
    ArrayList<Task> testTaskList;
    
    /*** Setup and Teardown ***/
    @Before
    public void setUp() throws Exception {
        sdParser = new StorageController();
    }

    @After
    public void tearDown() throws Exception {
    }

    public ArrayList<Task> repopulateTask() {
        // Populate sample arraylist
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Task task;
        task = new DeadlineTask(1, "CS2103 CE2", LocalDateTime.parse("2015-09-01 12:30", formatter), LocalDateTime.parse("2015-09-10 12:30", formatter), true);
        testTaskList.add(task);
        task = new TimedTask(2, "Appointment with dentist", LocalDateTime.parse("2015-09-02 16:34", formatter), LocalDateTime.parse("2015-09-10 14:30", formatter), LocalDateTime.parse("2015-09-10 15:30", formatter), false);
        testTaskList.add(task);
        task = new FloatingTask(3, "Buy chicken", LocalDateTime.parse("2015-09-05 13:03", formatter), true);
        testTaskList.add(task);
        task = new DeadlineTask(4, "Submit CS2106 Lab 1", LocalDateTime.parse("2015-09-06 22:16", formatter), LocalDateTime.parse("2015-09-10 23:59", formatter), false);
        testTaskList.add(task);
        task = new TimedTask(5, "Go out gai gai", LocalDateTime.parse("2015-09-07 23:00", formatter), LocalDateTime.parse("2015-09-17 13:00", formatter), LocalDateTime.parse("2015-09-17 17:00", formatter), true);
        testTaskList.add(task);
        task = new FloatingTask(6, "Eat more chicken", LocalDateTime.parse("2015-09-07 23:01", formatter), false);
        testTaskList.add(task);
        task = new DeadlineTask(7, "Pay money for steamboat", LocalDateTime.parse("2015-09-08 08:55", formatter), LocalDateTime.parse("2015-09-10 23:59", formatter), false);
        testTaskList.add(task);
        task = new TimedTask(8, "Walk the neighbor's dog", LocalDateTime.parse("2015-09-08 10:20", formatter), LocalDateTime.parse("2015-09-13 17:00", formatter), LocalDateTime.parse("2015-09-13 18:00", formatter), true);
        testTaskList.add(task);
        task = new FloatingTask(9, "Stock up locker", LocalDateTime.parse("2015-09-10 11:45", formatter), true);
        testTaskList.add(task);
        task = new DeadlineTask(10, "Sign up for chicken eating competition", LocalDateTime.parse("2015-09-14 21:10", formatter), LocalDateTime.parse("2015-09-20 23:59", formatter), false);
        testTaskList.add(task);
        
        Document doc = sdParser.parseTask(testTaskList);
        sdParser.writeXml(doc);
        
        return testTaskList;
    }
    
    /*** Test Cases 
     * @throws Exception  ***/
    @Test
    public void testGetFile() throws Exception {
    	Method m = sdParser.getClass().getDeclaredMethod("getFile");
		m.setAccessible(true);
        File file = (File)m.invoke(sdParser);
        if (file.exists()) {
            assert true;
        } else {
            assert false;
        }
        
        file = new File(FILE_NAME);
        file.delete();
        m = sdParser.getClass().getDeclaredMethod("getFile");
        m.setAccessible(true);
        file = (File)m.invoke(sdParser);
        if (file.exists()) {
            assert true;
        } else {
            assert false;
        }
    }

    @Test
    public void testReadTask() {
        testTaskList = new ArrayList<Task>();
        ArrayList<Task> taskList = sdParser.readTask();
        if (taskList != null) {
            assert true;
        } else {
            assert false;
        }
        if (taskList.size() == 0) {
            assert true;
        } else {
            assert false;
        }
        
        testTaskList = repopulateTask();
        taskList = sdParser.readTask();
        if (taskList.size() > 0) {
            assert true;
        } else {
            assert false;
        }
    }
    
    @Test
    public void testParseTask() {
        testTaskList = new ArrayList<Task>();
        Document doc = sdParser.parseTask(testTaskList);
        if (doc != null) {
            assert true;
        } else {
            assert false;
        }
        if (doc.getElementsByTagName("item").getLength() == 0) {
            assert true;
        } else {
            assert false;
        }
        
        testTaskList = repopulateTask();
        doc = sdParser.parseTask(testTaskList);
        if (doc.getDocumentElement().getNodeName().equals("task")) {
            assert true;
        } else {
            assert false;
        }
        if (doc.getElementsByTagName("item").getLength() == 10) {
            assert true;
        } else {
            assert false;
        }
    }
    
    @Test
    public void testParseXml() throws Exception {
        Method m = sdParser.getClass().getDeclaredMethod("parseXml");
        m.setAccessible(true);
        Document doc = (Document)m.invoke(sdParser);
        if (doc != null) {
            assert true;
        } else {
            assert false;
        }
        if (doc.getDocumentElement().getNodeName().equals("task")) {
            assert true;
        } else {
            assert false;
        }
    }
    
    @Test
    public void testWriteXml() {
        testTaskList = new ArrayList<Task>();
        Document doc = sdParser.parseTask(testTaskList);
        boolean result = sdParser.writeXml(doc);
        assertEquals(true, result);
        
        testTaskList = repopulateTask();
        doc = sdParser.parseTask(testTaskList);
        result = sdParser.writeXml(doc);
        assertEquals(true, result);
    }
}
