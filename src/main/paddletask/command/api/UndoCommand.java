//@@author A0125473H
package main.paddletask.command.api;

import java.util.ArrayList;

import main.paddletask.common.exception.InvalidUndoException;
import main.paddletask.task.entity.Task;

public class UndoCommand extends Command {

	@Override
	public ArrayList<Task> execute() throws Exception {
		ArrayList<Command> commandList = getCommandList();
		assert(commandList != null);
		if (isUndoable(commandList)) {
			throw new InvalidUndoException("Unable to undo further");
		}
		Command previousCommand = getPreviousCommand(commandList);
		//add previously done command into the list of undone commands
		addTo(getUndoCommandList(), previousCommand);
		return previousCommand.undo();
	}

	private void addTo(ArrayList<Command> commandList, Command command) {
		getCommandList().add(command);
	}

	private boolean isUndoable(ArrayList<Command> commandList) {
		return commandList.size() < 1;
	}

	private Command getPreviousCommand(ArrayList<Command> commandList) {
		return commandList.remove(commandList.size() - 1);
	}

	@Override
	public ArrayList<Task> undo() {
		return null;
	}
}
