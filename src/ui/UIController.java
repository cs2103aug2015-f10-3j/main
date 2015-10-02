package ui;

import java.util.ArrayList;

import logic.data.Task;
import logic.Executor;
import util.Pair;

public class UIController {

	/*** Variables ***/
	private static final String ERROR_INCORRECT_FORMAT = "Invalid Format";
	private static final String ERROR_BAD_COMMAND = "Command fail to execute";

	/*** Constructor ***/
	public UIController(){

	}

	/*** Methods ***/

	/**
	 * This method returns an array of output from Logic Component, Executor class.
	 * 
	 * @param String 
	 *            Input string of the user
	 * @return String array
	 */
	public String[] processUserInput(String input){
		String[] output = null;
		Pair<ArrayList<Task>, Boolean> result = Executor.processCommand(input);
		ArrayList<Task> taskList = result.getFirst();
		Boolean confirmation = result.getSecond();

		if(taskList == null || result == null){
			output = new String[1];
			output[0] = ERROR_INCORRECT_FORMAT;
		}
		else if(!confirmation){
			output = new String[1];
			output[0] = ERROR_BAD_COMMAND;
		}
		else{
			output = new String[taskList.size()];
			for(int i = 0; i < taskList.size(); i++){
				Task selectedTask = taskList.get(i);
				output[i] = selectedTask.toString();
			}
		}
		return output;
	}
}
