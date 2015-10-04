package input;

import java.util.ArrayList;

import logic.TaskController;
import logic.data.Task;
import util.Pair;

public class DeleteTaskCommand extends Command {

	@Override
	public Pair<ArrayList<Task>,Boolean> execute() {
		TaskController taskController = TaskController.getInstance();
		int taskId = getOption("delete").getIntegerValue();
		boolean deleteTaskRes = taskController.deleteTask(taskId);
		return new Pair<ArrayList<Task>, Boolean>(null, deleteTaskRes);
	}

}
