package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ui.controller.UIController;

public class TestViewCommand {
	 /*** Variables ***/
	private UIController uiController = null;
	
	/*** Setup and Teardown ***/
	@Before
	public void setUp() throws Exception {
		uiController = UIController.getInstance(null);
	}

	@After
	public void tearDown() throws Exception {
	}

    /*** Test Cases 
     * @throws Exception  ***/
	@Test
	public void testView() throws Exception {
		fail("Not yet implemented");
	}

}
