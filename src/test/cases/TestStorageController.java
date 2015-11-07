//@@author A0126332R
package test.cases;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.swing.filechooser.FileSystemView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import main.paddletask.storage.api.StorageController;
import main.paddletask.task.entity.DeadlineTask;
import main.paddletask.task.entity.FloatingTask;
import main.paddletask.task.entity.Task;
import main.paddletask.task.entity.Task.RECUR_TYPE;
import main.paddletask.task.entity.TimedTask;

public class TestStorageController {
    /*** Variables ***/
    protected static String fileName;
    StorageController sController;
    ArrayList<Task> testTaskList;
    byte[] backedUpContent = null;
    
    /*** Setup and Teardown ***/
    @Before
    public void setUp() throws Exception {
        sController = StorageController.getInstance();
        fileName = StorageController.DEFAULT_FILE;
        backUpData();
    }

    @After
    public void tearDown() throws Exception {
        restoreData();
    }
    
    public void backUpData() {
        File file = new File(fileName);
        if (file.exists()) {
            backedUpContent = sController.getFileInBytes(fileName);
        }
    }
    
    public void restoreData() {
        if (backedUpContent != null) {
            sController.writeBytesToFile(fileName, backedUpContent, false);
        }
    }

    public ArrayList<Task> repopulateTask() {
        // Populate sample arraylist
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Task task;
        task = new DeadlineTask(1, "Complete CS2103 CE2", LocalDateTime.parse("2015-09-01 12:30", formatter), LocalDateTime.parse("2015-09-10 12:30", formatter), LocalDateTime.parse("2015-09-10 12:25", formatter), true, 1, new ArrayList<String>(), false, RECUR_TYPE.NULL);
        testTaskList.add(task);
        task = new TimedTask(2, "Appointment with dentist", LocalDateTime.parse("2015-09-02 16:34", formatter), LocalDateTime.parse("2015-09-10 14:30", formatter), LocalDateTime.parse("2015-09-10 15:30", formatter), LocalDateTime.parse("2015-09-10 15:25", formatter), false, 2, new ArrayList<String>(), true, RECUR_TYPE.YEAR);
        testTaskList.add(task);
        task = new FloatingTask(3, "Buy groceries", LocalDateTime.parse("2015-09-05 13:03", formatter), true, 3, new ArrayList<String>());
        testTaskList.add(task);
        task = new DeadlineTask(4, "Submit CS2106 Lab 3", LocalDateTime.parse("2015-09-06 22:16", formatter), LocalDateTime.parse("2015-09-10 23:59", formatter), LocalDateTime.parse("2015-09-10 23:54", formatter), false, 3, new ArrayList<String>(), false, RECUR_TYPE.NULL);
        testTaskList.add(task);
        task = new TimedTask(5, "Go out gai gai", LocalDateTime.parse("2015-09-07 23:00", formatter), LocalDateTime.parse("2015-09-17 13:00", formatter), LocalDateTime.parse("2015-09-17 17:00", formatter), LocalDateTime.parse("2015-09-17 16:55", formatter), true, 2, new ArrayList<String>(), true, RECUR_TYPE.WEEK);
        testTaskList.add(task);
        task = new FloatingTask(6, "Eat more Vitamin C", LocalDateTime.parse("2015-09-07 23:01", formatter), false, 1, new ArrayList<String>());
        testTaskList.add(task);
        task = new DeadlineTask(7, "Pay money for steamboat", LocalDateTime.parse("2015-09-08 08:55", formatter), LocalDateTime.parse("2015-09-10 23:59", formatter), LocalDateTime.parse("2015-09-10 23:54", formatter), false, 2, new ArrayList<String>(), false, RECUR_TYPE.NULL);
        testTaskList.add(task);
        task = new TimedTask(8, "Walk the neighbor's dog", LocalDateTime.parse("2015-09-08 10:20", formatter), LocalDateTime.parse("2015-09-13 17:00", formatter), LocalDateTime.parse("2015-09-13 18:00", formatter), LocalDateTime.parse("2015-09-13 17:55", formatter), true, 1, new ArrayList<String>(), false, RECUR_TYPE.DAY);
        testTaskList.add(task);
        task = new FloatingTask(9, "Stock up locker with maggimee", LocalDateTime.parse("2015-09-10 11:45", formatter), true, 3, new ArrayList<String>());
        testTaskList.add(task);
        task = new DeadlineTask(10, "Sign up for linux workshop", LocalDateTime.parse("2015-09-14 21:10", formatter), LocalDateTime.parse("2015-09-20 23:59", formatter), LocalDateTime.parse("2015-09-20 23:54", formatter), false, 3, new ArrayList<String>(), false, RECUR_TYPE.NULL);
        testTaskList.add(task);
        
        Document doc = sController.parseTask(testTaskList);
        sController.writeXml(doc);
        
        return testTaskList;
    }
    
    /*** Test Cases 
     * @throws Exception  ***/
    @Test
    public void testSetDirectory() throws Exception {
        String oldPath = StorageController.DEFAULT_FILE;
        int endIndex = oldPath.lastIndexOf(File.separator);
        if (endIndex != -1) {
            oldPath = oldPath.substring(0, endIndex);
        }
        
        Method m = sController.getClass().getDeclaredMethod("setDirectory", String.class);
        m.setAccessible(true);
        boolean success = (boolean)m.invoke(sController, FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath());
        if (success) {
            assert true;
        } else {
            assert false;
        }
        
        m = sController.getClass().getDeclaredMethod("setDirectory", String.class);
        m.setAccessible(true);
        success = (boolean)m.invoke(sController, oldPath);
        if (success) {
            assert true;
        } else {
            assert false;
        }
        
        File file = new File(FileSystemView.getFileSystemView().getHomeDirectory() + File.separator + StorageController.TASK_XML);
        file.delete();
    }
    
    @Test
    public void testGetXmlFile() throws Exception {
    	Method m = sController.getClass().getDeclaredMethod("getXmlFile", String.class);
		m.setAccessible(true);
        File file = (File)m.invoke(sController, fileName);
        if (file.exists()) {
            assert true;
        } else {
            assert false;
        }
        
        file = new File(fileName);
        file.delete();
        m = sController.getClass().getDeclaredMethod("getXmlFile", String.class);
        m.setAccessible(true);
        file = (File)m.invoke(sController, fileName);
        if (file.exists()) {
            assert true;
        } else {
            assert false;
        }
    }

    @Test
    public void testReadTask() {
        testTaskList = new ArrayList<Task>();
        ArrayList<Task> taskList = sController.readTask();
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
        taskList = sController.readTask();
        if (taskList.size() > 0) {
            assert true;
        } else {
            assert false;
        }
    }
    
    @Test
    public void testParseTask() {
        testTaskList = new ArrayList<Task>();
        Document doc = sController.parseTask(testTaskList);
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
        doc = sController.parseTask(testTaskList);
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
        Method m = sController.getClass().getDeclaredMethod("parseXml");
        m.setAccessible(true);
        Document doc = (Document)m.invoke(sController);
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
        Document doc = sController.parseTask(testTaskList);
        boolean result = sController.writeXml(doc);
        assertEquals(true, result);
        
        testTaskList = repopulateTask();
        doc = sController.parseTask(testTaskList);
        result = sController.writeXml(doc);
        assertEquals(true, result);
    }
}
