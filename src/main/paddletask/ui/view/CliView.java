//@@author A0125528E
package main.paddletask.ui.view;

import java.util.ArrayList;
import java.util.Scanner;

import main.paddletask.common.util.DateTimeHelper;
import main.paddletask.task.entity.Task;
import main.paddletask.ui.controller.UIController;

public class CliView {

	private static final String WELCOME_MSG_1 = "Welcome to PaddleTask.";
	private static final String WELCOME_MSG_2 = "Today is %s.";
	private static final String WELCOME_MSG_3 = "Your upcoming tasks for today:";
	private static final String FIRST_COMMAND = "view all today";
	private static final String REMINDER_MSG = "Reminder Alert!";
	private static final char PRIORITY_INDICATOR = '*';
	private static final char BOLD_INDICATOR = '@';
	private static final int WELCOME_MSG_SIZE = 3;
	private static final int REMOVE_ONE = 1;
	private static final int CHARACTER_LOCATION = 0;
	private static final int OFFSET_ZERO = 0;
	private static UIController uiController;
	
	private static Scanner sc = new Scanner(System.in);
	
	public CliView(UIController uiController) {
		CliView.uiController = uiController;
		cliMode();
		UIController.setUIModeEnabled(false);
	}
	
	/**
	 * This method is to initiate the command line mode
	 * of PaddleTask. This will take in input from the user
	 * to be processed.
	 * 
	 */
	public void cliMode(){
		prepareWelcome();
		while(sc.hasNext()){
			String command = sc.nextLine();
			String[] output = uiController.processUserInput(command);
			if(output!= null){
				outputToCmd(output);
			}

		}
	}

	/**
	 * This method is process the output and
	 * display it onto the command line.
	 * 
	 * @param output 
	 * 					String array to be displayed
	 * 
	 */
	public void outputToCmd(String[] output){
		System.out.println();
		for(String s : output){
			if(s!=null){
				while(s.charAt(CHARACTER_LOCATION) == BOLD_INDICATOR || 
						s.charAt(CHARACTER_LOCATION) == PRIORITY_INDICATOR ){
					s = s.substring(REMOVE_ONE);
				}
				System.out.println(s);
			}
		}
		System.out.println();
	}

	/**
	 * This method is to prepare the welcome message of 
	 * PaddleTask. It will also send a "view all today"
	 * command that will be used to display user's task today
	 * on initialization of PaddleTask.
	 * 
	 */
	public void prepareWelcome(){
		String today = DateTimeHelper.getDate(DateTimeHelper.now());
		String[] outputs = new String[WELCOME_MSG_SIZE];
		int counter = OFFSET_ZERO;
		outputs[counter++] = WELCOME_MSG_1;
		outputs[counter++] = String.format(WELCOME_MSG_2, today);
		outputs[counter++] = WELCOME_MSG_3;
		String[] output = uiController.processUserInput(FIRST_COMMAND);
		outputToCmd(outputs);
		outputToCmd(output);
	}

	/**
	 * This method is will create Reminder on the command line mode and
	 * display the output of the arraylist of tasks to the Command Prompt.
	 * 
	 * @param taskList
	 * 				arraylist of tasks
	 * 
	 */
	public void createReminder(ArrayList<Task> taskList) {
		String[] output = uiController.format((ArrayList<Task>)taskList);
		System.out.println(REMINDER_MSG);
		outputToCmd(output);
	}
}
