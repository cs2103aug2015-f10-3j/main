package test.cases;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.Test;
import main.paddletask.command.api.ViewTaskCommand;
import main.paddletask.task.entity.Task;

public class ViewTaskCommandTest {
	@Test
	public void testViewByType() throws Exception{
		ViewTaskCommand view = new ViewTaskCommand();
		@SuppressWarnings("unused")
		ArrayList<Task> allTask = new ArrayList<Task>();
		assertNotNull(view.execute());
		//view.selectTaskByPeriod(allTask);
	}
}
