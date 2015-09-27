package storage;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import storage.data.DeadlineTask;
import storage.data.FloatingTask;
import storage.data.Task;
import storage.data.TimedTask;

public class StorageControllerTest {
    /*** Variables ***/
    StorageDataParser sdParser;
    ArrayList<Task> testTaskList;
    StorageController sController;
    
    /*** Setup and Teardown ***/
    @Before
    public void setUp() throws Exception {
        sdParser = new StorageDataParser();
        testTaskList = repopulateTask();
        sController = new StorageController();
    }

    @After
    public void tearDown() throws Exception {
    }
    
    public ArrayList<Task> repopulateTask() {
        ArrayList<Task> testTaskList = new ArrayList<Task>();
        
        // Populate sample arraylist
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Task task;
        task = new DeadlineTask(1, "CS2103 CE2", LocalDateTime.parse("2015-09-01 12:30", formatter), LocalDateTime.parse("2015-09-10 12:30", formatter), true);
        testTaskList.add(task);
        task = new TimedTask(2, "Appointment with dentist", LocalDateTime.parse("2015-09-02 16:34", formatter), LocalDateTime.parse("2015-09-10 14:30", formatter), LocalDateTime.parse("2015-09-10 15:30", formatter));
        testTaskList.add(task);
        task = new FloatingTask(3, "Buy chicken", LocalDateTime.parse("2015-09-05 13:03", formatter));
        testTaskList.add(task);
        task = new DeadlineTask(4, "Submit CS2106 Lab 1", LocalDateTime.parse("2015-09-06 22:16", formatter), LocalDateTime.parse("2015-09-10 23:59", formatter), false);
        testTaskList.add(task);
        task = new TimedTask(5, "Go out gaigai", LocalDateTime.parse("2015-09-07 23:00", formatter), LocalDateTime.parse("2015-09-17 13:00", formatter), LocalDateTime.parse("2015-09-17 17:00", formatter));
        testTaskList.add(task);
        task = new FloatingTask(6, "Eat more chicken", LocalDateTime.parse("2015-09-07 23:01", formatter));
        testTaskList.add(task);
        task = new DeadlineTask(7, "Pay money for steamboat", LocalDateTime.parse("2015-09-08 08:55", formatter), LocalDateTime.parse("2015-09-10 23:59", formatter), false);
        testTaskList.add(task);
        task = new TimedTask(8, "Walk the neighbor's dog", LocalDateTime.parse("2015-09-08 10:20", formatter), LocalDateTime.parse("2015-09-13 17:00", formatter), LocalDateTime.parse("2015-09-13 18:00", formatter));
        testTaskList.add(task);
        task = new FloatingTask(9, "Stock up locker", LocalDateTime.parse("2015-09-10 11:45", formatter));
        testTaskList.add(task);
        task = new DeadlineTask(10, "Sign up for chicken eating competition", LocalDateTime.parse("2015-09-14 21:10", formatter), LocalDateTime.parse("2015-09-20 23:59", formatter), false);
        testTaskList.add(task);
        
        Document doc = sdParser.parseTask(testTaskList);
        sdParser.writeXml(doc);
        
        return testTaskList;
    }

    /*** Test Cases ***/
    @Test
    public void testAddTask() {
        // Set up
        testTaskList = repopulateTask();
        
        // Add DeadlineTask
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Task task = new DeadlineTask("Stock up rainbow icecream", LocalDateTime.parse("2015-09-20 12:00", formatter));
        boolean result = sController.addTask(task);
        assertEquals(true, result);
        assertEquals(11, Task.getTaskList().size());
        
        // Add TimedTask
        task = new TimedTask("Eat lunch with senpai", LocalDateTime.parse("2015-10-01 12:00", formatter), LocalDateTime.parse("2015-10-01 14:00", formatter));
        result = sController.addTask(task);
        assertEquals(true, result);
        assertEquals(12, Task.getTaskList().size());
        
        // Add TimedTask
        task = new FloatingTask("Rainbows and clouds :3");
        result = sController.addTask(task);
        assertEquals(true, result);
        assertEquals(13, Task.getTaskList().size());
    }
    
    @Test
    public void testViewTask() {
        fail("Not yet implemented");
    }
    
    @Test
    public void testViewTaskString() {
        fail("Not yet implemented");
    }
    
    @Test
    public void testViewTaskInt() {
        fail("Not yet implemented");
    }
    
    @Test
    public void testUpdateTask() {
        fail("Not yet implemented");
    }
    
    @Test
    public void testCompleteTask() {
        fail("Not yet implemented");
    }
    
    @Test
    public void testWriteAllToFile() {
        fail("Not yet implemented");
    }
    
    @Test
    public void testGetAvailableTaskId() {
        // Set up
        testTaskList = repopulateTask();
        
        // Perform test
        int result = sController.getAvailableTaskId();
        assertEquals(testTaskList.size() + 1, result);
    }
}
