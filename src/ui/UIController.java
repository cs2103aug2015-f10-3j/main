package ui;

import java.util.ArrayList;

import logic.data.Task;
import logic.Logic;
import util.Pair;

public class UIController {
	
	private static final String ERROR_INCORRECT_FORMAT = "Invalid Format";
	private static final String ERROR_BAD_COMMAND = "Command fail to execute";
	public UIController(){

	}

	public String[] processUserInput(String input){
		String[] output = null;
		Pair<ArrayList<Task>, Boolean > result = Logic.processCommand(input);
		/*
		//Call logicApi to process input
		//Logic.processCommand(input)
		 * 
		 */
		ArrayList<Task> taskList = result.getFirst();
		Boolean confirmation = result.getSecond();
		
		if(taskList == null){
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
