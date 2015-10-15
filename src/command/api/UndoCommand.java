package command.api;

import java.util.ArrayList;

import common.data.Pair;
import task.entity.Task;

public class UndoCommand extends Command {

	@Override
	public Pair<ArrayList<Task>, Boolean> execute() {
		ArrayList<Command> commandList = getCommandList();
		Command previousCommand = commandList.remove(commandList.size() - 1);
		getUndoCommandList().add(previousCommand);
		return previousCommand.undo();
	}

	@Override
	public Pair<ArrayList<Task>, Boolean> undo() {
		return null;
	}
}
