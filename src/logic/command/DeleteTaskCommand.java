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
		Task deletedTask = taskController.getTask(taskId);
		boolean deleteTaskRes = taskController.deleteTask(taskId);
		ArrayList<Task> taskList = new ArrayList<Task>();
		taskList.add(deletedTask);
		return new Pair<ArrayList<Task>, Boolean>(taskList, deleteTaskRes);
	}

}
