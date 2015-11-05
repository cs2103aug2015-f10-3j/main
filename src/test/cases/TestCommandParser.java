package test.cases;

import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

import main.paddletask.common.data.ParserConstants.COMMAND_TYPE;
import main.paddletask.parser.api.CommandParser;
import main.paddletask.parser.logic.ParseLogic;
import main.paddletask.task.api.TaskController;

public class TestCommandParser {
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

	@Test
	public void testInvalidCommands() throws Exception{
		checkCommand(COMMAND_TYPE.INVALID, "");
		checkCommand(COMMAND_TYPE.INVALID, "hello");
		checkCommand(COMMAND_TYPE.INVALID, "HELLO");
	}

	@Test(expected=NullPointerException.class)
	public void testNullCommands() throws Exception{
		checkCommand(COMMAND_TYPE.INVALID, null);
	}
	
	/**
	 * Testing add commands
	 * @throws Exception 
	 */
	@Test
	public void testCommandAdd() throws Exception{
		checkCommand(COMMAND_TYPE.ADD, "add");
		checkCommand(COMMAND_TYPE.ADD, "ADD");
		checkCommand(COMMAND_TYPE.ADD, "aDd");
		checkCommand(COMMAND_TYPE.ADD, "aDD");
		
		checkCommand(COMMAND_TYPE.ADD, "/a");
		checkCommand(COMMAND_TYPE.ADD, "/A");
	}
	
	/**
	 * Test clear data
	 * @throws Exception 
	 */
	@Test
	public void testCommandClear() throws Exception{
		checkCommand(COMMAND_TYPE.CLEAR, "CLEAR");
		checkCommand(COMMAND_TYPE.CLEAR, "clear");
		checkCommand(COMMAND_TYPE.CLEAR, "/cl");
		checkCommand(COMMAND_TYPE.CLEAR, "/CL");
	}
	
	/**
	 * Test Delete
	 * @throws Exception 
	 */
	@Test
	public void testCommandDelete() throws Exception{
		checkCommand(COMMAND_TYPE.DELETE, "DELETE");
		checkCommand(COMMAND_TYPE.DELETE, "delete");
		checkCommand(COMMAND_TYPE.DELETE, "/d");
		checkCommand(COMMAND_TYPE.DELETE, "/D");
	}
	
	/**
	 * Test edit 
	 * @throws Exception 
	 */
	@Test
	public void testCommandEdit() throws Exception{
		checkCommand(COMMAND_TYPE.EDIT, "EDIT");
		checkCommand(COMMAND_TYPE.EDIT, "edit");
		checkCommand(COMMAND_TYPE.EDIT, "/e");
		checkCommand(COMMAND_TYPE.EDIT, "/E");
	}
	
	/**
	 * Test search
	 * @throws Exception 
	 */
	@Test
	public void testCommandSearch() throws Exception{
		checkCommand(COMMAND_TYPE.SEARCH, "SEARCH");
		checkCommand(COMMAND_TYPE.SEARCH, "search");
		checkCommand(COMMAND_TYPE.SEARCH, "/s");
	}
	
	/**
	 * Test list 
	 * @throws Exception 
	 */
	@Test
	public void testCommandList() throws Exception{
		checkCommand(COMMAND_TYPE.VIEW, "VIEW");
		checkCommand(COMMAND_TYPE.VIEW, "view");
		checkCommand(COMMAND_TYPE.VIEW, "/v");
		checkCommand(COMMAND_TYPE.VIEW, "/V");
	}
	
	/**
	 * Test redo
	 * @throws Exception 
	 */
	@Test
	public void testCommandRedo() throws Exception{
		checkCommand(COMMAND_TYPE.REDO, "REDO");
		checkCommand(COMMAND_TYPE.REDO, "redo");
		checkCommand(COMMAND_TYPE.REDO, "/r");
		checkCommand(COMMAND_TYPE.REDO, "/R");
	}
	
	/**
	 * Test done
	 * @throws Exception 
	 */
	@Test
	public void testCommandDone() throws Exception{
		checkCommand(COMMAND_TYPE.COMPLETE, "COMPLETE");
		checkCommand(COMMAND_TYPE.COMPLETE, "complete");
		checkCommand(COMMAND_TYPE.COMPLETE, "/c");
		checkCommand(COMMAND_TYPE.COMPLETE, "/C");
	}
	
	/**
	 * Test undo
	 * @throws Exception 
	 */
	@Test
	public void testCommandUndo() throws Exception{
		checkCommand(COMMAND_TYPE.UNDO, "UNDO");
		checkCommand(COMMAND_TYPE.UNDO, "undo");
		checkCommand(COMMAND_TYPE.UNDO, "/u");
		checkCommand(COMMAND_TYPE.UNDO, "/U");
	}
	
	/**
	 * Test exit
	 * @throws Exception 
	 */
	@Test
	public void testCommandExit() throws Exception{
		checkCommand(COMMAND_TYPE.EXIT, "EXIT");
		checkCommand(COMMAND_TYPE.EXIT, "exit");
		checkCommand(COMMAND_TYPE.EXIT, "/ex");
		checkCommand(COMMAND_TYPE.EXIT, "/EX");
	}
	
	/**
	 * Test help
	 * @throws Exception 
	 */
	@Test
	public void testCommandHelp() throws Exception{
		checkCommand(COMMAND_TYPE.HELP, "HELP");
		checkCommand(COMMAND_TYPE.HELP, "help");
		checkCommand(COMMAND_TYPE.HELP, "/h");
		checkCommand(COMMAND_TYPE.HELP, "/H");
	}
	
	/** 
	 * Testing Any order of Commands
	 * @throws Exception 
	 */
	@Test
	public void testCommand() throws Exception {
		
		checkCommand(COMMAND_TYPE.ADD, "add");
		checkCommand(COMMAND_TYPE.ADD, "/a");
		checkCommand(COMMAND_TYPE.ADD, "add ");
		
		checkCommand(COMMAND_TYPE.DELETE, "delete");
		checkCommand(COMMAND_TYPE.INVALID, "   delete\n");
		checkCommand(COMMAND_TYPE.INVALID, "   /d");
		
		checkCommand(COMMAND_TYPE.COMPLETE, "complete");
		
		checkCommand(COMMAND_TYPE.CLEAR, "/cl");
		
		checkCommand(COMMAND_TYPE.EDIT, "edit");
		
		checkCommand(COMMAND_TYPE.UNDO, "undo");
		
		checkCommand(COMMAND_TYPE.SEARCH, "search");
		
		checkCommand(COMMAND_TYPE.VIEW, "view");
		
		checkCommand(COMMAND_TYPE.HELP, "HELP");
		
		checkCommand(COMMAND_TYPE.EXIT, "eXiT");
	}
	
	private void checkCommand (COMMAND_TYPE expected, String input) throws Exception{
    	Field f = parser.getClass().getDeclaredField("parserLogic");
		f.setAccessible(true);
		ParseLogic pl = (ParseLogic) f.get(parser);
		assertEquals(expected, pl.determineCommandType(input));
	}

}
