package ui;

import java.util.ArrayList;

import commons.Pair;
import logic.data.Task;
import logic.Executor;

public class UIController {

	/*** Variables ***/
	private static final String ERROR_INCORRECT_FORMAT = "Invalid Format";
	private static final String ERROR_BAD_COMMAND = "Command fail to execute";
	private static final String FORMAT = "%1$-5s %2$-10s %3$-60s %4$-11s %5$-5s";
	private static final String VIEW_HEADER = String.format(FORMAT, "ID", "Type", "Description", "Deadline", "");
	private static final String STRING_EMPTY = "";
	private static final int OFFSET_ONE = 1;
	
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
			output = new String[OFFSET_ONE];
			output[0] = ERROR_INCORRECT_FORMAT;
		}
		else if(!confirmation){
			output = new String[OFFSET_ONE];
			output[0] = ERROR_BAD_COMMAND;
		}else{
			output = formatOutput(taskList);
		}
		return output;
	}
	
	/**
	 * This method formats the variable needed from each task to string
	 * and store them into a string array.
	 * 
	 * @param ArrayList 
	 *            Array list of task
	 * @return String array
	 */
	public String[] formatOutput(ArrayList<Task> taskList){
		String[] output = new String[taskList.size() + OFFSET_ONE];

		output[0] = VIEW_HEADER;
		for(int i = 0; i < taskList.size(); i++){
			Task selectedTask = taskList.get(i);
			String[] taskDetails = selectedTask.toString().split(" ");
			int detailsPointer = 0;
			switch(taskDetails.length){
			case 3:
				output[i+OFFSET_ONE] = String.format(FORMAT, taskDetails[detailsPointer],taskDetails[++detailsPointer], 
									   taskDetails[++detailsPointer], STRING_EMPTY, STRING_EMPTY);
				break;
			case 4:
				output[i+OFFSET_ONE] = String.format(FORMAT, taskDetails[detailsPointer],taskDetails[++detailsPointer], 
									   taskDetails[++detailsPointer], taskDetails[++detailsPointer], STRING_EMPTY);
				break;
			case 5:
				output[i+OFFSET_ONE] = String.format(FORMAT, taskDetails[detailsPointer],taskDetails[++detailsPointer], 
									   taskDetails[++detailsPointer], taskDetails[++detailsPointer], taskDetails[++detailsPointer]);
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
