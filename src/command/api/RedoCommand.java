package command.api;

import java.util.ArrayList;

import common.exception.InvalidRedoException;
import task.entity.Task;

public class RedoCommand extends Command {

	@Override
	public ArrayList<Task> execute() throws Exception {
		ArrayList<Command> undoCommandList = getUndoCommandList();
		if (undoCommandList.size() < 1) {
			throw new InvalidRedoException("Unable to undo further");
		}
		Command previousCommand = undoCommandList.remove(undoCommandList.size() - 1);
		getCommandList().add(previousCommand);
		return previousCommand.execute();
	}

	@Override
	public ArrayList<Task> undo() {
		return null;
	}

}
