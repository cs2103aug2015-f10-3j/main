package test.cases;

import static org.junit.Assert.assertArrayEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.paddletask.ui.controller.UIController;
import main.paddletask.ui.view.MainFrame;
import main.paddletask.ui.view.MainPanel;

public class FlowTest {
	UIController uiController;
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAdd() {

		String input = "add watch movie";
		String[] expected = {"New task added successfully\n","Description: watch movie", "Task Type: floating", "Priority: 3", "\n"};
		String[] actual = MainFrame.testDriver(input);
		System.out.println("add test");
		for(int i = 0; i < 5; i ++){
			//System.out.println("ex:" + expected[i]);
			System.out.println("ac:" + actual[i]);
		}
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testHelp(){
		String input = "help exit";
		String[] expected = {"EXIT COMMAND",
				"exit - Close and exit PaddleTask"};
		String[] actual = MainFrame.testDriver(input);
		assertArrayEquals(expected, actual);
	}

}
