//@@author A0125473H
package main.paddletask.command.api;

import java.util.ArrayList;
import java.util.Stack;

import main.paddletask.common.exception.InvalidUndoException;
import main.paddletask.task.entity.Task;

public class UndoCommand extends Command {

	@Override
	public ArrayList<Task> execute() throws Exception {
		Stack<Command> commandList = getCommandList();
		assert(commandList != null);
		if (isUndoable(commandList)) {
			throw new InvalidUndoException("Unable to undo further");
		}
		Command previousCommand = getPreviousCommand(commandList);
		//add previously done command into the list of undone commands
		addTo(getUndoCommandList(), previousCommand);
		return previousCommand.undo();
	}

	private void addTo(Stack<Command> commandList, Command command) {
		commandList.add(command);
	}

	private boolean isUndoable(Stack<Command> commandList) {
		return commandList.size() < 1;
	}

	private Command getPreviousCommand(Stack<Command> commandList) {
		return commandList.pop();
	}

	@Override
	public ArrayList<Task> undo() {
		return null;
	}
}
