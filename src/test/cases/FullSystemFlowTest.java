package test.cases;

import static org.junit.Assert.assertArrayEquals;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import main.paddletask.storage.api.StorageController;
import main.paddletask.ui.controller.UIController;
import main.paddletask.ui.view.MainFrame;

public class FullSystemFlowTest {
    StorageController sController;
    protected static String fileName;
    byte[] backedUpContent = null;
    
	@Before
	public void setUp() throws Exception {
        sController = StorageController.getInstance();
        fileName = StorageController.DEFAULT_FILE;
        backUpData();
	}

	@After
	public void tearDown() throws Exception {
		 restoreData();
	}

	@Test
	public void testAddFloating() {
		String input = "add watch movie";
		String[] expected = {"New task added successfully\n","Description: watch movie", "Task Type: floating", "Priority: 3", "\n"};
		String[] actual = MainFrame.testDriver(input);
		System.out.println("add test");
		assertArrayEquals(expected, actual);
	}
	
	@Test
	public void testAddDeadline() {
		String input = "add watch movie by 10/12/2014 23:59";
		String[] expected = {"New task added successfully\n","Description: watch movie", 
				"Task Type: deadline", "Priority: 3", "Deadline: 10/12/2014 23:59",
				"Reminder: 10/12/2014 23:54","\n"};
		String[] actual = MainFrame.testDriver(input);

		assertArrayEquals(expected, actual);
	}
	
	/*@Test
	public void testEdit() {
		String[] input = {"view all", "edit 1 priority 1"};
		String[] expected = {"Selected task edit successfully\n","Description: watch movie", 
				"Task Type: deadline", "Priority: 1", "Deadline: 10/12/2014 23:59",
				"Reminder: 10/12/2014 23:54","\n"};
		String[] actual = MainFrame.testDriver(input);

		assertArrayEquals(expected, actual);
	}*/
	
	//@@author A0126332R-reused
    public void backUpData() {
        File file = new File(fileName);
        if (file.exists()) {
            backedUpContent = sController.getFileInBytes(fileName);
        }
    }
    
    public void restoreData() {
        if (backedUpContent != null) {
            sController.writeBytesToFile(fileName, backedUpContent, false);
        }
    }

}
