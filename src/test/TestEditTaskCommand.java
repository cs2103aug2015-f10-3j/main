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
import common.exception.NoSuchTaskException;
import common.exception.UpdateTaskException;
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
		commandParserInstance = new CommandParser();
	}

	public ArrayList<Task> repopulateTask() {
		ArrayList<Task> testTaskList = new ArrayList<Task>();

		// Populate sample arraylist
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		Task task;
		task = new FloatingTask(1, "Dabian", DateTimeHelper.now(), false);
		testTaskList.add(task);
		task = new DeadlineTask(2, "CS2103 CE2", DateTimeHelper.now(), LocalDateTime.parse("2015-01-02 23:59", formatter), DateTimeHelper.now(),false);
		testTaskList.add(task);
		task = new TimedTask(3, "Appointment with dentist", DateTimeHelper.now(), LocalDateTime.parse("2015-01-03 23:59", formatter), LocalDateTime.parse("2015-09-10 15:30", formatter), DateTimeHelper.now(), false);
		testTaskList.add(task);
		Document doc = storageControllerInstance.parseTask(testTaskList);
		storageControllerInstance.writeXml(doc);
		return testTaskList;
	}

	public void prepareDummyData() {
		testTaskList = repopulateTask();
		Task.setTaskList(testTaskList);
	}

	/*** Test Cases ***/

	/*** FloatingTask related test cases 
	 * @throws UpdateTaskException 
	 * @throws NoSuchTaskException ***/

	@Test
	public void editFloatingDescription() throws InvalidCommandFormatException, NoSuchTaskException, UpdateTaskException {
		prepareDummyData();

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
	public void addEndTimeToFloating() throws InvalidCommandFormatException, NoSuchTaskException, UpdateTaskException{
		prepareDummyData();

		// Build my End with default date and custom time
		String defaultDate = DateTimeHelper.getDate(DateTimeHelper.now());
		String testTime = "11:11";
		dummy_userCommand = "edit 1 end " + testTime;

		// Verify that task to edit is still Floating if casting is successful
		dummy_floatingTask = (FloatingTask) taskControllerInstance.getTask(0);

		//Execute command
		dummy_editTaskCommand = (EditTaskCommand) commandParserInstance.parse(dummy_userCommand);
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
	public void addEndDateToFloating() throws InvalidCommandFormatException, NoSuchTaskException, UpdateTaskException {
		prepareDummyData();

		// Build my End with custom date and default time
		String testDate = DateTimeHelper.getDate(DateTimeHelper.now());
		String defaultTime = "00:00";
		dummy_userCommand = "edit 1 end " + testDate;

		// Verify that task to edit is still Floating if casting is successful
		dummy_floatingTask = (FloatingTask)taskControllerInstance.getTask(0);

		//Execute command
		dummy_editTaskCommand = (EditTaskCommand) commandParserInstance.parse(dummy_userCommand);
		dummy_editTaskCommand.execute();

		// Verify become Deadline
		dummy_genericTask = taskControllerInstance.getTask(1);
		assertEquals("test addEndDateToFloating: verify new task is deadline",
				dummy_genericTask.getType(), Task.TASK_TYPE.DEADLINE);

		// Verify new End Date is given user specified date
		dummy_deadlineTask = (DeadlineTask) dummy_genericTask;
		String editedEndDate = DateTimeHelper.getDate(dummy_deadlineTask.getEnd());
		assertEquals("test addEndDateToFloating: verify edited task end date",
				testDate, editedEndDate);

		// Verify new End Time is given default time of 00:00
		String editedEndTime = DateTimeHelper.getTime(dummy_deadlineTask.getEnd());
		assertEquals("test addEndDateToFloating: verify edited task end time default to 00:00",
				defaultTime, editedEndTime);
	}

	@Test
	public void addEndDateAndTimeToFloating() throws InvalidCommandFormatException, NoSuchTaskException, UpdateTaskException {
		prepareDummyData();

		// Build my End with custom date and time
		String testDateTime = "01/01/2015 12:34";
		dummy_userCommand = "edit 1 end " + testDateTime;

		// Verify that task to edit is still Floating if casting is successful
		dummy_floatingTask = (FloatingTask)taskControllerInstance.getTask(0);

		//Execute command
		dummy_editTaskCommand = (EditTaskCommand) commandParserInstance.parse(dummy_userCommand);
		dummy_editTaskCommand.execute();

		// Verify become Deadline
		dummy_genericTask = taskControllerInstance.getTask(1);
		assertEquals("test addEndDateAndTimeToFloating: verify new task is deadline",
				dummy_genericTask.getType(), Task.TASK_TYPE.DEADLINE);

		// Verify new End is the expected End
		dummy_deadlineTask = (DeadlineTask) dummy_genericTask;
		String editedEnd = DateTimeHelper.parseDateTimeToString(dummy_deadlineTask.getEnd());
		assertEquals("test addEndDateAndTimeToFloating: verify edited task end date and time",
				testDateTime, editedEnd);
	}

	@Test
	public void addStartAndEndToFloating() throws InvalidCommandFormatException, NoSuchTaskException, UpdateTaskException {
		prepareDummyData();

		// Build my Start and End with custom date and time
		String testStart = "01/01/2015 10:00";
		String testEnd = "01/02/2015 23:59";
		dummy_userCommand = "edit 1 start " + testStart + " end " + testEnd;

		// Verify that task to edit is still Floating if casting is successful
		dummy_floatingTask = (FloatingTask)taskControllerInstance.getTask(0);

		//Execute command
		dummy_editTaskCommand = (EditTaskCommand) commandParserInstance.parse(dummy_userCommand);
		dummy_editTaskCommand.execute();

		// Verify become Timed
		dummy_genericTask = taskControllerInstance.getTask(1);
		assertEquals("test addStartAndEndToFloating: verify new task is Timed",
				dummy_genericTask.getType(), Task.TASK_TYPE.TIMED);
		
		// Verify new Start and End is the expected Start and End
		dummy_timedTask = (TimedTask) dummy_genericTask;
		String editedStart = DateTimeHelper.parseDateTimeToString(dummy_timedTask.getStart());
		String editedEnd = DateTimeHelper.parseDateTimeToString(dummy_timedTask.getEnd());		
		assertEquals("test addStartAndEndToFloating: verify edited task start",
				testStart, editedStart);
		assertEquals("test addStartAndEndToFloating: verify edited task start",
				testEnd, editedEnd);
	}

	/*** DeadlineTask related test cases 
	 * @throws UpdateTaskException 
	 * @throws NoSuchTaskException ***/
	
	@Test
	public void editDeadlineDescription() throws InvalidCommandFormatException, NoSuchTaskException, UpdateTaskException {
		prepareDummyData();

		// Initialize dummy user command and description that will be used for testing
		String testDescription = "Testing Deadline Description Change!";
		dummy_userCommand = "edit 2 name " + testDescription;
		dummy_deadlineTask = (DeadlineTask) taskControllerInstance.getTask(2);

		// Verify that original task description is not the one that will be used for testing
		assertNotEquals("test editDeadlineDescription: initial description not equals the one used for testing",
				testDescription,dummy_deadlineTask.getDescription());

		// Execute edit command
		dummy_editTaskCommand = (EditTaskCommand) commandParserInstance.parse(dummy_userCommand);
		dummy_editTaskCommand.execute();

		// Verify if task description has been correctly set
		dummy_deadlineTask = (DeadlineTask) taskControllerInstance.getTask(2);
		assertEquals("test editDeadlineDescription: new description set",
				testDescription, dummy_deadlineTask.getDescription());
	}

	@Test
	public void editDeadlineEndTime() {
		prepareDummyData();
		
		
	}

	@Test
	public void editDeadlineEndDate() {
		
	}
	
	@Test
	public void editDeadlineEndDateTime() {
		
	}
	
	@Test
	public void editDeadlineDescriptionAndEnd() {

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