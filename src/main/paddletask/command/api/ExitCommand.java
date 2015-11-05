package main.paddletask.command.api;

import java.util.ArrayList;
import main.paddletask.task.entity.Task;

public class ExitCommand extends Command {

	@Override
	public ArrayList<Task> execute() {
		exitProgram();
		return null;
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	@Override
	public ArrayList<Task> undo() {
		return null;
	}
}
