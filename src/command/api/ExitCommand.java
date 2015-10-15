package command.api;

import java.util.ArrayList;

import common.data.Pair;
import task.entity.Task;

public class ExitCommand extends Command {

	@Override
	public Pair<ArrayList<Task>,Boolean> execute() {
		exitProgram();
		return null;
	}
	
	private void exitProgram() {
		System.exit(0);
	}

	@Override
	public Pair<ArrayList<Task>, Boolean> undo() {
		// TODO Auto-generated method stub
		return null;
	}
}
