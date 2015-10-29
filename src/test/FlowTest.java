package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ui.controller.UIController;
import ui.view.CommandLinePanel;

public class FlowTest {
	UIController uiController;
	@Before
	public void setUp() throws Exception {
		CommandLinePanel panel = new CommandLinePanel();
		UIController uiController = UIController.getInstance(panel);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAdd() {
		String input = "add buy hotdog";
		String[] expected = {"Description: buy hotdog", "Task Type: floating", "Priority: 3"};
		assertArrayEquals(uiController.processUserInput(input), expected);
	}
	
	@Test
	public void testHelp(){
		String input = "help exit";
		String[] expected = {"EXIT COMMAND",
				"exit - Close and exit PaddleTask"};
		String[] actual = uiController.processUserInput(input);
		for(int i = 0;i < expected.length; i++){
			assertArrayEquals(expected, actual);
		}
	}

}
