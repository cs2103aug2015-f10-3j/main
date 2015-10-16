package command.api;

import java.util.ArrayList;

import common.data.Pair;
import task.entity.Task;
import ui.view.Observer;

public class ClearCommand extends Command {

	private Observer panel;
	
	public ClearCommand(Observer panel) {
		super();
		this.panel = panel;
	}
	
	@Override
	public Pair<ArrayList<Task>, Boolean> execute() {
		panel.clear();
		return null;
	}

	@Override
	public Pair<ArrayList<Task>, Boolean> undo() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
