/*
 * Author : Tan Qiu Hao, Joel
 * Date	  : 3/10/2015
 */
package command.api;

import java.util.ArrayList;
import java.util.HashMap;
import command.data.Option;
import task.entity.Task;

public abstract class Command {
	
	private static ArrayList<Command> commandList;
	private static ArrayList<Command> undoCommandList;
	private HashMap<String, Option> options;
	
	public Command() {
		if (commandList == null || undoCommandList == null) {
			commandList = new ArrayList<Command>();
			undoCommandList = new ArrayList<Command>();
		}
		options = new HashMap<String, Option>();
	}
	
	public boolean hasOption(String optionName) {
		return options.containsKey(optionName);
	}
	
	public Option getOption(String optionName) {
		if (hasOption(optionName)) {
			return options.get(optionName);
		} else {
			return null;
		}
	}
	
	public Option[] getOptions() {
		return options.values().toArray(new Option[options.size()]);
	}
	
	public boolean addOption(String optionName, Option option) {
		if (options.put(optionName, option) != null) {
			return false;
		} else {
			return true;
		}
	}
	
	public void setOption(HashMap<String, Option> options) {
		this.options = options;
	}
	
	public static ArrayList<Command> getCommandList() {
		return commandList;
	}
	
	public static void setCommandList(ArrayList<Command> commandList) {
		Command.commandList = commandList;
	}
	
	public static ArrayList<Command> getUndoCommandList() {
		return undoCommandList;
	}
	
	public static void setUndoCommandList(ArrayList<Command> undoCommandList) {
		Command.undoCommandList = undoCommandList;
	}
	
	public abstract ArrayList<Task> execute() throws Exception;
	
	public abstract ArrayList<Task> undo() throws Exception;
}
