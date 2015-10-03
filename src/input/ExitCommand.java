package input;

import java.util.ArrayList;

import logic.data.Task;
import util.Pair;

public class ExitCommand implements Command {

	@Override
	public Pair<ArrayList<Task>,Boolean> execute() {
		exitProgram();
		return null;
	}
	
	private void exitProgram() {
		System.exit(0);
	}
}
