package logic.api;

import java.util.ArrayList;
import java.util.Observer;

import background.Reminder;
import task.entity.Task;

public class OutputProcessor {
	/*** Variables ***/
	private static OutputProcessor instance = null;
	private static LogicController executor;
	private static Observer observer;
	private static Reminder reminder;
	
	//private static final String ERROR_BAD_COMMAND = "Command fail to execute";
	//private static final String FORMAT = "| %1$-5s | %2$-10s | %3$-60s | %4$-11s | %5$-5s | %6$-11s | %7$-5s |";
	private static final String FORMAT = " %1$-5s  %2$-10s  %3$-60s  %4$-11s  %5$-8s  %6$-11s  %7$-5s ";
	private static final String VIEW_HEADER = String.format(FORMAT, "ID", "Type", "Description", "Start Date","","Deadline", "");
	private static final int OFFSET_ONE = 1;

	/*** Constructor ***/
	private OutputProcessor(){
		executor = LogicController.getInstance(observer);
		executor.addObserver(observer);
		reminder = Reminder.getInstance(observer);
		reminder.addObserver(observer);
	}
	
	/*** Methods ***/
	/**
	 * This method returns an instance of the OutputProcessor. 
	 * This is an implementation of Singleton Class.
	 * 
	 * @return OutputProcessor instance
	 */
	public static OutputProcessor getInstance(Observer observer){
		if(instance == null){
			OutputProcessor.observer = observer;
			instance = new OutputProcessor();
		}
		
		return instance;
	}
	
	/**
	 * This method returns an array of output from LogicController class.
	 * 
	 * @param String 
	 *            Input string of the user
	 * @return String array
	 */
	public String[] processUserInput(String input){
		String[] output = new String[0];
		ArrayList<Task> taskList = executor.processCommand(input);

		if(taskList != null){
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
			String[] taskDetails = selectedTask.toDetailsArray();
			int detailsPointer = 0;
			output[i+OFFSET_ONE] = String.format(FORMAT, i+1,taskDetails[++detailsPointer], 
								   taskDetails[++detailsPointer].length() > 60 ? 
								   taskDetails[detailsPointer].substring(0, 57) + "..." : taskDetails[detailsPointer]
								   , taskDetails[++detailsPointer], taskDetails[++detailsPointer],
								   taskDetails[++detailsPointer],taskDetails[++detailsPointer]);
		}
		return output;
	}
}
