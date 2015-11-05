//@@A0125473H
package main.paddletask.command.api;

import java.util.ArrayList;

import main.paddletask.common.exception.InvalidUndoException;
import main.paddletask.task.entity.Task;

public class UndoCommand extends Command {

	@Override
	public ArrayList<Task> execute() throws Exception {
		ArrayList<Command> commandList = getCommandList();
		if (commandList.size() < 1) {
			throw new InvalidUndoException("Unable to undo further");
		}
		Command previousCommand = commandList.remove(commandList.size() - 1);
		getUndoCommandList().add(previousCommand);
		return previousCommand.undo();
	}

	@Override
	public ArrayList<Task> undo() {
		return null;
	}
}
