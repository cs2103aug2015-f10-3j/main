package test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import command.api.EditTaskCommand;
import command.api.ViewTaskCommand;
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
	protected static ArrayList<Task> testStateList;
	protected static StorageController storageControllerInstance;
	protected static TaskController taskControllerInstance;
	protected static CommandParser commandParserInstance;
	protected static int[] stateTaskId;
	
	protected static String DUMMY_USERCOMMAND = "";
	protected static EditTaskCommand DUMMY_EDIT_TASK_COMMAND = new EditTaskCommand();
	protected static FloatingTask DUMMY_FLOATING_TASK = new FloatingTask();
	protected static DeadlineTask DUMMY_DEADLINE_TASK = new DeadlineTask();
	protected static TimedTask DUMMY_TIMED_TASK = new TimedTask();
	protected static Task DUMMY_GENERIC_TASK = new Task();

	/*** Setup ***/
	@Before
	public void setUp() throws Exception {
		storageControllerInstance = StorageController.getInstance();
		testTaskList = repopulateTask();
		taskControllerInstance = TaskController.getInstance();
		commandParserInstance = new CommandParser();
	}

	public ArrayList<Task> repopulateTask() throws Exception {
		ArrayList<Task> testTaskList = new ArrayList<Task>();
		commandParserInstance = new CommandParser();
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
		initTaskState();
		return testTaskList;
	}

	public void initTaskState() throws Exception{
		String viewInputCommand = "view";
		ViewTaskCommand viewCommand = (ViewTaskCommand) commandParserInstance.parse(viewInputCommand);
		testStateList = viewCommand.execute();
	}
	
	public int[] getStateTaskId() {
		int numIds = testStateList.size();
		stateTaskId = new int[numIds];
		for (int i=0; i<numIds; i++) {
			stateTaskId[i] = testStateList.get(i).getTaskId();
		}
		return stateTaskId;
	}
	
	public void generateTestData() throws Exception {
		testTaskList = repopulateTask();
		Task.setTaskList(testTaskList);
	}
	
	/*** Test Cases ***/
	
	@Test
	public void testEditTaskCommand(String taskType) {
		
	}
	
	/*** FloatingTask related test cases ***/

	@Test
	public void editFloatingDescription() throws Exception {
		generateTestData();

		// Initialize dummy user command and description that will be used for testing
		String testDescription = "Testing Floating Description Change!";
		DUMMY_USERCOMMAND = "edit 1 desc " + testDescription;
		DUMMY_FLOATING_TASK = (FloatingTask) taskControllerInstance.getTask(1);

		// Verify that original task description is not the one that will be used for testing
		assertNotEquals("test editFloatingDescription: initial description not equals the one used for testing",
				testDescription,DUMMY_FLOATING_TASK.getDescription());

		// Execute edit command
		DUMMY_EDIT_TASK_COMMAND = (EditTaskCommand)commandParserInstance.parse(DUMMY_USERCOMMAND,getStateTaskId());
		DUMMY_EDIT_TASK_COMMAND.execute();

		// Verify if task description has been correctly set
		DUMMY_FLOATING_TASK = (FloatingTask) taskControllerInstance.getTask(1);
		assertEquals("test editFloatingDescription: new description set",
				testDescription, DUMMY_FLOATING_TASK.getDescription());
	}

	@Test
	public void addEndTimeToFloating() throws Exception {
		generateTestData();

		// Build my End with default date and custom time
		String defaultDate = DateTimeHelper.getDate(DateTimeHelper.now());
		String testTime = "11:11";
		DUMMY_USERCOMMAND = "edit 1 end " + testTime;

		// Verify that task to edit is still Floating if casting is successful
		DUMMY_FLOATING_TASK = (FloatingTask) taskControllerInstance.getTask(1);

		//Execute command
		DUMMY_EDIT_TASK_COMMAND = (EditTaskCommand) commandParserInstance.parse(DUMMY_USERCOMMAND,getStateTaskId());
		DUMMY_EDIT_TASK_COMMAND.execute();

		// Verify become Deadline
		DUMMY_GENERIC_TASK = taskControllerInstance.getTask(1);
		assertEquals("test addEndTimeToFloating: verify new task is deadline",
				DUMMY_GENERIC_TASK.getType(), Task.TASK_TYPE.DEADLINE);

		// Verify new End is the expected End
		DUMMY_DEADLINE_TASK = (DeadlineTask) DUMMY_GENERIC_TASK;
		LocalDateTime editedEnd = DUMMY_DEADLINE_TASK.getEnd();

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
	public void addEndDateToFloating() throws Exception {
		generateTestData();

		// Build my End with custom date and default time
		String testDate = DateTimeHelper.getDate(DateTimeHelper.now());
		String defaultTime = "00:00";
		DUMMY_USERCOMMAND = "edit 1 end " + testDate;

		// Verify that task to edit is still Floating if casting is successful
		DUMMY_FLOATING_TASK = (FloatingTask)taskControllerInstance.getTask(1);

		//Execute command
		DUMMY_EDIT_TASK_COMMAND = (EditTaskCommand) commandParserInstance.parse(DUMMY_USERCOMMAND,getStateTaskId());
		DUMMY_EDIT_TASK_COMMAND.execute();

		// Verify become Deadline
		DUMMY_GENERIC_TASK = taskControllerInstance.getTask(1);
		assertEquals("test addEndDateToFloating: verify new task is deadline",
				DUMMY_GENERIC_TASK.getType(), Task.TASK_TYPE.DEADLINE);

		// Verify new End Date is given user specified date
		DUMMY_DEADLINE_TASK = (DeadlineTask) DUMMY_GENERIC_TASK;
		String editedEndDate = DateTimeHelper.getDate(DUMMY_DEADLINE_TASK.getEnd());
		assertEquals("test addEndDateToFloating: verify edited task end date",
				testDate, editedEndDate);

		// Verify new End Time is given default time of 00:00
		String editedEndTime = DateTimeHelper.getTime(DUMMY_DEADLINE_TASK.getEnd());
		assertEquals("test addEndDateToFloating: verify edited task end time default to 00:00",
				defaultTime, editedEndTime);
	}

	@Test
	public void addEndDateAndTimeToFloating() throws Exception {
		generateTestData();

		// Build my End with custom date and time
		String testDateTime = "01/01/2015 12:34";
		DUMMY_USERCOMMAND = "edit 1 end " + testDateTime;

		// Verify that task to edit is still Floating if casting is successful
		DUMMY_FLOATING_TASK = (FloatingTask)taskControllerInstance.getTask(0);

		//Execute command
		DUMMY_EDIT_TASK_COMMAND = (EditTaskCommand) commandParserInstance.parse(DUMMY_USERCOMMAND);
		DUMMY_EDIT_TASK_COMMAND.execute();

		// Verify become Deadline
		DUMMY_GENERIC_TASK = taskControllerInstance.getTask(1);
		assertEquals("test addEndDateAndTimeToFloating: verify new task is deadline",
				DUMMY_GENERIC_TASK.getType(), Task.TASK_TYPE.DEADLINE);

		// Verify new End is the expected End
		DUMMY_DEADLINE_TASK = (DeadlineTask) DUMMY_GENERIC_TASK;
		String editedEnd = DateTimeHelper.parseDateTimeToString(DUMMY_DEADLINE_TASK.getEnd());
		assertEquals("test addEndDateAndTimeToFloating: verify edited task end date and time",
				testDateTime, editedEnd);
	}

	@Test
	public void addStartAndEndToFloating() throws Exception {
		generateTestData();

		// Build my Start and End with custom date and time
		String testStart = "01/01/2015 10:00";
		String testEnd = "01/02/2015 23:59";
		DUMMY_USERCOMMAND = "edit 1 start " + testStart + " end " + testEnd;

		// Verify that task to edit is still Floating if casting is successful
		DUMMY_FLOATING_TASK = (FloatingTask)taskControllerInstance.getTask(0);

		//Execute command
		DUMMY_EDIT_TASK_COMMAND = (EditTaskCommand) commandParserInstance.parse(DUMMY_USERCOMMAND);
		DUMMY_EDIT_TASK_COMMAND.execute();

		// Verify become Timed
		DUMMY_GENERIC_TASK = taskControllerInstance.getTask(1);
		assertEquals("test addStartAndEndToFloating: verify new task is Timed",
				DUMMY_GENERIC_TASK.getType(), Task.TASK_TYPE.TIMED);
		
		// Verify new Start and End is the expected Start and End
		DUMMY_TIMED_TASK = (TimedTask) DUMMY_GENERIC_TASK;
		String editedStart = DateTimeHelper.parseDateTimeToString(DUMMY_TIMED_TASK.getStart());
		String editedEnd = DateTimeHelper.parseDateTimeToString(DUMMY_TIMED_TASK.getEnd());		
		assertEquals("test addStartAndEndToFloating: verify edited task start",
				testStart, editedStart);
		assertEquals("test addStartAndEndToFloating: verify edited task start",
				testEnd, editedEnd);
	}

	/*** DeadlineTask related test cases 
	 * @throws UpdateTaskException 
	 * @throws NoSuchTaskException ***/
	
	@Test
	public void editDeadlineDescription() throws Exception {
		generateTestData();

		// Initialize dummy user command and description that will be used for testing
		String testDescription = "Testing Deadline Description Change!";
		DUMMY_USERCOMMAND = "edit 2 desc " + testDescription;
		DUMMY_DEADLINE_TASK = (DeadlineTask) taskControllerInstance.getTask(2);

		// Verify that original task description is not the one that will be used for testing
		assertNotEquals("test editDeadlineDescription: initial description not equals the one used for testing",
				testDescription,DUMMY_DEADLINE_TASK.getDescription());

		// Execute edit command
		DUMMY_EDIT_TASK_COMMAND = (EditTaskCommand) commandParserInstance.parse(DUMMY_USERCOMMAND);
		DUMMY_EDIT_TASK_COMMAND.execute();

		// Verify if task description has been correctly set
		DUMMY_DEADLINE_TASK = (DeadlineTask) taskControllerInstance.getTask(2);
		assertEquals("test editDeadlineDescription: new description set",
				testDescription, DUMMY_DEADLINE_TASK.getDescription());
	}

	@Test
	public void editDeadlineEndTime() throws Exception{
		generateTestData();

		// Build my End with default date and custom time
		String defaultDate = DateTimeHelper.getDate(DateTimeHelper.now());
		String testTime = "11:11";
		DUMMY_USERCOMMAND = "edit 2 end " + testTime;

		// Verify that task to edit is a Deadline Task
		DUMMY_DEADLINE_TASK = (DeadlineTask) taskControllerInstance.getTask(2);

		//Execute command
		DUMMY_EDIT_TASK_COMMAND = (EditTaskCommand) commandParserInstance.parse(DUMMY_USERCOMMAND,getStateTaskId());
		DUMMY_EDIT_TASK_COMMAND.execute();

		// Verify still Deadline
		DUMMY_GENERIC_TASK = taskControllerInstance.getTask(2);
		assertEquals("test editDeadlineEndTime: verify task is still deadline Task",
				DUMMY_GENERIC_TASK.getType(), Task.TASK_TYPE.DEADLINE);

		// Verify new End is the expected End
		DUMMY_DEADLINE_TASK = (DeadlineTask) DUMMY_GENERIC_TASK;
		LocalDateTime editedEnd = DUMMY_DEADLINE_TASK.getEnd();

		// Verify new End Date is given default value of now() date
		String editedEndDate = DateTimeHelper.getDate(editedEnd);
		assertEquals("test editDeadlineEndTime: verify edited task end date default to today's date",
				defaultDate,editedEndDate);

		// Verify new End Time is given user specified value
		String editedEndTime = DateTimeHelper.getTime(editedEnd);
		assertEquals("test editDeadlineEndTime: verify edited task end time",
				testTime, editedEndTime);
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