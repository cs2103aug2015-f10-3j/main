//@@author A0125473H
package main.paddletask.command.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import main.paddletask.command.data.Option;
import main.paddletask.task.entity.Task;

public abstract class Command extends Observable {

	/*** Variables ***/
	private static ArrayList<Command> _commandList;
	private static ArrayList<Command> _undoCommandList;
	private HashMap<String, Option> _options = new HashMap<String, Option>();

	/*** Constructor ***/
	public Command() {
		createCommandHistories();
		assert(_options != null);
	}

	private void createCommandHistories() {
		if (_commandList == null || _undoCommandList == null) {
			_commandList = new ArrayList<Command>();
			_undoCommandList = new ArrayList<Command>();
		}
	}

	/*** Methods mainly for Option manipulation ***/
	/**
	 * This method is invoked to check if a specific option exists
	 * for the command
	 * 
	 * @return <code>true</code> if option exists
	 * 		   <code>false</code> otherwise
	 */
	public boolean hasOption(String optionName) {
		return _options.containsKey(optionName);
	}

	/**
	 * This method is invoked to get a specific option if it exists
	 * for the command
	 * 
	 * @return <code>Option</code> if option exists
	 * 		   <code>NULL</code> otherwise
	 */
	public Option getOption(String optionName) {
		if (hasOption(optionName)) {
			return _options.get(optionName);
		} else {
			return null;
		}
	}

	/**
	 * This method is invoked to add a specific option 
	 * for the command
	 * 
	 * @return <code>true</code> if option was successfully added
	 * 		   <code>false</code> otherwise
	 */
	public boolean addOption(String optionName, Option option) {
		if (_options.put(optionName, option) != null) {
			return false;
		} else {
			return true;
		}
	}

	/*** Accessor methods ***/
	public Option[] getOptions() {
		return _options.values().toArray(new Option[_options.size()]);
	}

	public void setOption(HashMap<String, Option> options) {
		this._options = options;
	}
	public static ArrayList<Command> getCommandList() {
		return _commandList;
	}

	public static void setCommandList(ArrayList<Command> commandList) {
		Command._commandList = commandList;
	}

	public static ArrayList<Command> getUndoCommandList() {
		return _undoCommandList;
	}

	public static void setUndoCommandList(ArrayList<Command> undoCommandList) {
		Command._undoCommandList = undoCommandList;
	}

	//All subclasses are forced to implement execute and undo
	public abstract ArrayList<Task> execute() throws Exception;

	public abstract ArrayList<Task> undo() throws Exception;
}
