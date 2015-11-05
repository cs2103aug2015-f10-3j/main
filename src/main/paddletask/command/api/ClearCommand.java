//@@A0125473H
package main.paddletask.command.api;

import java.util.ArrayList;

import main.paddletask.task.entity.Task;

public class ClearCommand extends Command {

	/*** Methods ***/
	@Override
	public ArrayList<Task> execute() {
		clearScreen();
		return null;
	}
	
	public void clearScreen() {
		//set observable state as changed
		setChanged();
		//notify the observer - screen
		notifyObservers(null);
	}

	//@@author generated
	@Override
	public ArrayList<Task> undo() {
		return null;
	}
	
}
