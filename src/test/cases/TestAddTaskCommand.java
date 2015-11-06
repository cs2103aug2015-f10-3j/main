//@@author A0125473H
package test.cases;

import static org.junit.Assert.*;

import java.io.File;
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
import main.paddletask.common.exception.NoSuchTaskException;
import main.paddletask.common.exception.UpdateTaskException;
import main.paddletask.common.util.DateTimeHelper;

public class TestAddTaskCommand {
	/*
	 * Test Assumption:
	 * 1. StorageController is working correctly
	 * 2. TaskController is working correctly
	 * 3. CommandParser is working correctly
	 */

	/*** Variables ***/
	protected static TaskController taskController;
	protected static CommandParser parser;

	/*** Setup ***/
	@Before
	public void setUp() throws Exception {
		taskController = TaskController.getInstance();
		parser = new CommandParser();
		
		// wipe out all data within task.xml
		File f = new File("task.xml");
		f.delete();
	}
	
	/*** Test Cases ***/

	/*** FloatingTask related test cases ***/
	
}