package test;

import static org.junit.Assert.*;

import java.io.File;
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
import common.exception.NoSuchTaskException;
import common.exception.UpdateTaskException;
import common.util.DateTimeHelper;

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