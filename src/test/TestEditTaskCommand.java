package test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import command.api.EditTaskCommand;
import storage.api.StorageController;
import task.api.TaskController;
import task.entity.DeadlineTask;
import task.entity.FloatingTask;
import task.entity.Task;
import task.entity.TimedTask;
import parser.api.CommandParser;
import common.exception.InvalidCommandFormatException;
import common.util.DateTimeHelper;

public class TestEditTaskCommand {
    /*
     * Test Assumption:
     * 1. StorageController is working correctly
     * 2. TaskController is working correctly
     * 3. CommandParser is working correctly
     */
	
	/*** Variables ***/
    protected static ArrayList<Task> testTaskList;
    protected static StorageController storageControllerInstance;
    protected static TaskController taskControllerInstance;
	protected static CommandParser commandParserInstance;
	
	protected static String dummy_userCommand;
	protected static EditTaskCommand dummy_editTaskCommand;
	protected static FloatingTask dummy_floatingTask;
	protected static DeadlineTask dummy_deadlineTask;
	protected static TimedTask dummy_timedTask;
	protected static Task dummy_genericTask;

	/*** Setup ***/
	@Before
	public void setUp() throws Exception {
		storageControllerInstance = StorageController.getInstance();
		testTaskList = repopulateTask();
		taskControllerInstance = TaskController.getInstance();
        commandParserInstance = new CommandParser(null);
	}

	public ArrayList<Task> repopulateTask() {
        ArrayList<Task> testTaskList = new ArrayList<Task>();
  
        // Populate sample arraylist
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Task task;
        task = new FloatingTask(1, "Dabian", LocalDateTime.parse("2015-01-01 00:00", formatter), false);
        testTaskList.add(task);
        task = new DeadlineTask(2, "CS2103 CE2", LocalDateTime.parse("2015-01-02 20:15", formatter), LocalDateTime.parse("2015-01-02 23:59", formatter), false);
        testTaskList.add(task);
        task = new TimedTask(3, "Appointment with dentist", LocalDateTime.parse("2015-01-03 12:34", formatter), LocalDateTime.parse("2015-01-03 23:59", formatter), LocalDateTime.parse("2015-09-10 15:30", formatter), false);
        testTaskList.add(task);
        Document doc = storageControllerInstance.parseTask(testTaskList);
        storageControllerInstance.writeXml(doc);
        return testTaskList;
    }
	
	/*** Test Cases ***/
	/*** FloatingTask related test cases 
	 * @throws InvalidCommandFormatException ***/
	@Test
	public void editFloatingDescription() throws InvalidCommandFormatException {
		testTaskList = repopulateTask();
		Task.setTaskList(testTaskList);
		// Initialize dummy user command and description that will be used for testing
		String testDescription = "Testing Floating Description Change!";
		dummy_userCommand = "edit 1 name " + testDescription;
		dummy_floatingTask = (FloatingTask) taskControllerInstance.getTask(1);
		
		// Verify that original task description is not the one that will be used for testing
		assertNotEquals("test editFloatingDescription: initial description not equals the one used for testing",
				testDescription,dummy_floatingTask.getDescription());
		
		// Execute edit command
		dummy_editTaskCommand = (EditTaskCommand)commandParserInstance.parse(dummy_userCommand);
		dummy_editTaskCommand.execute();
		
		// Verify if task description has been correctly set
		dummy_floatingTask = (FloatingTask) taskControllerInstance.getTask(1);
		assertEquals("test editFloatingDescription: new description set",
				testDescription, dummy_floatingTask.getDescription());
	}
	
	@Test
	public void addEndTimeToFloating() throws InvalidCommandFormatException{
		testTaskList = repopulateTask();
		Task.setTaskList(testTaskList);
		// build my date with current date and fixed time
		String defaultDate = DateTimeHelper.getDate(DateTimeHelper.now());
		String testTime = "11:11";
		dummy_userCommand = "edit 1 end " + testTime;
		
		// Verify that task to edit is still Floating if casting is successful
		dummy_floatingTask = (FloatingTask)taskControllerInstance.getTask(0);
		
		//Execute command
		dummy_editTaskCommand = (EditTaskCommand)commandParserInstance.parse(dummy_userCommand);
		dummy_editTaskCommand.execute();
		
		// Verify become Deadline
		dummy_genericTask = taskControllerInstance.getTask(1);
		assertEquals("test addEndTimeToFloating: verify new task is deadline",
				dummy_genericTask.getType(), Task.TASK_TYPE.DEADLINE);
		
		// Verify new End is the expected End
		dummy_deadlineTask = (DeadlineTask) dummy_genericTask;
		LocalDateTime editedEnd = dummy_deadlineTask.getEnd();
		
		// Verify new End Date is given default value of now() date
		String editedEndDate = DateTimeHelper.getDate(editedEnd);
		assertEquals("test addEndTimeToFloating: verify edited task end date default to today's date",
				defaultDate,editedEndDate);
		
		// Verify new End Time is given user specified value
		String editedEndTime = DateTimeHelper.getTime(editedEnd);
		assertEquals("test addEndTimeToFloating: verify edited task end time",
				testTime, editedEndTime);
	}	
	
	@Test
	public void addEndDateToFloating() throws InvalidCommandFormatException {
		testTaskList = repopulateTask();
		Task.setTaskList(testTaskList);
		// build my date with current date and fixed time
		String testDate = DateTimeHelper.getDate(DateTimeHelper.now());
		String defaultTime = "00:00";
		dummy_userCommand = "edit 1 end " + testDate;
		
		// Verify that task to edit is still Floating if casting is successful
		dummy_floatingTask = (FloatingTask)taskControllerInstance.getTask(0);
		
		//Execute command
		dummy_editTaskCommand = (EditTaskCommand)commandParserInstance.parse(dummy_userCommand);
		dummy_editTaskCommand.execute();
		
		// Verify become Deadline
		dummy_genericTask = taskControllerInstance.getTask(1);
		assertEquals("test addEndTimeToFloating: verify new task is deadline",
				dummy_genericTask.getType(), Task.TASK_TYPE.DEADLINE);
		
		// Verify new End is the expected End
		dummy_deadlineTask = (DeadlineTask) dummy_genericTask;
		LocalDateTime editedEnd = dummy_deadlineTask.getEnd();
		
		// Verify new End Date is given default value of now() date
		String editedEndDate = DateTimeHelper.getDate(editedEnd);
		assertEquals("test addEndTimeToFloating: verify edited task end date default to today's date",
				testDate, editedEndDate);
		
		// Verify new End Time is given user specified value
		String editedEndTime = DateTimeHelper.getTime(editedEnd);
		assertEquals("test addEndTimeToFloating: verify edited task end time",
				defaultTime, editedEndTime);
	}
	
	@Test
	public void addEndDateAndTimeToFloating() {
		
	}
	
	@Test
	public void addStartAndEndToFloating() {
		
	}
	
	/*** DeadlineTask related test cases***/
	@Test
	public void editDeadlineDescription() {
		
	}
	
	@Test
	public void editDeadlineEnd() {
		
	}
	
	@Test
	public void editDeadlineDescriptionAndEnd() {
		
	}
	
	@Test
	public void editDeadlineCompleteStatus() {
		
	}
	
	@Test
	public void addEndToDeadline() {
		
	}

	@Test
	public void addStartToDeadline() {
		
	}
	
	@Test
	public void addStartAndEndToDeadline() {
		
	}
	
	// Reminder related test case for DeadlineTask
	
	/*** TimedTask related test cases ***/
	@Test
	public void editTimedDescription() {
		
	}
	
	@Test
	public void editTimedStart() {
		
	}
	
	@Test
	public void editTimedEnd() {
		
	}
	
	@Test
	public void editTimedCompleteStatus() {
		
	}
	
	@Test
	public void editTimedDescriptionAndStart() {
		
	}
	
	@Test
	public void editTimedDescriptionAndStartAndEnd() {
		
	}
	
	@Test
	public void editTimedDescriptionAndStartAndEndAndCompleteStatus() {
		
	}
	
	// Reminder related test case for TimedTask
}
