package command.api;

import java.util.ArrayList;
import task.entity.Task;

public class UndoCommand extends Command {

	@Override
	public ArrayList<Task> execute() throws Exception {
		ArrayList<Command> commandList = getCommandList();
		Command previousCommand = commandList.remove(commandList.size() - 1);
		getUndoCommandList().add(previousCommand);
		return previousCommand.undo();
	}

	@Override
	public ArrayList<Task> undo() {
		return null;
	}
}
