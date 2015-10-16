package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import command.api.ViewTaskCommand;
import task.entity.Task;

public class ViewTaskCommandTest {
	@Test
	public void testViewByType() throws Exception{
		ViewTaskCommand view = new ViewTaskCommand();
		ArrayList<Task> allTask = new ArrayList<Task>();
		//view.execute();
		view.selectTaskByPeriod(allTask);
	}
}
