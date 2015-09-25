package input;

import java.util.ArrayList;

import logic.data.Task;

public class ExitCommand implements Command {

	@Override
	public ArrayList<Task> execute() {
		exitProgram();
		return null;
	}
	
	private void exitProgram() {
		System.exit(0);
	}
}
