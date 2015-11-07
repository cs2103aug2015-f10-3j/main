//@@author A0125473H
package main.paddletask.command.api;

import java.util.ArrayList;
import java.util.Stack;

import main.paddletask.common.exception.InvalidRedoException;
import main.paddletask.task.entity.Task;

public class RedoCommand extends Command {

	@Override
	public ArrayList<Task> execute() throws Exception {
		Stack<Command> undoCommandList = getUndoCommandList();
		assert(undoCommandList != null);
		if (isRedoable(undoCommandList)) {
			throw new InvalidRedoException("Unable to redo further");
		}
		Command previousCommand = getPreviousCommand(undoCommandList);
		//add previously undone command back into the list of done commands
		addTo(getCommandList(), previousCommand);
		return previousCommand.execute();
	}
	
	private void addTo(Stack<Command> commandList, Command command) {
		commandList.add(command);
	}
	
	private boolean isRedoable(Stack<Command> undoCommandList) {
		return undoCommandList.size() < 1;
	}
	
	private Command getPreviousCommand(Stack<Command> undoCommandList) {
		return undoCommandList.pop();
	}

	@Override
	public ArrayList<Task> undo() {
		return null;
	}

}
