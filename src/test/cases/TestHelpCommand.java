package test.cases;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.paddletask.command.api.Command;
import main.paddletask.command.api.HelpCommand;
import main.paddletask.common.data.HelpManual;
import main.paddletask.common.exception.InvalidCommandFormatException;
import main.paddletask.logic.api.LogicController;
import main.paddletask.parser.api.CommandParser;
import main.paddletask.ui.controller.UIController;

public class TestHelpCommand {
	/*** Variables ***/
	private CommandParser parser = null;
	private HelpManual manual = null;

	/*** Setup and Teardown ***/
	@Before
	public void setUp() throws Exception {
		parser = new CommandParser();
		manual = HelpManual.getInstance();
	}

	@After
	public void tearDown() throws Exception {
	}

	/*** Test Cases 
	 * @throws Exception  ***/
	@Test
	public void testAdd() throws Exception{
		String testInput = "help add";
		HelpCommand command = (HelpCommand)parser.parse(testInput);
		command.execute();
		assertEquals(command.getHelpComments(), manual.getHelp("add") );
		//fail("Not yet implemented");
	}
	
	@Test
	public void testEdit() throws Exception{
		String testInput = "help edit";
		HelpCommand command = (HelpCommand)parser.parse(testInput);
		command.execute();
		assertEquals(command.getHelpComments(), manual.getHelp("edit") );
		//fail("Not yet implemented");
	}
	
	@Test
	public void testView() throws Exception{
		String testInput = "help view";
		HelpCommand command = (HelpCommand)parser.parse(testInput);
		command.execute();
		assertEquals(command.getHelpComments(), manual.getHelp("view") );
		//fail("Not yet implemented");
	}
	
	@Test
	public void testDelete() throws Exception{
		String testInput = "help delete";
		HelpCommand command = (HelpCommand)parser.parse(testInput);
		command.execute();
		assertEquals(command.getHelpComments(), manual.getHelp("delete") );
		//fail("Not yet implemented");
	}
	
	@Test
	public void testComplete() throws Exception{
		String testInput = "help complete";
		HelpCommand command = (HelpCommand)parser.parse(testInput);
		command.execute();
		assertEquals(command.getHelpComments(), manual.getHelp("complete") );
		//fail("Not yet implemented");
	}
	
	@Test
	public void testSearch() throws Exception{
		String testInput = "help search";
		HelpCommand command = (HelpCommand)parser.parse(testInput);
		command.execute();
		assertEquals(command.getHelpComments(), manual.getHelp("search") );
		//fail("Not yet implemented");
	}
	
	@Test
	public void testUndo() throws Exception{
		String testInput = "help undo";
		HelpCommand command = (HelpCommand)parser.parse(testInput);
		command.execute();
		assertEquals(command.getHelpComments(), manual.getHelp("undo") );
		//fail("Not yet implemented");
	}
	
	@Test
	public void testRedo() throws Exception{
		String testInput = "help redo";
		HelpCommand command = (HelpCommand)parser.parse(testInput);
		command.execute();
		assertEquals(command.getHelpComments(), manual.getHelp("redo") );
		//fail("Not yet implemented");
	}
	
	@Test
	public void testClear() throws Exception{
		String testInput = "help clear";
		HelpCommand command = (HelpCommand)parser.parse(testInput);
		command.execute();
		assertEquals(command.getHelpComments(), manual.getHelp("clear") );
		//fail("Not yet implemented");
	}
	
	@Test
	public void testHelp() throws Exception{
		String testInput = "help help";
		HelpCommand command = (HelpCommand)parser.parse(testInput);
		command.execute();
		assertEquals(command.getHelpComments(), manual.getHelp("help") );
		//fail("Not yet implemented");
	}
	
	@Test
	public void testExit() throws Exception{
		String testInput = "help exit";
		HelpCommand command = (HelpCommand)parser.parse(testInput);
		command.execute();
		assertEquals(command.getHelpComments(), manual.getHelp("exit") );
		//fail("Not yet implemented");
	}
	
	@Test
	public void testAll() throws Exception{
		String testInput = "help all";
		HelpCommand command = (HelpCommand)parser.parse(testInput);
		command.execute();
		assertEquals(command.getHelpComments(), manual.getHelp("all") );
		//fail("Not yet implemented");
	}
	

}
