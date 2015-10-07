package logic.command;

import java.util.ArrayList;

import commons.Pair;
import logic.data.Task;

public class ExitCommand extends Command {

	@Override
	public Pair<ArrayList<Task>,Boolean> execute() {
		exitProgram();
		return null;
	}
	
	private void exitProgram() {
		System.exit(0);
	}
}
