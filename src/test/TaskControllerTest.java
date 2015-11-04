package test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import storage.api.StorageController;
import task.api.TaskController;
import task.entity.DeadlineTask;
import task.entity.FloatingTask;
import task.entity.Task;
import task.entity.TimedTask;
import task.entity.Task.RECUR_TYPE;
import task.entity.Task.TASK_TYPE;

public class TaskControllerTest {
    /*** Variables ***/
    protected static final String FILE_NAME = "task.xml";
    StorageController sController;
    ArrayList<Task> testTaskList;
    TaskController tController;
    
    /*** Setup and Teardown ***/
    @Before
    public void setUp() throws Exception {
        sController = StorageController.getInstance();
        testTaskList = repopulateTask();
        tController = TaskController.getInstance();
    }

    @After
    public void tearDown() throws Exception {
    }
    
    public ArrayList<Task> repopulateTask() {
        ArrayList<Task> testTaskList = new ArrayList<Task>();
        
        // Populate sample arraylist
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Task task;
        task = new DeadlineTask(1, "CS2103 CE2", LocalDateTime.parse("2015-09-01 12:30", formatter), LocalDateTime.parse("2015-09-10 12:30", formatter), LocalDateTime.parse("2015-09-10 12:25", formatter), true, 1, new ArrayList<String>(), false, RECUR_TYPE.NULL);
        testTaskList.add(task);
        task = new TimedTask(2, "Appointment with dentist", LocalDateTime.parse("2015-09-02 16:34", formatter), LocalDateTime.parse("2015-09-10 14:30", formatter), LocalDateTime.parse("2015-09-10 15:30", formatter), LocalDateTime.parse("2015-09-10 15:25", formatter), false, 2, new ArrayList<String>(), true, RECUR_TYPE.YEAR);
        testTaskList.add(task);
        task = new FloatingTask(3, "Buy chicken", LocalDateTime.parse("2015-09-05 13:03", formatter), true, 3, new ArrayList<String>());
        testTaskList.add(task);
        task = new DeadlineTask(4, "Submit CS2106 Lab 1", LocalDateTime.parse("2015-09-06 22:16", formatter), LocalDateTime.parse("2015-09-10 23:59", formatter), LocalDateTime.parse("2015-09-10 23:54", formatter), false, 3, new ArrayList<String>(), false, RECUR_TYPE.NULL);
        testTaskList.add(task);
        task = new TimedTask(5, "Go out gai gai", LocalDateTime.parse("2015-09-07 23:00", formatter), LocalDateTime.parse("2015-09-17 13:00", formatter), LocalDateTime.parse("2015-09-17 17:00", formatter), LocalDateTime.parse("2015-09-17 16:55", formatter), true, 2, new ArrayList<String>(), true, RECUR_TYPE.WEEK);
        testTaskList.add(task);
        task = new FloatingTask(6, "Eat more chicken", LocalDateTime.parse("2015-09-07 23:01", formatter), false, 1, new ArrayList<String>());
        testTaskList.add(task);
        task = new DeadlineTask(7, "Pay money for steamboat", LocalDateTime.parse("2015-09-08 08:55", formatter), LocalDateTime.parse("2015-09-10 23:59", formatter), LocalDateTime.parse("2015-09-10 23:54", formatter), false, 2, new ArrayList<String>(), false, RECUR_TYPE.NULL);
        testTaskList.add(task);
        task = new TimedTask(8, "Walk the neighbor's dog", LocalDateTime.parse("2015-09-08 10:20", formatter), LocalDateTime.parse("2015-09-13 17:00", formatter), LocalDateTime.parse("2015-09-13 18:00", formatter), LocalDateTime.parse("2015-09-13 17:55", formatter), true, 1, new ArrayList<String>(), false, RECUR_TYPE.DAY);
        testTaskList.add(task);
        task = new FloatingTask(9, "Stock up locker", LocalDateTime.parse("2015-09-10 11:45", formatter), true, 3, new ArrayList<String>());
        testTaskList.add(task);
        task = new DeadlineTask(10, "Sign up for chicken eating competition", LocalDateTime.parse("2015-09-14 21:10", formatter), LocalDateTime.parse("2015-09-20 23:59", formatter), LocalDateTime.parse("2015-09-20 23:54", formatter), false, 3, new ArrayList<String>(), false, RECUR_TYPE.NULL);
        testTaskList.add(task);
        
        Document doc = sController.parseTask(testTaskList);
        sController.writeXml(doc);
        
        return testTaskList;
    }

    /*** Test Cases ***/
    @Test
    public void testAddTask() {
        // Test first add
        File file = new File(FILE_NAME);
        if (file.exists()) {
            System.gc();
            try {
                Files.delete(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Task.setTaskList(new ArrayList<Task>());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Task task = new DeadlineTask("My first task", LocalDateTime.parse("2015-09-20 12:00", formatter), LocalDateTime.parse("2015-09-20 11:55", formatter), 1, false, RECUR_TYPE.NULL);
        boolean result = tController.addTask(task);
        assertEquals(true, result);
        assertEquals(1, Task.getTaskList().size());
        
        // Set up
        testTaskList = repopulateTask();
        Task.setTaskList(testTaskList);
        
        // Add DeadlineTask
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        task = new DeadlineTask("Stock up rainbow icecream", LocalDateTime.parse("2015-09-20 12:00", formatter), LocalDateTime.parse("2015-09-20 11:55", formatter), 2, false, RECUR_TYPE.NULL);
        result = tController.addTask(task);
        assertEquals(true, result);
        assertEquals(11, Task.getTaskList().size());
        
        // Add TimedTask
        task = new TimedTask("Eat lunch with senpai", LocalDateTime.parse("2015-10-01 12:00", formatter), LocalDateTime.parse("2015-10-01 14:00", formatter), LocalDateTime.parse("2015-10-01 13:55", formatter), 3, false, RECUR_TYPE.NULL);
        result = tController.addTask(task);
        assertEquals(true, result);
        assertEquals(12, Task.getTaskList().size());
        
        // Add TimedTask
        task = new FloatingTask("Rainbows and clouds :3", 1);
        result = tController.addTask(task);
        assertEquals(true, result);
        assertEquals(13, Task.getTaskList().size());
    }
    
    @Test
    public void testGetTask() {
        // Set up
        testTaskList = repopulateTask();
        Task.setTaskList(testTaskList);
        
        // Perform test
        ArrayList<Task> taskList = tController.getTask();
        assertEquals(testTaskList.size(), taskList.size());
    }
    
    @Test
    public void testGetTaskString() {
        // Set up
        testTaskList = repopulateTask();
        Task.setTaskList(testTaskList);
        
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
        Task.setTaskList(testTaskList);
        
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
        Task.setTaskList(testTaskList);
        
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
        Task.setTaskList(testTaskList);
        
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
        Task.setTaskList(testTaskList);
        
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
        Task.setTaskList(testTaskList);
        
        // Perform update
        tController.writeAllToFile(testTaskList);
        
        // Check
        updatedTaskList = tController.getTask();
        assertEquals(10, updatedTaskList.size());
    }
    
    @Test
    public void testGetAvailableTaskId() {
        // Set up
        testTaskList = new ArrayList<Task>();
        Task.setTaskList(testTaskList);
        tController.writeAllToFile(testTaskList);
        
        // Perform test
        int result = tController.getAvailableTaskId();
        assertEquals(1, result);
        
        // Set up
        testTaskList = repopulateTask();
        tController.writeAllToFile(testTaskList);
        
        // Perform test
        result = tController.getAvailableTaskId();
        assertEquals(testTaskList.size() + 1, result);
    }
}
