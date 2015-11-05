//@@author A0133662J
package test.cases;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import main.paddletask.command.api.EditTaskCommand;
import main.paddletask.command.api.ViewTaskCommand;
import main.paddletask.storage.api.StorageController;
import main.paddletask.task.api.TaskController;
import main.paddletask.task.entity.DeadlineTask;
import main.paddletask.task.entity.FloatingTask;
import main.paddletask.task.entity.Task;
import main.paddletask.task.entity.TimedTask;
import main.paddletask.parser.api.CommandParser;
import main.paddletask.common.util.DateTimeHelper;

public class TestEditTaskCommand {
    /*
     * Test Assumption: 1. StorageController is working correctly 2.
     * TaskController is working correctly 3. CommandParser is working correctly
     */

    /*** Variables ***/
    protected static ArrayList<Task> testTaskList;
    protected static ArrayList<Task> testStateList;
    protected static StorageController storageControllerInstance;
    protected static TaskController taskControllerInstance;
    protected static CommandParser commandParserInstance;
    protected static int[] stateTaskId;

    protected static String dummy_userCommand;
    protected static EditTaskCommand dummy_editTaskCommand;
    protected static FloatingTask dummy_floatingTask;
    protected static DeadlineTask dummy_deadlineTask;
    protected static TimedTask dummy_timedTask;
    protected static Task dummy_genericTask;

    protected static final String WHITE_SPACE = " ";
    protected static final String TEST_COMMAND = "edit %1s%2s%3s%4s%5s"; // id,
                                                                         // description,
                                                                         // end,
                                                                         // start,
                                                                         // reminder

    protected static final int TEST_SAMPLE_FLOAT_ID = 1;
    protected static final int TEST_SAMPLE_DEADLINE_ID = 2;
    protected static final int TEST_SAMPLE_TIMED_ID = 3;

    protected static final String TEST_SAMPLE_DESCRIPTION = "Lorem ipsum dolor";
    protected static final String TEST_SAMPLE_END_DATE = "25/12/2015";
    protected static final String TEST_SAMPLE_END_TIME = "12:30";
    protected static final String TEST_SAMPLE_START_DATE = "24/12/2015";
    protected static final String TEST_SAMPLE_START_TIME = "12:00";
    protected static final String TEST_SAMPLE_REMINDER_DATE_TIME = "20/12/2015 20:00";
    protected static final String TEST_KEYWORD_DESC = " desc ";
    protected static final String TEST_KEYWORD_END = " end ";
    protected static final String TEST_KEYWORD_START = " start ";
    protected static final String TEST_KEYWORD_REMIND = " remind ";
    protected static final String TEST_KEYWORD_EMPTY = "";

    protected static final Task.TASK_TYPE TEST_TASKTYPE_FLOATING = Task.TASK_TYPE.FLOATING;
    protected static final Task.TASK_TYPE TEST_TASKTYPE_DEADLINE = Task.TASK_TYPE.DEADLINE;
    protected static final Task.TASK_TYPE TEST_TASKTYPE_TIMED = Task.TASK_TYPE.TIMED;

    protected static final String TEST_EDIT_FLOATING_DESCRIPTION = "Test editing Floating Task description";
    protected static final String TEST_ADD_END_DATE_TO_FLOATING = "Test adding only an End date to a Floating Task";
    protected static final String TEST_ADD_END_DATE_AND_REMINDER_TO_FLOATING = "Test adding only an End date and a custom Reminder to a Floating Task";
    protected static final String TEST_ADD_END_TIME_TO_FLOATING = "Test adding only an End time to a Floating Task";
    protected static final String TEST_ADD_END_TIME_AND_REMINDER_TO_FLOATING = "Testing adding only an End time and a custom Reminder to a Floating Task";
    protected static final String TEST_ADD_END_DATE_TIME_TO_FLOATING = "Test adding both an End time and date to a Floating Task";
    protected static final String TEST_ADD_END_DATE_TIME_AND_REMINDER_TO_FLOATING = "Testing adding both an End time,date and a Reminder to a Floating task";

    protected static final String TEST_EDIT_DEADLINE_DESCRIPTION = "Test editing Deadline Task description";
    protected static final String TEST_EDIT_DEADLINE_END_DATE = "Test editing a Deadline Task's End date only";
    protected static final String TEST_EDIT_DEADLINE_END_DATE_AND_REMINDER = "Test editing a Deadline Task's End date and Reminder";
    protected static final String TEST_EDIT_DEADLINE_END_TIME = "Test editing a Deadline Task's End time only";
    protected static final String TEST_EDIT_DEADLINE_END_TIME_AND_REMINDER = "Test editing a Deadline Task's End time and Reminder";
    protected static final String TEST_EDIT_DEADLINE_END_DATE_TIME = "Test editing a Deadline Task's End date and time";
    protected static final String TEST_EDIT_DEADLINE_END_DATE_TIME_AND_REMINDER = "Test editing a Deadline Task's End date,time and Reminder";
    protected static final String TEST_ADD_START_DATE_TO_DEADLINE = "Test adding a Start date only to a Deadline Task";
    protected static final String TEST_ADD_START_TIME_TO_DEADLINE = "Test adding a Start time only to a Deadline Task";
    protected static final String TEST_ADD_START_DATE_TIME_TO_DEADLINE = "Test adding a Start date and time to a Deadline Task";

    protected static final String TEST_EDIT_TIMED_DESCRIPTION = "Test editing a Timed Task description";
    protected static final String TEST_EDIT_TIMED_END_DATE = "Test editing a Timed Task's End date only";
    protected static final String TEST_EDIT_TIMED_END_DATE_AND_REMINDER = "Test editing a Timed Task's End date and Reminder";
    protected static final String TEST_EDIT_TIMED_END_TIME = "Test editing a Timed Task's End time only";
    protected static final String TEST_EDIT_TIMED_END_TIME_AND_REMINDER = "Test editing a Timed Task's End time and Reminder";
    protected static final String TEST_EDIT_TIMED_END_DATE_TIME = "Test editing a Timed Task's End date and time";
    protected static final String TEST_EDIT_TIMED_START_DATE = "Test editing a Timed Task's Start date only";
    protected static final String TEST_EDIT_TIMED_START_TIME = "Test editing a Timed Task's Start time only";
    protected static final String TEST_EDIT_TIMED_START_DATE_TIME = "Test editing a Timed Task's Start date and time";

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
        task = new FloatingTask(1, "Get a good luck charm", DateTimeHelper.now(), false, 3);
        testTaskList.add(task);
        task = new DeadlineTask(2, "CS2103 CE2", DateTimeHelper.now(),
                LocalDateTime.parse("2015-01-02 23:59", formatter), DateTimeHelper.now(), false, 3, false,
                Task.RECUR_TYPE.NULL);
        testTaskList.add(task);
        task = new TimedTask(3, "Appointment with dentist", DateTimeHelper.now(),
                LocalDateTime.parse("2015-01-03 23:59", formatter), LocalDateTime.parse("2015-09-10 15:30", formatter),
                DateTimeHelper.now(), false, 3, false, Task.RECUR_TYPE.NULL);
        testTaskList.add(task);
        Document doc = storageControllerInstance.parseTask(testTaskList);
        storageControllerInstance.writeXml(doc);
        initTaskState();
        return testTaskList;
    }

    public void initTaskState() throws Exception {
        String viewInputCommand = "view";
        ViewTaskCommand viewCommand = (ViewTaskCommand) commandParserInstance.parse(viewInputCommand);
        testStateList = viewCommand.execute();
    }

    public int[] initStateTaskId() {
        int numIds = testStateList.size();
        stateTaskId = new int[numIds];
        for (int i = 0; i < numIds; i++) {
            stateTaskId[i] = testStateList.get(i).getTaskId();
        }
        return stateTaskId;
    }

    public void prepareDummyData() throws Exception {
        testTaskList = repopulateTask();
        Task.setTaskList(testTaskList);
    }

    public Task getTestTask(Task.TASK_TYPE type) {
        if (type.equals(Task.TASK_TYPE.FLOATING)) {
            return dummy_floatingTask;
        } else if (type.equals(Task.TASK_TYPE.DEADLINE)) {
            return dummy_deadlineTask;
        } else {
            return dummy_timedTask;
        }
    }
    /*** Test Cases ***/

    /**
     * Returns a Command String specified by the input parameters that will
     * represent a valid user input to PaddleTask
     * 
     * @param runningIndex
     *            Index of the Task to edit
     * @param desc
     *            New description of the edited Task
     * @param end_date
     *            New end date of the edited Task
     * @param end_time
     *            New end time of the edited Task
     * @param start_date
     *            New start date of the edited Task
     * @param start_time
     *            New start time of the edited Task
     * @param remind
     *            New reminder of the edited Task (both date and time)
     * @return The Command String that will be used as dummy user input
     */
    public String getTestCommand(int runningIndex, String desc, String end_date, String end_time, String start_date,
            String start_time, String remind) {
        String start = TEST_KEYWORD_EMPTY;
        String end = TEST_KEYWORD_EMPTY;

        if (end_date != TEST_KEYWORD_EMPTY || end_time != TEST_KEYWORD_EMPTY) {
            end += TEST_KEYWORD_END;
        }
        if (start_date != TEST_KEYWORD_EMPTY || start_time != TEST_KEYWORD_EMPTY) {
            start += TEST_KEYWORD_START;
        }

        if (desc != TEST_KEYWORD_EMPTY) {
            desc = TEST_KEYWORD_DESC + desc;
        }
        if (end_date != TEST_KEYWORD_EMPTY) {
            end += end_date;
        }
        if (end_time != TEST_KEYWORD_EMPTY) {
            end += end_time;
        }
        if (start_date != TEST_KEYWORD_EMPTY) {
            start += start_date;
        }
        if (start_time != TEST_KEYWORD_EMPTY) {
            start += start_time;
        }
        if (remind != TEST_KEYWORD_EMPTY) {
            remind = TEST_KEYWORD_REMIND + remind;
        }
        return String.format(TEST_COMMAND, runningIndex, desc, end, start, remind);
    }

    // Test errthing
    @Test
    public void test() throws Exception {
        // Test Floating
        testEditTaskCommand(TEST_TASKTYPE_FLOATING, TEST_EDIT_FLOATING_DESCRIPTION, TEST_SAMPLE_FLOAT_ID,
                getTestCommand(TEST_SAMPLE_FLOAT_ID, // Running index of sample
                                                     // Floating Task
                        TEST_SAMPLE_DESCRIPTION, // Sample description for
                                                 // edited Task
                        TEST_KEYWORD_EMPTY, // End date
                        TEST_KEYWORD_EMPTY, // End time
                        TEST_KEYWORD_EMPTY, // Start date
                        TEST_KEYWORD_EMPTY, // Start time
                        TEST_KEYWORD_EMPTY), // Reminder date and time
                TEST_SAMPLE_DESCRIPTION, null, null, null);

        testEditTaskCommand(TEST_TASKTYPE_DEADLINE, TEST_ADD_END_DATE_TO_FLOATING, TEST_SAMPLE_FLOAT_ID,
                getTestCommand(TEST_SAMPLE_FLOAT_ID, // Running index of sample
                                                     // Floating Task
                        TEST_SAMPLE_DESCRIPTION, // Sample description for
                                                 // edited Task
                        TEST_SAMPLE_END_DATE, // End date
                        TEST_KEYWORD_EMPTY, // End time
                        TEST_KEYWORD_EMPTY, // Start date
                        TEST_KEYWORD_EMPTY, // Start time
                        TEST_KEYWORD_EMPTY), // Reminder date and time
                TEST_SAMPLE_DESCRIPTION, getExpectedEnd(TEST_SAMPLE_END_DATE, null), null, null);

        testEditTaskCommand(TEST_TASKTYPE_DEADLINE, TEST_ADD_END_DATE_AND_REMINDER_TO_FLOATING, TEST_SAMPLE_FLOAT_ID,
                getTestCommand(TEST_SAMPLE_FLOAT_ID, // Running index of sample
                                                     // Floating Task
                        TEST_SAMPLE_DESCRIPTION, // Sample description for
                                                 // edited Task
                        TEST_SAMPLE_END_DATE, // End date
                        TEST_KEYWORD_EMPTY, // End time
                        TEST_KEYWORD_EMPTY, // Start date
                        TEST_KEYWORD_EMPTY, // Start time
                        TEST_KEYWORD_EMPTY), // Reminder date and time
                TEST_SAMPLE_DESCRIPTION, getExpectedEnd(TEST_SAMPLE_END_DATE, null), null, null);

        testEditTaskCommand(TEST_TASKTYPE_DEADLINE, TEST_ADD_END_DATE_TO_FLOATING, TEST_SAMPLE_FLOAT_ID,
                getTestCommand(TEST_SAMPLE_FLOAT_ID, // Running index of sample
                                                     // Floating Task
                        TEST_SAMPLE_DESCRIPTION, // Sample description for
                                                 // edited Task
                        TEST_SAMPLE_END_DATE, // End date
                        TEST_KEYWORD_EMPTY, // End time
                        TEST_KEYWORD_EMPTY, // Start date
                        TEST_KEYWORD_EMPTY, // Start time
                        TEST_KEYWORD_EMPTY), // Reminder date and time
                TEST_SAMPLE_DESCRIPTION, getExpectedEnd(TEST_SAMPLE_END_DATE, null), null, null);
                // Test Deadline

        // Test Timed
    }

    /**
     * Executes the command specified by the input userCommand and perform
     * various assert checks to verify expected results (input parameters)
     * against actual results
     * 
     * @param expectedTaskType
     *            Expected Task type (FLOATING,DEADLINE,TIMED)
     * @param testDescription
     *            Description of this test case
     * @param taskId
     *            TaskID of the Task being edited (For retrieving and performing
     *            assert checks)
     * @param userCommand
     *            Sample userinput that will be treated as valid user command
     *            String
     * @param expectedTaskDescription
     *            Expected Task description
     * @param expectedStart
     *            Expected Start (date/time)
     * @param expectedEnd
     *            Expected End (date/time)
     * @param expectedReminder
     *            Expected Reminder (date/time)
     * @throws Exception
     */
    public void testEditTaskCommand(Task.TASK_TYPE expectedTaskType, String testDescription, int taskId,
            String userCommand, String expectedTaskDescription, LocalDateTime expectedEnd, LocalDateTime expectedStart,
            LocalDateTime expectedReminder) throws Exception {
        prepareDummyData();

        // Execute the test command
        dummy_editTaskCommand = (EditTaskCommand) commandParserInstance.parse(userCommand, initStateTaskId());
        dummy_editTaskCommand.execute();

        // Get the task of the modified task and retrieve the TASK_TYPE to check
        // what kind of check should be done
        dummy_genericTask = TaskController.getInstance().getTask(taskId);
        Task.TASK_TYPE type = dummy_genericTask.getType();

        switch (type) {
        case FLOATING:
            dummy_floatingTask = (FloatingTask) dummy_genericTask;
            if (expectedTaskType != null) {
                testTaskType(testDescription, expectedTaskType, type);
            }
            if (expectedTaskDescription != null) {
                testTaskDescription(testDescription, expectedTaskDescription, dummy_floatingTask.getDescription());
            }
            break;
        case DEADLINE:
            dummy_deadlineTask = (DeadlineTask) dummy_genericTask;
            if (expectedTaskType != null) {
                testTaskType(testDescription, expectedTaskType, type);
            }
            if (expectedTaskDescription != null) {
                testTaskDescription(testDescription, expectedTaskDescription, dummy_deadlineTask.getDescription());
            }
            if (expectedEnd != null) {
                testTaskEnd(testDescription, expectedEnd, dummy_deadlineTask.getEnd());
            }
            if (expectedReminder != null) {
                testTaskReminder(testDescription, expectedReminder, dummy_deadlineTask.getReminder());
            }
            break;
        case TIMED:
            dummy_timedTask = (TimedTask) dummy_genericTask;
            if (expectedTaskType != null) {
                testTaskType(testDescription, expectedTaskType, type);
            }
            if (expectedTaskDescription != null) {
                testTaskDescription(testDescription, expectedTaskDescription, dummy_timedTask.getDescription());
            }
            if (expectedStart != null) {
                testTaskStart(testDescription, expectedStart, dummy_timedTask.getStart());
            }
            if (expectedEnd != null) {
                testTaskEnd(testDescription, expectedEnd, dummy_timedTask.getEnd());
            }
            if (expectedReminder != null) {
                testTaskReminder(testDescription, expectedReminder, dummy_timedTask.getReminder());
            }
            break;
        default:
            throw new Exception("Task has invalid type!");
        }
    }

    public LocalDateTime getExpectedEnd(String end_date, String end_time) {
        String end = TEST_KEYWORD_EMPTY;
        if (end_date != null) {
            end += end_date + WHITE_SPACE;
        }
        if (end_time != null) {
            end += end_time;
        }
        return DateTimeHelper.parseStringToDateTime(end);
    }

    public void testTaskType(String testDescription, Task.TASK_TYPE expectedTaskType, Task.TASK_TYPE actualTaskType) {
        assertEquals(testDescription, expectedTaskType, actualTaskType);
    }

    public void testTaskDescription(String testDescription, String expectedDescription, String actualDescription) {
        assertEquals(testDescription, expectedDescription, actualDescription);
    }

    public void testTaskStart(String testDescription, LocalDateTime expectedStart, LocalDateTime actualStart) {
        String expectedStartDate = DateTimeHelper.getDate(expectedStart);
        String expectedStartTime = DateTimeHelper.getTime(expectedStart);
        String actualStartDate = DateTimeHelper.getDate(actualStart);
        String actualStartTime = DateTimeHelper.getTime(actualStart);
        assertEquals(testDescription, expectedStartDate, actualStartDate);
        assertEquals(testDescription, expectedStartTime, actualStartTime);
    }

    public void testTaskEnd(String testDescription, LocalDateTime expectedEnd, LocalDateTime actualEnd) {
        String expectedEndDate = DateTimeHelper.getDate(expectedEnd);
        String expectedEndTime = DateTimeHelper.getTime(expectedEnd);
        String actualEndDate = DateTimeHelper.getDate(actualEnd);
        String actualEndTime = DateTimeHelper.getTime(actualEnd);
        assertEquals(testDescription, expectedEndDate, actualEndDate);
        assertEquals(testDescription, expectedEndTime, actualEndTime);
    }

    public void testTaskReminder(String testDescription, LocalDateTime expectedReminder, LocalDateTime actualReminder) {
        String expectedReminderDate = DateTimeHelper.getDate(expectedReminder);
        String expectedReminderTime = DateTimeHelper.getTime(expectedReminder);
        String actualReminderDate = DateTimeHelper.getDate(actualReminder);
        String actualReminderTime = DateTimeHelper.getTime(actualReminder);
        assertEquals(testDescription, expectedReminderDate, actualReminderDate);
        assertEquals(testDescription, expectedReminderTime, actualReminderTime);
    }

    /*
     * @Test public void editFloatingDescription() throws Exception {
     * prepareDummyData();
     * 
     * // Initialize dummy user command and description that will be used for
     * testing String testDescription = "Testing Floating Description Change!";
     * dummy_userCommand = "edit 1 desc " + testDescription; dummy_floatingTask
     * = (FloatingTask) taskControllerInstance.getTask(1);
     * 
     * // Verify that original task description is not the one that will be used
     * for testing assertNotEquals(
     * "test editFloatingDescription: initial description not equals the one used for testing"
     * , testDescription,dummy_floatingTask.getDescription());
     * 
     * // Execute edit command dummy_editTaskCommand =
     * (EditTaskCommand)commandParserInstance.parse(dummy_userCommand,
     * initStateTaskId()); dummy_editTaskCommand.execute();
     * 
     * // Verify if task description has been correctly set dummy_floatingTask =
     * (FloatingTask) taskControllerInstance.getTask(1); assertEquals(
     * "test editFloatingDescription: new description set", testDescription,
     * dummy_floatingTask.getDescription()); }
     * 
     * @Test public void addEndTimeToFloating() throws Exception {
     * prepareDummyData();
     * 
     * // Build my End with default date and custom time String defaultDate =
     * DateTimeHelper.getDate(DateTimeHelper.now()); String testTime = "11:11";
     * dummy_userCommand = "edit 1 end " + testTime;
     * 
     * // Verify that task to edit is still Floating if casting is successful
     * dummy_floatingTask = (FloatingTask) taskControllerInstance.getTask(1);
     * 
     * //Execute command dummy_editTaskCommand = (EditTaskCommand)
     * commandParserInstance.parse(dummy_userCommand,initStateTaskId());
     * dummy_editTaskCommand.execute();
     * 
     * // Verify become Deadline dummy_genericTask =
     * taskControllerInstance.getTask(1); assertEquals(
     * "test addEndTimeToFloating: verify new task is deadline",
     * dummy_genericTask.getType(), Task.TASK_TYPE.DEADLINE);
     * 
     * // Verify new End is the expected End dummy_deadlineTask = (DeadlineTask)
     * dummy_genericTask; LocalDateTime editedEnd = dummy_deadlineTask.getEnd();
     * 
     * // Verify new End Date is given default value of now() date String
     * editedEndDate = DateTimeHelper.getDate(editedEnd); assertEquals(
     * "test addEndTimeToFloating: verify edited task end date default to today's date"
     * , defaultDate,editedEndDate);
     * 
     * // Verify new End Time is given user specified value String editedEndTime
     * = DateTimeHelper.getTime(editedEnd); assertEquals(
     * "test addEndTimeToFloating: verify edited task end time", testTime,
     * editedEndTime); }
     * 
     * @Test public void addEndDateToFloating() throws Exception {
     * prepareDummyData();
     * 
     * // Build my End with custom date and default time String testDate =
     * DateTimeHelper.getDate(DateTimeHelper.now()); String defaultTime =
     * "00:00"; dummy_userCommand = "edit 1 end " + testDate;
     * 
     * // Verify that task to edit is still Floating if casting is successful
     * dummy_floatingTask = (FloatingTask)taskControllerInstance.getTask(1);
     * 
     * //Execute command dummy_editTaskCommand = (EditTaskCommand)
     * commandParserInstance.parse(dummy_userCommand,initStateTaskId());
     * dummy_editTaskCommand.execute();
     * 
     * // Verify become Deadline dummy_genericTask =
     * taskControllerInstance.getTask(1); assertEquals(
     * "test addEndDateToFloating: verify new task is deadline",
     * dummy_genericTask.getType(), Task.TASK_TYPE.DEADLINE);
     * 
     * // Verify new End Date is given user specified date dummy_deadlineTask =
     * (DeadlineTask) dummy_genericTask; String editedEndDate =
     * DateTimeHelper.getDate(dummy_deadlineTask.getEnd()); assertEquals(
     * "test addEndDateToFloating: verify edited task end date", testDate,
     * editedEndDate);
     * 
     * // Verify new End Time is given default time of 00:00 String
     * editedEndTime = DateTimeHelper.getTime(dummy_deadlineTask.getEnd());
     * assertEquals(
     * "test addEndDateToFloating: verify edited task end time default to 00:00"
     * , defaultTime, editedEndTime); }
     * 
     * @Test public void addEndDateAndTimeToFloating() throws Exception {
     * prepareDummyData();
     * 
     * // Build my End with custom date and time String testDateTime =
     * "01/01/2015 12:34"; dummy_userCommand = "edit 1 end " + testDateTime;
     * 
     * // Verify that task to edit is still Floating if casting is successful
     * dummy_floatingTask = (FloatingTask)taskControllerInstance.getTask(0);
     * 
     * //Execute command dummy_editTaskCommand = (EditTaskCommand)
     * commandParserInstance.parse(dummy_userCommand);
     * dummy_editTaskCommand.execute();
     * 
     * // Verify become Deadline dummy_genericTask =
     * taskControllerInstance.getTask(1); assertEquals(
     * "test addEndDateAndTimeToFloating: verify new task is deadline",
     * dummy_genericTask.getType(), Task.TASK_TYPE.DEADLINE);
     * 
     * // Verify new End is the expected End dummy_deadlineTask = (DeadlineTask)
     * dummy_genericTask; String editedEnd =
     * DateTimeHelper.parseDateTimeToString(dummy_deadlineTask.getEnd());
     * assertEquals(
     * "test addEndDateAndTimeToFloating: verify edited task end date and time",
     * testDateTime, editedEnd); }
     * 
     * @Test public void addStartAndEndToFloating() throws Exception {
     * prepareDummyData();
     * 
     * // Build my Start and End with custom date and time String testStart =
     * "01/01/2015 10:00"; String testEnd = "01/02/2015 23:59";
     * dummy_userCommand = "edit 1 start " + testStart + " end " + testEnd;
     * 
     * // Verify that task to edit is still Floating if casting is successful
     * dummy_floatingTask = (FloatingTask)taskControllerInstance.getTask(0);
     * 
     * //Execute command dummy_editTaskCommand = (EditTaskCommand)
     * commandParserInstance.parse(dummy_userCommand);
     * dummy_editTaskCommand.execute();
     * 
     * // Verify become Timed dummy_genericTask =
     * taskControllerInstance.getTask(1); assertEquals(
     * "test addStartAndEndToFloating: verify new task is Timed",
     * dummy_genericTask.getType(), Task.TASK_TYPE.TIMED);
     * 
     * // Verify new Start and End is the expected Start and End dummy_timedTask
     * = (TimedTask) dummy_genericTask; String editedStart =
     * DateTimeHelper.parseDateTimeToString(dummy_timedTask.getStart()); String
     * editedEnd =
     * DateTimeHelper.parseDateTimeToString(dummy_timedTask.getEnd());
     * assertEquals("test addStartAndEndToFloating: verify edited task start",
     * testStart, editedStart); assertEquals(
     * "test addStartAndEndToFloating: verify edited task end", testEnd,
     * editedEnd); }
     * 
     * 
     * @Test public void editDeadlineDescription() throws Exception {
     * prepareDummyData();
     * 
     * // Initialize dummy user command and description that will be used for
     * testing String testDescription = "Testing Deadline Description Change!";
     * dummy_userCommand = "edit 2 desc " + testDescription; dummy_deadlineTask
     * = (DeadlineTask) taskControllerInstance.getTask(2);
     * 
     * // Verify that original task description is not the one that will be used
     * for testing assertNotEquals(
     * "test editDeadlineDescription: initial description not equals the one used for testing"
     * , testDescription,dummy_deadlineTask.getDescription());
     * 
     * // Execute edit command dummy_editTaskCommand = (EditTaskCommand)
     * commandParserInstance.parse(dummy_userCommand);
     * dummy_editTaskCommand.execute();
     * 
     * // Verify if task description has been correctly set dummy_deadlineTask =
     * (DeadlineTask) taskControllerInstance.getTask(2); assertEquals(
     * "test editDeadlineDescription: new description set", testDescription,
     * dummy_deadlineTask.getDescription()); }
     * 
     * @Test public void editDeadlineEndTime() {
     * 
     * }
     * 
     * @Test public void editDeadlineEndDate() {
     * 
     * }
     * 
     * @Test public void editDeadlineEndDateTime() {
     * 
     * }
     * 
     * @Test public void editDeadlineDescriptionAndEnd() {
     * 
     * }
     * 
     * @Test public void addStartToDeadline() {
     * 
     * }
     * 
     * @Test public void addStartAndEndToDeadline() {
     * 
     * }
     * 
     * // Reminder related test case for DeadlineTask
     * 
     * @Test public void editTimedDescription() {
     * 
     * }
     * 
     * @Test public void editTimedStart() {
     * 
     * }
     * 
     * @Test public void editTimedEnd() {
     * 
     * }
     * 
     * @Test public void editTimedCompleteStatus() {
     * 
     * }
     * 
     * @Test public void editTimedDescriptionAndStart() {
     * 
     * }
     * 
     * @Test public void editTimedDescriptionAndStartAndEnd() {
     * 
     * }
     * 
     * @Test public void editTimedDescriptionAndStartAndEndAndCompleteStatus() {
     * 
     * }
     */
}