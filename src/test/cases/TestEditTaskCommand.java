//@@author A0133662J
package test.cases;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
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
     * Test Assumption: 
     * 1. StorageController is working correctly 
     * 2. TaskController is working correctly 
     * 3. CommandParser is working correctly
     */

    /*** Variables ***/
    protected static ArrayList<Task> _testTaskList;
    protected static ArrayList<Task> _testStateList;
    protected static StorageController _storageControllerInstance;
    protected static TaskController _taskControllerInstance;
    protected static CommandParser _commandParserInstance;
    protected static int[] _stateTaskId;

    protected static String _dummy_userCommand;
    protected static EditTaskCommand _dummy_editTaskCommand;
    protected static FloatingTask _dummy_floatingTask;
    protected static DeadlineTask _dummy_deadlineTask;
    protected static TimedTask _dummy_timedTask;
    protected static Task _dummy_genericTask;

    /*** Final Constants ***/
    protected static final String WHITE_SPACE = " ";
    
    // id,
    // description,
    // end,
    // start,
    // reminder
    // priority
    // recurType
    protected static final String TEST_COMMAND = "edit %1$s%2$s%3$s%4$s%5$s%6$s%7$s%8$s%9$s"; 
    protected static final String TEST_DATE_FORMAT = "%1$s %2$s";
    
    protected static final int TEST_SAMPLE_FLOAT_ID = 0;
    protected static final int TEST_SAMPLE_DEADLINE_ID = 1;
    protected static final int TEST_SAMPLE_TIMED_ID = 2;
    
    protected static final int TEST_SAMPLE_FLOAT_INDEX = 1;
    protected static final int TEST_SAMPLE_DEADLINE_INDEX = 2;
    protected static final int TEST_SAMPLE_TIMED_INDEX = 3;
    protected static final int TEST_SAMPLE_PRIORITY = 1;
    
    
    protected static final int TEST_TASK_DEFAULT_PRIORITY = 3;
    
    protected static final String TEST_SAMPLE_DESCRIPTION = "Lorem ipsum dolor";
    protected static final String TEST_SAMPLE_END_DATE = DateTimeHelper.getDate(DateTimeHelper.addDays(DateTimeHelper.now(), 2));
    protected static final String TEST_SAMPLE_TODAY_DATE = DateTimeHelper.getDate(DateTimeHelper.now());
    protected static final String TEST_SAMPLE_END_TIME = "23:59";
    protected static final String TEST_SAMPLE_START_DATE = DateTimeHelper.getDate(DateTimeHelper.addDays(DateTimeHelper.now(), -1));
    protected static final String TEST_SAMPLE_START_TIME = "10:00";
    protected static final String TEST_SAMPLE_REMINDER_DATE_TIME = DateTimeHelper.parseDateTimeToString(DateTimeHelper.addDays(DateTimeHelper.now(), 3));;
    
    protected static final String TEST_KEYWORD_DESC = " desc ";
    protected static final String TEST_KEYWORD_END_DATE = " edate ";
    protected static final String TEST_KEYWORD_END_TIME = " etime ";
    protected static final String TEST_KEYWORD_START_DATE = " sdate ";
    protected static final String TEST_KEYWORD_START_TIME = " stime ";
    protected static final String TEST_KEYWORD_REMIND = " remind ";
    protected static final String TEST_KEYWORD_PRIORITY = " priority ";
    protected static final String TEST_KEYWORD_RECURDAY = " repeat daily ";
    protected static final String TEST_KEYWORD_RECURWEEK = " repeat weekly ";
    protected static final String TEST_KEYWORD_RECURMONTH = " repeat monthly ";
    protected static final String TEST_KEYWORD_RECURYEAR = " repeat yearly ";
    protected static final String TEST_KEYWORD_EMPTY = "";
    
    protected static final String DEADLINE_CREATED_SAMPLE_END_DATE = DateTimeHelper.getDate(DateTimeHelper.addDays(DateTimeHelper.now(), 1));
    protected static final String DEADLINE_CREATED_SAMPLE_END_TIME = "08:00";
    protected static final String TIMED_CREATED_SAMPLE_START_DATE = DateTimeHelper.getDate(DateTimeHelper.addDays(DateTimeHelper.now(), 1));
    protected static final String TIMED_CREATED_SAMPLE_START_TIME = "12:00";
    protected static final String TIMED_CREATED_SAMPLE_END_DATE = DateTimeHelper.getDate(DateTimeHelper.addDays(DateTimeHelper.now(), 1));
    protected static final String TIMED_CREATED_SAMPLE_END_TIME = "16:00";
    
    protected static final Task.TASK_TYPE TEST_TASKTYPE_FLOATING = Task.TASK_TYPE.FLOATING;
    protected static final Task.TASK_TYPE TEST_TASKTYPE_DEADLINE = Task.TASK_TYPE.DEADLINE;
    protected static final Task.TASK_TYPE TEST_TASKTYPE_TIMED = Task.TASK_TYPE.TIMED;

    protected static final Task.RECUR_TYPE TEST_RECURTYPE_NULL = Task.RECUR_TYPE.NULL;
    protected static final Task.RECUR_TYPE TEST_RECURTYPE_DAY = Task.RECUR_TYPE.DAY;
    protected static final Task.RECUR_TYPE TEST_RECURTYPE_WEEK = Task.RECUR_TYPE.WEEK;
    protected static final Task.RECUR_TYPE TEST_RECURTYPE_MONTH = Task.RECUR_TYPE.MONTH;
    protected static final Task.RECUR_TYPE TEST_RECURTYPE_YEAR = Task.RECUR_TYPE.YEAR;
    
    /*** FloatingTask Test Case Descriptions ***/
    protected static final String TEST_FLOATING_EDIT_DESCRIPTION = "Test editing Floating Task description";
//    protected static final String TEST_FLOATING_ADD_END_DATE = "Test adding only an End date to a Floating Task";
//    protected static final String TEST_FLOATING_ADD_END_DATE_AND_REMINDER = "Test adding only an End date and a custom Reminder to a Floating Task";
    protected static final String TEST_FLOATING_ADD_END_TIME = "Test adding only an End time to a Floating Task";
    protected static final String TEST_FLOATING_ADD_END_TIME_AND_REMINDER = "Testing adding only an End time and a custom Reminder to a Floating Task";
    protected static final String TEST_FLOATING_ADD_END_DATE_TIME = "Test adding both an End time and date to a Floating Task";
    protected static final String TEST_FLOATING_ADD_END_DATE_TIME_AND_REMINDER = "Testing adding both an End time,date and a Reminder to a Floating task";
//    protected static final String TEST_FLOATING_ADD_START_DATE_AND_END_DATE_TIME = "Test adding only a Start date and both an End date/time";
    protected static final String TEST_FLOATING_ADD_START_DATE_AND_END_TIME = "Test adding a Start date only and an End time only to a Floating Task";
    protected static final String TEST_FLOATING_ADD_START_TIME_AND_END_TIME = "Test adding a Start time only and an End time only to a Floating Task";
    protected static final String TEST_FLOATING_ADD_START_DATE_TIME_AND_END_TIME = "Test adding both Start date/time only and an End time only to a Floating Task";
    protected static final String TEST_FLOATING_ADD_START_DATE_AND_END_DATE = "Test adding a Start date only and an End date only to a Floating Task";
    protected static final String TEST_FLOATING_ADD_START_TIME_AND_END_DATE = "Test adding a Start time only and an End date only to a Floating Task";
    protected static final String TEST_FLOATING_ADD_START_DATE_TIME_AND_END_DATE = "Test adding both a Start date/time and an End date only";
    protected static final String TEST_FLOATING_ADD_START_TIME_AND_END_DATE_TIME = "Test adding only a Start time and both an End date/time";
    protected static final String TEST_FLOATING_ADD_START_DATE_AND_END_DATE_TIME = "Testing add only a Start date and both an End date/time";
    protected static final String TEST_FLOATING_ADD_START_DATE_TIME_AND_END_DATE_TIME = "Testing add both a Start date/time and both an End date/time";
    protected static final String TEST_FLOATING_EDIT_PRIORITY = "Test editing the priority level of a Floating Task";
    
    /*** DeadlineTask Test Case Descriptions ***/
    protected static final String TEST_DEADLINE_EDIT_DESCRIPTION = "Test editing Deadline Task description";
    protected static final String TEST_DEADLINE_EDIT_END_DATE = "Test editing a Deadline Task's End date only";
    protected static final String TEST_DEADLINE_EDIT_END_DATE_AND_REMINDER = "Test editing a Deadline Task's End date and Reminder";
    protected static final String TEST_DEADLINE_EDIT_END_TIME = "Test editing a Deadline Task's End time only";
    protected static final String TEST_DEADLINE_EDIT_END_TIME_AND_REMINDER = "Test editing a Deadline Task's End time and Reminder";
    protected static final String TEST_DEADLINE_EDIT_END_DATE_TIME = "Test editing a Deadline Task's End date and time";
    protected static final String TEST_DEADLINE_EDIT_END_DATE_TIME_AND_REMINDER = "Test editing a Deadline Task's End date,time and Reminder";
    protected static final String TEST_DEADLINE_ADD_START_DATE = "Test adding a Start date only to a Deadline Task";
    protected static final String TEST_DEADLINE_ADD_START_TIME = "Test adding a Start time only to a Deadline Task";
    protected static final String TEST_DEADLINE_ADD_START_DATE_TIME = "Test adding both a Start date/time to a Deadline Task";
    protected static final String TEST_DEADLINE_EDIT_PRIORITY = "Test editing the priority level of a Deadline Task";
    protected static final String TEST_DEADLINE_EDIT_RECURTYPE_DAY = "Testing editing the recur type of a Deadline Task to 'every day'";
    protected static final String TEST_DEADLINE_EDIT_RECURTYPE_WEEK = "Testing editing the recur type of a Deadline Task to 'every week'";;
    protected static final String TEST_DEADLINE_EDIT_RECURTYPE_MONTH = "Testing editing the recur type of a Deadline Task to 'every month'";;
    protected static final String TEST_DEADLINE_EDIT_RECURTYPE_YEAR = "Testing editing the recur type of a Deadline Task to 'every year'";;
    
    /*** TimedTask Test Case Descriptions ***/
    protected static final String TEST_TIMED_EDIT_DESCRIPTION = "Test editing a Timed Task description";
    protected static final String TEST_TIMED_EDIT_END_DATE = "Test editing a Timed Task's End date only";
    protected static final String TEST_TIMED_EDIT_END_DATE_AND_REMINDER = "Test editing a Timed Task's End date and Reminder";
    protected static final String TEST_TIMED_EDIT_END_TIME = "Test editing a Timed Task's End time only";
    protected static final String TEST_TIMED_EDIT_END_TIME_AND_REMINDER = "Test editing a Timed Task's End time and Reminder";
    protected static final String TEST_TIMED_EDIT_END_DATE_TIME = "Test editing a Timed Task's End date and time";
    protected static final String TEST_TIMED_EDIT_END_DATE_TIME_AND_REMINDER = "Test editing a Timed Task's both End date/time and a new Reminder";
    protected static final String TEST_TIMED_EDIT_START_DATE = "Test editing a Timed Task's Start date only";
    protected static final String TEST_TIMED_EDIT_START_DATE_AND_REMINDER = "Testing editing a Timed Task's Start date and Reminder";
    protected static final String TEST_TIMED_EDIT_START_TIME = "Test editing a Timed Task's Start time only";
    protected static final String TEST_TIMED_EDIT_START_TIME_AND_REMINDER = "Testing editing a Timed Task's Start time and Reminder";
    protected static final String TEST_TIMED_EDIT_START_DATE_TIME = "Test editing a Timed Task's Start date and time";
    protected static final String TEST_TIMED_EDIT_START_DATE_TIME_REMINDER = "Test editing a Timed Task's both Start date/time and a new Reminder";
    protected static final String TEST_TIMED_EDIT_START_TIME_END_DATE = "Test editing a Timed Task's Start time and End date only";
    protected static final String TEST_TIMED_EDIT_START_DATE_END_TIME = "Test editing a Timed Task's Start date and End time only";
    protected static final String TEST_TIMED_EDIT_START_DATE_TIME_END_DATE_TIME = "Testing editing a Timed Task's both Start date/time and End date/time";
    protected static final String TEST_TIMED_EDIT_RECURTYPE_DAY = "Testing editing the recur type of a Timed Task to 'every day'";
    protected static final String TEST_TIMED_EDIT_RECURTYPE_WEEK = "Testing editing the recur type of a Timed Task to 'every week'";;
    protected static final String TEST_TIMED_EDIT_RECURTYPE_MONTH = "Testing editing the recur type of a Timed Task to 'every month'";;
    protected static final String TEST_TIMED_EDIT_RECURTYPE_YEAR = "Testing editing the recur type of a Timed Task to 'every year'";;
    
    /*** Setup ***/
    @Before
    public void setUp() throws Exception {
        _storageControllerInstance = StorageController.getInstance();
        _testTaskList = repopulateTask();
        _taskControllerInstance = TaskController.getInstance();
        _commandParserInstance = new CommandParser();
    }

    public ArrayList<Task> repopulateTask() throws Exception {
        ArrayList<Task> testTaskList = new ArrayList<Task>();
        _commandParserInstance = new CommandParser();
        
        // Populate sample arraylist
        Task task;
        LocalDateTime sampleDeadlineEnd = DateTimeHelper.parseStringToDateTime(String.format(TEST_DATE_FORMAT, DEADLINE_CREATED_SAMPLE_END_DATE, DEADLINE_CREATED_SAMPLE_END_TIME));
        LocalDateTime sampleTimedStart = DateTimeHelper.parseStringToDateTime(String.format(TEST_DATE_FORMAT , TIMED_CREATED_SAMPLE_START_DATE, TIMED_CREATED_SAMPLE_START_TIME));
        LocalDateTime sampleTimedEnd = DateTimeHelper.parseStringToDateTime(String.format(TEST_DATE_FORMAT, TIMED_CREATED_SAMPLE_END_DATE, TIMED_CREATED_SAMPLE_END_TIME));
        LocalDateTime sampleReminder = DateTimeHelper.parseStringToDateTime(TEST_SAMPLE_REMINDER_DATE_TIME);
        
        // Float Constructor: taskId | description | createdAt | isComplete | priority | 
        // Deadline Constructor: taskId | description | createdAt | end | reminder | isComplete | priority | isRecurring | recurType
        // Timed Constructor: taskId | description | createdAt | start | end | reminder | isComplete | priority | isRecurring | recurType
        
        // Sample Floating Task
        task = new FloatingTask(TEST_SAMPLE_FLOAT_ID, "Test Floating Task", DateTimeHelper.now(), false, 3);
        testTaskList.add(task);
        
        // Sample Deadline Task
        task = new DeadlineTask(TEST_SAMPLE_DEADLINE_ID, "Test Deadline Task", DateTimeHelper.now(), sampleDeadlineEnd, sampleReminder, false, 3, false, TEST_RECURTYPE_NULL);
        testTaskList.add(task);
        
        // Sample Timed Task
        task = new TimedTask(TEST_SAMPLE_TIMED_ID, "Test TimedTask", DateTimeHelper.now(), sampleTimedStart, sampleTimedEnd, sampleReminder, false, 3, false, TEST_RECURTYPE_NULL);
        testTaskList.add(task);
        
        // Save sample tasks into XML
        Document doc = _storageControllerInstance.parseTask(testTaskList);
        _storageControllerInstance.writeXml(doc);
        initTaskState();
        return testTaskList;
    }

    public void initTaskState() throws Exception {
        String viewInputCommand = "view";
        ViewTaskCommand viewCommand = (ViewTaskCommand) _commandParserInstance.parse(viewInputCommand);
        _testStateList = viewCommand.execute();
    }

    public int[] initStateTaskId() {
        int numIds = _testStateList.size();
        _stateTaskId = new int[numIds];
        for (int i = 0; i < numIds; i++) {
            _stateTaskId[i] = _testStateList.get(i).getTaskId();
        }
        return _stateTaskId;
    }

    public void prepareDummyData() throws Exception {
        _testTaskList = repopulateTask();
        Task.setTaskList(_testTaskList);
    }

    public Task getTestTask(Task.TASK_TYPE type) {
        if (type.equals(Task.TASK_TYPE.FLOATING)) {
            return _dummy_floatingTask;
        } else if (type.equals(Task.TASK_TYPE.DEADLINE)) {
            return _dummy_deadlineTask;
        } else {
            return _dummy_timedTask;
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
     * @param endDate
     *            New end date of the edited Task
     * @param endTime
     *            New end time of the edited Task
     * @param startDate
     *            New start date of the edited Task
     * @param startTime
     *            New start time of the edited Task
     * @param remind
     *            New reminder of the edited Task (both date and time)
     * @param priority
     *            New priority of the edited Task 
     * @param recurType
     *            New RECUR period type of the edit Task (NULL, DAILY, WEEKLY, MONTHLY, YEARLY)
     
     * @return The Command String that will be used as dummy user input
     */
    public String getTestCommand(int runningIndex, String desc, String endDate, String endTime, 
                                 String startDate, String startTime, String remind, int priority, Task.RECUR_TYPE recurType) {
        String formattedCommand = TEST_KEYWORD_EMPTY;
        String editPriority = TEST_KEYWORD_EMPTY;
        String editRecur = TEST_KEYWORD_EMPTY;
                
        if (remind != TEST_KEYWORD_EMPTY) {
            remind = TEST_KEYWORD_REMIND + remind;
        }
        
        if (endDate != TEST_KEYWORD_EMPTY) {
            endDate = TEST_KEYWORD_END_DATE + endDate;
        }
        
        if (endTime != TEST_KEYWORD_EMPTY) {
            endTime = TEST_KEYWORD_END_TIME + endTime;
        }
        
        if (startDate != TEST_KEYWORD_EMPTY) {
            startDate = TEST_KEYWORD_START_DATE + startDate;
        }
        
        if (startTime != TEST_KEYWORD_EMPTY) {
            startTime = TEST_KEYWORD_START_TIME + startTime;
        }

        if (desc != TEST_KEYWORD_EMPTY) {
            desc = TEST_KEYWORD_DESC + desc;
        }
        
        if (priority != -1) {
            editPriority = TEST_KEYWORD_PRIORITY + priority;
        }
        
        if (recurType.equals(TEST_RECURTYPE_DAY)) {
            editRecur = TEST_KEYWORD_RECURDAY;
        } else if (recurType.equals(TEST_RECURTYPE_WEEK)) {
            editRecur = TEST_KEYWORD_RECURWEEK;
        } else if (recurType.equals(TEST_RECURTYPE_MONTH)) {
            editRecur = TEST_KEYWORD_RECURMONTH;
        } else if (recurType.equals(TEST_RECURTYPE_YEAR)) {
            editRecur = TEST_KEYWORD_RECURYEAR;
        }
        
        formattedCommand = String.format(TEST_COMMAND, runningIndex, desc, endDate, endTime, 
                                         startDate, startTime, remind, editPriority, editRecur);
        
        return formattedCommand;
    }

    @Test
    public void test() throws Exception {
        // testEditTaskCommand parameters: 
        // 1. expected task type
        // 2. test case description
        // 3. task id
        // 4. test command
        // 5. expected task description
        // 6. expected end
        // 7. expected start
        // 8. expected reminder
        // 9. expected priority
        // 10. expected recur status
        // 11. expected recur type
        
        /*** FloatingTask Test Cases ***/
        testEditTaskCommand(TEST_TASKTYPE_FLOATING, TEST_FLOATING_EDIT_DESCRIPTION, TEST_SAMPLE_FLOAT_ID,
                            getTestCommand(
                                TEST_SAMPLE_FLOAT_INDEX,    // Running index of sample Floating Task
                                TEST_SAMPLE_DESCRIPTION, // Sample description for edited Task
                                TEST_KEYWORD_EMPTY,      // End date
                                TEST_KEYWORD_EMPTY,      // End time
                                TEST_KEYWORD_EMPTY,      // Start date
                                TEST_KEYWORD_EMPTY,      // Start time
                                TEST_KEYWORD_EMPTY,      // Reminder date and time
                                -1,                      // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),    // Recur type
                            TEST_SAMPLE_DESCRIPTION, null, null, null, TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_DEADLINE, TEST_FLOATING_ADD_END_DATE_TIME, TEST_SAMPLE_FLOAT_ID,
                            getTestCommand(
                                TEST_SAMPLE_FLOAT_INDEX,             // Running index of sample Floating Task
                                TEST_SAMPLE_DESCRIPTION,          // Sample description for edited Task
                                TEST_SAMPLE_END_DATE,             // End date
                                TEST_SAMPLE_END_TIME,             // End time
                                TEST_KEYWORD_EMPTY,               // Start date
                                TEST_KEYWORD_EMPTY,               // Start time
                                TEST_KEYWORD_EMPTY,               // Reminder date and time
                                -1,                               // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),             // Recur type
                            TEST_SAMPLE_DESCRIPTION, getExpectedEnd(TEST_SAMPLE_END_DATE, TEST_SAMPLE_END_TIME), 
                            null, null, TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        
        testEditTaskCommand(TEST_TASKTYPE_DEADLINE, TEST_FLOATING_ADD_END_DATE_TIME_AND_REMINDER, TEST_SAMPLE_FLOAT_ID,
                            getTestCommand(
                                TEST_SAMPLE_FLOAT_INDEX,            // Running index of sample Floating Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_SAMPLE_END_DATE,            // End date
                                TEST_SAMPLE_END_TIME,            // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_SAMPLE_REMINDER_DATE_TIME,  // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TEST_SAMPLE_END_DATE, TEST_SAMPLE_END_TIME), null, 
                            getExpectedReminder(TEST_SAMPLE_REMINDER_DATE_TIME), 
                            TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_DEADLINE, TEST_FLOATING_ADD_END_TIME, TEST_SAMPLE_FLOAT_ID,
                            getTestCommand(
                                TEST_SAMPLE_FLOAT_INDEX,            // Running index of sample Floating Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_KEYWORD_EMPTY,              // End date
                                TEST_SAMPLE_END_TIME,            // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, getExpectedEnd(TEST_SAMPLE_TODAY_DATE, TEST_SAMPLE_END_TIME), 
                            null, null, TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_DEADLINE, TEST_FLOATING_ADD_END_TIME_AND_REMINDER, TEST_SAMPLE_FLOAT_ID,
                            getTestCommand(
                                TEST_SAMPLE_FLOAT_INDEX,            // Running index of sample Floating Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_KEYWORD_EMPTY,              // End date
                                TEST_SAMPLE_END_TIME,            // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_SAMPLE_REMINDER_DATE_TIME,  // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TEST_SAMPLE_TODAY_DATE, TEST_SAMPLE_END_TIME), null, 
                            getExpectedReminder(TEST_SAMPLE_REMINDER_DATE_TIME), 
                            TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_FLOATING_ADD_START_DATE_AND_END_TIME, TEST_SAMPLE_FLOAT_ID,
                            getTestCommand(
                                TEST_SAMPLE_FLOAT_INDEX,         // Running index of sample Floating Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_KEYWORD_EMPTY,              // End date
                                TEST_SAMPLE_END_TIME,            // End time
                                TEST_SAMPLE_START_DATE,          // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TEST_SAMPLE_TODAY_DATE, TEST_SAMPLE_END_TIME), 
                            getExpectedStart(TEST_SAMPLE_START_DATE, DateTimeHelper.getTime(DateTimeHelper.now())), 
                            null, TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_FLOATING_ADD_START_TIME_AND_END_TIME, TEST_SAMPLE_FLOAT_ID,
                            getTestCommand(
                                TEST_SAMPLE_FLOAT_INDEX,         // Running index of sample Floating Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_KEYWORD_EMPTY,              // End date
                                TEST_SAMPLE_END_TIME,            // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_SAMPLE_START_TIME,          // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TEST_SAMPLE_TODAY_DATE, TEST_SAMPLE_END_TIME), 
                            getExpectedStart(TEST_SAMPLE_TODAY_DATE, TEST_SAMPLE_START_TIME), 
                            null, TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_FLOATING_ADD_START_DATE_TIME_AND_END_TIME, TEST_SAMPLE_FLOAT_ID,
                            getTestCommand(
                                TEST_SAMPLE_FLOAT_INDEX,         // Running index of sample Floating Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_KEYWORD_EMPTY,              // End date
                                TEST_SAMPLE_END_TIME,            // End time
                                TEST_SAMPLE_START_DATE,          // Start date
                                TEST_SAMPLE_START_TIME,          // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TEST_SAMPLE_TODAY_DATE, TEST_SAMPLE_END_TIME), 
                            getExpectedStart(TEST_SAMPLE_START_DATE, TEST_SAMPLE_START_TIME), 
                            null, TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_FLOATING_ADD_START_DATE_AND_END_DATE, TEST_SAMPLE_FLOAT_ID,
                            getTestCommand(
                                TEST_SAMPLE_FLOAT_INDEX,         // Running index of sample Floating Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_SAMPLE_END_DATE,            // End date
                                TEST_KEYWORD_EMPTY,              // End time
                                TEST_SAMPLE_START_DATE,          // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TEST_SAMPLE_END_DATE, DateTimeHelper.getTime(DateTimeHelper.now())), 
                            getExpectedStart(TEST_SAMPLE_START_DATE, DateTimeHelper.getTime(DateTimeHelper.now())), 
                            null, TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_FLOATING_ADD_START_TIME_AND_END_DATE, TEST_SAMPLE_FLOAT_ID,
                            getTestCommand(
                                TEST_SAMPLE_FLOAT_INDEX,         // Running index of sample Floating Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_SAMPLE_END_DATE,            // End date
                                TEST_KEYWORD_EMPTY,              // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_SAMPLE_START_TIME,          // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TEST_SAMPLE_END_DATE, DateTimeHelper.getTime(DateTimeHelper.now())), 
                            getExpectedStart(TEST_SAMPLE_TODAY_DATE, TEST_SAMPLE_START_TIME), 
                            null, TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_FLOATING_ADD_START_DATE_TIME_AND_END_DATE, TEST_SAMPLE_FLOAT_ID,
                            getTestCommand(
                                TEST_SAMPLE_FLOAT_INDEX,         // Running index of sample Floating Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_SAMPLE_END_DATE,            // End date
                                TEST_KEYWORD_EMPTY,              // End time
                                TEST_SAMPLE_START_DATE,          // Start date
                                TEST_SAMPLE_START_TIME,          // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TEST_SAMPLE_END_DATE, DateTimeHelper.getTime(DateTimeHelper.now())), 
                            getExpectedStart(TEST_SAMPLE_START_DATE, TEST_SAMPLE_START_TIME), 
                            null, TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_FLOATING_ADD_START_DATE_AND_END_DATE_TIME, TEST_SAMPLE_FLOAT_ID,
                            getTestCommand(
                                TEST_SAMPLE_FLOAT_INDEX,         // Running index of sample Floating Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_SAMPLE_END_DATE,            // End date
                                TEST_SAMPLE_END_TIME,            // End time
                                TEST_SAMPLE_START_DATE,          // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TEST_SAMPLE_END_DATE, TEST_SAMPLE_END_TIME), 
                            getExpectedStart(TEST_SAMPLE_START_DATE, DateTimeHelper.getTime(DateTimeHelper.now())), 
                            null, TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_FLOATING_ADD_START_TIME_AND_END_DATE_TIME, TEST_SAMPLE_FLOAT_ID,
                            getTestCommand(
                                TEST_SAMPLE_FLOAT_INDEX,            // Running index of sample Floating Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_SAMPLE_END_DATE,            // End date
                                TEST_SAMPLE_END_TIME,            // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_SAMPLE_START_TIME,          // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TEST_SAMPLE_END_DATE, TEST_SAMPLE_END_TIME), 
                            getExpectedStart(TEST_SAMPLE_TODAY_DATE,TEST_SAMPLE_START_TIME), 
                            null, TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_FLOATING_ADD_START_DATE_TIME_AND_END_DATE_TIME, TEST_SAMPLE_FLOAT_ID,
                            getTestCommand(
                                TEST_SAMPLE_FLOAT_INDEX,            // Running index of sample Floating Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_SAMPLE_END_DATE,            // End date
                                TEST_SAMPLE_END_TIME,            // End time
                                TEST_SAMPLE_START_DATE,          // Start date
                                TEST_SAMPLE_START_TIME,          // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TEST_SAMPLE_END_DATE, TEST_SAMPLE_END_TIME), 
                            getExpectedStart(TEST_SAMPLE_START_DATE,TEST_SAMPLE_START_TIME), 
                            null, TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_FLOATING, TEST_FLOATING_EDIT_PRIORITY, TEST_SAMPLE_FLOAT_ID,
                            getTestCommand(
                                TEST_SAMPLE_FLOAT_INDEX,            // Running index of sample Floating Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_KEYWORD_EMPTY,              // End date
                                TEST_KEYWORD_EMPTY,              // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                TEST_SAMPLE_PRIORITY,            // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            null, null, null, TEST_SAMPLE_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        /*** DeadlineTask Test Cases ***/
        testEditTaskCommand(TEST_TASKTYPE_DEADLINE, TEST_DEADLINE_EDIT_DESCRIPTION, TEST_SAMPLE_DEADLINE_ID,
                            getTestCommand(
                                TEST_SAMPLE_DEADLINE_INDEX,         // Running index of sample Deadline Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_KEYWORD_EMPTY,              // End date
                                TEST_KEYWORD_EMPTY,              // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(DEADLINE_CREATED_SAMPLE_END_DATE,DEADLINE_CREATED_SAMPLE_END_TIME), 
                            null, null, TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_DEADLINE, TEST_DEADLINE_EDIT_END_DATE_AND_REMINDER, TEST_SAMPLE_DEADLINE_ID,
                            getTestCommand(
                                TEST_SAMPLE_DEADLINE_INDEX,         // Running index of sample Deadline Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_SAMPLE_END_DATE,            // End date
                                TEST_KEYWORD_EMPTY,              // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_SAMPLE_REMINDER_DATE_TIME,  // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TEST_SAMPLE_END_DATE, DEADLINE_CREATED_SAMPLE_END_TIME), 
                            null, getExpectedReminder(TEST_SAMPLE_REMINDER_DATE_TIME), 
                            TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_DEADLINE, TEST_DEADLINE_EDIT_END_DATE, TEST_SAMPLE_DEADLINE_ID,
                            getTestCommand(
                                TEST_SAMPLE_DEADLINE_INDEX,         // Running index of sample Deadline Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_SAMPLE_END_DATE,            // End date
                                TEST_KEYWORD_EMPTY,              // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TEST_SAMPLE_END_DATE, DEADLINE_CREATED_SAMPLE_END_TIME), 
                            null, null, TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_DEADLINE, TEST_DEADLINE_EDIT_END_DATE_TIME, TEST_SAMPLE_DEADLINE_ID,
                            getTestCommand(
                                TEST_SAMPLE_DEADLINE_INDEX,         // Running index of sample Deadline Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_SAMPLE_END_DATE,            // End date
                                TEST_SAMPLE_END_TIME,            // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TEST_SAMPLE_END_DATE, TEST_SAMPLE_END_TIME), 
                            null, null, TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_DEADLINE, TEST_DEADLINE_EDIT_END_DATE_TIME_AND_REMINDER, TEST_SAMPLE_DEADLINE_ID,
                            getTestCommand(
                                TEST_SAMPLE_DEADLINE_INDEX,         // Running index of sample Deadline Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_SAMPLE_END_DATE,            // End date
                                TEST_SAMPLE_END_TIME,            // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_SAMPLE_REMINDER_DATE_TIME,  // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TEST_SAMPLE_END_DATE, TEST_SAMPLE_END_TIME), 
                            null, getExpectedReminder(TEST_SAMPLE_REMINDER_DATE_TIME), 
                            TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_DEADLINE_ADD_START_TIME, TEST_SAMPLE_DEADLINE_ID,
                            getTestCommand(
                                TEST_SAMPLE_DEADLINE_INDEX,         // Running index of sample Deadline Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_KEYWORD_EMPTY,              // End date
                                TEST_KEYWORD_EMPTY,              // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_SAMPLE_START_TIME,          // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(DEADLINE_CREATED_SAMPLE_END_DATE, DEADLINE_CREATED_SAMPLE_END_TIME), 
                            getExpectedStart(TEST_SAMPLE_TODAY_DATE,TEST_SAMPLE_START_TIME), 
                            null, TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_DEADLINE_ADD_START_DATE_TIME, TEST_SAMPLE_DEADLINE_ID,
                            getTestCommand(
                                TEST_SAMPLE_DEADLINE_INDEX,         // Running index of sample Deadline Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_KEYWORD_EMPTY,              // End date
                                TEST_KEYWORD_EMPTY,              // End time
                                TEST_SAMPLE_START_DATE,          // Start date
                                TEST_SAMPLE_START_TIME,          // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(DEADLINE_CREATED_SAMPLE_END_DATE, DEADLINE_CREATED_SAMPLE_END_TIME), 
                            getExpectedStart(TEST_SAMPLE_START_DATE,TEST_SAMPLE_START_TIME), 
                            null, TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_DEADLINE, TEST_DEADLINE_EDIT_PRIORITY, TEST_SAMPLE_DEADLINE_ID,
                            getTestCommand(
                                TEST_SAMPLE_DEADLINE_INDEX,         // Running index of sample Deadline Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                DEADLINE_CREATED_SAMPLE_END_DATE,              // End date
                                DEADLINE_CREATED_SAMPLE_END_TIME,              // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                TEST_SAMPLE_PRIORITY,            // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(DEADLINE_CREATED_SAMPLE_END_DATE, DEADLINE_CREATED_SAMPLE_END_TIME), 
                            null, null, TEST_SAMPLE_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_DEADLINE, TEST_DEADLINE_EDIT_RECURTYPE_DAY, TEST_SAMPLE_DEADLINE_ID,
                            getTestCommand(
                                TEST_SAMPLE_DEADLINE_INDEX,         // Running index of sample Deadline Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_KEYWORD_EMPTY,              // End date
                                TEST_KEYWORD_EMPTY,              // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_DAY),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(DEADLINE_CREATED_SAMPLE_END_DATE, DEADLINE_CREATED_SAMPLE_END_TIME), 
                            null, null, TEST_TASK_DEFAULT_PRIORITY, true, TEST_RECURTYPE_DAY);
        
        testEditTaskCommand(TEST_TASKTYPE_DEADLINE, TEST_DEADLINE_EDIT_RECURTYPE_WEEK, TEST_SAMPLE_DEADLINE_ID,
                            getTestCommand(
                                TEST_SAMPLE_DEADLINE_INDEX,         // Running index of sample Floating Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_KEYWORD_EMPTY,              // End date
                                TEST_KEYWORD_EMPTY,              // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_WEEK),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(DEADLINE_CREATED_SAMPLE_END_DATE, DEADLINE_CREATED_SAMPLE_END_TIME), 
                            null, null, TEST_TASK_DEFAULT_PRIORITY, true, TEST_RECURTYPE_WEEK);
        
        testEditTaskCommand(TEST_TASKTYPE_DEADLINE, TEST_DEADLINE_EDIT_RECURTYPE_MONTH, TEST_SAMPLE_DEADLINE_ID,
                            getTestCommand(
                                TEST_SAMPLE_DEADLINE_INDEX,         // Running index of sample Deadline Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_KEYWORD_EMPTY,              // End date
                                TEST_KEYWORD_EMPTY,              // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_MONTH),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(DEADLINE_CREATED_SAMPLE_END_DATE, DEADLINE_CREATED_SAMPLE_END_TIME), 
                            null, null, TEST_TASK_DEFAULT_PRIORITY, true, TEST_RECURTYPE_MONTH);
        
        testEditTaskCommand(TEST_TASKTYPE_DEADLINE, TEST_DEADLINE_EDIT_RECURTYPE_YEAR, TEST_SAMPLE_DEADLINE_ID,
                            getTestCommand(
                                TEST_SAMPLE_DEADLINE_INDEX,         // Running index of sample Deadline Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_KEYWORD_EMPTY,              // End date
                                TEST_KEYWORD_EMPTY,              // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_YEAR),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(DEADLINE_CREATED_SAMPLE_END_DATE, DEADLINE_CREATED_SAMPLE_END_TIME), 
                            null, null, TEST_TASK_DEFAULT_PRIORITY, true, TEST_RECURTYPE_YEAR);
    
        /*** TimedTask Test Cases ***/
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_TIMED_EDIT_DESCRIPTION, TEST_SAMPLE_TIMED_ID,
                            getTestCommand(
                                TEST_SAMPLE_TIMED_INDEX,            // Running index of sample Timed Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_KEYWORD_EMPTY,              // End date
                                TEST_KEYWORD_EMPTY,              // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TIMED_CREATED_SAMPLE_END_DATE, TIMED_CREATED_SAMPLE_END_TIME), 
                            getExpectedStart(TIMED_CREATED_SAMPLE_START_DATE, TIMED_CREATED_SAMPLE_START_TIME), 
                            null, TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_TIMED_EDIT_END_DATE, TEST_SAMPLE_TIMED_ID,
                            getTestCommand(
                                TEST_SAMPLE_TIMED_INDEX,            // Running index of sample Timed Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_SAMPLE_END_DATE,            // End date
                                TEST_KEYWORD_EMPTY,              // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TEST_SAMPLE_END_DATE, TIMED_CREATED_SAMPLE_END_TIME), 
                            getExpectedStart(TIMED_CREATED_SAMPLE_START_DATE, TIMED_CREATED_SAMPLE_START_TIME), 
                            null, TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_TIMED_EDIT_END_DATE_AND_REMINDER, TEST_SAMPLE_TIMED_ID,
                            getTestCommand(
                                TEST_SAMPLE_TIMED_INDEX,            // Running index of sample Timed Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_SAMPLE_END_DATE,            // End date
                                TEST_KEYWORD_EMPTY,              // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_SAMPLE_REMINDER_DATE_TIME,  // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TEST_SAMPLE_END_DATE, TIMED_CREATED_SAMPLE_END_TIME), 
                            getExpectedStart(TIMED_CREATED_SAMPLE_START_DATE, TIMED_CREATED_SAMPLE_START_TIME),
                            getExpectedReminder(TEST_SAMPLE_REMINDER_DATE_TIME), 
                            TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_TIMED_EDIT_END_TIME, TEST_SAMPLE_TIMED_ID,
                            getTestCommand(
                                TEST_SAMPLE_TIMED_INDEX,            // Running index of sample Timed Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_KEYWORD_EMPTY,              // End date
                                TEST_SAMPLE_END_TIME,            // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TIMED_CREATED_SAMPLE_END_DATE, TEST_SAMPLE_END_TIME), 
                            getExpectedStart(TIMED_CREATED_SAMPLE_START_DATE, TIMED_CREATED_SAMPLE_START_TIME), 
                            null, TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_TIMED_EDIT_END_TIME_AND_REMINDER, TEST_SAMPLE_TIMED_ID,
                            getTestCommand(
                                TEST_SAMPLE_TIMED_INDEX,            // Running index of sample Timed Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_KEYWORD_EMPTY,              // End date
                                TEST_SAMPLE_END_TIME,            // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_SAMPLE_REMINDER_DATE_TIME,  // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TIMED_CREATED_SAMPLE_END_DATE, TEST_SAMPLE_END_TIME), 
                            getExpectedStart(TIMED_CREATED_SAMPLE_START_DATE, TIMED_CREATED_SAMPLE_START_TIME), 
                            getExpectedReminder(TEST_SAMPLE_REMINDER_DATE_TIME), 
                            TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_TIMED_EDIT_END_DATE_TIME, TEST_SAMPLE_TIMED_ID,
                            getTestCommand(
                                TEST_SAMPLE_TIMED_INDEX,            // Running index of sample Timed Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_SAMPLE_END_DATE,            // End date
                                TEST_SAMPLE_END_TIME,            // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TEST_SAMPLE_END_DATE, TEST_SAMPLE_END_TIME), 
                            getExpectedStart(TIMED_CREATED_SAMPLE_START_DATE, TIMED_CREATED_SAMPLE_START_TIME), 
                            null, TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_TIMED_EDIT_END_DATE_TIME_AND_REMINDER, TEST_SAMPLE_TIMED_ID,
                            getTestCommand(
                                TEST_SAMPLE_TIMED_INDEX,            // Running index of sample Timed Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_SAMPLE_END_DATE,            // End date
                                TEST_SAMPLE_END_TIME,            // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_SAMPLE_REMINDER_DATE_TIME,  // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TEST_SAMPLE_END_DATE, TEST_SAMPLE_END_TIME), 
                            getExpectedStart(TIMED_CREATED_SAMPLE_START_DATE, TIMED_CREATED_SAMPLE_START_TIME),
                            getExpectedReminder(TEST_SAMPLE_REMINDER_DATE_TIME), 
                            TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_TIMED_EDIT_START_DATE, TEST_SAMPLE_TIMED_ID,
                            getTestCommand(
                                TEST_SAMPLE_TIMED_INDEX,            // Running index of sample Timed Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_KEYWORD_EMPTY,              // End date
                                TEST_KEYWORD_EMPTY,              // End time
                                TEST_SAMPLE_START_DATE,          // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TIMED_CREATED_SAMPLE_END_DATE, TIMED_CREATED_SAMPLE_END_TIME), 
                            getExpectedStart(TEST_SAMPLE_START_DATE, TIMED_CREATED_SAMPLE_START_TIME), 
                            null, TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_TIMED_EDIT_START_DATE_AND_REMINDER, TEST_SAMPLE_TIMED_ID,
                            getTestCommand(
                                TEST_SAMPLE_TIMED_INDEX,            // Running index of sample Timed Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_KEYWORD_EMPTY,              // End date
                                TEST_KEYWORD_EMPTY,              // End time
                                TEST_SAMPLE_START_DATE,          // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_SAMPLE_REMINDER_DATE_TIME,  // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TIMED_CREATED_SAMPLE_END_DATE, TIMED_CREATED_SAMPLE_END_TIME), 
                            getExpectedStart(TEST_SAMPLE_START_DATE, TIMED_CREATED_SAMPLE_START_TIME), 
                            getExpectedReminder(TEST_SAMPLE_REMINDER_DATE_TIME), 
                            TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_TIMED_EDIT_START_TIME, TEST_SAMPLE_TIMED_ID,
                            getTestCommand(
                                TEST_SAMPLE_TIMED_INDEX,            // Running index of sample Timed Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_KEYWORD_EMPTY,              // End date
                                TEST_KEYWORD_EMPTY,              // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_SAMPLE_START_TIME,          // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TIMED_CREATED_SAMPLE_END_DATE, TIMED_CREATED_SAMPLE_END_TIME), 
                            getExpectedStart(TIMED_CREATED_SAMPLE_START_DATE, TEST_SAMPLE_START_TIME), 
                            null, TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_TIMED_EDIT_START_TIME_AND_REMINDER, TEST_SAMPLE_TIMED_ID,
                            getTestCommand(
                                TEST_SAMPLE_TIMED_INDEX,            // Running index of sample Timed Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_KEYWORD_EMPTY,              // End date
                                TEST_KEYWORD_EMPTY,              // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_SAMPLE_START_TIME,          // Start time
                                TEST_SAMPLE_REMINDER_DATE_TIME,  // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TIMED_CREATED_SAMPLE_END_DATE, TIMED_CREATED_SAMPLE_END_TIME), 
                            getExpectedStart(TIMED_CREATED_SAMPLE_START_DATE, TEST_SAMPLE_START_TIME), 
                            getExpectedReminder(TEST_SAMPLE_REMINDER_DATE_TIME), 
                            TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_TIMED_EDIT_START_DATE_TIME, TEST_SAMPLE_TIMED_ID,
                            getTestCommand(
                                TEST_SAMPLE_TIMED_INDEX,            // Running index of sample Timed Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_KEYWORD_EMPTY,              // End date
                                TEST_KEYWORD_EMPTY,              // End time
                                TEST_SAMPLE_START_DATE,          // Start date
                                TEST_SAMPLE_START_TIME,          // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TIMED_CREATED_SAMPLE_END_DATE, TIMED_CREATED_SAMPLE_END_TIME), 
                            getExpectedStart(TEST_SAMPLE_START_DATE, TEST_SAMPLE_START_TIME), 
                            null, TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_TIMED_EDIT_START_DATE_TIME_REMINDER, TEST_SAMPLE_TIMED_ID,
                            getTestCommand(
                                TEST_SAMPLE_TIMED_INDEX,            // Running index of sample Timed Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_KEYWORD_EMPTY,              // End date
                                TEST_KEYWORD_EMPTY,              // End time
                                TEST_SAMPLE_START_DATE,          // Start date
                                TEST_SAMPLE_START_TIME,          // Start time
                                TEST_SAMPLE_REMINDER_DATE_TIME,  // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TIMED_CREATED_SAMPLE_END_DATE, TIMED_CREATED_SAMPLE_END_TIME), 
                            getExpectedStart(TEST_SAMPLE_START_DATE, TEST_SAMPLE_START_TIME), 
                            getExpectedReminder(TEST_SAMPLE_REMINDER_DATE_TIME), 
                            TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_TIMED_EDIT_START_TIME_END_DATE, TEST_SAMPLE_TIMED_ID,
                            getTestCommand(
                                TEST_SAMPLE_TIMED_INDEX,            // Running index of sample Timed Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_SAMPLE_END_DATE,            // End date
                                TEST_KEYWORD_EMPTY,              // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_SAMPLE_START_TIME,          // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TEST_SAMPLE_END_DATE, TIMED_CREATED_SAMPLE_END_TIME), 
                            getExpectedStart(TIMED_CREATED_SAMPLE_START_DATE, TEST_SAMPLE_START_TIME), 
                            null, TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_TIMED_EDIT_START_DATE_END_TIME, TEST_SAMPLE_TIMED_ID,
                            getTestCommand(
                                TEST_SAMPLE_TIMED_INDEX,            // Running index of sample Timed Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_KEYWORD_EMPTY,              // End date
                                TEST_SAMPLE_END_TIME,            // End time
                                TEST_SAMPLE_START_DATE,          // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TIMED_CREATED_SAMPLE_END_DATE, TEST_SAMPLE_END_TIME), 
                            getExpectedStart(TEST_SAMPLE_START_DATE, TIMED_CREATED_SAMPLE_START_TIME), 
                            null, TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_TIMED_EDIT_START_DATE_TIME_END_DATE_TIME, TEST_SAMPLE_TIMED_ID,
                            getTestCommand(
                                TEST_SAMPLE_TIMED_INDEX,            // Running index of sample Timed Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_SAMPLE_END_DATE,            // End date
                                TEST_SAMPLE_END_TIME,            // End time
                                TEST_SAMPLE_START_DATE,          // Start date
                                TEST_SAMPLE_START_TIME,          // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_NULL),            // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TEST_SAMPLE_END_DATE, TEST_SAMPLE_END_TIME), 
                            getExpectedStart(TEST_SAMPLE_START_DATE, TEST_SAMPLE_START_TIME), 
                            null, TEST_TASK_DEFAULT_PRIORITY, false, TEST_RECURTYPE_NULL);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_TIMED_EDIT_RECURTYPE_DAY, TEST_SAMPLE_TIMED_ID,
                            getTestCommand(
                                TEST_SAMPLE_TIMED_INDEX,            // Running index of sample Timed Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_KEYWORD_EMPTY,              // End date
                                TEST_KEYWORD_EMPTY,              // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_DAY),             // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TIMED_CREATED_SAMPLE_END_DATE, TIMED_CREATED_SAMPLE_END_TIME), 
                            getExpectedStart(TIMED_CREATED_SAMPLE_START_DATE, TIMED_CREATED_SAMPLE_START_TIME), 
                            null, TEST_TASK_DEFAULT_PRIORITY, true, TEST_RECURTYPE_DAY);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_TIMED_EDIT_RECURTYPE_WEEK, TEST_SAMPLE_TIMED_ID,
                            getTestCommand(
                                TEST_SAMPLE_TIMED_INDEX,            // Running index of sample Timed Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_KEYWORD_EMPTY,              // End date
                                TEST_KEYWORD_EMPTY,              // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_WEEK),             // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TIMED_CREATED_SAMPLE_END_DATE, TIMED_CREATED_SAMPLE_END_TIME), 
                            getExpectedStart(TIMED_CREATED_SAMPLE_START_DATE, TIMED_CREATED_SAMPLE_START_TIME), 
                            null, TEST_TASK_DEFAULT_PRIORITY, true, TEST_RECURTYPE_WEEK);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_TIMED_EDIT_RECURTYPE_MONTH, TEST_SAMPLE_TIMED_ID,
                            getTestCommand(
                                TEST_SAMPLE_TIMED_INDEX,            // Running index of sample Timed Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_KEYWORD_EMPTY,              // End date
                                TEST_KEYWORD_EMPTY,              // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_MONTH),             // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TIMED_CREATED_SAMPLE_END_DATE, TIMED_CREATED_SAMPLE_END_TIME), 
                            getExpectedStart(TIMED_CREATED_SAMPLE_START_DATE, TIMED_CREATED_SAMPLE_START_TIME), 
                            null, TEST_TASK_DEFAULT_PRIORITY, true, TEST_RECURTYPE_MONTH);
        
        testEditTaskCommand(TEST_TASKTYPE_TIMED, TEST_TIMED_EDIT_RECURTYPE_YEAR, TEST_SAMPLE_TIMED_ID,
                            getTestCommand(
                                TEST_SAMPLE_TIMED_INDEX,            // Running index of sample Timed Task
                                TEST_SAMPLE_DESCRIPTION,         // Sample description for edited Task
                                TEST_KEYWORD_EMPTY,              // End date
                                TEST_KEYWORD_EMPTY,              // End time
                                TEST_KEYWORD_EMPTY,              // Start date
                                TEST_KEYWORD_EMPTY,              // Start time
                                TEST_KEYWORD_EMPTY,              // Reminder date and time
                                -1,                              // Priority: -1 symbolize no specified priority
                                TEST_RECURTYPE_YEAR),             // Recur type
                            TEST_SAMPLE_DESCRIPTION, 
                            getExpectedEnd(TIMED_CREATED_SAMPLE_END_DATE, TIMED_CREATED_SAMPLE_END_TIME), 
                            getExpectedStart(TIMED_CREATED_SAMPLE_START_DATE, TIMED_CREATED_SAMPLE_START_TIME), 
                            null, TEST_TASK_DEFAULT_PRIORITY, true, TEST_RECURTYPE_YEAR);
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
     * @param expectedEnd
     *            Expected End (date/time)
     * @param expectedStart
     *            Expected Start (date/time)
     * @param expectedReminder
     *            Expected Reminder (date/time)
     * @throws Exception
     */
    public void testEditTaskCommand(Task.TASK_TYPE expectedTaskType, String testDescription, int taskId,
            String userCommand, String expectedTaskDescription, LocalDateTime expectedEnd, LocalDateTime expectedStart,
            LocalDateTime expectedReminder, int expectedTaskPriority, boolean expectedRecurStatus, Task.RECUR_TYPE expectedRecurType) throws Exception {
        prepareDummyData();

        // Execute the test command
        _dummy_editTaskCommand = (EditTaskCommand) _commandParserInstance.parse(userCommand, initStateTaskId());
        _dummy_editTaskCommand.execute();

        // Get the task of the modified task and retrieve the TASK_TYPE to check
        // what kind of check should be done
        _dummy_genericTask = TaskController.getInstance().getTask(taskId);
        Task.TASK_TYPE type = _dummy_genericTask.getType();

        switch (type) {
        case FLOATING:
            _dummy_floatingTask = (FloatingTask) _dummy_genericTask;
            if (expectedTaskType != null) {
                testTaskType(testDescription, expectedTaskType, type);
            }
            if (expectedTaskDescription != null) {
                testTaskDescription(testDescription, expectedTaskDescription, _dummy_floatingTask.getDescription());
            }
            if (expectedTaskPriority != -1) {
                testTaskPriority(testDescription, expectedTaskPriority, _dummy_floatingTask.getPriority());
            }
            break;
        case DEADLINE:
            _dummy_deadlineTask = (DeadlineTask) _dummy_genericTask;
            if (expectedTaskType != null) {
                testTaskType(testDescription, expectedTaskType, type);
            }
            if (expectedTaskDescription != null) {
                testTaskDescription(testDescription, expectedTaskDescription, _dummy_deadlineTask.getDescription());
            }
            if (expectedEnd != null) {
                testTaskEnd(testDescription, expectedEnd, _dummy_deadlineTask.getEnd());
            }
            if (expectedReminder != null) {
                testTaskReminder(testDescription, expectedReminder, _dummy_deadlineTask.getReminder());
            }
            if (expectedTaskPriority != -1) {
                testTaskPriority(testDescription, expectedTaskPriority, _dummy_deadlineTask.getPriority());
            }
            testTaskRecurrence(testDescription, expectedRecurStatus, expectedRecurType, _dummy_deadlineTask.isRecurring(), _dummy_deadlineTask.getRecurPeriod());
            break;
        case TIMED:
            _dummy_timedTask = (TimedTask) _dummy_genericTask;
            if (expectedTaskType != null) {
                testTaskType(testDescription, expectedTaskType, type);
            }
            if (expectedTaskDescription != null) {
                testTaskDescription(testDescription, expectedTaskDescription, _dummy_timedTask.getDescription());
            }
            if (expectedStart != null) {
                testTaskStart(testDescription, expectedStart, _dummy_timedTask.getStart());
            }
            if (expectedEnd != null) {
                testTaskEnd(testDescription, expectedEnd, _dummy_timedTask.getEnd());
            }
            if (expectedReminder != null) {
                testTaskReminder(testDescription, expectedReminder, _dummy_timedTask.getReminder());
            }
            if (expectedTaskPriority != -1) {
                testTaskPriority(testDescription, expectedTaskPriority, _dummy_timedTask.getPriority());
            }
            testTaskRecurrence(testDescription, expectedRecurStatus, expectedRecurType, _dummy_timedTask.isRecurring(), _dummy_timedTask.getRecurPeriod());

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

    public LocalDateTime getExpectedStart(String start_date, String start_time) {
        String start = TEST_KEYWORD_EMPTY;
        if (start_date != null) {
            start += start_date + WHITE_SPACE;
        }
        if (start_time != null) {
            start += start_time;
        }
        return DateTimeHelper.parseStringToDateTime(start);
    }
    
    
    public LocalDateTime getExpectedReminder(String reminder_date_time) {
        String reminder = TEST_KEYWORD_EMPTY;
        if (reminder_date_time != null) {
            reminder += reminder_date_time;
        }
        return DateTimeHelper.parseStringToDateTime(reminder);
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
    
    public void testTaskPriority(String testDescription, int expectedPriority, int actualPriority) {
        assertEquals(testDescription, expectedPriority, actualPriority);
    }
    
    public void testTaskRecurrence(String testDescription, boolean expectedRecurStatus, Task.RECUR_TYPE expectedRecurType, boolean actualRecurStatus, Task.RECUR_TYPE actualRecurType) {
        assertEquals(testDescription, expectedRecurStatus, actualRecurStatus);
        assertEquals(testDescription, expectedRecurType, actualRecurType);
    }
    
}