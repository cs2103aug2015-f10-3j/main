package command.api;

import java.util.ArrayList;

import common.data.Pair;
import task.entity.Task;

public class RedoCommand extends Command {

	@Override
	public Pair<ArrayList<Task>, Boolean> execute() {
		ArrayList<Command> undoCommandList = getUndoCommandList();
		Command previousCommand = undoCommandList.remove(undoCommandList.size() - 1);
		getCommandList().add(previousCommand);
		return previousCommand.execute();
	}

	@Override
	public Pair<ArrayList<Task>, Boolean> undo() {
		// TODO Auto-generated method stub
		return null;
	}

}
