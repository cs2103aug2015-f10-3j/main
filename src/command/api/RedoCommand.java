package command.api;

import java.util.ArrayList;
import task.entity.Task;

public class RedoCommand extends Command {

	@Override
	public ArrayList<Task> execute() throws Exception {
		ArrayList<Command> undoCommandList = getUndoCommandList();
		Command previousCommand = undoCommandList.remove(undoCommandList.size() - 1);
		getCommandList().add(previousCommand);
		return previousCommand.execute();
	}

	@Override
	public ArrayList<Task> undo() {
		return null;
	}

}
