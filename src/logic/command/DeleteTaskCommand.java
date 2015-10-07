package logic.command;

import java.util.ArrayList;

import commons.Pair;
import logic.TaskController;
import logic.data.Task;

public class DeleteTaskCommand extends Command {

	@Override
	public Pair<ArrayList<Task>,Boolean> execute() {
		TaskController taskController = TaskController.getInstance();
		int taskId = getOption("delete").getIntegerValue();
		boolean deleteTaskRes = taskController.deleteTask(taskId);
		return new Pair<ArrayList<Task>, Boolean>(null, deleteTaskRes);
	}

}
