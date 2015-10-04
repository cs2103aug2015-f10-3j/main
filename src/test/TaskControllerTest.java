package test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import logic.TaskController;
import logic.data.DeadlineTask;
import logic.data.FloatingTask;
import logic.data.Task;
import logic.data.TimedTask;
import logic.data.Task.TASK_TYPE;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import storage.StorageController;

public class TaskControllerTest {
    /*** Variables ***/
    StorageController sController;
    ArrayList<Task> testTaskList;
    TaskController tController;
    
    /*** Setup and Teardown ***/
    @Before
    public void setUp() throws Exception {
        sController = new StorageController();
        testTaskList = repopulateTask();
        tController = new TaskController();
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
        task = new TimedTask(2, "Appointment with dentist", LocalDateTime.parse("2015-09-02 16:34", formatter), LocalDateTime.parse("2015-09-10 14:30", formatter), LocalDateTime.parse("2015-09-10 15:30", formatter), false);
        testTaskList.add(task);
        task = new FloatingTask(3, "Buy chicken", LocalDateTime.parse("2015-09-05 13:03", formatter), true);
        testTaskList.add(task);
        task = new DeadlineTask(4, "Submit CS2106 Lab 1", LocalDateTime.parse("2015-09-06 22:16", formatter), LocalDateTime.parse("2015-09-10 23:59", formatter), false);
        testTaskList.add(task);
        task = new TimedTask(5, "Go out gaigai", LocalDateTime.parse("2015-09-07 23:00", formatter), LocalDateTime.parse("2015-09-17 13:00", formatter), LocalDateTime.parse("2015-09-17 17:00", formatter), true);
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
        
        Document doc = sController.parseTask(testTaskList);
        sController.writeXml(doc);
        
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
        boolean result = tController.addTask(task);
        assertEquals(true, result);
        assertEquals(11, Task.getTaskList().size());
        
        // Add TimedTask
        task = new TimedTask("Eat lunch with senpai", LocalDateTime.parse("2015-10-01 12:00", formatter), LocalDateTime.parse("2015-10-01 14:00", formatter));
        result = tController.addTask(task);
        assertEquals(true, result);
        assertEquals(12, Task.getTaskList().size());
        
        // Add TimedTask
        task = new FloatingTask("Rainbows and clouds :3");
        result = tController.addTask(task);
        assertEquals(true, result);
        assertEquals(13, Task.getTaskList().size());
    }
    
    @Test
    public void testGetTask() {
        // Set up
        testTaskList = repopulateTask();
        
        // Perform test
        ArrayList<Task> taskList = tController.getTask();
        assertEquals(testTaskList.size(), taskList.size());
    }
    
    @Test
    public void testGetTaskString() {
        // Set up
        testTaskList = repopulateTask();
        
        // Get DeadlineTask
        ArrayList<Task> filteredTaskList = tController.getTask(TASK_TYPE.DEADLINE);
        assertEquals(4, filteredTaskList.size());
        
        // Get TimedTask
        filteredTaskList = tController.getTask(TASK_TYPE.TIMED);
        assertEquals(3, filteredTaskList.size());
        
        // Get FloatingTask
        filteredTaskList = tController.getTask(TASK_TYPE.FLOATING);
        assertEquals(3, filteredTaskList.size());
        
    }
    
    @Test
    public void testGetTaskInt() {
        // Set up
        testTaskList = repopulateTask();
        
        // Get DeadlineTask
        Task task = tController.getTask(5);
        assertEquals(5, task.getTaskId());
        
        // Get null
        task = tController.getTask(100);
        assertEquals(null, task);
        
    }
    
    @Test
    public void testUpdateTask() {
        // Set up
        ArrayList<Task> updatedTaskList = new ArrayList<Task>();
        testTaskList = repopulateTask();
        
        // Perform update
        Task task = testTaskList.get(5);
        task.setDescription("Submit CS2106 Lab is cancelled lol");
        tController.updateTask(task);
        
        // Check
        updatedTaskList = tController.getTask();
        assertEquals(task.getTaskId(), updatedTaskList.get(5).getTaskId());
        assertEquals("Submit CS2106 Lab is cancelled lol", updatedTaskList.get(5).getDescription());
    }
    
    @Test
    public void testDeleteTask() {
        // Set up
        ArrayList<Task> updatedTaskList = new ArrayList<Task>();
        testTaskList = repopulateTask();
        
        // Perform update
        tController.deleteTask(3);
        
        // Check
        updatedTaskList = tController.getTask();
        assertEquals(9, updatedTaskList.size());
    }
    
    @Test
    public void testCompleteTask() {
        // Set up
        testTaskList = repopulateTask();
        
        // Perform update
        tController.completeTask(4);
        
        // Check
        Task task = tController.getTask(4);
        assertEquals(true, ((DeadlineTask) task).isComplete());
    }
    
    @Test
    public void testWriteAllToFile() {
        // Set up
        ArrayList<Task> updatedTaskList = new ArrayList<Task>();
        testTaskList = repopulateTask();
        
        // Perform update
        tController.writeAllToFile(testTaskList);
        
        // Check
        updatedTaskList = tController.getTask();
        assertEquals(10, updatedTaskList.size());
    }
    
    @Test
    public void testGetAvailableTaskId() {
        // Set up
        testTaskList = repopulateTask();
        
        // Perform test
        int result = tController.getAvailableTaskId();
        assertEquals(testTaskList.size() + 1, result);
    }
}
