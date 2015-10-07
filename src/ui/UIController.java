package ui;

import java.util.ArrayList;

import commons.Pair;
import logic.data.Task;
import logic.Executor;

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

		if(taskList == null){
			output = new String[1];
			output[0] = ERROR_INCORRECT_FORMAT;
		}
		else if(!confirmation){
			output = new String[1];
			output[0] = ERROR_BAD_COMMAND;
		}else{

			output = formatOutput(taskList);
			/*
			output = new String[taskList.size()];
			for(int i = 0; i < taskList.size(); i++){
				Task selectedTask = taskList.get(i);
				output[i] = selectedTask.toString();
			}*/
		}
		return output;
	}
	
	public String[] formatOutput(ArrayList<Task> taskList){
		String[] output = new String[taskList.size() + 1];
		String format = "%1$-5s %2$-20s %3$-60s %4$-11s %5$-6s";
		output[0] = String.format(format, "ID", "Type", "Description", "Deadline", "");
		for(int i = 0; i < taskList.size(); i++){
			Task selectedTask = taskList.get(i);
			String[] taskDetails = selectedTask.toString().split(" ");
			switch(taskDetails.length){
			case 3:
				output[i+1] = String.format(format, taskDetails[0],taskDetails[1], taskDetails[2], "", "");
				break;
			case 4:
				output[i+1] = String.format(format, taskDetails[0],taskDetails[1], taskDetails[2], taskDetails[3], "");
				break;
			case 5:
				output[i+1] = String.format(format, taskDetails[0],taskDetails[1], taskDetails[2], taskDetails[3], taskDetails[4]);
				break;
			}
		}
		return output;
	}
	
	
	
	/* Formatting with HTML
	public String formatOutput(ArrayList<Task> taskList){
		String output = "<html>\n<head></head>\n<body><table border=\"1\">\n"
				+ "<tr><th>ID</th><th>Type</th><th>Description</th><th>Deadline</th><th></th></tr>";
		
		for(Task task : taskList){
			output +="\n<tr>";
			String[] taskDetails = task.toString().split(" ");
			for(int i = 0; i < 5; i++){
				if(taskDetails.length > i){
					output+="<th>" + taskDetails[i] + "</th>";
				}else{
					output+="<th></th>";
				}
			}
			output += "</tr>";
		}
		output +="\n</table></body></html>";
		return output;
	}*/
}
