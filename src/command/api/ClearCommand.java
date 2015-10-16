package command.api;

import java.util.ArrayList;

import task.entity.Task;
import ui.view.Observer;

public class ClearCommand extends Command {

	private Observer panel;
	
	public ClearCommand(Observer panel) {
		super();
		this.panel = panel;
	}
	
	@Override
	public ArrayList<Task> execute() {
		panel.clear();
		return null;
	}

	@Override
	public ArrayList<Task> undo() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
