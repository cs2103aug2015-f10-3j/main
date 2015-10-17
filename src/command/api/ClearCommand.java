package command.api;

import java.util.ArrayList;

import task.entity.Task;

public class ClearCommand extends Command {
	
	@Override
	public ArrayList<Task> execute() {
		setChanged();
		notifyObservers(null);
		return null;
	}

	@Override
	public ArrayList<Task> undo() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
